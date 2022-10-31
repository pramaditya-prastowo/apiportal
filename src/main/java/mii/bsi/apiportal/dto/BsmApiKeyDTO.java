package mii.bsi.apiportal.dto;

import mii.bsi.apiportal.domain.BsmApiKey;

import java.util.List;

public class BsmApiKeyDTO {
    private String responseCode;
    private String responseMessage;
    private String apiKey;
    private String corpId;
    private String appName;
    private String corpName;

    public BsmApiKeyDTO(){}

    public BsmApiKeyDTO(String apiKey){
        this.apiKey = apiKey;
    }

    public BsmApiKeyDTO(String responseCode, String responseMessage, String apiKey, String corpId, String appName, String corpName){
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.apiKey = apiKey;
        this.corpId = corpId;
        this.appName = appName;
        this.corpName = corpName;
    }

    public BsmApiKeyDTO(BsmApiKey bsmApiKey){
        this.apiKey = bsmApiKey.getApiKey();
        this.appName = bsmApiKey.getAppName();
        this.corpId = bsmApiKey.getCorpId();
        this.corpName = bsmApiKey.getCorpName();
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
}
