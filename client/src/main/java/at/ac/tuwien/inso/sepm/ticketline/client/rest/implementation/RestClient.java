package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties.RestClientConfigurationProperties;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RestClient extends RestTemplate {

    private final String baseUrl;

    public RestClient(
        List<ClientHttpRequestInterceptor> interceptors,
        RestClientConfigurationProperties restClientConfigurationProperties
    ) {
        super(new HttpComponentsClientHttpRequestFactory(HttpClients
            .custom()
            .setConnectionManager(new PoolingHttpClientConnectionManager())
            .build()));
        setInterceptors(interceptors);
        String computedBaseUrl = restClientConfigurationProperties.getRemote().getFullUrl();
        if (computedBaseUrl.endsWith("/")) {
            computedBaseUrl = computedBaseUrl.substring(0, computedBaseUrl.length() - 1);
        }
        baseUrl = computedBaseUrl;
    }

    /**
     * Get the full URI of the endpoint.
     *
     * @param serviceLocation the endpoints location
     * @return full URI of the endpoint
     */
    public URI getServiceURI(String serviceLocation) {
        if (!serviceLocation.startsWith("/")) {
            return URI.create(baseUrl + "/" + serviceLocation);
        }
        return URI.create(baseUrl + serviceLocation);
    }

    public URI getServiceURI(String serviceLocation, MultiValueMap<String, String> params) {
        return getServiceURI(UriComponentsBuilder.fromPath(serviceLocation).queryParams(params).build().encode().toString());
    }

    public URI getServiceURI(String serviceLocation, Pageable pageable) {
        return getServiceURI(serviceLocation,pageableToMap(pageable));
    }

    public URI getServiceURI(String serviceLocation, MultiValueMap<String, String> params, Pageable pageable) {
        params.addAll(pageableToMap(pageable));
        return getServiceURI(serviceLocation,params);
    }

    private MultiValueMap<String, String> pageableToMap(Pageable pageable){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        //?size=3&page=2&sort=userName,DESC
        map.add("size", ""+pageable.getPageSize());
        map.add("page", ""+pageable.getPageNumber());
        if(pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> map.add("sort", order.getProperty() + "," + order.getDirection()));
        }
        return map;
    }

}