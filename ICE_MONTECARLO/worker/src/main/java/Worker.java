
import MontCarloPiEstimation.MasterPrx;
import MontCarloPiEstimation.MontCarloWorkerI;
import MontCarloPiEstimation.WorkerPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class Worker {

    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
            // Crear y activar el adaptador del trabajador
            ObjectAdapter adapter = communicator.createObjectAdapter("WorkerAdapter");

            MontCarloWorkerI worker = new MontCarloWorkerI();

            // Asignar un nombre único para cada Worker
            String workerName = System.getProperty("workerName"); // Leer de los argumentos o propiedades del sistema

            if (workerName == null || workerName.isEmpty()) {
                workerName = "Worker_" + java.util.UUID.randomUUID(); // Generar un nombre único si no se especifica
            }

            adapter.add(worker, Util.stringToIdentity(workerName));
            adapter.activate();

            // Obtener el proxy del maestro
            MasterPrx master = MasterPrx.checkedCast(
                    communicator.stringToProxy("Master:default -p 5000 -h 192.168.212.66")
            );

            if (master == null) {
                throw new Error("Proxy inválido para Master");
            }

            WorkerPrx workerPrx = WorkerPrx.checkedCast(adapter.createProxy(Util.stringToIdentity(workerName)));
            if (workerPrx == null) {
                throw new Error("Proxy inválido para Worker");
            }

            // Registrar el trabajador con el maestro
            master.registerWorker(workerName, workerPrx);

            System.out.println(workerName + " está registrado y en ejecución...");

            // Añadir un shutdown hook para cerrar el ExecutorService correctamente
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                worker.shutdown();
                System.out.println(" se esta apagando...");
            }));

            // Esperar hasta que el trabajador se apague
            communicator.waitForShutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
