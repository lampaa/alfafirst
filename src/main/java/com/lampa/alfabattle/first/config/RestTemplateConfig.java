package com.lampa.alfabattle.first.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Collections;

@Configuration
public class RestTemplateConfig {
    @Value("${alfa.keystore.sert}")
    private Resource sert;

    @Value("${alfa.keystore.password}")
    private String password;

    @Value("${alfa.clientid}")
    private String clientid;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadKeyMaterial(sert.getFile(), password.toCharArray(), password.toCharArray())
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        HttpClient client = HttpClients.custom()
                .setSSLContext(sslContext)
                .build();

        RestTemplate restTemplate = builder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client))
                .build();

        restTemplate.setInterceptors(Collections.singletonList(new AlfaInterceptor()));
        return restTemplate;
    }

    private class AlfaInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            headers.add("accept", MediaType.APPLICATION_JSON.toString());
            headers.add("X-IBM-Client-Id", clientid);
            return execution.execute(request, body);
        }
    }
}
