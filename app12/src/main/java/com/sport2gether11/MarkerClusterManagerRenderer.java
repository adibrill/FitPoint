package com.sport2gether11;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.sport2gether11.ui.home.MarkerCluster;

public class MarkerClusterManagerRenderer extends DefaultClusterRenderer<MarkerCluster> {



    public MarkerClusterManagerRenderer(Context context, GoogleMap map, ClusterManager<MarkerCluster> clusterManager) {
        super(context, map, clusterManager);
    }

    //private

}
