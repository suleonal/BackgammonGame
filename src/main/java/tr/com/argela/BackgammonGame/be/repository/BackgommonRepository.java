package tr.com.argela.BackgammonGame.be.repository;

import tr.com.argela.BackgammonGame.be.exception.GameSessionNotFoundException;
import tr.com.argela.BackgammonGame.be.model.BackgammonBoard;

public interface BackgommonRepository {

    public BackgammonBoard getBySessionId(String sessionId) throws GameSessionNotFoundException;
    
    public void save(String sessionId, BackgammonBoard BackgammonBoard);

    public void deleteById(String sessionId) throws GameSessionNotFoundException;
}
