package openkfc.mcmodslash.core;

import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = MCMODSplash.MODID,
        name = MCMODSplash.NAME,
        version = MCMODSplash.VERSION,
        clientSideOnly = true
)
public class MCMODSplash {
    public static final String MODID = "mcmodslash";
    public static final String NAME = "MCMOD-Slash";
    public static final String VERSION = "0.1.0";
    @Mod.Instance
    public MCMODSplash instance;

}
