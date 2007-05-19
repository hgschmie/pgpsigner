package de.softwareforge.pgpsigner.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.Options;
import org.apache.commons.collections.map.ListOrderedMap;

import de.softwareforge.pgpsigner.commands.Command;
import de.softwareforge.pgpsigner.key.KeyId;
import de.softwareforge.pgpsigner.key.PublicKey;
import de.softwareforge.pgpsigner.key.PublicKeyRing;
import de.softwareforge.pgpsigner.key.SecretKey;
import de.softwareforge.pgpsigner.key.SecretKeyRing;

public class AppContext
{

    @SuppressWarnings("unchecked")
    private Map<String, Command> commandMap = (Map<String, Command>) ListOrderedMap.decorate(new HashMap<String, Command>());

    private Options options = new Options();

    private final PublicKeyRing partyRing = new PublicKeyRing();

    private final PublicKeyRing publicRing = new PublicKeyRing();

    private final SecretKeyRing secretRing = new SecretKeyRing();

    private SecretKey signKey = null;

    private String mailServer = null;

    private String keyServer = null;

    private String signEvent = null;

    private boolean shutdown = false;

    private boolean simulation = false;

    public void reset()
    {
        this.partyRing.clear();
        this.publicRing.clear();
        this.secretRing.clear();
        this.signKey = null;
        this.mailServer = null;
        this.keyServer = null;
        this.signEvent = null;
        this.simulation = false;
    }

    public Options getOptions()
    {
        return options;
    }

    public void addCommand(final Command command)
    {
        command.setContext(this);
        commandMap.put(command.getName(), command);

        if (command.hasCommandLineOption())
        {
            options.addOption(command.getCommandLineOption());
        }
    }

    public Collection<Command> getCommands()
    {
        return commandMap.values();
    }

    public void toggleSimulation()
    {
        simulation = !simulation;
    }

    public boolean isSimulation()
    {
        return simulation;
    }

    public boolean isShutdown()
    {
        return shutdown;
    }

    public void setShutdown(final boolean shutdown)
    {
        this.shutdown = shutdown;
    }

    public PublicKeyRing getPartyRing()
    {
        return partyRing;
    }

    public SecretKeyRing getSecretRing()
    {
        return secretRing;
    }

    public PublicKeyRing getPublicRing()
    {
        return publicRing;
    }

    public SecretKey getSignKey()
    {
        return this.signKey;
    }

    public void setSignKey(final SecretKey signKey)
    {
        this.signKey = signKey;
        updatePartyRing(true);
    }

    public String getSignEvent()
    {
        return this.signEvent;
    }

    public void setSignEvent(final String signEvent)
    {
        this.signEvent = signEvent;
    }

    public String getKeyServer()
    {
        return this.keyServer;
    }

    public void setKeyServer(final String keyServer)
    {
        this.keyServer = keyServer;
    }

    public String getMailServer()
    {
        return this.mailServer;
    }

    public void setMailServer(final String mailServer)
    {
        this.mailServer = mailServer;
    }

    public void updatePartyRing(boolean resetAllFlags)
    {

        for (KeyId entry : partyRing.getIds())
        {
            if (resetAllFlags)
            {
                partyRing.resetAllFlags(entry);
            }

            partyRing.setVisible(entry, true);

            if (signKey != null)
            {
                // If we ever signed that key, there is a good chance, that it
                // is on our public keyring. Check there.
                if (publicRing.containsId(entry))
                {
                    PublicKey pubKey = publicRing.getKey(entry);
                    if (pubKey.isSignedWith(signKey))
                    {
                        System.out.println("Party Key " + pubKey.getKeyId() + " has already been signed with Key "
                                + signKey.getKeyId() + ", skipping.");
                        partyRing.setVisible(entry, false);
                    }
                }
            }
        }

        for (KeyId entry : secretRing.getIds())
        {
            if (partyRing.containsId(entry))
            {
                System.out.println("Party Key " + entry + " is on secret keyring, skipping.");
                partyRing.setVisible(entry, false);
            }
        }
    }
}
