package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class ZPiece extends Tetrominoe {

    public ZPiece(Board board) {
        super(board, new boolean[][] { { true, true, false }, 
                                       { false, true, true },
                                       { false, false, false }, }, 
                                       true, Color.RED);
    }
    
}
