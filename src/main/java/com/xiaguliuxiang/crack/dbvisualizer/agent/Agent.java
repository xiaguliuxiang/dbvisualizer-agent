package com.xiaguliuxiang.crack.dbvisualizer.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author xiaguliuxiang@gmail.com
 * @date 2019-09-09 20:00:00
 */
public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        try {
            inst.addTransformer(new KeyTransformer());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
