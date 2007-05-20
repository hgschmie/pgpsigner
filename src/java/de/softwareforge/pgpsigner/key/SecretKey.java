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

import java.security.NoSuchProviderException;
import java.util.Iterator;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;

/**
 * A wrapper around a PGPSecretKey object to manage application state information.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class SecretKey extends Key
{

    private final PGPSecretKey pgpSecretKey;

    private PGPPrivateKey privateKey = null;

    public SecretKey(final PGPSecretKey pgpSecretKey)
    {
        super(pgpSecretKey.getKeyID());
        this.pgpSecretKey = pgpSecretKey;
    }

    public PGPSecretKey getPGPSecretKey()
    {
        return pgpSecretKey;
    }

    public PGPPublicKey getPGPPublicKey()
    {
        return pgpSecretKey.getPublicKey();
    }

    public PGPPrivateKey getPGPPrivateKey()
    {
        return privateKey;
    }

    public boolean isUnlocked()
    {
        return privateKey != null;
    }

    public void unlock(final String passPhrase) throws PGPException, NoSuchProviderException
    {
        privateKey = pgpSecretKey.extractPrivateKey(passPhrase.toCharArray(), "BC");
    }

    public Iterator getUserIds()
    {
        return pgpSecretKey.getUserIDs();
    }

    public String toString()
    {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("key", toString(null).toString()).toString();
    }

    public StringBuffer toString(final StringBuffer buf)
    {
        StringBuffer sb = (buf != null) ? buf : new StringBuffer();

        sb.append(getKeyId()).append(" | ");

        sb.append(isUnlocked() ? "U" : " ");

        sb.append(" | ");

        Iterator it = getUserIds();
        if (it.hasNext())
        {
            sb.append(it.next());
        }

        return sb;
    }
}
