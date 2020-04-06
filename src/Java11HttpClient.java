import netscape.javascript.JSObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Java11HttpClient {

    // optional
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private final HttpClient httpClient = HttpClient.newBuilder()
            .executor(executorService)
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static void main(String[] args) throws Exception {
        Java11HttpClient obj = new Java11HttpClient();
        obj.test2();
    }

    private List<CompletableFuture<String>> sendGET() throws Exception {

        List<URI> targets = Arrays.asList(
                new URI("https://httpbin.org/get?namr=mkyong1"),
                new URI("https://httpbin.org/get?name=mkyong2"),
                new URI("https://httpbin.org/get?namr=mkyong3"),
                new URI("https://httpbin.org/get?namr=mkyong4"),
                new URI("https://httpbin.org/get?namr=mkyong5"));

        List<CompletableFuture<String>> result = targets.stream()
                .map(url -> httpClient.sendAsync(
                        HttpRequest.newBuilder(url)
                                .GET()
                                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                                .build(),
                        HttpResponse.BodyHandlers.ofString())
                        .thenApply(response -> response.body()))
                .collect(Collectors.toList());

        return result;


    }
//
//    private void josn() throws IOException {
//        URL url = new URL("https://graph.facebook.com/search?q=java&type=post");
//        try (InputStream is = url.openStream();
//             JsonReader rdr = Json.createReader(is)) {
//
//            JsonObject obj = rdr.readObject();
//            JsonArray results = obj.getJsonArray("data");
//            for (JsonObject result : results.getValuesAs(JsonObject.class)) {
//                System.out.print(result.getJsonObject("from").getString("name"));
//                System.out.print(": ");
//                System.out.println(result.getString("message", ""));
//                System.out.println("-----------");
//            }
//        }
//    }

    public void test2() throws Exception {
//
        List<CompletableFuture<String>> result = sendGET();

        List<String> respose = new ArrayList<>();
        for (CompletableFuture<String> future : result) {
            respose.add(future.get());
        }
        System.out.println("FINISED");


        for (String s : respose) {

        }
    }
}

