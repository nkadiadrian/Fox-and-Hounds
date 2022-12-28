import java.util.Scanner;

/**
 * The Main class of the fox hound program.
 *
 * It contains the main game loop where main menu interactions
 * are processed and handler functions are called.
 */
public class FoxHoundGame {

    /**
     * This scanner can be used by the program to read from
     * the standard input. 
     *
     * Every scanner should be closed after its use, however, if you do
     * that for StdIn, it will close the underlying input stream as well
     * which makes it difficult to read from StdIn again later during the
     * program.
     *
     * Therefore, it is advisable to create only one Scanner for StdIn 
     * over the course of a program and only close it when the program
     * exits. Additionally, it reduces complexity. 
     */
    private static final Scanner STDIN_SCAN = new Scanner(System.in);

    /**
     * Swap between fox and hounds to determine the next
     * figure to move.
     *
     * @param currentTurn last figure to be moved
     * @return next figure to be moved
     */
    private static char swapPlayers(char currentTurn) {
        if (currentTurn == FoxHoundUtils.FOX_FIELD) {
            return FoxHoundUtils.HOUND_FIELD;
        } else {
            return FoxHoundUtils.FOX_FIELD;
        }
    }

    /**
     *The loop that handle the piece's movements.
     * It updates the player's positions instructed by a valid command.
     * Loop will not break until valid movement selection is made.
     *
     * @param dim dimensions of the board
     * @param players array of the current positions of the pieces on the board
     * @param turn turn of the player making the move
     * @param stdin input stream - used to request a move command.
     * @return updated player array
     */

    private static String[] moveLoop(int dim, String[] players, char turn, Scanner stdin) {
        boolean gotMove = false;
        String[] movement = new String[2];
        while(!gotMove) {
            movement = FoxHoundUI.positionQuery(dim, stdin);
            if (FoxHoundUtils.isValidMove(dim, players, turn, movement[0], movement[1])) {
                gotMove = true;
            }
        }
        return FoxHoundUtils.switchPlace(players, movement);
    }

    /**
     * The loop used to save games to be accessed later.
     * It saves the data required for a game to be laid out in a named file.
     *
     * @param players array of the current positions of the pieces on the board
     * @param turn last figure to be used
     * @param stdin input stream - used to specify a file name
     */

    private static void saveLoop(String[] players, char turn, Scanner stdin) {
        boolean saveSuccessful = FoxHoundIO.saveGame(players, turn, FoxHoundUI.fileQuery(stdin));
        if (!saveSuccessful) {
            System.err.println("ERROR: Saving file failed.");
        }
    }

    /** The loop used to load previous games.
     * It replaces the current game with a game from a chosen save file.
     *
     * @param players array of the current positions of the pieces on the board
     * @param turn last figure to be used
     * @param stdin input stream - used to specify a file name
     */
    private static void loadLoop(String[] players, char turn, Scanner stdin) {
        char playerStatus = FoxHoundIO.loadGame(players, FoxHoundUI.fileQuery(stdin));
        if (playerStatus == FoxHoundIO.ERROR_CHAR) {
            System.err.println("ERROR: Loading from file failed.");
        } else {
            turn = playerStatus;
        }
    }

    /**
     * The main loop of the game. Interactions with the main
     * menu are interpreted and executed here.
     *
     * @param dim the dimension of the game board
     * @param players current position of all figures on the board in board coordinates
     */
    private static void gameLoop(int dim, String[] players) {

        // start each game with the Fox
        char turn = FoxHoundUtils.FOX_FIELD;
        boolean exit = false;
        while(!exit) {
            System.out.println("\n#################################\n");
            //The display function for the board table, can be displayBoard or fancyDisplayBoard
            FoxHoundUI.fancyDisplayBoard(players, dim);
            int choice = FoxHoundUI.mainMenuQuery(turn, STDIN_SCAN);

            // handle menu choice
            switch(choice) {
                case FoxHoundUI.MENU_MOVE:
                    moveLoop(dim, players, turn, STDIN_SCAN);
                    exit = FoxHoundUtils.isWinner(players, dim);
                    turn = swapPlayers(turn);
                    break;
                case FoxHoundUI.MENU_SAVE:
                    saveLoop(players, turn, (STDIN_SCAN));
                    break;
                case FoxHoundUI.MENU_LOAD:
                    loadLoop(players, turn, STDIN_SCAN);
                    break;
                case FoxHoundUI.MENU_EXIT:
                    exit = true;
                    break;
                default:
                    System.err.println("ERROR: invalid menu choice: " + choice);
            }
        }
    }

    /**
     * Entry method for the Fox and Hound game.
     *
     * The dimensions of the game board can be passed in as
     * optional command line argument.
     *
     * If no argument is passed, a default dimension of
     * {@value FoxHoundUtils#DEFAULT_DIM} is used.
     *
     * Dimensions must be between {@value FoxHoundUtils#MIN_DIM} and
     * {@value FoxHoundUtils#MAX_DIM}.
     *
     * @param args contain the command line arguments where the first can be
     * board dimensions.
     */
    public static void main(String[] args) {
        int dimension = FoxHoundUtils.validateDim(STDIN_SCAN);

        String[] players = FoxHoundUtils.initialisePositions(dimension);
        gameLoop(dimension, players);

        // Close the scanner reading the standard input stream
        STDIN_SCAN.close();
    }
}