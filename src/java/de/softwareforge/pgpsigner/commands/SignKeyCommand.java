package de.softwareforge.pgpsigner.commands;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.NullCompletor;
import jline.SimpleCompletor;

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.jline.SecretKeyCompletor;
import de.softwareforge.pgpsigner.key.KeyId;
import de.softwareforge.pgpsigner.key.SecretKey;
import de.softwareforge.pgpsigner.util.DisplayHelpers;

public class SignKeyCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "signkey";
    }

    public String getHelp()
    {
        return "sets the signing key";
    }

    @Override
    public Option getCommandLineOption()
    {
        Option option = super.getCommandLineOption();
        option.setArgs(1);
        option.setArgName("key");
        return option;
    }

    @Override
    public Completor getCompletor()
    {
        return new ArgumentCompletor(new Completor[] { new SimpleCompletor(getName()), new SecretKeyCompletor(getContext()),
                new NullCompletor() });
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Current sign key is " + DisplayHelpers.showKey(getContext().getSignKey()));
            return false;
        }

        if (getContext().getSecretRing() == null)
        {
            System.out.println("Secret key ring has not yet been set, can not set sign key.");
            return false;
        }

        if (!getContext().getSecretRing().containsId(new KeyId(args[1])))
        {
            System.out.println("Sign key " + args[1] + " is not on secret keyring, sign key unchanged.");
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        SecretKey secretKey = getContext().getSecretRing().getKey(new KeyId(args[1]));
        getContext().setSignKey(secretKey);
        System.out.println("Sign Key is now " + DisplayHelpers.showKey(secretKey));
    }
}
