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
import jline.FileNameCompletor;
import jline.NullCompletor;
import jline.SimpleCompletor;

import org.apache.commons.cli.Option;

/**
 * The "partyring" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class PartyRingCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "partyring";
    }

    public String getHelp()
    {
        return "sets the party key ring";
    }

    @Override
    public Option getCommandLineOption()
    {
        Option option = super.getCommandLineOption();
        option.setArgs(1);
        option.setArgName("file");
        return option;
    }

    @Override
    public Completor getCompletor()
    {
        return new ArgumentCompletor(
                new Completor[] { new SimpleCompletor(getName()), new FileNameCompletor(), new NullCompletor() });
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Current party key ring is "
                    + (getContext().getPartyRing() == null ? "<unset>" : getContext().getPartyRing().getRingFileName()));
            return false;
        }

        return true;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        String ringFile = args[1];

        System.out.print("Loading key ring " + ringFile + "...");

        try
        {
            getContext().getPartyRing().load(ringFile);
            System.out.println("...ok! " + getContext().getPartyRing().size() + " Keys loaded");

        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            System.out.println("... failed! Could not read keyRing " + ringFile + ": " + e.getMessage());
        }
        finally
        {
            getContext().updatePartyRing(true);
        }
    }
}
