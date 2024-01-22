package dev.nincodedo.mcserverdescption;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class McServerDescription implements ModInitializer {

    private Properties properties;

    @Override
    public void onInitialize() {
        Config.createConfig();
        properties = Config.getConfigProperties();
        String descriptionTemplate = properties.getProperty("description");
        String descriptionTemplateStart = descriptionTemplate.substring(0, descriptionTemplate.indexOf("{"));
        boolean use24HourClock = Boolean.parseBoolean(properties.getProperty("use24HourClock", "false"));
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.isRemote()) {
                String motd = server.getServerMotd();
                if (motd != null && motd.contains("\n" + descriptionTemplateStart)) {
                    motd = motd.substring(0, motd.indexOf("\n" + descriptionTemplateStart));
                }
                World overworld = server.getOverworld();
                if (overworld != null) {
                    long dayCount = overworld.getTimeOfDay() / 24000;
                    String timeString = getTimeString(overworld.getTimeOfDay(), use24HourClock);
                    String weatherStatus = getWeatherStatus(overworld.isRaining(), overworld.isThundering());

                    String stringBuilder = descriptionTemplate;
                    stringBuilder = stringBuilder.replace("{dayCount}", String.valueOf(dayCount));
                    stringBuilder = stringBuilder.replace("{timeString}", timeString);
                    stringBuilder = stringBuilder.replace("{weatherStatus}", weatherStatus);
                    stringBuilder = motd + "\n" + stringBuilder;

                    server.setMotd(stringBuilder);
                }
            }
        });
    }

    @NotNull
    private String getTimeString(long serverTimeOfDay, boolean use24HourClock) {
        long currentTime = serverTimeOfDay % 24000;
        int hour = (int) (currentTime / 1000 + 6);
        int minute = (int) (currentTime % 1000 * 60 / 1000);
        if (use24HourClock) {
            if (hour >= 24) {
                hour -= 24;
            }
            return String.format("%02d", hour) + ":" + String.format("%02d", minute);
        } else {
            String ampm = hour > 12 && hour <= 23 ? "PM" : "AM";
            if (hour == 0 || hour == 24) {
                hour = 12;
            }
            if (hour != 12) {
                hour = hour % 12;
            }
            return hour + ":" + String.format("%02d", minute) + " " + ampm;
        }
    }

    private String getWeatherStatus(boolean isRaining, boolean isThundering) {
        return isThundering ? properties.getProperty("thundering") : isRaining ? properties.getProperty("raining") :
                properties.getProperty("clear");
    }
}
