package tr.com.argela.BackgammonGame.be.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import tr.com.argela.BackgammonGame.be.constant.GameState;
import tr.com.argela.BackgammonGame.be.constant.Player;
import tr.com.argela.BackgammonGame.be.exception.AllStoneNotIsPlayerZoneException;
import tr.com.argela.BackgammonGame.be.exception.DestionationPunishZoneException;
import tr.com.argela.BackgammonGame.be.exception.GameException;
import tr.com.argela.BackgammonGame.be.exception.PitIsBlokedByComponentException;
import tr.com.argela.BackgammonGame.be.exception.PunishZoneHasNoStoneException;
import tr.com.argela.BackgammonGame.be.exception.PunishZoneHasStoneException;
import tr.com.argela.BackgammonGame.be.exception.TreasureZoneException;
import tr.com.argela.BackgammonGame.be.exception.WrongMoveException;

@Getter
@Setter
public class BackgammonBoard {

    String sessionId;
    int pitSize;
    @JsonIgnore
    Player nextPlayer;
    Player currentPlayer;
    GameState gameState;
    Map<Integer, List<Stone>> pits;

    List<Integer> moves = new ArrayList<>();

    Map<Player, Integer> punishZone = new HashMap();
    Map<Player, Integer> treasureZone = new HashMap();

    public BackgammonBoard(String sessionId, int pitSize) {
        this.sessionId = sessionId;
        initializedBoard(pitSize);
    }

    public void initializedBoard(int pitSize) {
        this.pitSize = pitSize;

        currentPlayer = Player.ONE;
        nextPlayer = Player.TWO;
        gameState = GameState.ACTIVE;

        pits = new LinkedHashMap<>();
        for (int pitId = 0; pitId < pitSize; pitId++) {
            pits.put(pitId, new ArrayList<>());
        }

        try {
            addStone(Player.ONE, 0, 2);
            addStone(Player.ONE, 11, 5);
            addStone(Player.ONE, 16, 3);
            addStone(Player.ONE, 18, 5);

            addStone(Player.TWO, 23, 2);
            addStone(Player.TWO, 12, 5);
            addStone(Player.TWO, 7, 3);
            addStone(Player.TWO, 5, 5);

        } catch (GameException e) {
            e.printStackTrace();
        }

        for (Player player : Player.values()) {
            punishZone.put(player, 0);
            treasureZone.put(player, 0);
        }

    }

    public void addStone(Player player, int pitId, int size) throws GameException {
        if (Player.isPlayerZone(pitId)) {
            if (player.getPunishZoneId() == pitId) {
                int val = punishZone.get(player);
                val++;
                punishZone.put(player, val);
            } else if (player.getTreasureZoneId() == pitId) {
                if (!isAllStoneInPlayerZone(player)) {
                    throw new AllStoneNotIsPlayerZoneException();
                }
                int val = treasureZone.get(player);
                val++;
                treasureZone.put(player, val);
            }
        } else
            for (int i = 0; i < size; i++)
                pits.get(pitId).add(new Stone(player, isInPlayerZone(player, pitId)));
    }

    private boolean isAllStoneInPlayerZone(Player player) {
        int total = 0;
        int begin = 0;
        int end = 0;

        switch (player) {
            case ONE: {
                begin = 18;
                end = 23;
                break;
            }
            case TWO: {
                begin = 0;
                end = 5;
                break;
            }
        }

        total += getTreasureZone().get(player);

        for (int i = begin; i <= end; i++) {
            List<Stone> stones = pits.get(i);
            if (stones.isEmpty() || stones.get(0).getPlayer() != player) {

                continue;
            }

            total += pits.get(i).size();
        }

        return total == 15;
    }

    private boolean isInPlayerZone(Player player, int pitId) {
        switch (player) {
            case ONE: {
                if (pitId >= 18 && pitId <= 23)
                    return true;
                break;
            }
            case TWO: {
                if (pitId >= 0 && pitId <= 5)
                    return true;
                break;
            }
        }
        return false;
    }

    public void removeStone(int pitId, int size) throws GameException {

        if (Player.isPlayerZone(pitId)) {
            if (this.getCurrentPlayer().getPunishZoneId() == pitId) {
                int val = punishZone.get(this.getCurrentPlayer());
                if (val == 0) {
                    throw new PunishZoneHasNoStoneException(this.getCurrentPlayer());
                }
                val--;
                punishZone.put(this.getCurrentPlayer(), val);
            } else if (this.getCurrentPlayer().getTreasureZoneId() == pitId) {
                throw new TreasureZoneException(this.getCurrentPlayer());
            }
        } else
            for (int i = size - 1; i >= 0; i--)
                pits.get(pitId).remove(i);
    }

    public boolean hasStone(int pitId) throws GameException {
        if (Player.isPlayerZone(pitId)) {
            if (this.getCurrentPlayer().getPunishZoneId() == pitId) {
                int val = punishZone.get(this.getCurrentPlayer());
                return val > 0;
            } else if (this.getCurrentPlayer().getTreasureZoneId() == pitId) {
                throw new TreasureZoneException(this.getCurrentPlayer());
            }
        } else {
            if (pits.get(pitId).size() == 0) {
                return false;
            }
            if (pits.get(pitId).get(0).getPlayer() == this.getCurrentPlayer()) {
                return true;
            }
        }
        return false;
    }

    public boolean isDestinationValid(int destPitId) throws GameException {
        if (Player.isPlayerZone(destPitId)) {
            if (this.getCurrentPlayer().getPunishZoneId() == destPitId) {
                throw new DestionationPunishZoneException();
            } else if (this.getCurrentPlayer().getTreasureZoneId() == destPitId) {
                return true;
            }
        } else {
            if (pits.get(destPitId).size() == 0) {
                return true;
            } else {
                Stone stone = pits.get(destPitId).get(0);
                if (stone.getPlayer() == this.getCurrentPlayer())
                    return true;
                else if (pits.get(destPitId).size() > 1) {
                    throw new PitIsBlokedByComponentException(destPitId);
                } else if (pits.get(destPitId).size() == 1) {
                    // tasi kir
                    removeStone(destPitId, 1);
                    addStone(this.getCurrentPlayer().getOtherPlayer(),
                            this.getCurrentPlayer().getOtherPlayer().getPunishZoneId(), 1);

                }
            }
        }
        return true;
    }

    public boolean isPunishmentZoneHasStone(int sourcePitId,int destPitId) throws GameException {
        int val = punishZone.get(this.getCurrentPlayer());
        if (val != 0) {
            if (sourcePitId == -1 || sourcePitId == -3) {
                return true;
            } else {
                throw new PunishZoneHasStoneException();
            }
        }
        return true;
    }

    public void rollDice() {
        int dice1;
        int dice2;

        dice1 = (int) (Math.random() * 6 + 1);
        dice2 = (int) (Math.random() * 6 + 1);

        int totalCount = dice1 + dice2;

        moves.clear();
        if (dice1 == dice2) {
            for (int i = 0; i < 4; i++) {
                moves.add(dice1);
            }
        } else {
            moves.add(dice1);
            moves.add(dice2);
        }
    }

}
