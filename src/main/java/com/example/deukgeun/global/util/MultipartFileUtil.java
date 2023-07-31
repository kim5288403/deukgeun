package com.example.deukgeun.global.util;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class MultipartFileUtil {

    public static void deleteFileToDirectory(String fileName, String filePath) throws IOException {
        Path directory = Path.of(filePath);

        try (Stream<Path> pathStream = Files.find(directory, Integer.MAX_VALUE,
                (path, attributes) -> path.getFileName().toString().equals(fileName))) {
            pathStream.forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static String getUUIDPath(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("Empty fileName");
        }

        String uuid = UUID.randomUUID().toString();

        return uuid + "_" + fileName;
    }

    public static File getServerImage(String getRequestURI, String postFilePath) {
        String[] url = getRequestURI.split("/");
        String filename = url[url.length - 1];

        return new File(postFilePath, filename);
    }

    public static String getExtensionFromContentType(String contentType) {
        if (contentType != null) {
            int slashIndex = contentType.lastIndexOf("/");
            if (slashIndex != -1 && slashIndex < contentType.length() - 1) {
                return "." + contentType.substring(slashIndex + 1);
            }
        }
        return "";
    }

    public static String getFilePathFromUrl(String src, String filePath) {
        String[] url = src.split("/");
        String filename = url[url.length - 1];
        return filePath + "\\" + filename;
    }

    public static boolean isSupportedContentType(MultipartFile file) {
        String fileType = Objects.requireNonNull(file.getContentType());
        return fileType.equals("image/png") || fileType.equals("image/jpg") || fileType.equals("image/jpeg");
    }

    public static void saveFileToDirectory(MultipartFile profile, String fileName, String filePath) throws IOException {
        Path path = Paths.get(filePath).toAbsolutePath().normalize();

        Path targetPath = path.resolve(fileName).normalize();

        profile.transferTo(targetPath);
    }

    public static void saveServerImage(Part filePart, PrintWriter writer, File file) {
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath());
        } catch (Exception e) {
            writer.println("<br/> ERROR: " + e);
        }
    }

    public static void validMimeType(String mimeType, String name, String filePath) throws Exception {
        String[] allowedMimeTypes = new String[]{
                "image/gif",
                "image/jpeg",
                "image/pjpeg",
                "image/x-png",
                "image/png",
                "image/svg+xml"
        };

        boolean contains = Arrays.asList(allowedMimeTypes).contains(mimeType.toLowerCase());

        if (!contains) {
            deleteFileToDirectory(name, filePath);

            throw new Exception("Image does not meet the validation.");
        }
    }

    public static void validContentType(String contentType) throws Exception {
        if (contentType == null ||
                !contentType.toLowerCase().contains("multipart/form-data")) {

            throw new Exception("Invalid contentType. It must be multipart/form-data");
        }
    }
}
