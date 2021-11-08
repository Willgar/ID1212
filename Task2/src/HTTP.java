import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Random;

import static java.lang.System.in;

public class HTTP {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8282);
        HTTPServer[] servers = new HTTPServer[100];
        int clientID = 0;
        try {
            while (true) {
                System.out.println("Waiting for Client to connect");
                Socket socket = serverSocket.accept();
                int[] cookieID = checkRequest(socket);
                System.out.println("Connection Receieved with socket: " + socket);
                if(servers[cookieID[0]] != null && cookieID[0] != 99) {
                    servers[cookieID[0]].guess(cookieID[1]);
                } else {
                    HTTPServer newServer = new HTTPServer(socket, clientID);
                    servers[clientID] = newServer;
                    servers[clientID].start();
                    clientID++;
                }
            }
        } catch(IOException err) {
            System.out.println(err);
        }
    }

    static int[] checkRequest(Socket socket) throws IOException {
        int[] returnArray = {99,-1};
        boolean flag = true;
        boolean flag2 = false;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String msg ="";
        while ((msg = buffer.readLine())!= null) {
            if(msg.contains("Accept-Language")){//nice hardcode
                msg = buffer.readLine();
                flag2 = true;
            }
            if(msg.contains("Cookie")){
                msg = msg.split("=")[1];
                msg = msg.split(" ")[0];
                System.out.println("COOKIE: "+ msg);
                returnArray[0] = Integer.parseInt(msg);
            }
            if(msg.contains("guess")&&flag){
                msg = msg.split("=")[1];
                msg = msg.split(" ")[0];
                System.out.println("GUESS: "+ msg);
                flag=false;
                returnArray[1] = Integer.parseInt(msg);
            }
            if((returnArray[0] != 99 && returnArray[1] != -1) || flag2){
                return returnArray;
            }
        }
        return returnArray;
    }
    static int getGuess(Socket socket) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String msg ="";
        while ((msg = buffer.readLine())!= null) {
            if(msg.contains("guess")){
                msg = msg.split("=")[1];
                msg = msg.split(" ")[0];
                System.out.println(msg);
                return Integer.parseInt(msg);
            }
        }
        System.out.println("no guess");
        return 0;
    }
}

class HTTPServer extends Thread {
    Socket socket;
    int clientID;
    int correctValue;
    int guesses;
    PrintWriter output;

    public HTTPServer(Socket socket, int clientID) throws IOException {
        this.socket = socket;
        this.clientID = clientID;
        this.guesses = 0;
        Random rand = new Random();
        this.correctValue = rand.nextInt(100);

    }

    public void run() {
        try {
            sendStartPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void guess(int guess) throws IOException {
        if(guess==correctValue){
            sendWinPage();
        } else if(guess < correctValue){
            sendHigherPage();
        } else{
            sendLowerPage();
        }
    }
    private void sendStartPage() throws IOException {
        output = new PrintWriter(socket.getOutputStream());
        output.println("HTTP/1.1 200 OK");
        output.println("Set-Cookie: clientId=" + clientID + " expires=Wednesday,24-Dec-21 23:59:59 GMT");
        output.println("Content-Type: text/html");
        output.println("Connection: closed");
        output.println("\r\n");
        output.println("<p> Welcome to the guessing game </p>");
        output.println("<p> guess between 1 and 100 </p>");
        output.println("<p> Guess </p>");
        //output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input onClick=\" window.location.href = \"http://localhost:8282/?guess=\"document.getElementById(\"guess\")\" type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
        output.println("""
                <form action="" method="post">
                    <label for="guess"> guess number 
                        <input type="text" id="guess" name="guess"> 
                    </label>
                </form>
                """);
        output.println();
        output.flush();
        System.out.println("Game Started");
    }
    private void sendWinPage() throws IOException {
        output = new PrintWriter(socket.getOutputStream());
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html");
        output.println("\r\n");
        output.println("<p> You won. You have made " + guesses + " guesses </p>");
        output.println("<p> Guess </p>");
        output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
        output.println();
        output.flush();
        System.out.println("Game Won");

    }
    private void sendHigherPage() throws IOException {
        output = new PrintWriter(socket.getOutputStream());
        output.println("HTTP/1.1 200 OK");
        output.println("Connection: closed");
        output.println("Content-Type: text/html");
        output.println("\r\n");
        output.println("<p> Nope, guess higher. You have made " + ++guesses + " guesses so far </p>");
        output.println("<p> Guess </p>");
        output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
        output.println();
        output.flush();
        System.out.println("Guess Higher");

    }
    private void sendLowerPage() throws IOException {
        output = new PrintWriter(socket.getOutputStream());
        output.println("HTTP/1.1 200 OK");
        output.println("Connection: closed");
        output.println("Content-Type: text/html");
        output.println("\r\n");
        output.println("<p> Nope, guess lower. You have made " + ++guesses + " guesses so far </p>");
        output.println("<p> Guess </p>");
        output.println("""
                        <p> Guess </p>
                       """);
        output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
        output.println();
        output.flush();
        System.out.println("Guess Lower");

    }
}
