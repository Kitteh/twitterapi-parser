package digital.jameel.twitterparser;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.DetectEntitiesRequest;
import com.amazonaws.services.comprehend.model.DetectEntitiesResult;
import com.amazonaws.services.comprehend.model.DetectSentimentRequest;
import com.amazonaws.services.comprehend.model.DetectSentimentResult;

public class MessageParser {

    private AmazonComprehend comprehendClient;

    private Regions region;
    private String languageCode;

    public MessageParser(Regions region, String languageCode){
        this.region = region;
        this.languageCode = languageCode;
        this.init();
    }

    private void init(){
        this.comprehendClient = AmazonComprehendClientBuilder
                .standard()
                .withRegion(this.region)
                .build();
    }

    public Response parseMessage(String subject, String message){
        return this.parseMessage(subject, message, 0,0);
    }

    public Response parseMessage(String subject, String message, long createdAt, int favouriteCount){
        DetectEntitiesResult entitiesResult = this.getEntities(message);
        DetectSentimentResult sentimentResult = this.getSentiment(message);
        Response response = new Response(subject, message, this.languageCode, createdAt, favouriteCount);
        response.setEntitiesResult(entitiesResult);
        response.setSentimentResult(sentimentResult);
        return response;
    }

    public Response parseMessage(String message){
        return this.parseMessage("", message);
    }

    protected DetectSentimentResult getSentiment(String message){
        DetectSentimentRequest detectSentimentRequest = new DetectSentimentRequest()
                .withText(message)
                .withLanguageCode(this.languageCode);
        DetectSentimentResult detectSentimentResult = this.comprehendClient.detectSentiment(detectSentimentRequest);
        return detectSentimentResult;
    }

    protected DetectEntitiesResult getEntities(String message){
        DetectEntitiesRequest detectEntitiesRequest = new DetectEntitiesRequest()
                .withText(message)
                .withLanguageCode(this.languageCode);
        DetectEntitiesResult detectEntitiesResult = this.comprehendClient.detectEntities(detectEntitiesRequest);
        return detectEntitiesResult;
    }
}
