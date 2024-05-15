package openkfc.mcmodslash.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

public class SlashPlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{SlashTransformer.class.getName()};
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override public String getModContainerClass() {return null;}

    @Nullable @Override public String getSetupClass() {return null;}

    @Override public String getAccessTransformerClass() {return null;}
}
