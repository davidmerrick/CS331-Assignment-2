/**
 * This class represents the module for minimax.
 * @author David Merrick
 *
 * Code structure based on:
 * http://youtu.be/OkP8BAwfO24
 *
 * public methods:
 * getNextMove(): returns next move
 * getPlayerType(): returns player type
 *
 * The premise:
 *
 * The computer is MAX. Human is MIN.
 * So we want to maximize each node's utility value if it's MAX's turn.
 * And minimize each node's utility value if it's MIN's turn.
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

    //The players
    int MAX = TicTacToeBoard.PLAYER_X;
    int MIN = TicTacToeBoard.PLAYER_O;

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
        if (turn == MAX) {
            return MIN;
        }

        return MAX;
    }

    /**
     * Returns the utility value of the game state.
     * Only called when game is in a terminal state.
     * The purpose is to give a value to a terminal state.
     * High values are good for MAX and bad for MIN.
     * @param state The current board state in the game
     * @return 1 (MAX wins), -1 (MIN wins), or 0 (tie).
     */
    private int getUtility(TicTacToeBoard state) {
        try {
            if (state.isWin(MAX)) {
                //MAX wins. Good!
                return 1;
            } else if(state.isWin(MIN)) {
                //MIN wins. Bad!
                return -1;
            }
        } catch (Exception e) {
            //player is not a legal player index
        }

        //It was a tie
        return 0;
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
        //Default to false
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

        //v represents the maximum or minimum possible utility value of any successor state
        int v;

        //Find out which player's turn it is. This determines whether to maximize or minimize.
        if (turn == MAX) {
            //MAX's turn, maximize; Find the maximum possible value recursively.
            v = maxValue(state);
        } else {
            //MIN's turn, minimize; Find the minimum possible value recursively.
            v = minValue(state);
        }

        //Call the successors method to generate a list of possible successor board states
        //from the current state
        for (TicTacToeBoard b : getSuccessors(state)) {
            //bUtil represents the utility value of board state b
            int bUtil;

            //Find out which player's turn it is. This determines whether to maximize or minimize.
            if (turn == MAX) {
                //Minimize the maximum utility value of player X. Find this value recursively.
                bUtil = minValue(b);
            } else {
                //Maximize the minimum utility value of player O. Find this value recursively.
                bUtil = maxValue(b);
            }

            if (bUtil == v) {
                //Found the optimal move and it's the previous one. Break and return it.
                optimalMove = b.getPreviousMove();
                break;
            }
        }

        return optimalMove;
    }

    /**
     * Successor function
     * Generates the list of successor board states from the current state
     * This list has all possible moves
     * @param state The current board state in the game
     * @return The list of successor board states
     */
    private List<TicTacToeBoard> getSuccessors(TicTacToeBoard state) {
        //Create a new list for the successors
        List<TicTacToeBoard> successorList = new ArrayList<TicTacToeBoard>();

        //Generate the successor board states and append them to the successor list.
        for (int i=0; i<3; i++) { //Rows
            for (int j=0; j<3; j++) { //Columns
                //Only generate the list of successors initially (when board is blank)
                if (state.getState(i, j) == TicTacToeBoard.BLANK) {
                    //Clone the current board so we can append it to the
                    //successorList without interfering with it
                    TicTacToeBoard b = (TicTacToeBoard) state.clone();

                    //Get current player's turn
                    int turn = state.getTurn();

                    try {
                        //"play" the game ahead
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
     * Returns min utility value of game state
     * @param state The current board state in the game
     * @return The min value
     */
    private int minValue(TicTacToeBoard state) {
        //If state is a terminal or leaf node, then return utility(state)
        if (isGameOver(state)) {
            return getUtility(state);
        }

        //Set v to "+infinity" (Just needs to be greater than 1)
        int v = 2;

        //Iterate through the successor list
        //Recursively find the minimum of the max values of all the successor states
        for (TicTacToeBoard b : getSuccessors(state)) {
            v = min(v, maxValue(b));
        }

        return v;
    }

    /**
     * Returns the max utility value of the game state
     * @param state The current board state in the game
     * @return The max value
     */
    private int maxValue(TicTacToeBoard state) {
        //If state is a terminal or leaf node, then return utility(state)
        if (isGameOver(state)) {
            return getUtility(state);
        }

        //Set v to "-infinity" (Just needs to be less than -1)
        int v = -2;

        //Iterate through the successor list
        //Recursively find the maximum of the min values of all the successor states
        for (TicTacToeBoard b : getSuccessors(state)) {
            v = max(v, minValue(b));
        }

        return v;
    }
}
