package de.softwareforge.pgpsigner.key;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;

/**
 * A wrapper around a PGPPublicKey object to manage application state information.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class PublicKey extends Key
{

    private PGPPublicKey pgpPublicKey;

    private boolean visible = true;

    private boolean signed = false;

    private boolean mailed = false;

    private boolean uploaded = false;

    public PublicKey(final PGPPublicKey pgpPublicKey)
    {
        super(pgpPublicKey.getKeyID());
        this.pgpPublicKey = pgpPublicKey;
    }

    public PGPPublicKey getPGPPublicKey()
    {
        return pgpPublicKey;
    }

    public void setPGPPublicKey(final PGPPublicKey pgpPublicKey)
    {
        this.pgpPublicKey = pgpPublicKey;
        setMailed(false);
        setUploaded(false);
    }

    public Iterator getUserIds()
    {
        return pgpPublicKey.getUserIDs();
    }

    public boolean isSignedWith(final SecretKey signKey)
    {

        if (signKey == null)
        {
            return false;
        }

        PGPSecretKey pgpSecretKey = signKey.getPGPSecretKey();

        for (Iterator it = pgpPublicKey.getSignatures(); it.hasNext();)
        {
            PGPSignature sig = (PGPSignature) it.next();

            if (pgpSecretKey.getKeyID() == sig.getKeyID())
            {
                return true;
            }
        }

        return false;
    }

    public void resetAllFlags()
    {
        setMailed(false);
        setSigned(false);
        setUploaded(false);
        setVisible(false);
    }

    public boolean isMailed()
    {
        return this.mailed;
    }

    public void setMailed(final boolean mailed)
    {
        this.mailed = mailed;
    }

    public boolean isSigned()
    {
        return this.signed;
    }

    public void setSigned(final boolean signed)
    {
        this.signed = signed;
    }

    public boolean isUploaded()
    {
        return this.uploaded;
    }

    public void setUploaded(final boolean uploaded)
    {
        this.uploaded = uploaded;
    }

    public boolean isVisible()
    {
        return this.visible;
    }

    public void setVisible(final boolean visible)
    {
        this.visible = visible;
    }

    public byte[] getArmor() throws IOException
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ArmoredOutputStream aos = null;
        try
        {
            aos = new ArmoredOutputStream(baos);
            pgpPublicKey.encode(aos);
            aos.flush();
        }
        finally
        {
            IOUtils.closeQuietly(aos);
        }
        return baos.toByteArray();
    }

    public String toString()
    {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("key", toString(null).toString()).toString();
    }

    public StringBuffer toString(final StringBuffer buf)
    {
        StringBuffer sb = (buf != null) ? buf : new StringBuffer();

        sb.append(getKeyId()).append(" | ");

        sb.append(isVisible() ? "V" : " ");
        sb.append(isSigned() ? "S" : " ");
        sb.append(isMailed() ? "M" : " ");
        sb.append(isUploaded() ? "U" : " ");

        sb.append(" | ");

        Iterator it = getUserIds();
        if (it.hasNext())
        {
            sb.append(it.next());
        }

        return sb;
    }
}
