package mii.bsi.apiportal.constant;

public enum DocumentName {
    SURAT_PERNYATAAN("Surat Penyataan Kerjasama", "surat-pernyataan"),
    SK_KEMENKUMHAM("SK Kemenkumham", "sk-kemenkumham"),
    AKTA_PENDIRIAN_PERUBAHAN("Akta Pendirian Perubahan", "akta-pendirian-perubahan"),
    COMPANY_PROFILE("Company Profile", "company-profile"),
    NPWP("NPWP", "npwp"),
    KTP_PENGURUS("KTP Pengurus", "ktp-pengurus"),
    KTP_PM("KTP Project Manager", "ktp-pm"),
    SIUP("SIUP", "siup"),
    NIB("NIB", "nib"),
    HASIL_SANDBOX("Hasil Sandbox", "hasil-sandbox"),
    BUKU_TABUNGAN("Buku Tabungan", "buku-tabungan"),
    PUBLIC_KEY("Public Key", "public-key");

    private final String name;
    private final String keyName;

    DocumentName(String name, String keyName) {
        this.name = name;
        this.keyName = keyName;
    }



    public String toString() {
        return this.name;
    }
    public String getKeyName(){
        return this.keyName;
    }
}
