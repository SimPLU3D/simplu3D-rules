package fr.ign.common.topology;

import fr.ign.common.geometry.Box3D;
import fr.ign.common.util.GeomUtils;
import fr.ign.common.equation.PlanEquation;
import fr.ign.common.util.Vecteur;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.ign.common.topology.Edge.NUMERIC_TOLERANCE;

/**
 * Class of triangle used in the geometric-topologic model.
 */
public class Triangle {
    org.locationtech.jts.geom.Triangle triangle;
    Vertex[] lVertices = new Vertex[3];
    /**
     * crée un triangle et affecte les sommes si ils sont au nombre de 3 sinon
     * renvoie un objet vide
     *
     */
    public Triangle(Vertex[] listeSommets) {
        this.triangle = new org.locationtech.jts.geom.Triangle(listeSommets[0].coordinate, listeSommets[1].coordinate, listeSommets[2].coordinate);
        if (listeSommets.length == 3) {
            this.lVertices = listeSommets;
        }
    }
    public Coordinate getCoordinate(int i) {
        return this.lVertices[i].coordinate;
    }
    public Coordinate[] getCoordinates() {
        return Arrays.stream(this.lVertices).map(v->v.coordinate).toList().toArray(new Coordinate[0]);
    }

    /**
     * créer un triangle à partir de 3 sommets
     *
     */
    public Triangle(Vertex sommet1, Vertex sommet2, Vertex sommet3) {
        this.triangle = new org.locationtech.jts.geom.Triangle(sommet1.coordinate, sommet2.coordinate, sommet3.coordinate);
        this.lVertices[0] = sommet1;
        this.lVertices[1] = sommet2;
        this.lVertices[2] = sommet3;
    }
    public final double TOLERANCE = 0.001;
    private PlanEquation eq = null;
    private Box3D box = null;
    /**
     * @return plan d'équation d'un triangle
     */
    public PlanEquation getPlanEquation() {
        if (this.eq == null) {
            this.eq = new PlanEquation(
                    new Coordinate[]{
                            this.lVertices[0].coordinate,
                            this.lVertices[1].coordinate,
                            this.lVertices[2].coordinate
                    }
            );
        }
        return this.eq;
    }

    public Coordinate getCenter(){
        double x = (this.lVertices[0].coordinate.getX() + this.lVertices[1].coordinate.getX() + this.lVertices[2].coordinate.getX() )/3;
        double y = (this.lVertices[0].coordinate.getY() + this.lVertices[1].coordinate.getY() + this.lVertices[2].coordinate.getY() )/3;
        double z = (this.lVertices[0].coordinate.getZ() + this.lVertices[1].coordinate.getZ() + this.lVertices[2].coordinate.getZ() )/3;
        return new Coordinate(x,y,z);
    }

    /**
     * @return le tableau des sommets
     */
    public Vertex[] getLVertices() {
        return this.lVertices;
    }

    /**
     * @param sommets le nouveau tableau de sommets
     */
    public void setLSommets(Vertex[] sommets) {
        this.lVertices = sommets;
    }

    /**
     * Translate un triangle suivant un vecteur
     *
     * @param v vecteur dont on translate le triangle
     * @return un nouveau triangle translaté de v
     */
    public Triangle translateTriangle(Vecteur v) {
        return this.translateTriangle(v.getX(), v.getY(), v.getZ());
    }

    /**
     * Translate un triangle suivant un vecteur x,y,z
     *
     * @return un nouveau triangle translaté de x,y,z
     */
    public Triangle translateTriangle(double x, double y, double z) {

        Vertex s1 = new Vertex(
                this.lVertices[0].coordinate.getX() + x,
                this.lVertices[0].coordinate.getY() + y,
                this.lVertices[0].coordinate.getZ() + z);
        Vertex s2 = new Vertex(
                this.lVertices[1].coordinate.getX() + x,
                this.lVertices[1].coordinate.getY() + y,
                this.lVertices[1].coordinate.getZ() + z);
        Vertex s3 = new Vertex(
                this.lVertices[2].coordinate.getX() + x,
                this.lVertices[2].coordinate.getY() + y,
                this.lVertices[2].coordinate.getZ() + z);
        return new Triangle(s1, s2, s3);
    }

    /**
     * Translate un triangle suivant un sommet
     *
     * @param dp le vecteur dont on translate le triangle
     * @return un triangle translaté suivant le vecteur dp
     */
    public Triangle translateTriangle(Coordinate dp) {
        return this.translateTriangle(dp.getX(), dp.getY(), dp.getZ());
    }

    ArrayList<Edge> edgeContenus = null;

