import java.nio.file.Path;
import java.util.Scanner;
import java.util.Objects;
/**
 * A utility class for the fox hound program.
 *
 * It contains helper functions for all user interface related
 * functionality such as printing menus and displaying the game board.
 */
public class FoxHoundUI {

    /** Number of main menu entries. */
    private static final int MENU_ENTRIES = 4;
    /** Main menu display string. */
    private static final String MAIN_MENU =
            "\n1. Move\n2. Save Game\n3. Load Game\n4. Exit\n\nEnter 1 - 4:";

    /** Menu entry to select a move action. */
    public static final int MENU_MOVE = 1;
    /** Menu entry to save a game */
    public static final int MENU_SAVE = 2;
    /**Menu entry to load a game */
    public static final int MENU_LOAD = 3;
    /** Menu entry to terminate the program. */
    public static final int MENU_EXIT = 4;


    /**Print the chessboard with simple dots
     *
     * @param players array of the current positions of the pieces on the board
     * @param dimension dimension of the board
     */
    public static void displayBoard(String[] players, int dimension) {
        //A number used as a multiplier to adjust spacing when the dimension is greater than or equal to 10 or less than 10
        int padder = dimension/10 + 1;
        //Creates the alphabetic header and footer for the board representing dimension across
        String title = "";
        for (int i = 1; i <= dimension; i++) {
            title = title + (char)(FoxHoundUtils.CHAR_SHIFT + i);
        }
        System.out.format(" ".repeat(padder) + " %s  %n%n", title);
        //for loop creating the rows of the board, using player coordinates to determine what character to print
        for (int vert = 1; vert <= dimension; vert++) { //
            System.out.format("%0"+padder+"d ", vert);
            for (int lat = 1; lat <= dimension; lat++) {
                boolean isHound = false;
                char positionVal = '.';
                for (int place = 0; place < players.length - 1 && !isHound; place++) {
                    if (players[place].equals(FoxHoundUtils.makeCoordinate(lat, vert))) {
                        positionVal = FoxHoundUtils.HOUND_FIELD;
                        isHound = true;
                    }
                }
                if (players[players.length - 1].equals(FoxHoundUtils.makeCoordinate(lat, vert))) positionVal = FoxHoundUtils.FOX_FIELD;
                System.out.print(positionVal);
            }
            System.out.format(" %0"+padder+"d%n", vert);
        }
        System.out.format("%n" + " ".repeat(padder) + " %s%n%n", title);
    }

    /**
     * Print the chessboard in a fancier and clearer format
     * @param players array of the current positions of the pieces on the board
     * @param dimension dimension of the board
     */
    public static void fancyDisplayBoard(String[] players, int dimension) {
        //A number used as a multiplier to adjust spacing when the dimension is greater than or equal to 10 or less than 10
        int padder = dimension/10 + 1;
        String title = "";
        //Creates the alphabetic header and footer for the board representing dimension across
        for (int i = 1; i <= dimension; i++) {
            title = title + "   " + Character.toString(FoxHoundUtils.CHAR_SHIFT + i);
        }
        //Creates horizontal divider based on board dimension to aid clarity
        String divider = " ".repeat(padder) + " " + "|===".repeat(dimension) + '|';
        System.out.format(" ".repeat(padder) + "%s%n%s%n", title, divider);
        //for loop creating the rows of the board, using player coordinates to determine what character to print
        for (int vert = 1; vert <= dimension; vert++) {
            System.out.format("%0"+padder+"d ", vert);
            for (int lat = 1; lat <= dimension; lat++) { //
                boolean isHound = false;
                String positionVal = "|   ";
                for (int place = 0; place < players.length - 1 && !isHound; place++) {
                    if (players[place].equals(FoxHoundUtils.makeCoordinate(lat, vert))) {
                        positionVal = "| H ";
                        isHound = true;
                    }
                }
                if (players[players.length - 1].equals(FoxHoundUtils.makeCoordinate(lat, vert))) positionVal = "| F ";
                System.out.print(positionVal);
            }
            System.out.format("| %0"+padder+"d%n", vert);
            System.out.println(divider);
        }
        System.out.format(" ".repeat(padder)+ "%s%n%n", title);
        //System.out.println(Arrays.toString(players) + "\n"); //Remember to comment out later to pass checks
    }

