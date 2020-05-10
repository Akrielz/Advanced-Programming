package com.example.demo;


import com.example.demo.Exceptions.ExceptionBoardLength;
import com.example.demo.Exceptions.ExceptionColor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ai")
public class AIController {
    private static final int ACCEPTED_LENGTH = 15;

    public AIController() {}

    /*
        Mapping: {
            Free spot -> A -> 0
            Player 1  -> B -> 1
            Player 2  -> C -> 2
        }

        ex input:
        {
            "color" : 0,
            "board" : "50A1B15A1C1A1B11A1C3B1C11A1B1C1B11A1C1B2C12A1C98A"
        }
     */

    @GetMapping(value = "/obj", consumes = "application/json")
    public String getMove(@RequestBody AIBoard aiBoard){

        String code = "";

        int nr = 0;
        for (int i = 0; i < aiBoard.getBoard().length(); i++){

            if (Character.isDigit(aiBoard.getBoard().charAt(i))){
                nr = nr*10 + (aiBoard.getBoard().charAt(i) - '0');
            }
            else{
                for (int j = 0; j < nr; j++){
                    code += (char)(aiBoard.getBoard().charAt(i) - 'A' + '0');
                }
                nr = 0;
            }
        }

        if (code.length() != ACCEPTED_LENGTH*ACCEPTED_LENGTH){
            throw new ExceptionBoardLength();
        }

        if (aiBoard.getColor() != 0 && aiBoard.getColor() != 1){
            throw new ExceptionColor();
        }

        int[][] decoded = new int [ACCEPTED_LENGTH][ACCEPTED_LENGTH];
        for (int i = 0; i < ACCEPTED_LENGTH; i++){
            for (int j = 0; j < ACCEPTED_LENGTH; j++){
                int k = i*ACCEPTED_LENGTH+j;
                decoded[i][j] = (code.charAt(k)-'0')-1;
            }
        }

        int move = getMove(decoded, aiBoard.getColor());
        int x = move%ACCEPTED_LENGTH;
        int y = move/ACCEPTED_LENGTH;
        return "X=" + x + ", Y=" + y;
    }

    public static int getMove(int[][] board, int color){
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
                if (analiseMove(move, color, board, lookUp)){
                    System.out.println("E" + lookUp);
                    return move;
                }
            }

            if (lookUp > 3) {
                for (int move : possibleMoves) {
                    if (analiseMove(move, 1 - color, board, lookUp)) {
                        System.out.println("C" + lookUp);
                        return move;
                    }
                }
            }
        }

        int position = (int)(Math.random()*possibleMoves.size()/4);
        return possibleMoves.get(position);
    }

    private static boolean analiseMove(int move, int color, int[][] insideBoard, int lookUp) {
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
}
