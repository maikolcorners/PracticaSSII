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
	public String toString(){
		String a="(" + idO.getId();
		for(int i=0;i<idD.size();i++){
			if(i==0){
				a=a+"[";
			}
			if(i<idD.size()-1){
				a=a+idD.get(i).getId()+",";
			}else{
				a=a+idD.get(i).getId()+"]";
			}				
		}
		a=a+")";
		return a;
	}
	
	
}
