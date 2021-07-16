package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class ZPiece extends Tetrominoe {

    public ZPiece(Board board) {
        super(board, new boolean[][] { { false, true, true }, 
                                       { true, true, false },
                                       { false, false, false }, }, 
                                       true, Color.RED);
    }
    
}
