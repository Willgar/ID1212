import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.HashMap;

import static javax.swing.UIManager.put;

public class HTTPTask2 {
    public static void main(String[] args) throws IOException {
        int serverPort = 8282;
        String httpServer = "localhost";

        int timeout = 10000;
        String httpRequest = "GET / HTTP/1.1";
        String hostHeader = "Host: localhost";
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();

            var values = new HashMap<String, String>() {{
                put("name", "John Doe");
                put ("occupation", "gardener");
            }};
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("localhost:8282/"))
                    .build();

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

}
