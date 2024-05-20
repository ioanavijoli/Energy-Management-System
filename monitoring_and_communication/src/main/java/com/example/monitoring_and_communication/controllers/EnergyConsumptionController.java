package com.example.monitoring_and_communication.controllers;

import com.example.monitoring_and_communication.dtos.EnergyConsumptionDTO;
import com.example.monitoring_and_communication.services.EnergyConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/history")
@CrossOrigin("*")
public class EnergyConsumptionController {

    private final EnergyConsumptionService energyConsumptionService;

    @Autowired
    public EnergyConsumptionController(EnergyConsumptionService energyConsumptionService) {
        this.energyConsumptionService = energyConsumptionService;

    }

    @GetMapping("/energyconsumption")
    public ResponseEntity<List<EnergyConsumptionDTO>> getEnergyConsumptionByDateAndByDevice(
            @RequestParam UUID deviceId,
            @RequestParam LocalDateTime dateTime,
            @RequestParam LocalDateTime endTime
            ) {

        List<EnergyConsumptionDTO> energyConsumptionList = energyConsumptionService.getEnergyConsumptionByDateAndByDevice(deviceId, dateTime, endTime);
        return new ResponseEntity<>(energyConsumptionList, HttpStatus.OK);


    }
}