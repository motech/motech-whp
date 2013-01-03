package org.motechproject.whp.wgn.outbound.logging;

import org.apache.log4j.Logger;
import org.motechproject.whp.wgn.outbound.WGNRequest;

public class WGNRequestLogger {

    private Logger logger;

    public WGNRequestLogger(Logger logger) {
        this.logger = logger;
    }

    public void log(String url, WGNRequest serializable) {
        StringBuilder writer = new StringBuilder();
        writer.append(url + " ");
        writer.append(serializable.toXML());
        logger.info(writer.toString());
    }
}
