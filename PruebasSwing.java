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

	int compensacionX = 0;
	int compensacionY = 0;

	static HashMap<Integer, Parada> paradas;

	static JButton zoomIn;
	static JButton zoomOut;

	static JButton moveUp;
	static JButton moveDown;
	static JButton moveRight;
	static JButton moveLeft;
	static JButton reset;

	private static Parada origen;
	private static Parada destino;

	static JButton limpiar;

	int nMarcados = 0;

	public PruebasSwing() {
		origen = null;
		destino = null;
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

		// Se escriben las paradas origen y destino

		g1.setStroke(new BasicStroke(3f));
		g1.setPaint(Color.BLACK);
		g1.drawString("Origen: ", width - 6 * mar,  mar);
		if (origen != null)
			g1.drawString(origen.nombre, width - 4 * mar, mar);
		g1.drawString("Destino: ",  width - 6 * mar, 2 * mar);
		if (destino != null)
			g1.drawString(destino.nombre, width - 4 * mar, 2 * mar);

		// Se actualiza las posiciones de los botones

		zoomIn.setBounds(width - 3 * mar, height - mar, 50, 25);
		zoomOut.setBounds(width - 2 * mar, height - mar, 50, 25);

		moveRight.setBounds(width - 5 * mar, height - mar, 50, 25);
		moveLeft.setBounds(width - 7 * mar, height - mar, 50, 25);
		moveUp.setBounds(width - 6 * mar, height - mar - mar / 2, 50, 25);
		moveDown.setBounds(width - 6 * mar, height - mar, 50, 25);

		reset.setBounds(width - 9 * mar, height - mar, 75, 25);

		limpiar.setBounds(width - 6 * mar, (int) (2.5*mar), 5 * mar, 25);

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
		// el destino de la conexion

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

			// Si la parada destino no esta entre las visitadas se pinta

			if (!set.contains(paradaAux))
				pintarPuntos(width, height, set, paradaAux);

		}
	}

	public static void main(String[] args) {

		// Inicializacion de los botones generales, zoom, move...
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		zoomIn = new JButton();
		zoomOut = new JButton();
		zoomOut.setText("-");
		zoomIn.setText("+");
		zoomOut.setMultiClickThreshhold(100);
		zoomIn.setMultiClickThreshhold(100);

		moveUp = new JButton();
		moveDown = new JButton();
		moveRight = new JButton();
		moveLeft = new JButton();
		reset = new JButton();


		moveUp.setText("â†‘");
		moveLeft.setText("â†�");
		moveRight.setText("â†’");
		moveDown.setText("â†“");
		reset.setText("Reset");
		moveUp.setMultiClickThreshhold(100);
		moveDown.setMultiClickThreshhold(100);
		moveLeft.setMultiClickThreshhold(100);
		moveRight.setMultiClickThreshhold(100);
		reset.setMultiClickThreshhold(100);

		limpiar=new JButton();

		limpiar.setText("Borrar selección");

		PruebasSwing PS = new PruebasSwing();
		// Prueba entre Lisova-Pecherska
		// Pair<Double, Parada> res = A_estrella.calcular(paradas.get(0), paradas.get(42));

		// System.out.println("\tCoste total: " + res.getLeft());
		// Parada meta = res.getRight();
		// while (meta != null) {
		// 	System.out.println(meta.nombre);
		// 	meta = meta.parent;
		// }
		reset.addActionListener(PS);
		moveUp.addActionListener(PS);
		moveDown.addActionListener(PS);
		moveRight.addActionListener(PS);
		moveLeft.addActionListener(PS);

		zoomOut.addActionListener(PS);
		zoomIn.addActionListener(PS);

		limpiar.addActionListener(PS);
		

		frame.add(PS);
		PS.add(zoomIn);
		PS.add(zoomOut);
		PS.add(moveUp);
		PS.add(moveDown);
		PS.add(moveRight);
		PS.add(moveLeft);
		PS.add(reset);
		PS.add(limpiar);
		
		frame.setSize(1000, 1000);
		frame.setLocation(200, 200);
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

		// zoomOut disminuye el zoom hasta que llega a un limite
		if (e.getSource() == zoomOut) {

			zoom -= 0.25f;
			zoomIn.setEnabled(true);

			if (zoom == 0.75f) {
				zoomOut.setEnabled(false);
			}
		}
		// zoomIn aumenta el zoom hasta que llega a un limite
		if (e.getSource() == zoomIn) {

			zoom += 0.25f;
			zoomOut.setEnabled(true);

			if (zoom == 3.5f) {
				zoomIn.setEnabled(false);
			}

		}
		// moveUp mueve la "camara" hacia arriba, en realidad lo que hace es disminuir
		// la y del mapa
		if (e.getSource() == moveUp) {

			y += .01;
			moveDown.setEnabled(true);
			if (y >= .2) {
				moveUp.setEnabled(false);
			}

		}
		// moveUp mueve la "camara" hacia abajo, en realidad lo que hace es aumentar la
		// y del mapa
		if (e.getSource() == moveDown) {

			y -= .01;
			moveUp.setEnabled(true);
			if (y <= -.7) {
				moveDown.setEnabled(false);
			}

		}
		// moveRight mueve la "camara" hacia derecha, en realidad lo que hace es
		// disminuir la x del mapa
		if (e.getSource() == moveRight) {

			x -= .01;
			moveLeft.setEnabled(true);

			if (x <= -.7) {
				moveRight.setEnabled(false);
			}

		}
		// moveLeft mueve la "camara" hacia izquierda, en realidad lo que hace es
		// aumentar la x del mapa
		if (e.getSource() == moveLeft) {
			x += .01;
			moveRight.setEnabled(true);

			if (x >= .2) {
				moveLeft.setEnabled(false);
			}

		}
		// Restablece los valores por defecto del mapa
		if (e.getSource() == reset) {
			x = 0;
			y = 0;
			zoom = 1;
			moveRight.setEnabled(true);
			moveLeft.setEnabled(true);
			moveUp.setEnabled(true);
			moveDown.setEnabled(true);
			zoomIn.setEnabled(true);
			zoomOut.setEnabled(true);
		}
		if(e.getSource()==limpiar){
			origen.btn.setEnabled(true);
			origen.btn.setSelected(false);
			destino.btn.setEnabled(true);
			destino.btn.setSelected(false);
			origen=null;
			destino=null;
			nMarcados=0;
			habilitarCheckBoxes();
		}



		for (int i = 0; i < paradas.size(); i++) {

			if (e.getSource() == paradas.get(i).btn) {
				nMarcados++;
				paradas.get(i).btn.setEnabled(false);
				if (nMarcados == 2) {
					destino = paradas.get(i);
					deshabilitarCheckBoxes();
					Pair<Double, Parada> res = A_estrella.calcular(origen, destino);

					System.out.println("\tCoste total: " + res.getLeft());
					Parada meta = res.getRight();
					while (meta != null) {
						meta.btn.setBackground(meta.color);
						System.out.println(meta.nombre);
						meta = meta.parent;
					}
				} else {
					origen = paradas.get(i);
				}
			}

		}
		this.repaint();
	}

	private void deshabilitarCheckBoxes() {

		for (int i = 0; i < paradas.size(); i++) {
			Parada temp = paradas.get(i);
			temp.btn.setEnabled(false);
			if (temp != origen && temp != destino) {
				temp.btn.setBackground(new Color(238, 238, 238));

			}
		}

	}
	private void habilitarCheckBoxes(){
		for (int i = 0; i < paradas.size(); i++) {
			Parada temp = paradas.get(i);
			temp.btn.setEnabled(true);
			if (temp != origen && temp != destino) {
				temp.btn.setBackground(temp.color);

			}
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
