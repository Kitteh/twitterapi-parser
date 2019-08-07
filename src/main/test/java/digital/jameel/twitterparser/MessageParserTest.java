package digital.jameel.twitterparser;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.comprehend.model.DetectEntitiesResult;
import com.amazonaws.services.comprehend.model.DetectSentimentResult;
import com.amazonaws.services.comprehend.model.Entity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MessageParserTest {

    private static MessageParser parser;
    private static Response positiveResponse;
    private static Response negativeResponse;
    private static final Regions region = Regions.EU_WEST_1;
    private static final String languageCode = "en";

    @BeforeAll
    public static void setup(){
        parser = new MessageParser(region, languageCode);
        positiveResponse = parser.parseMessage("I am really happy to see the new Spiderman movie with Tom Holland");
        negativeResponse = parser.parseMessage("I really hate the new Spiderman movie with Tom Holland, they should not have bothered !");
    }

    @Test
    public void parseMessageTestPositive(){
        Response response = positiveResponse;
        DetectSentimentResult sentimentResult = response.getSentimentResult();
        assert sentimentResult.getSentiment().equals("POSITIVE");
    }

    @Test
    public void parseMessageTestNegative(){
        Response response = negativeResponse;
        DetectSentimentResult sentimentResult = response.getSentimentResult();
        assert sentimentResult.getSentiment().equals("NEGATIVE");
    }

    @Test
    public void parseMessageTestEntitiesAreValid(){
        Response response = positiveResponse;
        DetectEntitiesResult entitiesResult = response.getEntitiesResult();
        String person = "";
        String title = "";

        for (Entity entity : entitiesResult.getEntities()){
            String type = entity.getType();
            String text = entity.getText();
            if (type.equals("TITLE")){
                title = text;
            }
            if (type.equals("PERSON")){
                person = text;
            }
        }
        assert title.equals("Spiderman");
        assert person.equals("Tom Holland");
    }

}