package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public abstract class Tetrominoe {

    private Board board;

    private boolean[][] matrix;
    private boolean canRotate;

    private int x;
    private int y;

    private Color color;

    public Tetrominoe(Board board, boolean[][] matrix, boolean canRotate, Color color) {
        this.board = board;
        this.matrix = matrix;
        this.canRotate = canRotate;
        this.color = color;
        x = 5;
        y = 0;
    }

    public boolean fall() {
        boolean[][] boardMatrix = board.getBoardMatrix();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j]) {
                    if (j + y + 1 == board.getGridY() || boardMatrix[i + x][j + y + 1]) {
                        return false;
                    }
                }
            }
        } 

        y -= 1;
        return true;
    }

    public void moveLeft() {
        if (kick(x - 1)) {
            x--;
        }
    }

    public void moveRight() {
        if (kick(x + 1)) {
            x++;
        }
    }

    public void rotateLeft() {

    }

    public void rotateRight() {

    }

    private boolean kick(int xPos) {
        boolean[][] totalMatrix = board.getBoard();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j]) {
                    if (x + i < 0 || x + i > board.getGridX() - 1) {
                        return true;
                    }

                    if (totalMatrix[i][j]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void convert(boolean[][] boardMatrix, Color[][] colorMatrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j]) {
                    boardMatrix[i + x][j + y] = true;
                    colorMatrix[i + x][j + y] = color;
                }
            }
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(color);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j]) {
                    g2d.fillRect(i + x, j + y, board.getGridSquareSize(), board.getGridSquareSize());
                }
            }
        }
    }

    public boolean[][] getMatrix() {
        return matrix;
    }

    public Color getColor() {
        return color;
    }
}