package fr.ign.cogit.simplu3d.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import fr.ign.cogit.simplu3d.dao.SpatialRepository;
import fr.ign.cogit.simplu3d.io.feature.ParcelBoundaryReader;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.common.analysis.FindObjectInDirection;

public class OppositeBoundaryFinder {

	private final static Logger logger = Logger.getLogger(OppositeBoundaryFinder.class.getName());
	private final SpatialRepository<ParcelBoundary> boundaries;
	private static double maximalValue = 50;
	public static double getMaximalValue() {
		return maximalValue;
	}

	public static void setMaximalValue(double maximalValue) {
		OppositeBoundaryFinder.maximalValue = maximalValue;
	}

	public OppositeBoundaryFinder(SpatialRepository<CadastralParcel> parcelles) {
		Collection<ParcelBoundary> collection = new ArrayList<>();
		for (CadastralParcel parcel : parcelles.findAll()) {
			collection.addAll(parcel.getBoundariesByType(ParcelBoundaryType.ROAD));
		}
		boundaries = new ParcelBoundaryReader().create(collection, parcelles.getCoordinateReferenceSystem());
	}

	public ParcelBoundary find(ParcelBoundary bound, CadastralParcel parcel) {
		if (bound.getGeometry().getCoordinates().length != 2) {
			logger.warning(OppositeBoundaryFinder.class + " POSITION SIZE FOR A BOUNDARY IS DIFFERENT THAN 2 "
					+ parcel.getCode());
		}
		return FindObjectInDirection.find(bound, parcel, this.boundaries, maximalValue);
	}
}
