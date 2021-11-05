import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class HTTP {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8282);

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
        PrintWriter output = null;
        Random rand = new Random();
        try {
            output = new PrintWriter(socket.getOutputStream());
            System.out.println("hello");
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html");
            output.println("Set-Cookie: clientId=" + rand.nextInt(100) + " expires=Wednesday,24-Dec-21 23:59:59 GMT");
            output.println();
            output.flush();
            System.out.println("hello2");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
