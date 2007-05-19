package de.softwareforge.pgpsigner.commands;

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.PGPSigner;

public class QuitCommand extends AbstractCommand implements Command
{

    public QuitCommand()
    {
    }

    public String getName()
    {
        return "quit";
    }

    public String getHelp()
    {
        return "quit " + PGPSigner.APPLICATION_NAME;
    }

    @Override
    public Option getCommandLineOption()
    {
        return null;
    }

    public void executeInteractiveCommand(final String[] args)
    {
        getContext().setShutdown(true);
    }
}
