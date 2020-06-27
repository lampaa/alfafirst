package com.lampa.alfabattle.first.repos;

import com.lampa.alfabattle.first.entities.Atm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AtmRepo extends JpaRepository<Atm, Long> {

    Optional<Atm> findByExternal(Integer deviceId);

    @Query(value = "SELECT * from atm \n" +
            "ORDER BY ST_Distance(st_setsrid(st_makepointm(latitude, longitude, id), 4326), " +
            "st_setsrid(st_makepointm(:lat, :lon, id), 4326)) ASC\n" +
            "LIMIT 1", nativeQuery = true)
    Optional<Atm> findByNearest(@Param("lat") Double lat, @Param("lon") Double lon);

    @Query(value = "SELECT * from atm \n" +
            "WHERE payments = :payments ORDER BY ST_Distance(st_setsrid(st_makepointm(latitude, longitude, id), 4326), " +
            "st_setsrid(st_makepointm(:lat, :lon, id), 4326)) ASC\n" +
            "LIMIT 1", nativeQuery = true)
    Optional<Atm> findByNearestAndPayments(@Param("lat") Double lat, @Param("lon") Double lon, @Param("payments") Boolean payments);

    @Query(value = "SELECT * from atm \n" +
            "ORDER BY ST_Distance(st_setsrid(st_makepointm(latitude, longitude, id), 4326), " +
            "st_setsrid(st_makepointm(:lat, :lon, id), 4326)) ASC", nativeQuery = true)
    List<Atm> findAllByNearestAlfik(@Param("lat") Double latitude, @Param("lon") Double longitude);
}

