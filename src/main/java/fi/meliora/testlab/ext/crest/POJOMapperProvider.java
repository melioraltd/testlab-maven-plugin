package fi.meliora.testlab.ext.crest;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * ObjectMapper provider that uses jackson POJO mapping instead of jaxb. This way
 * for example maps and lists get mapped automatically.
 *
 * @author Marko Kanala
 */
@Provider
public class POJOMapperProvider implements ContextResolver<ObjectMapper> {
    private static final Logger log = LoggerFactory.getLogger(POJOMapperProvider.class);

    final ObjectMapper mapper;

    public POJOMapperProvider() {
        mapper = new ObjectMapper();
        AnnotationIntrospector jacksonIntrospector = new JacksonAnnotationIntrospector();
        mapper.setConfig(mapper.getDeserializationConfig().with(jacksonIntrospector));
        mapper.setConfig(mapper.getSerializationConfig().with(jacksonIntrospector));
        if(log.isDebugEnabled())
            log.debug("Constructed ObjectMapper with POJOMapperProvider.");
    }

    /**
     * Get a context of type <code>T</code> that is applicable to the supplied
     * type.
     *
     * @param type the class of object for which a context is desired
     * @return a context for the supplied type or <code>null</code> if a
     *         context for the supplied type is not available from this provider.
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
        if(log.isDebugEnabled())
            log.debug("getContext: {}", type);
        return mapper;
    }

}
