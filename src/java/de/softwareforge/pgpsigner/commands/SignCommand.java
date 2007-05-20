package de.softwareforge.pgpsigner.commands;

import java.util.Iterator;

import org.apache.commons.cli.Option;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;

import de.softwareforge.pgpsigner.key.PublicKey;
import de.softwareforge.pgpsigner.key.SecretKey;
import de.softwareforge.pgpsigner.util.DisplayHelpers;

/**
 * The "sign" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class SignCommand extends AbstractCommand implements Command
{

    public SignCommand()
    {
    }

    public String getName()
    {
        return "sign";
    }

    public String getHelp()
    {
        return "sign keys on the party key ring";
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
            System.out.println("No sign key has been selected!");
            return false;
        }

        if (!getContext().getSignKey().isUnlocked())
        {
            System.out.println("Sign key must be unlocked before signing!");
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {

        PGPSignatureGenerator signatureGenerator = null;

        SecretKey signKey = getContext().getSignKey();
        PGPPublicKey pubKey = signKey.getPGPPublicKey();

        try
        {
            signatureGenerator = new PGPSignatureGenerator(pubKey.getAlgorithm(), PGPUtil.SHA1, "BC");
            signatureGenerator.initSign(PGPSignature.DEFAULT_CERTIFICATION, signKey.getPGPPrivateKey());

            PGPSignatureSubpacketGenerator subpacketGenerator = new PGPSignatureSubpacketGenerator();
            for (Iterator it = pubKey.getUserIDs(); it.hasNext();)
            {
                subpacketGenerator.setSignerUserID(false, (String) it.next());
                signatureGenerator.setHashedSubpackets(subpacketGenerator.generate());
            }
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            System.out.println("Could not generate signature for signing.");
            return;
        }

        for (PublicKey key : getContext().getPartyRing().getVisibleKeys().values())
        {

            if (!key.isSigned())
            {
                try
                {
                    PGPPublicKey newKey = key.getPGPPublicKey();
                    PGPSignature signature = signatureGenerator.generateCertification(newKey);

                    for (Iterator it = key.getUserIds(); it.hasNext();)
                    {
                        String userId = (String) it.next();
                        newKey = PGPPublicKey.addCertification(newKey, userId, signature);
                    }

                    key.setPGPPublicKey(newKey);
                    key.setSigned(true);
                    System.out.println("Signed Key " + key.getKeyId() + " with " + signKey.getKeyId());

                }
                catch (RuntimeException re)
                {
                    throw re;
                }
                catch (Exception e)
                {
                    System.out.println("Could not sign key " + DisplayHelpers.showKey(key) + ", skipping.");
                }
            }
        }
    }
}
