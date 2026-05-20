package fr.ign.common.topo;

import fr.ign.cogit.simplu3d.model.UrbanFeature;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.locationtech.jts.geom.*;

import java.util.*;
import java.util.stream.Collectors;

public class CarteTopo {
    DefaultDirectedGraph<Coordinate, HalfEdge> graph = new DefaultDirectedGraph<>(HalfEdge.class);
    static GeometryFactory fact = new GeometryFactory(new PrecisionModel(100));
    List<Arc> edges = new ArrayList<>();
    List<Face> faces = new ArrayList<>();
    Map<Coordinate, Noeud> nodes = new HashMap<>();
    public CarteTopo() {
    }

    public void addFeatures(Collection<? extends UrbanFeature> features) {
        addGeometries(features.stream().map(UrbanFeature::getGeometry).toList());
    }

    private void addHalfEdge(Coordinate start, Coordinate end, Face face) {
        HalfEdge halfEdge = new HalfEdge();
        halfEdge.setFace(face);
        halfEdge.setGeometrie(fact.createLineString(new Coordinate[]{start, end}));
        if (graph.containsEdge(end, start)) {
            HalfEdge twin = graph.getEdge(end, start);
            halfEdge.setTwin(twin);
            twin.setTwin(halfEdge);
        }
        graph.addEdge(start, end, halfEdge);
    }

    private void addRing(Face face, LinearRing ring, boolean direct) {
        for (int index = 0 ; index < ring.getNumPoints() - 1 ; index++) {
            Coordinate c1 = ring.getCoordinateN(index);
            Coordinate c2 = ring.getCoordinateN(index + 1);
            if (!graph.vertexSet().contains(c1)) {
                graph.addVertex(c1);
            }
            if (!graph.vertexSet().contains(c2)) {
                graph.addVertex(c2);
            }
            if (direct) {
                addHalfEdge(c1, c2, face);
            } else {
                addHalfEdge(c2, c1, face);
            }
        }
    }

    public void addGeometries(Collection<? extends Geometry> geometries) {
        SimplePolygonizer polygonizer = new SimplePolygonizer(10);
        polygonizer.addGeometries(geometries);
        for (Polygon polygon : polygonizer.getPolygons()) {
            Face face = new Face(polygon);
            faces.add(face);
            LinearRing ring = polygon.getExteriorRing();
            addRing(face, ring, true);
            for  (int i = 0; i < polygon.getNumInteriorRing() - 1; i++) {
                addRing(face, polygon.getInteriorRingN(i), false);
            }
        }
        for (Coordinate c : graph.vertexSet()) {
            nodes.put(c, new Noeud(c));
        }
        Set<HalfEdge> visited = new HashSet<>();
        for (HalfEdge he : graph.edgeSet()) {
            if (!visited.contains(he)) {
                visited.add(he);
                visited.add(he.getTwin());
                Arc edge = new Arc(
                        nodes.get(graph.getEdgeSource(he)),
                        nodes.get(graph.getEdgeTarget(he)),
                        he.getGeometrie(),
                        he.getFace(),
                        (he.getTwin() != null) ? he.getTwin().getFace() : null);
                edges.add(edge);
                he.setEdge(edge);
                if (he.getTwin() != null) he.getTwin().setEdge(edge);
            }
        }
    }

    public Collection<Arc> getPopArcs() {
        return this.edges;
    }

    public Collection<Face> getPopFaces() {
        return this.faces;
    }

    public Collection<? extends Arc> getSortants(Noeud node) {
        return graph.outgoingEdgesOf(node.getCoord()).stream().map(HalfEdge::getEdge).collect(Collectors.toSet());
    }

    public Collection<? extends Arc> getEntrants(Noeud node) {
        return graph.incomingEdgesOf(node.getCoord()).stream().map(HalfEdge::getEdge).collect(Collectors.toSet());
    }

    public Collection<Arc> getEdges(Noeud node) {
        return graph.edgesOf(node.getCoord()).stream().map(HalfEdge::getEdge).collect(Collectors.toSet());
    }
    public Collection<Arc> getEdges(Face face) {
        return graph.edgeSet().stream().filter(halfEdge -> halfEdge.getFace() == face).map(HalfEdge::getEdge).collect(Collectors.toSet());
    }

    public Collection<Groupe> decomposeConnexes() {
        StrongConnectivityAlgorithm<Coordinate, HalfEdge> scAlg = new KosarajuStrongConnectivityInspector<>(graph);
        List<Graph<Coordinate, HalfEdge>> stronglyConnectedSubgraphs = scAlg.getStronglyConnectedComponents();
        List<Groupe> groupes = new ArrayList<>();
        for (Graph<Coordinate, HalfEdge> component : stronglyConnectedSubgraphs) {
            Groupe groupe = new Groupe();
            groupe.setListeNoeuds(component.vertexSet().stream().map(c->nodes.get(c)).toList());
            groupe.setListeArcs(component.edgeSet().stream().map(HalfEdge::getEdge).collect(Collectors.toSet()).stream().toList());
            groupe.setListeFaces(component.edgeSet().stream().map(HalfEdge::getFace).collect(Collectors.toSet()).stream().toList());
            groupes.add(groupe);
        }
        return groupes;
    }
}
