package sample.Mechanics;

public class Board {
    private int boardSize;
    private int [][]board;

    // -1 ---> Nothing
    //  0 ---> Player0
    //  1 ---> Player1

    public Board() {
        boardSize = 15;

        board = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                board[i][j] = -1;
            }
        }
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int[][] getBoard() {
        return board;
    }
}
