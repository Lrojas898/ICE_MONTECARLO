import com.zeroc.Ice.*;
import java.io.*;

public class MontecarloPiEstimationClient {

    public static void main(String[] args) {

        ///////////////////////////////////////////
        //  La conexión general con Ice en Java  //
        ///////////////////////////////////////////
        try(Communicator communicator = Util.initialize(args, "properties.cfg") ) {

            System.out.println("Give me a name: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String name = reader.readLine();
            reader.close();


            //ObjectAdapter adapter = communicator.createObjectAdapter("Suscriber");

            adapter.activate();


            communicator.waitForShutdown();

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
