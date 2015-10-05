package PSSII;

import java.util.Hashtable;

public class CrearGrafo {
	private Hashtable<Long, Nodo> TablaNodos;

	public CrearGrafo() {
		TablaNodos = new Hashtable<Long, Nodo>();
	}

	public Hashtable<Long, Nodo> getTablaNodos() {
		return TablaNodos;
	}

	public Nodo getNodo(long id) {
		return TablaNodos.get(id);
	}

	public double calcularDistancia(double lon1, double lat1, double lon2, double lat2) {
		int R = 6371;

		double latDistance = toRad(lat2 - lat1);
		double lonDistance = toRad(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;

		return distance;

	}

	private static Double toRad(double value) {
		return value * Math.PI / 180;
	}
}
