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
 * The "mailserver" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class MailServerCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "mailserver";
    }

    public String getHelp()
    {
        return "sets the mailserver for mailing keys";
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
            System.out.println("Current mail server is " + DisplayHelpers.showNullValue(getContext().getMailServerHost()));
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        String mailServerHost = args[1];
        String mailServerPort = "25";

        int colonIndex = mailServerHost.indexOf(':');
        if (colonIndex > -1) {
            mailServerPort = mailServerHost.substring(colonIndex +1);
            mailServerHost = mailServerHost.substring(0, colonIndex);
        }
        getContext().setMailServerHost(mailServerHost);
        getContext().setMailServerPort(Integer.parseInt(mailServerPort));

        for (PublicKey key : getContext().getPartyRing().getVisibleKeys().values())
        {
            key.setUploaded(false);
        }
    }
}
