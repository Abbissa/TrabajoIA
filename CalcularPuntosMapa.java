import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class CalcularPuntosMapa {
	private static Color[] colores = { new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255) };
	private static String[][] nombres = {
			{ "Lisova", "Chernihivska", "Darnytsia", "Livoberezhna", "Hidropark", "Dnipro", "Arsenaina", "Khreshchatyk",
					"Teatralna",
					"Universytet", "Vokzalna", "Politekhnichnyi_instytut", "Shuliavska", "Beresteiska", "Nyvky",
					"Sviatoshyn", "Zhytomyrska", "Akademmistechko" },
			{ "Heroiv_Dnipra",
					"Minska", "Obolon", "Petrivka", "Tarasa_Shevchenka", "Kontraktova_ploshcha", "Poshtova_ploshcha",
					"Maidan_Nezalezhnosti", "Ploshcha_Lva_Tolstoho",
					"Olimpiiska", "Palats_Ukraina", "Lybidska", "Demiivska", "Holosiilvska", "Vasylkivska",
					"Vystavkovyi_tsentr", "Ipodrom", "Teremky" },
			{ "Syrets", "Dorohozhychi",
					"Lukianivska", "Zoloti_vorota", "Palats_sportu", "Klovska", "Pecherska", "Druzhby_narodiv",
					"Vydubychi", "Slavutych", "Osokorky", "Pozniaky", "Kharkivska",
					"Vyrlytsia", "Boryspilska", "Cherovnyi_khutir" } };

	private static int[] origen = { 7, 8, 26 };
	private static int[] destino = { 25, 39, 40 };
	private static double[] distanciaT = { 0.12, 0.1, 0.1 };

	private static double[][] distancias = {
			{ 1.32, 1.12, 1.63, 1.36, 1.1, 1.5, 0.58902, 0.80376, 1.3, 1.9, 1.5, 2.22, 1.02, 0.98366, 1.79, 1.53 },
			{ 1.2, 1.2, 1.76, 1.36, 1.2, 1.2, 1.36, 1.05, 0.9, 1.14, 0.9, 1.22, 1.05, 1.49, 1.5, 1, 1.5 },
			{ 1.56, 2.67, 3.11, 0.71, 0.973, 1.2, 1.28, 1.93, 3.38, 0.823, 1.34, 1.27, 1.09, 1.26, 1.09 } };

	private static double[][][] coords = {{{50.46469285361899 ,30.645690751718917},
{50.459970753371834, 30.62992780208801},
{50.45602338316065, 30.6130034727788},
{50.45190463125389 ,30.598220413367624},
{50.446022039333116 ,30.576987356782972},
{50.44116114470389 ,30.559289564377764},
{50.44286708002273, 30.547132518257197},
{50.44732846630205, 30.522715400201474},
{50.444704172369114, 30.51560661899538},
{50.443332322463554, 30.504554754787925},
{50.44129833560563, 30.49013114074657},
{50.45110844763096, 30.464297302655268},
{50.45435917631197,30.44841394527877},
{50.45940619390695, 30.41876703975154},
{50.45881589117922, 30.404137374081024},
{50.45789762784854, 30.390434940741738},
{50.456290543474665, 30.365174901868585},
{50.46501362176046, 30.355387449483377}
},{
{50.52283682829911, 30.49888914107685},
{50.51222249442236, 30.498607937095613},
{50.501652541051946, 30.49810979530699},
{50.48604072215515, 30.497827336892556},
{50.47398578840243, 30.503752139871246},
{50.46596339560829, 30.515110931698402},
{50.45929250583085, 30.524408375663125},
{50.44810476663474, 30.52508460907009},
{50.440211870579255, 30.518407901332573},
{50.43128833572436, 30.51683963742671},
{50.42141864327411, 30.520677180439186},
{50.41400415837839, 30.524915617156857},
{50.40481855216166, 30.516906599303695},
{50.397372676471015, 30.50825405475751},
{50.39382191922691, 30.48984365182861},
{50.38242819510429, 30.47760218344564},
{50.37658419884681, 30.468846395733593},
{50.36711504614719, 30.454132289170378},
},{
{50.47680910655473, 30.432891838982627},
{50.47370268288503, 30.449114506610265},
{50.4614127346531, 30.483613987968017},
{50.44589403022826, 30.515250582263242},
{50.43958367744201, 30.519586573730432},
{50.437018839689095, 30.532040474061137},
{50.42708591739942, 30.539383518210105},
{50.417101197093515, 30.54723117094871},
{50.40194133910983, 30.560290335481913},
{50.394630060303534, 30.604894435158435},
{50.39536813773621, 30.616007603442725},
{50.39804571756886, 30.63476933205523},
{50.400838151781535, 30.652292371213864},
{50.40310890066547, 30.666948004030672},
{50.403431494949984, 30.68445729130873},
{50.40948171574571, 30.696161688710923},
}};

	public static HashMap<Integer, Parada> paradas;

	public static HashMap<Integer, Parada> CalcularPoints(int scale) {

		String path = ".\\Input";
		if (File.separator.equals("/"))
			path = "./Input";
		try (Scanner sc = new Scanner(new File(path))) {

			paradas = new HashMap<Integer, Parada>(52);

			leerSinScan();
			//LeerDef(sc);
			//System.out.println("Fin de lecura de paradas");
			//definirTrasbordos(sc);
			// mostrarConexiones();

			definirPuntos(scale);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return paradas;
	}

	private static void leerSinScan() {

		
		double velocidadMetro = 36.11;
		int k=0;
		for (int i = 0; i < colores.length; i++) {

			Parada prev = null;
			Conexion conexionPre = null;
			Conexion conexionPost = null;
			double distancia = 0;
			for (int j = 0; j < nombres[i].length; j++) {
				Parada parada = new Parada(nombres[i][j], coords[i][j][1], coords[i][j][0], colores[i]);
				if (prev != null) {
					conexionPost = new Conexion();
					conexionPost.destino = prev;
					conexionPost.distancia = distancia;
					conexionPost.velocidad = velocidadMetro;
					parada.conexiones.add(conexionPost);
					conexionPre.destino = parada;
					prev.conexiones.add(conexionPre);

				}
				paradas.put(k, parada);
				prev = parada;
				conexionPre = new Conexion();
				if (j < distancias.length-1) {
					distancia = distancias[i][j];
					conexionPre.distancia = distancia;
					conexionPre.velocidad = velocidadMetro;
				}
				k++;

			}

		}

		int id1 = 0;
		int id2 = 0;
		double distanciaTrasbordo;
		double velocidadHumano = 5.0;
		Parada parada1;
		Parada parada2;
		Conexion conexion1;
		for(int i=0;i<origen.length;i++){
			id1 = origen[i];
			id2 = destino[i];
			distanciaTrasbordo = distanciaT[i];
			System.out.println(id1);
			System.out.println(id2);
			parada1 = paradas.get(id1);
			parada2 = paradas.get(id2);
			conexion1 = new Conexion();
			conexion1.distancia = distanciaTrasbordo;
			conexion1.velocidad = velocidadHumano;
			conexion1.destino = parada2;
			parada1.conexiones.add(conexion1);
			conexion1 = new Conexion();
			conexion1.distancia = distanciaTrasbordo;
			conexion1.velocidad = velocidadHumano;
			conexion1.destino = parada1;
			parada2.conexiones.add(conexion1);
		}
	}

	private static void LeerDef(Scanner sc) {
		boolean fin = false;
		int i = 0;
		double velocidadMetro = 36.11;

		while (!fin && sc.hasNextInt()) {

			int[] colores = { sc.nextInt(), sc.nextInt(), sc.nextInt() };

			Color color = new Color(colores[0], colores[1], colores[2]);
			Parada prev = null;
			Conexion conexionPre = null;
			Conexion conexionPost = null;
			String nombre;
			double x;
			double y;
			double distancia = 0;

			while (!fin && sc.hasNext("[a-zA-Z]*[\\p{Punct}*[a-zA-Z]*]*")) {
				nombre = sc.next();

				if (nombre.equals("fin")) {
					fin = true;
				} else {
					y = sc.nextDouble();
					x = sc.nextDouble();

					Parada parada = new Parada(nombre, x, y, color);
					if (prev != null) {
						conexionPost = new Conexion();
						conexionPost.destino = prev;
						conexionPost.distancia = distancia;
						conexionPost.velocidad = velocidadMetro;
						parada.conexiones.add(conexionPost);
						conexionPre.destino = parada;
						prev.conexiones.add(conexionPre);

					}
					paradas.put(i, parada);
					prev = parada;
					conexionPre = new Conexion();
					if (sc.hasNextDouble()) {
						distancia = sc.nextDouble();
						conexionPre.distancia = distancia;
						conexionPre.velocidad = velocidadMetro;
					} else
						sc.next();
					i++;
				}
			}
		}

	}

	private static void definirTrasbordos(Scanner sc) {
		int id1 = 0;
		int id2 = 0;
		double distanciaTrasbordo;
		double velocidadHumano = 5.0;
		Parada parada1;
		Parada parada2;
		Conexion conexion1;
		while (sc.hasNext()) {
			id1 = sc.nextInt();
			id2 = sc.nextInt();
			distanciaTrasbordo = sc.nextDouble();
			parada1 = paradas.get(id1);
			parada2 = paradas.get(id2);
			conexion1 = new Conexion();
			conexion1.distancia = distanciaTrasbordo;
			conexion1.velocidad = velocidadHumano;
			conexion1.destino = parada2;
			parada1.conexiones.add(conexion1);
			conexion1 = new Conexion();
			conexion1.distancia = distanciaTrasbordo;
			conexion1.velocidad = velocidadHumano;
			conexion1.destino = parada1;
			parada2.conexiones.add(conexion1);
		}

	}

	private static void definirPuntos(int scale) {

		Double maxX = null;
		Double maxY = null;
		Double minX = null;
		Double minY = null;

		for (int i = 0; i < paradas.size(); i++) {
			Parada parada = paradas.get(i);

			if (maxX == null || maxX < parada.x) {
				maxX = parada.x;
			}
			if (minX == null || minX > parada.x) {
				minX = parada.x;
			}

			if (maxY == null || maxY < parada.y) {
				maxY = parada.y;
			}
			if (minY == null || minY > parada.y) {
				minY = parada.y;
			}

		}
		for (int i = 0; i < paradas.size(); i++) {
			Parada parada = paradas.get(i);

			parada.x = (scale * (parada.x - minX) / (maxX - minX));
			parada.y = (scale * (parada.y - minY) / (maxY - minY));

		}

	}

	private static void mostrarConexiones() {
		Parada parada;
		for (int i = 0; i < paradas.size(); i++) {
			parada = paradas.get(i);
			System.out.println(i + " " + parada.nombre + ": ");
			for (Conexion conexion : parada.conexiones) {

				System.out.println(conexion.destino.nombre);

			}
			System.out.println();
		}
	}

}
