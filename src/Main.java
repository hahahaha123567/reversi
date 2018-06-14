import algorithm.MCTS;
import reversi.Move;
import reversi.State;

public class Main {

    public static void main (String[] args) throws Exception {
        MCTS mcts = new MCTS();
        API api = new API(9);
//        api.init();
        char id = api.getId();
        System.out.println("My color is: " + id);
        while (api.isOnGoing()) {
            if (api.isMyTurn()) {
                System.out.println("ni");
                String boardInfo = api.getBoardInfo();
                State state = new State(boardInfo, id);
                state.printInfo();
                Move move = mcts.nextMove(state);
                api.postMove(move.getRow(), move.getColumn());
                state.execMove(move);
                state.printInfo();
            } else {
                Thread.sleep(100);
            }
        }
    }

}
