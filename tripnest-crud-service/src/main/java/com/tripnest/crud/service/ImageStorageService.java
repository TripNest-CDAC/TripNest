package com.tripnest.crud.service;

import com.tripnest.crud.entity.PackageImage;
import com.tripnest.crud.entity.TravelPackage;
import com.tripnest.crud.exception.ResourceNotFoundException;
import com.tripnest.crud.repository.PackageImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ImageStorageService {
    private final PackageImageRepository imageRepository;
    private final Path packageDirectory;
    public ImageStorageService(PackageImageRepository imageRepository,
            @Value("${app.upload.directory:uploads}") String uploadDirectory) throws IOException {
        this.imageRepository = imageRepository;
        this.packageDirectory = Path.of(uploadDirectory, "packages").toAbsolutePath().normalize();
        Files.createDirectories(packageDirectory);
    }
    @Transactional
    public List<String> savePackageImages(TravelPackage travelPackage, List<MultipartFile> files, boolean thumbnail) {
        if (files == null || files.isEmpty()) throw new IllegalArgumentException("Select at least one image");
        if (thumbnail) imageRepository.findAllByTravelPackagePackageIdOrderByImageIdAsc(travelPackage.getPackageId())
                .forEach(image -> { image.setThumbnail(false); imageRepository.save(image); });
        boolean first = true;
        for (MultipartFile file : files) {
            if (file.isEmpty() || file.getContentType() == null || !file.getContentType().startsWith("image/"))
                throw new IllegalArgumentException("Only image files can be uploaded");
            if (file.getSize() > 5 * 1024 * 1024) throw new IllegalArgumentException("Each image must be 5 MB or smaller");
            String extension = file.getOriginalFilename() != null && file.getOriginalFilename().contains(".")
                    ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')) : ".jpg";
            String filename = UUID.randomUUID() + extension.toLowerCase();
            try { Files.copy(file.getInputStream(), packageDirectory.resolve(filename), StandardCopyOption.REPLACE_EXISTING); }
            catch (IOException exception) { throw new IllegalStateException("Unable to store image", exception); }
            PackageImage image = new PackageImage(); image.setTravelPackage(travelPackage); image.setImagePath("/uploads/packages/" + filename);
            image.setThumbnail(thumbnail && first); imageRepository.save(image); first = false;
        }
        return imageRepository.findAllByTravelPackagePackageIdOrderByImageIdAsc(travelPackage.getPackageId())
                .stream().map(PackageImage::getImagePath).toList();
    }
    @Transactional
    public void deleteImage(Integer imageId, TravelPackage travelPackage) {
        PackageImage image = imageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("Package image was not found"));
        if (!image.getTravelPackage().getPackageId().equals(travelPackage.getPackageId())) throw new ResourceNotFoundException("Package image was not found");
        imageRepository.delete(image);
    }
}
