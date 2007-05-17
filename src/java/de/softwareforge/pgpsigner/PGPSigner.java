package de.softwareforge.pgpsigner;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import jline.Completor;
import jline.ConsoleReader;
import jline.History;
import jline.MultiCompletor;
import jline.Terminal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import de.softwareforge.pgpsigner.commands.Command;
import de.softwareforge.pgpsigner.commands.HelpCommand;
import de.softwareforge.pgpsigner.commands.KeyServerCommand;
import de.softwareforge.pgpsigner.commands.ListCommand;
import de.softwareforge.pgpsigner.commands.MailCommand;
import de.softwareforge.pgpsigner.commands.MailServerCommand;
import de.softwareforge.pgpsigner.commands.PartyRingCommand;
import de.softwareforge.pgpsigner.commands.PublicRingCommand;
import de.softwareforge.pgpsigner.commands.QuitCommand;
import de.softwareforge.pgpsigner.commands.RemoveCommand;
import de.softwareforge.pgpsigner.commands.ResetCommand;
import de.softwareforge.pgpsigner.commands.SecretRingCommand;
import de.softwareforge.pgpsigner.commands.ShowCommand;
import de.softwareforge.pgpsigner.commands.SignCommand;
import de.softwareforge.pgpsigner.commands.SignEventCommand;
import de.softwareforge.pgpsigner.commands.SignKeyCommand;
import de.softwareforge.pgpsigner.commands.SimulateCommand;
import de.softwareforge.pgpsigner.commands.UnlockCommand;
import de.softwareforge.pgpsigner.commands.UploadCommand;
import de.softwareforge.pgpsigner.commands.WriteCommand;
import de.softwareforge.pgpsigner.util.AppContext;

public class PGPSigner
{

    public static final String APPLICATION_NAME = "PGPSigner";

    public static final String APPLICATION_VERSION = APPLICATION_NAME + " V1.0";

    private final AppContext context = new AppContext();

    private Thread shutdownHook = new Thread()
    {
        public void run()
        {
            System.out.println();
            System.out.println("Goodbye");
        }
    };

    public static void main(final String[] args)
    {
        PGPSigner pgpSigner = new PGPSigner(args);

        try
        {
            pgpSigner.run();
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private PGPSigner(final String[] args)
    {

        Security.addProvider(new BouncyCastleProvider());

        registerCommand(new HelpCommand());
        registerCommand(new QuitCommand());
        registerCommand(new ResetCommand());
        registerCommand(new ShowCommand());
        registerCommand(new ListCommand());
        registerCommand(new UnlockCommand());
        registerCommand(new SimulateCommand());
        registerCommand(new PartyRingCommand());
        registerCommand(new PublicRingCommand());
        registerCommand(new SecretRingCommand());
        registerCommand(new SignKeyCommand());
        registerCommand(new MailServerCommand());
        registerCommand(new KeyServerCommand());
        registerCommand(new SignEventCommand());
        registerCommand(new RemoveCommand());
        registerCommand(new WriteCommand());
        registerCommand(new SignCommand());
        registerCommand(new MailCommand());
        registerCommand(new UploadCommand());

        CommandLineParser parser = new GnuParser();

        CommandLine line = null;

        try
        {
            line = parser.parse(context.getOptions(), args);
        }
        catch (ParseException pe)
        {
            System.err.println("Command line error: " + pe.getMessage());
            System.exit(0);
        }

        processOptions(line);
    }

    private void registerCommand(final Command command)
    {
        context.addCommand(command);
    }

    private void processOptions(CommandLine line)
    {
        for (Command command : context.getCommands())
        {
            if (command.hasCommandLineOption())
            {
                command.processCommandLineOption(line);
            }
        }
    }

    private void run() throws Exception
    {
        Terminal.setupTerminal();
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        List<Completor> completorList = new ArrayList<Completor>();

        for (Command command : context.getCommands())
        {
            if (command.hasCompletor())
            {
                completorList.add(command.getCompletor());
            }
        }

        Completor completor = new MultiCompletor(completorList.toArray(new Completor[completorList.size()]));

        ConsoleReader reader = new ConsoleReader();
        reader.setHistory(new History());
        reader.setUseHistory(true);
        reader.setBellEnabled(false);
        reader.setDefaultPrompt(PGPSigner.APPLICATION_NAME + "> ");
        reader.addCompletor(completor);

        System.out.println("\n\nWelcome to " + APPLICATION_VERSION);

        String line = null;

        commandLoop: while (!context.isShutdown() && (line = reader.readLine()) != null)
        {
            String[] cmd = StringUtils.stripAll(StringUtils.split(line));
            if (cmd.length > 0)
            {

                for (Command command : context.getCommands())
                {
                    if (command.matchInteractiveCommand(cmd[0]))
                    {
                        if (command.prerequisiteInteractiveCommand(cmd))
                        {
                            command.executeInteractiveCommand(cmd);
                        }
                        continue commandLoop;
                    }
                }
                System.out.println("Unknown Command: " + cmd[0]);
            }
        }

        Runtime.getRuntime().removeShutdownHook(shutdownHook);
        System.out.println("Goodbye");
    }

}
