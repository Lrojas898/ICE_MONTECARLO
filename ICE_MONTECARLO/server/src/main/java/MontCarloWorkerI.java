import MontCarloPiEstimation.*;
import com.zeroc.Ice.Current;

import java.util.Random;

public class MontCarloWorkerI implements Worker {


    @Override
    public int calculatePoints(int totalPoints, Current current) {
        int pointsInCircle = 0;
        Random rand = new Random();

        for (int i = 0; i < totalPoints; i++) {
            // Generar coordenadas aleatorias (x, y) entre -1 y 1
            double x = rand.nextDouble() * 2 - 1;
            double y = rand.nextDouble() * 2 - 1;

            // Verificar si el punto está dentro del círculo
            if (x * x + y * y <= 1) {
                pointsInCircle++;
            }
        }

        return pointsInCircle;
    }
}
