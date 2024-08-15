package com.example.estacioneschallenge.controller;

import com.example.estacioneschallenge.request.CreatePathRequest;
import com.example.estacioneschallenge.request.CreateStationRequest;
import com.example.estacioneschallenge.response.GetShortestPathResponse;
import com.example.estacioneschallenge.response.StatusResponse;
import com.example.estacioneschallenge.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class StationController {

    @Autowired
    StationService stationService;

    //*********** CODIFICACION DE ERRORES Y VALIDACION DE CAMPOS

    @GetMapping("/paths/{sourceId}/{destinationId}")
    public ResponseEntity<GetShortestPathResponse> getShortestPath(@PathVariable long sourceId, @PathVariable long destinationId){

        if (sourceId <= 0L || destinationId <= 0L)
            return new ResponseEntity<>(new GetShortestPathResponse("source_id and destination_id must be greater than 0"), HttpStatus.BAD_REQUEST);

        if (sourceId == destinationId)
            return new ResponseEntity<>(new GetShortestPathResponse("source_id and destination_id cannot be the same"), HttpStatus.BAD_REQUEST);


        return stationService.getShortestPath(sourceId, destinationId);
    }

    @PutMapping("/paths/{pathId}")
    public ResponseEntity<StatusResponse> createPath(@PathVariable long pathId, @Validated @RequestBody CreatePathRequest createPathRequest){

        if (pathId <= 0L)
            return new ResponseEntity<>(new StatusResponse("Error", "path_id must be greater than 0"), HttpStatus.BAD_REQUEST);

        return stationService.createPath(pathId, createPathRequest);
    }

    @PutMapping("/stations/{stationId}")
    public ResponseEntity<StatusResponse> createStation(@PathVariable long stationId, @Validated @RequestBody CreateStationRequest createStationRequest){

        if (stationId <= 0L)
            return new ResponseEntity<>(new StatusResponse("Error", "station_id must be greater than 0"), HttpStatus.BAD_REQUEST);

        return stationService.createStation(stationId, createStationRequest);
    }
}
