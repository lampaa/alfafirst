package com.lampa.alfabattle.first.services;

import com.lampa.alfabattle.first.dto.ATMDetails;
import com.lampa.alfabattle.first.dto.ATMStatus;
import com.lampa.alfabattle.first.dto.JSONResponseBankATMDetails;
import com.lampa.alfabattle.first.dto.JSONResponseBankATMStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class RestRequestService {
    private RestTemplate restTemplate;

    @Autowired
    public RestRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    private void init() {
        this.getAtms();
    }

    /**
     * Получение списка банкоматов
     *
     * @return
     */
    public List<ATMDetails> getAtms() throws HttpClientErrorException {
        ResponseEntity<JSONResponseBankATMDetails> response = restTemplate.exchange(
                "https://apiws.alfabank.ru/alfabank/alfadevportal/atm-service/atms", HttpMethod.GET, null, JSONResponseBankATMDetails.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().getData().getAtms();
        }

        throw new HttpClientErrorException(response.getStatusCode());
    }

    /**
     * Получение списка банкоматов
     *
     * @return
     */
    public List<ATMStatus> getAtmsStatus() throws HttpClientErrorException {
        ResponseEntity<JSONResponseBankATMStatus> response = restTemplate.exchange(
                "https://apiws.alfabank.ru/alfabank/alfadevportal/atm-service/atms/status", HttpMethod.GET, null, JSONResponseBankATMStatus.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().getData().getAtms();
        }

        throw new HttpClientErrorException(response.getStatusCode());
    }
}