    /**
     * Permet de calculer les différentes arrètes autour d'un triangle Les arrètes
     * sont orientés et la géométries àtant 1 Manifold à la base Chaque arrète est
     * unique (compte tenue de son orientation)
     *
     * @return une liste d'arrètes bordant le triangle
     */
    public List<Edge> calculEdge() {
        if (this.edgeContenus == null) {
            this.edgeContenus = new ArrayList<>();
            this.edgeContenus.add(new Edge(this.lVertices[0], this.lVertices[1]));
            this.edgeContenus.add(new Edge(this.lVertices[1], this.lVertices[2]));
            this.edgeContenus.add(new Edge(this.lVertices[2], this.lVertices[0]));
        }
        return this.edgeContenus;
    }

    /**
     * Calcule une somme de minkoswki entre un triangle et un vecteur
     *
     * @param edge le vecteur sur lequel on calcul la somme de Minkowski
     * @return les triangles issus de la somme
     */
    public ArrayList<Triangle> minkoVecteur(Edge edge) {

        ArrayList<Triangle> lTrianglesResultats = new ArrayList<>();
        Coordinate centre = this.Barycentre();
        double normeEdge = edge.getVector().norme();
        double cos = 0;
        int indice = 0;
        for (int i = 0; i < 3; i++) {
            Vecteur vTemp = new Vecteur(this.lVertices[i].coordinate, centre);
            double cosTemp = vTemp.prodScalaire(edge.getVector()) / (normeEdge * vTemp.norme());
            // Cosinus plus petit on continue
            if (Math.abs(cosTemp) < Math.abs(cos)) {
                continue;
            }
            cos = cosTemp;
            indice = i;
        }
        // Cos ne devrait pas pouvoir être nul
        Vecteur dp1;
        Vecteur dp2;
        // Sinon ca signifie la prèsence d'un triangle plat
        if (cos < 0) {
            dp1 = new Vecteur(edge.getVertIni().coordinate);
            dp2 = new Vecteur(edge.getVertFin().coordinate);
        } else {
            dp1 = new Vecteur(edge.getVertFin().coordinate);
            dp2 = new Vecteur(edge.getVertIni().coordinate);
        }

        // Cas ou la pointe est dans le sens opposé au vecteur
        lTrianglesResultats.add(this.translateTriangle(dp1));
        Vertex somIni1, SomIni2;
        if (indice == 0) {
            somIni1 = this.lVertices[1];
        } else {
            somIni1 = this.lVertices[0];
        }
        if (indice != 2) {
            SomIni2 = this.lVertices[2];
        } else {
            SomIni2 = this.lVertices[1];
        }
        Vertex som1, som2, som3, som4;
        som1 = new Vertex((dp1.translate(somIni1.coordinate)));
        som2 = new Vertex((dp1.translate(SomIni2.coordinate)));
        som3 = new Vertex((dp2.translate(somIni1.coordinate)));
        som4 = new Vertex((dp2.translate(SomIni2.coordinate)));
        lTrianglesResultats.add(new Triangle(som1, som2, som3));
        lTrianglesResultats.add(new Triangle(som2, som4, som3));
        return lTrianglesResultats;
    }

    /**
     * @return le barycentre du triangle
     */
    public Coordinate Barycentre() {
        double x = this.lVertices[0].coordinate.getX() + this.lVertices[1].coordinate.getX()
                + this.lVertices[2].coordinate.getX();
        double y = this.lVertices[0].coordinate.getY() + this.lVertices[1].coordinate.getY()
                + this.lVertices[2].coordinate.getY();
        double z = this.lVertices[0].coordinate.getZ() + this.lVertices[1].coordinate.getZ()
                + this.lVertices[2].coordinate.getZ();
        return new Coordinate(x / 3, y / 3, z / 3);
    }

    /**
     * Vérifie si une arrête est contenu dans un triangle mais, ne vérifie que
     * l'égalité des objets
     *
     * @param e une arrête dont on veut savoir si elle est contenu dans le
     *          triangle
     * @return indique si l'arrête est contenu dans un triangle
     */
    public boolean containEdge(Edge e) {
        return this.edgeContenus.contains(e);
    }

