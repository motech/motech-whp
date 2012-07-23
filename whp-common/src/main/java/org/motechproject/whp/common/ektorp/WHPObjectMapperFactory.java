package org.motechproject.whp.common.ektorp;

import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.impl.StdObjectMapperFactory;

import static org.codehaus.jackson.map.DeserializationConfig.Feature.USE_GETTERS_AS_SETTERS;

public class WHPObjectMapperFactory extends StdObjectMapperFactory {

    @Override
    public synchronized ObjectMapper createObjectMapper() {
        ObjectMapper mapper = super.createObjectMapper();
        mapper.getDeserializationConfig().disable(USE_GETTERS_AS_SETTERS);
        return mapper;
    }

    @Override
    public ObjectMapper createObjectMapper(CouchDbConnector connector) {
        ObjectMapper mapper = super.createObjectMapper(connector);
        mapper.getDeserializationConfig().disable(USE_GETTERS_AS_SETTERS);
        return mapper;
    }
}
