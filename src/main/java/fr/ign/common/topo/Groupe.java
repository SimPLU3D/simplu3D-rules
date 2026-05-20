package fr.ign.common.topo;

import java.util.Collection;
import java.util.List;

public class Groupe {
    Collection<Arc> edges;
    public void setListeArcs(List<Arc> listeArcs) {
        this.edges = listeArcs;
    }
    Collection<Face> faces;
    public void setListeFaces(List<Face> listeFaces) {
        this.faces = listeFaces;
    }
    Collection<Noeud> nodes;
    public void setListeNoeuds(Collection<Noeud> listeNoeuds) {
        this.nodes = listeNoeuds;
    }
    public Collection<Arc> getListeArcs() {
        return this.edges;
    }
}
