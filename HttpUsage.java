import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpUsage {

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {

        String fileURL = "http://th.if.uj.edu.pl/~atg/Java/Enums.txt";
        String saveDir = "C:\\Users\\Vlad_F\\IdeaProjects\\Enums\\src";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(fileURL))
                .GET()
                .build();

        //Path tempFile = Files.createTempFile("data", ".txt");
        Path tempFile = Files.createTempFile(Paths.get(saveDir), "data", ".txt");
        HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(tempFile));
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}
