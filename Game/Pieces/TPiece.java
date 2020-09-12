package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class TPiece extends Tetrominoe {

    public TPiece(Board board) {
        super(board, new boolean[][] { { false, true, false }, 
                                       { true, true, false },
                                       { false, true, false }, }, 
                                       true, Color.MAGENTA);
    }
    
}
