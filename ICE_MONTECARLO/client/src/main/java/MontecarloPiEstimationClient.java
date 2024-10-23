import MontCarloPiEstimation.*;
import com.zeroc.Ice.*;

public class MontecarloPiEstimationClient {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args)) {
            // Obtener el proxy del maestro
            ObjectPrx base = communicator.stringToProxy("master:default -p 10000");
            MasterPrx master = MasterPrx.checkedCast(base);

            if (master == null) {
                throw new Error("Invalid proxy");
            }

            // Pedir la estimación de Pi con N puntos y n trabajadores
            int totalPoints = 1000000;
            int numWorkers = 4; // Número de trabajadores
            float piEstimate = master.estimatePi(totalPoints, numWorkers);
            System.out.println("Estimación de Pi: " + piEstimate);
        }
    }
}
