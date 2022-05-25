package tr.com.argela.BackgammonGame.be.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.stereotype.Service;

import tr.com.argela.BackgammonGame.be.config.GameConfig;
import tr.com.argela.BackgammonGame.be.constant.Player;
import tr.com.argela.BackgammonGame.be.exception.CurrentPlayerIsChangedException;
import tr.com.argela.BackgammonGame.be.exception.DestionationPunishZoneException;
import tr.com.argela.BackgammonGame.be.exception.GameException;
import tr.com.argela.BackgammonGame.be.exception.PunishZoneHasStoneException;
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

    public void validateMove(BackgammonBoard backgammonBoard, int requestedMove, int source, int dest)
            throws GameException {
        if (!(Player.isPlayerZone(source) || Player.isPlayerZone(dest)
                || ((source >= 0 && source < gameConfig.getPitSize())
                        && (dest >= 0 && dest < gameConfig.getPitSize())))) {
            throw new WrongMoveException(player, source, dest);
        }

        if (!backgammonBoard.isPunishmentZoneHasStone(source, dest, requestedMove)) {
            throw new PunishZoneHasStoneException();
        }

        if (!backgammonBoard.hasStone(source)) {
            throw new WrongMoveException(player, source, dest, " has not stone on source pit");
        }

        if (!backgammonBoard.isDestinationValid(dest)) {
            throw new WrongMoveException(player, source, dest, " not valid action");
        }

    }

    public void move(String sessionId, int source, int dest) throws GameException {

        BackgammonBoard board = getBackgammonBoard(sessionId);

        int requestedMove = board.validateDice(source, dest);
        try {
            validateMove(board, requestedMove, source, dest);
            board.addStone(board.getCurrentPlayer(), dest, 1);
            board.removeStone(source, 1);
        } catch (CurrentPlayerIsChangedException e) {
            System.out.println("");
        }
        if (board.getMoves().size() == 0) {
            board.setCurrentPlayer(board.getCurrentPlayer().getOtherPlayer());
        }

    }

}
