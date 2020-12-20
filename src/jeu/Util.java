package jeu;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

public class Util {
	//méthode de lecture du fichier des probabilités de faire un certain resultat avec n dés
	public static double[][] proba(File file) {
		double[][] res = new double[49][9];
		try {
			Scanner read = new Scanner(file);
			int i = 0;
			while (read.hasNext()) {
				String info = read.nextLine();
				info.split(";");
				String[] temp = info.split(";");
				if (i == 0) {
					temp[0] = "0";
				}
				for (int j = 0; j < 9; j++) {
					res[i][j] = Double.parseDouble(temp[j]);
				}
				i++;
			}
			for (int j = 1; j < 9; j++) {
				for (i = 1; i < 49; i++) {
					res[i][j] = res[i][j] / Math.pow(6, j);
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}
