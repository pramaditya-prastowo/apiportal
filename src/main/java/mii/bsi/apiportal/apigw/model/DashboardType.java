package mii.bsi.apiportal.apigw.model;

public enum DashboardType {

    TOTAL_SERVICE_DAILY("TOTAL_SERVICE_DAILY","GET"),
    TOTAL_SERVICE_BY_CORP_ID("TOTAL_SERVICE_BY_CORP_ID", "POST"),
    TOTAL_HIT_API_BY_CORP_ID("TOTAL_HIT_API_BY_CORP_ID", "POST"),
    DETAIL_HIT_SERVICE_DAILY("DETAIL_HIT_SERVICE_DAILY","GET"),
    DETAIL_HIT_SERVICE_MONTHLY("DETAIL_HIT_SERVICE_MONTHLY", "POST"),
    TOTAL_MITRA_PER_SERVICE("TOTAL_MITRA_PER_SERVICE","GET"),
    TOTAL_MITRA_BY_API("TOTAL_MITRA_BY_API","GET"),
    TOTAL_HIT_SERVICE_BY_SERVICE_NAME("TOTAL_HIT_SERVICE_BY_SERVICE_NAME","GET"),
    TOTAL_HIT_SERVICE_MONTHLY("TOTAL_HIT_SERVICE_MONTHLY", "POST");

    private final String text;
    private final String method;

    DashboardType(String text, String method){this.text = text; this.method = method;}

    @Override
    public String toString() {
        return this.text;
    }
}
