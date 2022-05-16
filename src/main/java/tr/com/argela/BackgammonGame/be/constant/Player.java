package tr.com.argela.BackgammonGame.be.constant;

import lombok.Getter;

@Getter
public enum Player {
    ONE(-1, -2),
    TWO(-3, -4);

    int punishZoneId, treasureZoneId;

    Player(int punishZoneId, int treasureZoneId) {
        this.punishZoneId = punishZoneId;
        this.treasureZoneId = treasureZoneId;
    }

    public Player getOtherPlayer() {
        if (this == ONE) {
            return TWO;
        }
        return ONE;
    }

    public static boolean isPlayerZone(int pitId) {
        for (Player player : values()) {
            if (player.getPunishZoneId() == pitId || player.getTreasureZoneId() == pitId)
                return true;
        }
        return false;
    }
}
