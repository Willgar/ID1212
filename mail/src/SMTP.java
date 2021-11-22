import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Pattern;

public class SMTP {
    static private PrintWriter pw;
    static private BufferedReader buffer;
    public static void main(String[] args) throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        //Clears the password argument from terminal
        String sHost = "smtp.kth.se";
        String rHost = "webmail.kth.se";
        int sPort = 587;
        int rPort = 993;
        Console console = System.console();
        System.out.println("Enter your KTH mail:");
        String mail = console.readLine();
        String user = mail.split("@")[0];
        System.out.println(user);
        System.out.println("Enter your password:");
        char[] pass1 = console.readPassword();
        String pass = String.valueOf(pass1);
        System.out.println("Enter the recipient:");
        String RCPT = console.readLine();

        String user64 = Base64.getEncoder().encodeToString(user.getBytes());
        String pass64 = Base64.getEncoder().encodeToString(pass.getBytes());


        //Creates Connection and encrypts with startTLS
        Socket socket = new Socket(sHost, sPort);
        pw = new PrintWriter(socket.getOutputStream());
        buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        print("EHLO " + sHost);
        print("STARTTLS");

        //Takes some input from the buffer
        String msg = "";
        while((msg=buffer.readLine()) != null){
            System.out.println(msg);
            if(msg.contains("220 2.0.0"))
                break;
        }

        SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) ssf.createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);
        pw = new PrintWriter(sslSocket.getOutputStream(), true);
        buffer = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        print("EHLO " + sHost);

        print("AUTH LOGIN");
        print(user64);
        print(pass64);
        msg = "";
        while((msg=buffer.readLine()) != null){
            System.out.println(msg);
            if(msg.contains("334")){        //prints until 334
                System.out.println(buffer.readLine()+"\n"+buffer.readLine());
                print("MAIL FROM:<"+mail+">");
                print("RCPT TO:<"+RCPT+">");
                print("RCPT TO:<"+mail+">");
                print("DATA");
                print2("Date: "+ LocalTime.now());
                print2("From: SMTP User <"+mail+">");
                print2("Subject: SMTP");
                print2("To: "+RCPT);
                print2("");
                print("Hello from the SMTP shell\r\n.");
                print("QUIT");
            }

        }
        System.out.println("END");
        pw.close();
        buffer.close();
        socket.close();
        sslSocket.close();

    }
    static void print(String msg) throws IOException {
        pw.println(msg);
        pw.flush();
        System.out.println(msg);
        System.out.println(buffer.readLine());
    }
    static void print2(String msg) throws IOException {
        pw.println(msg);
        pw.flush();
        System.out.println(msg);
    }
}

