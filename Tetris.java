// importation des classes
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel {

	private static final long serialVersionUID = -8715353373678321308L;
	

    // création des pièces
	private final Point[][][] Tetraminos = {
			// I
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},
			
			// J
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
			},
			
			// L
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
			},
			
			// O
			{
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
			},
			
			// S
			{
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
			},
			
			// T
			{
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
			},
			
			// Z
			{
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
			}
	};
	// les différentes couleurs
	private final Color[] tetraminoColors = {
		Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red
	};
	
	private Point pieceOrigin; // point d'apparition de la pièce
	private int currentPiece;
	private int rotation;
	private ArrayList<Integer> nextPieces = new ArrayList<Integer>(); // pièce suivante

	private long score; // score
	private Color[][] well; // plateau de jeu avec les cases qui sont des couleurs
	
	private static boolean gameOver;
	
	// création de la bordure et initialisation des pièces qui tombent
	private void init() {
		well = new Color[12][24];
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				if (i == 0 || i == 11 || j == 22) {
					well[i][j] = Color.GRAY;
				} else {
					well[i][j] = Color.BLACK;
				}
			}
		}
		newPiece(); // création d'une pièce
	}
	
	// envoie d'une nouvelle pièce au hasard
	public void newPiece() {
		pieceOrigin = new Point(5, 2); // point d'apparition du centre de la pièce
		rotation = 0;
		if (nextPieces.isEmpty()) {
			Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nextPieces);
		}
		currentPiece = nextPieces.get(0);
		nextPieces.remove(0);
	}
	

	public void checkGameOver() {
		if (collidesAt(pieceOrigin.x, pieceOrigin.y, rotation)) {
			// La pièce est arrêtée en haut, le jeu est terminé
			System.out.println("Game Over");
			gameOver = true;
			// Arrêtez ici toute logique de jeu supplémentaire, comme l'arrêt du thread qui fait tomber la pièce
		}
	}


	// vérification si la pièce tombante touche une autre pièce
	private boolean collidesAt(int x, int y, int rotation) {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			if (well[p.x + x][p.y + y] != Color.BLACK) {
				return true;
			}
		}
		return false;
	}
	
	// rotation de la pièce dans le sens des aiguilles d'une montre ou dans le sens inverse des aiguilles d'une montre
	public void rotate(int i) {
		int newRotation = (rotation + i) % 4;
		if (newRotation < 0) {
			newRotation = 3;
		}
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
			rotation = newRotation;
		}
		repaint();
	}
	
	// déplacement de la pièce vers la gauche ou vers la droite
	public void move(int i) {
		if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
			pieceOrigin.x += i;	
		}
		checkGameOver();
		repaint();
	}
	
	// laisse tomber la pièce ou la fixe si elle ne peut pas descendre plus
	public void dropDown() {
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
			pieceOrigin.y += 1;
		} else {
			fixToWell();
			checkGameOver();
		}	
		repaint();
	}
	
	// fait en sorte que la pièce fasse partie du système afin qu'elle soit détecter pour la collision
	public void fixToWell() {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[currentPiece];
		}
		clearRows();
		newPiece();
	}
	
	public void deleteRow(int row) {
		for (int j = row-1; j > 0; j--) {
			for (int i = 1; i < 11; i++) {
				well[i][j+1] = well[i][j];
			}
		}
	}
	
	// effacement des rangées complétées et attribution du score en fonction du nombre de lignes supprimées
	public void clearRows() {
		boolean gap;
		int numClears = 0;
		
		for (int j = 21; j > 0; j--) {
			gap = false;
			for (int i = 1; i < 11; i++) {
				if (well[i][j] == Color.BLACK) {
					gap = true;
					break;
				}
			}
			if (!gap) {
				deleteRow(j);
				j += 1;
				numClears += 1;
			}
		}
		
		// calcul du score
		switch (numClears) {
		case 1:
			score += 100; // ajout de 100 points pour 1 ligne supprimée
			break;
		case 2:
			score += 300; // ajout de 300 points pour 2 lignes supprimées
			break;
		case 3:
			score += 500; // ajout de 500 points pour 3 lignes supprimées
			break;
		case 4:
			score += 800; // ajout de 800 points pour 4 lignes supprimées
			break;
		}
	}
	
	// dessine la pièce qui tombe
	private void drawPiece(Graphics g) {		
		g.setColor(tetraminoColors[currentPiece]);
		for (Point p : Tetraminos[currentPiece][rotation]) {
			g.fillRect((p.x + pieceOrigin.x) * 26, 
					   (p.y + pieceOrigin.y) * 26, 
					   25, 25);
		}
	}
	
	@Override 
	public void paintComponent(Graphics g)
	{
		// dessine le composant
		g.fillRect(0, 0, 26*12, 26*23);
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 23; j++) {
				g.setColor(well[i][j]);
				g.fillRect(26*i, 26*j, 25, 25);
			}
		}


		// affiche le score
		g.setColor(Color.WHITE);
		g.drawString("" + score, 19*12, 25);
		
		// dessine la pièce qui tombe actuellement
		drawPiece(g);		
		// Afficher la prochaine pièce
		drawNextPiece(g);
	}

	private void drawNextPiece(Graphics g) {	
		int blockSize = 25;
		int startX = 19 * blockSize;
		int startY = 110;
	
		int nextPiece = nextPieces.get(0);

	
		g.setColor(tetraminoColors[nextPiece]);
		for (Point p : Tetraminos[nextPiece][0]) {
			int x = 25 + p.x * blockSize;
			int y = p.y * blockSize;
			g.fillRect(x, y, blockSize, blockSize);
		}
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("Tetris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(12*26+10, 26*23+25);
		f.setVisible(true);
		
		final Tetris game = new Tetris();
		game.init();
		f.add(game);
		
		// Keyboard controls
		f.addKeyListener(new KeyListener() {
			

			public void keyTyped(KeyEvent e) {
			}
			
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					game.rotate(-1); // rotation après un appuie sur la flèche du haut
					break;
				case KeyEvent.VK_DOWN:
					game.dropDown(); // accélération de la pièce après maintient la flèche du bas
					break;
				case KeyEvent.VK_LEFT:
					game.move(-1); // déplacement d'une case vers la gauche après appuie sur la flèche de gauche
					break;
				case KeyEvent.VK_RIGHT:
					game.move(+1); // déplacement d'une case vers la droite après appuie sur la flèche de droite
					break;					
				} 
				
			}
			
			public void keyReleased(KeyEvent e) {
			}
		});
		
		// fait tomber la pièce à chaque seconde
		new Thread() {
			@Override public void run() {
				while (true) {
					try {					
						Thread.sleep(1000);

						if(!gameOver){
							game.dropDown();
						}
					} catch ( InterruptedException e ) {}
				}
			}
		}.start();
	}
}