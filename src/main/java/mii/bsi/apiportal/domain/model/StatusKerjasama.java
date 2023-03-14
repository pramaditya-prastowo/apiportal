package mii.bsi.apiportal.domain.model;

public enum StatusKerjasama {
    MENUNGGU_PERSETUJUAN("MENUNGGU_PERSETUJUAN"),
    DALAM_PROSES_PERSETUJUAN("DALAM_PROSES_PERSETUJUAN"),
    DITOLAK("DITOLAK"),
    DISETUJUI("DISETUJUI"),
    SELESAI("SELESAI"),
    DITAHAN("DITAHAN"),
    DIBATALKAN("DIBATALKAN");

    private final String text;

    StatusKerjasama(String text){this.text = text;}

    @Override
    public String toString() {
        return this.text;
    }
}
