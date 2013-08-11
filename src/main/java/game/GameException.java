package game;

/**
 * User: S.Rogachevsky
 * Date: 24.07.13
 * Time: 19:13
 */
public class GameException extends Exception {
    GameException(){
        super();
    }
    GameException(String cause){
        super(cause);
    }
}
