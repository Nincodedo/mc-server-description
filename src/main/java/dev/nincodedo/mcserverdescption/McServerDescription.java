package dev.nincodedo.mcserverdescption;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.Properties;

public class McServerDescription implements ModInitializer {

    private Properties properties;
    private MotDModifier motDModifier;

    @Override
    public void onInitialize() {
        Config.createConfig();
        properties = Config.getConfigProperties();
        String descriptionTemplate = properties.getProperty("description");
        String descriptionTemplateStart = descriptionTemplate.substring(0, descriptionTemplate.indexOf("{"));
        boolean use24HourClock = Boolean.parseBoolean(properties.getProperty("use24HourClock", "false"));
        motDModifier = new MotDModifier(properties, descriptionTemplate, descriptionTemplateStart, use24HourClock);
        ServerTickEvents.END_SERVER_TICK.register(server -> motDModifier.updateMotD(server));
    }
}
