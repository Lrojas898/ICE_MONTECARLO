
import MontCarloPiEstimation.Master;
import MontCarloPiEstimation.WorkerPrx;
import com.zeroc.Ice.Current;
import java.util.HashMap;
import java.util.Map;

public class MasterI implements Master {
    // The line `private Map<String, WorkerPrx> workers;` in the Java code snippet
    // is declaring a private
    // instance variable named `workers` of type `Map<String, WorkerPrx>`. This
    // variable is used to store a
    // mapping of worker names (String) to corresponding worker proxies (`WorkerPrx`
    // objects). It is
    // initialized as an empty `HashMap` in the constructor `MasterI()`. The
    // `workers` map is used to keep
    // track of registered workers in the system, allowing the Master to interact
    // with them for estimating
    // the value of Pi using a Monte Carlo simulation.
    private Map<String, WorkerPrx> workers;

    // The `public MasterI() { workers = new HashMap<>(); }` method is a constructor for the `MasterI`
    // class in Java. When an instance of the `MasterI` class is created, this constructor is called to
    // initialize the object.
    public MasterI() {
        workers = new HashMap<>();
    }

    /**
     * The function `estimatePi` calculates an estimate of the value of Pi using a Monte Carlo method
     * with multiple workers processing points.
     * 
     * @param totalPoints The `totalPoints` parameter represents the total number of points that will
     * be used to estimate the value of Pi using a Monte Carlo method. This method involves generating
     * random points within a square and determining how many fall within a quarter circle inscribed
     * within the square.
     * @param current The `estimatePi` method you provided is used to estimate the value of Pi using a
     * Monte Carlo simulation with multiple workers. The method divides the total number of points to
     * be generated among the available workers, calculates the number of points each worker should
     * process, and then aggregates the results to estimate Pi.
     * @return The method `estimatePi` is returning an estimate of the mathematical constant Pi (π)
     * based on the total number of points and the points calculated by each worker. The estimate is
     * calculated as 4 times the total number of points inside a circle (totalInside) divided by the
     * total number of points generated (totalPoints).
     */
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
            if (remainder > 0)
                remainder--;

            try {
                int inside = worker.calculatePoints(points);
                totalInside += inside;
                System.out.println("Trabajador " + name + " proceso " + points + " puntos, dentro: " + inside);
            } catch (Exception e) {
                System.out.println("Error al obtener resultado del trabajador " + name + ": " + e.getMessage());
            }
        }

        float piEstimate = 4.0f * totalInside / totalPoints;
        return piEstimate;
    }

    /**
     * The `registerWorker` method registers a worker with a given name and prints a success message if
     * the worker is not null, otherwise it prints an error message.
     * 
     * @param name The `name` parameter is a `String` representing the name of the worker being
     * registered.
     * @param worker The `worker` parameter in the `registerWorker` method is of type `WorkerPrx`,
     * which is likely a proxy object representing a worker in a distributed system. This parameter is
     * used to associate a worker with a name in a map called `workers`.
     * @param current The `current` parameter in the `registerWorker` method is an Ice `Current`
     * object. It provides information about the current request being processed by the Ice server,
     * such as the operation being invoked, the connection details, and other contextual information.
     * It is typically used for advanced server-side processing and
     */
    @Override
    public void registerWorker(String name, WorkerPrx worker, Current current) {
        if (worker != null) {
            workers.put(name, worker);
            System.out.println("Trabajador registrado: " + name);
        } else {
            System.out.println("Error al registrar el trabajador: " + name);
        }
    }

    /**
     * The `unregisterWorker` method removes a worker from a collection and prints a message indicating
     * the worker has been unregistered.
     * 
     * @param name The `name` parameter in the `unregisterWorker` method represents the name of the
     * worker that you want to unregister from the system.
     * @param current The `current` parameter in the `unregisterWorker` method is of type `Current`.
     * This parameter typically represents the current request context or state within the application.
     * It is often used in Ice middleware for communication between client and server components.
     */
    @Override
    public void unregisterWorker(String name, Current current) {
        workers.remove(name);
        System.out.println("Trabajador desregistrado: " + name);
    }

    /**
     * The `estimatePi` function calculates an estimate of the value of Pi using a Monte Carlo method
     * with multiple workers processing points.
     * 
     * @param totalPoints The `totalPoints` parameter represents the total number of points that will
     * be used to estimate the value of Pi using a Monte Carlo method. This method involves generating
     * random points within a square and determining how many fall within a quarter circle inscribed
     * within the square. The ratio of points inside the quarter circle
     * @return The method `estimatePi` returns an estimate of the mathematical constant Pi (π) based on
     * the total number of points calculated by the workers.
     */
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
            if (remainder > 0)
                remainder--;

            try {
                int inside = worker.calculatePoints(points);
                totalInside += inside;
                System.out.println("Trabajador " + name + " proceso " + points + " puntos, dentro: " + inside);
            } catch (Exception e) {
                System.out.println("Error al obtener resultado del trabajador " + name + ": " + e.getMessage());
            }
        }

        float piEstimate = 4.0f * totalInside / totalPoints;
        return piEstimate;
    }
}
