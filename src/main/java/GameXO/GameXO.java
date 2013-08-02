package GameXO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * User: S.Rogachevsky
 * Date: 22.07.13
 * Time: 20:02
 */
public class GameXO extends JFrame implements ActionListener {

    public final static int DEFAULT_MAX_STEPS = 9;
    public final static int DEFAULT_DIMENSION = 3;
    public final static int DEFAULT_WIN_POINTS = 3;
    public final static int IMG_SIZE_PX = 110;
    public final static int DEFAULT_MATRIX_VALUE = -1;
    public final static int MATRIX_VALUE_X = 1;
    public final static int MATRIX_VALUE_0 = 0;
    private static int maxSteps;
    private static int dimension;
    private static int winPoints;
    private boolean gameSuccessfulFinished = false;
    private int step = 0;
    private int[][] matrix;
    private HashMap<String, JButton> buttons = new HashMap<>();

    /**
     * Start method
     *
     * @param args - array 0..3 [dimension of filed, max count of steps for win, ]
     */
    public static void main(String[] args) {
        new GameXO(args).setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isLastStep() || gameSuccessfulFinished) {
            showMessageDialog(null, "Game is over!");
            return;
        }
        String actionCommand = e.getActionCommand();
        try {
            setMatrix(actionCommand);
        } catch (GameException ge) {
            System.out.println(" Exception = " + ge.getMessage());
            return;
        }
        step++;
    }

    /**
     * Private constructor
     */
    private GameXO(String... args) {
        super("GameXO");
        initGameOptions(args);
        init();
        setLayout(new GridLayout(dimension, dimension));
        setSize(IMG_SIZE_PX * dimension, IMG_SIZE_PX * dimension);
        setResizable(false);
    }

    /**
     * Initialize main options of the game
     *
     * @param args parameters for the game
     *             possible
     *             dimension - number of field in row /column
     *             maxSteps - maximum steps until game finished
     *             winPoints - count of signs to win
     */
    private static void initGameOptions(String... args) {

        dimension = DEFAULT_DIMENSION;
        maxSteps = DEFAULT_MAX_STEPS;
        winPoints = DEFAULT_WIN_POINTS;
        for (String arg : args) {
            if (arg.startsWith("dimension"))
                dimension = Integer.parseInt(arg.substring(arg.indexOf("=") + 1));
            if (arg.startsWith("maxSteps"))
                maxSteps = Integer.parseInt(arg.substring(arg.indexOf("=") + 1));
            if (arg.startsWith("winPoints"))
                winPoints = Integer.parseInt(arg.substring(arg.indexOf("=") + 1));


        }
    }

    /**
     * init game fields
     */
    private void init() {
        matrix = new int[dimension][dimension];
        for (int i = 0; i < dimension * dimension; i++) {
            buttons.put(String.valueOf(i), new JButton());
        }

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                matrix[i][j] = DEFAULT_MATRIX_VALUE;

        JButton jButton;
        for (int i = 0; i < buttons.size(); i++) {
            jButton = buttons.get(String.valueOf(i));
            jButton.addActionListener(this);
            jButton.setActionCommand(String.valueOf(i));
            add(jButton);
        }
    }

    /**
     * checks the condition of last step
     *
     * @return boolean if step is last for the game
     */
    private boolean isLastStep() {
        return step == dimension * dimension || step >= maxSteps;
    }

    /**
     * return the image of sign per step
     *
     * @return image of sign
     */
    private ImageIcon getIcon() {
        Class cl = this.getClass();
        if (isStepX())
            return new ImageIcon(cl.getResource("/images/x.jpg"));
        else
            return new ImageIcon(cl.getResource("/images/0.jpg"));
    }

    /**
     * checks if the step for X
     *
     * @return step is X
     */
    private boolean isStepX() {
        return step % 2 == 0;
    }

    /**
     * Checks sign and set the result of the step
     *
     * @param place - number of matrix field
     * @throws GameException game exception
     */
    private void setMatrix(String place) throws GameException {
        int iPlace = Integer.parseInt(place);
        int x = iPlace / dimension;
        int y = iPlace % dimension;
        if (matrix[x][y] != DEFAULT_MATRIX_VALUE) {
            showMessageDialog(null, "Choose another place!");
            throw new GameException("Already has a sign");
        }
        if (isStepX())
            matrix[x][y] = MATRIX_VALUE_X;
        else
            matrix[x][y] = MATRIX_VALUE_0;
        buttons.get(place).setIcon(getIcon());
        alertWin(x, y);
    }


    //TODO: refactor

    /**
     * Check the result of the step and alerts if step won
     *
     * @param x - step's x coordinate of matrix
     * @param y - step's y coordinate of matrix
     */
    private void alertWin(int x, int y) {
        int currentPointValue = matrix[x][y];
        int sameSignCount;

        //check horizontal
        sameSignCount = 1;
        for (int i = x - 1; i >= 0; i--)
            if (currentPointValue == matrix[i][y]) {
                sameSignCount++;
            } else {
                break;
            }
        for (int i = x + 1; i < dimension; i++)
            if (currentPointValue == matrix[i][y]) {
                sameSignCount++;
            } else {
                break;
            }
        gameSuccessfulFinished |= sameSignCount >= winPoints;


        //check vertical
        sameSignCount = 1;
        for (int j = y - 1; j >= 0; j--)
            if (currentPointValue == matrix[x][j]) {
                sameSignCount++;
            } else {
                break;
            }
        for (int j = y + 1; j < dimension; j++)
            if (currentPointValue == matrix[x][j]) {
                sameSignCount++;
            } else {
                break;
            }

        gameSuccessfulFinished |= sameSignCount >= winPoints;


        //check main diagonal
        sameSignCount = 1;
        for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--)
            if (currentPointValue == matrix[i][j]) {
                sameSignCount++;
            } else {
                break;
            }
        for (int i = x + 1, j = y + 1; i < dimension && j < dimension; i++, j++)
            if (currentPointValue == matrix[i][j]) {
                sameSignCount++;
            } else {
                break;
            }

        gameSuccessfulFinished |= sameSignCount >= winPoints;


        //check sub diagonal
        sameSignCount = 1;
        for (int i = x - 1, j = y + 1; i >= 0 && j < dimension; i--, j++)
            if (currentPointValue == matrix[i][j]) {
                sameSignCount++;
            } else {
                break;
            }
        for (int i = x + 1, j = y - 1; i < dimension && j >= 0; i++, j--)
            if (currentPointValue == matrix[i][j]) {
                sameSignCount++;
            } else {
                break;
            }

        gameSuccessfulFinished |= sameSignCount >= winPoints;


        if (gameSuccessfulFinished)
            showMessageDialog(null, "You Won!");
    }
}

