package com.example.estacioneschallenge.helper;

import com.example.estacioneschallenge.entity.Path;
import com.example.estacioneschallenge.entity.Station;
import com.example.estacioneschallenge.exception.InvalidGraphOperationException;

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

    /**
     * Adds station name and ID to graph. Assures unique IDs, if station already exists, name is replaced.
     * @param id stationId
     * @param nombre stationName
     */
    public void addStation(long id, String nombre) {
        nombresEstaciones.put(id, nombre);
        grafo.putIfAbsent(id, new ArrayList<>());
    }

    /**
     * Adds path between two stations. Assures unique IDs, if pathId already exists, path is updated.
     * @param id pathId
     * @param origenId source_id station
     * @param destinoId destonaion_id station
     * @param peso path cost
     * @throws InvalidGraphOperationException
     */
    public void addPath(long id, long origenId, long destinoId, double peso) throws InvalidGraphOperationException {
        if (grafo.get(origenId) == null)
            throw new InvalidGraphOperationException("Source station does not exist.");

        if (grafo.get(destinoId) == null)
            throw new InvalidGraphOperationException("Destination station does not exist.");

        addOrUpdatePath(grafo.get(origenId), new Path(id, destinoId, peso));
        addOrUpdatePath(grafo.get(destinoId), new Path(id, origenId, peso)); // Grafo no dirigido
    }

    private void addOrUpdatePath(List<Path> pathList, Path newPath) {
        // Encuentra el índice del Path existente en la lista (si existe)
        int index = pathList.indexOf(newPath);

        if (index != -1) {
            // Si el Path ya existe en la lista, actualiza el objeto existente
            pathList.set(index, newPath);
        } else {
            // Si el Path no existe en la lista, agrégalo
            pathList.add(newPath);
        }
    }

    /**
     * Calculates the shortest path from given station to all remaining station loaded applying Dijkstra algorithm
     * @param estacionInicialId source_id station
     * @throws InvalidGraphOperationException
     */
    public void encontrarCaminoMasCorto(long estacionInicialId) throws InvalidGraphOperationException {
        if (grafo.get(estacionInicialId) == null)
            throw new InvalidGraphOperationException("Source station does not exist.");

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

    /**
     * Gets the shortest path calculated for given  destination station
     * @param destinoId destination_id station
     * @return List of Long: path through stations
     * @throws InvalidGraphOperationException
     */
    public List<Long> obtenerCamino(long destinoId) throws InvalidGraphOperationException {
        if (grafo.get(destinoId) == null)
            throw new InvalidGraphOperationException("Destination station does not exist.");

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

    /**
     * Gets cost of given destination_id station
     * @param destinoId destination_id station
     * @return
     * @throws InvalidGraphOperationException
     */
    public double getCost(long destinoId) throws InvalidGraphOperationException {
        if (grafo.get(destinoId) == null)
            throw new InvalidGraphOperationException("Destination station does not exist.");

        return distancias.get(destinoId);
    }
}

