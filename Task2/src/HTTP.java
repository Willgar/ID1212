import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        double tries = 0;
        for(int i = 0; i <100; i++) {
            int guesses = 50;
            int previous = 100;
            int j = 0;
            boolean flag = true;
            while (flag) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8282/?guess=" + guesses))
                        .header("Cookie", "clientId="+i+" expires=Wednesday,24-Dec-21 23:59:59 GMT")
                        .build();
                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());
                //checks the game logic and changes the guess accordingly
                if(response.body().contains("won")) {
                    System.out.println("It took " + j + " guesses before you won");
                    tries = tries+j;
                    flag=false;
                    break;
                } else if(response.body().contains("higher")){
                    int temp = guesses;
                    guesses += (Math.abs(guesses-previous)/2);
                    previous = temp;
                    if(guesses==temp)
                        guesses++;
                }else if(response.body().contains("lower")){
                    int temp = guesses;
                    guesses -= (Math.abs(guesses-previous)/2);
                    previous = temp;
                    if(guesses==temp)
                        guesses--;
                }
                j++;
            }

        }
        System.out.println("Average: " +tries/100 + " guesses per match");
    }
}
public class HTTP {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(8282);
        HTTPServer[] servers = new HTTPServer[1000];
        int clientID = 0;
        try {
            while (true) {
                System.out.println("Waiting for Client to connect");
                Socket socket = serverSocket.accept();
                int[] cookieID = checkRequest(socket);
                System.out.println("Connection Receieved with socket: " + socket);
                if(servers[cookieID[0]] != null && cookieID[1] != -1) {
                    servers[cookieID[0]].guess(cookieID[1], socket);
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

    static int[] checkRequest(Socket socket) throws IOException {  //Custom parsing of the HTTP Request receieved
        int[] returnArray = {999,-1};                              // to find Cookies and guess queries
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
            if((returnArray[0] != 999 && returnArray[1] != -1) || flag2){
                return returnArray;
            }
        }
        return returnArray;
    }
}

class HTTPServer extends Thread {
    Socket socket;
    int clientID;
    int correctValue;
    int guesses;
    private int[] doublettes;
    Random rand;

    public HTTPServer(Socket socket, int clientID) throws IOException {
        this.socket = socket;
        this.clientID = clientID;
        this.guesses = 0;
        rand = new Random();
        this.correctValue = rand.nextInt(100);
        doublettes = new int[100];
    }

    public void run() {
        try {
            sendStartPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void guess(int guess, Socket socket) throws IOException {
        if(doublettes[guess]!=1){               //To avoid double guessing errors the guess only counts once per number
            doublettes[guess]++;
            guesses++;
        }
            System.out.println("Guessed: " + guess + "\nCorrect: " + correctValue + "\nGuesses: " + guesses);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);       //Opens a new printwriter for the socket
            if(guess==correctValue){    //Game logic
                sendWinPage(output);
            } else if(guess < correctValue){
                sendHigherPage(output);
            } else{
                sendLowerPage(output);
            }
    }
    private void sendStartPage() throws IOException {
        PrintWriter startup = new PrintWriter(socket.getOutputStream(), true);
        startup.println("HTTP/1.1 200 OK");
        startup.println("Set-Cookie: clientId=" + clientID + " expires=Wednesday,24-Dec-21 23:59:59 GMT");
        startup.println("Content-Type: text/html");
        startup.println("\r\n");
        startup.println("<p> Welcome to the guessing game </p>");
        startup.println("<p> guess between 1 and 100 </p>");
        startup.println("<form action=\"\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label>  </form> ");
        startup.println();
        startup.close();
    }
    private void sendWinPage(PrintWriter output) {
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html");
        output.println("\r\n");
        output.println("<p> You won. You have made " + guesses + " guesses </p>");
        output.println("<p> The game has now been reset, play again.</p>");
        output.println("<form action=\"\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"> </label>  </form> ");
        output.println();
        output.close();
        correctValue = rand.nextInt(100);       //resetting the game
        doublettes = new int[100];
        guesses = 0;
    }
    private void sendHigherPage(PrintWriter output)  {
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html");
        output.println("\r\n");
        output.println("<p> Nope, guess higher. You have made " + guesses + " guesses so far </p>");
        output.println("<form action=\"\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"></label>  </form> ");
        output.println();
        output.close();
    }
    private void sendLowerPage(PrintWriter output)  {
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html");
        output.println("\r\n");
        output.println("<p> Nope, guess lower. You have made " + guesses + " guesses so far </p>");
        output.println("<form action=\"\"> <label for=\"guess\"> guess number <input type=\"text\" id=\"guess\" name=\"guess\"></label>  </form> ");
        output.println();
        output.close();
    }
}
