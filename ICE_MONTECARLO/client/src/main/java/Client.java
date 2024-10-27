import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import MontCarloPiEstimation.MasterPrx;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Client {
    /**
     * The main function initializes communication, interacts with a Master object to estimate Pi based
     * on user input, and saves the results to a CSV file.
     */
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
           // The line `MasterPrx master =
           // MasterPrx.checkedCast(communicator.stringToProxy("Master:default -p 5000 -h
           // 192.168.212.66"));` in the Java code snippet is creating a proxy object `master` for the
           // `Master` interface using Ice, which is a middleware for developing distributed
           // applications.
            MasterPrx master = MasterPrx.checkedCast(
                    communicator.stringToProxy("Master:default -p 5000 -h 192.168.212.66")
            );

            // The `if (master == null)` block in the Java code snippet is checking if the proxy object
            // `master` for the `Master` interface is `null`. If the `master` object is `null`, it
            // means that the attempt to create a proxy for the `Master` interface using Ice
            // communication framework was unsuccessful.
            if (master == null) {
                throw new Error("Proxy inválido para Master");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Preguntar al usuario por la opción deseada
            System.out.println("Elige una opción:");
            System.out.println("1. Introducir el número de puntos manualmente.");
            System.out.println("2. Ejecutar 10 casos con puntos desde 10^1 hasta 10^15.");

            String option = reader.readLine();

            // This part of the code in the `Client` class is responsible for handling user input based
            // on the selected option:
            if (option.equals("1")) {
                // Option 1: Input total points manually
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
            } else if (option.equals("2")) {
                // Option 2: Execute 10 cases with points from 10^1 to 10^15
                for (int i = 1; i <= 15; i++) {
                    int totalPoints = (int) Math.pow(10, i);

                    long startTime = System.nanoTime();
                    float piEstimate = master.estimatePi(totalPoints);

                    long endTime = System.nanoTime();
                    long duration = endTime - startTime;

                    System.out.println("Caso " + i + ": Puntos usados = " + totalPoints);
                    System.out.println("Estimación de Pi = " + piEstimate);
                    System.out.println("Tiempo total: " + duration / 1_000_000_000.0 + " segundos");

                    // Save data to CSV file
                    saveData(totalPoints, piEstimate, duration);
                }
            } else {
                System.out.println("Opción inválida.");
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The `saveData` function saves total points, Pi estimate, and duration to a CSV file in a
     * specified folder.
     * 
     * @param totalPoints The `totalPoints` parameter in the `saveData` method represents the total
     * number of points used in estimating the value of pi. This value is typically generated randomly
     * within a specified range and is crucial for calculating the pi estimate accurately using methods
     * like Monte Carlo simulation.
     * @param piEstimate The `piEstimate` parameter in the `saveData` method represents an estimation
     * of the mathematical constant Pi (π). It is a floating-point number that is calculated during the
     * execution of the program, typically using a Monte Carlo method or another numerical technique to
     * approximate the value of Pi. In the
     * @param duration The `duration` parameter in the `saveData` method represents the time duration
     * in nanoseconds. This value is used to track how long a certain operation took to complete, such
     * as calculating an estimation of pi. The duration is then saved along with other data like the
     * total points and the pi estimate
     */
    private static void saveData(int totalPoints, float piEstimate, long duration) {
        String folderName = "resultado_pi";  // create file
        String fileName = "estimacion_pi.csv";  // Name file csv

        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Path complete file csv
        String filePath = folderName + "/" + fileName;

        // Verifiy file csv exists
        File csvFile = new File(filePath);
        boolean fileExists = csvFile.exists();

        try (FileWriter writer = new FileWriter(csvFile, true);
             PrintWriter printWriter = new PrintWriter(writer)) {

            // if the file does not exist, write the header
            if (!fileExists) {
                printWriter.println("TotalPoints,PiEstimate,Duration(ns)");
            }

            // Write data to the file in a new line
            printWriter.println(totalPoints + "," + piEstimate + "," + duration);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
