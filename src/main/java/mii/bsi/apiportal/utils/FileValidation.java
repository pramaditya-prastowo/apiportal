package mii.bsi.apiportal.utils;

import mii.bsi.apiportal.domain.DocKerjasama;
import org.springframework.stereotype.Component;

@Component
public class FileValidation {

    public boolean existDocument(String filename, DocKerjasama docKerjasama){
        if(docKerjasama.getAktaPendirianPerubahan().equals(filename)) return true;
        if(docKerjasama.getBukuTabungan().equals(filename)) return true;
        if(docKerjasama.getCompanyNpwp().equals(filename)) return true;
        if(docKerjasama.getCompanyProfile().equals(filename)) return true;
        if(docKerjasama.getFotoKtpPM().equals(filename)) return true;
        if(docKerjasama.getFotoKtpPengurus().equals(filename)) return true;
        if(docKerjasama.getHasilSandbox().equals(filename)) return true;
        if(docKerjasama.getPublicKey().equals(filename)) return true;
        if(docKerjasama.getSrtPernyataanPengajuan().equals(filename)) return true;
        if(docKerjasama.getSrtSiup().equals(filename)) return true;
        if(docKerjasama.getSrtKetKemenkumham().equals(filename)) return true;
        return false;

    }
}
