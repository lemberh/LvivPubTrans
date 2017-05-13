package org.rnaz.lvivpubtrans.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.rnaz.lvivpubtrans.R;
import org.rnaz.lvivpubtrans.model.IRouteModel;
import org.rnaz.lvivpubtrans.model.RouteModel;
import org.rnaz.lvivpubtrans.view.RoutesListFrg;

import java.util.List;

/**
 * Created by Roman on 1/20/2017.
 */

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RouteVH> {

    private List<IRouteModel> models;
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
        IRouteModel routeModel = models.get(position);
        holder.routeName.setText(routeModel.getName());
        holder.setIcon(routeModel.getType());
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 :models.size();
    }

    public void setModels(List<IRouteModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    class RouteVH extends RecyclerView.ViewHolder{
        TextView routeName;
        ImageView routeIcon;

        RouteVH(final View itemView) {
            super(itemView);
            routeName = (TextView) itemView.findViewById(R.id.route_name);
            routeIcon = (ImageView) itemView.findViewById(R.id.route_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemSelected.onSelected(models.get(getAdapterPosition()));
                }
            });
        }

        void setIcon (@RouteModel.TransportType int transportType){
            switch (transportType) {
                case RouteModel.BUS:
                    routeIcon.setImageResource(R.drawable.bus);
                    break;
                case RouteModel.TRAM:
                    routeIcon.setImageResource(R.drawable.tram);
                    break;
                case RouteModel.TROLLEYBUS:
                    routeIcon.setImageResource(R.drawable.trolleybus);
                    break;
            }
        }
    }
}
