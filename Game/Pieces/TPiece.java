package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class TPiece extends Tetrominoe {

    public TPiece(Board board, boolean[][] matrix, boolean canRotate, Color color) {
        super(board, new boolean[][] { { false, true, false }, 
                                       { true, true, true },
                                       { false, false, false }, }, 
                                       true, Color.MAGENTA);
    }
    
}
