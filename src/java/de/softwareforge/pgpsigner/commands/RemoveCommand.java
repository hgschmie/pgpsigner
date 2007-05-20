package de.softwareforge.pgpsigner.commands;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.NullCompletor;
import jline.SimpleCompletor;

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.jline.PartyKeyCompletor;
import de.softwareforge.pgpsigner.key.KeyId;

/**
 * The "remove" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class RemoveCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "remove";
    }

    public String getHelp()
    {
        return "remove a key from the party list";
    }

    @Override
    public Option getCommandLineOption()
    {
        return null;
    }

    @Override
    public Completor getCompletor()
    {
        return new ArgumentCompletor(new Completor[] { new SimpleCompletor(getName()), new PartyKeyCompletor(getContext()),
                new NullCompletor() });
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Missing argument for " + args[0]);
            return false;
        }

        if (!getContext().getPartyRing().getVisibleKeys().containsId(new KeyId(args[1])))
        {
            System.out.println("Key " + args[1] + " is not on the party key ring!");
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        getContext().getPartyRing().setVisible(new KeyId(args[1]), false);
    }
}
