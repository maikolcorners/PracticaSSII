package PSSII;

import java.util.LinkedList;

public class Estado {
	private Nodo idO;
	private LinkedList <Nodo>idD=new LinkedList<Nodo>();
	public Estado(Nodo idO, LinkedList <Nodo>idD) {		
		this.idO = idO;
		this.idD=idD;	
	}
	public Nodo getIdO() {
		return idO;
	}
	public void setIdO(Nodo idO) {
		this.idO = idO;
	}
	public LinkedList<Nodo> getIdD() {
		return idD;
	}
	public void setIdD(LinkedList<Nodo> idD) {
		this.idD = idD;
	}
	
}
