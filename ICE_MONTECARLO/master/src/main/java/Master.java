
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import com.google.gson.*;

public class Master {
    /**
     * The main function initializes a communication system, creates a master
     * adapter, listens for
     * commands to estimate Pi, and waits for shutdown.
     */
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
            // The line `ObjectAdapter masterAdapter =
            // communicator.createObjectAdapter("MasterAdapter");` is
            // creating an ObjectAdapter named "MasterAdapter" using the Communicator object
            // `communicator`.
            ObjectAdapter masterAdapter = communicator.createObjectAdapter("MasterAdapter");
            Gson gson = new Gson();
            MasterI masterI = new MasterI();
            masterAdapter.add(masterI, Util.stringToIdentity("Master"));
            masterAdapter.activate();

            System.out.println("Master esta en ejecucion...");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String msg;

            // This part of the code is responsible for reading user input from the console,
            // parsing it
            // to extract the command and its argument, and then executing the corresponding
            // action
            // based on the command.
            while ((msg = reader.readLine()) != null) {
                String[] command = msg.split("::");
                if (command.length == 2) {
                    if (command[0].equalsIgnoreCase("estimatePi")) {
                        try {
                            int totalPoints = Integer.parseInt(command[1]);
                            float pi = masterI.estimatePi(totalPoints);
                            System.out.println("Estimación de Pi = " + pi);
                        } catch (NumberFormatException e) {
                            System.out.println("Formato de numero inválido para totalPoints.");
                        }
                    }
                } else {
                    System.out.println("Formato de comando invalido. Use: estimatePi::<número_de_puntos>");
                }
            }

            // The line `communicator.waitForShutdown();` is causing the program to wait
            // until the Ice communicator
            // is shut down. This method blocks the current thread until the communicator is
            // shut down, allowing
            // the program to gracefully handle the shutdown process before exiting.
            communicator.waitForShutdown();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