    /**
     * Vérifie si une arrête est contenu dans un triangle mais, vérifie sommet par
     * sommet
     *
     * @param e une arrête dont on veut savoir si elle est contenu dans le
     *          triangle
     * @return indique si l'arrête est contenu dans un triangle
     */
    public boolean contientEdge(Edge e) {
        List<Edge> lEdge = this.calculEdge();
        for (int i = 0; i < 3; i++) {
            Edge eTemp = lEdge.get(i);
            if (e.getVertFin().equals(eTemp.getVertFin())
                    && e.getVertIni().equals(eTemp.getVertIni())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fonction permettant de trouver le point d'intersection entre une droite et
     * une triangle
     *
     * @param e l'arrête permettant de définir la droite
     * @return un sommet comme intersection d'un droite et d'un triangle. Renvoie
     *         null si il n'y a pas d'intersection
     */
    public Vertex intersection(Edge e) {
        // l.start + lambda *l.dir = point on plane
        double D = this.getPlanEquation().getCoeffd();
        Vecteur normal = this.getPlanEquation().getNormale();
        Vertex somIni = e.getVertIni();
        double lambda = (D - normal.prodScalaire(new Vecteur(somIni.coordinate)))
                / e.prodScalaire(normal);
        // Is point on plane is between start and endpoint of line?
        // start + lambda * length = Point on top of triangle
        // --> lambda > 1 end point of line before intersection point
        // --> lambda < 0 intersection before start point
        if (lambda >= 0 - this.TOLERANCE && lambda <= 1 + this.TOLERANCE) {
            Vertex result = new Vertex(
                    somIni.coordinate.getX() + lambda * e.getVector().getX(),
                    somIni.coordinate.getY() + lambda * e.getVector().getY(),
                    somIni.coordinate.getZ() + lambda * e.getVector().getZ()
            );
            if (!this.liesInside(result.coordinate)) {
                return null;
            }
            return result;
        }

        // Has no intersection Point
        return null;
    }

    /**
     * Vérifie si un point se trouve dans un triangle (en 3D)
     *
     * @param p le point que l'on test
     * @return indique si il se trouve dans le triangle
     */
    public boolean liesInside(Coordinate p) {
        Vecteur vec1 = new Vecteur(p, this.lVertices[1].coordinate);
        Vecteur vec2 = new Vecteur(p, this.lVertices[2].coordinate);
        Vecteur vec3 = new Vecteur(p, this.lVertices[3].coordinate);
        Vecteur pdVec1 = vec1.prodVectoriel(vec2);
        Vecteur pdVec2 = vec2.prodVectoriel(vec3);
        Vecteur pdVec3 = vec3.prodVectoriel(vec1);
        double prodS1 = pdVec1.prodScalaire(pdVec2);
        double prodS2 = pdVec2.prodScalaire(pdVec3);
        double prodS3 = pdVec3.prodScalaire(pdVec1);
        return (prodS1 * prodS2 * prodS3 > NUMERIC_TOLERANCE);
    }

    /**
     * Calcul l'intersection entre 2 triangles TODO : finish this funtion
     *
     * @param t a triangle
     */
    @Deprecated
    public void cut(Triangle t) {

        if (!this.getBox3D().intersect(t.getBox3D())) {
            return;
        }

        Coordinate[] ips = new Coordinate[2];
        int index = 0;

        Coordinate singleIP = null;

        for (int i = 0; i < 3; i++) {
            Coordinate ip = t.intersection(this.edgeContenus.get(i)).coordinate;

            if (ip != null) {
                if (singleIP == null || !GeomUtils.equals(singleIP, ip, this.TOLERANCE)) {
                    singleIP = ip;
                    ips[index++] = ip;
                    if (index > 1) {
                        break;
                    }
                }
            }

        }

        // lines of t gets intersected with this plane
        // Check for intersection points inside, if necessary
        if (index < 2) {
            for (int i = 0; i < 3; i++) {
                Coordinate ip = this.intersection(this.edgeContenus.get(i)).coordinate;
                if (ip != null) {
                    if (singleIP == null || !GeomUtils.equals(singleIP, ip, this.TOLERANCE)) {
                        ips[index++] = ip;
                        if (index > 1) {
                            break;
                        }
                    }
                }
            }
        }
        if (index == 2) {
            // cutline.add(new UVLine(calculateUVCoord(ips[0]),
            // calculateUVCoord(ips[1])));
        }
    }

    /**
     * Translate les sommets contenu dans e et appartenant au triangle d'un
     * vecteur v
     *
     * @param e l'arrête dont on teste les somemet
     * @param v le vecteur dont on effectue une translation
     * @return un nouveau triangle dont les sommets contenu dans e ont été
     *         translatés de v
     */
    public Triangle translateEdge(Edge e, Vecteur v) {
        Vertex[] lSom = new Vertex[3];
        for (int i = 0; i < 3; i++) {
            if (GeomUtils.equals(e.getVertIni().coordinate, this.lVertices[i].coordinate, 0.01)) {
                lSom[i] = new Vertex(v.translate(this.lVertices[i].coordinate));
                continue;
            }
            if (GeomUtils.equals(e.getVertFin().coordinate, this.lVertices[i].coordinate, 0.01)) {
                lSom[i] = new Vertex(v.translate(this.lVertices[i].coordinate));
                continue;
            }
            lSom[i] = this.lVertices[i];
        }
        return new Triangle(lSom);
    }

    /**
     * Translate les sommets contenu dans s et appartenant au triangle d'un
     * vecteur v
     *
     * @param s le vecteur dont on teste les somemet
     * @param v le vecteur dont on effectue une translation
     * @return un nouveau triangle dont les sommets contenu dans s ont été
     *         translatés de v
     */
    public Triangle translateSommet(Vertex s, Vecteur v) {
        Vertex[] lSom = this.lVertices.clone();
        for (int i = 0; i < 3; i++) {
            if (lSom[i].equals(s)) {
                lSom[i] = new Vertex(v.translate(lSom[i].coordinate));
                break;
            }
        }
        return new Triangle(lSom);
    }

    /**
     * Vérifie l'égalité entre 2 triangles en testant sommet par sommet
     *
     * @param o le triangle dont on teste l'égalité
     * @return indique l'égalité des triangles
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Triangle t)){
            return false;
        }
        Coordinate[] som = t.getCoordinates();
        bouclei: for (int i = 0; i < 3; i++) {
            Vertex som1 = this.lVertices[i];
            for (int j = 0; j < 3; j++) {
                Coordinate som2 = som[i];
                if (som2 == null) {
                    continue;
                }
                if (GeomUtils.equals(som1.coordinate, som2, 0.01)) {
                    // On le vire pour àviter de trouver 3 fois la même
                    // coordonnées (triangle mal formé)
                    continue bouclei;
                }
            }
            // Si on trouve un point correspondant, on ne sort jamais de la
            // boucle j
            return false;
        }
        return true;
    }

    /**
     * Calcul dune boite 3D du triangle
     *
     * @return une boite englobant le triangle
     */
    public Box3D getBox3D() {
        if(box != null)
        {
            Vertex[] som = this.getLVertices();
            double xmin = Double.POSITIVE_INFINITY;
            double ymin = Double.POSITIVE_INFINITY;
            double zmin = Double.POSITIVE_INFINITY;
            double xmax = Double.NEGATIVE_INFINITY;
            double ymax = Double.NEGATIVE_INFINITY;
            double zmax = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < 3; i++) {
                Coordinate dp = som[i].coordinate;
                xmin = Math.min(xmin, dp.getX());
                ymin = Math.min(ymin, dp.getY());
                zmin = Math.min(zmin, dp.getZ());
                xmax = Math.max(xmax, dp.getX());
                ymax = Math.max(ymax, dp.getY());
                zmax = Math.max(zmax, dp.getZ());
            }
            this.box =  new Box3D(
                    new Coordinate(xmin, ymin, zmin),
                    new Coordinate(xmax, ymax, zmax));
        }
        return box;
    }
    public boolean intersects(Triangle t) {
        for (int i = 0; i < 3; i++) {
            Coordinate dp = t.getPlanEquation().intersectionLinePlan(
                    this.getCoordinate(i), this.getCoordinate(i + 1));
            if (t.liesInside(dp)) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            Coordinate dp = this.getPlanEquation().intersectionLinePlan(
                    t.getCoordinate(i), t.getCoordinate(i + 1));
            if (this.liesInside(dp)) {
                return true;
            }
        }
        return false;
    }
    public Vecteur getNormal() {
        return this.getPlanEquation().getNormale();
    }
    public void reversePoints() {
        Vertex vTemp = this.lVertices[0];
        this.lVertices[0] = this.lVertices[2];
        this.lVertices[2] = vTemp;
        this.edgeContenus = null;
    }
    List<Triangle> neighBourTriangles = null;
    public List<Triangle> getNeighBourTriangles() {
        if (neighBourTriangles == null) {
            neighBourTriangles = new ArrayList<>();
            for (Edge e : this.calculEdge()) {
                List<Triangle> lTriTemp = e.getNeighbourTriangles();
                for (Triangle triTemp : lTriTemp) {
                    if (!triTemp.equals(this)) {
                        if (!neighBourTriangles.contains(triTemp)) {
                            neighBourTriangles.add(triTemp);
                        }
                    }
                }
            }
        }
        return neighBourTriangles;
    }

    public double area() {
        return this.triangle.area();
    }
    public Polygon getPolygon() {
        return new GeometryFactory().createPolygon(
                new Coordinate[]{
                        this.lVertices[0].coordinate,
                        this.lVertices[1].coordinate,
                        this.lVertices[2].coordinate,
                        this.lVertices[0].coordinate
                }
        );
    }

    public boolean isEmpty() {
        return this.getPolygon().isEmpty();
    }
}
