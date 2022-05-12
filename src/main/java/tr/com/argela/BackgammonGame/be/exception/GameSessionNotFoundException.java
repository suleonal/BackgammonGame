package tr.com.argela.BackgammonGame.be.exception;

    public class GameSessionNotFoundException extends GameException {

        public GameSessionNotFoundException(String sessionId) {
            super("Game session is not found at server. SessionId : " + sessionId);
        }
    
}
