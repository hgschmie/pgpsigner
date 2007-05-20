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

import org.apache.commons.cli.Option;
import org.apache.commons.lang.StringUtils;

import de.softwareforge.pgpsigner.key.PublicKey;
import de.softwareforge.pgpsigner.util.HKPSender;

/**
 * The "upload" command.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class UploadCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "upload";
    }

    public String getHelp()
    {
        return "upload signed keys to a public key server";
    }

    @Override
    public boolean prerequisiteInteractiveCommand(final String[] args)
    {
        if (StringUtils.isEmpty(getContext().getKeyServer()))
        {
            System.out.println("Key server must be set!");
            return false;
        }

        return true;
    }

    @Override
    public Option getCommandLineOption()
    {
        return null;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        HKPSender sender = new HKPSender(getContext().getKeyServer());

        for (PublicKey key : getContext().getPartyRing().getVisibleKeys().values())
        {

            if (key.isSigned() && !key.isUploaded())
            {
                System.out.print("Uploading Key " + key.getKeyId() + " to key server " + getContext().getKeyServer() + "...");

                try
                {
                    if (getContext().isSimulation())
                    {
                        System.out.println("... ok! (simulated)");
                        key.setUploaded(true);
                    }
                    else
                    {

                        if (sender.uploadKey(key.getArmor()))
                        {
                            System.out.println("... ok!");
                            key.setUploaded(true);
                        }
                        else
                        {
                            System.out.println("... failed!");
                        }
                    }
                }
                catch (IOException ioe)
                {
                    System.out.println("... failed (exception!");
                }
            }
        }
    }
}
