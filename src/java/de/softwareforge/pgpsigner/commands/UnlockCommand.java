package de.softwareforge.pgpsigner.commands;

import java.io.IOException;
import java.security.NoSuchProviderException;

import jline.ConsoleReader;

import org.apache.commons.cli.Option;
import org.bouncycastle.openpgp.PGPException;

import de.softwareforge.pgpsigner.key.SecretKey;
import de.softwareforge.pgpsigner.util.DisplayHelpers;

public class UnlockCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "unlock";
    }

    public String getHelp()
    {
        return "unlock the current sign key.";
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

        if (getContext().getSignKey().isUnlocked())
        {
            System.out.println("Sign key is already unlocked!");
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {

        SecretKey signKey = getContext().getSignKey();

        try
        {
            ConsoleReader reader = new ConsoleReader();
            reader.setUseHistory(false);
            reader.setBellEnabled(false);
            reader.setDefaultPrompt("Passphrase for " + signKey.getKeyId() + "> ");
            String passPhrase = reader.readLine('*');

            signKey.unlock(passPhrase);

            System.out.println("Private key " + DisplayHelpers.showKey(signKey) + " unlocked!");
        }
        catch (IOException ioe)
        {
            System.out.println("Could not read pass phrase: " + ioe.getMessage());
            return;
        }
        catch (PGPException pgpe)
        {
            System.out.println("Could not unlock private key.");
            return;
        }
        catch (NoSuchProviderException nspe)
        {
            System.out.println("Could not decrypt private key.");
            return;
        }
    }
}
