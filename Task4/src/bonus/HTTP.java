package bonus;
import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class HTTP {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException, NoSuchProviderException {
        //setup for the SSL Server socket
        KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        InputStream is = new FileInputStream("C:/Users/Willi/.keystore");
        char[] pwd = "password".toCharArray();
        ks.load(is,pwd);
        SSLContext sslctx = SSLContext.getInstance("TLS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, pwd);
        sslctx.init(kmf.getKeyManagers(), null, null);
        SSLServerSocketFactory  ssf = sslctx.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket)ssf.createServerSocket(8282);


        HTTPServer[] servers = new HTTPServer[1000];
        int clientID = 0;
        try {
            while (true) {
                System.out.println("Waiting for Client to connect");
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                int[] cookieID = checkRequest(socket, buffer);
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

    static int[] checkRequest(SSLSocket socket,BufferedReader buffer) throws IOException {  //Custom parsing of the HTTP Request receieved
        int[] returnArray = {999,-1};                              // to find Cookies and guess queries
        boolean flag = true;
        boolean flag2 = false;
        String msg ="";
        try {
            while ((msg = buffer.readLine()) != null) {
                System.out.println(msg);
                if (msg.contains("Accept-Language")) {
                    msg = buffer.readLine();
                    //flag2 = true;
                }
                if (msg.contains("Cookie")) {
                    msg = msg.split("=")[1];            //basic regex to get the request
                    msg = msg.split(" ")[0];
                    System.out.println("COOKIE: " + msg);
                    returnArray[0] = Integer.parseInt(msg);
                    flag2 = true;
                }
                if (msg.contains("guess") && flag) {
                    msg = msg.split("=")[1];
                    msg = msg.split(" ")[0];
                    System.out.println("GUESS: " + msg);
                    flag = false;
                    returnArray[1] = Integer.parseInt(msg);
                }
                if ((returnArray[0] != 999 && returnArray[1] != -1) || flag2) {
                    System.out.println("finished here " + returnArray[0]);
                    return returnArray;
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return returnArray;
    }
}

class HTTPServer extends Thread {
    SSLSocket socket;
    int clientID;
    int correctValue;
    int guesses;
    private int[] doublettes;
    Random rand;

    public HTTPServer(SSLSocket socket, int clientID) throws IOException {
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
    public void guess(int guess, SSLSocket socket) throws IOException {
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
