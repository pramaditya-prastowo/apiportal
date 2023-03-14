package mii.bsi.apiportal.apigw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppRequestDTO {
    @JsonProperty("CORP_ID")
    private String corpId;
    @JsonProperty("KODE_BIAYA")
    private String kodeBiaya;
    @JsonProperty("JENIS_BIAYA")
    private String jenisBiaya;
    @JsonProperty("PROFIT_CENTRE_DEPT")
    private String profitCentreDept;
    @JsonProperty("LIMIT")
    private String limit;
    @JsonProperty("FEE")
    private String fee;
    @JsonProperty("DAILY_LIMIT")
    private String dailyLimit;
    @JsonProperty("JENIS_BIAYA_SKN")
    private String jenisBiayaSkn;
    @JsonProperty("NOMINAL_BIAYA_SKN")
    private String nominalBiayaSkn;
    @JsonProperty("JENIS_BIAYA_RTGS")
    private String jenisBiayaRtgs;
    @JsonProperty("NOMINAL_BIAYA_RTGS")
    private String nominalBiayaRtgs;
    @JsonProperty("DD_DEST_ACCOUNT")
    private String ddDestAccount;
    @JsonProperty("DD_JENIS_BIAYA")
    private String ddJenisBiaya;
    @JsonProperty("DD_NOMINAL_BIAYA")
    private String ddNominalBiaya;
    @JsonProperty("BENEFICIARY_ACCOUNT")
    private String beneficiaryAccount;
    @JsonProperty("BPI_FEE_AMOUNT")
    private String bpiFeeAmount;
    @JsonProperty("USER_TYPE")
    private String userType;
    @JsonProperty("DATA_ID_INPUTTER")
    private String dataIdInputter;
    @JsonProperty("DATA_ID_CHECKER")
    private String dataIdChecker;
    @JsonProperty("DATA_ID_APPROVAL")
    private String dataIdApproval;
    @JsonProperty("SECRET_KEY")
    private String secretKey;
    @JsonProperty("PRIVATE_KEY")
    private String privateKey;
    @JsonProperty("PUBLIC_KEY")
    private String publicKey;
    @JsonProperty("API_KEY")
    private String apiKey;
    @JsonProperty("APP_NAME")
    private String appName;
    @JsonProperty("CORP_NAME")
    private String corpName;

    public CreateAppRequestDTO(String corpId, String appName, String corpName) {
        this.corpId = corpId;
        this.kodeBiaya = "1";
        this.jenisBiaya = "1";
        this.profitCentreDept = "1";
        this.limit = "100";
        this.fee = "100";
        this.dailyLimit = "100";
        this.jenisBiayaSkn =  "1";
        this.nominalBiayaSkn = "100";
        this.jenisBiayaRtgs =  "1";
        this.nominalBiayaRtgs = "100";
        this.ddDestAccount =  "1";
        this.ddJenisBiaya =  "1";
        this.ddNominalBiaya =  "1";
        this.beneficiaryAccount =  "1";
        this.bpiFeeAmount =  "1";
        this.userType =  "1";
        this.dataIdInputter =  "1";
        this.dataIdChecker =  "1";
        this.dataIdApproval =  "1";
        this.secretKey =  "1";
        this.privateKey =  "1";
        this.publicKey =  "1";
        this.apiKey =  "1";
        this.appName = appName;
        this.corpName = corpName;
    }


}
