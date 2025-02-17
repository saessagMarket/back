package com.market.saessag.util;

import java.util.HashMap;
import java.util.Map;

public class FileTypeUtil {
    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("mp4", "video/mp4");
        MIME_TYPES.put("mov", "video/quicktime");
        MIME_TYPES.put("avi", "video/x-msvideo");
        MIME_TYPES.put("pdf", "application/pdf");
        MIME_TYPES.put("txt", "text/plain");
    }

    public static String getMimeType(String fileUrl) {
        String extension = getFileExtension(fileUrl);
        return MIME_TYPES.getOrDefault(extension.toLowerCase(), "application/octet-stream");
    }

    private static String getFileExtension(String fileUrl) {
        int lastDotIndex = fileUrl.lastIndexOf(".");
        return (lastDotIndex == -1) ? "" : fileUrl.substring(lastDotIndex + 1);
    }
}
