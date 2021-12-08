import javax.swing.JCheckBox;
import java.awt.Color;
import java.util.ArrayList;

//	Podria ser mejor que el ActionListener de los botones de las Paradas sean las propias paradas
// aunque puede llevar a problemas ya que la parada es la que tiene que comunicar a el main quien es

public  class Parada{
	String nombre;
	Float x;
	Float y;
	JCheckBox btn;
	int id;	
	ArrayList<Conexion> conexiones;
	Color color;
	Parada parent;
	double g;
	double h;

	public Parada(String nombre, Float x, Float y, Color color) {

		this.conexiones=new ArrayList<>(2);
		this.color=color;
		btn = new JCheckBox();
		this.nombre=nombre;
		this.x=x;
		this.y=y;
		this.parent = null;
		this.g = 0;
		this.h = 0;
	}

	public Parada(Parada p) {
		this.conexiones=p.conexiones;
		this.color=p.color;
		btn = p.btn;
		this.nombre=p.nombre;
		this.x=p.x;
		this.y=p.y;
		this.parent = p.parent;
		this.g = p.g;
		this.h = p.h;
	}

	//Calcular g(x) + h(x) 
	public double f(){
		return this.g + this.h;
	}


	@Override
	public boolean equals(Object otro){
		if(otro==null){
			return false;
		}
		if(!(otro instanceof Parada)){
			return false;
		}
		Parada p = (Parada)otro;
		return this.nombre.equals(p.nombre);
	}

	
}
