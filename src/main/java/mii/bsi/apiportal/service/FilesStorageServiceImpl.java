package mii.bsi.apiportal.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.FileGroup;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path root = Paths.get("files/uploads");
    private final Path serviceApiIcon = Paths.get("files/uploads/service-api/icon");
    private final Path serviceApiSwagger = Paths.get("files/uploads/service-api/swagger");
    private final Path iconMyApps = Paths.get("files/uploads/apps/icon");
    private final Path bannerPromo = Paths.get("files/uploads/promo/banner");
    private final Path kerjasama = Paths.get("files/uploads/kerjasama");
    private final Path fileDownload = Paths.get("files/download");
    private final Path fileProfile = Paths.get("files/profile");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
            Files.createDirectories(serviceApiIcon);
            Files.createDirectories(serviceApiSwagger);
            Files.createDirectories(iconMyApps);
            Files.createDirectories(bannerPromo);
            Files.createDirectories(fileDownload);
            Files.createDirectories(kerjasama);
            Files.createDirectories(fileProfile);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file, FileGroup fileGroup) {
        String extension = file.getOriginalFilename(). substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        System.out.println("file extension "+extension);
        try {
            switch (fileGroup) {
                case SERVICE_API:
                    if(extension.equals("json")){
                        Files.copy(file.getInputStream(), this.serviceApiSwagger.resolve(file.getOriginalFilename()));
                    }else{
                        Files.copy(file.getInputStream(), this.serviceApiIcon.resolve(file.getOriginalFilename()));
                    }
                    break;
                case MY_APPLICATION:
                    Files.copy(file.getInputStream(), this.iconMyApps.resolve(file.getOriginalFilename()));
                    break;
                case PROMO:
                    Files.copy(file.getInputStream(), this.bannerPromo.resolve(file.getOriginalFilename()));
                case KERJASAMA:
                    Files.copy(file.getInputStream(), this.kerjasama.resolve(file.getOriginalFilename()));
                    break;
            }

        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void save(MultipartFile file, FileGroup fileGroup, User user) {
        String extension = file.getOriginalFilename(). substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        System.out.println("file extension "+extension);
        String pathUser = user.getId();
        try {
            switch (fileGroup) {
                case SERVICE_API:
                    if(extension.equals("json")){
                        Files.copy(file.getInputStream(), this.serviceApiSwagger.resolve(file.getOriginalFilename()));
                    }else{
                        Files.copy(file.getInputStream(), this.serviceApiIcon.resolve(file.getOriginalFilename()));
                    }
                    break;
                case MY_APPLICATION:
                    Files.copy(file.getInputStream(), this.iconMyApps.resolve(file.getOriginalFilename()));
                    break;
                case PROMO:
                    Files.copy(file.getInputStream(), this.bannerPromo.resolve(file.getOriginalFilename()));
                    break;
                case KERJASAMA:
                    Files.copy(file.getInputStream(), this.kerjasama.resolve(file.getOriginalFilename()));
                    break;
                case PROFILE:
                    Path path = Paths.get("files/profile/"+pathUser);
                    System.out.println("files/profile/"+pathUser);
                    Files.createDirectories(path);

                    Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
                    break;
            }

        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public Resource load(String filename, FileGroup fileGroup) {
        try {
            Path file = resourceFrom(fileGroup, filename);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename, FileGroup fileGroup, User user) {
        try {

            Path file = resourceFrom(fileGroup, filename, user);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public Path resourceFrom(FileGroup fileGroup, String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1);
        switch (fileGroup) {
            case SERVICE_API:
                Path file;
                if(extension.equals("json")){
                    file =   serviceApiSwagger.resolve(filename);
                }else{
                    file =  serviceApiIcon.resolve(filename);
                }
                return file;
            case MY_APPLICATION:
                return iconMyApps.resolve(filename);
            case PROMO:
                return bannerPromo.resolve(filename);
            case KERJASAMA:
                return kerjasama.resolve(filename);
            case PROFILE:
                String [] filenames = filename.split("/");
                Path path = Paths.get("files/profile/"+ filenames[0]);
                return path.resolve(filenames[1]);
        }
        return null;
    }

    @Override
    public Path resourceFrom(FileGroup fileGroup, String filename, User user) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1);
        switch (fileGroup) {
            case SERVICE_API:
                Path file;
                if(extension.equals("json")){
                    file =   serviceApiSwagger.resolve(filename);
                }else{
                    file =  serviceApiIcon.resolve(filename);
                }
                return file;
            case MY_APPLICATION:
                return iconMyApps.resolve(filename);
            case PROMO:
                return bannerPromo.resolve(filename);
            case KERJASAMA:
                return kerjasama.resolve(filename);
            case PROFILE:
                Path path = Paths.get("files/profile/"+ user.getId());
                return path.resolve(filename);
        }
        return null;
    }
}
