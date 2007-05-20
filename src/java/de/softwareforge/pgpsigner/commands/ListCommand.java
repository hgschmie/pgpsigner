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

import java.util.Iterator;

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.key.PublicKey;
import de.softwareforge.pgpsigner.key.PublicKeyRing;

/**
 * The "list" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class ListCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "list";
    }

    public String getHelp()
    {
        return "list the keys available for signing";
    }

    @Override
    public Option getCommandLineOption()
    {
        return null;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        PublicKeyRing partyRing = getContext().getPartyRing().getVisibleKeys();

        int count = 0;
        for (PublicKey key : partyRing.values())
        {
            System.out.print(++count + " | " + key.getKeyId() + " | ");

            System.out.print(key.isSigned() ? "S" : " ");
            System.out.print(key.isMailed() ? "M" : " ");
            System.out.print(key.isUploaded() ? "U" : " ");

            Iterator it = key.getUserIds();
            if (it.hasNext())
            {
                System.out.println(" | " + it.next());
            }
        }
    }
}
