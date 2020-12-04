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

    private final int WIDTH_EXTRA = GRID_SQUARE_SIZE * 5;
    private final int HEIGHT_EXTRA = GRID_SIZE_Y * 2;

    private final int DRAW_NEXT = 3;

    private final int DELAY = 0;
    private final int DELAY_BUFFER = 180;
    private final int PLACE_DELAY = 4;
    private final int FALL_SPEED = 60;
    private final int STALL = 30;

    private final int DAS = 80;
    private final int ARR = 5;

    private boolean[][] board;
    private Color[][] colorBoard;

    private Timer timer;
    private int buffer;
    private int placeBuffer;
    private boolean bufferControlTimer;

    private Tetrominoe currentPiece;
    private Tetrominoe heldPiece;
    private boolean held;

    private boolean moveLeft;
    private boolean moveRight;
    private boolean fall;

    private Tetrominoe[][] pieces;
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
        setPreferredSize(new Dimension(WIDTH + (WIDTH_EXTRA * 2), HEIGHT));
        setFocusable(true);
        setBackground(Color.BLACK);
    }

    private void setPieces() {
        pieces = new Tetrominoe[2][];
        pieces[0] = new Tetrominoe[] { new IPiece(this), new JPiece(this), new LPiece(this), new SPiece(this),
                new TPiece(this), new ZPiece(this), new OPiece(this) };

        pieces[1] = new Tetrominoe[] { new IPiece(this), new JPiece(this), new LPiece(this), new SPiece(this),
                new TPiece(this), new ZPiece(this), new OPiece(this) };

        shufflePieces(pieces[0]);
        shufflePieces(pieces[0]);
        pieceIndex = 1;
        currentPiece = pieces[0][0];
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
                repaint();
            }
        });

        getActionMap().put("RotateLeft", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentPiece.rotateLeft();
                bufferStall();
                repaint();
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
                repaint();
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
                repaint();
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
        if (pieceIndex == pieces[0].length) {
            shufflePieces(pieces[0]);
        }

        if (pieceIndex == pieces[0].length + pieces[1].length) {
            shufflePieces(pieces[1]);

            pieceIndex = 1;
            currentPiece = pieces[0][0];
        } else {
            if (pieceIndex < pieces[0].length) {
                currentPiece = pieces[0][pieceIndex];
                pieceIndex++;
            } else {
                currentPiece = pieces[1][pieceIndex % 7];
                pieceIndex++;
            }
        }

        dasCounter = 0;
        arrCounter = 0;
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

    private void shufflePieces(Tetrominoe[] arr) {
        Random random = new Random();
        int newPos;
        Tetrominoe temp;
        arr[0] = new IPiece(this);
        arr[1] = new JPiece(this);
        arr[2] = new LPiece(this);
        arr[3] = new SPiece(this);
        arr[4] = new TPiece(this);
        arr[5] = new ZPiece(this);
        arr[6] = new OPiece(this);

        for (int i = 0; i < arr.length; i++) {
            newPos = random.nextInt(arr.length);

            temp = arr[i];
            arr[i] = arr[newPos];
            arr[newPos] = temp;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintBoard(g);
        paintGrid(g);
        paintHold(g);
        paintNext(g);
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

    private void paintHold(Graphics g) {
        if (heldPiece != null) {
            heldPiece.drawFloating(g, WIDTH + (WIDTH_EXTRA * 5 / 4), HEIGHT_EXTRA);
        }
    }

    private void paintNext(Graphics g) {
        for (int i = 0; i < DRAW_NEXT; i++) {
            pieces[((i + pieceIndex) / 7) % 2][(pieceIndex + i) % 7].drawFloating(g, WIDTH + (WIDTH_EXTRA / 4),
                    HEIGHT_EXTRA + (i * 4 * GRID_SQUARE_SIZE));
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

            if (moveLeft || moveRight) {
                if (dasCounter >= DAS) {
                    if (arrCounter >= ARR) {
                        move();
                        repaint();
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
    }

    private void update() {
        if (!currentPiece.fall()) {
            if (placeBuffer == PLACE_DELAY) {
                placeBuffer = 0;
                held = false;

                currentPiece.convert(board, colorBoard);
                getNextPiece();
            } else {
                placeBuffer++;
            }
        } else {
            placeBuffer = 0;
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
