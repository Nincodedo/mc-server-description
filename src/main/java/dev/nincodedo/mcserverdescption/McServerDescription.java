package dev.nincodedo.mcserverdescption;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class McServerDescription implements ModInitializer {

    @Override
    public void onInitialize() {
        Config.createConfig();
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.isRemote()) {
                String motd = server.getServerMotd();
                if (motd != null && motd.contains("\n§fCurrent Day: ")) {
                    motd = motd.substring(0, motd.indexOf("\n§fCurrent Day: "));
                }
                World overworld = server.getOverworld();
                if (overworld != null) {
                    long dayCount = overworld.getTimeOfDay() / 24000;
                    String timeString = getTimeString(overworld.getTimeOfDay());
                    String weatherStatus = getWeatherStatus(overworld.isRaining(), overworld.isThundering());

                    String stringBuilder = Config.getServerDescription();
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
    private String getTimeString(long serverTimeOfDay) {
        long currentTime = serverTimeOfDay % 24000;
        int hour = (int) (currentTime / 1000 + 6);
        int minute = (int) (currentTime % 1000 * 60 / 1000);
        String ampm = hour > 12 && hour <= 23 ? "PM" : "AM";
        if (hour == 0 || hour == 24) {
            hour = 12;
        }
        if (hour != 12) {
            hour = hour % 12;
        }
        return hour + ":" + String.format("%02d", minute) + " " + ampm;
    }

    private String getWeatherStatus(boolean isRaining, boolean isThundering) {
        //return isThundering ? "Thundering" : isRaining ? "Raining" : "Clear";
        Properties weather = Config.getConfigProperties();
        return isThundering ? weather.getProperty("thundering") : isRaining ? weather.getProperty("raining") : weather.getProperty("clear");
    }
}
