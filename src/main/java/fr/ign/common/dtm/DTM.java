package fr.ign.common.dtm;

import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.Polygon;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class DTM extends AbstractDTM {
    private static final Logger logger = Logger.getLogger(DTM.class.getName());

    public int echantillonage = 1;
    public static int CONSTANT_OFFSET;
    /*
     * Les paramètres du MNT xIni : il s'agit du X du coin supérieur gauche du MNT
     * yIni : il s'agit du Y du coin supérieur gauche du MNT pasX : il s'agit du pas
     * en X du MNT (echantillonnage inclus) pasY : il s'agit du pas en Y du MNT
     * (echantillonnage inclus) nX : il s'agit du nombre de mailles en X à afficher
     * nY : il s'agit du nombre de mailles en Y à afficher strip : il s'agit de la
     * liste de triangles formant le MNT exageration : il s'agi du coefficient en Z
     * que l'on applique bgeo : il s'agit de la BranchGroup représentant le MNT
     */
    protected double xIni;
    protected double yIni;
    protected double zMax;
    protected double zMin;
    protected double stepX;
    protected double stepY;
    protected int nX;
    protected int nY;
    protected double noDataValue;
    double[][] tabAlt = null;

    // Echantillonnage
    protected int sampling;

    //protected Color4f[] color4fShade = null;

    public DTM(String file, String layerName, int exager) {
        super(file, layerName, null, exager);
    }


    /**
     * Renvoie le triangle qui se trouve dans la maille indexX, indexY et dans le
     * bas de la maille si inferior est true et dans le haut de celle-ci sinon
     * Renvoie null si les indices ne correspondent pas à un triangle existant
     *
     * @param indexX   l'indice de la maille en X
     * @param indexY   l'indice de la maille en Y
     * @param inferior la position du triangle par rapport à la maille
     * @return une liste de points correspondants au sommet du triangle concerné
     */
    public Coordinate[] getTriangle(int indexX, int indexY, boolean inferior) {
        // Si les coordonnées ne sont pas dans le MNT, on renvoie null
        if (indexX >= (this.nX - 1) || indexX < 0 || indexY >= (this.nY - 1) || indexY < 0) {
            return null;
        }
        Coordinate pt12D = new Coordinate(indexX * stepX + this.xIni, indexY * stepY + this.yIni,
                tabAlt[indexX][indexY]);
        Coordinate pt22D = new Coordinate((indexX + this.echantillonage) * stepX + this.xIni,
                indexY * stepY + this.yIni, tabAlt[indexX + 1][indexY]);
        Coordinate pt32D = new Coordinate(indexX * stepX + this.xIni,
                (indexY + this.echantillonage) * stepY + this.yIni, tabAlt[indexX][indexY + 1]);
        Coordinate pt42D = new Coordinate((indexX + this.echantillonage) * stepX + this.xIni,
                (indexY + this.echantillonage) * stepY + this.yIni, tabAlt[indexX + 1][indexY + 1]);
        return inferior ? new Coordinate[]{pt12D, pt22D, pt32D} : new Coordinate[]{pt42D, pt22D, pt32D};
    }

    @Override
    public MultiPolygon processSurfacicGrid(double xmin, double xmax, double ymin, double ymax) {
        // On récupère dans quels triangles se trouvent dpMin et dpMax
        int posxMin = (int) ((xmin - this.xIni) / (this.stepX * this.echantillonage));
        int posyMin = (int) ((ymin - this.yIni) / (this.stepY * this.echantillonage));
        int posxMax = 1 + (int) ((xmax - this.xIni) / (this.stepX * this.echantillonage));
        int posyMax = 1 + (int) ((ymax - this.yIni) / (this.stepY * this.echantillonage));
        // On récupère les sommets extérieurs de ces triangles (ceux qui
        // permettent d'englober totalement le rectangle dpMin, dpMax
        Coordinate dpOrigin = new Coordinate(posxMin * this.stepX + this.xIni, posyMin * this.stepY + this.yIni);
        Coordinate dpFin = new Coordinate(posxMax * this.stepX + this.xIni, posyMax * this.stepY + this.yIni);

        // On évalue le nombre de mailles à couvrir
        int nbInterX = Math.max(1, (int) ((dpFin.x - dpOrigin.x) / this.stepX));
        int nbInterY = Math.max(1, (int) ((dpFin.y - dpOrigin.y) / this.stepY));

        Polygon[] lPolys = new Polygon[2 * nbInterX * nbInterY];
        int indPoly = 0;
        // On crée une géométrie géoxygne pour chacune de ces mailles
        // (2 triangles par maille)
        for (int i = 0; i < nbInterX; i++) {
            for (int j = 0; j < nbInterY; j++) {
                Coordinate dp1 = new Coordinate(dpOrigin.x + i * this.stepX, dpOrigin.y + j * this.stepY);
                Coordinate dp2 = new Coordinate(dpOrigin.x + (i + 1) * this.stepX, dpOrigin.y + j * this.stepY);
                Coordinate dp3 = new Coordinate(dpOrigin.x + i * this.stepX, dpOrigin.y + (j + 1) * this.stepY);
                Coordinate dp4 = new Coordinate(dpOrigin.x + (i + 1) * this.stepX, dpOrigin.y + (j + 1) * this.stepY);
                Coordinate[] coord = new Coordinate[4];
                coord[0] = dp1;
                coord[1] = dp2;
                coord[2] = dp4;
                coord[3] = dp1;
                LinearRing l1 = factory.createLinearRing(coord);
                Coordinate[] coord2 = new Coordinate[4];
                coord2[0] = dp1;
                coord2[1] = dp4;
                coord2[2] = dp3;
                coord2[3] = dp1;
                LinearRing l2 = factory.createLinearRing(coord2);
                lPolys[indPoly] = factory.createPolygon(l1, null);
                indPoly++;
                lPolys[indPoly] = factory.createPolygon(l2, null);
                indPoly++;
            }
        }
        // On renvoie la liste des triangles
        return factory.createMultiPolygon(lPolys);
    }

    @Override
    public MultiLineString processLinearGrid(double xmin, double ymin, double xmax, double ymax) {
        // On récupère l'indice des triangles contenant ces points
        int posxMin = (int) ((xmin - this.xIni) / (this.stepX * this.echantillonage));
        int posyMin = (int) ((ymin - this.yIni) / (this.stepY * this.echantillonage));

        int posxMax = 1 + (int) ((xmax - this.xIni) / (this.stepX * this.echantillonage));
        int posyMax = 1 + (int) ((ymax - this.yIni) / (this.stepY * this.echantillonage));

        // On récupère les points extrêmes appartenant à ces triangles
        Coordinate dpOrigin = new Coordinate(posxMin * this.stepX + this.xIni, posyMin * this.stepY + this.yIni);
        Coordinate dpFin = new Coordinate(posxMax * this.stepX + this.xIni, posyMax * this.stepY + this.yIni);

        // On calcule le nombre de géométries à générer
        int nbInterX = (int) ((dpFin.x - dpOrigin.x) / this.stepX);
        int nbInterY = (int) ((dpFin.y - dpOrigin.y) / this.stepY);

        nbInterX = Math.max(1, nbInterX);
        nbInterY = Math.max(1, nbInterY);

        LineString[] lineStrings = new LineString[(nbInterX) * (nbInterY) * 3 + 2];
        int indL = 0;

        // On crée 3 lignes par maill du MNT
        for (int i = 0; i < nbInterX; i++) {

            for (int j = 0; j < nbInterY; j++) {

                Coordinate dp1 = new Coordinate(dpOrigin.x + i * this.stepX, dpOrigin.y + j * this.stepY);
                Coordinate dp2 = new Coordinate(dpOrigin.x + (i + this.echantillonage) * this.stepX,
                        dpOrigin.y + j * this.stepY);
                Coordinate dp3 = new Coordinate(dpOrigin.x + i * this.stepX,
                        dpOrigin.y + (j + this.echantillonage) * this.stepY);
                Coordinate dp4 = new Coordinate(dpOrigin.x + (i + this.echantillonage) * this.stepX,
                        dpOrigin.y + (j + this.echantillonage) * this.stepY);

                Coordinate[] c1 = new Coordinate[2];
                Coordinate[] c2 = new Coordinate[2];
                Coordinate[] c3 = new Coordinate[2];

                c1[0] = dp1;
                c1[1] = dp2;

                LineString s1 = factory.createLineString(c1);

                c2[0] = dp1;
                c2[1] = dp3;

                LineString s2 = factory.createLineString(c2);

                c3[0] = dp1;
                c3[1] = dp4;

                LineString s3 = factory.createLineString(c3);

                lineStrings[indL] = s1;
                indL++;
                lineStrings[indL] = s2;
                indL++;
                lineStrings[indL] = s3;
                indL++;

            }

        }
        // On complète en ajoutant les bordures supérieures et inférieures (qui
        // ne sont pas parcourus par le boucle précédente
        Coordinate dpInterX = new Coordinate(dpFin.x, dpOrigin.y);
        Coordinate dpInterY = new Coordinate(dpOrigin.x, dpFin.y);

        Coordinate[] c1 = new Coordinate[2];
        c1[0] = dpInterX;
        c1[1] = dpFin;

        lineStrings[indL] = factory.createLineString(c1);
        indL++;

        Coordinate[] c2 = new Coordinate[2];
        c2[0] = dpInterY;
        c2[1] = dpFin;

        lineStrings[indL] = factory.createLineString(c2);

        // On renvoie le tout
        return factory.createMultiLineString(lineStrings);
    }

    @Override
    public Coordinate castCoordinate(double x, double y) {
        // Etant donne que l'on a translaté le MNT
        // On fait de meme avec le vecteur
        // On recupere la maille dans laquelle se trouve le point
        int posx = (int) ((x - this.xIni) / (this.stepX * this.stepX));
        int posy = (int) ((y - this.yIni) / (this.stepY * this.stepY));

        if (posx >= (this.nX) || posx < 0 || posy >= (this.nY) || posy < 0) {

            return new Coordinate(x, y, 0);

        } else if (posx == this.nX - 1) {

            return new Coordinate(x, y, 0);

        } else if (posy == this.nY - 1) {

            return new Coordinate(x, y, 0);
        } else {

            Coordinate pt12D = new Coordinate(posx * stepX + this.xIni, posy * stepY + this.yIni,
                    tabAlt[posx][posy]);
            Coordinate pt22D = new Coordinate((posx + this.echantillonage) * stepX + this.xIni,
                    posy * stepY + this.yIni, tabAlt[posx + 1][posy]);
            Coordinate pt32D = new Coordinate(posx * stepX + this.xIni,
                    (posy + this.echantillonage) * stepY + this.yIni, tabAlt[posx][posy + 1]);
            Coordinate pt42D = new Coordinate((posx + this.echantillonage) * stepX + this.xIni,
                    (posy + this.echantillonage) * stepY + this.yIni, tabAlt[posx + 1][posy + 1]);

            Coordinate ptloc2D = new Coordinate(x, y);

            double d1 = ptloc2D.distance(pt12D);
            double d2 = ptloc2D.distance(pt22D);

            double d3 = ptloc2D.distance(pt32D);

            double d4 = ptloc2D.distance(pt42D);

            double zMoy = 0;
            if (d1 < 5) {

                zMoy = pt12D.getZ();
                return new Coordinate(x, y, zMoy + DTM.CONSTANT_OFFSET);
            } else if (d2 < 5) {
                zMoy = pt22D.getZ();
                return new Coordinate(x, y, zMoy + DTM.CONSTANT_OFFSET);
            } else if (d3 < 5) {
                zMoy = pt32D.getZ();
                return new Coordinate(x, y, zMoy + DTM.CONSTANT_OFFSET);
            } else if (d4 < 5) {
                zMoy = pt42D.getZ();
                return new Coordinate(x, y, zMoy + DTM.CONSTANT_OFFSET);
            }
            Vecteur v1 = new Vecteur(pt22D, pt32D);
            Vecteur v2 = new Vecteur(pt22D, ptloc2D);
            double d = v1.prodVectoriel(v2).prodScalaire(new Vecteur(0, 0, 1));
            if (d > 0) {// Triangle 1 2 3
                double xn = (pt22D.getY() - pt12D.getY()) * (pt32D.getZ() - pt12D.getZ())
                        - (pt32D.getY() - pt12D.getY()) * (pt22D.getZ() - pt12D.getZ());
                double yn = (pt32D.getX() - pt12D.getX()) * (pt22D.getZ() - pt12D.getZ())
                        - (pt22D.getX() - pt12D.getX()) * (pt32D.getZ() - pt12D.getZ());
                double zn = (pt22D.getX() - pt12D.getX()) * (pt32D.getY() - pt12D.getY())
                        - (pt32D.getX() - pt12D.getX()) * (pt22D.getY() - pt12D.getY());
                zMoy = (pt12D.getX() * xn + pt12D.getY() * yn + pt12D.getZ() * zn - x * xn - y * yn) / zn;
            } else {// Il se trouve dans le triangle 2 3 4
                double xn = (pt22D.getY() - pt42D.getY()) * (pt32D.getZ() - pt42D.getZ())
                        - (pt32D.getY() - pt42D.getY()) * (pt22D.getZ() - pt42D.getZ());
                double yn = (pt32D.getX() - pt42D.getX()) * (pt22D.getZ() - pt42D.getZ())
                        - (pt22D.getX() - pt42D.getX()) * (pt32D.getZ() - pt42D.getZ());
                double zn = (pt22D.getX() - pt42D.getX()) * (pt32D.getY() - pt42D.getY())
                        - (pt32D.getX() - pt42D.getX()) * (pt22D.getY() - pt42D.getY());
                zMoy = (pt42D.getX() * xn + pt42D.getY() * yn + pt42D.getZ() * zn - x * xn - y * yn) / zn;
            }
            return new Coordinate(x, y, zMoy + DTM.CONSTANT_OFFSET);
        }
    }
    protected GeometryFactory factory = new GeometryFactory();
    @Override
    public Geometry getGeometryAt(double x, double y) {
        int posx = (int) ((x - this.xIni) / (this.stepX * this.echantillonage));
        int posy = (int) ((y - this.yIni) / (this.stepY * this.echantillonage));

        Coordinate[] lTri = this.getTriangle(posx, posy, true);
        Polygon tri = factory.createPolygon(lTri);

        if (tri.contains(factory.createPoint(new Coordinate(x, y)))) {
            return tri;
        }
        lTri = this.getTriangle(posx, posy, false);
        return factory.createPolygon(lTri);
    }
    protected void representationProcess(InputStream is, String layerName, int exager) {
        // Les informations concernant le coin supérieur gauche du MNT
        double shiftX = 0;
        double shiftY = 0;
        // Le nombre d'éléments présents dans le MNT
        int numligne = 0;
        int numcol = 0;
        // Valeur indiquant l'absence de données
        this.noDataValue = Double.NaN;
        this.zMin = Double.POSITIVE_INFINITY;
        this.zMax = Double.NEGATIVE_INFINITY;
        // Il s'agit d'une chaine contenant les informations en train d'être lu dans le fichier
        String ligne;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            // Lecture du fichier et récupèration des différentes valeurs citées précédemment
            ligne = br.readLine();
            String[] result = ligne.split(" ");
            int ncols = Integer.parseInt(result[result.length - 1]);
            // System.out.println("ncols" + result[result.length - 1]);
            ligne = br.readLine();
            result = ligne.split("\\s");
            // System.out.println("nrows" + result[result.length - 1]);
            int nrows = Integer.parseInt(result[result.length - 1]);
            ligne = br.readLine();
            result = ligne.split("\\s");
            shiftX = Double.parseDouble(result[result.length - 1]);
            // System.out.println("xllcorner" + result[result.length - 1]);
            ligne = br.readLine();
            result = ligne.split("\\s");
            shiftY = Double.parseDouble(result[result.length - 1]);
            // System.out.println("yllcorner" + shiftY);
            ligne = br.readLine();
            result = ligne.split("\\s");
            stepX = Double.parseDouble(result[result.length - 1]);
            if (result[0].equalsIgnoreCase("CELLSIZE")) {
                // There is not CELLSIZEX and CELLSIZEY
                stepY = stepX;
            } else {
                ligne = br.readLine();
                result = ligne.split("\\s");
                stepY = Double.parseDouble(result[result.length - 1]);
            }
            ligne = br.readLine();
            result = ligne.split("\\s");
            this.noDataValue = Double.parseDouble(result[result.length - 1]);
            ligne = br.readLine();
            double maxy = ((nrows - 1) * stepY + shiftY);
            // On prépare le nombre de lignes
            nrows = nrows / this.echantillonage;
            ncols = ncols / this.echantillonage;

            tabAlt = new double[nrows][ncols];
            // Tab contiendra les Z de la ligne précédente
            int[] tab = new int[nrows - 1];

            for (int i = 0; i < nrows - 1; i++) {
                tab[i] = 2 * ncols;
            }

            // On renseigne les différentes valeurs donnant des informations sur
            // le MNT
            this.xIni = shiftX;
            this.yIni = shiftY;
            // on initialize la ligne précédente
            double[] lignePred = new double[ncols];
            result = ligne.split("\\s");
            String s = result[0];
            int shift = 0;
            if (s.isEmpty()) {
                shift = 1;
            }
            // On remplit la premiere ligne : initialisation
            for (int i = shift; i < ncols + shift; i++) {
                lignePred[i - shift] = Double.parseDouble(result[i * this.echantillonage]);
                if (lignePred[i - shift] == noDataValue) {
                    continue;
                }
                this.zMin = Math.min(lignePred[i - shift], this.zMin);
                this.zMax = Math.max(lignePred[i - shift], this.zMax);
            }
            tabAlt[0] = lignePred;
            // On passe des lignes pour sous échantillonner
            for (int i = 0; i < this.echantillonage; i++) {
                ligne = br.readLine();
            }
            // On traite le fichier ligne par ligne
            for (int j = 1; j < nrows; j++) {
                if (ligne == null) {
                    break;
                }
                numcol = 0;
                // On charge la ligne en mémoire
                result = ligne.split("\\s");
                // Pour chaque colonne que l'on souhaite récupèrer
                for (int i = shift; i < ncols + shift; i++) {
                    // System.out.println("row : " + j + " col : " + i );
                    // On récupère le Z du point que l'on traite actuellement
                    // Le Z du point en dessous
                    double nouvZ = Double.parseDouble(result[i * this.echantillonage]);
                    double oldZ = lignePred[i - shift];
                    if (nouvZ != noDataValue) {
                        this.zMin = Math.min(nouvZ, this.zMin);
                        this.zMax = Math.max(nouvZ, this.zMax);
                    }
                    lignePred[i - shift] = nouvZ;
                    numcol = numcol + this.echantillonage;
                }
                tabAlt[j] = lignePred;
                numligne = numligne + this.echantillonage;
                // On passe des lignes pour sous àchantillonner
                for (int l = 0; l < this.echantillonage; l++) {
                    ligne = br.readLine();
                }
            }
            br.close();
            this.stepX = this.echantillonage * stepX;
            this.stepY = this.echantillonage * stepY;
            this.nX = ncols;
            this.nY = nrows;
            this.sampling = this.echantillonage;
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
