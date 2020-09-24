package com.xiaguliuxiang.crack.dbvisualizer.agent;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author xiaguliuxiang@gmail.com
 * @date 2019-09-09 20:00:00
 */
public class KeyTransformer implements ClassFileTransformer {
    private static final String ENCODED_KEY_SPEC = "sun/security/provider/DSAPublicKeyImpl";
    private static final String JAVA_NET_URI = "java/net/URI";
    private static final String JAVA_NET_URL = "java/net/URL";
    private static final String BLACKLIST_CLASS = "com/onseven/dbvis(/n/h)";

    private static final String RAW_KEY = "MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAJzCKYi3W8LGq0Dw10OB1H/ICqJ6MKs4KJYABa/rWus1wy4umHQn0s6Il764yTyFi34ft+kJau5NmG0pYw2hfCMS94TXnoruoxnovjstnpabpb9GIDOppAa7YxCxU5qZXW8gbrTz2EnTHXXv00WbYbClpOfda3T7rXlRlENqPtVo=";
    private static final String XIAGULIUXIANG_KEY = "MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAZkR/ADfOBvyfKSmpiMnjzd2xeSSIy5lUVp6DU5UPmdOJejc8wHsS8lBW2fBN5mJHxXDVI3DHe0WEyK7C3iqwNp13+NiXSJyTE1Ek5IuaQ7QjUd8oQ+epZ6HjgTmwaC5pePlDxDxDjsvcsPy32IzV2Nm7DHH03asV0lF0gqI+Ees=";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className == null) {
            return classfileBuffer;
        }

        if (className.equals(ENCODED_KEY_SPEC)) {
            return handleKeySpec(classfileBuffer);
        }

        if (className.equals(JAVA_NET_URI)) {
            return handleNetwork(classfileBuffer);
        }

        if (className.equals(JAVA_NET_URL)) {
            return handleNetwork(classfileBuffer);
        }

        if (className.matches(BLACKLIST_CLASS)) {
            return handleBlacklist(classfileBuffer);
        }

        return classfileBuffer;
    }

    private byte[] handleKeySpec(byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            String b64f;
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

            classPool.importPackage("java.util.Arrays");
            try {
                Class.forName("java.util.Base64");
                classPool.importPackage("java.util.Base64");
                b64f = "Base64.getDecoder().decode";
            } catch (ClassNotFoundException e) {
                try {
                    Class.forName("javax.xml.bind.DatatypeConverter");
                    classPool.importPackage("javax.xml.bind.DatatypeConverter");
                    b64f = "DatatypeConverter.parseBase64Binary";
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }

            ctClass.addField(CtField.make("private static final byte[] __h_ok=" + b64f + "(\"" + RAW_KEY + "\");", ctClass));
            ctClass.addField(CtField.make("private static final byte[] __h_nk=" + b64f + "(\"" + XIAGULIUXIANG_KEY + "\");", ctClass));
            CtConstructor ctConstructor = ctClass.getConstructor("([B)V");
            ctConstructor.insertBefore("if(Arrays.equals($1,__h_ok)){$1=__h_nk;}");

            return ctClass.toBytecode();
        } catch (Exception e) {
            throw new IllegalClassFormatException(e.getMessage());
        }
    }

    private byte[] handleNetwork(byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

            CtConstructor ctConstructor = ctClass.getConstructor("(Ljava/lang/String;)V");
            ctConstructor.insertBefore("if($1.contains(\"dbvis.com\")){$1=$1.replaceAll(\"(www.)dbvis.com\",\"127.0.0.1\");}");

            return ctClass.toBytecode();
        } catch (Exception e) {
            throw new IllegalClassFormatException(e.getMessage());
        }
    }

    private byte[] handleBlacklist(byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

            CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
            CtClass propertiesClass = classPool.get("java.util.Properties");
            for (CtMethod ctMethod : declaredMethods) {
                CtClass[] parameterTypes = ctMethod.getParameterTypes();
                String returnTypeName = ctMethod.getReturnType().getName();
                boolean isBlacklistMethod = (null != parameterTypes) && (parameterTypes.length == 1) && (parameterTypes[0] == propertiesClass);
                isBlacklistMethod = isBlacklistMethod && ("boolean".equals(returnTypeName));
                if (isBlacklistMethod) {
                    ctMethod.setBody("{return false;}");
                }
            }

            return ctClass.toBytecode();
        } catch (Exception e) {
            throw new IllegalClassFormatException(e.getMessage());
        }
    }

}
