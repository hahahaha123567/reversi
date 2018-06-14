package algorithm;

import reversi.Move;
import reversi.State;

public class Random implements Algorithm {
    @Override
    public Move nextMove(State state) {
        int n = state.possibleMoves().size();
        return state.possibleMoves().get( (int) (Math.random() * n) );
    }
}
