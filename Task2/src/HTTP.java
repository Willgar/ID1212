import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTP {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        try {
            while (true) {
                System.out.println("Waiting for Client to connect");
                Socket socket = serverSocket.accept();
                System.out.println("Connection Receieved with socket: " + socket);
                new HTTPServer(socket).start();
            }
        } catch(IOException err) {
            System.out.println(err);
        }
    }
}

class HTTPServer extends Thread {
    Socket socket;
    public HTTPServer(Socket socket){
        this.socket = socket;
    }

    public void run() {

    }
}
