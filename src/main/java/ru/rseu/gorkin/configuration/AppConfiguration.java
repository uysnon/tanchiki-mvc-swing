package ru.rseu.gorkin.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public enum AppConfiguration {
    INSTANCE;

    private static final String PATH_TO_APP_CONFIGURATION = "src/main/resources/config.properties";
    private static final String PATH_TO_RESOURCE_FOLDER = "src/main/resources/";

    AppConfiguration() {
        try {
            properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(PATH_TO_APP_CONFIGURATION);
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Properties properties;

    public String getProperty(String key){
        return properties.getProperty(key);
    }
    public String getPathToResourcesFolder(){
        return PATH_TO_RESOURCE_FOLDER;
    }
}
