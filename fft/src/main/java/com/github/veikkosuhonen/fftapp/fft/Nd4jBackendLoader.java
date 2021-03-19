package com.github.veikkosuhonen.fftapp.fft;

import org.nd4j.common.config.ND4JEnvironmentVars;
import org.nd4j.common.config.ND4JSystemProperties;
import org.nd4j.linalg.factory.Nd4jBackend;

public class Nd4jBackendLoader {
    public static void load() throws Nd4jBackend.NoAvailableBackendException {
        long start = System.currentTimeMillis();
        System.setProperty(ND4JSystemProperties.JAVACPP_MEMORY_MAX_PHYSICAL_BYTES, "0");
        Nd4jBackend.load();
        System.out.println("ND4J backend loaded in " + (System.currentTimeMillis() - start)/1e6 + "s");
    }
}
