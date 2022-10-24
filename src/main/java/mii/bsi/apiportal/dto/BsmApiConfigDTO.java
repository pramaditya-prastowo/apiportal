package mii.bsi.apiportal.dto;

import mii.bsi.apiportal.domain.BsmApiConfig;

import javax.persistence.Column;

public class BsmApiConfigDTO {

    private String responseCode;
    private String responseMessage;
    private String keyName;
    private String keygroup;
    private String value;
    private String description;
    private String cacheManager;
    private String cacheName;
    private String enabled;

    public BsmApiConfigDTO() {
    }

    public BsmApiConfigDTO(String keyName, String cacheManager, String cacheName){
        this.keyName = keyName;
        this.cacheManager = cacheManager;
        this.cacheName = cacheName;

    }

    public BsmApiConfigDTO(String keyName, String keygroup, String value, String description, String cacheManager, String cacheName, String enabled, String responseCode, String responseMessage) {
        this.keyName = keyName;
        this.keygroup = keygroup;
        this.value = value;
        this.description = description;
        this.cacheManager = cacheManager;
        this.cacheName = cacheName;
        this.enabled = enabled;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public BsmApiConfigDTO(BsmApiConfig bsmApiConfig) {
        this.keyName = bsmApiConfig.getKeyName();
        this.keygroup = bsmApiConfig.getKeygroup();
        this.value = bsmApiConfig.getValue();
        this.description = bsmApiConfig.getDescription();
        this.cacheManager = bsmApiConfig.getCacheManager();
        this.cacheName = bsmApiConfig.getCacheName();
        this.enabled = bsmApiConfig.getEnabled();
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeygroup() {
        return keygroup;
    }

    public void setKeygroup(String keygroup) {
        this.keygroup = keygroup;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(String cacheManager) {
        this.cacheManager = cacheManager;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
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
}
