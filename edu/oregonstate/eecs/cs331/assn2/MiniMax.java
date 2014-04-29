/**
 * This class represents the module for minimax.
 * @author David Merrick
 *
 * public methods:
 * getNextMove(): returns next move
 * getPlayerType(): returns player type
 *
 */

import java.util.*;
import static java.lang.Math.*;

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
     * @return The next position
     */
    public Position getNextMove(TicTacToeBoard state) {
        //Make a miniMax decision to determine the optimal next position
        nextPosition = decideMiniMax(state);

        return nextPosition;
    }

    /**
     * Returns the player type
     */
    public int getPlayerType() {
        return MINIMAX_PLAYER;
    }

    /**
     * Returns the integer value of the next player
     */
    private int getNextPlayer(int turn) {
        //return next player's turn.
        if (turn == TicTacToeBoard.PLAYER_X) {
            return TicTacToeBoard.PLAYER_O;
        }

        return TicTacToeBoard.PLAYER_X;
    }

    /**
     * Returns the utility value of the game state.
     * Only called when game is in a terminal state.
     * @param state The current board state in the game
     * @return 1 (Player X wins), -1 (Player O wins), or 0 (tie).
     */
    private int getUtility(TicTacToeBoard state) {
        int utility = 0;
        try {
            if (state.isWin(TicTacToeBoard.PLAYER_X)) {
                utility = 1;
            } else if (state.isWin(TicTacToeBoard.PLAYER_O)) {
                utility = -1;
            }
        } catch (Exception e) {
            //player is not a legal player index
        }

        return utility;
    }

    /**
     * Returns whether the game is over
     * @param state The current board state in the game
     * @return Boolean. True if game over, otherwise false
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

        //Get current player's turn
        int turn = state.getTurn();

        //v represents the utility value of the game state
        int v;

        if (turn == TicTacToeBoard.PLAYER_X) {
            //Player X's turn, maximize player X
            v = maxValue(state);
        } else {
            //Player O's turn, minimize Player O
            v = minValue(state);
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

        //Generate the successors and append them to the successor list.
        for (int i=0; i<3; i++) { //Loop through the rows
            for (int j=0; j<3; j++) { //Loop through the columns
                //Only generate the list of successors initially
                if (state.getState(i, j) == TicTacToeBoard.BLANK) {
                    //Clone the current board so we can append it to the
                    //successorList without interfering with it
                    TicTacToeBoard b = (TicTacToeBoard) state.clone();

                    //Get current player'b turn
                    int turn = state.getTurn();

                    try {
                        //Set the game state
                        b.setState(i, j, turn);
                    } catch (Exception e) {
                        //Invalid player symbol
                    }

                    //Set the next player.
                    int nextTurn = getNextPlayer(turn);
                    b.setTurn(nextTurn);

                    //Append board to list of successors.
                    successorList.add(b);
                }
            }
        }

        return successorList;
    }

    /**
     * Returns min value of game state
     * @param state The current board state in the game
     * @return The min value
     */
    private int minValue(TicTacToeBoard state) {
        //Test if the game is over in current state
        //If so, return utility value
        if (isGameOver(state)) {
            return getUtility(state);
        }

        //Set v to a "large" value (Just needs to be greater than 1)
        int v = 2;

        //Iterate through the successor list, set v to the min value
        for (TicTacToeBoard b : successors(state)) {
            v = min(v, maxValue(b));
        }

        return v;
    }

    /**
     * Returns the max value of the game state
     * @param state The current board state in the game
     * @return The max value
     */
    private int maxValue(TicTacToeBoard state) {
        //Test if the game is over in current state
        //If so, return utility value
        if (isGameOver(state)) {
            return getUtility(state);
        }

        //Set v to a "small" value (Just needs to be less than -1)
        int v = -2;

        //Iterate through the successor list, set v to the max value
        for (TicTacToeBoard b : successors(state)) {
            v = max(v, minValue(b));
        }

        return v;
    }
}
