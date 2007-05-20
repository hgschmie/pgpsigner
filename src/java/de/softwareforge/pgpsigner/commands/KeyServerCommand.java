package de.softwareforge.pgpsigner.commands;

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.key.PublicKey;
import de.softwareforge.pgpsigner.util.DisplayHelpers;

public class KeyServerCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "keyserver";
    }

    public String getHelp()
    {
        return "sets the keyserver for uploading keys";
    }

    @Override
    public Option getCommandLineOption()
    {
        Option option = super.getCommandLineOption();
        option.setArgs(1);
        option.setArgName("hostname");
        return option;
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Current key server is " + DisplayHelpers.showNullValue(getContext().getKeyServer()));
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        getContext().setKeyServer(args[1]);

        for (PublicKey key : getContext().getPartyRing().getVisibleKeys().values())
        {
            key.setUploaded(false);
        }
    }
}
