package fr.ign.common.util;

import fr.ign.common.analysis.roof.RoofDetection;
import fr.ign.common.geometry.Box3D;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;
//FIXME ADD TEST
public class OrientedBoundingBox {
    private Polygon poly = null;
    private double minZ = Double.NaN;
    private double maxZ = Double.NaN;
    private Coordinate centre = null;
    private double width = -1;
    private double length = -1;
    private Vecteur shortestDirection = null;
    private Vecteur longestDirection = null;
    private List<LineString> shortestEdges = null, longestEdges = null;
    public OrientedBoundingBox(Geometry geometry) {
        if (geometry != null && geometry.getNumPoints() != 0) {
            this.computeOrientedBoundingBox(geometry);
        }
    }

    private void computeOrientedBoundingBox(Geometry geometry) {
        List<Polygon> lOS =  RoofDetection.detectRoof(geometry, 0.2);//FIXME
        if (lOS == null || lOS.isEmpty()) {
            poly = SmallestSurroundingRectangleComputation.getSSR(geometry);
        }else {
            poly = SmallestSurroundingRectangleComputation.getSSR(geometry.getFactory().createMultiPolygon(lOS.toArray(new Polygon[0])));
        }
        Box3D b = new Box3D(geometry);
        minZ = b.getLLDP().getZ();
        maxZ = b.getURDP().getZ();
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public double getHeight() {
        return (this.getMaxZ() - this.getMinZ());
    }

    private void calculateWidthAndLength() {
        CoordinateList dpl = new CoordinateList(poly.getCoordinates());
        double v1 = dpl.get(0).distance(dpl.get(1));
        double v2 = dpl.get(1).distance(dpl.get(2));
        length = Math.max(v1, v2);
        width = Math.min(v1, v2);
    }

    public double getWidth() {
        if (width == -1) {
            calculateWidthAndLength();
        }
        return width;
    }

    public double getLength() {
        if (length == -1) {
            calculateWidthAndLength();
        }
        return length;
    }

    public Coordinate getCentre() {
        if (centre == null) {
            Coordinate dp1 = poly.getCoordinates()[0];
            Coordinate dp2 = poly.getCoordinates()[2];
            double x = (dp1.getX() + dp2.getX()) / 2;
            double y = (dp1.getY() + dp2.getY()) / 2;
            centre = new Coordinate(x, y);
        }
        return centre;
    }

    public Polygon getPoly() {
        return this.poly;
    }

    private void calculateDirection() {
        CoordinateList dpl = new CoordinateList(this.getPoly().getCoordinates());
        double v1 = dpl.get(0).distance(dpl.get(1));
        double v2 = dpl.get(1).distance(dpl.get(2));
        if (v1 > v2) {
            shortestDirection = new Vecteur(dpl.get(1), dpl.get(2));
            longestDirection = new Vecteur(dpl.get(0), dpl.get(1));
        } else {
            shortestDirection = new Vecteur(dpl.get(0), dpl.get(1));
            longestDirection = new Vecteur(dpl.get(1), dpl.get(2));
        }
    }

    public Vecteur shortestDirection() {
        if (shortestDirection == null) {
            calculateDirection();
        }
        return shortestDirection;
    }

    public Vecteur longestDirection() {
        if (longestDirection == null) {
            calculateDirection();
        }
        return longestDirection;
    }
    
    public List<LineString> getShortestEdges(){
        if(shortestEdges == null){
            calculateEdges();
        }
        return shortestEdges;
    }
    
    public List<LineString> getLongestEdges(){
        if(longestEdges == null){
            calculateEdges();
        }
        return longestEdges;
    }

    private void calculateEdges(){
        shortestEdges = new ArrayList<>();
        longestEdges = new ArrayList<>();
        CoordinateList dpl = new CoordinateList(this.getPoly().getCoordinates());
        double v1 = dpl.get(0).distance(dpl.get(1));
        double v2 = dpl.get(1).distance(dpl.get(2));
        CoordinateList dpl1 = new CoordinateList() ;
        CoordinateList dpl2 =  new CoordinateList();
        dpl1.add(dpl.get(1));
        dpl1.add(dpl.get(2));
        dpl2.add(dpl.get(3));
        dpl2.add(dpl.get(4));
        CoordinateList dpl3 = new CoordinateList()  ;
        CoordinateList dpl4 =  new CoordinateList() ;
        dpl3.add(dpl.get(0));
        dpl3.add(dpl.get(1));
        dpl4.add(dpl.get(2));
        dpl4.add(dpl.get(3));
        if (v1 > v2) {
            shortestEdges.add(poly.getFactory().createLineString(dpl1.toCoordinateArray()));
            shortestEdges.add(poly.getFactory().createLineString(dpl2.toCoordinateArray()));
            longestEdges.add(poly.getFactory().createLineString(dpl3.toCoordinateArray()));
            longestEdges.add(poly.getFactory().createLineString(dpl4.toCoordinateArray()));
        }else{
            shortestEdges.add(poly.getFactory().createLineString(dpl3.toCoordinateArray()));
            shortestEdges.add(poly.getFactory().createLineString(dpl4.toCoordinateArray()));
            longestEdges.add(poly.getFactory().createLineString(dpl1.toCoordinateArray()));
            longestEdges.add(poly.getFactory().createLineString(dpl2.toCoordinateArray()));
        }
    }
}
