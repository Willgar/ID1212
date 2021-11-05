import java.io.*;
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
            output.println("HTTP/1.1 200 OK");
            output.println("Set-Cookie: clientId=" + rand.nextInt(100) + " expires=Wednesday,24-Dec-21 23:59:59 GMT");
            output.println("Content-Type: text/html");
            output.println("\r\n");
            output.println("<p> Welcome to the guessing game </p>");
            output.println("<p> guess between 1 and 100 </p>");
            output.println("<p> Guess </p>");
            output.println("<form action=\"\" method=\"get\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label></label>  </form> ");
            output.println();
            output.flush();
            new HTTPGuess(socket).start();
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

    public HTTPGuess(Socket socket){
        this.socket = socket;
        guesses = 0;
        correctValue = rand.nextInt(100);
    }
    public void run() {
        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            String msg="";
            while ((msg = buffer.readLine())!= null) {
                if(msg.contains("guess")){
                    guessValue = msg.split("=")[1];
                    guessValue = guessValue.split(" ")[0];
                    System.out.println(guessValue);
                    break;
                }
            }

            if(Integer.parseInt(guessValue) == correctValue){
                //guess higher
                output.println("HTTP/1.1 200 OK");
                output.println("Set-Cookie: clientId=" + rand.nextInt(100) + " expires=Wednesday,24-Dec-21 23:59:59 GMT");
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
                output.flush();            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}