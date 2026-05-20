package fr.ign.common.dtm;

import java.io.*;
import java.util.logging.Logger;

public class DTMArea extends DTM {
    private final static Logger logger = Logger.getLogger(DTMArea.class.getName());
    public DTMArea(String file, String layerName, int exager) throws FileNotFoundException {
        this(new FileInputStream(file), layerName, exager);
    }
    public DTMArea(InputStream is, String layerName, int exager) {
        super("", layerName, exager);
        representationProcess(is, layerName, exager);
    }
}
