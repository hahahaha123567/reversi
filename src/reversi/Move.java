package reversi;

public class Move {

    private int row;

    private int column;

    public Move(int x, int y) {
        row = x;
        column = y;
    }

    boolean inBoard () {
        return inBoard(row, column);
    }

    static boolean inBoard (int x, int y) {
        return 0 <= x && x < Board.rowNum
                && 0 <= y && y < Board.columnNum;
    }

    @Override
    public String toString() {
        return "Move{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return row == move.row && column == move.column;
    }
}
