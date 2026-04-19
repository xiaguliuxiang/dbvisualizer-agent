package com.xiaguliuxiang.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.IllegalClassFormatException;
import java.util.Properties;

public class BlockedLicenseTransformer {

    static boolean b(Properties properties) {
        return false;
    }

    public static byte[] transform(byte[] classFileBuffer) throws IllegalClassFormatException {
        try {
            ClassReader cr = new ClassReader(classFileBuffer);
            ClassNode cn = new ClassNode(Opcodes.ASM9);
            cr.accept(cn, 0);

            for (MethodNode mn : cn.methods) {
                if (Opcodes.ACC_STATIC == mn.access && "(Ljava/util/Properties;)Z".equals(mn.desc)) {
                    mn.instructions.clear();
                    InsnList list = new InsnList();
                    list.add(new InsnNode(Opcodes.ICONST_0));
                    list.add(new InsnNode(Opcodes.IRETURN));
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
}
