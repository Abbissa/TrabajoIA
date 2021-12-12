import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


public class CalcularPuntosMapa {

	
	public static HashMap<Integer, Parada> paradas;

	public static HashMap<Integer, Parada> CalcularPoints(int scale) {

		String path= ".\\Input";
		if(File.separator.equals("/"))
			path="./Input";
		try(Scanner sc= new Scanner(new File(path))){
			
			paradas = new HashMap<Integer,Parada>(52);
			LeerDef(sc);
			System.out.println("Fin de lecura de paradas");
			definirTrasbordos(sc);
			//mostrarConexiones();

			definirPuntos(scale);




		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return paradas;
	}

	private static void LeerDef(Scanner sc) {
		boolean fin=false;
		int i=0;
		double velocidadMetro = 36.11;

		while(!fin&&sc.hasNextInt()){
			int[]colores= {sc.nextInt(),sc.nextInt(),sc.nextInt()};

			Color color= new Color(colores[0],colores[1],colores[2]);
			Parada prev=null;
			Conexion conexionPre=null;
			Conexion conexionPost=null;
			String nombre;
			double x;
			double y;
			double distancia=0;
			while (!fin&&sc.hasNext("[a-zA-Z]*[\\p{Punct}*[a-zA-Z]*]*")){
				nombre=sc.next();


				if(nombre.equals("fin")) {
					fin=true;
				}else {
					y=sc.nextDouble();
					x=sc.nextDouble();

					Parada parada=new Parada(nombre, x, y,color);
					if(prev!=null) {
						conexionPost=new Conexion();
						conexionPost.destino=prev;
						conexionPost.distancia=distancia;
						conexionPost.velocidad=velocidadMetro;
						parada.conexiones.add(conexionPost);
						conexionPre.destino=parada;
						prev.conexiones.add(conexionPre);

					}
					paradas.put(i, parada);
					prev=parada;
					conexionPre=new Conexion();
					if(sc.hasNextDouble()){
						distancia=sc.nextDouble();
						conexionPre.distancia=distancia;
						conexionPre.velocidad=velocidadMetro;
					}else
						sc.next();
					i++;
				}
			}
		}

	}

	private static void definirTrasbordos(Scanner sc) {
		int id1=0;
		int id2=0;
		double distanciaTrasbordo;
		double velocidadHumano =5.0;
		Parada parada1;
		Parada parada2;
		Conexion conexion1;
		while(sc.hasNext()){
			id1=sc.nextInt();
			id2=sc.nextInt();
			distanciaTrasbordo = sc.nextDouble();
			parada1=paradas.get(id1);
			parada2=paradas.get(id2);
			conexion1=new Conexion();
			conexion1.distancia=distanciaTrasbordo;
			conexion1.velocidad=velocidadHumano;
			conexion1.destino=parada2;
			parada1.conexiones.add(conexion1);
			conexion1=new Conexion();
			conexion1.distancia=distanciaTrasbordo;
			conexion1.velocidad=velocidadHumano;
			conexion1.destino=parada1;
			parada2.conexiones.add(conexion1);
		}

	}

	private static void definirPuntos(int scale) {

		Double maxX=null;
		Double  maxY=null;
		Double  minX=null;
		Double minY=null;

		for (int i = 0; i < paradas.size(); i++) {
			Parada parada=paradas.get(i);


			if(maxX==null||maxX<parada.x) {
				maxX=parada.x;
			}
			if(minX==null||minX>parada.x) {
				minX=parada.x;
			}

			if(maxY==null||maxY<parada.y) {
				maxY=parada.y;
			}
			if(minY==null||minY>parada.y){
				minY=parada.y;
			}

		}
		for (int i = 0; i < paradas.size(); i++) {
			Parada parada=paradas.get(i);

			parada.x=(scale*(parada.x-minX)/(maxX-minX));
			parada.y=(scale*(parada.y-minY)/(maxY-minY));

		}

	}

	private static void mostrarConexiones() {
		Parada parada;
		for (int i = 0; i < paradas.size(); i++) {
			parada=paradas.get(i);
			System.out.println(i+" "+parada.nombre+": ");
			for (Conexion conexion : parada.conexiones) {

				System.out.println(conexion.destino.nombre);

			}
			System.out.println();
		}
	}


}




