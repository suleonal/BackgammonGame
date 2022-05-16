package tr.com.argela.BackgammonGame.be.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.argela.BackgammonGame.be.config.GameConfig;
import tr.com.argela.BackgammonGame.be.constant.Player;
import tr.com.argela.BackgammonGame.be.exception.GameException;
import tr.com.argela.BackgammonGame.be.exception.WrongMoveException;
import tr.com.argela.BackgammonGame.be.model.BackgammonBoard;
import tr.com.argela.BackgammonGame.be.repository.BackgommonRepository;

@Service
public class BackgammonService {

    Logger logger = LoggerFactory.getLogger(BackgammonService.class);

    Player player;

    @Autowired
    BackgommonRepository backgammonRepository;

    @Autowired
    GameConfig gameConfig;

    int zar = (int) Math.random() * 5 + 1;

    public String createNewGame() {
        String sessionId = createSessionId();
        BackgammonBoard backgammonBoard = new BackgammonBoard(sessionId, gameConfig.getPitSize());
        backgammonRepository.save(sessionId, backgammonBoard);
        if (logger.isInfoEnabled()) {
            logger.debug("[NewGame] sessionId:" + sessionId + " , pitSize:" + gameConfig.getPitSize());
        }

        return sessionId;

    }

    private String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public BackgammonBoard getBackgammonBoard(String sessionId) throws GameException {
        return backgammonRepository.getBySessionId(sessionId);

    }

    public void rollDice(String sessionId) throws GameException {
        BackgammonBoard board = getBackgammonBoard(sessionId);
        board.rollDice();
    }

    private int validateDice(BackgammonBoard backgammonBoard, int source, int dest) throws GameException {
        int requestedMove = Math.abs(source - dest);

        for (int index = backgammonBoard.getMoves().size() - 1; index >= 0; index--) {

            Integer move = backgammonBoard.getMoves().get(index);

            if (move == requestedMove) {
                backgammonBoard.getMoves().remove(index);
                return requestedMove;
            }
        }
        throw new WrongMoveException(backgammonBoard.getCurrentPlayer(), source, dest);
    }

    public void move(String sessionId, int source, int dest) throws GameException {

        BackgammonBoard board = getBackgammonBoard(sessionId);

        int requestedMove = validateDice(board, source, dest);

        board.removeStone(source, 1);
        board.addStone(board.getCurrentPlayer(), dest, 1);

        if (board.getMoves().size() == 0) {
            board.setCurrentPlayer(board.getCurrentPlayer().getOtherPlayer());
        }

    }

}
