import java.awt.*;

import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.util.HashMap;
import java.util.HashSet;

public class PruebasSwing extends JPanel implements ActionListener, KeyListener {

	int marX;
	int marY;
	float x;
	float y;
	int mar = 50;
	float zoom = 1;
	Graphics2D g1;

	static HashMap<Integer, Parada> paradas;

	static JButton reset;

	private static Parada paradaOrigen;
	private static Parada paradaDestino;

	static JLabel origen;
	static JLabel destino;

	static JLabel tiempo;
	static JLabel distancia;

	static JButton limpiar;

	int nMarcados = 0;

	public PruebasSwing() {
		paradaOrigen = null;
		paradaDestino = null;
		paradas = CalcularPuntosMapa.CalcularPoints(1);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g1 = (Graphics2D) g;
		g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);





		int width = getWidth();
		int height = getHeight();
		marX = (int) width / 100;
		marY = (int) height / 100;

		// Se escriben las paradas paradaOrigen y paradaDestino

		g1.setStroke(new BasicStroke(3f));
		g1.setPaint(Color.BLACK);

		origen.setBounds(width - 6 * mar, mar, 250, 20);
		destino.setBounds(width - 6 * mar, 2 * mar, 250, 20);

		tiempo.setBounds(width - 6 * mar, 4 * mar, 250, 20);
		distancia.setBounds(width - 6 * mar, 5 * mar, 250, 20);

		// Se actualiza las posiciones de los botones

		reset.setBounds(width - 6 * mar, height - mar, 5 * mar, 25);

		limpiar.setBounds(width - 6 * mar, (int) (3 * mar), 5 * mar, 25);

		// Se pintan las paradas y las lineas que los unen, empezando por la parada con
		// id = 0

