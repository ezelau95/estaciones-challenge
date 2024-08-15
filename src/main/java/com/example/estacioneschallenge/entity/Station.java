package com.example.estacioneschallenge.entity;

public class Station implements Comparable<Station> {
    public long id;
    public String name;
    public double cost;

    public Station(long id, String name, double cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    @Override
    public int compareTo(Station otra) {
        return Double.compare(this.cost, otra.cost);
    }
}