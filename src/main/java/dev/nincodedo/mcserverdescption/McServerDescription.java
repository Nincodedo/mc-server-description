package dev.nincodedo.mcserverdescption;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class McServerDescription implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.isRemote()) {
                String motd = server.getServerMotd();
                World overworld = server.getOverworld();
                if (overworld != null) {
                    long dayCount = overworld.getTimeOfDay() / 24000;
                    String timeString = getTimeString(overworld.getTimeOfDay());
                    String weatherStatus = getWeatherStatus(overworld.isRaining(), overworld.isThundering());

                    String stringBuilder = motd
                            + "\n§fCurrent Day: "
                            + dayCount
                            + " - "
                            + "Time: "
                            + timeString
                            + " - "
                            + "Weather: "
                            + weatherStatus;

                    server.getServerMetadata().setDescription(Text.literal(stringBuilder));
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
        return isThundering ? "Thundering" : isRaining ? "Raining" : "Clear";
    }
}
