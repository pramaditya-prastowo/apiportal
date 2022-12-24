package mii.bsi.apiportal.controller;

import mii.bsi.apiportal.domain.model.FileGroup;
import mii.bsi.apiportal.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@RestController
public class SwaggerController {

    @Autowired
    private FilesStorageService storageService;

    @GetMapping("/swagger/{json}")
    public String getSwagger(@PathVariable("json") String json){

        try {
            Resource file = storageService.load(json, FileGroup.SERVICE_API);
            File resource = file.getFile();
            String result = new String(
                    Files.readAllBytes(resource.toPath()));
            return result;
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
