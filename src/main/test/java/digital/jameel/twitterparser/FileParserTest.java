package digital.jameel.twitterparser;

import com.amazonaws.regions.Regions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

class FileParserTest {

    private static final String filePath = "twitterapi-stream.json";
    private static FileParser fileParser;
    private static final Regions region = Regions.EU_WEST_1;
    private static final String languageCode = "en";

    @BeforeAll
    public static void setup() throws IOException, URISyntaxException {
        URL url = FileParserTest.class.getClassLoader().getResource(filePath);
        URI uri = url.toURI();
        FileInputStream file = new FileInputStream(url.getFile());
        fileParser = new FileParser(file, region, languageCode);
    }

    @Test
    @Order(1)
    public void parseFileTest() throws IOException {
        try {
            fileParser.parseFile(); // it parses without error
        }
        catch (Exception e){
            throw e;
        }
    }

    @Test
    @Order(2)
    public void generateJsonTest(){
        JsonArray jsonArray = fileParser.getResponsesAsJson(); // it parses without error
        JsonElement object = jsonArray.get(0);
        assert object.getAsJsonObject().get("sentimentResult").isJsonObject();
        assert object.getAsJsonObject().get("entitiesResult").isJsonObject();
    }
}