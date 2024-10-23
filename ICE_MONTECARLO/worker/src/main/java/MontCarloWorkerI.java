package MontCarloPiEstimation;

import MontCarloPiEstimation.Worker;
import com.zeroc.Ice.Current;
import java.util.Random;

public class MontCarloWorkerI implements Worker {
    private Random random;

    public MontCarloWorkerI() {
        this.random = new Random();
    }

    @Override
    public int calculatePoints(int totalPoints, Current current) {
        int insideCircle = 0;
        for (int i = 0; i < totalPoints; i++) {
            double x = random.nextDouble() * 2 - 1; // Genera un número entre -1 y 1
            double y = random.nextDouble() * 2 - 1; // Genera un número entre -1 y 1
            if (x * x + y * y <= 1) {
                insideCircle++;
            }
        }
        return insideCircle;
    }
}
