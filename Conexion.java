public class Conexion {
	
	Parada destino;		// destino de la conexion
	double distancia;	// distancia a recorrer, no aerea, en metros o kilometros, por determinar 
	double velocidad;	// velocidad de tren o de una persona al andar, dependiendo de que conexion sea, pueden plantearse 
						// como variables de CalcularPuntosMapa, depende de como lo hagamos
						
	double tdeEspera;	// tiempo de espera medio hasta que llega un tren a esa conexion, 0 en el caso de trasbordo, no se si es necesario implementarlo
	boolean trasbordo;	// indica si es un trasbordo 
	
	public Conexion() {
		distancia=0;
		velocidad=0;
		tdeEspera=0;
		trasbordo=false;
	}

}
