package de.softwareforge.pgpsigner.jline;

import java.util.List;
import java.util.Set;

import de.softwareforge.pgpsigner.key.KeyId;

/**
 * A generic key completor that works on a set of KeyId objects.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public abstract class AbstractKeyCompletor
{

    @SuppressWarnings("unchecked")
    protected int complete(final Set<KeyId> keySet, final String buffer, final int cursor, final List candidates)
    {
        String start = (buffer == null) ? "" : buffer;

        for (KeyId key : keySet)
        {
            if (key.toString().startsWith(start))
            {
                candidates.add(key.toString());
            }

        }

        if (candidates.size() == 1)
        {
            candidates.set(0, ((String) candidates.get(0)) + " ");
        }

        // the index of the completion is always from the beginning of
        // the buffer.
        return (candidates.size() == 0) ? (-1) : 0;
    }
}
