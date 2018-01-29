package de.felix_klauke.doctrin.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implementation of the {@link DoctrinServerApplication}. It implements how the server behaves on
 * lifecycle instructions like starting up or stopping. The application should log all information regarding this
 * lifecycle using the {@link #logger}. The application runs on the main thread on should try to be careful using
 * IO or other intensive operations and should delegate as many tasks as possible to prevent failures and lags
 * in the application itself as we aim to interact on a real time level.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerApplicationImpl implements DoctrinServerApplication {

    /**
     * The logger that will announce all general actions performed by the central application. That includes
     * lifecycle information like starting up or stopping as well as important instructions coming from outside.
     */
    private final Logger logger = LoggerFactory.getLogger(DoctrinServerApplicationImpl.class);

    @Override
    public void initialize() {
        logger.info("Initializing doctrin server application.");

        logger.info("Initialized doctrin server application.");
    }
}
