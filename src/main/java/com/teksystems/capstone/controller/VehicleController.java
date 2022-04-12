package com.teksystems.capstone.controller;

import com.teksystems.capstone.database.dao.VehicleDAO;
import com.teksystems.capstone.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    VehicleDAO vehicleDAO;
    @GetMapping("")
    public List<Vehicle> getAllVehicles(){
        return vehicleDAO.findAll();
    };
    @GetMapping("/findByYearMakeModel")
    public List<Vehicle> findVehiclesByYearMakeModel(@RequestParam String make, @RequestParam String model, @RequestParam Integer year) {
        return vehicleDAO.findVehiclesByMakeAndModelAndYear(make, model, year);
    }
}
