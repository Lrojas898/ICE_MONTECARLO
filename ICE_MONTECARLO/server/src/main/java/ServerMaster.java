import com.zeroc.Ice.*;

public class ServerMaster {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args)) {
            // Crear el adaptador para el objeto Master
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("MasterAdapter", "default -p 10000");

            // Añadir el objeto MasterI al adaptador, sin necesidad de cast a Object
            adapter.add(new MasterI(communicator), Util.stringToIdentity("master"));

            // Activar el adaptador para comenzar a recibir solicitudes
            adapter.activate();
            System.out.println("Servidor Master iniciado.");

            // Esperar hasta que el servidor sea apagado
            communicator.waitForShutdown();
        }
    }
}
