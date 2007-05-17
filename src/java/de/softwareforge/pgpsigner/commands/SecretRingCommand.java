package de.softwareforge.pgpsigner.commands;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.FileNameCompletor;
import jline.NullCompletor;
import jline.SimpleCompletor;

import org.apache.commons.cli.Option;

public class SecretRingCommand extends AbstractCommand implements Command
{

    public SecretRingCommand()
    {
    }

    public String getName()
    {
        return "secretring";
    }

    public String getHelp()
    {
        return "sets the secret key ring";
    }

    public Option getCommandLineOption()
    {
        Option option = super.getCommandLineOption();
        option.setArgs(1);
        option.setArgName("file");
        return option;
    }

    public Completor getCompletor()
    {
        return new ArgumentCompletor(
                new Completor[] { new SimpleCompletor(getName()), new FileNameCompletor(), new NullCompletor() });
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Current secret key ring is "
                    + (getContext().getSecretRing() == null ? "<unset>" : getContext().getSecretRing().getRingFileName()));
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        String ringFile = args[1];

        System.out.print("Loading key ring " + ringFile + "...");

        try
        {
            getContext().getSecretRing().load(ringFile);
            System.out.println("...ok! " + getContext().getSecretRing().size() + " Keys loaded");

        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            System.out.println("... failed! Could not read keyRing " + ringFile + ": " + e.getMessage());
        }
        finally
        {
            getContext().updatePartyRing(false);
        }
    }
}
