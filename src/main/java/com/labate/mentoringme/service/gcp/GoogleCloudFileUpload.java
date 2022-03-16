package com.labate.mentoringme.service.gcp;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobTargetOption;
import com.google.cloud.storage.Storage.PredefinedAcl;
import com.labate.mentoringme.exception.InvalidImageException;
import com.labate.mentoringme.validator.ImageFormatValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class GoogleCloudFileUpload {

  // get service by env var GOOGLE_APPLICATION_CREDENTIALS. Json file generated in API & Services ->
  // Service account key
  private final Storage storage;

  @Value("${gcp.bucket-name}")
  String bucketName;

  @Value("${gcp.subdirectory}")
  String subdirectory;

  public String upload(MultipartFile file) throws IOException {
    try {
      var extension = FilenameUtils.getExtension(file.getOriginalFilename());

      if (!ImageFormatValidator.validateExtension(extension)) {
        throw new InvalidImageException("format " + extension + " is not supported");
      }

      var fileName = generateFileName(extension);
      // Prepare the blobId
      // BlobId is a combination of bucketName + subdirectory(optional) + fileName
      final var blobId = constructBlobId(bucketName, subdirectory, fileName);
      var blobInfo =
          storage.create(
              BlobInfo.newBuilder(blobId).build(),
              file.getBytes(), // the file
              BlobTargetOption.predefinedAcl(PredefinedAcl.PUBLIC_READ) // Set file permission
              );
      var signedPathStyleUrl = createSignedPathStyleUrl(blobInfo, 10, TimeUnit.MINUTES);
      return signedPathStyleUrl.toString(); // Return file url
    } catch (IllegalStateException e) {
      throw new RuntimeException(e);
    }
  }

  private URL createSignedPathStyleUrl(BlobInfo blobInfo, int duration, TimeUnit timeUnit) {
    return storage.signUrl(blobInfo, duration, timeUnit, Storage.SignUrlOption.withPathStyle());
  }
  /**
   * Construct Blob ID
   *
   * @param bucketName
   * @param subdirectory optional
   * @param fileName
   * @return
   */
  private BlobId constructBlobId(
      String bucketName, @Nullable String subdirectory, String fileName) {
    return Optional.ofNullable(subdirectory)
        .map(s -> BlobId.of(bucketName, subdirectory + "/" + fileName))
        .orElse(BlobId.of(bucketName, fileName));
  }

  // Random image file name generation
  private String generateFileName(String fileExtension) {
    return UUID.randomUUID() + "-" + System.currentTimeMillis() + "." + fileExtension;
  }
}
