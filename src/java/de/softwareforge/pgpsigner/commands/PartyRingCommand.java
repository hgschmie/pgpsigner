package de.softwareforge.pgpsigner.commands;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.FileNameCompletor;
import jline.NullCompletor;
import jline.SimpleCompletor;

import org.apache.commons.cli.Option;

public class PartyRingCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "partyring";
    }

    public String getHelp()
    {
        return "sets the party key ring";
    }

    @Override
    public Option getCommandLineOption()
    {
        Option option = super.getCommandLineOption();
        option.setArgs(1);
        option.setArgName("file");
        return option;
    }

    @Override
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
            System.out.println("Current party key ring is "
                    + (getContext().getPartyRing() == null ? "<unset>" : getContext().getPartyRing().getRingFileName()));
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
            getContext().getPartyRing().load(ringFile);
            System.out.println("...ok! " + getContext().getPartyRing().size() + " Keys loaded");

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
            getContext().updatePartyRing(true);
        }
    }
}
