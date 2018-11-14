package fr.ign.cogit.simplu3d.io.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.simplu3d.model.CadastralParcel;

/**
 * 
 * CadastralParcel reader
 * 
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
	public Collection<CadastralParcel> readAll(IFeatureCollection<IFeature> features) {
		List<CadastralParcel> result = new ArrayList<>(features.size());
		for (IFeature feature : features) {
			// We split parcel with multi geometries into several parcels
			List<IOrientableSurface> lSurfaces = FromGeomToSurface.convertGeom(feature.getGeom());
			int nbSurfaces = lSurfaces.size();
			if (nbSurfaces == 1) {
				result.add(read(feature));
			} else if (nbSurfaces > 1) {
				for (IOrientableSurface surf : lSurfaces) {

					try {
						IFeature featTemp = feature.cloneGeom();
						featTemp.setGeom(surf);
						result.add(read(featTemp));
					} catch (CloneNotSupportedException e) {

						e.printStackTrace();
					}

				}
			}

		}
		return result;
	}

	@Override
	public CadastralParcel read(IFeature feature) {
		CadastralParcel cadastralParcel = new CadastralParcel();
		cadastralParcel.setId(feature.getId());

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
					e2.printStackTrace();
					System.out.println("Attribute : " + ATT_HAS_TO_BE_SIMULATED+ " from parcel is not Integer or Boolean. Value : " + o);
				}
			}

		}

		IGeometry geometry = feature.getGeom();
		if (!geometry.isSimple()) {
			logger.warning("Geometry is not simple for CadastralParcel " + code + "!");
		}
		List<IOrientableSurface> polygons = FromGeomToSurface.convertGeom(geometry);
		if (polygons.size() == 1) {
			IPolygon polygon = (IPolygon) polygons.get(0);
			cadastralParcel.setGeom(polygon);
		} else if (polygons.size() > 1) {
			throw new RuntimeException("CadastralParcel should be either Polygon or MultiPolygon with 1 polygon (" + polygons.size() + " found) - ID : " + code);
		}
		return cadastralParcel;
	}

}
