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
                System.out.println("Waiting for server");
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
            socketArray[i].getOutputStream().write(convToByte(msg));;
        }
    }
    public void run(){
        try {
            InputStream input = socket.getInputStream();
            socket.setSoTimeout(20000);         //Closes the socket after 20 seconds of inactivity.
            int byteLen = 0;
            byte[] inputBuffer = new byte[1024];
            while(socket != null){              //Reads the inputstreams buffer for messages from the client, converts it to text, and sends it to all other clients.
                try {                           //This method of reading input and sending output is inspired from previous course work from IK1203, and adapted for ID1212.
                    byteLen = input.read(inputBuffer);
                    String msg = convToString(inputBuffer, byteLen);
                    if(byteLen != -1) {
                        System.out.println("server:" + msg);
                        sendOutput(msg);
                    }
                } catch (Exception e) {
                    byteLen = -1;       //Interrupts the loop.
                }
            }
            socket.close();
            System.out.println("Closing socket: " + socket);
            return;
        } catch (IOException err){
            System.out.println(err);
            return;
        }


    }
    private static String convToString(byte[] b, int len) throws UnsupportedEncodingException { //Inspired from IK1203 course work
        return new String(b, 0, len, "UTF-8");
    }
    private static byte[] convToByte(String text) throws UnsupportedEncodingException { //Inspired from IK1203 course work
        return (text).getBytes("UTF-8");
    }

}

class ChatClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 4321);
        OutputStream output = socket.getOutputStream();
        Scanner scanner = new Scanner(System.in);
        new ChatClientListener(socket).start();
        String send = scanner.next();
        while(!send.equals("QUIT")){
            System.out.println("sender:" + send);
            output.write(convToByte(send));
            send = scanner.next();
        }
        socket.close();
        System.out.println("Closing Client");
        System.exit(0);
    }
    private static byte[] convToByte(String text) throws UnsupportedEncodingException { //Inspired from IK1203 course work
        return (text).getBytes("UTF-8");
    }
}

class ChatClientListener extends Thread {
    Socket socket;
    public ChatClientListener(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try{
            InputStream input = socket.getInputStream();
            StringBuilder sb = new StringBuilder();
            socket.setSoTimeout(20000);
            int byteLen = 0;
            byte[] inputBuffer = new byte[1024];
            while(socket != null){
                try {
                    byteLen = input.read(inputBuffer);
                    String msg = convToString(inputBuffer, byteLen);

                    if(byteLen != -1) {
                        System.out.println("listener:" + msg);
                        sb.append(msg);
                    }
                } catch (Exception e) {
                    byteLen = -1;
                }
            }

        } catch (Exception err) {
            System.out.println(err);
        }

    }
    private static String convToString(byte[] b, int len) throws UnsupportedEncodingException { //Inspired from IK1203 course work
        return new String(b, 0, len, "UTF-8");
    }
}
/*
When the ChatClient starts, it sends traffic first with a SYN flag marked, which is followed by traffic back with SYN ACK
to acknowledge that it is synchronized, and a last handshake with ACK, IE ChatClient and ChatServer is now connected via socket and a 3-way handshake.
Then when sending data from the client to the server, we get a PSH ACK that shows that it has pushed data and acknowledged it
which is followed by an ACK back.
Then when a connection closes it sends a FIN, and gets a FIN ACK back followed by a ACK to acknowledge, so a 3 way handshake to close.
 */