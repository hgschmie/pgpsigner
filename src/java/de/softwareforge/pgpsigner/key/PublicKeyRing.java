package de.softwareforge.pgpsigner.key;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.Predicate;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;

public class PublicKeyRing extends KeyRing
{

    private final Map<KeyId, PublicKey> keys = new HashMap<KeyId, PublicKey>();

    public void load(final String ringFileName) throws IOException, PGPException
    {
        File ringFile = new File(ringFileName);

        if (!ringFile.exists() || !ringFile.isFile())
        {
            throw new IOException("Ring file " + ringFileName + " is not a file!");
        }

        clear();
        setRingFileName(ringFileName);

        PGPPublicKeyRingCollection publicRing = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(new FileInputStream(
                ringFile)));

        for (Iterator ringIt = publicRing.getKeyRings(); ringIt.hasNext();)
        {
            PGPPublicKeyRing keyRing = (PGPPublicKeyRing) ringIt.next();

            for (Iterator it = keyRing.getPublicKeys(); it.hasNext();)
            {
                PGPPublicKey publicKey = (PGPPublicKey) it.next();

                if (publicKey.isMasterKey())
                {
                    PublicKey pubKey = new PublicKey(publicKey);
                    keys.put(pubKey.getKeyId(), pubKey);
                }
            }
        }
    }

    public PublicKey getKey(final KeyId keyId)
    {
        return keys.get(keyId);
    }

    private void putKey(final KeyId keyId, final PublicKey publicKey)
    {
        keys.put(keyId, publicKey);
    }

    public void setVisible(final KeyId keyId, final boolean visible)
    {
        if (containsId(keyId))
        {
            getKey(keyId).setVisible(visible);
        }
    }

    public void resetAllFlags(final KeyId keyId)
    {
        if (containsId(keyId))
        {
            getKey(keyId).resetAllFlags();
        }
    }

    protected Map<KeyId, PublicKey> getKeys()
    {
        return keys;
    }

    public Collection<PublicKey> values()
    {
        return keys.values();
    }

    public PublicKeyRing getVisibleKeys()
    {

        return processMap(new Predicate()
        {
            public boolean evaluate(Object o)
            {
                return ((PublicKey) o).isVisible();
            }
        });
    }

    public PublicKeyRing getSignedKeys()
    {

        return processMap(new Predicate()
        {
            public boolean evaluate(Object o)
            {
                return ((PublicKey) o).isSigned();
            }
        });
    }

    public PublicKeyRing getMailedKeys()
    {

        return processMap(new Predicate()
        {
            public boolean evaluate(Object o)
            {
                return ((PublicKey) o).isMailed();
            }
        });
    }

    public PublicKeyRing getUploadedKeys()
    {

        return processMap(new Predicate()
        {
            public boolean evaluate(Object o)
            {
                return ((PublicKey) o).isUploaded();
            }
        });
    }

    private PublicKeyRing processMap(final Predicate predicate)
    {
        PublicKeyRing processedKeys = new PublicKeyRing();
        processedKeys.setRingFileName(getRingFileName());

        for (Map.Entry<KeyId, PublicKey> entry : keys.entrySet())
        {
            if (predicate.evaluate(entry.getValue()))
            {
                processedKeys.putKey(entry.getKey(), entry.getValue());
            }
        }

        return processedKeys;
    }
}
