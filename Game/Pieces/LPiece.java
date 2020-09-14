package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class LPiece extends Tetrominoe {

    public LPiece(Board board) {
        super(board, new boolean[][] { { false, false, true }, 
                                       { true, true, true },
                                       { false, false, false }, }, 
                                       true, Color.ORANGE);
    }
    
}
