
import MontCarloPiEstimation.MasterPrx;
import MontCarloPiEstimation.WorkerPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class Worker {

    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

            ObjectAdapter adapter = communicator.createObjectAdapter("WorkerAdapter");

            MontCarloWorkerI worker = new MontCarloWorkerI();

            String workerName = System.getProperty("workerName"); // Leer de los argumentos o propiedades del sistema

            if (workerName == null || workerName.isEmpty()) {
                workerName = "Worker_" + java.util.UUID.randomUUID(); // Generar un nombre único si no se especifica
            }

            adapter.add(worker, Util.stringToIdentity(workerName));
            adapter.activate();

            MasterPrx master = MasterPrx.checkedCast(
                    communicator.stringToProxy("Master:default -p 5000 -h localhost")
            );

            if (master == null) {
                throw new Error("Proxy inválido para Master");
            }

            WorkerPrx workerPrx = WorkerPrx.checkedCast(adapter.createProxy(Util.stringToIdentity(workerName)));
            if (workerPrx == null) {
                throw new Error("Proxy inválido para Worker");
            }

            master.registerWorker(workerName, workerPrx);

            System.out.println(workerName + " está registrado y en ejecución...");

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
