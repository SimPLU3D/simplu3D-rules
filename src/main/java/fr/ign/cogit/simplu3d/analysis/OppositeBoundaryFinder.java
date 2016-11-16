package fr.ign.cogit.simplu3d.analysis;

import java.util.logging.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.sig3d.util.MathConstant;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.util.index.Tiling;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;

public class OppositeBoundaryFinder {

	private final static Logger logger = Logger.getLogger(OppositeBoundaryFinder.class.getName());

	private IFeatureCollection<ParcelBoundary> boundaries;

	private double maximumDistance = 50.0;
	private double smallVectorSize = 0.01;

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

		IGeometry geom = bound.getGeom();

		ILineString ls = this.generateLineofSight(geom, parcel);

		if (ls == null) {
			return null;
		}

		double distance = Double.POSITIVE_INFINITY;
		ParcelBoundary bestcandidateParcel = null;

		for (ParcelBoundary boundaryTemp : boundaries.select(ls)) {


			double distTemp = boundaryTemp.getGeom().distance(geom);
			
			if(parcel.getBoundaries().contains(boundaryTemp)){
				continue;
			}
			


			if (distTemp < distance) {
				distance = distTemp;
				bestcandidateParcel = boundaryTemp;
			}

		}

		return bestcandidateParcel;
	}

	private ILineString generateLineofSight(IGeometry geom, CadastralParcel parcel) {
		IDirectPositionList dplBound = geom.coord();

		if (dplBound.size() != 2) {
			logger.warning(OppositeBoundaryFinder.class + " POSITION SIZE FOR A BOUNDARY IS DIFFERENT THAN 2");
		}

		IDirectPosition dp1 = dplBound.get(0);
		IDirectPosition dp2 = dplBound.get(1);

		Vecteur vLine = new Vecteur(dp1, dp2);

		Vecteur vectOrth = vLine.prodVectoriel(MathConstant.vectZ).getNormalised();
		Vecteur vectOrthNeg = vectOrth.multConstante(-1).getNormalised();

		vectOrth = vectOrth.multConstante(smallVectorSize);
		vectOrthNeg = vectOrthNeg.multConstante(smallVectorSize);

		IDirectPosition lineCenter = geom.centroid();

		IDirectPosition dpDep = vectOrth.translate(lineCenter);
		IDirectPosition dpDepNeg = vectOrthNeg.translate(lineCenter);

		boolean isInPolygonDep = parcel.getGeom().contains(new GM_Point(dpDep));
		boolean isInPolygonDepNeg = parcel.getGeom().contains(new GM_Point(dpDepNeg));

		if (isInPolygonDep && isInPolygonDepNeg) {
			logger.warning(OppositeBoundaryFinder.class + " TRANSLATION IS IN PARCEL IN BOTH DIRECTION");
		}

		if ((!isInPolygonDep) && (!isInPolygonDepNeg)) {
			logger.warning(OppositeBoundaryFinder.class + " TRANSLATION IS IN PARCEL IN NO DIRECTION");
		}

		Vecteur rightVector = null;

		if (isInPolygonDep) {
			vectOrthNeg.normalise();
			rightVector = vectOrthNeg.multConstante(maximumDistance);

		}

		if (!isInPolygonDep) {
			vectOrth.normalise();
			rightVector = vectOrth.multConstante(maximumDistance);
		}

		if (rightVector == null) {
			return null;
		}

		IDirectPosition dpLine = rightVector.translate(lineCenter);

		IDirectPositionList dplLineString = new DirectPositionList();
		dplLineString.add(dpLine);
		dplLineString.add(lineCenter);

		return new GM_LineString(dplLineString);

	}

	public double getMaximumDistance() {
		return maximumDistance;
	}

	public void setMaximumDistance(double maximumDistance) {
		this.maximumDistance = maximumDistance;
	}

}
