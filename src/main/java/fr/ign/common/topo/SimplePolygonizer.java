package fr.ign.common.topo;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.util.LinearComponentExtracter;
import org.locationtech.jts.operation.polygonize.Polygonizer;
import org.locationtech.jts.precision.GeometryPrecisionReducer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SimplePolygonizer {
    PrecisionModel pm = null;
    Polygonizer polygonizer = new Polygonizer();
    GeometryFactory fact = null;
    public SimplePolygonizer(double scale) {
        pm = new PrecisionModel(scale);
        fact = new GeometryFactory(pm);
    }

/*
    private void addGeometry(Polygonizer polygonizer, Geometry geometry) {
        if (geometry instanceof Polygon) {
            List<LineString> lines = GeomUtils.getLineStrings((Polygon) geometry);
            lines.forEach(line -> addGeometry(polygonizer, line));
        } else if (geometry instanceof MultiPolygon) {
            List<Polygon> polygons = GeomUtils.getPolygons((MultiPolygon) geometry);
            polygons.forEach(polygon -> addGeometry(polygonizer, polygon));
        } else if (geometry instanceof LineString) {
            polygonizer.add(fact.createLineString(geometry.getCoordinates()));
        } else if (geometry instanceof MultiLineString) {
            List<LineString> lines = GeomUtils.getLineStrings((MultiLineString) geometry);
            lines.forEach(line -> addGeometry(polygonizer, line));
        }
    }
    private void addGeometry(LineMerger merger, Geometry geometry) {
        if (geometry instanceof Polygon) {
            List<LineString> lines = GeomUtils.getLineStrings((Polygon) geometry);
            lines.forEach(line -> addGeometry(merger, line));
        } else if (geometry instanceof MultiPolygon) {
            List<Polygon> polygons = GeomUtils.getPolygons((MultiPolygon) geometry);
            polygons.forEach(polygon -> addGeometry(merger, polygon));
        } else if (geometry instanceof LineString) {
            merger.add(fact.createLineString(geometry.getCoordinates()));
            //merger.add(geometry);
        } else if (geometry instanceof MultiLineString) {
            List<LineString> lines = GeomUtils.getLineStrings((MultiLineString) geometry);
            lines.forEach(line -> addGeometry(merger, line));
        }
    }
*/

    private List<LineString> getLines(List<Geometry> inputFeatures) {
        List<LineString> linesList = new ArrayList<>();
        LinearComponentExtracter lineFilter = new LinearComponentExtracter(linesList);
        for (Geometry feature : inputFeatures)
            feature.apply(lineFilter);
        return linesList;
    }

    private Point extractPoint(List<LineString> lines) {
        Point point = null;
        // extract first point from first non-empty geometry
        for (Geometry geometry : lines) {
            if (!geometry.isEmpty()) {
                Coordinate p = geometry.getCoordinate();
                point = geometry.getFactory().createPoint(p);
                break;
            }
        }
        return point;
    }

    private Geometry nodeLines(List<LineString> lines) {
        MultiLineString linesGeom = fact.createMultiLineString(lines.toArray(new LineString[0]));
        Geometry unionInput = fact.createMultiLineString(null);
        Point point = extractPoint(lines);
        if (point != null)
            unionInput = point;
        return linesGeom.union(unionInput);
    }

    public void addGeometries(Collection<? extends Geometry> geometries) {
/*
        LineMerger merger = new LineMerger();
        for (Geometry geometry : geometries) {
            addGeometry(merger, geometry);
        }
        polygonizer.add(merger.getMergedLineStrings());
*/
        List<Geometry> reduced = geometries.stream().map(g ->
                GeometryPrecisionReducer.reduce(g, pm)).collect(Collectors.toList()
        );
        // extract linear components from input geometries
        List<LineString> lines = getLines(reduced);
        // node all geometries together
        Geometry nodedLines = nodeLines(lines);
        if (nodedLines instanceof MultiLineString mls) {
            // noding a second time to be sure
            List<LineString> geoms = new ArrayList<>(mls.getNumGeometries());
            for (int i = 0; i < mls.getNumGeometries(); i++)
                geoms.add((LineString) mls.getGeometryN(i));
            nodedLines = nodeLines(geoms);
        }
        //System.out.println(Calendar.getInstance().getTime() + " insert lines");
        polygonizer.add(nodedLines);
/*
        System.out.println("Polygonizer: input = " + geometries.size());
        System.out.println("Polygonizer: getPolygons = " + polygonizer.getPolygons().size());
        System.out.println("Polygonizer: getInvalidRingLines = " + polygonizer.getInvalidRingLines().size());
        System.out.println("Polygonizer: getDangles = " + polygonizer.getDangles().size());
        System.out.println("Polygonizer: getCutEdges = " + polygonizer.getCutEdges().size());
        for (Object cutedge: polygonizer.getCutEdges()) {
            System.out.println(cutedge);
        }
*/
    }
    public Collection<Polygon> getPolygons() {
        //noinspection unchecked
        return this.polygonizer.getPolygons();
    }
    public Geometry getGeometry() {
        return this.polygonizer.getGeometry();
    }
}
