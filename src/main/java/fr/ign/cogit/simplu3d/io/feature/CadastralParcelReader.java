package fr.ign.cogit.simplu3d.io.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/*
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
*/
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.common.conversion.FromGeomToSurface;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Polygonal;

/**
 * 
 * CadastralParcel reader
 * <p>
 * TODO cleanup "code" management. Currently either "CODE" or CODE_DEP+CODE_COM+COM_ABS+SECTION+NUMERO
 * 
 * @author MBorne
 *
 */
public class CadastralParcelReader extends AbstractFeatureReader<CadastralParcel> {

	private final static Logger logger = Logger.getLogger(CadastralParcelReader.class.getCanonicalName());

	public static final String ATT_CODE_PARC;// = "CODE";

	public static final String ATT_BDP_CODE_DEP;// = "CODE_DEP";
	public static final String ATT_BDP_CODE_COM;// = "CODE_COM";
	public static final String ATT_BDP_COM_ABS;// = "COM_ABS";
	public static final String ATT_BDP_SECTION;// = "SECTION";
	public static final String ATT_BDP_NUMERO;// = "NUMERO";

	public static final String ATT_HAS_TO_BE_SIMULATED;// = "SIMUL";

	static {
		ATT_CODE_PARC = AttribNames.getATT_CODE_PARC();
		ATT_BDP_CODE_DEP = AttribNames.getATT_BDP_CODE_DEP();
		ATT_BDP_CODE_COM = AttribNames.getATT_BDP_CODE_COM();
		ATT_BDP_COM_ABS = AttribNames.getATT_BDP_COM_ABS();
		ATT_BDP_SECTION = AttribNames.getATT_BDP_SECTION();
		ATT_BDP_NUMERO = AttribNames.getATT_BDP_NUMERO();
		ATT_HAS_TO_BE_SIMULATED = AttribNames.getATT_HAS_TO_BE_SIMULATED();
	}

	@Override
	public Collection<CadastralParcel> readAll(FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
		List<CadastralParcel> result = new ArrayList<>(collection.size());
        try (FeatureIterator<SimpleFeature> iterator = collection.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                // We split parcel with multi geometries into several parcels
                List<Polygon> lSurfaces = FromGeomToSurface.convertGeom((Geometry) feature.getDefaultGeometry());
                int nbSurfaces = lSurfaces.size();
                if (nbSurfaces == 1) {
                    result.add(read(feature));
                } else if (nbSurfaces > 1) {
                    for (Polygonal surf : lSurfaces) {
                        //FIXME no idea if that works
                        SimpleFeature featTemp = new SimpleFeatureImpl(feature.getAttributes(), feature.getFeatureType(), feature.getIdentifier());
                        featTemp.setDefaultGeometry(surf);
                        result.add(read(featTemp));
                    }
                }
            }
        }
		return result;
	}

	@Override
	public CadastralParcel read(SimpleFeature feature) {
		CadastralParcel cadastralParcel = new CadastralParcel();
		cadastralParcel.setId(feature.getID());
		/*
		 * read code attribute
		 */
		String code = readStringAttribute(feature, ATT_CODE_PARC);
		if (code == null) {
			String codeDep = readStringAttribute(feature, ATT_BDP_CODE_DEP);
			String codeCom = readStringAttribute(feature, ATT_BDP_CODE_COM);
			String comAbs = readStringAttribute(feature, ATT_BDP_COM_ABS);
			String section = readStringAttribute(feature, ATT_BDP_SECTION);
			String numero = readStringAttribute(feature, ATT_BDP_NUMERO);
			// complete BDP
			if (codeDep != null && codeCom != null && comAbs != null && section != null && numero != null) {
				code = codeDep + codeCom + comAbs + section + numero;
			}
		}
		cadastralParcel.setCode(code);

		/*
		 * read simulation attribute
		 */
		Object o = findAttribute(feature, ATT_HAS_TO_BE_SIMULATED);
		if (o != null) {

			try {
				cadastralParcel.setHasToBeSimulated(1 == Integer.parseInt(o.toString()));
			} catch (Exception e) {
				try {
					cadastralParcel.setHasToBeSimulated(Boolean.parseBoolean(o.toString()));

				} catch (Exception e2) {
					logger.severe(e2.getMessage());
					logger.severe("Attribute : " + ATT_HAS_TO_BE_SIMULATED+ " from parcel is not Integer or Boolean. Value : " + o);
				}
			}

		}

		Geometry geometry = (Geometry) feature.getDefaultGeometry();
		List<Polygon> polygons = FromGeomToSurface.convertGeom(geometry);
		if (polygons.size() == 1) {
			Polygon polygon = polygons.get(0);
			cadastralParcel.setGeometry(polygon);
		} else if (polygons.size() > 1) {
			throw new RuntimeException("CadastralParcel should be either Polygon or MultiPolygon with 1 polygon (" + polygons.size() + " found) - ID : " + code);
		}
		return cadastralParcel;
	}
}
