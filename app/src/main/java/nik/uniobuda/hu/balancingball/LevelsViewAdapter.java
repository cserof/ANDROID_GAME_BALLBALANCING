package nik.uniobuda.hu.balancingball;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import nik.uniobuda.hu.balancingball.model.Level;

/**
 * Created by cserof on 11/17/2017.
 */

public class LevelsViewAdapter extends BaseAdapter {

    List<Level> items;

    public LevelsViewAdapter(List<Level> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items == null ? null : items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myview =  convertView;
        if (myview == null) {
            myview = View.inflate(
                    parent.getContext(),
                    R.layout.listitem_levels,
                    null
            );
        }

        TextView levelsTextView = (TextView) myview.findViewById(R.id.lvlNrTextView);
        Level lvl = (Level) getItem(position);
        levelsTextView.setText("Level " + lvl.getLevelNumber());

        return myview;
    }
}
