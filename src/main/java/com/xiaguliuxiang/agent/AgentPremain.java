package com.xiaguliuxiang.agent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.net.URL;
import java.util.jar.JarFile;

public class AgentPremain {

    private static final String RESOURCE = "d9580d1c51a16fb89b884300e3bc2b4a.properties";

    public static void premain(String agentArgs, Instrumentation inst) {
        try {
            JarFile jarFile = new JarFile(getAgentPackageUri().getPath());
            inst.appendToBootstrapClassLoaderSearch(jarFile);
        } catch (Throwable e) {
            return;
        }

        ClassHookTransformer classHookTransformer = new ClassHookTransformer();
        inst.addTransformer(classHookTransformer, true);
        inst.setNativeMethodPrefix(classHookTransformer, "$$$native$$$");

        for (Class<?> clazz : inst.getAllLoadedClasses()) {
            String className = clazz.getName();
            if (!ClassHookTransformer.HOOK_CLASS_NAME_SET.contains(className)) {
                continue;
            }
            try {
                Object ignored = clazz.getGenericSuperclass();
                inst.retransformClasses(clazz);
            } catch (Throwable ignore) {
            }
        }
    }

    private static URI getAgentPackageUri() throws Exception {
        URL url = AgentPremain.class.getProtectionDomain().getCodeSource().getLocation();
        if (url != null) {
            return url.toURI();
        }
        URL resource = AgentPremain.class.getClassLoader().getResource(RESOURCE);
        if (resource == null) {
            throw new IOException("failed to locate agent jar file, because the resource url is null.");
        }
        String path = resource.getPath();
        if (!path.endsWith("!/" + RESOURCE)) {
            throw new IOException("failed to locate agent jar file, resource url: " + resource);
        }
        path = path.substring(0, path.length() - RESOURCE.length() - 2);
        return new URI(path);
    }

}
