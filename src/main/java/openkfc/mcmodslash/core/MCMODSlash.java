package openkfc.mcmodslash.core;

import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = MCMODSlash.MODID,
        name = MCMODSlash.NAME,
        version = MCMODSlash.VERSION,
        clientSideOnly = true
)
public class MCMODSlash {
    public static final String MODID = "mcmodslash";
    public static final String NAME = "MCMOD-Slash";
    public static final String VERSION = "0.1.0";
    @Mod.Instance
    public MCMODSlash instance;

}
