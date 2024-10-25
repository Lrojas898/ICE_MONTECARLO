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

            // Preguntar al usuario por la opción deseada
            System.out.println("Elige una opción:");
            System.out.println("1. Introducir el número de puntos manualmente.");
            System.out.println("2. Ejecutar 10 casos con puntos desde 10^1 hasta 10^15.");

            String option = reader.readLine();

            if (option.equals("1")) {
                // Opción 1: Introducir número de puntos manualmente
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
                // Opción 2: Ejecutar 15 casos desde 10^1 hasta 10^15
                for (int i = 1; i <= 15; i++) {
                    int totalPoints = (int) Math.pow(10, i);

                    long startTime = System.nanoTime();
                    float piEstimate = master.estimatePi(totalPoints);

                    long endTime = System.nanoTime();
                    long duration = endTime - startTime;

                    System.out.println("Caso " + i + ": Puntos usados = " + totalPoints);
                    System.out.println("Estimación de Pi = " + piEstimate);
                    System.out.println("Tiempo total: " + duration / 1_000_000_000.0 + " segundos");

                    // Guardar los datos de cada caso en el archivo CSV
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

    // Método para crear carpeta y archivo CSV, y guardar los datos
    private static void saveData(int totalPoints, float piEstimate, long duration) {
        String folderName = "resultado_pi";  // Carpeta a crear
        String fileName = "estimacion_pi.csv";  // Nombre del archivo CSV

        // Verificar si la carpeta existe, y crearla si no existe
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Ruta completa del archivo CSV
        String filePath = folderName + "/" + fileName;

        // Verificar si el archivo CSV existe
        File csvFile = new File(filePath);
        boolean fileExists = csvFile.exists();

        try (FileWriter writer = new FileWriter(csvFile, true);
             PrintWriter printWriter = new PrintWriter(writer)) {

            // Si el archivo no existía, escribir la cabecera
            if (!fileExists) {
                printWriter.println("TotalPoints,PiEstimate,Duration(ns)");
            }

            // Escribir los datos en una nueva fila
            printWriter.println(totalPoints + "," + piEstimate + "," + duration);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}