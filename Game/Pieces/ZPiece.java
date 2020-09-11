package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class ZPiece extends Tetrominoe {

    public ZPiece(Board board, boolean[][] matrix, boolean canRotate, Color color) {
        super(board, new boolean[][] { { false, true, false }, 
                                       { true, true, false },
                                       { true, false, false }, }, 
                                       true, Color.CYAN);
    }
    
}
