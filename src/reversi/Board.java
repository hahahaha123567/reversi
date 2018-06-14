package reversi;

class Board {

    static final int rowNum = 8;
    static final int columnNum = 8;
    private Player[][] map = new Player[rowNum][columnNum];

    private static int[][] d1 = { {1, 0}, {0, 1}, {1, 1}, {1, -1} };
    private static int[][] d2 = { {-1, 0}, {0, -1}, {-1, -1}, {-1, 1} };

    Board () { }

    Board (String boardInfo) {
        String[] lineInfoArray = boardInfo.split(";");
        for (int i = 0; i < lineInfoArray.length; ++i) {
            String[] info = lineInfoArray[i].split("\\.");
            for (int j = 0; j < info.length; ++j) {
                char id = info[j].charAt(0);
                if (id == 'W') {
                    map[i][j] = Player.white;
                } else if (id == 'B') {
                    map[i][j] = Player.black;
                }
            }
        }
    }

    int getScore (Player player) {
        int count = 0;
        for (Player[] mapRow : map) {
            for (Player tempPlayer : mapRow) {
                if (tempPlayer == player) {
                    count++;
                }
            }
        }
        return count;
    }

    boolean isLegalMove (int xx, int yy, Player player) {
        // used in possibleMoves(), must in board
        if ( map[xx][yy] != null ) {
            return false;
        }
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i == j && i == 0) { continue; }
                int x = xx;
                int y = yy;
                do {
                    x += i;
                    y += j;
                } while ( Move.inBoard(x, y) && map[x][y] != null && !map[x][y].equals(player) );
                if (Move.inBoard(x, y) && map[x][y] != null && map[x][y].equals(player)) {
                    if (x - xx != i || y - yy != j) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean isLegalMove (Move move, Player player) {
        // used to judge the user input is legal or not, need to calcu inBoard()
        return move.inBoard() && isLegalMove(move.getRow(), move.getColumn(), player);
    }

    void execMove (Move move, Player player) {
        if ( !move.inBoard() ) {
            return;
        }
        map[move.getRow()][move.getColumn()] = player;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i == j && i == 0) { continue; }
                int x = move.getRow();
                int y = move.getColumn();
                do {
                    x += i;
                    y += j;
                } while ( Move.inBoard(x, y) && map[x][y] != null && !map[x][y].equals(player) );
                if (Move.inBoard(x, y) && map[x][y] != null && map[x][y].equals(player)) {
                    x -= i;
                    y -= j;
                    while ( Move.inBoard(x, y) && map[x][y] != null && !map[x][y].equals(player)) {
                        map[x][y] = player;
                        x -= i;
                        y -= j;
                    }
                }
            }
        }
    }

    int getStability (Player player) {
        int sum = 0;
        for (int i = 0; i < rowNum; ++i) {
            for (int j = 0; j < columnNum; ++j) {
                if (map[i][j] == player) {
                    sum += singleStability(i, j);
                }
            }
        }
        return sum;
    }

    private int singleStability (int xx, int yy) {
        Player player = map[xx][yy];
        int count = 0;
        for (int index = 0; index < 4; ++index) {
            int enemyNum = 0;
            for (int i = 0; i < 2; ++i) {
                int[][] dd = i == 0 ? d1 : d2;
                int x = xx;
                int y = yy;
                while (inBoard(x, y) && map[x][y] == player) {
                    x += dd[index][0];
                    y += dd[index][1];
                }
                if (!inBoard(x, y)) {
                    count++;
                    break;
                } else if (map[x][y] != null) {
                    enemyNum++;
                    if (enemyNum == 2) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private boolean inBoard (int x, int y) {
        return 0 <= x && x < rowNum && 0 <= y && y < columnNum;
    }

    void printBoard() {
        System.out.print(' ');
        for (int i = 0; i < rowNum; ++i) {
            System.out.print(i);
        }
        System.out.println();
        int i = 0;
        for (Player[] mapRow : map) {
            System.out.print(i++);
            for (Player tempPlayer : mapRow) {
                System.out.print(tempPlayer == null ? '-' : tempPlayer.sign);
            }
            System.out.println();
        }
    }

    @Override
    protected Board clone() {
        Board another = new Board();
        for (int i = 0; i < rowNum; ++i) {
            System.arraycopy(this.map[i], 0, another.map[i], 0, columnNum);
        }
        return another;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map[0].length; ++j) {
                if (map[i][j] != board.map[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    static Board beginBoard () {
        Board board = new Board();
        board.map[3][3] = board.map[4][4] = Player.white;
        board.map[3][4] = board.map[4][3] = Player.black;
        return board;
    }

    boolean isFull () {
        for (int i = 0; i < rowNum; ++i) {
            for (int j = 0; j < columnNum; ++j) {
                if (map[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Board board = new Board("B.0.0.0.0.0.0.0;0.0.0.0.0.0.0.0;0.0.0.0.0.0.0.0;B.W.W.W.W.W.W.W;0.0.0.0.0.0.0.0;0.0.0.0.0.0.0.0;0.0.0.0.0.0.0.0;0.0.0.0.0.0.0.0");
        System.out.println(board.getStability(Player.black));
    }
}
