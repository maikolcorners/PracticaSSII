package PSSII;



public class NodoBusqueda implements Comparable<NodoBusqueda>{
	NodoBusqueda nodoBusq;
	private Estado estado;
	private double costo;
	private String accion;
	private int profundidad;
	private float valor;
	public NodoBusqueda(NodoBusqueda nodoBusq, Estado estado, double costo, String accion, int profundidad) {
		this.nodoBusq = nodoBusq;
		this.estado = estado;
		this.costo = costo;
		this.accion = accion;
		this.profundidad = profundidad;		
	}
	public NodoBusqueda getNodoBusq() {
		return nodoBusq;
	}
	public void setNodoBusq(NodoBusqueda nodoBusq) {
		this.nodoBusq = nodoBusq;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public double getCosto() {
		return costo;
	}
	public void setCosto(double costo) {
		this.costo = costo;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public int getProfundidad() {
		return profundidad;
	}
	public void setProfundidad(int profundidad) {
		this.profundidad = profundidad;
	}
	public float getValorE(int estrategia, NodoBusqueda aux, Estado estado) {			
		switch (estrategia) {		
        case 1:  
        	valor=aux.getProfundidad();
            break;
        case 2:  
        	valor=-aux.getProfundidad();
            break;
        case 3:
        	valor=-aux.getProfundidad();        	
        	break;
        case 4:  
        	valor=(float) aux.getCosto();
            break;               
        case 5:   		 
    		valor=heuristica(estado);
            break;
        case 6:     		    		
    		valor=(float) (heuristica(estado)+aux.getCosto());
            break;
        }		
		return estrategia;
	}
	public Float heuristica(Estado estado){
		float aux1=0;
		float valor=0;
		if(estado.getIdD().isEmpty()){
			aux1=0;
		}else{
			for(int i=0;i<estado.getIdD().size();i++){    	
	    		valor=(float) estado.getIdO().calcularDistanciaNodoAdy(estado.getIdD().get(i));
	    		if(aux1<=valor){
					aux1=valor;
				}	
			}
		}
		return aux1;
	}
	public float getValor(){
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	public int compareTo(NodoBusqueda o) {
		int v;
		if(valor<o.getValor())
			v= -1;
		else if (valor> o.getValor())
			v = 1;
		else 
			v=0;		
		return v;
	}
}
	
