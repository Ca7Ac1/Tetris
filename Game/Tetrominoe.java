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
        x = board.getGridX() / 3;
        y = 0;
    }

    public void resetPosition() {
        x = board.getGridX() / 3;
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

        y++;
        return true;
    }

    public void moveLeft() {
        if (!kick(x - 1, y)) {
            x--;
        }
    }

    public void moveRight() {
        if (!kick(x + 1, y)) {
            x++;
        }
    }

    public void rotateLeft() {
        boolean[][] previousMatrix = new boolean[matrix.length][matrix[0].length];

        if (canRotate) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    previousMatrix[i][j] = matrix[i][j];
                }
            }

            for (int i = 0; i < matrix.length / 2; i++) {
                for (int j = i; j < matrix.length - i - 1; j++) {
                    boolean temp = matrix[i][j];

                    matrix[i][j] = matrix[matrix.length - j - 1][i];
                    matrix[matrix.length - j - 1][i] = matrix[matrix.length - i - 1][matrix.length - j - 1];
                    matrix[matrix.length - i - 1][matrix.length - j - 1] = matrix[j][matrix.length - i - 1];
                    matrix[j][matrix.length - i - 1] = temp;
                }
            }
        }

        testLeft(previousMatrix);
    }

    public void rotateRight() {
        boolean[][] previousMatrix = new boolean[matrix.length][matrix[0].length];

        if (canRotate) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    previousMatrix[i][j] = matrix[i][j];
                }
            }

            for (int i = 0; i < matrix.length / 2; i++) {
                for (int j = i; j < matrix.length - i - 1; j++) {
                    boolean temp = matrix[i][j];

                    matrix[i][j] = matrix[j][matrix.length - i - 1];
                    matrix[j][matrix.length - i - 1] = matrix[matrix.length - i - 1][matrix.length - j - 1];
                    matrix[matrix.length - i - 1][matrix.length - j - 1] = matrix[matrix.length - j - 1][i];
                    matrix[matrix.length - j - 1][i] = temp;
                }
            }
        }

        testRight(previousMatrix);
    }

    protected void testLeft(boolean[][] workingMatrix) {
    }

    protected void testRight(boolean[][] workingMatrix) {
        if (!kick(x, y)) {
            return;
        } else if (!kick(x - 1, y)) {
            x--;
        } else if (!kick(x + 1, y)) {
            x++;
        } else if (!kick(x - 1, y + 1)) {
            x--;
            y++;
        } else if (!kick(x + 1, y + 1)) {
            x++;
            y++;
        } else if (!kick(x, y - 2)) {
            y -= 2;
        } else if (!kick(x - 1, y - 2)) {
            x--;
            y -= 2;
        } else if (!kick(x + 1, y - 2)) {
            x++;
            y -= 2;
        } else {
            matrix = workingMatrix;
        }
    }

    private boolean kick(int xPos, int yPos) {
        boolean[][] totalMatrix = board.getBoard();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j]) {
                    if (i + xPos < 0 || i + xPos >= board.getGridX()) {
                        return true;
                    }

                    if (j + yPos >= board.getGridY()) {
                        return true;
                    }

                    if (totalMatrix[i + xPos][j + yPos]) {
                        return true;
                    }

                }
            }
        }
        return false;
    }

    public void convert(boolean[][] boardMatrix, Color[][] colorMatrix) {
        int row;
        boolean rowDeleted = false;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j]) {
                    row = 0;

                    boardMatrix[i + x][j + y] = true;
                    colorMatrix[i + x][j + y] = color;
                }
            }
        }

        for (int i = 0; i < matrix[0].length; i++) {
            row = 0;

            if (i + y < board.getGridY() && i + y >= 0) {
                for (int j = 0; j < boardMatrix.length; j++) {
                    if (boardMatrix[j][i + y]) {
                        row++;
                    } else {
                        break;
                    }
                }

                if (row == board.getGridX()) {
                    rowDeleted = true;

                    for (int k = 0; k < boardMatrix.length; k++) {
                        boardMatrix[k][i + y] = false;
                        colorMatrix[k][i + y] = null;
                    }
                }
            }
        }

        if (rowDeleted) {
            board.drop();
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int squareSize = board.getGridSquareSize();

        g2d.setColor(color);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j]) {
                    g2d.fillRect((i + x) * squareSize, (j + y) * squareSize, squareSize, squareSize);
                }
            }
        }
    }

    public void drawFloating(Graphics g, int xPos, int yPos) {
        Graphics2D g2d = (Graphics2D) g;
        int squareSize = board.getGridSquareSize();

        g2d.setColor(color);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j]) {
                    g2d.fillRect((i * squareSize) + xPos, (j * squareSize) + yPos, squareSize, squareSize);
                }
            }
        }
    }

    public void drawGhost(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int squareSize = board.getGridSquareSize();
        int originalY = y;

        g2d.setColor(new Color(1, 1, 1, .5f));

        while (fall()) {
            continue;
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j]) {
                    g2d.fillRect((i + x) * squareSize, (j + y) * squareSize, squareSize, squareSize);
                }
            }
        }

        y = originalY;
    }

    public boolean[][] getMatrix() {
        return matrix;
    }

    public Color getColor() {
        return color;
    }
}