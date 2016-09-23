package fr.ign.cogit.simplu3d.io.feature;

import java.util.List;
import java.util.logging.Logger;

import fr.ign.cogit.geoxygene.api.feature.IFeature;
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
public class CadastralParcelReader extends AbstractFeatureReader<CadastralParcel>{
	
	private final static Logger logger = Logger.getLogger(CadastralParcelReader.class.getCanonicalName());
	
	public static final String ATT_CODE_PARC = "CODE";
	
	public static final String ATT_BDP_CODE_DEP = "CODE_DEP";
	public static final String ATT_BDP_CODE_COM = "CODE_COM";
	public static final String ATT_BDP_COM_ABS = "COM_ABS";	
	public static final String ATT_BDP_SECTION = "SECTION";	
	public static final String ATT_BDP_NUMERO = "NUMERO";	

	public static final String ATT_HAS_TO_BE_SIMULATED = "SIMUL";
	
	@Override
	public CadastralParcel read(IFeature feature) {
		CadastralParcel cadastralParcel = new CadastralParcel();
		cadastralParcel.setId(feature.getId());

		/*
		 * read code attribute
		 */
		String code = readStringAttribute(feature, ATT_CODE_PARC);
		if ( code == null ){
			String codeDep = readStringAttribute(feature, ATT_BDP_CODE_DEP);
			String codeCom = readStringAttribute(feature, ATT_BDP_CODE_COM);
			String comAbs  = readStringAttribute(feature, ATT_BDP_COM_ABS);
			String section = readStringAttribute(feature, ATT_BDP_SECTION);
			String numero = readStringAttribute(feature, ATT_BDP_NUMERO);
			// complete BDP
			if ( codeDep != null && codeCom != null && comAbs != null && section != null && numero != null ){
				code = codeDep+codeCom+comAbs+section+numero;
			}
		}
		cadastralParcel.setCode(code);
		
		/*
		 * read simulation attribute
		 */
		Object o = findAttribute(feature, ATT_HAS_TO_BE_SIMULATED);
		if (o != null) {
			cadastralParcel.setHasToBeSimulated(1 == Integer.parseInt(o.toString()));
		}

		IGeometry geometry = feature.getGeom() ;
		if ( ! geometry.isSimple() ){
			logger.warning("Geometry is not simple for CadastralParcel "+code+"!");
		}
		List<IOrientableSurface> polygons = FromGeomToSurface.convertGeom(geometry);
		if ( polygons.size() == 1 ){
			IPolygon polygon = (IPolygon)polygons.get(0);
			cadastralParcel.setGeom(polygon);
		}else if ( polygons.size() > 1 ){
			throw new RuntimeException(
				"CadastralParcel should be either Polygon or MultiPolygon with 1 polygon ("+polygons.size()+" found)"
			);
		}
		return cadastralParcel;
	}
	

}
