package utils;

import exceptions.InvalidPathForFilesException;

import java.io.File;
import java.nio.file.Paths;

public class FileUtils {
    public static String getFilePath(String fileName){
        return Paths.get("data/" + fileName)
                .toAbsolutePath()
                .toString();
    }

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static void validateFileExists(String filePath) {
        if (!fileExists(filePath)) {
            throw new InvalidPathForFilesException("File not found: " + filePath +
                "\nPlease create the file or check the path.");
        }
    }
}
