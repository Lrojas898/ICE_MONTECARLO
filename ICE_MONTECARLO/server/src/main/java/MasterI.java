import MontCarloPiEstimation.*;
import com.zeroc.Ice.*;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MasterI implements Master {
    private Communicator communicator;

    public MasterI(Communicator communicator) {
        this.communicator = communicator;
    }

    @Override
    public float estimatePi(int totalPoints, int numWorkers, Current current) {

        int pointsPerWorker = totalPoints / numWorkers;
        List<WorkerPrx> workers = getWorkerProxies(numWorkers);
        int pointsInCircle = 0;

        // Ejecutar llamadas asíncronas a los trabajadores
        ExecutorService executor = Executors.newFixedThreadPool(numWorkers);
        List<Future<Integer>> results = new ArrayList<>();

        for (WorkerPrx worker : workers) {
            Future<Integer> result = executor.submit(() -> worker.calculatePoints(pointsPerWorker));
            results.add(result);
        }

        // Recoger los resultados
        for (Future<Integer> result : results) {
            try {
                pointsInCircle += result.get();  // Sumar puntos dentro del círculo
            } catch (Exception e) {
                System.err.println("Error al obtener resultado del trabajador: " + e);
            }
        }

        // Estimar el valor de Pi
        float piEstimate = 4.0f * pointsInCircle / totalPoints;
        executor.shutdown();
        return piEstimate;
    }

    private List<WorkerPrx> getWorkerProxies(int numWorkers) {
        // Lógica para obtener proxies de los trabajadores
        // Ejemplo básico:
        List<WorkerPrx> workerProxies = new ArrayList<>();
        for (int i = 0; i < numWorkers; i++) {
            WorkerPrx worker = WorkerPrx.checkedCast(
                    communicator.stringToProxy("worker" + i + ":default -h localhost -p 10000")
            );
            workerProxies.add(worker);
        }
        return workerProxies;
    }
}
