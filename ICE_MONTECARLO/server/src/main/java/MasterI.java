import MontCarloPiEstimation.Master;
import com.zeroc.Ice.Current;

public class MasterI implements Master {
    @Override
    public float estimatePi(int totalPoints, int workers, Current current) {
        return 0;
    }
}
