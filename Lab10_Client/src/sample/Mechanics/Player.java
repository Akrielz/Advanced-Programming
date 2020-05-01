package sample.Mechanics;

public interface Player {
    public int getMove(int[][] board);

    public void setSelected(int selectedX, int selectedY);

    public int getSelectedX();

    public int getSelectedY();

    public void setGame(Game game);
}
