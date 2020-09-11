package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class SPiece extends Tetrominoe {

    public SPiece(Board board) {
        super(board, new boolean[][] { { true, false, false }, 
                                       { true, true, false },
                                       { false, true, false }, }, 
                                       true, Color.GREEN);
    }
    
}
