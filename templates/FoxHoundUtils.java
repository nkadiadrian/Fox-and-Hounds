import java.util.Arrays;
import java.util.Scanner;

/**
 * A utility class for the fox hound program.
 *
 * It contains helper functions to check the state of the game
 * board and validate board coordinates and figure positions.
 */
public class FoxHoundUtils {

    // ATTENTION: Changing the following given constants can
    // negatively affect the outcome of the auto grading!

    /** Default dimension of the game board in case none is specified. */
    public static final int DEFAULT_DIM = 8;
    /** Minimum possible dimension of the game board. */
    public static final int MIN_DIM = 4;
    /** Maximum possible dimension of the game board. */
    public static final int MAX_DIM = 26;

    /** Symbol to represent a hound figure. */
    public static final char HOUND_FIELD = 'H';
    /** Symbol to represent the fox figure. */
    public static final char FOX_FIELD = 'F';

    /** Figure that can be used to convert a lateral coordinate to an equivalent letter when added
     * and a letter to a coordinate when subtracted in coordination with the respective char and int type casts.
     * 65 isn't used because the first index of a coordinate is 1 not 0.
     */
    public static final int CHAR_SHIFT = 64;

    // HINT Write your own constants here to improve code readability ...
    /**
     * A method that takes cartesian coordinates and converts and converts them
     * creating the equivalent coordinate in the letter and number type used by the game
     *
     * @param lat horizontal coordinate of a piece, measured from left to right of the board
     * @param vert vertical coordinate of a piece, measured from top to bottom of the board
     * @return equivalent letter then number coordinates in the form of a string
     */
    public static String makeCoordinate(int lat, int vert) {
        return Character.toString(CHAR_SHIFT + lat) + Integer.toString(vert);
    }


    /**
     * Function that creates the starting array for the game, defining the locations of all the pieces it creates
     *
     * @param dimension dimension of the board
     * @return string array with the starting location of each piece
     * @throws IllegalArgumentException if the board dimension isn't between {@value FoxHoundUtils#MIN_DIM} and
     * {@value FoxHoundUtils#MAX_DIM}.
     */
    public static String[] initialisePositions(int dimension) {
        if (dimension < MIN_DIM || dimension > MAX_DIM) {
            throw new IllegalArgumentException("You must have a board dimension from 4 to 26");
        }
        String[] positions = new String[(dimension / 2) + 1];
        //variable that is true if the middle of the bottom row, where the fox could be, is white
        boolean whiteMiddle =  (dimension/2 + 1 + dimension) % 2 == 0;
        //for loop initialises the hounds positions
        for (int i = 0; i < positions.length - 1; i++) {//
            positions[i] = makeCoordinate((2+2*i), 1);
        }
        //if-else clause where the fox's position is initialised
        if (whiteMiddle) {
            positions[positions.length - 1] = makeCoordinate((dimension/2 + 2), dimension);
        } else {
            positions[positions.length - 1] = makeCoordinate((dimension/2 + 1), dimension);
        }
        System.out.println(Arrays.toString(positions));
        return positions;
    }

