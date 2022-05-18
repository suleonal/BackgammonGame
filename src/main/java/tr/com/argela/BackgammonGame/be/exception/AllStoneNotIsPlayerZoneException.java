package tr.com.argela.BackgammonGame.be.exception;

public class AllStoneNotIsPlayerZoneException extends GameException{
    public AllStoneNotIsPlayerZoneException(){
        super("You did not collect all your stones in the treasure zone!");
    }
}
