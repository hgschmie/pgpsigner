package de.softwareforge.pgpsigner.key;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;

/**
 * Represents a PGP secret key ring file, offering methods for loading and finding keys.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class SecretKeyRing extends KeyRing
{

    private final Map<KeyId, SecretKey> keys = new HashMap<KeyId, SecretKey>();

    public void load(final String ringFileName) throws IOException, PGPException
    {
        File ringFile = new File(ringFileName);

        if (!ringFile.exists() || !ringFile.isFile())
        {
            throw new IOException("Ring file " + ringFileName + " is not a file!");
        }

        clear();
        setRingFileName(ringFileName);

        PGPSecretKeyRingCollection secretRing = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(new FileInputStream(
                ringFile)));

        for (Iterator ringIt = secretRing.getKeyRings(); ringIt.hasNext();)
        {
            PGPSecretKeyRing keyRing = (PGPSecretKeyRing) ringIt.next();

            for (Iterator it = keyRing.getSecretKeys(); it.hasNext();)
            {
                PGPSecretKey secretKey = (PGPSecretKey) it.next();

                if (secretKey.isMasterKey())
                {
                    SecretKey secKey = new SecretKey(secretKey);
                    keys.put(secKey.getKeyId(), secKey);
                }
            }
        }
    }

    public SecretKey getKey(final KeyId keyId)
    {
        return keys.get(keyId);
    }

    protected Map<KeyId, SecretKey> getKeys()
    {
        return keys;
    }
}
