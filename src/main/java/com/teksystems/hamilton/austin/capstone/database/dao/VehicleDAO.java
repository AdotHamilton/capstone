package com.teksystems.hamilton.austin.capstone.database.dao;

import com.teksystems.hamilton.austin.capstone.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleDAO extends JpaRepository<Vehicle, Long> {

    public List<Vehicle> findAll();
    public boolean existsVehicleByMakeAndModelAndYear(@Param("make") String make, @Param("model") String model, @Param("year") Integer year);
    public List<Vehicle> findVehiclesByMakeAndModelAndYear(@Param("make") String make, @Param("model") String model, @Param("year") Integer year);


//    @Query("From Vehicle v where v.make=:make and v.model=:model and v.year=:year")
//    public List<Vehicle> findVehiclesByAllFields(String make, String model, Integer year);

}
