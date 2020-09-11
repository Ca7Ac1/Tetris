package Game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int GRID_SIZE_X = 10;
    private final int GRID_SIZE_Y = 24;
    private final int WIDTH = GRID_SIZE_X * 30;
    private final int HEIGHT = GRID_SIZE_Y * 30;

    private boolean[][] board;
    private Color[][] colorBoard;

    private Timer timer;

    private Tetrominoe currentPiece;

    public Board() {

    }

    private void setKeys() {

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
    public void actionPerformed(ActionEvent e) {
        update();
    }
}
