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
import java.security.NoSuchProviderException;

import jline.ConsoleReader;

import org.apache.commons.cli.Option;
import org.bouncycastle.openpgp.PGPException;

import de.softwareforge.pgpsigner.key.SecretKey;
import de.softwareforge.pgpsigner.util.DisplayHelpers;

/**
 * The "unlock" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class UnlockCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "unlock";
    }

    public String getHelp()
    {
        return "unlock the current sign key";
    }

    @Override
    public Option getCommandLineOption()
    {
        return null;
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {

        if (getContext().getSignKey() == null)
        {
            System.out.println("No sign key has been selected!");
            return false;
        }

        if (getContext().getSignKey().isUnlocked())
        {
            System.out.println("Sign key is already unlocked!");
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {

        SecretKey signKey = getContext().getSignKey();

        try
        {
            ConsoleReader reader = new ConsoleReader();
            reader.setUseHistory(false);
            reader.setBellEnabled(false);
            reader.setDefaultPrompt("Passphrase for " + signKey.getKeyId() + "> ");
            String passPhrase = reader.readLine('*');

            signKey.unlock(passPhrase);

            System.out.println("Private key " + DisplayHelpers.showKey(signKey) + " unlocked!");
        }
        catch (IOException ioe)
        {
            System.out.println("Could not read pass phrase: " + ioe.getMessage());
            return;
        }
        catch (PGPException pgpe)
        {
            System.out.println("Could not unlock private key.");
            return;
        }
        catch (NoSuchProviderException nspe)
        {
            System.out.println("Could not decrypt private key.");
            return;
        }
    }
}
