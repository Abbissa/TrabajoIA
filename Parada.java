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
	public Parada(String nombre, Float x, Float y, Color color) {

		this.conexiones=new ArrayList<Conexion>(2);
		this.color=color;
		btn = new JCheckBox();
		this.nombre=nombre;
		this.x=x;
		this.y=y;
	}
}