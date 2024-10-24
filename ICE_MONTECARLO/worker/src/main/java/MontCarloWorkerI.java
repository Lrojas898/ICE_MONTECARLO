package MontCarloPiEstimation;

import MontCarloPiEstimation.Worker;
import com.zeroc.Ice.Current;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MontCarloWorkerI implements Worker {

    // Número de hilos a utilizar dentro del Worker
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    // ExecutorService para gestionar los hilos
    private final ExecutorService executor;

    public MontCarloWorkerI() {
        this.executor = Executors.newFixedThreadPool(NUM_THREADS);
    }

    @Override
    public int calculatePoints(int totalPoints, Current current) {
        if (totalPoints <= 0) {
            return 0;
        }

        // Tamaño del lote por hilo
        int pointsPerThread = totalPoints / NUM_THREADS;
        int remainder = totalPoints % NUM_THREADS;

        // Lista para almacenar las tareas
        List<Callable<Integer>> tasks = new ArrayList<>();

        for (int i = 0; i < NUM_THREADS; i++) {
            // Ajustar la cantidad de puntos para manejar el resto
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

        // Ejecutar todas las tareas y esperar los resultados
        int totalInside = 0;
        try {
            List<Future<Integer>> results = executor.invokeAll(tasks);
            for (Future<Integer> result : results) {
                totalInside += result.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
        }

        return totalInside;
    }

    // Método para cerrar el ExecutorService
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
