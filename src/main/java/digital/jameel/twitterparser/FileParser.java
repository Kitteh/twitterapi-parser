package digital.jameel.twitterparser;

import com.amazonaws.regions.Regions;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileParser {

    private InputStream reader;
    private MessageParser messageParser;
    private ArrayList<Response> responses;

    /**
     * Parse a document containing multiple JSON objects (one per line) using the Message Parser
     * @param reader an input stream to parse
     * @param region AWS Region
     * @param languageCode Language code e.g. 'en'
     */
    public FileParser(InputStream reader, Regions region, String languageCode){
        this.reader = reader;
        this.messageParser = new MessageParser(region, languageCode);
        this.responses = new ArrayList<Response>();
    }

    /**
     * Parse the file provided and populate a series of responses
     * @throws IOException
     */
    public void parseFile() throws IOException {

        ArrayList<JsonArray> inputArray = this.parseFileToJson();

        for (JsonArray elements : inputArray){
            this.parseElementToResponse(elements);
        }
    }

    public JsonArray getResponsesAsJson(){
        JsonArray jsonArray = new JsonArray();
        for (Response response : this.responses){
            JsonObject jsonObject = response.generateJson();
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * Parse an individual document into the responses array
     * @param elements
     */
    private void parseElementToResponse(JsonArray elements){
        for (JsonElement element : elements){
            JsonObject object = element.getAsJsonObject();
            String message = object.get("twitterMessage").getAsString();
            String subject = object.get("subject").getAsString();
            long createdAt = object.get("createdAt").getAsLong();
            int favouriteCount = object.get("favouriteCount").getAsInt();
            Response response = this.messageParser.parseMessage(subject, message, createdAt, favouriteCount);
            this.responses.add(response);
        }
    }

    /**
     * Read the provided input file line by line and return a list of JSON documents
     * @return A list of Json Objects
     * @throws IOException
     */
    private ArrayList<JsonArray> parseFileToJson() throws IOException{
        BufferedReader bf = new BufferedReader(new InputStreamReader(reader));
        String line;

        ArrayList<JsonArray> inputArray = new ArrayList<JsonArray>();

        while ((line = bf.readLine()) != null){
            JsonArray elements = (JsonArray) new JsonParser().parse(line);
            inputArray.add(elements);
        }
        return inputArray;
    }
}
