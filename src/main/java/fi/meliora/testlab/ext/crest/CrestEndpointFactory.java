package fi.meliora.testlab.ext.crest;

import org.codegist.crest.CRestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Class;
import java.lang.String;

/**
 * A helper class to provide crest endpoints for calling external rest endpoints.
 *
 * @author Marko Kanala
 */
public class CrestEndpointFactory {
    private static final Logger log = LoggerFactory.getLogger(CrestEndpointFactory.class);

    private static CrestEndpointFactory instance = new CrestEndpointFactory();

    public static CrestEndpointFactory getInstance() {
        return instance;
    }

    private CrestEndpointFactory() {
    }

    /**
     * Constructs a new CRest endpoint, caches it and returns it for use.
     *
     * @param url
     * @param username
     * @param password
     * @return
     */
    public <T>T getEndpoint(String url, String username, String password, Class<T> endpointClass) {
        T endpoint;
        CRestBuilder b = new CRestBuilder().endpoint(url);
        if(username != null && password != null) {
            b = b.basicAuth(username, password);
        }
        endpoint = b.build().build(endpointClass);

        if(log.isDebugEnabled())
            log.debug("Returning endpoint to {}: {}", url, endpoint);

        return endpoint;
    }

    /**
     * Returns an endpoint to Testlab.
     *
     * If onpremiseUrl is set it used as a base url. It not, this methods peeks for
     * TESTLAB_<companyid in upper case> system environment variable for testlab api address.
     * If none is set a default of https://companyid.melioratestlab.com/api is used.
     *
     * @param companyId
     * @parma onpremiseUrl
     * @param apiKey
     * @param endpointClass
     * @param <T>
     * @return
     */
    public <T>T getTestlabEndpoint(String companyId, String onpremiseUrl, String apiKey, Class<T> endpointClass) {
        String url;
        if(onpremiseUrl != null && onpremiseUrl.trim().length() > 0) {
            // on-premise testlab
            StringBuilder sb = new StringBuilder();
            sb.append(onpremiseUrl);
            if(!onpremiseUrl.endsWith("/")) {
                sb.append('/');
            }
            sb.append("api");
            url = sb.toString();
            // force company id as "company" for calls to on-premise installations
            companyId = "company";

            if(log.isDebugEnabled())
                log.debug("Using on-premise url {} as testlab endpoint.", url);
        } else {
            // hosted testlab
            url = System.getProperty(
                    "TESTLAB_" + companyId.toUpperCase(),
                    "https://" + companyId.toLowerCase() + ".melioratestlab.com/api"
            );

            if(log.isDebugEnabled())
                log.debug("Using hosted url {} as testlab endpoint.", url);
        }

        return getEndpoint(url, companyId, apiKey, endpointClass);
    }

}
