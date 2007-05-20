package de.softwareforge.pgpsigner.commands;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.NullCompletor;
import jline.SimpleCompletor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.lang.StringUtils;

import de.softwareforge.pgpsigner.util.AppContext;

/**
 * Base class for all the application commands. It contains common functionality and
 * offers a reasonable default behaviour for most commands.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public abstract class AbstractCommand
{

    private AppContext context = null;

    /**
     * @see Command#getName()
     */
    public abstract String getName();

    /**
     * @see Command#getHelp()
     */
    public abstract String getHelp();

    /**
     * @see Command#setContext(AppContext)
     */
    public void setContext(final AppContext context)
    {
        this.context = context;
    }

    /**
     * Get the current application context.
     *
     * @return The current application context.
     */
    protected AppContext getContext()
    {
        return context;
    }

    /**
     * Returns true if this command should be available on the command line at startup. The implementation
     * returns true if getCommandLineOption returns not-null.
     *
     * @true if this command should be available from the command line.
     */
    public boolean hasCommandLineOption()
    {
        return getCommandLineOption() != null;
    }

    /**
     * Returns a default command line option containing of the command name and no parameters. Override with
     * a method returning null if you do not want any command line option or extend with a method adding
     * parameters if you want to have parameters for the command line option.
     *
     * @return An Option object describing the command line option or null if no command line option is wanted.
     */
    public Option getCommandLineOption()
    {
        return new Option(getName(), getHelp());
    }

    /**
     * Returns true if this command offers completion in interactive mode. This implementation returns true
     * if getCompletor() returns a non-null object.
     *
     * @return True if this command offers completion in interactive mode.
     */
    public boolean hasCompletor()
    {
        return getCompletor() != null;
    }

    /**
     * Returns a Completor object for completion in interactive mode which contains only the command name
     * and no arguments. Override with a method returning a more complex completor if you need argument
     * completion in interactive mode.
     *
     * @return A Completor object for completion in interactive mode or null if no completion is required.
     */
    public Completor getCompletor()
    {
        return new ArgumentCompletor(new Completor[] { new SimpleCompletor(getName()), new NullCompletor() });
    }

    /**
     * Process a command line option. If a command has declared a command line option, this method will be
     * called at start up with the command line. The command is responsible for reacting on the passed command
     * line.
     *
     * This method normally should never be overridden by a command implementation unless you need special reaction
     * by the command (e.g. the "help" command line option also quits the application).
     *
     * @param line The CommandLine object representing all user command line options.
     */
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

    /**
     * Returns true if this command object considers itself responsible for the given interactive command. This implementation
     * does a case insensitive match on the name returned by getName() and returns true if they match.
     *
     * @param command The current command entered by the user.
     * @return True if the command object wants to process this command.
     */
    public boolean matchInteractiveCommand(final String command)
    {
        return StringUtils.equalsIgnoreCase(command, getName());
    }

    /**
     * Allows the command to check prerequisites before actually executing the command. Commands that offer
     * one mode where parameters are processed and another mode where just the command name will display the
     * current setting of a command, should use this method to display the current setting when no arguments
     * are given.
     *
     * This implementation does no checking and always returns true.
     *
     * @param args An array of Strings containing the interactive command and its arguments. This array is never null its length is
     * always at least one.
     * @return True if the command considers all its prerequisites fulfilled, False otherwise.
     */
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        return true;
    }

    /**
     * @see Command#executeInteractiveCommand(String [])
     */
    public abstract void executeInteractiveCommand(final String[] args);
}
