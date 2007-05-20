package de.softwareforge.pgpsigner.commands;

/*
 * Copyright (C) 2007 Henning P. Schmiedehausen
 *
 * See the NOTICE file distributed with this work for additional
 * information
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */

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
