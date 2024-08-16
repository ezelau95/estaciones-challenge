package com.example.estacioneschallenge.service;

import com.example.estacioneschallenge.exception.InvalidGraphOperationException;
import com.example.estacioneschallenge.helper.Dijkstra;
import com.example.estacioneschallenge.request.CreatePathRequest;
import com.example.estacioneschallenge.request.CreateStationRequest;
import com.example.estacioneschallenge.response.GetShortestPathResponse;
import com.example.estacioneschallenge.response.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    Logger logger = LoggerFactory.getLogger(StationService.class);

    Dijkstra graph = new Dijkstra();

    /**
     * Creates a new station or updates it if station already exists
     * @param id station_id
     * @param createStationRequest requested station information
     * @return result Status information
     */
    public ResponseEntity<StatusResponse> createOrUpdateStation(long id, CreateStationRequest createStationRequest) {

        ResponseEntity<StatusResponse> response;
        try {
            logger.debug("Recibido id: " + id + " CreateStationRequest.getName: " + createStationRequest.getName());

            graph.addStation(id, createStationRequest.getName());

            response = new ResponseEntity<>(new StatusResponse("OK"), HttpStatus.OK);
        } catch (Exception ex){
            logger.error("Error createStation: " + ex.getMessage(), ex);
            response = new ResponseEntity<>(new StatusResponse("Error", "Unexpected Error processing request"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    /**
     * Creates a new path or updates it if path already exists
     * @param pathId path_id
     * @param createPathRequest requested path information
     * @return result Status information
     */
    public ResponseEntity<StatusResponse> createOrUpdatePath(long pathId, CreatePathRequest createPathRequest) {

        ResponseEntity<StatusResponse> response;
        try {
            logger.info("Recibido id: " + pathId + " CreateStationRequest: " + createPathRequest);

            graph.addPath(pathId, createPathRequest.getSourceId(), createPathRequest.getDestinationId(), createPathRequest.getCost());

            response = new ResponseEntity<>(new StatusResponse("OK"), HttpStatus.OK);
        } catch (InvalidGraphOperationException ex){
            logger.error("Error createPath: " + ex.getMessage(), ex);
            response = new ResponseEntity<>(new StatusResponse("Error", ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            logger.error("Error createPath: " + ex.getMessage(), ex);
            response = new ResponseEntity<>(new StatusResponse("Error", "Unexpected Error processing request"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    /**
     * Calculates the shortest path between given station IDs
     * @param sourceId source_id station
     * @param destinationId destination_id station
     * @return result Status and shortest path information
     */
    public ResponseEntity<GetShortestPathResponse> getShortestPath(long sourceId, long destinationId) {

        ResponseEntity<GetShortestPathResponse> response;
        try {
            Dijkstra auxGraph = new Dijkstra(graph.getGrafo(), graph.getNombresEstaciones());
            auxGraph.encontrarCaminoMasCorto(sourceId);
            List<Long> shortestPath = auxGraph.obtenerCamino(destinationId);
            double cost = auxGraph.getCost(destinationId);
            response = new ResponseEntity<>(new GetShortestPathResponse(shortestPath, cost), HttpStatus.OK);


            logger.debug("El camino más corto de " + sourceId + " a " + destinationId + " es " + shortestPath + " COST: " + cost);

        } catch (InvalidGraphOperationException ex){
            logger.error("Error getShortestPath: " + ex.getMessage(), ex);
            response = new ResponseEntity<>(new GetShortestPathResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            logger.error("Error getShortestPath: " + ex.getMessage(), ex);
            response = new ResponseEntity<>(new GetShortestPathResponse("Unexpected Error processing request"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }
}
