package de.softwareforge.pgpsigner.commands;

import org.apache.commons.cli.Option;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import de.softwareforge.pgpsigner.util.DisplayHelpers;

/**
 * The "signevent" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class SignEventCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "signevent";
    }

    public String getHelp()
    {
        return "sets the key signing event name";
    }

    @Override
    public Option getCommandLineOption()
    {
        Option option = super.getCommandLineOption();
        option.setArgs(1);
        option.setArgName("eventname");
        return option;
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Current key signing event is " + DisplayHelpers.showNullValue(getContext().getSignEvent()));
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        getContext().setSignEvent(StringUtils.join(ArrayUtils.subarray(args, 1, args.length), ' '));
    }
}
