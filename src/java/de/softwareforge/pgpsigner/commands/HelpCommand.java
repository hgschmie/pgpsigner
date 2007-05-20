package de.softwareforge.pgpsigner.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import de.softwareforge.pgpsigner.PGPSigner;

/**
 * The "help" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class HelpCommand extends AbstractCommand implements Command
{

    public HelpCommand()
    {
    }

    public String getName()
    {
        return "help";
    }

    public String getHelp()
    {
        return "display help";
    }

    @Override
    public void processCommandLineOption(final CommandLine line)
    {
        if (line.hasOption(getName()))
        {
            usage();
            System.exit(0);
        }
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        for (Command command : getContext().getCommands())
        {
            System.out.println(command.getName() + " - " + command.getHelp());
        }
    }

    private void usage()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(PGPSigner.APPLICATION_NAME, getContext().getOptions());
    }
}
