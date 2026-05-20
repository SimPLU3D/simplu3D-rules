package fr.ign.cogit.simplu3d.analysis;

import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;

import java.util.Collection;

public class AssignOppositeToBoundary {
	public static void process(Collection<CadastralParcel> cadastralParcels) {
		OppositeBoundaryFinder  finder = new OppositeBoundaryFinder(cadastralParcels);
		for (CadastralParcel parcel : cadastralParcels) {
			for (ParcelBoundary bound : parcel.getBoundariesByType(ParcelBoundaryType.ROAD)) {
				bound.setOppositeBoundary(finder.find(bound, parcel));
			}
		}
	}
}
