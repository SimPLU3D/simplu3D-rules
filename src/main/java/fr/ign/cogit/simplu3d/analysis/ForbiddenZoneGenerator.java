package fr.ign.cogit.simplu3d.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.Prescription;
import fr.ign.cogit.simplu3d.model.PrescriptionType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.SpatialIndex;
import org.locationtech.jts.index.strtree.STRtree;

public class ForbiddenZoneGenerator {
    private static double DISTANCERECOILEVEGETATION = 3;
	private static double DISTANCERECOILERESERVEDEMPLACEMENT = 3;
	private static double DISTANCERECOILPAYSAGE = 3;
	private static double DISTANCERENUISANCERISQUE = 1;
	private static double DISTANCETVB = 3;
	private static double DISTANCEALIGNMENT = 1;

	public void setDistanceAlignment(double distance) {
		DISTANCEALIGNMENT = distance;
	}
	
	public void setDistanceTVB(double distance) {
		DISTANCETVB = distance;
	}

	public void setDistanceRecoilVegetation(double distance) {
		DISTANCERECOILEVEGETATION = distance;
	}

	public void setDistanceRecoilReservedEmplacement(double distance) {
		DISTANCERECOILERESERVEDEMPLACEMENT = distance;
	}

	public void setDistanceRecoilPaysage(double distance) {
		DISTANCERECOILPAYSAGE = distance;
	}

	public void setDistancenNuisanceRisque(double distance) {
		DISTANCERENUISANCERISQUE = distance;
	}

	public Geometry generateUnionGeometry(Collection<Prescription> prescriptions, BasicPropertyUnit bPU) {
		SpatialIndex index = new STRtree();
		for (Prescription p : prescriptions) {
			index.insert(p.getGeometry().getEnvelopeInternal(), p);
		}
        double radiusOfInfluence = 20;
        Geometry selectionGeom = bPU.getGeometry().buffer(radiusOfInfluence);
		@SuppressWarnings("unchecked")
		Collection<Prescription> selected = index.query(selectionGeom.getEnvelopeInternal());
		selected = selected.stream().filter(p->p.getGeometry().intersects(selectionGeom)).toList();
		List<Geometry> geometries = new ArrayList<>();
		for (Prescription pres : selected) {
			Geometry geom = generateWithCheckedGeom(pres, bPU);
			if (geom != null && !geom.isEmpty()) {
				geometries.add(geom);
			}
		}
		if (geometries.isEmpty())
			return null;
		Geometry geom = geometries.remove(0);
		for (Geometry geomTemp : geometries) {
			geom = geom.union(geomTemp);
		}
		return geom;
	}

	public Geometry generate(Prescription p, BasicPropertyUnit bPU) {
		if (!bPU.getGeometry().intersects(p.getGeometry()))
			return null;
		return generateWithCheckedGeom(p, bPU);
	}

	private Geometry generateWithCheckedGeom(Prescription p, BasicPropertyUnit bPU) {
		PrescriptionType type = p.getType();
        return switch (type) {
            case ELEMENT_PAYSAGE -> generateGeometryElementPaysage(p, bPU);
            case EMPLACEMENT_RESERVE -> generateEmplacementReserve(p, bPU);
            case ESPACE_BOISE -> generateEspaceBoise(p, bPU);
            case NUISANCES_RISQUES -> generateNuisanceRisque(p, bPU);
            case RECOIL -> generateRecul(p, bPU);
            case TVB -> generateTVB(p, bPU);
            default -> null;
        };
    }

	private Geometry generateTVB(Prescription p, BasicPropertyUnit bPU) {
        return switch (p.getGeometry().getDimension()) {
            case 0, 1 -> p.getGeometry().buffer(DISTANCETVB);
            case 2 -> p.getGeometry();
            default -> null;
        };
    }

	private Geometry generateRecul(Prescription p, BasicPropertyUnit bPU) {
        return switch (p.getGeometry().getDimension()) {
            case 0, 1 -> generateReculFacade(p, bPU);
            case 2 -> p.getGeometry();
            default -> null;
        };
    }
	
	private Geometry generateReculFacade(Prescription p, BasicPropertyUnit bPU) {
		//Object recul = p.getAttribute(PrescriptionReader.ATT_RECOIL);
		Double recul = p.getPrescriptionValue();//FIXME
		double valRecul =0.0;
		if (recul == null) {
			valRecul = DISTANCEALIGNMENT;
		}
		else {
			 valRecul = recul;//Double.parseDouble(recul.toString());
		}
		return generateReculFacade(p, bPU, valRecul);
	}

	private Geometry generateReculFacade(Prescription p, BasicPropertyUnit bPU, double valRecul) {
		if (valRecul <= 0)
			return null;
		return p.getGeometry().buffer(valRecul);
	}

	private Geometry generateEspaceBoise(Prescription p, BasicPropertyUnit bPU) {
        return switch (p.getGeometry().getDimension()) {
            case 0, 1 -> p.getGeometry().buffer(DISTANCERECOILEVEGETATION);
            case 2 -> p.getGeometry();
            default -> null;
        };
    }

	private Geometry generateEmplacementReserve(Prescription p, BasicPropertyUnit bPU) {
        return switch (p.getGeometry().getDimension()) {
            case 0, 1 -> p.getGeometry().buffer(DISTANCERECOILERESERVEDEMPLACEMENT);
            case 2 -> p.getGeometry();
            default -> null;
        };
    }

	private Geometry generateNuisanceRisque(Prescription p, BasicPropertyUnit bPU) {
        return switch (p.getGeometry().getDimension()) {
            case 0, 1 -> p.getGeometry().buffer(DISTANCERENUISANCERISQUE);
            case 2 -> p.getGeometry();
            default -> null;
        };
    }

	private Geometry generateGeometryElementPaysage(Prescription p, BasicPropertyUnit bPU) {
        return switch (p.getGeometry().getDimension()) {
            case 0, 1 -> p.getGeometry().buffer(DISTANCERECOILPAYSAGE);
            case 2 -> p.getGeometry();
            default -> null;
        };
    }
}
