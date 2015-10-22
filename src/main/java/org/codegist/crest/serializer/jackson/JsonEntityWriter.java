package org.codegist.crest.serializer.jackson;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.io.Request;
import org.codegist.crest.param.Param;
import org.codegist.crest.serializer.Serializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.codegist.crest.config.ParamType.FORM;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Custom jsonentitywriter to add
 *
 * SomePOJO doSomething(SomePOJO pojo)
 *
 * .. support to Crest.
 *
 * See https://groups.google.com/forum/?fromgroups=#!topic/codegist-crest/YWHtkjkR4xs
 *
 * @author Marko Kanala
 */
public class JsonEntityWriter implements EntityWriter {
    private static final Logger log = LoggerFactory.getLogger(JsonEntityWriter.class);

    public class JsonEncodedFormJacksonSerializer implements
            Serializer<List<Param>> {

        private final ObjectMapper jackson;

        public JsonEncodedFormJacksonSerializer(CRestConfig crestConfig) {
            this.jackson = JacksonFactory.createObjectMapper(
                    crestConfig, getClass()
            );
        }

        public void serialize(List<Param> value, Charset charset, OutputStream out)
                throws Exception {
            if (!value.isEmpty()) {
                Object[] list = value.get(0).getValue().toArray(new Object[0]);
                if(log.isDebugEnabled()) {
                    for(Object o : list) {
                        log.debug("serialize value: {}", o.getClass());
                    }
                }

                jackson.writeValue(out, list[0]);
            } else {
                if(log.isDebugEnabled())
                    log.debug("Not serializing, value is empty.");
            }
        }
    }

    public static final String MIME  = "application/form-jsonencoded";
    private static final String CONTENT_TYPE = "application/json";

    private final Serializer<List<Param>> serializer;

    public JsonEntityWriter(CRestConfig crestConfig) {
        this.serializer = new JsonEncodedFormJacksonSerializer(crestConfig);
    }

    public String getContentType(Request request) {
        return CONTENT_TYPE;
    }

    public int getContentLength(Request httpRequest) {
        return -1;
    }

    public void writeTo(Request request, OutputStream outputStream) throws Exception {
        serializer.serialize(request.getParams(FORM), request.getMethodConfig().getCharset(), outputStream);
    }

}