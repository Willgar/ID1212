import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;

public class Chat {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt("4321");                    //The main server goes with the port 4321
        ServerSocket serverSocket = new ServerSocket(port);
        Socket[] socketArray = new Socket[100];                     //To keep track of all Connections.
        int j = 0;
        try {
            while (true) {
                System.out.println("Waiting for Client to connect");
                Socket socket = serverSocket.accept();
                socketArray[j++] = socket;
                System.out.println("Connection Receieved with socket: " + socket);
                new ChatServer(socket, socketArray).start();
            }
        } catch(IOException err) {
            System.out.println(err);
        }
    }

}

class ChatServer extends Thread {
    Socket socket;
    Socket[] socketArray;
    public ChatServer(Socket socket, Socket[] socketArray){
        this.socket = socket;
        this.socketArray = socketArray;
    }
    private void sendOutput(String msg) throws IOException {                //Sends output to all open socket connections.
        for(int i=0; i<socketArray.length; i++){
            try {
                PrintWriter writer = new PrintWriter(socketArray[i].getOutputStream());
                writer.println(msg);
                writer.flush();
            } catch(Exception e){};
        }
    }
    public void run(){
        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg="";
            while ((msg = buffer.readLine())!= null) {
                sendOutput(msg);
                System.out.println("client: " + msg);
            }
            socket.close();
            System.out.println("Closing socket: " + socket);
            return;
        } catch (IOException err){
            System.out.println(socket + " lost connection");
            return;
        }
    }
}

class ChatClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 4321);
        PrintWriter output = new PrintWriter(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        new ChatClientListener(socket).start();
        System.out.println("Write to other users.\nWrite QUIT to exit.");
        String send = scanner.nextLine();
        while(!send.equals("QUIT")){
            output.println(send);
            output.flush();
            send = scanner.nextLine();
        }
        output.println(socket + " has left the chat");
        output.flush();
        socket.close();
        System.out.println("Closing Client");
        System.exit(0);
    }
}

class ChatClientListener extends Thread {
    Socket socket;
    public ChatClientListener(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try{
            BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String msg="";
            while ((msg = buffer.readLine())!= null) {
                System.out.println("client: " + msg);
            }
        } catch (Exception err) {
            System.out.println("Client lost connection");
            System.exit(0);
        }

    }
}
/*
When the ChatClient starts, it sends traffic first with a SYN flag marked, which is followed by traffic back with SYN ACK
to acknowledge that it is synchronized, and a last handshake with ACK, IE ChatClient and ChatServer is now connected via socket and a 3-way handshake.
Then when sending data from the client to the server, we get a PSH ACK that shows that it has pushed data and acknowledged it
which is followed by an ACK back.
Then when a connection closes it sends a FIN, and gets a FIN ACK back followed by a ACK to acknowledge, so a 3 way handshake to close.
 */