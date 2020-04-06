import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class Solution1 {

    public static void main(String[] a) throws IOException, ParseException, InterruptedException {
        BartAPI realTimeInfoEtd = new RealTimeInfoEtd("MONT");
        HttpResponse<String> response =
                realTimeInfoEtd.execute(RealTimeInfoEtd.MONT_DEPART_URL);

        int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new RuntimeException("Http status code: " + statusCode);
        }
        String body = response.body();
        RealTimeInfoEtd result = realTimeInfoEtd.parse(body);
        System.out.println("------------------------------------------------");
        System.out.println("Montgomery St  " + result.getTime());
        System.out.println("------------------------------------------------");

        List<FiledToDisplay> limitingToTen = result.getFiledToDisplaysLst().stream()
                .limit(10).collect(Collectors.toList());

        limitingToTen.stream()
                .forEach(System.out::println);


    }
}