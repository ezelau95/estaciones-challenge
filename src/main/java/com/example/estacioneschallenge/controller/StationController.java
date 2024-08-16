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

    /**
     * Calculates the shortest path given a station sourceId and a station destinationId.
     * @param sourceId station source id
     * @param destinationId station destination id
     * @return Shortest path and associated Cost
     */
    @GetMapping("/paths/{sourceId}/{destinationId}")
    public ResponseEntity<GetShortestPathResponse> getShortestPath(@PathVariable long sourceId, @PathVariable long destinationId){

        if (sourceId <= 0L || destinationId <= 0L)
            return new ResponseEntity<>(new GetShortestPathResponse("source_id and destination_id must be greater than 0"), HttpStatus.BAD_REQUEST);

        if (sourceId == destinationId)
            return new ResponseEntity<>(new GetShortestPathResponse("source_id and destination_id cannot be the same"), HttpStatus.BAD_REQUEST);


        return stationService.getShortestPath(sourceId, destinationId);
    }

    /**
     * Creates or updates path for given ID
     * @param pathId path_id
     * @param createPathRequest requested path information
     * @return Status confirmation
     */
    @PutMapping("/paths/{pathId}")
    public ResponseEntity<StatusResponse> createOrUpdatePath(@PathVariable long pathId, @Validated @RequestBody CreatePathRequest createPathRequest){

        if (pathId <= 0L)
            return new ResponseEntity<>(new StatusResponse("Error", "path_id must be greater than 0"), HttpStatus.BAD_REQUEST);

        return stationService.createOrUpdatePath(pathId, createPathRequest);
    }

    /**
     * Creates or updates station for given ID
     * @param stationId station_id
     * @param createStationRequest requested station information
     * @return Status confirmation
     */
    @PutMapping("/stations/{stationId}")
    public ResponseEntity<StatusResponse> createOrUpdateStation(@PathVariable long stationId, @Validated @RequestBody CreateStationRequest createStationRequest){

        if (stationId <= 0L)
            return new ResponseEntity<>(new StatusResponse("Error", "station_id must be greater than 0"), HttpStatus.BAD_REQUEST);

        return stationService.createOrUpdateStation(stationId, createStationRequest);
    }
}
