package dev.nincodedo.mcserverdescption;

import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class Config {
    public static void createConfig() {
        try {
            File config = new File(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("mcserverdescription.properties")));
            if (config.createNewFile()) {
                writeDefault();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private static void writeDefault() {
        try {
            FileWriter configWriter = new FileWriter(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("mcserverdescription.properties")));
            configWriter.write("# MC Server Description Config\n");
            configWriter.write("description=§fCurrent Day: {dayCount} - Time: {timeString} - Weather: {weatherStatus}\n");
            configWriter.write("thundering=Thundering\n");
            configWriter.write("raining=Raining\n");
            configWriter.write("clear=Clear\n");
            configWriter.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static String getServerDescription() {
        Properties properties = new Properties();
        String description;
        try {
            properties.load(Files.newInputStream(FabricLoader.getInstance().getConfigDir().resolve("mcserverdescription.properties")));
        } catch (IOException e) {

            e.printStackTrace();
        }
        description = properties.getProperty("description");

        return description;
    }

    public static Properties getConfigProperties() {
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(FabricLoader.getInstance().getConfigDir().resolve("mcserverdescription.properties")));
        } catch (IOException e) {

            e.printStackTrace();
        }
        return properties;
    }
}
