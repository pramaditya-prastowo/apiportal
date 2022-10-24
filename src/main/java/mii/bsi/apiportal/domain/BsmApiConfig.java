package mii.bsi.apiportal.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class BsmApiConfig implements Serializable {

    @Id
    @Column(name = "KEYNAME")
    private String keyName;
    @Column(name = "KEYGROUP")
    private String keygroup;
    @Column(name = "VALUE")
    private String value;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CHACHE_MANAGER")
    private String cacheManager;
    @Column(name = "CACHE_NAME")
    private String cacheName;
    @Column(name = "ENABLED")
    private String enabled;

    public BsmApiConfig() {

    }

    public BsmApiConfig(String keyName, String keygroup, String value, String description, String cacheManager, String cacheName, String enabled) {
        this.keyName = keyName;
        this.keygroup = keygroup;
        this.value = value;
        this.description = description;
        this.cacheManager = cacheManager;
        this.cacheName = cacheName;
        this.enabled = enabled;
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
}
