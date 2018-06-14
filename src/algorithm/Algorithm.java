package algorithm;

import reversi.Move;
import reversi.State;

public interface Algorithm {

    public Move nextMove (State state);

}
