package com.xiaguliuxiang.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.lang.instrument.IllegalClassFormatException;
import java.util.Arrays;
import java.util.Base64;

public class DSAPublicKeyImplTransformer {

    private static final String ORI_DSA_PUBLIC_KEY_BASE64 = "MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAJzCKYi3W8LGq0Dw10OB1H/ICqJ6MKs4KJYABa/rWus1wy4umHQn0s6Il764yTyFi34ft+kJau5NmG0pYw2hfCMS94TXnoruoxnovjstnpabpb9GIDOppAa7YxCxU5qZXW8gbrTz2EnTHXXv00WbYbClpOfda3T7rXlRlENqPtVo=";
    private static final String MY_DSA_PUBLIC_KEY_BASE64 = "MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAZkR/ADfOBvyfKSmpiMnjzd2xeSSIy5lUVp6DU5UPmdOJejc8wHsS8lBW2fBN5mJHxXDVI3DHe0WEyK7C3iqwNp13+NiXSJyTE1Ek5IuaQ7QjUd8oQ+epZ6HjgTmwaC5pePlDxDxDjsvcsPy32IzV2Nm7DHH03asV0lF0gqI+Ees=";
    private static final byte[] ORI_DSA_PUBLIC_KEY_BYTE = Base64.getDecoder().decode(ORI_DSA_PUBLIC_KEY_BASE64);
    private static final byte[] MY_DSA_PUBLIC_KEY_BYTE = Base64.getDecoder().decode(MY_DSA_PUBLIC_KEY_BASE64);

    public static byte[] transform(byte[] classFileBuffer) throws IllegalClassFormatException {
        try {
            ClassReader cr = new ClassReader(classFileBuffer);
            ClassNode cn = new ClassNode(Opcodes.ASM9);
            cr.accept(cn, 0);

            for (MethodNode mn : cn.methods) {
                if ("<init>".equals(mn.name) && "([B)V".equals(mn.desc)) {
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/xiaguliuxiang/transformer/DSAPublicKeyImplTransformer", "hook", "([B)[B", false));
                    list.add(new VarInsnNode(Opcodes.ASTORE, 1));
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
    public static byte[] hook(byte[] encodedKey) {
        if (Arrays.equals(ORI_DSA_PUBLIC_KEY_BYTE, encodedKey)) {
            return MY_DSA_PUBLIC_KEY_BYTE;
        }
        return encodedKey;
    }

}
