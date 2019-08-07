package digital.jameel.twitterparser;

import com.amazonaws.regions.Regions;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ResponseTest {

    private static Response response;
    private static final Regions region = Regions.EU_WEST_1;
    private static final String languageCode = "en";

    @BeforeAll
    public static void setup(){
        MessageParser parser = new MessageParser(region, languageCode);
        response = parser.parseMessage("I am really happy to see the new Spiderman movie with Tom Holland, but I really hated the soundtrack");
    }

    @Test
    public void responseGenerateJsonIsValid(){
        JsonObject json = response.generateJson();
        json.getAsJsonObject("sentimentResult").get("sentiment").getAsString().equals("MIXED");
    }
}
