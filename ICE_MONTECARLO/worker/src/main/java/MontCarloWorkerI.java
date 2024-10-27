
import MontCarloPiEstimation.Worker;
import com.zeroc.Ice.Current;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MontCarloWorkerI implements Worker {

    // The line `private static final int NUM_THREADS = 3;` in the
    // `MontCarloWorkerI` class is declaring a
    // constant integer variable named `NUM_THREADS` with a value of 3. This
    // constant represents the number
    // of threads that will be used in the fixed thread pool for executing tasks in
    // parallel. By setting
    // this value as a constant, the code ensures that the number of threads remains
    // consistent throughout
    // the execution of the program.
    private static final int NUM_THREADS = 3;

    private final ExecutorService executor;

    // The `public MontCarloWorkerI()` constructor in the `MontCarloWorkerI` class
    // is initializing the
    // `executor` field with a new fixed thread pool created using
    // `Executors.newFixedThreadPool(NUM_THREADS)`.
    public MontCarloWorkerI() {
        this.executor = Executors.newFixedThreadPool(NUM_THREADS);
    }

    /**
     * The function calculates the number of points falling inside a circle using
     * multiple threads in
     * Java.
     * 
     * @param totalPoints Total number of points to be calculated for determining if
     *                    they fall inside a
     *                    circle.
     * @param current     The `current` parameter in the `calculatePoints` method is
     *                    of type `Current`. It
     *                    seems like the `Current` class is not defined in the
     *                    provided code snippet. Could you please
     *                    provide the definition or details of the `Current` class
     *                    so that I can assist you further with
     *                    its usage in
     * @return The method `calculatePoints` returns the total number of points that
     *         fall inside a
     *         circle of radius 1, given the total number of points and the number
     *         of threads used for
     *         calculation.
     */
    @Override
    public int calculatePoints(int totalPoints, Current current) {
        if (totalPoints <= 0) {
            return 0;
        }

        int pointsPerThread = totalPoints / NUM_THREADS;
        int remainder = totalPoints % NUM_THREADS;

        List<Callable<Integer>> tasks = new ArrayList<>();

        for (int i = 0; i < NUM_THREADS; i++) {
            int points = pointsPerThread + (i < remainder ? 1 : 0);

            tasks.add(() -> {
                int insideCircle = 0;
                ThreadLocalRandom random = ThreadLocalRandom.current();

                for (int j = 0; j < points; j++) {
                    double x = random.nextDouble(-1.0, 1.0);
                    double y = random.nextDouble(-1.0, 1.0);
                    if (x * x + y * y <= 1.0) {
                        insideCircle++;
                    }
                }
                return insideCircle;
            });
        }

        int totalInside = 0;
        try {
            List<Future<Integer>> results = executor.invokeAll(tasks);
            for (Future<Integer> result : results) {
                totalInside += result.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return totalInside;
    }

    /**
     * The `shutdown` function shuts down an `ExecutorService` gracefully, waiting
     * for a specified time
     * before forcefully shutting it down if necessary.
     */
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("ExecutorService no se pudo cerrar correctamente.");
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
