package com.example.demo;

public class Game {
    private int id;
    private int[] playerIds;

    private String content;

    private int winner;

    public Game(int id, int[] playerIds, String content) {
        this.id = id;
        this.playerIds = playerIds;
        this.content = content;
        winner = 0;
    }

    public Game(int id, int[] playerIds, String content, int winner) {
        this.id = id;
        this.playerIds = playerIds;
        this.content = content;
        this.winner = winner;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(int[] playerIds) {
        this.playerIds = playerIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