    /**
     * Print the main menu and query the user for an entry selection.
     *
     * @param figureToMove the figure type that has the next move
     * @param stdin a Scanner object to read user input from
     * @return a number representing the menu entry selected by the user
     * @throws IllegalArgumentException if the given figure type is invalid
     * @throws NullPointerException if the given Scanner is null
     */
    public static int mainMenuQuery(char figureToMove, Scanner stdin) {
        Objects.requireNonNull(stdin, "Given Scanner must not be null");
        if (figureToMove != FoxHoundUtils.FOX_FIELD
                && figureToMove != FoxHoundUtils.HOUND_FIELD) {
            throw new IllegalArgumentException("Given figure field invalid: " + figureToMove);
        }

        String nextFigure =
                figureToMove == FoxHoundUtils.FOX_FIELD ? "Fox" : "Hounds";

        int input = -1;
        while (input == -1) {
            System.out.println(nextFigure + " to move");
            System.out.println(MAIN_MENU);

            boolean validInput = false;
            if (stdin.hasNextInt()) {
                input = stdin.nextInt();
                validInput = input > 0 && input <= MENU_ENTRIES;
            }

            if (!validInput) {
                System.out.println("Please enter valid number.");
                input = -1; // reset input variable
            }

            stdin.nextLine(); // throw away the rest of the line
        }

        return input;
    }

    /** A helper function used by positionQuery to check if the moves
     * requested represent positions that exist on the current board
     *
     * @param dimension dimension of the board
     * @param coordinate A position on the board represented by a letter followed by a number
     * @return whether the position selected exists on the current board or not.
     */
    public static boolean inRange(int dimension, String coordinate) {
        //try and catch prevents the program from crashing instead of simply denying a falsely formatted move
        try {
            /**Checks if a validly formatted letter and number combination
             * represent a coordinate that exists on the current board */
            return (coordinate.charAt(0) - FoxHoundUtils.CHAR_SHIFT <= dimension
                    && Integer.parseInt(coordinate.substring(1)) <= dimension);
        } catch (NumberFormatException e) {
            System.err.println(e);
            return false;
        }
    }

    /**
     * The method used to obtain a movement command from the user
     *
     * @param dim dimensions of the board
     * @param stdin input stream - used to specify a movement command requesting from the user
     * @return a valid pair of movement commands
     */
    public static String[] positionQuery(int dim, Scanner stdin) {
        int pairLength = 0;
        //Loop doesn't end till valid movement command is given
        while (true) {
            System.out.println("Provide origin and destination coordinates.");
            System.out.println("Enter two positions between A1-" + FoxHoundUtils.makeCoordinate(dim, dim) +":\n");
            String[] pair = stdin.nextLine().split(" ");
            pairLength = pair.length;
            if (pairLength == 2) {
                if (inRange(dim, pair[0]) && inRange(dim, pair[1])) {
                    return pair;
                }
            }
            System.err.println("ERROR: Please enter valid coordinate pair separated by space.");
        }
    }

    /**
     * The method used to get file names from the user
     * to save or load a game
     * @param stdin input stream - used to specify a file name asked from the user
     * @return a path variable pointing to a text file location
     * @throws NullPointerException if the given Scanner is null
     */
    public static Path fileQuery(Scanner stdin) {
        Objects.requireNonNull(stdin, "Given Scanner must not be null");
        System.out.println("Enter file path:");
        Path filePath = Path.of(stdin.nextLine());
        return filePath;
    }
}
