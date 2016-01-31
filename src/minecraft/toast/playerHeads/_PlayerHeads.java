package toast.playerHeads;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import java.util.Random;
import net.minecraftforge.common.Configuration;

@Mod(modid = "PlayerHeads", name = "Player Heads", version = "1.1")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class _PlayerHeads
{
    /// If true, this mod starts up in debug mode.
	public static final boolean debug = false;
    /// If true, this mod starts up in debug mode.
	public static final Random random = new Random();
    /// The drop rates for heads.
    public static double[] dropRates = new double[] { 0.025, -1.0, 0.02, 0.1, 0.025 };
    /// If true, player heads will only drop from players killed by players.
    public static boolean pvp = true;
    
    /// Called before initialization. Loads the properties/configurations.
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        debugConsole("Loading in debug mode!");
        initProperties(new Configuration(event.getSuggestedConfigurationFile()));
    }
    
    /// Called during initialization. Registers entities, mob spawns, and renderers.
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new EventHandler();
    }
    
    /// Initializes the mod's config file.
	private void initProperties(Configuration config) {
        config.load();
        dropRates[3] = config.get("general", "drop_rate", dropRates[3], "The chance (from 0 to 1) that a player will drop his or her head when killed.").getDouble(dropRates[3]);
        pvp = config.get("general", "PvP-only", pvp, "If this is true, players will only drop their head when killed by another player.").getBoolean(pvp);
        
        dropRates[0] = config.get("mob_heads", "skeleton", dropRates[0], "The chance (from 0 to 1) that a skeleton will drop its skull when killed.").getDouble(dropRates[0]);
        dropRates[2] = config.get("mob_heads", "zombie", dropRates[2], "The chance (from 0 to 1) that a zombie will drop its head when killed.").getDouble(dropRates[2]);
        dropRates[4] = config.get("mob_heads", "creeper", dropRates[4], "The chance (from 0 to 1) that a creeper will drop its head when killed.").getDouble(dropRates[4]);
        config.save();
    }
    
    /// Prints the message to the console with this mod's name tag if debugging is enabled.
    public static void debugConsole(String... messages) {
        if (debug) {
            String message = "[PlayerHeads] ";
            for (String part : messages)
                message += part;
            System.out.println(message);
        }
    }
    
    /// Throws a runtime exception with a message and this mod's name tag if debugging is enabled.
    public static void debugException(String... messages) {
        if (debug) {
            String message = "[PlayerHeads] ";
            for (String part : messages)
                message += part;
            throw new RuntimeException(message);
        }
    }
}