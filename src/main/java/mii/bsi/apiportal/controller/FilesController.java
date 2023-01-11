package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.model.FileGroup;
import mii.bsi.apiportal.domain.model.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import mii.bsi.apiportal.service.FilesStorageService;
import mii.bsi.apiportal.utils.ResponseHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api/files")
public class FilesController {
    @Autowired
    FilesStorageService storageService;

    @PostMapping("/service-api")
    public ResponseEntity<ResponseHandling> uploadFile(@RequestParam("file") MultipartFile file) {
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
}
