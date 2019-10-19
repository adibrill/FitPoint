package com.sport2gether11.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.*;
import com.sport2gether11.MemberProfileActivity;
import com.sport2gether11.R;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

       // LineGraphView graphView = new LineGraphView(
        //        this // context
        //        , "GraphViewDemo" // heading
       // );

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Add Workout Log", Toast.LENGTH_SHORT).show();

            }
        });


        GraphView graph = (GraphView)root.findViewById(R.id.graph);

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"0", "1", "2","3", "4", "5","6", "7", "8","9", "10", "11","12","13", "14", "15","16", "17", "18","19", "20", "21","22", "23", "24","25"});
        staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1", "2","3", "4", "5","6", "7", "8","9", "10", "11","12"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 5),
                new DataPoint(4, 5),
                new DataPoint(5, 6),
                new DataPoint(6, 6.6),
                new DataPoint(7, 7.5),
                new DataPoint(8, 8),
                new DataPoint(9, 8.1),
                new DataPoint(10, 8.5),
                new DataPoint(11, 9),
                new DataPoint(12, 9.1),
                new DataPoint(13, 9.9),
                new DataPoint(14, 10)

        });

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(15f);
        graph.addSeries(series);


        return root;
    }


}