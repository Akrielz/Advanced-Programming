package sample.Mechanics;

public class PlayerNet implements Player{
    public int getMove(int[][] board){
        return (int)(Math.random()*(15*15));
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
