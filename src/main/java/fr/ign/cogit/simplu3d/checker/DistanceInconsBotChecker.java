package fr.ign.cogit.simplu3d.checker;

import java.util.ArrayList;
import java.util.List;

import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.convert.FromGeomToLineString;
import fr.ign.cogit.geoxygene.convert.FromGeomToSurface;
import fr.ign.cogit.geoxygene.sig3d.convert.transform.Extrusion2DObject;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.simplu3d.checker.iauidf.Regulation;
import fr.ign.cogit.simplu3d.model.AbstractBuilding;
import fr.ign.cogit.simplu3d.model.BasicPropertyUnit;
import fr.ign.cogit.simplu3d.model.CadastralParcel;
import fr.ign.cogit.simplu3d.model.ParcelBoundary;
import fr.ign.cogit.simplu3d.model.ParcelBoundaryType;
import fr.ign.cogit.simplu3d.model.SubParcel;

/**
 * Une bande de getBandIncons() m (par rapport au fond de la parcelle) est
 * inconstructible
 * 
 * @author MBrasebin
 * @author MBorne
 *
 */
public class DistanceInconsBotChecker implements IRuleChecker {

	public final static String CODE_BANDE_FOND_PARCELLE = "BANDE_FOND_PARCELLE";

	private Rules rules;

	public DistanceInconsBotChecker(Rules rules) {
		this.rules = rules;
	}

	@Override
	public List<UnrespectedRule> check(BasicPropertyUnit bPU, RuleContext context) {
		List<UnrespectedRule> lUNR = new ArrayList<>();

		// On récupère la limite de fond
		IMultiCurve<IOrientableCurve> ims = getBotLimit(bPU);

		if (ims.isEmpty()) {
			return lUNR;
		}

		// On récupère les parties de bâtiments
		for (CadastralParcel cP : bPU.getCadastralParcels()) {

			for (SubParcel sP : cP.getSubParcels()) {

				for (AbstractBuilding bP : sP.getBuildingsParts()) {

					// On vérifie si la distance est bonne
					if (bP.getFootprint().distance(ims) < rules.getBandIncons()) {

						IGeometry buffer = ims.buffer(rules.getBandIncons());

						IGeometry geomError = cP.getGeom().intersection(buffer);

						lUNR.add(new UnrespectedRule("Bande de constructibilité de fond de parcelle non respectée ",
								geomError, CODE_BANDE_FOND_PARCELLE));
					}

				}

			}
		}

		return lUNR;
	}

	/**
	 * TODO describe
	 * @param bPU
	 * @return
	 */
	private IMultiCurve<IOrientableCurve> getBotLimit(BasicPropertyUnit bPU) {
		IMultiCurve<IOrientableCurve> img = new GM_MultiCurve<>();

		for (CadastralParcel cP : bPU.getCadastralParcels()) {
			for (ParcelBoundary sc : cP.getBoundaries()) {
				if (sc.getType() == ParcelBoundaryType.BOT) {
					img.addAll(FromGeomToLineString.convert(sc.getGeom()));
				}
			}
		}

		return img;
	}
	

    @Override
    public List<GeometricConstraints> generate(BasicPropertyUnit bPU) {
      List<GeometricConstraints>  geomConstraints = new ArrayList<>();
      
      IMultiCurve<IOrientableCurve>  iCurve = this.getBotLimit(bPU);
      

      Regulation r1 = bPU.getR1();
      
      
      if(r1!=null && r1.getArt_6() !=99){
        
        GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(bPU,r1,iCurve);
        if(gc!=null){
          geomConstraints.add(gc);
        
          
        }
        
  
      }
      
      Regulation r2 = bPU.getR2();
      
      
      
      if(r2!=null && r2.getArt_6() !=99){
        
        GeometricConstraints gc = generateGEometricConstraintsForOneRegulation(bPU,r2,iCurve);
        if(gc!=null){
          geomConstraints.add(gc);
        
          
        }
        
  
      }
      
      
      
      return geomConstraints;
          
    }
    
    private GeometricConstraints generateGEometricConstraintsForOneRegulation(BasicPropertyUnit bPU, Regulation r,IMultiCurve<IOrientableCurve>  iCurve ){
      IGeometry geom = bPU.getGeom().intersection(iCurve.buffer(r.getArt_6()));
      IMultiSurface<IOrientableSurface> iMS = FromGeomToSurface.convertMSGeom(geom);
      
      IGeometry finalGeom = iMS.intersection(r.getGeomBande());
      IMultiSurface<IOrientableSurface> iMSFinale = FromGeomToSurface.convertMSGeom(finalGeom);
      
      if(iMSFinale != null && ! iMSFinale.isEmpty() && iMSFinale.area() > 0.5){
        IGeometry returnedGeom = Extrusion2DObject.convertFromGeometry(iMSFinale, 0, 0);
        GeometricConstraints gC = new GeometricConstraints("Recul d'une distance de " + r.getArt_6()+ " m par rapport à la voirie", returnedGeom, "Art 6");
        return gC;
      }
       return null;
    }

}
