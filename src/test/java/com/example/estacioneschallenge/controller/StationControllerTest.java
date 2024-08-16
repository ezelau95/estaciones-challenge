package com.example.estacioneschallenge.controller;

import com.example.estacioneschallenge.app.EstacionesChallengeApplication;
import com.example.estacioneschallenge.request.CreatePathRequest;
import com.example.estacioneschallenge.request.CreateStationRequest;
import com.example.estacioneschallenge.service.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EstacionesChallengeApplication.class)
@AutoConfigureMockMvc
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StationService stationService;

    @BeforeEach
    void setUp(){
        CreateStationRequest station1 = new CreateStationRequest();
        station1.setName("Moreno");
        CreateStationRequest station2 = new CreateStationRequest();
        station2.setName("Paso del Rey");
        stationService.createOrUpdateStation(1L, station1);
        stationService.createOrUpdateStation(2L, station2);

        CreatePathRequest path = new CreatePathRequest();
        path.setCost(50D);
        path.setSourceId(1L);
        path.setDestinationId(2L);
        stationService.createOrUpdatePath(10L, path);
    }

    @Test
    void testGetShortestPath_Success() throws Exception {

        mockMvc.perform(get("/api/paths/{sourceId}/{destinationId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"path\":[1,2],\"cost\":50.0}"));
    }

    @Test
    void testGetShortestPath_SamePathVariable() throws Exception {

        mockMvc.perform(get("/api/paths/{sourceId}/{destinationId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetShortestPath_InvalidFirstPathVariable() throws Exception {

        mockMvc.perform(get("/api/paths/{sourceId}/{destinationId}", -1, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetShortestPath_InvalidSecondPathVariable() throws Exception {

        mockMvc.perform(get("/api/paths/{sourceId}/{destinationId}", 1, -2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreatePath_Success() throws Exception {
        // Definir el JSON de entrada
        String pathJson = "{\"cost\": 20, \"source_id\": 1,\"destination_id\": 2}";

        // Realizar la solicitud PUT
        mockMvc.perform(put("/api/paths/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pathJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":\"OK\"}"));
    }

    @Test
    void testCreatePath_DatatypeMismatch() throws Exception {
        // Definir el JSON de entrada
        String pathJson = "{\"cost\": \"String\", \"source_id\": 1,\"destination_id\": 2}";

        // Realizar la solicitud PUT
        mockMvc.perform(put("/api/paths/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pathJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreatePath_InvalidField() throws Exception {
        // Definir el JSON de entrada
        String pathJson = "{\"cost\": -1, \"source_id\": 1,\"destination_id\": 2}";

        // Realizar la solicitud PUT
        mockMvc.perform(put("/api/paths/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pathJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreatePath_MissingFields() throws Exception {
        // Definir el JSON de entrada
        String pathJson = "{\"cost\": 50, \"source_id\": 1}";

        // Realizar la solicitud PUT
        mockMvc.perform(put("/api/paths/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pathJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreatePath_WrongPathVariable() throws Exception {
        // Definir el JSON de entrada
        String pathJson = "{\"cost\": 20, \"source_id\": 1,\"destination_id\": 2}";

        // Realizar la solicitud PUT
        mockMvc.perform(put("/api/paths/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pathJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateStation_Success() throws Exception {
        // Definir el JSON de entrada
        String stationJson = "{ \"name\": \"Moreno\" }";

        // Realizar la solicitud PUT
        mockMvc.perform(put("/api/stations/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stationJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":\"OK\"}"));
    }

    @Test
    void testCreateStation_BlankName() throws Exception {
        // Definir el JSON de entrada
        String stationJson = "{ \"name\": \"\" }";

        // Realizar la solicitud PUT
        mockMvc.perform(put("/api/stations/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stationJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateStation_NullName() throws Exception {
        // Definir el JSON de entrada
        String stationJson = "{ \"name\": null }";

        // Realizar la solicitud PUT
        mockMvc.perform(put("/api/stations/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stationJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateStation_BlankRequest() throws Exception {
        // Definir el JSON de entrada
        String stationJson = "{}";

        // Realizar la solicitud PUT
        mockMvc.perform(put("/api/stations/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stationJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateStation_WrongPathVariable() throws Exception {
        // Definir el JSON de entrada
        String stationJson = "{ \"name\": \"Moreno\" }";

        // Realizar la solicitud PUT
        mockMvc.perform(put("/api/stations/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stationJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}