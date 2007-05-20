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
