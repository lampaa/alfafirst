package com.lampa.alfabattle.first.services;

import com.lampa.alfabattle.first.dto.ATMDetails;
import com.lampa.alfabattle.first.dto.ATMStatus;
import com.lampa.alfabattle.first.entities.Atm;
import com.lampa.alfabattle.first.repos.AtmRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.concurrent.FutureCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AtmService {
    private RestRequestService restRequestService;
    private AtmRepo atmRepo;
    private StompClientService stompClientService;

    @Autowired
    public AtmService(RestRequestService restRequestService, AtmRepo atmRepo, StompClientService stompClientService) {
        this.restRequestService = restRequestService;
        this.atmRepo = atmRepo;
        this.stompClientService = stompClientService;
    }

    @PostConstruct
    private void init() {
        log.info("Getting a list of ATMs");

        try {
            List<ATMDetails> atmDetails = restRequestService.getAtms();
            List<ATMStatus> atmsStatus = restRequestService.getAtmsStatus();

            log.info("Founded {} ATMs", atmDetails.size());

            for(ATMDetails details : atmDetails) {
                Optional<Atm> optionalAtm = atmRepo.findByExternal(details.getDeviceId());

                if(optionalAtm.isPresent()) {
                    atmRepo.delete(optionalAtm.get());
                }

                Optional<ATMStatus> findAtmStatus = atmsStatus.stream().filter(status -> status.getDeviceId().equals(details.getDeviceId())).findFirst();
                Atm atm = atmRepo.save(Atm.valueOf(details, findAtmStatus));

                stompClientService.request(details, new FutureCallback<>() {
                    @Override
                    public void completed(Integer integer) {
                        atm.setAlfik(integer);
                        atmRepo.save(atm);
                    }

                    @Override
                    public void failed(Exception e) {

                    }

                    @Override
                    public void cancelled() {

                    }
                });
            }

            log.info("Getting ATMs Done!");
        }
        catch (HttpClientErrorException e) {
            log.error("Error get list of ATMs", e);
        }
    }
}
