
import MontCarloPiEstimation.MasterPrx;
import MontCarloPiEstimation.MontCarloWorkerI;
import MontCarloPiEstimation.WorkerPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

public class Worker {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "properties.cfg")) {
            // Crear y activar el adaptador del trabajador
            ObjectAdapter adapter = communicator.createObjectAdapter("WorkerAdapter");
            MontCarloWorkerI worker = new MontCarloWorkerI();
            String workerName = "Worker1"; // Cambia esto para múltiples trabajadores

            // Agregar el trabajador al adaptador y obtener su proxy
            ObjectPrx base = adapter.add(worker, Util.stringToIdentity(workerName));
            WorkerPrx workerPrx = WorkerPrx.checkedCast(base); // Aquí se obtiene el proxy

            adapter.activate();

            // Obtener el proxy del maestro
            MasterPrx master = MasterPrx.checkedCast(
                    communicator.stringToProxy("Master:default -p 5000 -h 192.168.212.66"));

            if (master == null) {
                throw new Error("Proxy inválido para Master");
            }

            // Registrar el trabajador con el maestro
            master.registerWorker(workerName, workerPrx);

            System.out.println(workerName + " está registrado y en ejecución...");

            // Esperar hasta que el trabajador se apague
            communicator.waitForShutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
