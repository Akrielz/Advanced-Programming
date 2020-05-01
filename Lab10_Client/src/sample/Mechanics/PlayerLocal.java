package sample.Mechanics;

import sample.Graphics.InGameController;

public class PlayerLocal implements Player{
    public int getMove(int[][] board){
        InGameController.setLocalTurn(true);

        while(!InGameController.isHumanFinished()){
            try {
                Thread.sleep(30);
                //System.out.println("Player not ready");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        InGameController.setHumanFinished(false);
        InGameController.setLocalTurn(false);

        return getSelectedY()*board.length + getSelectedX();
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

    private Game game;
    public void setGame(Game game){
        this.game = game;
    }
}
