package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class IPiece extends Tetrominoe {

    public IPiece(Board board) {
        super(board, new boolean[][] { { false, false, true, false }, 
                                       { false, false, true, false },
                                       { false, false, true, false }, 
                                       { false, false, true, false } }, 
                                       true, Color.CYAN);
    }
}
