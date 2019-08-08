package digital.jameel.twitterparser;

import com.amazonaws.services.comprehend.model.DetectEntitiesResult;
import com.amazonaws.services.comprehend.model.DetectSentimentResult;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Response {

    private DetectSentimentResult sentimentResult;
    private DetectEntitiesResult entitiesResult;
    protected String message;
    protected String languageCode;
    protected String subject;
    protected long createdAt;
    protected int favouriteCount;
    protected int totalSentimentScore;

    public Response(String message, String languageCode){
        this.message = message;
        this.languageCode = languageCode;
    }

    public Response(String subject, String message, String languageCode){
        this(message, subject);
        this.subject = subject;
        this.languageCode = languageCode;
    }

    public Response(String subject, String message, String languageCode, long createdAt, int favouriteCount){
        this(message, subject);
        this.subject = subject;
        this.languageCode = languageCode;
        this.createdAt = createdAt;
        this.favouriteCount = favouriteCount;
        this.totalSentimentScore = 0;
    }

    public void setSentimentResult(DetectSentimentResult sentimentResult) {
        this.sentimentResult = sentimentResult;
        if (sentimentResult.getSentiment().equals("POSITIVE")){
            this.totalSentimentScore = 1;
        } else if (sentimentResult.getSentiment().equals("NEGATIVE")){
            this.totalSentimentScore = -1;
        }
    }

    public int getSentimentScore(){
        return this.totalSentimentScore;
    }

    public void setEntitiesResult(DetectEntitiesResult entitiesResult){
        this.entitiesResult = entitiesResult;
    }

    public DetectSentimentResult getSentimentResult() {
        return sentimentResult;
    }

    public DetectEntitiesResult getEntitiesResult() {
        return entitiesResult;
    }

    /**
     * Converts the current object into JSON
     * @return Converted JSON
     */
    public JsonObject generateJson(){
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(this);
        JsonObject jsonObject = (JsonObject)  element;
        jsonObject.getAsJsonObject("sentimentResult").remove("sdkResponseMetadata");
        jsonObject.getAsJsonObject("sentimentResult").remove("sdkHttpMetadata");
        jsonObject.getAsJsonObject("entitiesResult").remove("sdkResponseMetadata");
        jsonObject.getAsJsonObject("entitiesResult").remove("sdkHttpMetadata");
        return jsonObject;
    }

}
