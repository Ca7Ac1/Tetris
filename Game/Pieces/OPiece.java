package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class OPiece extends Tetrominoe {

    public OPiece(Board board) {
        super(board, new boolean[][] { { true, true, false }, 
                                       { true, true, false },
                                       { false, false, false }, }, 
                                       false, Color.YELLOW);
    }
    
}
