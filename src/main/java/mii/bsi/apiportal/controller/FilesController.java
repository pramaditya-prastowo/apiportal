package mii.bsi.apiportal.controller;

import io.jsonwebtoken.ExpiredJwtException;
import mii.bsi.apiportal.domain.DocKerjasama;
import mii.bsi.apiportal.domain.PengajuanKerjasama;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.FileGroup;
import mii.bsi.apiportal.domain.model.FileInfo;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.dto.UserResponseDTO;
import mii.bsi.apiportal.repository.PengajuanKerjasamaRepository;
import mii.bsi.apiportal.repository.UserRepository;
import mii.bsi.apiportal.utils.DateUtils;
import mii.bsi.apiportal.utils.FileValidation;
import mii.bsi.apiportal.utils.JwtUtility;
import mii.bsi.apiportal.validation.UserValidation;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import mii.bsi.apiportal.service.FilesStorageService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController()
@RequestMapping("/api/files")
public class FilesController {
    @Autowired
    FilesStorageService storageService;
    @Autowired
    private PengajuanKerjasamaRepository pengajuanKerjasamaRepository;
    @Autowired
    private UserValidation userValidation;
    @Autowired
    private FileValidation fileValidation;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/service-api")
    public ResponseEntity<ResponseHandling> uploadFile(@RequestParam("file") MultipartFile file,
                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        ResponseHandling responseData = new ResponseHandling();
        String message = "";
        try {
            storageService.save(file, FileGroup.SERVICE_API);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            responseData.success(message);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            responseData.failed(message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseData);
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/service-api/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Resource file = storageService.load(filename, FileGroup.SERVICE_API);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }catch (Exception e){
            e.printStackTrace();

            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<ResponseHandling> uploadProfile(@RequestParam("file") MultipartFile file,
                                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        ResponseHandling responseData = new ResponseHandling();
        String message = "";
        try {

            System.out.println(file.getOriginalFilename());

            User user = userValidation.getUserFromToken(token.substring(7));
            if(user == null){
                message = "Forbidden";
                responseData.failed(message);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData);
            }

            storageService.save(file, FileGroup.PROFILE,user);
            user.setPhotoProfile(user.getId()+"/"+file.getOriginalFilename());
            userRepository.save(user);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            responseData.success(message);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            responseData.failed(message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseData);
        }
    }

    @GetMapping("/profile")
    @ResponseBody
    public ResponseEntity<Resource> getPhotoProfile(@RequestParam("filename") String filename) {
        ResponseHandling responseData = new ResponseHandling();
        String message = "";
        try {


            Resource file = storageService.load(filename, FileGroup.PROFILE);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }catch (Exception e){
            e.printStackTrace();

            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/apps")
    public ResponseEntity<ResponseHandling> uploadIconApps(@RequestParam("file") MultipartFile file){
        ResponseHandling responseData = new ResponseHandling();
        String message = "";
        try {
            storageService.save(file, FileGroup.MY_APPLICATION);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            responseData.success(message);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            responseData.failed(message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseData);
        }
    }
    @GetMapping("/apps/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getFileApps(@PathVariable String filename) {
        try {
            Resource file = storageService.load(filename, FileGroup.MY_APPLICATION);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }catch (Exception e){
            e.printStackTrace();

            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/promo")
    public ResponseEntity<ResponseHandling> uploadBannerPromo(@RequestParam("file") MultipartFile file){
        ResponseHandling responseData = new ResponseHandling();
        String message = "";
        try {
            storageService.save(file, FileGroup.PROMO);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            responseData.success(message);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            responseData.failed(message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseData);
        }
    }

    @GetMapping("/promo/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getFilePromo(@PathVariable String filename) {
        try {
            Resource file = storageService.load(filename, FileGroup.PROMO);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam String fileName, HttpServletRequest request) throws IOException {

        // Load file as Resource
        Resource resource = new FileSystemResource("files/download/" + fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // log error
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @PostMapping("/kerjasama")
    public ResponseEntity<ResponseHandling> uploadDocKerjasama(@RequestParam("file") MultipartFile file){
        ResponseHandling responseData = new ResponseHandling();
        String message = "";
        try {
            storageService.save(file, FileGroup.KERJASAMA);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            responseData.success(message);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            responseData.failed(message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseData);
        }
    }

    @GetMapping("/kerjasama/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadFileKerjasama(@RequestParam String filename,
                                                          @RequestParam(name = "token") String token,
                                                          @RequestParam(name = "id") String pekerId,
                                                          HttpServletRequest request) {

        try {
            if(jwtUtility.isTokenExpired(token)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            User user = userValidation.getUserFromToken(token);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if(user.getAuthPrincipal() == Roles.MITRA){
                PengajuanKerjasama pengajuanKerjasama = pengajuanKerjasamaRepository.findByIdPeker(Long.parseLong(pekerId));
                if(pengajuanKerjasama == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                if(!fileValidation.existDocument(filename, pengajuanKerjasama.getDocPengajuan())){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                if(!pengajuanKerjasama.getCreatedBy().equals(user.getId())){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            }

            Resource resource = new FileSystemResource("files/uploads/kerjasama/" + filename);
            if(!resource.exists()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Try to determine file's content type
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                // log error
            }

            // Fallback to the default content type if type could not be determined
            if(contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }catch (Exception e){
            if(e instanceof ExpiredJwtException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }else if(e instanceof IOException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @GetMapping("/kerjasama/download/form-kerjasama")
    @ResponseBody
    public ResponseEntity<Resource> downloadFormKerjasama(@RequestParam String filename,
                                                          @RequestParam(name = "token") String token,
                                                          @RequestParam(name = "id") String pekerId,
                                                          HttpServletRequest request) {
        PengajuanKerjasama pengajuanKerjasama = null;
        try {
            if(jwtUtility.isTokenExpired(token)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            User user = userValidation.getUserFromToken(token);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if(user.getAuthPrincipal() == Roles.MITRA){
                pengajuanKerjasama = pengajuanKerjasamaRepository.findByIdPeker(Long.parseLong(pekerId));
                if(pengajuanKerjasama == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                if(!fileValidation.existDocument(filename, pengajuanKerjasama.getDocPengajuan())){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                if(!pengajuanKerjasama.getCreatedBy().equals(user.getId())){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            }

//            PengajuanKerjasama kerjasama = pengajuanKerjasamaRepository.findById(id).get();
            DocKerjasama docKerjasama = pengajuanKerjasama.getDocPengajuan();
//            // list of file paths for download
//            List<String> paths = new ArrayList<>();
//            paths.add("files/uploads/kerjasama/"+docKerjasama.getSrtPernyataanPengajuan());

            Resource resource = new FileSystemResource("files/uploads/kerjasama/" + docKerjasama.getSrtPernyataanPengajuan());
            if(!resource.exists()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Try to determine file's content type
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                // log error
            }

            // Fallback to the default content type if type could not be determined
            if(contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }catch (Exception e){
            if(e instanceof ExpiredJwtException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }else if(e instanceof IOException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @PostMapping("/kerjasama/all")
    public ResponseEntity<String> uploadAllFilesKerjasama(@RequestParam("files") List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (file.getSize() > 20 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("Ukuran file terlalu besar");
            }
        }

        for (MultipartFile file : files) {
            storageService.save(file, FileGroup.KERJASAMA);
        }

        return ResponseEntity.ok("File berhasil diupload");
    }

    @GetMapping("/kerjasama/all/download")
    public ResponseEntity<StreamingResponseBody> downloadZip(HttpServletResponse response,
                                                             @RequestParam(name = "id") Long id,
                                                             @RequestParam(name = "token") String token){

        if(token.equals("") || token == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            if(jwtUtility.isTokenExpired(token)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            User user = userValidation.getUserFromToken(token);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if(user.getAuthPrincipal() == Roles.MITRA){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

//        logger.info("download request for sampleId = {}", sampleId);
            PengajuanKerjasama kerjasama = pengajuanKerjasamaRepository.findById(id).get();
            DocKerjasama docKerjasama = kerjasama.getDocPengajuan();
            // list of file paths for download
            List<String> paths = new ArrayList<>();
            paths.add("files/uploads/kerjasama/"+docKerjasama.getSrtPernyataanPengajuan());
            paths.add("files/uploads/kerjasama/"+docKerjasama.getSrtKetKemenkumham());
            paths.add("files/uploads/kerjasama/"+docKerjasama.getAktaPendirianPerubahan());
            paths.add("files/uploads/kerjasama/"+docKerjasama.getCompanyProfile());
            paths.add("files/uploads/kerjasama/"+docKerjasama.getCompanyNpwp());
            paths.add("files/uploads/kerjasama/"+docKerjasama.getFotoKtpPengurus());
            paths.add("files/uploads/kerjasama/"+docKerjasama.getFotoKtpPM());
            paths.add("files/uploads/kerjasama/"+docKerjasama.getSrtSiup());
            paths.add("files/uploads/kerjasama/"+docKerjasama.getNib());
            paths.add("files/uploads/kerjasama/"+docKerjasama.getHasilSandbox());
            paths.add("files/uploads/kerjasama/"+docKerjasama.getBukuTabungan());
            paths.add("files/uploads/kerjasama/"+docKerjasama.getPublicKey());
//        List<String> paths = Arrays.asList("/home/Videos/part1.mp4",
//                "/home/Videos/part2.mp4",
//                "/home/Videos/part3.mp4",
//                "/home/Videos/part4.pp4");
            Date now = new Date();
            String filename =  RandomStringUtils.randomAlphanumeric(16)+"-doc-kerjasama.zip";;

            int BUFFER_SIZE = 1024;

            StreamingResponseBody streamResponseBody = out -> {

                final ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
                ZipEntry zipEntry = null;
                InputStream inputStream = null;

                try {
                    for (String path : paths) {
                        File file = new File(path);
                        zipEntry = new ZipEntry(file.getName());

                        inputStream = new FileInputStream(file);

                        zipOutputStream.putNextEntry(zipEntry);
                        byte[] bytes = new byte[BUFFER_SIZE];
                        int length;
                        while ((length = inputStream.read(bytes)) >= 0) {
                            zipOutputStream.write(bytes, 0, length);
                        }

                    }
                    // set zip size in response
                    response.setContentLength((int) (zipEntry != null ? zipEntry.getSize() : 0));
                } catch (IOException e) {
                    System.out.println("Exception while reading and streaming data {} "+ e);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (zipOutputStream != null) {
                        zipOutputStream.close();
                    }
                }

            };

            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename="+filename);
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Expires", "0");

            return ResponseEntity.ok(streamResponseBody);
        }catch (Exception e){
            if(e instanceof ExpiredJwtException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }else if(e instanceof IOException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
