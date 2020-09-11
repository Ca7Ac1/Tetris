import java.awt.Color;

public abstract class Tetrominoe {

    Board board;

    boolean[][] matrix;
    boolean canRotate;

    int x;
    int y;

    Color color;

    public Tetrominoe(Board board, boolean[][] matrix, boolean canRotate, Color color) {
        this.board = board;
        this.matrix = matrix;
        this.canRotate = canRotate;
        this.color = color;
        x = 5;
        y = 26;
    }

    public boolean fall() {
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
                if (matrix[i][j] == true) {
                    if (x + i < 0 || x + i > board.getGridX() - 1) {
                        return true;
                    }
                    
                    if (totalMatrix[i][j] == true) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}