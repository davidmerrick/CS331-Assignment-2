//package edu.oregonstate.eecs.cs331.assn2;

import static java.lang.Math.*;
import java.util.*;

/**
 * This class represents the module for minimax.
 * @author David Merrick
 *
 * public methods:
 * getNextMove(): returns next move
 * getPlayerType(): returns player type
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
        //Make a miniMax decision to determine the optimal next position
        nextPosition = decideMiniMax(state);

        return nextPosition;
    }

    /**
     * Returns the player type.
     */
    public int getPlayerType() {
        return MINIMAX_PLAYER;
    }

    private int getNextPlayer(int turn) {
        //return next player's turn.
        if (turn == TicTacToeBoard.PLAYER_X) {
            return TicTacToeBoard.PLAYER_O;
        }

        return TicTacToeBoard.PLAYER_X;
    }

    /**
     * Returns the getUtility of the game state
     * @param state The current board state in the game
     * @return 1 (Player X wins), -1 (Player O wins), or 0 (tie).
     */
    private int getUtility(TicTacToeBoard state) {
        int utility = 0;
        try {
            if (state.isWin(TicTacToeBoard.PLAYER_X)) {
                utility = 1;
            }
            else if (state.isWin(TicTacToeBoard.PLAYER_O)) {
                utility = -1;
            }
        }
        catch (Exception e) {
            //player is not a legal player index
        }

        return utility;
    }

    /**
     * Returns whether the game is over
     * @param state The current board state in the game
     * @return True if game over, otherwise false
     */
    private boolean isGameOver(TicTacToeBoard state) {
        try {
            return state.isGameOver();
        }
        catch (Exception e) {
            //Generic exception
        }

        return false;
    }

    /**
     * Returns the minimax decision
     * @param state The current board state in the game
     * @return The optimal move
     */
    private Position decideMiniMax(TicTacToeBoard state) {
        //Create a new Position object to represent the optimal move
        Position optimalMove = new Position();

        //Find out who's turn it is
        int turn = state.getTurn();

        //v represents the utility value of the game state
        int v;

        if (turn == TicTacToeBoard.PLAYER_X) {
            //It's Player X's turn,
            //Maximize Player X
            v = maxValue(state);
        } else {
            //It's Player O's turn,
            //Minimize Player O
            v = minValue(state);   // Player O minimizes.
        }

        //Call the successors method to generate list of successor board states
        //from the current state
        for (TicTacToeBoard b : successors(state)) {
            int bVal;
            if (turn == TicTacToeBoard.PLAYER_X) {
                bVal = minValue(b);
            } else {
                bVal = maxValue(b);
            }

            if (bVal == v) {
                optimalMove = b.getPreviousMove();
            }
        }

        return optimalMove;
    }

    /**
     * Generates the list of successor board states from the current state
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

                    try {
                        //Play the game.
                        s.setState(i, j, turn);
                    }
                    catch (Exception e) {
                        //Invalid player symbol
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

    /**
     * Returns the max value of the game state
     * @param state The current board state in the game
     * @return The max value
     */
    private int maxValue(TicTacToeBoard state) {
        if (isGameOver(state)) {
            return getUtility(state);
        }

        int v = -1000000;

        for (TicTacToeBoard s : successors(state)) {
            v = max(v, minValue(s));
        }

        return v;
    }

    /**
     * Returns min value of game state
     * @param state The current board state in the game
     * @return The min value
     */
    private int minValue(TicTacToeBoard state) {
        if (isGameOver(state)) {
            return getUtility(state);
        }

        int v = 1000000;

        for (TicTacToeBoard s : successors(state)) {
            v = min(v, maxValue(s));
        }

        return v;
    }
}
