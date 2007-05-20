package de.softwareforge.pgpsigner.commands;

import jline.Completor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.util.AppContext;

/**
 * This interface describes a command to be plugged into the application itself.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public interface Command
{
    /**
     * Returns the name of this command.
     *
     * @return The command name.
     */
    String getName();

    /**
     * Returns a short, one line help for this command.
     *
     * @return A short help text.
     */
    String getHelp();

    /**
     * Returns true if this command should be available on the command line at startup.
     *
     * @true if this command should be available from the command line.
     */
    boolean hasCommandLineOption();

    /**
     * Returns an option object describing the command line option.
     *
     * @return An Option object describing the command line option or null if no command line option is wanted.
     */
    Option getCommandLineOption();

    /**
     * Returns true if this command offers completion in interactive mode.
     *
     * @return True if this command offers completion in interactive mode.
     */
    boolean hasCompletor();

    /**
     * Returns a Completor object for completion in interactive mode.
     *
     * @return A Completor object for completion in interactive mode or null if no completion is required.
     */
    Completor getCompletor();

    /**
     * Process a command line option. If a command has declared a command line option, this method will be
     * called at start up with the command line. The command is responsible for reacting on the passed command
     * line.
     *
     * @param line The CommandLine object representing all user command line options.
     */
    void processCommandLineOption(CommandLine line);

    /**
     * Returns true if this command object considers itself responsible for the given interactive command.
     *
     * @param command The current command entered by the user.
     * @return True if the command object wants to process this command.
     */
    boolean matchInteractiveCommand(final String command);

    /**
     * Allows the command to check prerequisites before actually executing the command. Commands that offer 
     * one mode where parameters are processed and another mode where just the command name will display the
     * current setting of a command, should use this method to display the current setting when no arguments
     * are given.
     *
     * @param args An array of Strings containing the interactive command and its arguments. This array is never null its length is
     * always at least one.
     * @return True if the command considers all its prerequisites fulfilled, False otherwise.
     */
    boolean prerequisiteInteractiveCommand(final String[] args);

    /**
     * Execute the command.
     *
     * @param args An array of Strings containing the interactive command and its arguments. This array is never null its length is
     * always at least one.
     */
    void executeInteractiveCommand(final String[] args);

    /**
     * Set the application context for this command. This method is guaranteed to be called as the very first method call
     * on the command and it is called only once.
     *
     * @param context The current Application context object.
     */
    void setContext(AppContext context);
}
