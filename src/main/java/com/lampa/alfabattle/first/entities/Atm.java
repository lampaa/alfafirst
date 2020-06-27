package com.lampa.alfabattle.first.entities;

import com.lampa.alfabattle.first.dto.ATMDetails;
import com.lampa.alfabattle.first.dto.ATMStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

@Slf4j
@Data
@Table
@Entity
public class Atm extends AbstractId {
    @Column
    private String city;

    @Column
    private String address;

    @Column
    private Integer external;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private Boolean nfc;

    @Column
    private Boolean available = false;

    @Column
    private Boolean payments = false;

    @Column
    private Integer alfik = 0;

    public static Atm valueOf(ATMDetails details, Optional<ATMStatus> findAtmStatus) {
        Atm atm = new Atm();
        atm.setAddress(details.getAddress().getLocation());
        atm.setNfc(details.getNfc().equals("Y"));


        if (details.getCoordinates() != null && details.getCoordinates().getLatitude() != null && details.getCoordinates().getLongitude() != null) {
            try {
                atm.setLatitude(Double.valueOf(details.getCoordinates().getLatitude()));
                atm.setLongitude(Double.valueOf(details.getCoordinates().getLongitude()));
            } catch (NumberFormatException e) {
                log.warn("error parse {}, {}", details.getCoordinates().getLatitude(), details.getCoordinates().getLongitude());
            }
        }

        atm.setCity(details.getAddress().getCity());
        atm.setExternal(details.getDeviceId());

        //atm.setPaymentSystems(details.getAvailablePaymentSystems().stream().map(PaymentSystem::findByCode).collect(Collectors.toSet()));
        //atm.setCurrencies(details.getCashInCurrencies().stream().map(Currency::findByCode).collect(Collectors.toSet()));

        if (findAtmStatus.isPresent() && findAtmStatus.get().getAvailableNow() != null) {
            atm.setAvailable(findAtmStatus.get().getAvailableNow().getOnline().equals("Y"));
            atm.setPayments(findAtmStatus.get().getAvailableNow().getPayments().equals("Y"));
        }
        return atm;
    }
}
