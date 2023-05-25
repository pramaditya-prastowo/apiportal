package mii.bsi.apiportal.domain.model;

public enum ApprovalStatus {
    MENUNGGU_PERSETUJUAN("MENUNGGU_PERSETUJUAN"),
    DALAM_PROSES_PERSETUJUAN("DALAM_PROSES_PERSETUJUAN"),
    DITOLAK("DITOLAK"),
    DISETUJUI("DISETUJUI"),
    SELESAI("SELESAI"),
    DITAHAN("DITAHAN"),
    MINTA_PENGAJUAN_ULANG("MINTA_PENGAJUAN_ULANG"),
    PENGAJUAN_ULANG("PENGAJUAN_ULANG"),
    DIBATALKAN("DIBATALKAN");

    private final String text;

    ApprovalStatus(String text){this.text = text;}

    @Override
    public String toString() {
        return this.text;
    }
}
