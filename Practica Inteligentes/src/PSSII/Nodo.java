package PSSII;

import java.util.LinkedList;

public class Nodo {
	private long idNodo;
	private double lat;
	private double lon;
	LinkedList<NodoAdyacente> nodosAdyacentes;

	public Nodo(long idNodo,double lat,double lon) {
		this.idNodo = idNodo;
		this.lat=lat;
		this.lon=lon;
		nodosAdyacentes = new LinkedList<NodoAdyacente>();
	}

	public long getId() {
		return idNodo;
	}

	public double getLongitud() {
		return lon;
	}

	public double getLatitud() {
		return lat;
	}

	public LinkedList<NodoAdyacente> getNodosAdyacentes() {
		return nodosAdyacentes;
	}

	public void setNodosAdyacentes(LinkedList<NodoAdyacente> nodosAdyacentes) {
		this.nodosAdyacentes = nodosAdyacentes;
	}
	private double merc_x(double lon){
		double r_major = 6378137.000;
		return r_major*Math.toRadians(lon);
	}
	
	
	private  double merc_y(double lat){
		  if (lat > 89.5) lat = 89.5;
		  if (lat < -89.5) lat = -89.5;
		  double r_major = 6378137.000;
		  double r_minor = 6356752.3142;
		  double temp = r_minor/r_major;
		  double eccent = Math.sqrt(1-Math.pow(temp, 2));
		  double phi = Math.toRadians(lat);
		  double sinphi = Math.sin(phi);
		  double con = eccent*sinphi;
		  double com = eccent/2;
		  con = Math.pow(((1.0-con)/(1.0+con)), com);
		  double ts = Math.tan((Math.PI/2-phi)/2)/con;
		  double y = 0-r_major*Math.log(ts);
		  return y;
	}
	public double calcularDistanciaNodoAdy(Nodo nodo){
		double x1 = merc_x(getLongitud());
		double x2 = merc_x(nodo.getLongitud());
		double y1 = merc_y(getLatitud());
		double y2 = merc_y(nodo.getLatitud());
		return Math.sqrt(Math.pow((x1-x2), 2)+Math.pow((y1-y2), 2));
	}	
}
