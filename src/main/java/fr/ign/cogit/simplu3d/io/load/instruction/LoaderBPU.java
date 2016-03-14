package fr.ign.cogit.simplu3d.io.load.instruction;

import fr.ign.cogit.simplu3d.model.application.Environnement;

public class LoaderBPU {

  public final static String NOM_MNT = "MNT_BD3D.asc";

  public Environnement getEnvironnement(Integer IdVersion, Integer searchIdBPU)
      throws Exception {
    return LoaderBPU.load(IdVersion, searchIdBPU);
  }

  public static Environnement load(Integer IdVersion, Integer searchIdBPU)
      throws Exception {

    Environnement env = Environnement.getInstance();

    return LoadFromCollectionBPU.load(env, IdVersion, searchIdBPU);

  }

}
