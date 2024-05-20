package com.example.monitoring_and_communication.controllers;

import com.example.monitoring_and_communication.dtos.MeasurementDTO;
import com.example.monitoring_and_communication.services.MeasurementService;
import com.example.monitoring_and_communication.services.utils.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/device")
public class MeasurementController {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping()
    public ResponseEntity<List<MeasurementDTO>> getMeasurements() {

        List<MeasurementDTO> dtos = measurementService.findMeasurements();
        return new ResponseEntity<>(dtos, HttpStatus.OK);

    }


    @PostMapping()
    public ResponseEntity<UUID> insertMeasurement(@Valid @RequestBody MeasurementDTO measurementDTO) {
        UUID deviceID = measurementService.insert(measurementDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MeasurementDTO> getMeasurement(@PathVariable("id") UUID deviceId) {
        MeasurementDTO dto = measurementService.findMeasurementById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);


    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<MeasurementDTO> updateMeasurement(
            @PathVariable("id") UUID deviceId,
            @Valid @RequestBody MeasurementDTO measurementDTO) {

        MeasurementDTO updatedMeasurement = measurementService.update(deviceId, measurementDTO);
        return new ResponseEntity<>(updatedMeasurement, HttpStatus.OK);


    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteMeasurement(@PathVariable("id") UUID deviceId) {
        measurementService.delete(deviceId);
        String responseMessage = "Measurement with ID " + deviceId + " has been successfully deleted.";
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }
}
