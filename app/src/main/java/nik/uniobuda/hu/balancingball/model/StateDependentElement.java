package nik.uniobuda.hu.balancingball.model;

import nik.uniobuda.hu.balancingball.util.MapElementType;
import nik.uniobuda.hu.balancingball.util.MapState;

/**
 * Created by cserof on 12/5/2017.
 * Model of the map's elements and their positions.
 * (0,0)-point is the upper left corner
 * These elements is only represented in the game when their state is equals the actual state of the game.
 */

public class StateDependentElement extends MapElement {

    //An element is represented in the game when its state is equals the actual state of the game.
    private MapState state;

    public StateDependentElement(float left, float top, float right, float bottom, MapElementType type, boolean isDamage, MapState state) {
        super(left, top, right, bottom, type, isDamage);
        this.state = state;
    }

    public MapState getState() {
        return state;
    }
}
