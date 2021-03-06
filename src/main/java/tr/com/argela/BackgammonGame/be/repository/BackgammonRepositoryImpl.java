package tr.com.argela.BackgammonGame.be.repository;

import java.util.LinkedHashMap;
import java.util.Map;

import tr.com.argela.BackgammonGame.be.exception.GameSessionNotFoundException;
import tr.com.argela.BackgammonGame.be.model.BackgammonBoard;
import tr.com.argela.BackgammonGame.be.model.PlayerInfo;

public class BackgammonRepositoryImpl implements BackgommonRepository {

    //backgammonboard verilerine erişmek için 
    Map<String, BackgammonBoard> gameSessions = new LinkedHashMap<>();

    @Override
    public void save(String sessionId, BackgammonBoard BackgammonBoard) {
        gameSessions.put(sessionId, BackgammonBoard);
    }

    @Override
    public BackgammonBoard getBySessionId(String sessionId) throws GameSessionNotFoundException {
        BackgammonBoard BackgammonBoard = gameSessions.get(sessionId);
        if (BackgammonBoard == null) {
            throw new GameSessionNotFoundException(sessionId);
        }
        return BackgammonBoard;
    }

    @Override
    public void deleteById(String sessionId) throws GameSessionNotFoundException {
        BackgammonBoard BackgammonBoard = gameSessions.remove(sessionId);
        if (BackgammonBoard == null) {
            throw new GameSessionNotFoundException(sessionId);
        }

    }
}

