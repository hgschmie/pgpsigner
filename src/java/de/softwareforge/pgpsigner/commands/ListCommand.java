package de.softwareforge.pgpsigner.commands;

import java.util.Iterator;

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.key.PublicKey;
import de.softwareforge.pgpsigner.key.PublicKeyRing;

public class ListCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "list";
    }

    public String getHelp()
    {
        return "list the keys available for signing";
    }

    @Override
    public Option getCommandLineOption()
    {
        return null;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        PublicKeyRing partyRing = getContext().getPartyRing().getVisibleKeys();

        int count = 0;
        for (PublicKey key : partyRing.values())
        {
            System.out.print(++count + " | " + key.getKeyId() + " | ");

            System.out.print(key.isSigned() ? "S" : " ");
            System.out.print(key.isMailed() ? "M" : " ");
            System.out.print(key.isUploaded() ? "U" : " ");

            Iterator it = key.getUserIds();
            if (it.hasNext())
            {
                System.out.println(" | " + it.next());
            }
        }
    }
}
