package de.softwareforge.pgpsigner.jline;

import java.util.List;

import jline.Completor;
import de.softwareforge.pgpsigner.util.AppContext;

public class SecretKeyCompletor extends AbstractKeyCompletor implements Completor
{

    private final AppContext context;

    public SecretKeyCompletor(final AppContext context)
    {
        this.context = context;
    }

    public int complete(final String buffer, final int cursor, final List candidates)
    {
        return super.complete(context.getSecretRing().getIds(), buffer, cursor, candidates);
    }
}
