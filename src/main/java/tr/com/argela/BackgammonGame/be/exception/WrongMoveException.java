package tr.com.argela.BackgammonGame.be.exception;

import tr.com.argela.BackgammonGame.be.constant.Player;

public class WrongMoveException extends GameException{
    public WrongMoveException(Player player , int src, int dest){
        super("Wrong movement for player : "+player+ ", from : "+ src+ ", to : "+dest);
    }
}
