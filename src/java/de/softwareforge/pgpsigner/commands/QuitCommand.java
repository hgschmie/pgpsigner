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

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.PGPSigner;

/**
 * The "quit" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

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
