package de.softwareforge.pgpsigner.jline;

import java.util.List;

import jline.Completor;
import de.softwareforge.pgpsigner.util.AppContext;

public class PartyKeyCompletor extends AbstractKeyCompletor implements Completor
{

    private final AppContext context;

    public PartyKeyCompletor(final AppContext context)
    {
        this.context = context;
    }

    public int complete(final String buffer, final int cursor, final List candidates)
    {
        return super.complete(context.getPartyRing().getVisibleKeys().getIds(), buffer, cursor, candidates);
    }
}
