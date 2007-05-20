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

import jline.ArgumentCompletor;
import jline.Completor;
import jline.NullCompletor;
import jline.SimpleCompletor;

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.jline.SecretKeyCompletor;
import de.softwareforge.pgpsigner.key.KeyId;
import de.softwareforge.pgpsigner.key.SecretKey;
import de.softwareforge.pgpsigner.util.DisplayHelpers;

/**
 * The "signkey" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class SignKeyCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "signkey";
    }

    public String getHelp()
    {
        return "sets the signing key";
    }

    @Override
    public Option getCommandLineOption()
    {
        Option option = super.getCommandLineOption();
        option.setArgs(1);
        option.setArgName("key");
        return option;
    }

    @Override
    public Completor getCompletor()
    {
        return new ArgumentCompletor(new Completor[] { new SimpleCompletor(getName()), new SecretKeyCompletor(getContext()),
                new NullCompletor() });
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Current sign key is " + DisplayHelpers.showKey(getContext().getSignKey()));
            return false;
        }

        if (getContext().getSecretRing() == null)
        {
            System.out.println("Secret key ring has not yet been set, can not set sign key.");
            return false;
        }

        if (!getContext().getSecretRing().containsId(new KeyId(args[1])))
        {
            System.out.println("Sign key " + args[1] + " is not on secret keyring, sign key unchanged.");
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        SecretKey secretKey = getContext().getSecretRing().getKey(new KeyId(args[1]));
        getContext().setSignKey(secretKey);
        System.out.println("Sign Key is now " + DisplayHelpers.showKey(secretKey));
    }
}
