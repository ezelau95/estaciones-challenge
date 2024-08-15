package com.example.estacioneschallenge.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetShortestPathResponse {
    private List<Long> path;
    private Double cost;
    private String message;

    public GetShortestPathResponse(List<Long> path, Double cost) {
        this.path = path;
        this.cost = cost;
    }

    public GetShortestPathResponse(String message) {
        this.message = message;
    }

    public List<Long> getPath() {
        return path;
    }

    public void setPath(List<Long> path) {
        this.path = path;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
