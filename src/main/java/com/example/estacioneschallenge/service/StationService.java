package com.example.estacioneschallenge.service;

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

    public ResponseEntity<StatusResponse> createStation(long id, CreateStationRequest createStationRequest) {

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

    public ResponseEntity<StatusResponse> createPath(long pathId, CreatePathRequest createPathRequest) {
        ResponseEntity<StatusResponse> response;
        try {
            logger.info("Recibido id: " + pathId + " CreateStationRequest: " + createPathRequest);

            graph.addPath(pathId, createPathRequest.getSourceId(), createPathRequest.getDestinationId(), createPathRequest.getCost());

            response = new ResponseEntity<>(new StatusResponse("OK"), HttpStatus.OK);
        } catch (Exception ex){
            logger.error("Error createPath: " + ex.getMessage(), ex);
            response = new ResponseEntity<>(new StatusResponse("Error", "Unexpected Error processing request"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    public ResponseEntity<GetShortestPathResponse> getShortestPath(long sourceId, long destinationId) {

        ResponseEntity<GetShortestPathResponse> response;
        try {
            Dijkstra auxGraph = new Dijkstra(graph.getGrafo(), graph.getNombresEstaciones());
            auxGraph.encontrarCaminoMasCorto(sourceId);
            List<Long> shortestPath = auxGraph.obtenerCamino(destinationId);
            double cost = auxGraph.getCost(destinationId);
            response = new ResponseEntity<>(new GetShortestPathResponse(shortestPath, cost), HttpStatus.OK);

            //*****************CONTEMPLAR QUE NO HAYA CAMINO

            logger.debug("El camino más corto de " + sourceId + " a " + destinationId + " es " + shortestPath + " COST: " + cost);

        } catch (Exception ex){
            logger.error("Error getShortestPath: " + ex.getMessage(), ex);
            response = new ResponseEntity<>(new GetShortestPathResponse("Unexpected Error processing request"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }
}
