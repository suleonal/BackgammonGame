package tr.com.argela.BackgammonGame.be.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerInfo {


    private String id;

    private String name;

    private String ip;


    public PlayerInfo(String playerOneSessionId, String name, String ip) {
        this.name=name;
        this.ip=ip;
    }

}
