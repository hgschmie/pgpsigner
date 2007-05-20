package de.softwareforge.pgpsigner.key;

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

import org.apache.commons.lang.StringUtils;

/**
 * Base class for secret and public key class.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public abstract class Key
{

    private final KeyId keyId;

    protected Key(final long keyId)
    {
        this.keyId = new KeyId(keyId);
    }

    public KeyId getKeyId()
    {
        return keyId;
    }

    public abstract Iterator getUserIds();

    public String getMailAddress()
    {

        Iterator it = getUserIds();

        if (!it.hasNext())
        {
            return null;
        }

        String userId = (String) it.next();

        int leftIndex = userId.indexOf('<');
        int rightIndex = userId.indexOf('>');

        if (leftIndex == -1 || rightIndex == -1 || rightIndex <= leftIndex)
        {
            return null;
        }

        return userId.substring(leftIndex + 1, rightIndex);
    }

    public String getName()
    {

        Iterator it = getUserIds();

        if (!it.hasNext())
        {
            return null;
        }

        String userId = (String) it.next();

        int leftIndex = userId.indexOf('<');

        if (leftIndex == -1)
        {
            return "PGP key owner (unknown id)";
        }

        return StringUtils.strip(userId.substring(0, leftIndex));
    }
}