    /**
     * Method used by moveLoop that actually moves the pieces when a valid movement command is made
     * Updates the players array with the result of the move command given to moveLoop
     *
     * @param players array of the current positions of the pieces on the board
     * @param movement a two-element array containing the interpretation of the movement command
     * @return updated player array
     */
    public static String[] switchPlace(String[] players, String[] movement) {
        String origin = movement[0];
        String dest   = movement[1];
        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(origin)) {
                players[i] = dest;
                return players;
            }
        }
        return players;
    }

    /**
     *  Helper method used by isWinner that checks if the conditions for a fox win are satisfied
     *
     * @param foxPos coordinates of the fox piece
     * @return true if the fox has won and false otherwise
     */
    public static boolean isFoxWin(String foxPos) {
        int foxVert = Integer.parseInt(foxPos.substring(1));
        if (foxVert == 1) {
            return true;
        }
        return false;
    }

    /**
     * Helper method used by isWinner that checks if the fox can make no valid moves, securing a hound win
     *
     * @param players array of the current positions of the pieces on the board
     * @param dimension dimension of the board
     * @return true if the hounds have won and false otherwise
     */
    public static boolean isHoundWin(String[] players, int dimension) {
        if (dimension < MIN_DIM || dimension > MAX_DIM) {
            throw new IllegalArgumentException("The dimension of the board must be between 4 and 26");
        }
        String foxPos = players[players.length -1 ];
        int foxLat = foxPos.charAt(0) - CHAR_SHIFT;
        int foxVert = Integer.parseInt(foxPos.substring(1));
        String move1 = makeCoordinate(foxLat + 1, foxVert + 1);
        String move2 = makeCoordinate(foxLat + 1, foxVert - 1);
        String move3 = makeCoordinate(foxLat - 1, foxVert + 1);
        String move4 = makeCoordinate(foxLat - 1, foxVert - 1);
        String[] possibleMoves = {move1, move2, move3, move4};
        for (int i = 0; i < possibleMoves.length; i++) {
            if (isValidMove(dimension, players, FOX_FIELD, foxPos, possibleMoves[i]))
                return false;
        }
        return true;
    }

    /**
     * Method that checks if the current player position configurations constitutes a victory
     * for the fox or the hounds.
     *
     * @param players array of the current positions of the pieces on the board
     * @param dimension dimension of the board
     * @return true if there is a winner and false otherwise
     */
    public static boolean isWinner(String[] players, int dimension) {
        if (isFoxWin(players[players.length-1])) {
            System.out.println("The Fox wins!");
            return true;
        } else if (isHoundWin(players, dimension)) {
            System.out.println("The hounds win!");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method used to check if a proposed move is valid
     *
     * @param dim dimension of the board
     * @param players array of the current positions of the pieces on the board
     * @param figure the figure which would be moved
     * @param origin the coordinate of the piece being commanded to move
     * @param dest the coordinate which the piece would be moved to
     * @return true if the move is valid and false otherwise
     */
    public static boolean isValidMove(int dim, String[] players, char figure, String origin, String dest) {
        boolean isValid = false;
        int originLat = origin.charAt(0) - CHAR_SHIFT;
        int originVert = Integer.parseInt(origin.substring(1));
        int destLat = dest.charAt(0) - CHAR_SHIFT;
        int destVert = Integer.parseInt(dest.substring(1));

        //Checks if there's already a piece at the target coordinate and returns false if that is the case
        for (int place = 0; place < players.length; place++) {
            if (players[place].equals(FoxHoundUtils.makeCoordinate(destLat, destVert))) {
                return isValid;
            }
        }

        //Checks if the move type is valid when it is the fox's turn to move
        if (figure == FOX_FIELD) {
            if (players[players.length - 1].equals(makeCoordinate(originLat, originVert)) &&
                    destLat <= dim && destVert <= dim &&
                    (destLat  == originLat +  1 || destLat  == originLat  - 1) &&
                    (destVert == originVert + 1 || destVert == originVert - 1)) {
                isValid = true;
            }
        }

        //Checks if the move type is valid when it is the hounds' turn to move
        if (figure == HOUND_FIELD) {
            if (destLat <= dim && destVert <= dim &&
                    (destLat == originLat + 1 || destLat == originLat - 1) &&
                    destVert == originVert + 1) {
                boolean isHound = false;
                for (int place = 0; place < players.length - 1 && !isHound; place++) {
                    if (players[place].equals(FoxHoundUtils.makeCoordinate(originLat, originVert))) {
                        isValid = true;
                        isHound = true;
                    }
                }
            }
        }
        return isValid;
    }

    /**
     * Method used at the very beginning of the program to ask for a dimension and use the response
     * to set the dimension variable appropriately
     *
     * @param stdin input stream - used to specify a dimension value
     *
     * dimension is set to {@value FoxHoundUtils#DEFAULT_DIM} if the value obtained from the scanner isn't recognised
     * or isn't between {@value FoxHoundUtils#MIN_DIM} and {@value FoxHoundUtils#MAX_DIM}.
     *
     * @return a value for the dimension of the board to be used throughout the program
     */
    public static int validateDim(Scanner stdin) {
        System.out.println("Please enter the dimensions of the board");
        int dimension = stdin.nextInt();
        if (!(dimension >= MIN_DIM && dimension <= MAX_DIM)) {
            dimension = DEFAULT_DIM;
        }
        return dimension;
    }
}
