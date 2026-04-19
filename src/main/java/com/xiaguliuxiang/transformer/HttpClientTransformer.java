package com.xiaguliuxiang.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.lang.instrument.IllegalClassFormatException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpClientTransformer {

    private static final List<String> HOOK_URL_LIST = new ArrayList<>();

    static {
        HOOK_URL_LIST.add("https://www.dbvis.com/dbv/clientapi/");
        HOOK_URL_LIST.add("https://clientapi.dbvis.com/");
    }

    public static byte[] transform(byte[] classFileBuffer) throws IllegalClassFormatException {
        try {
            ClassReader cr = new ClassReader(classFileBuffer);
            ClassNode cn = new ClassNode(Opcodes.ASM9);
            cr.accept(cn, 0);

            for (MethodNode mn : cn.methods) {
                if ("openServer".equals(mn.name) && "()V".equals(mn.desc)) {
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    list.add(new FieldInsnNode(Opcodes.GETFIELD, "sun/net/www/http/HttpClient", "url", "Ljava/net/URL;"));
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/xiaguliuxiang/transformer/HttpClientTransformer", "hook", "(Ljava/net/URL;)V", false));
                    mn.instructions.insert(list);
                }
            }

            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            cn.accept(writer);
            return writer.toByteArray();
        } catch (Exception e) {
            throw new IllegalClassFormatException(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public static void hook(URL url) throws SocketTimeoutException {
        if (url != null && url.toString() != null && !(url.toString().isEmpty())) {
            String urlStr = url.toString();
            for (String str : HOOK_URL_LIST) {
                if (urlStr.contains(str)) {
                    throw new SocketTimeoutException("HttpClient.openServer(): SocketException: not available");
                }
            }
        }
    }

}
