package tr.com.argela.BackgammonGame.be.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.stereotype.Service;

import tr.com.argela.BackgammonGame.be.config.GameConfig;
import tr.com.argela.BackgammonGame.be.constant.Player;
import tr.com.argela.BackgammonGame.be.exception.DestionationPunishZoneException;
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
        if (Player.isPunishmentZone(source)) {
            source = backgammonBoard.getCurrentPlayer() == Player.ONE ? -1 : 24;
        }

        if (Player.isTreasureZone(dest)) {
            dest = backgammonBoard.getCurrentPlayer() == Player.ONE ? 24 : -1;
        }

        int requestedMove = Math.abs(source - dest);

        for (int index = backgammonBoard.getMoves().size() - 1; index >= 0; index--) {

            if (backgammonBoard.getCurrentPlayer() == Player.ONE) {
                if (source == -1) {
                   //0 dan 5 e 
                }if(source>dest){
                    
                }

            }
            for (int i = 18; i <= 23; i++) {
                Integer move = backgammonBoard.getMoves().get(index);

                if (move == requestedMove) {
                    backgammonBoard.getMoves().remove(index);
                    return requestedMove;
                }
            }

        }
        throw new WrongMoveException(backgammonBoard.getCurrentPlayer(), source, dest);
    }

    // public void diceControl(Player player) {
    // BackgammonBoard board;
    // int playerOneDice = 0;
    // int playerTwoDice = 0;
    // boolean notEqualDice = true;

    // while (notEqualDice) {
    // playerOneDice = (int) (Math.random() * 6 + 1);
    // playerTwoDice = (int) (Math.random() * 6 + 1);

    // if (playerOneDice != playerTwoDice) {
    // notEqualDice = false;
    // break;
    // }
    // }
    // if (playerOneDice < playerTwoDice) {
    // board.getCurrentPlayer() = Player.TWO;
    // nextPlayer = Player.ONE;
    // } else {
    // currentPlayer = Player.ONE;
    // nextPlayer = Player.TWO;
    // }
    // }

    public void validateMove(BackgammonBoard backgammonBoard, int source, int dest) throws GameException {
        if (!(Player.isPlayerZone(source) || Player.isPlayerZone(dest)
                || ((source >= 0 && source < gameConfig.getPitSize())
                        && (dest >= 0 && dest < gameConfig.getPitSize())))) {
            throw new WrongMoveException(player, source, dest);
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

        int requestedMove = validateDice(board, source, dest);

        validateMove(board, source, dest);

        board.addStone(board.getCurrentPlayer(), dest, 1);
        board.removeStone(source, 1);

        if (board.getMoves().size() == 0) {
            board.setCurrentPlayer(board.getCurrentPlayer().getOtherPlayer());
        }

    }

}
