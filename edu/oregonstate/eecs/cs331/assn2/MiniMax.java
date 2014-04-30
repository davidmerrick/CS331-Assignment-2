/**
 * This class represents the module for minimax.
 * @author David Merrick
 *
 * Code structure based on:
 * http://youtu.be/OkP8BAwfO24
 * http://classes.engr.oregonstate.edu/eecs/spring2014/cs331/slides/AdversarialSearch1.2pp.pdf
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
    //We will use the convention that the X player is the maximizing player and the O player is the minimizing player.
    int MAX = TicTacToeBoard.PLAYER_X;
    int MIN = TicTacToeBoard.PLAYER_O;

    /**
     * Returns the next move.
     * @param state The current board state in the game
     * @return The next position
     */
    public Position getNextMove(TicTacToeBoard state) {
        //Make a miniMax decision to determine the optimal next position
        return decideMiniMax(state);
    }

    /**
     * Returns the player type
     */
    public int getPlayerType() {
        return MINIMAX_PLAYER;
    }

    /**
     * Returns the integer value of the next player to determine the turn
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
            //state.isWin can throw an exception in this case:
            //player is not a legal player index
        }

        //It was a tie. Meh.
        return 0;
    }

    /**
     * Checks whether the game is over
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

        //value represents the maximum or minimum possible utility value of any successor state
        int value;

        //Find out which player's turn it is. This determines whether to maximize or minimize.
        if (turn == MAX) {
            //MAX's turn, maximize; Find the maximum possible value recursively.
            value = maxValue(state);
        } else {
            //MIN's turn, minimize; Find the minimum possible value recursively.
            value = minValue(state);
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

            if (bUtil == value) {
                //Found the optimal move and it's the previous one. Break and return it.
                optimalMove = b.getPreviousMove();
                break;
            }
        }

        return optimalMove;
    }

    /**
     * Successor method
     *
     * This method takes the current state of the game and generates a list of
     * all the successors that can be reached within one move of the current state.
     * @param state The current board state in the game
     * @return The list of successor board states
     */
    private List<TicTacToeBoard> getSuccessors(TicTacToeBoard state) {
        //Create a new list for the successors
        List<TicTacToeBoard> successorList = new ArrayList<TicTacToeBoard>();

        //Generate the successor board states and append them to the successor list
        for (int row=0; row<3; row++) {
            for (int col=0; col<3; col++) {
                //Only act if the square is blank
                if (state.getState(row, col) == TicTacToeBoard.BLANK) {
                    //Clone the current board so we can append it to the
                    //successorList without interfering with it
                    TicTacToeBoard b = (TicTacToeBoard) state.clone();

                    //Get current player's turn
                    int turn = state.getTurn();

                    try {
                        //"play" the game ahead
                        b.setState(row, col, turn);
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

        //Set value to "+infinity" (Just needs to be greater than 1)
        int value = 2;

        //Iterate through the successor list
        //Recursively find the minimum of the max values of all the successor states
        for (TicTacToeBoard b : getSuccessors(state)) {
            value = Math.min(value, maxValue(b));
        }

        return value;
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

        //Set value to "-infinity" (Just needs to be less than -1)
        int value = -2;

        //Iterate through the successor list
        //Recursively find the maximum of the min values of all the successor states
        for (TicTacToeBoard b : getSuccessors(state)) {
            value = Math.max(value, minValue(b));
        }

        return value;
    }
}
