package ru.rseu.gorkin.file;

import ru.rseu.gorkin.configuration.AppConfiguration;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class FileUtils {
    public static boolean isBackupFileExists() {
        Path path = null;
        path = Path.of(
                AppConfiguration.INSTANCE.getPathToResourcesFolder()
                        + AppConfiguration.INSTANCE.getProperty("src.backupGameFile")
        );
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            return true;
        } else {
            return false;
        }
    }
}
