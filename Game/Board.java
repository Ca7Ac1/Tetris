package Game;

import Game.Pieces.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int GRID_SIZE_X = 10;
    private final int GRID_SIZE_Y = 24;
    private final int GRID_SQUARE_SIZE = 30;
    private final int WIDTH = GRID_SIZE_X * GRID_SQUARE_SIZE;
    private final int HEIGHT = GRID_SIZE_Y * GRID_SQUARE_SIZE;

    private final int DELAY = 1000000;
    private final int DELAY_BUFFER = 6;

    private boolean[][] board;
    private Color[][] colorBoard;

    private Timer timer;
    private int buffer;

    private Tetrominoe currentPiece;

    private Tetrominoe[] pieceArray;
    private int pieceIndex;

    public Board() {
        timer = new Timer(DELAY, this);
        board = new boolean[GRID_SIZE_X][GRID_SIZE_Y];
        colorBoard = new Color[GRID_SIZE_X][GRID_SIZE_Y];

        timer.start();
        initBoard();
        setPieces();
        setKeys();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = false;
            }
        }   
    }

    private void initBoard() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        setBackground(Color.BLACK);
    }

    private void setPieces() {
        pieceArray = new Tetrominoe[] { new IPiece(this), new JPiece(this), new LPiece(this), new SPiece(this),
                new TPiece(this), new ZPiece(this), new OPiece(this) };

        shufflePieces();
        pieceIndex = 1;
        currentPiece = pieceArray[0];
    }

    private void setKeys() {
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "RotateRight");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, false), "RotateLeft");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "MoveRight");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "MoveRight");

        getActionMap().put("RotateRight", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }
        });

        getActionMap().put("RotateLeft", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }
        });

        getActionMap().put("MoveRight", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentPiece.moveRight();
            }
        });

        getActionMap().put("MoveLeft", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentPiece.moveLeft();
            }
        });
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

    public int getGridSquareSize() {
        return GRID_SQUARE_SIZE;
    }

    public boolean[][] getBoardMatrix() {
        return board;
    }

    private void update() {
        updatePiece();
        repaint();
    }

    private void updatePiece() {
        if (!currentPiece.fall()) {
            currentPiece.convert(board, colorBoard);
            
            if (pieceIndex != pieceArray.length) {
                currentPiece = pieceArray[pieceIndex];
                pieceIndex++;
            } else {
                shufflePieces();

                pieceIndex = 1;
                currentPiece = pieceArray[0];
            }
        }
    }

    private void shufflePieces() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintBoard(g);
    }

    private void paintBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < colorBoard.length; i++) {
            for (int j = 0; j < colorBoard[i].length; j++) {
                if (colorBoard[i][j] != null) {
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
