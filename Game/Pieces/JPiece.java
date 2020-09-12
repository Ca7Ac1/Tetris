package Game.Pieces;

import java.awt.Color;

import Game.Board;
import Game.Tetrominoe;

public class JPiece extends Tetrominoe {

    public JPiece(Board board) {
        super(board, new boolean[][] { { true, false, false }, 
                                       { true, true, true },
                                       { false, false, false }, }, 
                                       true, Color.BLUE);
    }
}
