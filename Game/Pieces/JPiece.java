package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class JPiece extends Tetrominoe {

    public JPiece(Board board, boolean[][] matrix, boolean canRotate, Color color) {
        super(board, new boolean[][] { { false, true, false }, 
                                       { false, true, false },
                                       { true, true, false }, }, 
                                       true, Color.BLUE);
    }
}
