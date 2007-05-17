package de.softwareforge.pgpsigner.key;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class KeyRing
{

    private String ringFileName = null;

    public String getRingFileName()
    {
        return ringFileName;
    }

    protected void setRingFileName(final String ringFileName)
    {
        this.ringFileName = ringFileName;
    }

    public boolean isLoaded()
    {
        return ringFileName != null;
    }

    public void clear()
    {
        getKeys().clear();
        setRingFileName(null);
    }

    protected abstract Map<KeyId, ? extends Key> getKeys();

    public int size()
    {
        return getKeys().size();
    }

    public boolean containsId(final KeyId keyId)
    {
        return getKeys().containsKey(keyId);
    }

    public Set<KeyId> getIds()
    {
        return getKeys().keySet();
    }

    public String toString()
    {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("keys", toString(null).toString()).toString();
    }

    public StringBuffer toString(final StringBuffer buf)
    {
        StringBuffer sb = (buf != null) ? buf : new StringBuffer();

        int count = 0;
        for (Key key : getKeys().values())
        {
            sb.append(++count).append(" | ").append(key.toString()).append("\n");
        }

        return sb;
    }
}
