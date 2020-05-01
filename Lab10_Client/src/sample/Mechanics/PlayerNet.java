package sample.Mechanics;

public class PlayerNet implements Player{
    public int getMove(int[][] board){

        while(!game.getTcpClient().isDecoded()){
            try {
                Thread.sleep(30);
                //System.out.println("Player not ready");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int move = game.getTcpClient().getMove();

        game.getTcpClient().setDecoded(false);

        return move;
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
