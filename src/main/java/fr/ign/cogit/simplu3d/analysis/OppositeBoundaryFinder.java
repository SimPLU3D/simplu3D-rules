package fr.ign.cogit.simplu3d.analysis;

import java.util.logging.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.analysis.FindObjectInDirection;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;

public class OppositeBoundaryFinder {

	private final static Logger logger = Logger.getLogger(OppositeBoundaryFinder.class.getName());

	private IFeatureCollection<ParcelBoundary> boundaries;

	private static double maximalValue = 50;

	public static double getMaximalValue() {
		return maximalValue;
	}

	public static void setMaximalValue(double maximalValue) {
		OppositeBoundaryFinder.maximalValue = maximalValue;
	}

	public OppositeBoundaryFinder(IFeatureCollection<CadastralParcel> parcelles) {
		boundaries = new FT_FeatureCollection<>();
		for (CadastralParcel parcel : parcelles) {
			boundaries.addAll(parcel.getBoundariesByType(ParcelBoundaryType.ROAD));
		}

		if (!this.boundaries.hasSpatialIndex()) {
			this.boundaries.initSpatialIndex(Tiling.class, false);
		}
	}

	public ParcelBoundary find(ParcelBoundary bound, CadastralParcel parcel) {

		if (bound.getGeom().coord().size() != 2) {
			logger.warning(OppositeBoundaryFinder.class + " POSITION SIZE FOR A BOUNDARY IS DIFFERENT THAN 2 "
					+ parcel.getCode());
		}

		return (ParcelBoundary) FindObjectInDirection.find(bound, parcel, this.boundaries, maximalValue);
	}

}
