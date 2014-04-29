Tic-tac-toe is a classic two-player, turn-based game in which players try to get three Xs or Os in a row on a 3x3 board. In this assignment you will convert a two-player game of tic-tac-toe into a one-player version in which a human player gets to play against a very smart computer opponent. You will be implementing the computer opponent using the MiniMax algorithm discussed in class.

Description

You will calculate the next move for the computer player using the MiniMax algorithm. We will use the convention that the X player is the maximizing player and the O player is the minimizing player. Since the command line allows you to select which player moves first (ie. the human or computer), your code must be able to handle the scenario where the human moves first and the scenario where the human moves second.
The game of tic-tac-toe is small enough that we can generate the entire game tree while doing the MiniMax search. As a result, you do not need to create an evaluation function for non-terminal nodes. What you will need to create is a utility function which returns the utility at terminal states.

You will also need to create a successor function. This function takes the current state of the game and generates all the successors that can be reached within one move of the current state. Both the utility function and the successor function need to be written. You get to decide what these functions should look like. Once these two functions are in place, you can begin coding the actual MiniMax algorithm.

Code

You can use Java, C++ or C. If you choose to use a programming language other than these three, please email the TA for approval. For the assignment, you must implement a GUI for the tic-tac-toe game. If you are programming in Java, I have provided code for a GUI below. Your main function should take the following command line arguments:

The command line arguments are: [Player 1 Type] [Player 2 Type] where the player type can be one of:

human
random
minimax
Player 1 is the X player who always goes first while player 2 is the O player. You can currently play against either another human opponent or the random opponent that selects the next move randomly from among the empty squares. Running against a minimax opponent will currently cause the program to crash since the code is incomplete.
