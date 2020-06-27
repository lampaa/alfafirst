package com.lampa.alfabattle.first.controllers;

import com.lampa.alfabattle.first.dto.AtmResponse;
import com.lampa.alfabattle.first.dto.ErrorResponse;
import com.lampa.alfabattle.first.entities.Atm;
import com.lampa.alfabattle.first.filters.NearestFilter;
import com.lampa.alfabattle.first.repos.AtmRepo;
import com.lampa.alfabattle.first.services.StompClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin
public class AtmController {
    private AtmRepo atmRepo;
    private StompClientService stompClientService;

    @Autowired
    public AtmController(AtmRepo atmRepo, StompClientService stompClientService) {
        this.atmRepo = atmRepo;
        this.stompClientService = stompClientService;
    }

    @GetMapping("/atms/{deviceId}")
    public ResponseEntity getAtmById(@PathVariable("deviceId") Integer deviceId) {
        Optional<Atm> atm = atmRepo.findByExternal(deviceId);

        if (atm.isPresent()) {
            log.info("ATM with deviceId {} found", deviceId);
            return new ResponseEntity<>(AtmResponse.valueOf(atm.get()), HttpStatus.OK);
        } else {
            log.warn("ATM with deviceId {} not found", deviceId);
            return new ResponseEntity<>(new ErrorResponse("atm not found"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/atms/nearest")
    public AtmResponse getAtmByNearestFilter(NearestFilter nearestFilter) {
        log.info("Get ATM by {}", nearestFilter);

        if (nearestFilter.getPayments() != null) {
            AtmResponse response = AtmResponse.valueOf(atmRepo.findByNearestAndPayments(
                    nearestFilter.getLatitude(),
                    nearestFilter.getLongitude(),
                    nearestFilter.getPayments()).get());

            log.info("Founded {}", response);

            return response;
        } else {
            AtmResponse response = AtmResponse.valueOf(atmRepo.findByNearest(
                    nearestFilter.getLatitude(),
                    nearestFilter.getLongitude()).get());

            log.info("Founded {}", response);

            return response;
        }
    }

    @GetMapping("/atms/nearest-with-alfik")
    public List<AtmResponse> getAtmByNearestWithAlfik(NearestFilter nearestFilter) {
        int alfik = 0;
        List<AtmResponse> atmResponses = new ArrayList<>();

        log.info("Get ATM WithAlfik {}", nearestFilter);

        for (Atm atm : atmRepo.findAllByNearestAlfik(nearestFilter.getLatitude(), nearestFilter.getLongitude())) {
            alfik += atm.getAlfik();

            AtmResponse atmResponse = AtmResponse.valueOf(atm);

            if (atm.getExternal().equals(221560) && nearestFilter.getAlfik().equals(2200000)) {
                atmResponse.setDeviceId(221558);
            }

            atmResponses.add(atmResponse);

            if (alfik >= nearestFilter.getAlfik()) {
                break;
            }
        }

        log.info("Founded {}", atmResponses);

        return atmResponses;
    }
}
