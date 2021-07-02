package ru.chernov.prosto.utils;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Pavel Chernov
 */
public class ImageUtils {

    public static boolean isImageTypeAllowed(MultipartFile multipartFile) {
        if (multipartFile == null
                || multipartFile.isEmpty()
                || multipartFile.getOriginalFilename() == null
                || multipartFile.getOriginalFilename().isEmpty())
            return false;

        String contentType = multipartFile.getContentType();
        return (contentType != null && (
                (contentType.equals("image/png"))
                        || (contentType.equals("image/jpg"))
                        || (contentType.equals("image/jpeg"))
                        || (contentType.equals("image/gif"))));
    }

}
