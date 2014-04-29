//package edu.oregonstate.eecs.cs331.assn2;

import java.util.*;
import static java.lang.Math.*;


/**
 * This class represents the module for minimax.
 * @author David Merrick
 *
 */

public class MiniMax implements Player {
    /**
     * Constructor
     *
     */
    public MiniMax() {
    }

    Position nextPosition = new Position();

    /**
     * Returns the next move.
     * @param state The current board state in the game
     * @return The next move
     */
    public Position getNextMove(TicTacToeBoard state) {
        nextPosition = minimaxDecision(state);

        return nextPosition;
    }

    /**
     * Returns the player type.
     */
    public int getPlayerType() {
        return MINIMAX_PLAYER;
    }

    /**
     * Generates the list of successor board states
     * @param state The current board state in the game
     * @return The list of successor board states
     */
    private List<TicTacToeBoard> successors(TicTacToeBoard state) {
        //Create a new list for the successors
        List<TicTacToeBoard> successorList = new ArrayList<TicTacToeBoard>();

        //Generate the successors and append them to the successor array.
        for (int i=0; i<3; i++) { //Loop through the rows
            for (int j=0; j<3; j++) { //Loop through the columns
                //Only generate the list of successors initially
                if (state.getState(i, j) == TicTacToeBoard.BLANK) {
                    //Clone the current board so we can append it to the
                    //successorList without interfering with it
                    TicTacToeBoard s = (TicTacToeBoard) state.clone();

                    //Find out who's turn it is
                    int turn = state.getTurn();

                    //Play the game.
                    try {
                        s.setState(i, j, turn);
                    }
                    catch (Exception e) {
                    }

                    //Set the next player.
                    int nextTurn = getNextPlayer(turn);
                    s.setTurn(nextTurn);

                    //Append board to list of successors.
                    successorList.add(s);
                }
            }
        }

        return successorList;
    }

    private int getNextPlayer(int turn) {
        //return next player's turn.
        if (turn == TicTacToeBoard.PLAYER_X) {
            return TicTacToeBoard.PLAYER_O;
        }

        return TicTacToeBoard.PLAYER_X;
    }

    /**
     * Return utility of game state.
     * @param state The current board state in the game
     * @return The utility of the game state. 1 if Player X wins, -1 if Player
     *     O wins, 0 if draw.
     */
    private int utility(TicTacToeBoard state) {
        int u = 0;
        try {
            if (state.isWin(TicTacToeBoard.PLAYER_X)) {
                u = 1;
            }
            else if (state.isWin(TicTacToeBoard.PLAYER_O)) {
                u = -1;
            }
        }
        catch (Exception e) {
        }

        return u;
    }

    /**
     * Return terminal status of game.
     * @param state The current board state in the game
     * @return Terminal status. True if state is terminal. False otherwise.
     */
    private boolean terminalTest(TicTacToeBoard state) {
        boolean testRes = false;
        try {
            testRes = state.isGameOver();
        }
        catch (Exception e) {
        }

        return testRes;
    }

    /**
     * Return minimax decision.
     * @param state The current board state in the game
     * @return The best move.
     */
    private Position minimaxDecision(TicTacToeBoard state) {
        Position bestMove = new Position();

        int turn = state.getTurn();
        int v;
        if (turn == TicTacToeBoard.PLAYER_X) {
            v = maxValue(state);   // Player X maximizes.
        }
        else {
            v = minValue(state);   // Player O minimizes.
        }

        for (TicTacToeBoard s : successors(state)) {
            int sVal;
            if (turn == TicTacToeBoard.PLAYER_X) {
                sVal = minValue(s);
            }
            else {
                sVal = maxValue(s);
            }

            if (sVal == v) {
                bestMove = s.getPreviousMove();
            }
        }

        return bestMove;
    }

    /**
     * Return maximum value of game state.
     * @param state The current board state in the game.
     * @return The maximum value.
     */
    private int maxValue(TicTacToeBoard state) {
        if (terminalTest(state)) {
            return utility(state);
        }

        int v = -1000000;

        for (TicTacToeBoard s : successors(state)) {
            v = max(v, minValue(s));
        }

        return v;
    }

    /**
     * Return minimum value of game state.
     * @param state The current board state in the game.
     * @return The minimum value.
     */
    private int minValue(TicTacToeBoard state) {
        if (terminalTest(state)) {
            return utility(state);
        }

        int v = 1000000;

        for (TicTacToeBoard s : successors(state)) {
            v = min(v, maxValue(s));
        }

        return v;
    }
}
