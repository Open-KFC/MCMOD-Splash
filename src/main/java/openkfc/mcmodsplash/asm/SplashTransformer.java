package openkfc.mcmodsplash.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SplashTransformer implements IClassTransformer {

    public Logger LOGGER = LogManager.getLogger("MCMOD-Splash");

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if("net.minecraft.client.gui.GuiMainMenu".equals(transformedName)){
            return this.patchGuiMainMenu(basicClass);
        }
        return basicClass;
    }

    public byte[] patchGuiMainMenu(byte[] basicClass){
        if(!splashTextFormMCMOD()){return basicClass;}

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        String splashFieldName = FMLDeobfuscatingRemapper.INSTANCE.mapFieldName("net/minecraft/client/gui/GuiMainMenu", "field_73975_c", "Ljava/lang/String;");
        String initGuiMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName("net/minecraft/client/gui/GuiMainMenu", "func_73866_w_", "()V");
        LOGGER.log(Level.INFO, "initGuiMethodName : " + initGuiMethodName);
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "openkfc/mcmodsplash/asm/SplashTransformer", "splashText", "Ljava/lang/String;"));
        insnList.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/gui/GuiMainMenu", splashFieldName, "Ljava/lang/String;"));

        for(MethodNode methodNode : classNode.methods){
            if("<init>".equals(methodNode.name)){
                for(int i = 0; i < methodNode.instructions.size(); ++i){
                    AbstractInsnNode ain = methodNode.instructions.get(i);
                    if(ain != null && ain.getOpcode() == Opcodes.RETURN){
                        methodNode.instructions.insertBefore(ain, insnList);
                        i += insnList.size();
                    }
                }
            }
            if(initGuiMethodName.equals(methodNode.name)){
                for(int i = 0; i < methodNode.instructions.size(); ++i){
                    AbstractInsnNode ain = methodNode.instructions.get(i);
                    if(ain != null && ain.getOpcode() == Opcodes.PUTFIELD && splashFieldName.equals(((FieldInsnNode)ain).name)){
                        methodNode.instructions.insert(ain, insnList);
                        i += insnList.size();
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public static String splashText = "MCMOD-Splash!";

    public static Boolean splashTextFormMCMOD(){
        boolean result = false;
        try {
            URL url = new URL("https://www.mcmod.cn/v1/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),StandardCharsets.UTF_8))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    splashText = content.toString().replaceAll("<[^>]*>", "");
                    result = true;
                }
            }
            connection.disconnect();
        } catch (Exception ignored) {}
        return result;
    }

}
