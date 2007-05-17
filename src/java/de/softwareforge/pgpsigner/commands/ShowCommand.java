package de.softwareforge.pgpsigner.commands;

import org.apache.commons.cli.Option;

import de.softwareforge.pgpsigner.key.PublicKeyRing;
import de.softwareforge.pgpsigner.key.SecretKey;
import de.softwareforge.pgpsigner.key.SecretKeyRing;
import de.softwareforge.pgpsigner.util.DisplayHelpers;

public class ShowCommand extends AbstractCommand implements Command
{

    public String getName()
    {
        return "show";
    }

    public String getHelp()
    {
        return "show the current options";
    }

    @Override
    public Option getCommandLineOption()
    {
        return null;
    }

    @Override
    public void executeInteractiveCommand(final String[] args)
    {
        SecretKeyRing secretRing = getContext().getSecretRing();
        PublicKeyRing publicRing = getContext().getPublicRing();
        PublicKeyRing partyRing = getContext().getPartyRing().getVisibleKeys();
        SecretKey signKey = getContext().getSignKey();

        System.out.println("Secret File: " + DisplayHelpers.showNullValue(secretRing.getRingFileName())
                + ((secretRing.size() > 0) ? " (" + secretRing.size() + " Keys loaded)" : ""));
        System.out.println("Public File: " + DisplayHelpers.showNullValue(publicRing.getRingFileName())
                + ((publicRing.size() > 0) ? " (" + publicRing.size() + " Keys loaded)" : ""));
        System.out.println("Party File:  " + DisplayHelpers.showNullValue(partyRing.getRingFileName())
                + ((partyRing.size() > 0) ? " (" + partyRing.size() + " Keys loaded)" : ""));
        System.out.println("Sign key:    " + DisplayHelpers.showNullValue(DisplayHelpers.showKey(signKey))
                + (signKey != null && signKey.isUnlocked() ? " (unlocked)" : ""));
        System.out.println("Mail Server: " + DisplayHelpers.showNullValue(getContext().getMailServer()));
        System.out.println("Key Server:  " + DisplayHelpers.showNullValue(getContext().getKeyServer()));
        System.out.println("Sign Event:  " + DisplayHelpers.showNullValue(getContext().getSignEvent()));
        System.out.println("Simulation:  " + (getContext().isSimulation() ? "yes" : "no"));
    }
}
