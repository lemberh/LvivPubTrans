package org.rnaz.lvivpubtrans.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.rnaz.lvivpubtrans.R;
import org.rnaz.lvivpubtrans.model.StopModel;

import java.util.List;

/**
 * Created by Roman on 1/20/2017.
 */

public class StopsAdapter extends RecyclerView.Adapter<StopsAdapter.StopVH> {

    private List<StopModel> models;

    @Override
    public StopVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StopVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(StopVH holder, int position) {
        holder.routeName.setText(models.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    public void setModels(List<StopModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    class StopVH extends RecyclerView.ViewHolder {
        TextView routeName;

        StopVH(final View itemView) {
            super(itemView);
            routeName = (TextView) itemView.findViewById(R.id.stop_name);
        }
    }
}
