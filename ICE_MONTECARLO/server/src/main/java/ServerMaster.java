import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ServerMaster {
    public static void main(String[] args) {

        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {


            ObjectAdapter adapter = communicator.createObjectAdapter("MasterAdapter");


            Gson gson = new Gson();


            MasterI master = new MasterI();


            adapter.add(master, Util.stringToIdentity("Master"));


            adapter.activate();

            System.out.println("ServerMaster is running...");


            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String msg;

            while ((msg = reader.readLine()) != null) {

                String[] command = msg.split("::");
                if (command.length == 2) {

                    master.processCommand(command[0], command[1]);
                } else {
                    System.out.println("Invalid command format. Use: Command::Message");
                }
            }

            // El servidor espera conexiones hasta que se apague
            communicator.waitForShutdown();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
