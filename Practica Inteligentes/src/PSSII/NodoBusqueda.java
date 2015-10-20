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
	public float getValor() {
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
