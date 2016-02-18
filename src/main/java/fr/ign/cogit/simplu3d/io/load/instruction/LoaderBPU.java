package fr.ign.cogit.simplu3d.io.load.instruction;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import fr.ign.cogit.simplu3d.model.application.Environnement;

public class LoaderBPU {

	public final static String NOM_MNT = "MNT_BD3D.asc";

	public Environnement getEnvironnement(String folder, Integer searchIdBPU) throws Exception {
		return LoaderBPU.load(folder, searchIdBPU);
	}

	public static Environnement load(String folder, Integer searchIdBPU) throws Exception {
		return load(folder, new FileInputStream(folder + File.separator + NOM_MNT), searchIdBPU);
	}

	public static Environnement load(String folder, InputStream dtmStream, Integer searchIdBPU) throws Exception {

		Environnement env = Environnement.getInstance();
		env.folder = folder;

		return LoadFromCollectionBPU.load(env, searchIdBPU);

	}

}
