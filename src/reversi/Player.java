package reversi;

public enum Player {

    white ('O'),
    black ('X');

    char sign;

    Player (char sign) {
        this.sign = sign;
    }

}
