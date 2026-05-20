package fr.ign.common.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import java.util.logging.Logger;

public class SmallestSurroundingRectangleComputation {
    private static final Logger logger = Logger.getLogger(SmallestSurroundingRectangleComputation.class.getName());

    public static Polygon getSSR(Geometry geom) {
        logger.finer("SSR computation of " + geom);
        Geometry hull_ = geom.convexHull();
        // Convex hull is not a polygon; the SSR is not defined: return null
        if (!(hull_ instanceof Polygon convHull)) {
            logger.warning(
                    "WARN in SSR computation of " + geom + ": convex hull computation returned something else as a polygon: " + hull_);
            return null;
        }
        // center coordinates (for rotation)
        Coordinate rotationCenter = geom.getCentroid().getCoordinate();
        // get convex hull coordinates
        Coordinate[] coord = convHull.getExteriorRing().getCoordinates();
        // go through the segments
        double minArea = Double.MAX_VALUE, minAngle = 0.0;
        Polygon ssr = null;
        for (int i = 0; i < coord.length - 1; i++) {
            // compute the rectangular hull of the rotated convew hull
            // compute the angle value
            double angle = Math.atan2(coord[i + 1].y - coord[i].y, coord[i + 1].x - coord[i].x);
            Polygon rect = (Polygon) GeomUtils.rotation(convHull, rotationCenter, -1.0 * angle).getEnvelope();
            logger.finer("   rectangle hull try: " + rect);
            // compute the rectangle area
            double area = rect.getArea();
            // check if it is minimum
            if (area < minArea) {
                minArea = area;
                ssr = rect;
                minAngle = angle;
            }
        }
        assert ssr != null;
        return GeomUtils.rotation(ssr, rotationCenter, minAngle);
    }
}
