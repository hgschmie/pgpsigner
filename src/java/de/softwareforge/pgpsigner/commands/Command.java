package de.softwareforge.pgpsigner.commands;

import jline.Completor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.util.AppContext;

public interface Command
{

    String getName();

    String getHelp();

    boolean hasCommandLineOption();

    Option getCommandLineOption();

    boolean hasCompletor();

    Completor getCompletor();

    void processCommandLineOption(CommandLine line);

    boolean matchInteractiveCommand(final String command);

    boolean prerequisiteInteractiveCommand(final String[] args);

    void executeInteractiveCommand(final String[] args);

    void setContext(AppContext context);
}
