import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This Interface is used to call bart API.
 * it has generic methods and its default implementation
 */
interface BartAPI {

    /*
    Constant used in BART API
     */
    String API_KEY = "&key=MW9S-E7SL-26DU-VV8V&l=1";
    String JSON_TRUE = "&json=y";
    String ROOT = "root";
    String STATION = "station";
    String TIME = "time";
    String DESTINATION = "destination";
    String ETD = "etd";
    String ESTIMATE = "estimate";
    String MINUTES = "minutes";
    String MESSAGE = "message";

    HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    /**
     * This method is used to parse the response of BartApi
     *
     * @param jsonString response to be parsed
     * @return return the response
     */
    @SuppressWarnings("unchecked")
    default <T> T parse(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
        JSONObject root = (JSONObject) jsonObject.get(ROOT);
        return (T) root;

    }


    /**
     * This method is used to execute the http request
     *
     * @return CloseableHttpResponse
     * @throws IOException
     */
    default HttpResponse<String> execute(String uri) throws IOException, InterruptedException {
        uri = uri + API_KEY + JSON_TRUE;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }
}


/**
 * This class is used to call BART API for Real-Time Information API.
 * this
 */
@SuppressWarnings("unchecked")
final class RealTimeInfoEtd implements BartAPI {

    public static String MONT_DEPART_URL = "http://api.bart.gov/api/etd.aspx?cmd=etd&orig=MONT&key=MW9S-E7SL-26DU-VV8V&l=1&json=y";
    private List<FiledToDisplay> filedToDisplaysLst;
    private String time;


    public RealTimeInfoEtd(String time, List<FiledToDisplay> filedToDisplaysLst) {
        this.time = time;
        this.filedToDisplaysLst = filedToDisplaysLst;
    }

    public RealTimeInfoEtd(String destination) {

        MONT_DEPART_URL = "http://api.bart.gov/api/etd.aspx?cmd=etd&orig=" + destination;
    }

    /**
     * this method is used to display the list of distanation
     *
     * @return
     */
    public List<FiledToDisplay> getFiledToDisplaysLst() {
        //filedToDisplaysLst.stream().sorted(Comparator.comparing(FiledToDisplay::getTime));


        return filedToDisplaysLst;
    }

    /**
     * This method is used to get time
     *
     * @return
     */
    public String getTime() {
        return time;
    }

    @Override
    public <T> T parse(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
        JSONObject root = (JSONObject) jsonObject.get(ROOT);
        JSONArray station = (JSONArray) root.get(STATION);
        String time = root.get(TIME).toString();


        List<FiledToDisplay> filedToDisplaysLst = new ArrayList<>();

        JSONObject ele = ((JSONObject) station.get(0));
        if (ele == null || ele.get(ETD) == null) {
            throw new RuntimeException(ele.get(MESSAGE).toString());
        }

        JSONArray result = (JSONArray) ele.get(ETD);

        for (int i = 0; i < result.size(); i++) {
            JSONObject objects = (JSONObject) result.get(i);
            String dest = (String) objects.get(DESTINATION);

            JSONArray estimate = (JSONArray) objects.get(ESTIMATE);
            String minutes = (String)((JSONObject) estimate.get(0)).get(MINUTES);
            filedToDisplaysLst.add(new FiledToDisplay(dest, minutes));
        }
        List<FiledToDisplay> leavingList = filedToDisplaysLst.stream()
                .filter(o -> o.getMinute().equals("Leaving")).collect(Collectors.toList());

        List<FiledToDisplay> numberList = filedToDisplaysLst.stream()
                .filter(o -> !(o.getMinute().equals("Leaving"))).collect(Collectors.toList());

        List<FiledToDisplay> sortedList= numberList.stream()
                .sorted(Comparator.comparing(test -> Integer.parseInt(test.getMinute())))
                .collect(Collectors.toList());
        List<FiledToDisplay> finalList= new ArrayList<>();
        finalList.addAll(leavingList);
        finalList.addAll(sortedList);
        return (T) new RealTimeInfoEtd(time, finalList);

    }
}

/**
 * This class has field that need to display to the user
 */
final class FiledToDisplay {

    private String destination;
    private String minute;

    public FiledToDisplay(String destination, String time) {
        this.destination = destination;
        this.minute = time;
    }

    public String getMinute() {
        return minute;
    }

    @Override
    public String toString() {

        return minute  + (minute.equals("Leaving")? "  now  " : "  min  ") + destination;
    }


}