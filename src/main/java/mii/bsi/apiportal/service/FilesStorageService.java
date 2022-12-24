package mii.bsi.apiportal.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import mii.bsi.apiportal.domain.model.FileGroup;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
    public void init();

    public void save(MultipartFile file, FileGroup fileGroup);

    public Resource load(String filename, FileGroup fileGroup);

    public void deleteAll();

    public Stream<Path> loadAll();

    public Path resourceFrom(FileGroup fileGroup, String filename);
}
