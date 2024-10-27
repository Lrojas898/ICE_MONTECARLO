
import MontCarloPiEstimation.MasterPrx;
import MontCarloPiEstimation.WorkerPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class Worker {

    /**
     * The main function initializes a communication setup, creates a worker object,
     * registers it with
     * a master object, and handles shutdown operations.
     */
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {

            // The line `ObjectAdapter adapter =
            // communicator.createObjectAdapter("WorkerAdapter");` is creating an
            // object adapter named "WorkerAdapter" using the communicator.
            ObjectAdapter adapter = communicator.createObjectAdapter("WorkerAdapter");

            MontCarloWorkerI worker = new MontCarloWorkerI();

            String workerName = System.getProperty("workerName");

            // This code snippet is checking if the `workerName` variable is either `null`
            // or an empty
            // string. If it meets either of these conditions, it generates a new worker
            // name using the
            // pattern "Worker_" followed by a randomly generated UUID (Universally Unique
            // Identifier).
            // This ensures that if the `workerName` is not provided or is empty, a unique
            // identifier
            // is generated for the worker.
            if (workerName == null || workerName.isEmpty()) {
                workerName = "Worker_" + java.util.UUID.randomUUID();
            }

            adapter.add(worker, Util.stringToIdentity(workerName));
            adapter.activate();

            // The line `MasterPrx master =
            // MasterPrx.checkedCast(communicator.stringToProxy("Master:default -p
            // 5000 -h 192.168.212.66"));` is creating a proxy for the `Master` object using
            // Ice communication
            // framework.
            MasterPrx master = MasterPrx
                    .checkedCast(communicator.stringToProxy("Master:default -p 5000 -h 192.168.212.66"));

            if (master == null) {
                throw new Error("Proxy inválido para Master");
            }

            // This code snippet is creating a proxy object for the worker using Ice
            // communication
            // framework. Here's a breakdown of what it's doing:
            WorkerPrx workerPrx = WorkerPrx.checkedCast(adapter.createProxy(Util.stringToIdentity(workerName)));
            if (workerPrx == null) {
                throw new Error("Proxy inválido para Worker");
            }

            // The line `master.registerWorker(workerName, workerPrx);` is registering the
            // worker with the master
            // object. It is passing the worker's name (`workerName`) and a proxy object
            // representing the worker
            // (`workerPrx`) to the `registerWorker` method of the master object. This
            // allows the master to keep
            // track of the worker and communicate with it as needed during the execution of
            // the distributed
            // system.
            master.registerWorker(workerName, workerPrx);

            System.out.println(workerName + " está registrado y en ejecución...");

            // The code `Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            //                 worker.shutdown();
            //                 System.out.println(" se esta apagando...");
            //             }));` is setting up a shutdown hook for the worker process.
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
