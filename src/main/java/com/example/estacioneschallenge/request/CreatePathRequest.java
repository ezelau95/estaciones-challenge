package com.example.estacioneschallenge.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreatePathRequest {

    @NotNull
    @Positive
    private Double cost;

    @NotNull
    @Positive
    @JsonProperty("source_id")
    private Long sourceId;

    @NotNull
    @Positive
    @JsonProperty("destination_id")
    private Long destinationId;

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    @Override
    public String toString() {
        return "CreatePathRequest{" +
                "cost=" + cost +
                ", sourceId=" + sourceId +
                ", destinationId=" + destinationId +
                '}';
    }
}
