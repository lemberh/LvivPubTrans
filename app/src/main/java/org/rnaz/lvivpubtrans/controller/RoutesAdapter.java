package org.rnaz.lvivpubtrans.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.rnaz.lvivpubtrans.R;
import org.rnaz.lvivpubtrans.model.RouteModel;
import org.rnaz.lvivpubtrans.view.RoutesListFrg;

import java.util.List;

/**
 * Created by Roman on 1/20/2017.
 */

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RouteVH> {

    private List<RouteModel> models;
    private RoutesListFrg.IOnItemSelected onItemSelected;

    public RoutesAdapter(RoutesListFrg.IOnItemSelected onItemSelected) {
        this.onItemSelected = onItemSelected;
    }

    @Override
    public RouteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RouteVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.route_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RouteVH holder, int position) {
        holder.routeName.setText(models.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 :models.size();
    }

    public void setModels(List<RouteModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    class RouteVH extends RecyclerView.ViewHolder{
        TextView routeName;

        RouteVH(final View itemView) {
            super(itemView);
            routeName = (TextView) itemView.findViewById(R.id.route_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemSelected.onSelected(models.get(getAdapterPosition()));
                }
            });
        }
    }
}
