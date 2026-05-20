package fr.ign.common.analysis.parcel;

import fr.ign.cogit.simplu3d.model.Building;

import java.util.List;

public class COSCalculation {
    public static double assess(List<Building> buildings, SHONCalculation.METHOD method, double area) {
        return SHONCalculation.assess(buildings, method) / area;
    }
}
