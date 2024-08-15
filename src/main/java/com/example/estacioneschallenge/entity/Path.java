package com.example.estacioneschallenge.entity;

public class Path {
    public long id;
    public long destinationId;
    public double cost;

    public Path(long id, long destinationId, double cost) {
        this.id = id;
        this.destinationId = destinationId;
        this.cost = cost;
    }
}
