import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import MontCarloPiEstimation.MasterPrx;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;

public class Client {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
            // Obtener el proxy del maestro
            MasterPrx master = MasterPrx.checkedCast(
                    communicator.stringToProxy("Master:default -p 5000 -h localhost")
            );

            if (master == null) {
                throw new Error("Proxy inválido para Master");
            }


            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Introduce el número de puntos para estimar Pi:");
            String input = reader.readLine();
            try {
                int totalPoints = Integer.parseInt(input);

                long startTime = System.nanoTime();
                float piEstimate = master.estimatePi(totalPoints);

                long endTime = System.nanoTime();
                long duration = endTime - startTime;

                System.out.println("Estimación de Pi = " + piEstimate);
                System.out.println("Puntos usados: " + totalPoints);
                System.out.println("Tiempo total: " + duration / 1_000_000_000.0 + " segundos");

                saveData(totalPoints, piEstimate, duration);

            } catch (NumberFormatException e) {
                System.out.println("Formato de número inválido.");
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveData(int totalPoints, float piEstimate, long duration) {
        String folderName = "resultado_pi";
        String fileName = "estimacion_pi.csv";

        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = folderName + "/" + fileName;

        File csvFile = new File(filePath);
        boolean fileExists = csvFile.exists();

        try (FileWriter writer = new FileWriter(csvFile, true);
             PrintWriter printWriter = new PrintWriter(writer)) {

            if (!fileExists) {
                printWriter.println("TotalPoints,PiEstimate,Duration(ns)");
            }

            printWriter.println(totalPoints + "," + piEstimate + "," + duration);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
