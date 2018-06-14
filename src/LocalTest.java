import algorithm.Algorithm;
import algorithm.MCTS;
import algorithm.Random;
import reversi.Move;
import reversi.Player;
import reversi.State;

import java.util.Scanner;

public class LocalTest {

    private Algorithm algorithm;
    private State state;

    public LocalTest(Algorithm algorithm, State state) {
        this.algorithm = algorithm;
        this.state = state;
    }

    public Move nextMove () {
        return algorithm.nextMove(this.state);
    }

    public void execMove (Move move) throws Exception {
        state.execMove(move);
    }

    public void execMove (int x, int y) throws Exception {
        execMove(new Move(x, y));
    }

    public void printInfo () {
        state.printInfo();
    }

    public static void main(String[] args) throws Exception {

        State state = State.beginState();
        LocalTest test1 = new LocalTest(new MCTS(), state);
        LocalTest test2 = new LocalTest(new Random(), state);

//        while (!state.gameOver()) {
//            state.printInfo();
//            Move move = test1.nextMove();
//            test1.execMove(move);
//            if (state.gameOver()) break;
//            state.printInfo();
//            test2.execMove(test2.nextMove());
//        }

        Scanner in = new Scanner(System.in);
//        LocalTest test = new LocalTest();
        boolean legal = true;
        for (int i = 0; i < 60; ++i) {
            if (legal) {
                state.printInfo();
//                System.out.println(test.nextMove(test.state));
            }
            int x = in.nextInt();
            int y = in.nextInt();
            try {
//                test.execMove(x, y);
                legal = true;
            } catch (Exception e) {
                if (e.getMessage().startsWith("illegal move")) {
                    System.out.println("illegal step, please try again");
                    legal = false;
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

}
