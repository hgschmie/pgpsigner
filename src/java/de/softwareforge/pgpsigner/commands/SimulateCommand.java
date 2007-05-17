package de.softwareforge.pgpsigner.commands;

public class SimulateCommand extends AbstractCommand implements Command
{

    public SimulateCommand()
    {
    }

    public String getName()
    {
        return "simulate";
    }

    public String getHelp()
    {
        return "toggle simulation flag";
    }

    public void executeInteractiveCommand(final String[] args)
    {
        getContext().toggleSimulation();

        System.out.println(getContext().isSimulation() ? "Running in simulation mode" : "Running in life mode");
    }
}
