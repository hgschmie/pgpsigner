package de.softwareforge.pgpsigner.commands;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.NullCompletor;
import jline.SimpleCompletor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.lang.StringUtils;

import de.softwareforge.pgpsigner.util.AppContext;

public abstract class AbstractCommand
{

    private AppContext context = null;

    public abstract String getName();

    public abstract String getHelp();

    public void setContext(final AppContext context)
    {
        this.context = context;
    }

    protected AppContext getContext()
    {
        return context;
    }

    public boolean hasCommandLineOption()
    {
        return getCommandLineOption() != null;
    }

    public Option getCommandLineOption()
    {
        return new Option(getName(), getHelp());
    }

    public boolean hasCompletor()
    {
        return getCompletor() != null;
    }

    public Completor getCompletor()
    {
        return new ArgumentCompletor(new Completor[] { new SimpleCompletor(getName()), new NullCompletor() });
    }

    public void processCommandLineOption(final CommandLine line)
    {
        if (line.hasOption(getName()))
        {
            String[] cmds = new String[] { getName(), line.getOptionValue(getName()) };

            if (prerequisiteInteractiveCommand(cmds))
            {
                executeInteractiveCommand(cmds);
            }
        }
    }

    public boolean matchInteractiveCommand(final String command)
    {
        return StringUtils.equalsIgnoreCase(command, getName());
    }

    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        return true;
    }

    public void executeInteractiveCommand(final String[] args)
    {
    }
}
