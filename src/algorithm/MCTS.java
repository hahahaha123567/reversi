package algorithm;

import reversi.Move;
import reversi.State;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MCTS implements Algorithm {

    private static double uctArg = 1.414213;
    private static Move[] corner = {
            new Move(0, 0),
            new Move(0, 7),
            new Move(7, 0),
            new Move(7, 7)
    };

    @Override
    public Move nextMove(State state) {
        List<Move> moves = state.possibleMoves();
        for (Move move : corner) {
            if (moves.contains(move)) {
                return move;
            }
        }
        Node root = new Node(state);
        return root.nextMove();
    }

    private static double uctValue (int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return ( nodeWinScore / (double) nodeVisit )
                + uctArg * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    static Node findBestNodeWithUCT(Node node) {
        int parentVisit = node.visitNum;
        return Collections.max(
                node.children,
                Comparator.comparing(c -> uctValue(parentVisit, c.winScore, c.visitNum))
        );
    }

}
