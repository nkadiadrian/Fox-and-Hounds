import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A utility class for the fox hound program.
 *
 * It contains helper functions for all file input / output
 * related operations such as saving and loading a game.
 */
public class FoxHoundIO {

    /**Character identified by the program as an error code for a failed loading operation*/
    public static final char ERROR_CHAR = '#';

    /**Length of the common 8x8 board player position array
     * Any other array length isn't compatible for saving or loading*/
    public static final int PIECE_NUM = 5;

    /**
     * A helper function used by loadGame
     * Determines if the file constitutes valid game data, element by element.
     *
     * @param gameData data from file read to a list of strings to be checked for validity before being loaded
     * @return whether the file's Data is valid or not
     */
    public static boolean isValidData(String[] gameData) {
        char turn = gameData[0].charAt(0);
        if (turn != FoxHoundUtils.FOX_FIELD && turn != FoxHoundUtils.HOUND_FIELD) {
            return false;
        }
        String tray = gameData[1]; // Program won't pass tests if this line is deleted for some reason
        //Checks if the data defining character positions is in the right format
        for (int i = 1; i < gameData.length; i++) {
            if (gameData[i].charAt(0) <= FoxHoundUtils.CHAR_SHIFT
                    || gameData[i].charAt(0) > FoxHoundUtils.CHAR_SHIFT + FoxHoundUtils.DEFAULT_DIM){
                return false;
            }
            try {
                if (Integer.parseInt(gameData[i].substring(1)) > FoxHoundUtils.DEFAULT_DIM) {
                    return false;
                }
            } catch (Exception e) {
                System.err.println(e);
                return false;
            }
        }
        return true;
    }

    /**
     * The method that attempts to load a saved game from a file location
     *
     * @param players array of the current positions of the pieces on the board
     * @param loadFile path pointing to the file location where the data is to be loaded from
     * @return character indicating who's turn it is to play
     */
    public static char loadGame(String[] players, Path loadFile) {
        if (players.length != PIECE_NUM) {
            throw new IllegalArgumentException("You can only load games when you have a board dimension of 8");
        }
        if (loadFile.equals(null)) {
            throw new NullPointerException("There was no path entered");
        }
        String[] playersTemp = players.clone();
        try {
            String gameLine = Files.readString(loadFile);
            String[] gameData = gameLine.split(" ");
            char turn = gameData[0].charAt(0);
            if (!isValidData(gameData)) {
                throw new IllegalArgumentException("The file's format is wrong");
            }
            for (int i = 1; i < gameData.length; i++) {
                players[i-1] = gameData[i];
            }
            return turn;
        } catch (Exception e) {
            System.err.println(e);
            players = playersTemp.clone();
            return ERROR_CHAR;
        }

    }

    /**
     * Method used to save a game to a specified file location so it can be played later
     *
     * @param players array of the current positions of the pieces on the board
     * @param nextMove character indicating whose turn it is to play at the time of saving the game
     * @param saveFile path pointing to the file location where the data is to be saved to
     * @return true if the process of saving was successful and false otherwise
     */
    public static boolean saveGame(String[] players, char nextMove, Path saveFile) {
        if (saveFile.equals(null)) {
            throw new NullPointerException("There was no path entered");
        }
        if (players.length != PIECE_NUM) {
            throw new IllegalArgumentException("You can only save games with a board dimension of 8");
        }
        try {
            if (Files.exists(saveFile)) {
                return false;
            }
            String content = "" + nextMove;
            for (int i = 0; i < players.length; i++) {
                content += " " + players[i];
            }
            Files.write(saveFile, content.getBytes());
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
        return true;
    }
}