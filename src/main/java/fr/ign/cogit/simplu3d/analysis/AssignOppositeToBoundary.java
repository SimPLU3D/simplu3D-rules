package fr.ign.cogit.simplu3d.analysis;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;

public class AssignOppositeToBoundary {

	public static void process(IFeatureCollection<CadastralParcel> cadastralParcels) {

		for (CadastralParcel parcel : cadastralParcels) {

			for (ParcelBoundary bound : parcel.getBoundariesByType(ParcelBoundaryType.ROAD)) {
				OppositeBoundaryFinder finder = new OppositeBoundaryFinder(cadastralParcels);
				bound.setOppositeBoundary(finder.find(bound, parcel));

			}

		}
	}
}
