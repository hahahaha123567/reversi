package reversi;

import com.sun.istack.internal.Nullable;

import java.util.List;
import java.util.ArrayList;

public class State {

    private Board board;
    private Player currPlayer;

    public State () { }

    public State (String boardInfo, char player) {
        board = new Board(boardInfo);
        currPlayer = player == 'W' ? Player.white : Player.black;
    }

    @Override
    public State clone() {
        State another = new State();
        another.board = this.board.clone();
        another.currPlayer = this.currPlayer;
        return another;
    }

    public void execMove (Move move) throws Exception {
        if (board.isLegalMove(move, currPlayer)) {
            board.execMove(move, currPlayer);
        } else {
            throw new Exception("illegal move" + move);
        }
        currPlayer = currPlayer.equals(Player.black) ? Player.white : Player.black;
    }

    public void printInfo() {
        board.printBoard();
        System.out.print("Black: " + board.getScore(Player.black) + "  ");
        System.out.println("White: " + board.getScore(Player.white));
        System.out.println("Current player: " + currPlayer.sign);
        System.out.println(possibleMoves());
    }

    public List<Move> possibleMoves() {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < Board.rowNum; ++i) {
            for (int j = 0; j < Board.columnNum; ++j) {
                if ( board.isLegalMove(i, j, currPlayer) ) {
                    moves.add(new Move(i, j));
                }
            }
        }
        return moves;
    }

    public Player currWinner () {
        if ( board.getScore(Player.white) > board.getScore(Player.black) ) {
            return Player.white;
        } else if (board.getScore(Player.white) == board.getScore(Player.black)) {
            return null;
        } else {
            return Player.black;
        }
    }

    public Player getCurrPlayer () {
        return currPlayer;
    }

    // bigger value, better for currPlayer
    public int evaluate () {
        int mobility = possibleMoves().size();
        Player enemy = currPlayer == Player.black ? Player.white : Player.black;
        int stability = board.getStability(enemy) - board.getStability(currPlayer);
        return stability * 10 - mobility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return board.equals(state.board) &&
                currPlayer.equals(state.currPlayer);
    }

    public static State beginState() {
        State state = new State();
        state.board = Board.beginBoard();
        state.currPlayer = Player.black;
        return state;
    }

    public boolean gameOver () {
        return possibleMoves().size() == 0; // board.isFull();
    }

    public void randomMove () {
        List<Move> moves = possibleMoves();
        try {
            execMove(moves.get( (int) (Math.random() * moves.size()) ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
