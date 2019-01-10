package org.hiro.mapper.params;

public class TypePlayer {
    private int playerId;
    private int typeId;

    public TypePlayer(int playerId, int typeId){
        this.playerId = playerId;
        this.typeId = typeId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getTypeId() {
        return typeId;
    }
}
