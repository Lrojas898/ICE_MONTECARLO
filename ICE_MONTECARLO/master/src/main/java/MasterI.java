
import MontCarloPiEstimation.Master;
import MontCarloPiEstimation.WorkerPrx;
import com.zeroc.Ice.Current;
import java.util.HashMap;
import java.util.Map;

public class MasterI implements Master {
    private Map<String, WorkerPrx> workers;

    public MasterI() {
        workers = new HashMap<>();
    }

    @Override
    public float estimatePi(int totalPoints, Current current) {
        int n = workers.size();
        if (n == 0) {
            System.out.println("No hay trabajadores disponibles.");
            return 0;
        }

        int pointsPerWorker = totalPoints / n;
        int remainder = totalPoints % n;
        int totalInside = 0;

        for (Map.Entry<String, WorkerPrx> entry : workers.entrySet()) {
            String name = entry.getKey();
            WorkerPrx worker = entry.getValue();
            int points = pointsPerWorker + (remainder > 0 ? 1 : 0);
            if (remainder > 0) remainder--;

            try {
                int inside = worker.calculatePoints(points);
                totalInside += inside;
                System.out.println("Trabajador " + name + " procesó " + points + " puntos, dentro: " + inside);
            } catch (Exception e) {
                System.out.println("Error al obtener resultado del trabajador " + name + ": " + e.getMessage());
            }
        }

        float piEstimate = 4.0f * totalInside / totalPoints;
        return piEstimate;
    }


    @Override
    public void registerWorker(String name, WorkerPrx worker, Current current) {
        if (worker != null) {
            workers.put(name, worker);
            System.out.println("Trabajador registrado: " + name);
        } else {
            System.out.println("Error al registrar el trabajador: " + name);
        }
    }

    @Override
    public void unregisterWorker(String name, Current current) {
        workers.remove(name);
        System.out.println("Trabajador desregistrado: " + name);
    }


    public float estimatePi(int totalPoints) {
        int n = workers.size();
        if (n == 0) {
            System.out.println("No hay trabajadores disponibles.");
            return 0;
        }

        int pointsPerWorker = totalPoints / n;
        int remainder = totalPoints % n;
        int totalInside = 0;

        for (Map.Entry<String, WorkerPrx> entry : workers.entrySet()) {
            String name = entry.getKey();
            WorkerPrx worker = entry.getValue();
            int points = pointsPerWorker + (remainder > 0 ? 1 : 0);
            if (remainder > 0) remainder--;

            try {
                int inside = worker.calculatePoints(points);
                totalInside += inside;
                System.out.println("Trabajador " + name + " procesó " + points + " puntos, dentro: " + inside);
            } catch (Exception e) {
                System.out.println("Error al obtener resultado del trabajador " + name + ": " + e.getMessage());
            }
        }

        float piEstimate = 4.0f * totalInside / totalPoints;
        return piEstimate;
    }
}
