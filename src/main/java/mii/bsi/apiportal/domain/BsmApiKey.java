package mii.bsi.apiportal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class BsmApiKey implements Serializable {
    @Id
    @Column(name="API_KEY")
    private String apiKey;

    @Column(name="CORP_ID")
    private String corpId;

    @Column(name="APP_NAME")
    private String appName;

    @Column(name="CORP_NAME")
    private String corpName;

    public BsmApiKey(String apiKey, String corpId, String appName, String corpName){
        this.apiKey = apiKey;
        this.corpId = corpId;
        this.appName = appName;
        this.corpName = corpName;
    }

    public BsmApiKey(){}

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
