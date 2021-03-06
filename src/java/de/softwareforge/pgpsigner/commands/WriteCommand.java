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

import java.io.IOException;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.NullCompletor;
import jline.SimpleCompletor;

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.jline.PartyKeyCompletor;
import de.softwareforge.pgpsigner.key.KeyId;
import de.softwareforge.pgpsigner.key.PublicKey;
import de.softwareforge.pgpsigner.util.DisplayHelpers;

/**
 * The "write" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class WriteCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "write";
    }

    public String getHelp()
    {
        return "write an ASCII-armored key from the party list";
    }

    @Override
    public Option getCommandLineOption()
    {
        return null;
    }

    @Override
    public Completor getCompletor()
    {
        return new ArgumentCompletor(new Completor[] { new SimpleCompletor(getName()), new PartyKeyCompletor(getContext()),
                new NullCompletor() });
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Missing argument for " + args[0]);
            return false;
        }

        if (!getContext().getPartyRing().getVisibleKeys().containsId(new KeyId(args[1])))
        {
            System.out.println("Key " + args[1] + " is not on the party key ring!");
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {

        PublicKey key = getContext().getPartyRing().getVisibleKeys().getKey(new KeyId(args[1]));

        try
        {
            String armoredKey = new String(key.getArmor(), "ISO-8859-1");
            System.out.println(armoredKey);
        }
        catch (IOException ioe)
        {
            System.out.println("Could not write key " + DisplayHelpers.showKey(key));
        }
    }
}
