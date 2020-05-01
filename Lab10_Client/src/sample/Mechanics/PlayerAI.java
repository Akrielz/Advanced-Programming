package sample.Mechanics;

import java.util.ArrayList;
import java.util.List;

public class PlayerAI implements Player{

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

        int position = (int)(Math.random()*possibleMoves.size());
        return possibleMoves.get(position);
    }

    private int selectedX;
    private int selectedY;
    public void setSelected(int selectedX, int selectedY){
        this.selectedX = selectedX;
        this.selectedY = selectedY;
    }

    public int getSelectedX() {
        return selectedX;
    }

    public int getSelectedY() {
        return selectedY;
    }
}
