

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ServerMaster {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
            // Crear y activar el adaptador del maestro
            ObjectAdapter masterAdapter = communicator.createObjectAdapter("MasterAdapter");
            MasterI master = new MasterI();
            masterAdapter.add(master, Util.stringToIdentity("Master"));
            masterAdapter.activate();

            System.out.println("ServerMaster está en ejecución...");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String msg;

            // Escuchar comandos desde la consola para estimar π
            while ((msg = reader.readLine()) != null) {
                String[] command = msg.split("::");
                if (command.length == 2) {
                    if (command[0].equalsIgnoreCase("estimatePi")) {
                        try {
                            int totalPoints = Integer.parseInt(command[1]);
                            float pi = master.estimatePi(totalPoints);
                            System.out.println("Estimación de Pi = " + pi);
                        } catch (NumberFormatException e) {
                            System.out.println("Formato de número inválido para totalPoints.");
                        }
                    }
                } else {
                    System.out.println("Formato de comando inválido. Use: estimatePi::<número_de_puntos>");
                }
            }

            // Esperar hasta que el servidor se apague
            communicator.waitForShutdown();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
