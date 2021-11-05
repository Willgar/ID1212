import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
                if(servers[cookieID[0]] != null) {
                    //servers[cookieID].guess(cookieID[1]);
                    System.out.println("it worked"+ cookieID);
                } else {
                    System.out.println("test");
                    HTTPServer newServer = new HTTPServer(socket, clientID);
                    servers[clientID] = newServer;
                    //servers[clientID].start();
                    newServer.start();
                    clientID++;
                }
            }
        } catch(IOException err) {
            System.out.println(err);
        }
    }

    static int[] checkRequest(Socket socket) throws IOException {
        int[] returnArray = new int[2];
        returnArray[0] = -1;
        returnArray[1] = -1;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String msg ="";
        while ((msg = buffer.readLine())!= null) {
            if(msg.contains("Cookie")){
                msg = msg.split("=")[1];
                msg = msg.split(" ")[0];
                System.out.println(msg);
                returnArray[0] = Integer.parseInt(msg);
            }
            if(msg.contains("guess")){
                msg = msg.split("=")[1];
                msg = msg.split(" ")[0];
                System.out.println(msg);
                returnArray[1] = Integer.parseInt(msg);
            }
            if(returnArray[0] != -1 && returnArray[1] != -1){
                return returnArray;
            }
        }
        System.out.println("no cookie");
        int[] arr = {-1,-1};
        return arr;
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
    Random rand = new Random();
    String guessValue;
    int guesses;

    public HTTPServer(Socket socket, int clientID){
        this.socket = socket;
        this.clientID = clientID;
        this.guesses = 0;
        this.correctValue = rand.nextInt(100);
    }

    public void run() {
        PrintWriter output = null;
        try {
            System.out.println("hello");
            //BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());
            output.println("HTTP/1.1 200 OK");
            output.println("Set-Cookie: clientId=" + clientID + " expires=Wednesday,24-Dec-21 23:59:59 GMT");
            output.println("Content-Type: text/html");
            output.println("\r\n");
            output.println("<p> Welcome to the guessing game </p>");
            output.println("<p> guess between 1 and 100 </p>");
            output.println("<p> Guess </p>");
            //output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
            output.println();
            output.flush();
            System.out.println("hello2");

            //if((msg = buffer.readLine()).contains("guess")){
                /*if(Integer.parseInt(guessValue) == correctValue){
                    //guess higher
                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html");
                    output.println("\r\n");
                    output.println("<p> You won. You have made " + guesses + " guesses </p>");
                    output.println("<p> Guess </p>");
                    output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
                    output.println();
                    output.flush();
                } else if(Integer.parseInt(guessValue) < correctValue) {
                    //guess higher
                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html");
                    output.println("\r\n");
                    output.println("<p> Nope, guess higher. You have made " + ++guesses + " guesses so far </p>");
                    output.println("<p> Guess </p>");
                    output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
                    output.println();
                    output.flush();
                } else {
                    //guess lower
                    guesses++;
                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html");
                    output.println("\r\n");
                    output.println("<p> Nope, guess lower. You have made " + ++guesses + " guesses so far </p>");
                    output.println("<p> Guess </p>");
                    output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
                    output.println();
                    output.flush();
                }
           // }

*/


            //new HTTPGuess(socket, clientID).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class HTTPGuess extends Thread {
    Socket socket;
    int guesses;
    Random rand = new Random();
    int correctValue;
    String guessValue;
    int clientID;

    public HTTPGuess(Socket socket, int clientID){
        this.socket = socket;
        this.guesses = 0;
        this.correctValue = rand.nextInt(100);
        this.clientID = clientID;
    }
    public void run() {
        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            String msg="";
            int q = 0;
            while ((msg = buffer.readLine())!= null && q < 5) {
                if(msg.contains("guess")){
                    guessValue = msg.split("=")[1];
                    guessValue = guessValue.split(" ")[0];
                    System.out.println(guessValue);
                    break;
                }
                q++;
            }
            System.out.println(guessValue);
            if(Integer.parseInt(guessValue) == correctValue){
                //guess higher
                output.println("HTTP/1.1 200 OK");
                output.println("Set-Cookie: clientId=" + clientID + " expires=Wednesday,24-Dec-21 23:59:59 GMT");
                output.println("Content-Type: text/html");
                output.println("\r\n");
                output.println("<p> You won. You have made " + guesses + " guesses </p>");
                output.println("<p> Guess </p>");
                output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
                output.println();
                output.flush();
            } else if(Integer.parseInt(guessValue) < correctValue) {
                //guess higher
                output.println("HTTP/1.1 200 OK");
                output.println("Set-Cookie: clientId=" + rand.nextInt(100) + " expires=Wednesday,24-Dec-21 23:59:59 GMT");
                output.println("Content-Type: text/html");
                output.println("\r\n");
                output.println("<p> Nope, guess higher. You have made " + ++guesses + " guesses so far </p>");
                output.println("<p> Guess </p>");
                output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
                output.println();
                output.flush();
            } else {
                //guess lower
                guesses++;
                output.println("HTTP/1.1 200 OK");
                output.println("Set-Cookie: clientId=" + rand.nextInt(100) + " expires=Wednesday,24-Dec-21 23:59:59 GMT");
                output.println("Content-Type: text/html");
                output.println("\r\n");
                output.println("<p> Nope, guess lower. You have made " + ++guesses + " guesses so far </p>");
                output.println("<p> Guess </p>");
                output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
                output.println();
                output.flush();
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}