		HashSet<Parada> set = new HashSet<Parada>();
		Parada paradaTemp = CalcularPuntosMapa.paradas.get(0);
		pintarPuntos(width, height, set, paradaTemp);

	}

	private void pintarPuntos(int width, int height, HashSet<Parada> set, Parada parada) {

		// Se añade la parada al set de paradas visitadas y se determinan las
		// coordenadas en las que se va a pintar

		set.add(parada);
		Float X = 5 * marX + Math.min(height - 10 * marY, width - 5 * marX) * (parada.x + x) * zoom;
		Float Y = 5 * marY + Math.min(height - 10 * marY, width - 5 * marX) * (1 - parada.y + y) * zoom;

		// Se dan las propiedades al boton, es posible que haya cosas de mas en esta
		// parte

		JCheckBox btn = parada.btn;
		btn.setBounds(X.intValue() - 2, Y.intValue() - 2, 15, 15);

		Border border = new LineBorder(parada.color, 1);
		btn.setBorder(border);

		// Para cada conexion de cada parada se pintan las lineas desde esa parada hasta
		// el paradaDestino de la conexion

		for (Conexion conexion : parada.conexiones) {

			Parada paradaAux = conexion.destino;
			Float Xaux = 5 * marX + Math.min(height - 10 * marY, width - 5 * marX) * (paradaAux.x + x) * zoom;
			Float Yaux = 5 * marY + Math.min(height - 10 * marY, width - 5 * marX) * (1 - paradaAux.y + y) * zoom;

			// Si es un trasbordo se pinta en negro, si no en el color correspondiente

			if (!parada.color.equals(paradaAux.color)) {
				g1.setPaint(Color.BLACK);
				g1.setStroke(new BasicStroke(7.5f));
				Line2D linea = new Line2D.Float(Xaux.intValue() + 5, Yaux.intValue() + 5, X.intValue() + 5,
						Y.intValue() + 5);
				g1.draw(linea);
				g1.setStroke(new BasicStroke(3f));

			} else {
				g1.setPaint(parada.color);
			}

			Line2D linea = new Line2D.Float(Xaux.intValue() + 5, Yaux.intValue() + 5, X.intValue() + 5,
					Y.intValue() + 5);
			g1.draw(linea);

			// Si la parada paradaDestino no esta entre las visitadas se pinta

			if (!set.contains(paradaAux))
				pintarPuntos(width, height, set, paradaAux);

		}
	}

	public static void main(String[] args) {

		// Inicializacion de los botones generales, zoom, move...
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		reset = new JButton();

		reset.setText("Reset");

		reset.setMultiClickThreshhold(100);

		limpiar = new JButton();

		limpiar.setText("Borrar selección");

		PruebasSwing PS = new PruebasSwing();


		reset.addActionListener(PS);



		origen = new JLabel();
		destino = new JLabel();
		tiempo= new JLabel();
		distancia=new JLabel();


		origen.setFont(origen.getFont().deriveFont(origen.getFont().getStyle(),15));
		destino.setFont(origen.getFont().deriveFont(origen.getFont().getStyle(),15));

		tiempo.setFont(origen.getFont().deriveFont(origen.getFont().getStyle(),15));
		distancia.setFont(origen.getFont().deriveFont(origen.getFont().getStyle(),15));
		reset.setFont(origen.getFont().deriveFont(origen.getFont().getStyle(),15));
		limpiar.setFont(origen.getFont().deriveFont(origen.getFont().getStyle(),15));	

		origen.setText("Origen: ");
		destino.setText("Destino: ");

		tiempo.setText("Tiempo total: ");
		distancia.setText("Distancia total: ");

		limpiar.addActionListener(PS);
		frame.setFocusable(true);
		frame.addKeyListener(PS);

		frame.add(PS);
		PS.add(reset);
		PS.add(limpiar);
		PS.add(origen);
		PS.add(destino);

		PS.add(tiempo);
		PS.add(distancia);

		frame.setSize(1000, 1000);
		frame.setLocation(50, 50);
		frame.setVisible(true);

		for (int i = 0; i < paradas.size(); i++) {
			Parada temp = paradas.get(i);
			JCheckBox btn = temp.btn;

			btn.addActionListener(PS);
			btn.setToolTipText(temp.nombre);
			btn.setBackground(temp.color);

			PS.add(btn);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Acciones de los botones
		// Restablece los valores por defecto del mapa
		getTopLevelAncestor().requestFocus();
		if (e.getSource() == reset) {
			x = 0;
			y = 0;
			zoom = 1;
		}
		if (e.getSource() == limpiar) {
			if (paradaOrigen != null) {
				paradaOrigen.btn.setEnabled(true);
				paradaOrigen.btn.setSelected(false);
				paradaOrigen = null;
				origen.setText("Origen: ");
			}
			if (paradaDestino != null) {
				paradaDestino.btn.setEnabled(true);
				paradaDestino.btn.setSelected(false);
				paradaDestino = null;
				destino.setText("Destino: ");
			}
			tiempo.setText("Tiempo total: ");
			distancia.setText("Distancia total: ");

			nMarcados = 0;
			habilitarCheckBoxes();
		}

		for (int i = 0; i < paradas.size(); i++) {

			if (e.getSource() == paradas.get(i).btn) {
				nMarcados++;
				paradas.get(i).btn.setEnabled(false);
				if (nMarcados == 2) {
					paradaDestino = paradas.get(i);
					destino.setText("Destino: " + paradaDestino.nombre);
					deshabilitarCheckBoxes();
					Trio<Double, Double, Parada> res = A_estrella.calcular(paradaOrigen, paradaDestino);

					System.out.println("\tCoste total: " + res.getLeft());

					tiempo.setText("Tiempo total: "+(int) (res.getLeft()*60) +" min. y "+ (int)((res.getLeft()*60-(int)(res.getLeft()*60))*60)+" seg.");
					distancia.setText("Distancia total: "+String.format("%.3f", res.getCenter())+" Km");
					// System.out.println("\tDistancia total: " + res.getCenter());
					Parada meta = res.getRight();
					while (meta != null) {
						meta.btn.setBackground(meta.color);
						System.out.println(meta.nombre);
						meta = meta.parent;
					}
				} else {
					
					paradaOrigen = paradas.get(i);
					origen.setText("Origen: " + paradaOrigen.nombre);
				}
			}

		}
		this.repaint();
	}

	private void deshabilitarCheckBoxes() {

		for (int i = 0; i < paradas.size(); i++) {
			Parada temp = paradas.get(i);
			temp.btn.setEnabled(false);
			if (temp != paradaOrigen && temp != paradaDestino) {
				temp.btn.setBackground(new Color(238, 238, 238));

			}
		}

	}

	private void habilitarCheckBoxes() {
		for (int i = 0; i < paradas.size(); i++) {
			Parada temp = paradas.get(i);
			temp.btn.setEnabled(true);
			if (temp != paradaOrigen && temp != paradaDestino) {
				temp.btn.setBackground(temp.color);

			}
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_UP) {
			// mueve la "camara" hacia arriba, en realidad lo que hace es disminuir
			// la y del mapa
			if (y < .2) {
				y += .01;
			}
			this.repaint();

		} else if (code == KeyEvent.VK_DOWN) {
			// moveUp mueve la "camara" hacia abajo, en realidad lo que hace es aumentar la
			// y del mapa
			if (y > -.7) {
				y -= .01;
			}
			this.repaint();

		} else if (code == KeyEvent.VK_RIGHT) {
			// moveUp mueve la "camara" hacia la derecha, en realidad lo que hace es
			// disminuye la
			// x del mapa
			if (x > -.7) {
				x -= .01;
			}
			this.repaint();

		} else if (code == KeyEvent.VK_LEFT) {
			// moveUp mueve la "camara" hacia la izquierda, en realidad lo que hace es
			// aumenta la
			// x del mapa
			if (x < .2) {
				x += .01;
			}

			this.repaint();

		} else if (code == KeyEvent.VK_PLUS) {
			// aumenta el zoom hasta que llega a un limite
			if (zoom < 3.5f) {
				zoom += 0.25f;
			}
			this.repaint();

		} else if (code == KeyEvent.VK_MINUS) {
			// disminuye el zoom hasta que llega a un limite
			if (zoom > 0.75f) {
				zoom -= 0.25f;
			}
			this.repaint();

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

}