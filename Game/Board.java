package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int GRID_SIZE_X = 10;
    private final int GRID_SIZE_Y = 24;
    private final int GRID_SQUARE_SIZE = 30;
    private final int WIDTH = GRID_SIZE_X * GRID_SQUARE_SIZE;
    private final int HEIGHT = GRID_SIZE_Y * GRID_SQUARE_SIZE;

    private final int DELAY = 10;
    private final int DELAY_BUFFER = 6;

    private boolean[][] board;
    private Color[][] colorBoard;

    private Timer timer;
    private int buffer;

    private Tetrominoe currentPiece;

    public Board() {
        setKeys();
    }

    private void setKeys() {
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "RotateRight");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, false), "RotateLeft");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "MoveRight");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "MoveRight");
    }

    public boolean[][] getBoard() {
        return board;
    }

    public int getGridX() {
        return GRID_SIZE_X;
    }

    public int getGridY() {
        return GRID_SIZE_Y;
    }

    private void update() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private void paintBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < colorBoard.length; i++) {
            for (int j = 0; j < colorBoard[i].length; j++) {
                if (colorBoard[i][j].equals(null)) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(i * GRID_SQUARE_SIZE, j * GRID_SQUARE_SIZE, GRID_SQUARE_SIZE, GRID_SQUARE_SIZE);
                } else {
                    g2d.setColor(colorBoard[i][j]);
                    g2d.fillRect(i * GRID_SQUARE_SIZE, j * GRID_SQUARE_SIZE, GRID_SQUARE_SIZE, GRID_SQUARE_SIZE);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            if (buffer == DELAY_BUFFER) {
                update();
                buffer = 0;
            } else {
                buffer++;
            }
        }
    }
}
