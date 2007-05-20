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

import de.softwareforge.pgpsigner.key.PublicKey;
import de.softwareforge.pgpsigner.util.DisplayHelpers;

/**
 * The "keyserver" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class KeyServerCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "keyserver";
    }

    public String getHelp()
    {
        return "sets the keyserver for uploading keys";
    }

    @Override
    public Option getCommandLineOption()
    {
        Option option = super.getCommandLineOption();
        option.setArgs(1);
        option.setArgName("hostname");
        return option;
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Current key server is " + DisplayHelpers.showNullValue(getContext().getKeyServer()));
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        getContext().setKeyServer(args[1]);

        for (PublicKey key : getContext().getPartyRing().getVisibleKeys().values())
        {
            key.setUploaded(false);
        }
    }
}
