package dev.nincodedo.mcserverdescption;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class MotDModifier {

    private Properties properties;
    private String descriptionTemplate;
    private String descriptionTemplateStart;
    private boolean use24HourClock;

    public MotDModifier(Properties properties, String descriptionTemplate, String descriptionTemplateStart,
            boolean use24HourClock) {
        this.properties = properties;
        this.descriptionTemplate = descriptionTemplate;
        this.descriptionTemplateStart = descriptionTemplateStart;
        this.use24HourClock = use24HourClock;
    }

    public void updateMotD(MinecraftServer server) {
        if (server.isDedicatedServer()) {
            String motd = server.getMotd();
            if (motd != null && motd.contains("\n" + descriptionTemplateStart)) {
                motd = motd.substring(0, motd.indexOf("\n" + descriptionTemplateStart));
            }
            ServerLevel overworld = server.getLevel(Level.OVERWORLD);
            if (overworld != null) {
                long dayCount = overworld.getDayTime() / 24000;
                String timeString = getTimeString(overworld.getDayTime(), use24HourClock);
                String weatherStatus = getWeatherStatus(overworld.isRaining(), overworld.isThundering());

                String stringBuilder = descriptionTemplate;
                stringBuilder = stringBuilder.replace("{dayCount}", String.valueOf(dayCount));
                stringBuilder = stringBuilder.replace("{timeString}", timeString);
                stringBuilder = stringBuilder.replace("{weatherStatus}", weatherStatus);
                stringBuilder = motd + "\n" + stringBuilder;

                server.setMotd(stringBuilder);
            }
        }
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
