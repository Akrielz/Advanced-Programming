package sample.Mechanics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerAI implements Player{
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getMove(int[][] board){
        int size = board.length;

        List<Integer> possibleMoves = new ArrayList<>();

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (board[i][j] == -1){
                    possibleMoves.add(i*size+j);
                }
            }
        }

        possibleMoves.sort(
                (pos1, pos2) -> {
                    int x1 = pos1%size;
                    int y1 = pos1/size;
                    int x2 = pos2%size;
                    int y2 = pos2/size;

                    int yc = size/2;
                    int xc = size/2;

                    return ((yc-y1)*(yc-y1) + (xc-x1)*(xc-x1)) -
                            ((yc-y2)*(yc-y2) + (xc-x2)*(xc-x2));
                }
        );

        for (int lookUp = 5; lookUp >= 2; lookUp--){
            for (int move : possibleMoves){
                if (analizeMove(move, color, board, lookUp)){
                    System.out.println("E" + lookUp);
                    return move;
                }
            }

            if (lookUp > 3) {
                for (int move : possibleMoves) {
                    if (analizeMove(move, 1 - color, board, lookUp)) {
                        System.out.println("C" + lookUp);
                        return move;
                    }
                }
            }
        }

        int position = (int)(Math.random()*possibleMoves.size()/4);
        return possibleMoves.get(position);
    }

    private int selectedX;
    private int selectedY;
    public void setSelected(int selectedX, int selectedY){
        this.selectedX = selectedX;
        this.selectedY = selectedY;
    }

    private boolean analizeMove(int move, int color, int[][] insideBoard, int lookUp) {
        int line = move/15;
        int col = move%15;

        insideBoard[line][col] = color;

        int sum = 0;
        boolean relevant = false;
        for (int i = -4; i < 5; i++){
            if (col+i < 0 || col+i >= insideBoard.length) {
                sum = 0;
                continue;
            }
            if (insideBoard[line][col+i] == color){
                sum ++;
            }
            else{
                sum = 0;
                if (insideBoard[line][col+i] == 1-color){
                    relevant = false;
                }
            }

            if (i == 0){
                relevant = true;
            }

            if (sum >= lookUp && relevant) {
                insideBoard[line][col] = -1;
                return true;
            }
        }

        sum = 0;
        relevant = false;
        for (int i = -4; i < 5; i++){
            if (line+i < 0 || line+i >= insideBoard.length) {
                sum = 0;
                continue;
            }
            if (insideBoard[line+i][col] == color){
                sum ++;
            }
            else{
                sum = 0;
                if (insideBoard[line+i][col] == 1-color){
                    relevant = false;
                }
            }
            if (i == 0){
                relevant = true;
            }

            if (sum >= lookUp && relevant) {
                insideBoard[line][col] = -1;
                return true;
            }
        }

        sum = 0;
        relevant = false;
        for (int i = -4; i < 5; i++){
            if (line+i < 0 || line+i >= insideBoard.length || col+i < 0 || col+i >= insideBoard.length) {
                sum = 0;
                continue;
            }
            if (insideBoard[line+i][col+i] == color){
                sum ++;
            }
            else{
                sum = 0;
                if (insideBoard[line+i][col+i] == 1-color){
                    relevant = false;
                }
            }
            if (i == 0){
                relevant = true;
            }

            if (sum >= lookUp && relevant) {
                insideBoard[line][col] = -1;
                return true;
            }
        }

        sum = 0;
        relevant = false;
        for (int i = -4; i < 5; i++){
            if (line+i < 0 || line+i >= insideBoard.length || col-i < 0 || col-i >= insideBoard.length) {
                sum = 0;
                continue;
            }
            if (insideBoard[line+i][col-i] == color){
                sum ++;
            }
            else{
                sum = 0;
                if (insideBoard[line+i][col-i] == 1-color){
                    relevant = false;
                }
            }
            if (i == 0){
                relevant = true;
            }

            if (sum >= lookUp && relevant) {
                insideBoard[line][col] = -1;
                return true;
            }
        }

        insideBoard[line][col] = -1;
        return false;
    }

    public int getSelectedX() {
        return selectedX;
    }

    public int getSelectedY() {
        return selectedY;
    }

    private Game game;
    public void setGame(Game game){
        this.game = game;
    }
}


