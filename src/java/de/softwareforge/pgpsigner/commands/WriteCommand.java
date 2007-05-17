package de.softwareforge.pgpsigner.commands;

import java.io.IOException;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.NullCompletor;
import jline.SimpleCompletor;

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.jline.PartyKeyCompletor;
import de.softwareforge.pgpsigner.key.KeyId;
import de.softwareforge.pgpsigner.key.PublicKey;
import de.softwareforge.pgpsigner.util.DisplayHelpers;

public class WriteCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "write";
    }

    public String getHelp()
    {
        return "write an ASCII-armored key from the party list";
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

        PublicKey key = getContext().getPartyRing().getVisibleKeys().getKey(new KeyId(args[1]));

        try
        {
            String armoredKey = new String(key.getArmor(), "ISO-8859-1");
            System.out.println(armoredKey);
        }
        catch (IOException ioe)
        {
            System.out.println("Could not write key " + DisplayHelpers.showKey(key));
        }
    }
}
