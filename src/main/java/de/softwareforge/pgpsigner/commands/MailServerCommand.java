package de.softwareforge.pgpsigner.commands;

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.util.DisplayHelpers;

public class MailServerCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "mailserver";
    }

    public String getHelp()
    {
        return "sets the mailserver for mailing keys";
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
            System.out.println("Current mail server is " + DisplayHelpers.showNullValue(getContext().getMailServer()));
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        getContext().setMailServer(args[1]);
    }
}
