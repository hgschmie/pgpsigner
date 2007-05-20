package de.softwareforge.pgpsigner.commands;

import org.apache.commons.cli.Option;

/**
 * The "reset" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class ResetCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "reset";
    }

    public String getHelp()
    {
        return "reset all application state";
    }

    @Override
    public Option getCommandLineOption()
    {
        return null;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        getContext().reset();
    }
}
