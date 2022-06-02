package tr.com.argela.BackgammonGame.be.exception;

public class PlayerNotAuthorized extends GameException{
    public PlayerNotAuthorized(){
        super("You are not authorized");
    }
}

