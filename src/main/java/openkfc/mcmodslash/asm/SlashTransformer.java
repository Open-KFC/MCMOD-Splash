package openkfc.mcmodslash.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;

public class SlashTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(Objects.equals(transformedName, "net.minecraft.client.gui.GuiMainMenu")){
            return this.patchGuiMainMenu(basicClass);
        }
        return basicClass;
    }

    public byte[] patchGuiMainMenu(byte[] basicClass){
        if(!slashTextFormMCMOD()){return basicClass;}

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        MethodNode methodInit = null;
        for(MethodNode mn : classNode.methods){
            if(Objects.equals(mn.name, "<init>")){
                methodInit = mn;
                break;
            }
        }
        if(methodInit != null){
            for(int i = 0; i < methodInit.instructions.size(); ++i){
                AbstractInsnNode ain = methodInit.instructions.get(i);
                if(ain != null && ain.getType() == 15 && ((LineNumberNode)ain).line == 103){
                    for(int j = 4; j < 11; j++){
                        methodInit.instructions.remove(methodInit.instructions.get(i + 3));
                    }
                    methodInit.instructions.insertBefore(methodInit.instructions.get(i + 3), new MethodInsnNode(184, "openkfc/mcmodslash/asm/SlashTransformer", "getSlashText", "()Ljava/lang/String;", false));
                    break;
                }
            }
        }
        ClassWriter writer = new ClassWriter(COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public static String slashText = "MCMODSlash-Empty";

    public static String getSlashText(){
        return SlashTransformer.slashText;
    }

    public static Boolean slashTextFormMCMOD(){
        boolean result = false;
        try {
            URL url = new URL("https://www.mcmod.cn/v1/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    SlashTransformer.slashText = content.toString().replaceAll("<[^>]*>", "");
                    result = true;
                }
            }
            connection.disconnect();
        } catch (Exception ignored) {}
        return result;
    }

}
