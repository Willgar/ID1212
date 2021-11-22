import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class IMAP {
    private static PrintWriter pw;
    private static BufferedReader buffer;
    private static Console console;


    public static void main(String[] args) throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        //Clears the password argument from terminal
        String host = "webmail.kth.se";
        int port = 993;
        SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) ssf.createSocket(host,port);
        pw = new PrintWriter(sslSocket.getOutputStream());
        buffer = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        console = System.console();
        System.out.println("Enter your KTH mail:");
        String mail = console.readLine();
        String user = mail.split("@")[0];
        System.out.println(user);
        System.out.println("Enter your password:");
        char[] pass1 = console.readPassword();
        String pass = String.valueOf(pass1);
        printSecret("a001 LOGIN " + user + " " + pass );
        String msg = "";
        while((msg=buffer.readLine()) != null){
            System.out.println(msg);
            if(msg.contains("a001"))
                break;
        }
        openSession();
        /* hardcoded example
        print("a002 select inbox" );
        msg = "";
        while((msg=buffer.readLine()) != null){
            System.out.println(msg);
            if(msg.contains("a002"))
                break;
        }
        print("a003 fetch 1 full" );
        msg = "";
        while((msg=buffer.readLine()) != null){
            System.out.println(msg);
            if(msg.contains("a003"))
                break;
        }
            */
    }
    static void openSession() throws IOException {
        int i = 2;
        String tag = "a";
        console = System.console();
        String command = "";
        boolean flag = true;
        while(!command.contains("logout")){
            tag = "a"+String.format("%03d", i);
            i++;
            System.out.print(tag + " ");
            command = console.readLine();
            print(tag + " " + command);
            String msg = "";
            while((msg=buffer.readLine()) != null){
                System.out.println(msg);
                if(msg.contains(tag))
                    break;
            }
        }
    }
    static void print(String message) throws IOException {
        pw.println(message);
        pw.flush();
        System.out.println(message);
        System.out.println(buffer.readLine());
    }
    static void printSecret(String message) throws IOException {
        pw.println(message);
        pw.flush();
        System.out.println(buffer.readLine());
    }
}
