package com.xiaguliuxiang.agent;

import com.xiaguliuxiang.transformer.BlockedLicenseTransformer;
import com.xiaguliuxiang.transformer.DSAPublicKeyImplTransformer;
import com.xiaguliuxiang.transformer.HttpClientTransformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

public class ClassHookTransformer implements ClassFileTransformer {

    public static final Set<String> HOOK_CLASS_NAME_SET = new HashSet<>();

    static {
        HOOK_CLASS_NAME_SET.add("sun.security.provider.DSAPublicKeyImpl");
        HOOK_CLASS_NAME_SET.add("sun.net.www.http.HttpClient");
    }

    @Override
    public byte[] transform(
            ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classFileBuffer
    ) throws IllegalClassFormatException {
        if (className == null) {
            return classFileBuffer;
        }

        if ("sun/security/provider/DSAPublicKeyImpl".equals(className)) {
            return DSAPublicKeyImplTransformer.transform(classFileBuffer);
        }

        if ("sun/net/www/http/HttpClient".equals(className)) {
            return HttpClientTransformer.transform(classFileBuffer);
        }

        if ("com/onseven/dbvis/r/i".equals(className)) {
            return BlockedLicenseTransformer.transform(classFileBuffer);
        }

        return classFileBuffer;
    }

}
