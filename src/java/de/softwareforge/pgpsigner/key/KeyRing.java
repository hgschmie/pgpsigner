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

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Base class for public and secret key ring, containing common methods.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public abstract class KeyRing
{

    private String ringFileName = null;

    public String getRingFileName()
    {
        return ringFileName;
    }

    protected void setRingFileName(final String ringFileName)
    {
        this.ringFileName = ringFileName;
    }

    public boolean isLoaded()
    {
        return ringFileName != null;
    }

    public void clear()
    {
        getKeys().clear();
        setRingFileName(null);
    }

    protected abstract Map<KeyId, ? extends Key> getKeys();

    public int size()
    {
        return getKeys().size();
    }

    public boolean containsId(final KeyId keyId)
    {
        return getKeys().containsKey(keyId);
    }

    public Set<KeyId> getIds()
    {
        return getKeys().keySet();
    }

    public String toString()
    {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("keys", toString(null).toString()).toString();
    }

    public StringBuffer toString(final StringBuffer buf)
    {
        StringBuffer sb = (buf != null) ? buf : new StringBuffer();

        int count = 0;
        for (Key key : getKeys().values())
        {
            sb.append(++count).append(" | ").append(key.toString()).append("\n");
        }

        return sb;
    }
}
