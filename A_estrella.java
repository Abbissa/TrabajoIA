import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class A_estrella {

	public static Pair<Double,Parada> calcular(Parada origen,Parada meta){
		ParadaComparator comp = new ParadaComparator();
		ArrayList<Parada> listaAbierta = new ArrayList<>();
		HashMap<String,Parada> listaCerrada = new HashMap<>();
		Pair<Double,Parada> res = null;
		Parada nodoAct = null;
		boolean terminado = false;
		listaAbierta.add(origen);
		while(!listaAbierta.isEmpty()&&!terminado){
			nodoAct = listaAbierta.remove(listaAbierta.size()-1);
			ArrayList<Conexion> sucesores = getSucesores(nodoAct);
			Iterator<Conexion> it = sucesores.iterator();
			while(it.hasNext()&&!terminado){
				Conexion conex = it.next();
				Parada p = conex.destino;
				if(p.equals(meta)){
					Parada aux = new Parada(p);
					aux.g = nodoAct.g + conex.distancia;
					aux.h = 0;
					aux.parent = new Parada(nodoAct);
					res = new Pair<Double,Parada>(aux.g,aux);
					terminado = true;
				}else{
					int idx = findInArray(listaAbierta, p);
					if(idx==-1){
						Parada paradaEnListaCerrada = listaCerrada.get(p.nombre);
						if(paradaEnListaCerrada!=null){
							//Comprobamos si hay que redirigir punteros en los descendientes
							for(Conexion conexSuc : paradaEnListaCerrada.conexiones){
								double nuevoG = nodoAct.g + conexSuc.distancia;
								double f = nuevoG + paradaEnListaCerrada.h;
								if(f<paradaEnListaCerrada.f()){
									paradaEnListaCerrada.g = nuevoG;
									paradaEnListaCerrada.parent = new Parada(nodoAct);
								}
							}
						}else{
							Parada pCpy = new Parada(p);
							pCpy.g = nodoAct.g + conex.distancia;
							pCpy.h = haversine(p.y, p.x, meta.y, meta.x);
							pCpy.parent = new Parada(nodoAct);
							listaAbierta.add(pCpy);
						}


					}else{
						//Comprobamos si hay que actualizar puntero
						Parada paradaCoincidente = listaAbierta.get(idx);
						double nuevoG = nodoAct.g + conex.distancia;
						double f = nuevoG + paradaCoincidente.h;
						if(paradaCoincidente.f()<f){
							paradaCoincidente.g = nuevoG;
							paradaCoincidente.parent = new Parada(nodoAct);
						}


					}
					listaAbierta.sort(comp);
				}
			}
			//Añadir nodoAct a la lista cerrada, creando una copia.
			listaCerrada.put(nodoAct.nombre, new Parada(nodoAct)); 
		}
		return res;
	}


	private static int findInArray(ArrayList<Parada> listaAbierta, Parada p) {
		int res = -1;
		if(listaAbierta!=null&&!listaAbierta.isEmpty()){
			int lowIndex = 0;
			int highIndex = listaAbierta.size()-1;
			boolean terminado = false;
			
			while (lowIndex <= highIndex&&!terminado) {
				int midIndex = (lowIndex + highIndex) / 2;
				Parada parada = listaAbierta.get(midIndex);
				if (parada.equals(p)) {
					res = midIndex;
					terminado = true;
				} else if (p.f() > parada.f()) {
					highIndex = midIndex-1;
				} else if (p.f() < parada.f()) {
					lowIndex = midIndex+1;
				}
			}
		}
		return res;
	}


	private static ArrayList<Conexion> getSucesores(Parada p){
		ArrayList<Conexion> res = new ArrayList<>();
		ArrayList<Conexion> sucesores = p.conexiones;
		for(Conexion conex : sucesores){
			Parada sucesor = conex.destino;
			if(!estaEmparentado(sucesor, p)){
				res.add(conex);
			}
		}
		return res;
	}

	private static boolean estaEmparentado(Parada sucesor, Parada padre){
		boolean res = false;
		Parada p = padre;
		while(p!=null&&!res){
			if(p.equals(sucesor)){
				res = true;
			}
			p = p.parent;
		}
		return res;
	}


	static double haversine(double lat1, double lon1,
			double lat2, double lon2)
	{
		// distance between latitudes and longitudes
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);

		// convert to radians
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		// apply formulae
		double a = Math.pow(Math.sin(dLat / 2), 2) +
				Math.pow(Math.sin(dLon / 2), 2) *
				Math.cos(lat1) *
				Math.cos(lat2);
		double rad = 6371;
		double c = 2 * Math.asin(Math.sqrt(a));
		return rad * c;
	}

}
