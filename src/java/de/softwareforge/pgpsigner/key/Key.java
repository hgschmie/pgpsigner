package de.softwareforge.pgpsigner.key;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

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
