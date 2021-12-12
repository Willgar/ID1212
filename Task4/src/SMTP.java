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
        String host = "smtp.kth.se";
        int port = 587;
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
        Socket socket = new Socket(host, port);
        pw = new PrintWriter(socket.getOutputStream());
        buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        print("EHLO " + host);
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
        print("EHLO " + host);

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
                print("RCPT TO:<"+mail+">");//Extra to yourself
                print("DATA");
                print("Date: "+ LocalTime.now(), false);
                print("From: SMTP User <"+mail+">", false);
                print("Subject: SMTP", false);
                print("To: "+RCPT, false);
                print("", false);
                print("Required email sent for Task 4 in ID1212 Network Programming(sorry kth admin if it gets stuck again)\r\n.");
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
        print(msg, true);
    }
    static void print(String msg, boolean flag) throws IOException {
        pw.println(msg);
        pw.flush();
        System.out.println(msg);
        if(flag)
            System.out.println(buffer.readLine());
    }
}

