package de.softwareforge.pgpsigner.util;

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

import de.softwareforge.pgpsigner.key.Key;

/**
 * Some helper methods to display values and key related data.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public final class DisplayHelpers
{

    private DisplayHelpers()
    {
    }

    public static final String showNullValue(final Object value)
    {

        if (value == null)
        {
            return "<unset>";
        }

        if (value instanceof String)
        {
            return (String) value;
        }

        return String.valueOf(value);
    }

    public static final String showKey(final Key key)
    {

        if (key == null)
        {
            return "<unset>";
        }

        StringBuffer sb = new StringBuffer();

        Iterator it = key.getUserIds();
        if (it.hasNext())
        {
            sb.append(key.getKeyId()).append(" (");
            sb.append(it.next());
            sb.append(")");
        }
        return sb.toString();
    }
}
