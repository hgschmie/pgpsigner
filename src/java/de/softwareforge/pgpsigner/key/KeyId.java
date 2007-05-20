package de.softwareforge.pgpsigner.key;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Represents a Key Id for managing keys on the various key rings.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class KeyId implements Comparable<KeyId>
{

    private final Long id;

    private final String keyId;

    public KeyId(final Long id)
    {
        this.id = id;
        this.keyId = "0x" + StringUtils.right(Long.toHexString(id), 8);
    }

    public KeyId(final String keyId)
    {
        this.id = null;
        this.keyId = keyId;
    }

    public Long getId()
    {
        return id;
    }

    public String toString()
    {
        return keyId;
    }

    public int compareTo(KeyId o)
    {
        return new CompareToBuilder().append(this.id, o.id).toComparison();
    }

    public int hashCode()
    {
        return keyId.hashCode();
    }

    public boolean equals(final Object obj)
    {
        if (!(obj instanceof KeyId))
        {
            return false;
        }
        if (this == obj)
        {
            return true;
        }
        KeyId rhs = (KeyId) obj;
        return new EqualsBuilder().append(keyId, rhs.keyId).isEquals();
    }

}
