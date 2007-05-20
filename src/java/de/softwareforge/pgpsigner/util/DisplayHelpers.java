package de.softwareforge.pgpsigner.util;

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
