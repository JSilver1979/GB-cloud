import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        try(ServerSocket server = new ServerSocket(8189)) {
            System.out.println("Server started");
            while(true) {
                Socket socket = server.accept();
                ServerHandler serverHandler = new ServerHandler(socket);
                new Thread(serverHandler).start();
            }
        }
    }
}
