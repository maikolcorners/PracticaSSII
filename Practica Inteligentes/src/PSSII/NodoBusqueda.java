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
        	valor=(float) aux.getCosto();
            break;
        case 4:  
        	/*if(aux.getProfundidad()<=profundidadMax){
        		valor=-aux.getProfundidad();
        	}*/
        	break;
        /*case 5:          	
    		double lat, lon;
    		for(int i=0;i<estado.getIdD().size();i++){
    			lat=estado.getIdD().get(i).getLatitud();
    			lon=estado.getIdD().get(i).getLongitud();
    			valor=(float) aux.getEstado().getIdO().calcularDistanciaNodoAdy(lat,lon);
    		}    		
            break;
        case 6:          	
    		double lat1, lon1;
    		for(int i=0;i<estado.getIdD().size();i++){
    			lat1=estado.getIdD().get(i).getLatitud();
    			lon1=estado.getIdD().get(i).getLongitud();
    			valor=(float) aux.getEstado().getIdO().calcularDistanciaNodoAdy(lat1,lon1);
    			valor=(float) (valor+aux.getCosto());
    		}    		
            break;*/
        }		
		return estrategia;
	}
	public float getValor(){
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	@Override
	public int compareTo(NodoBusqueda nodo){
		int v = -1;
		if (valor > nodo.getValor())
			v = 1;
		return v;
	}
}
