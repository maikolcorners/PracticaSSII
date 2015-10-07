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

	
}
