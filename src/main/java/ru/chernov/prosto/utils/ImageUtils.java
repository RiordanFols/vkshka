package ru.chernov.prosto.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * @author Pavel Chernov
 */
public class ImageUtils {

    public static boolean isImageTypeAllowed(MultipartFile multipartFile) {
        if (multipartFile == null
                || multipartFile.isEmpty()
                || StringUtils.isEmpty(multipartFile.getOriginalFilename()))
            return false;

        String contentType = multipartFile.getContentType();
        if (contentType == null)
            return false;

        return Arrays.asList("image/png", "image/jpg", "image/jpeg", "image/gif").contains(contentType);
    }

}
