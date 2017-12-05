package nik.uniobuda.hu.balancingball.model;

import nik.uniobuda.hu.balancingball.util.MapElementType;
import nik.uniobuda.hu.balancingball.util.MapState;

/**
 * Created by cserof on 12/5/2017.
 */

public class StateDependentElement extends MapElement {

    private MapState state;

    public StateDependentElement(float left, float top, float right, float bottom, MapElementType type, boolean isDamage, MapState state) {
        super(left, top, right, bottom, type, isDamage);
        this.state = state;
    }

    public MapState getState() {
        return state;
    }
}
