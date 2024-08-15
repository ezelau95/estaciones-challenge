package com.example.estacioneschallenge.helper;

import com.example.estacioneschallenge.entity.Path;
import com.example.estacioneschallenge.entity.Station;

import java.util.*;


public class Dijkstra {
    private Map<Long, Double> distancias;
    private Map<Long, Long> predecesores;
    private Map<Long, Long> caminosUsados;
    private Set<Long> visitados;
    private PriorityQueue<Station> colaPrioridad;
    private Map<Long, List<Path>> grafo;
    private Map<Long, String> nombresEstaciones;

    public Dijkstra() {
        distancias = new HashMap<>();
        predecesores = new HashMap<>();
        caminosUsados = new HashMap<>();
        visitados = new HashSet<>();
        colaPrioridad = new PriorityQueue<>();
        grafo = new HashMap<>();
        nombresEstaciones = new HashMap<>();
    }

    public Dijkstra(Map<Long, List<Path>> grafo, Map<Long, String> nombresEstaciones) {
        this.grafo = grafo;
        this.nombresEstaciones = nombresEstaciones;
        distancias = new HashMap<>();
        predecesores = new HashMap<>();
        caminosUsados = new HashMap<>();
        visitados = new HashSet<>();
        colaPrioridad = new PriorityQueue<>();
    }

    public Map<Long, List<Path>> getGrafo() {
        return grafo;
    }

    public Map<Long, String> getNombresEstaciones() {
        return nombresEstaciones;
    }

    public void addStation(long id, String nombre) {
        nombresEstaciones.put(id, nombre);
        grafo.putIfAbsent(id, new ArrayList<>());
    }

    public void addPath(long id, long origenId, long destinoId, double peso) {
        grafo.get(origenId).add(new Path(id, destinoId, peso));
        grafo.get(destinoId).add(new Path(id, origenId, peso)); // Grafo no dirigido
    }

    public void encontrarCaminoMasCorto(long estacionInicialId) {
        for (long estacionId : grafo.keySet()) {
            distancias.put(estacionId, Double.MAX_VALUE);
            predecesores.put(estacionId, null);
            caminosUsados.put(estacionId, -1L);
        }
        colaPrioridad.add(new Station(estacionInicialId, nombresEstaciones.get(estacionInicialId), 0));
        distancias.put(estacionInicialId, 0D);

        while (!colaPrioridad.isEmpty()) {
            Station stationActual = colaPrioridad.poll();
            long idActual = stationActual.id;

            if (visitados.contains(idActual)) {
                continue;
            }

            visitados.add(idActual);

            for (Path path : grafo.get(idActual)) {
                long vecinoId = path.destinationId;
                if (!visitados.contains(vecinoId)) {
                    double nuevaDistancia = distancias.get(idActual) + path.cost;

                    if (nuevaDistancia < distancias.get(vecinoId)) {
                        distancias.put(vecinoId, nuevaDistancia);
                        predecesores.put(vecinoId, idActual);
                        caminosUsados.put(vecinoId, path.id);
                        colaPrioridad.add(new Station(vecinoId, nombresEstaciones.get(vecinoId), nuevaDistancia));
                    }
                }
            }
        }
    }

    public List<Long> obtenerCamino(long destinoId) {
        List<Long> camino = new ArrayList<>();
        List<Long> caminosUsadosEnCamino = new ArrayList<>();
        for (Long estacionId = destinoId; estacionId != null; estacionId = predecesores.get(estacionId)) {
            camino.add(estacionId);
            if (caminosUsados.get(estacionId) != -1L) {
                caminosUsadosEnCamino.add(caminosUsados.get(estacionId));
            }
        }
        Collections.reverse(camino);
        Collections.reverse(caminosUsadosEnCamino);
        System.out.println("Caminos usados: " + caminosUsadosEnCamino);
        return camino;
    }

    public double getCost(long destinoId) {
        return distancias.get(destinoId);
    }
}

