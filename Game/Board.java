package Game;

import Game.Pieces.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

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

    private final int DELAY = 1;
    private final int DELAY_BUFFER = 150;
    private final int FALL_DELAY = 3;
    private final int FALL_SPEED = 8;
    private final int STALL = 5;

    private final int DAS = 18;
    private final int ARR = 18;

    private boolean[][] board;
    private Color[][] colorBoard;

    private Timer timer;
    private int buffer;
    private int fallBuffer;
    private boolean bufferControlTimer;

    private Tetrominoe currentPiece;
    private Tetrominoe heldPiece;
    private boolean held;

    private boolean moveLeft;
    private boolean moveRight;
    private boolean fall;

    private Tetrominoe[] pieceArray;
    private int pieceIndex;

    private int fallCount;

    private int dasCounter;
    private int arrCounter;

    public Board() {
        timer = new Timer(DELAY, this);
        bufferControlTimer = false;
        held = false;
        fall = false;
        fallCount = 0;
        dasCounter = 0;
        arrCounter = 0;
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
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "MoveLeft");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "StopRight");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "StopLeft");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "SoftDrop");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "StopSoftDrop");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "HardDrop");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, false), "Hold");

        getActionMap().put("RotateRight", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentPiece.rotateRight();
                bufferStall();
            }
        });

        getActionMap().put("RotateLeft", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentPiece.rotateLeft();
                bufferStall();
            }
        });

        getActionMap().put("MoveRight", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentPiece.moveRight();
                moveRight = true;
                moveLeft = false;
                arrCounter = 0;
                dasCounter = 0;
                bufferStall();
            }
        });

        getActionMap().put("MoveLeft", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentPiece.moveLeft();
                moveLeft = true;
                moveRight = false;
                arrCounter = 0;
                dasCounter = 0;
                bufferStall();
            }
        });

        getActionMap().put("StopRight", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveRight = false;
                arrCounter = 0;
                dasCounter = 0;
            }
        });

        getActionMap().put("StopLeft", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveLeft = false;
                arrCounter = 0;
                dasCounter = 0;
            }
        });

        getActionMap().put("SoftDrop", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fall = true;

                update();
                repaint();
                bufferStall();
            }
        });

        getActionMap().put("StopSoftDrop", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fall = false;
            }
        });

        getActionMap().put("HardDrop", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                while (currentPiece.fall()) {
                    continue;
                }

                held = false;

                currentPiece.convert(board, colorBoard);
                getNextPiece();

                repaint();
                bufferStall();
            }
        });

        getActionMap().put("Hold", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                holdPiece();
            }
        });
    }

    private void bufferStall() {
        if (bufferControlTimer) {
            buffer -= STALL;
        }
    }

    public void drop() {
        int row;

        for (int i = GRID_SIZE_Y - 1; i >= findHighestPoint(); i--) {
            if (findHighestPoint() != GRID_SIZE_Y) {
                row = 0;

                for (int j = 0; j < GRID_SIZE_X; j++) {
                    if (!board[j][i]) {
                        row++;
                    } else {
                        break;
                    }
                }

                if (row == GRID_SIZE_X) {
                    for (int k = i; k > 0; k--) {
                        for (int replaceX = 0; replaceX < GRID_SIZE_X; replaceX++) {
                            board[replaceX][k] = board[replaceX][k - 1];
                            colorBoard[replaceX][k] = colorBoard[replaceX][k - 1];
                        }
                    }

                    i++;
                }
            } else {
                break;
            }
        }
    }

    private int findHighestPoint() {
        for (int i = 0; i < GRID_SIZE_Y; i++) {
            for (int j = 0; j < GRID_SIZE_X; j++) {
                if (board[j][i]) {
                    return i;
                }
            }
        }
        return GRID_SIZE_Y;
    }

    private void getNextPiece() {
        if (pieceIndex != pieceArray.length) {
            currentPiece = pieceArray[pieceIndex];
            pieceIndex++;
        } else {
            System.out.println("refresh");
            shufflePieces();

            pieceIndex = 1;
            currentPiece = pieceArray[0];
        }
    }

    private void holdPiece() {
        if (!held) {
            Tetrominoe temp;
            held = true;

            if (heldPiece == null) {
                heldPiece = currentPiece;

                heldPiece.resetPosition();
                getNextPiece();
            } else {
                temp = heldPiece;
                heldPiece = currentPiece;
                currentPiece = temp;

                currentPiece.resetPosition();
                heldPiece.resetPosition();
            }
        }
    }

    private void shufflePieces() {
        Random random = new Random();
        int newPos;
        Tetrominoe temp;
        pieceArray = new Tetrominoe[] { new IPiece(this), new JPiece(this), new LPiece(this), new SPiece(this),
                new TPiece(this), new ZPiece(this), new OPiece(this) };

        for (int i = 0; i < pieceArray.length; i++) {
            newPos = random.nextInt(pieceArray.length);

            temp = pieceArray[i];
            pieceArray[i] = pieceArray[newPos];
            pieceArray[newPos] = temp;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintBoard(g);
        paintGrid(g);
        currentPiece.draw(g);
        currentPiece.drawGhost(g);
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

    private void paintGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        for (int i = 0; i < GRID_SIZE_X; i++) {
            for (int j = 0; j < GRID_SIZE_Y; j++) {
                g2d.drawRect(i * GRID_SQUARE_SIZE, j * GRID_SQUARE_SIZE, GRID_SQUARE_SIZE, GRID_SQUARE_SIZE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        bufferControlTimer = true;

        if (e.getSource() == timer) {
            if (buffer == DELAY_BUFFER) {
                update();
                buffer = 0;
            } else {
                buffer++;
            }
        }

        if (moveLeft || moveRight) {
            if (dasCounter >= DAS) {
                if (arrCounter >= ARR) {
                    move();
                    arrCounter = 0;
                } else {
                    arrCounter++;
                }
            } else {
                dasCounter++;
            }
        } else {
            dasCounter = 0;
            arrCounter = 0;
        }

        if (fall) {
            if (fallCount >= FALL_SPEED) {
                update();
                repaint();
                fallCount = 0;
            } else {
                fallCount++;
            }
        } else {
            fallCount = 0;
        }
    }

    private void update() {
        if (!currentPiece.fall()) {
            if (fallBuffer == FALL_DELAY) {
                fallBuffer = 0;
                held = false;

                currentPiece.convert(board, colorBoard);
                getNextPiece();
            } else {
                fallBuffer++;
            }
        } else {
            fallBuffer = 0;
        }
    }

    private void move() {
        if (moveLeft) {
            currentPiece.moveLeft();
        } else if (moveRight) {
            currentPiece.moveRight();
        }
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

}
