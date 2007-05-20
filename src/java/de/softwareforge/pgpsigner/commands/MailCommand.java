package de.softwareforge.pgpsigner.commands;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import org.apache.commons.cli.Option;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import de.softwareforge.pgpsigner.key.PublicKey;
import de.softwareforge.pgpsigner.key.SecretKey;

public class MailCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "mail";
    }

    public String getHelp()
    {
        return "mail signed keys to their owners";
    }

    @Override
    public Option getCommandLineOption()
    {
        return null;
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        if (getContext().getSignKey() == null)
        {
            System.out.println("Sign key must be selected for sender mail address!");
            return false;
        }

        if (StringUtils.isEmpty(getContext().getMailServer()))
        {
            System.out.println("Mail server must be set!");
            return false;
        }

        if (StringUtils.isEmpty(getContext().getSignEvent()))
        {
            System.out.println("Sign event name must be set!");
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {

        SecretKey secretKey = getContext().getSignKey();

        String senderMail = secretKey.getMailAddress();
        String senderName = secretKey.getName();

        if (StringUtils.isEmpty(senderMail))
        {
            System.out.println("Could not extract sender mail from sign key.");
            return;
        }

        for (final PublicKey key : getContext().getPartyRing().getVisibleKeys().values())
        {

            if (key.isSigned() && !key.isMailed())
            {

                try
                {
                    String recipient = getContext().isSimulation() ? senderMail : key.getMailAddress();

                    if (StringUtils.isEmpty(recipient))
                    {
                        System.out.println("No mail address for key " + key.getKeyId() + ", skipping.");
                        continue;
                    }

                    System.out.println("Sending Key " + key.getKeyId() + " to " + recipient);

                    MultiPartEmail mail = new MultiPartEmail();
                    mail.setHostName(getContext().getMailServer());
                    mail.setFrom(senderMail, senderName);
                    mail.addTo(recipient);

                    if (!getContext().isSimulation())
                    {
                        mail.addBcc(senderMail);
                    }

                    mail.setSubject("Your signed PGP key - " + key.getKeyId());
                    mail.setMsg("This is your signed PGP key " + key.getKeyId() + " from the " + getContext().getSignEvent()
                            + " key signing event.");

                    final String name = key.getKeyId() + ".asc";

                    mail.attach(new DataSource()
                    {

                        public String getContentType()
                        {
                            return "application/pgp-keys";
                        }

                        public InputStream getInputStream() throws IOException
                        {
                            return new ByteArrayInputStream(key.getArmor());
                        }

                        public String getName()
                        {
                            return name;
                        }

                        public OutputStream getOutputStream() throws IOException
                        {
                            throw new UnsupportedOperationException();
                        }
                    }, name, "Signed Key " + key.getKeyId());

                    mail.send();
                    key.setMailed(true);

                }
                catch (EmailException ee)
                {
                    System.out.println("Could not send mail for Key " + key.getKeyId());
                }
            }
        }
    }
}
