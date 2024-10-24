import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import MontCarloPiEstimation.MasterPrx;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
            // Obtener el proxy del maestro
            MasterPrx master = MasterPrx.checkedCast(
                    communicator.stringToProxy("Master:default -p 5000 -h 192.168.212.66")
            );

            if (master == null) {
                throw new Error("Proxy inválido para Master");
            }

            long startTime = System.currentTimeMillis();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Introduce el número de puntos para estimar Pi:");
            String input = reader.readLine();
            try {
                int totalPoints = Integer.parseInt(input);
                float piEstimate = master.estimatePi(totalPoints);
                System.out.println("Estimación de Pi = " + piEstimate);
            } catch (NumberFormatException e) {
                System.out.println("Formato de número inválido.");
            }

            reader.close();

            System.out.println("Puntos usados: " + input);
            System.out.println("Tiempo total: " + (System.currentTimeMillis() - startTime)/1000 + " segundos");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}