/**
 *
 */
package de.softwareforge.pgpsigner.key;

import java.security.NoSuchProviderException;
import java.util.Iterator;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;

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
