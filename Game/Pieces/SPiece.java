package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class SPiece extends Tetrominoe {

    public SPiece(Board board) {
        super(board, new boolean[][] { { false, false, false }, 
                                       { true, true, false },
                                       { false, true, true }, }, 
                                       true, Color.GREEN);
    }
    
}
