package tr.com.argela.BackgammonGame.be.exception;



public class CurrentPlayerIsChangedException extends GameException{
    public CurrentPlayerIsChangedException(){
        super("Current player is changed");
    }
}
