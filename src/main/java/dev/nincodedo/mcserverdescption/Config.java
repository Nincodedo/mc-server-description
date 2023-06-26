package dev.nincodedo.mcserverdescption;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private static final String PROPERTY_FILE_NAME = "mcserverdescription.properties";

    public static void createConfig() {
        try {
            File config = new File(String.valueOf(FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve(PROPERTY_FILE_NAME)));
            if (config.createNewFile()) {
                writeDefault();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to create the config file.", e);
        }
    }

    private static void writeDefault() {
        try (FileWriter configWriter = new FileWriter(String.valueOf(FabricLoader.getInstance()
                .getConfigDir()
                .resolve(PROPERTY_FILE_NAME)), StandardCharsets.UTF_8)) {
            configWriter.write("# MC Server Description Config\n");
            configWriter.write("description=§fCurrent Day: {dayCount} - Time: {timeString} - Weather: "
                    + "{weatherStatus}\n");
            configWriter.write("thundering=Thundering\n");
            configWriter.write("raining=Raining\n");
            configWriter.write("clear=Clear\n");
        } catch (IOException e) {
            LOGGER.error("Failed to write default config file.", e);
        }
    }

    public static Properties getConfigProperties() {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(FabricLoader.getInstance()
                .getConfigDir()
                .resolve(PROPERTY_FILE_NAME)
                .toFile())) {
            properties.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Failed to get the config properties.", e);
        }
        return properties;
    }
}
