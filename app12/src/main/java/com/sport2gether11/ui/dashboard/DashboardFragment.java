package com.sport2gether11.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.sport2gether11.MapAndMenu;
import com.sport2gether11.MemberProfileActivity;
import com.sport2gether11.ProfileSettings;
import com.sport2gether11.R;
import com.sport2gether11.WorkoutForm;


public class DashboardFragment extends Fragment {

    public String EntriesCountStr = "0";
    public int EntriesCountInt = 0;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent AddWorkoutData = new Intent(getActivity(), WorkoutForm.class);
                startActivity(AddWorkoutData);
                Toast.makeText(view.getContext(), "Add Workout Log", Toast.LENGTH_SHORT).show();

            }
        });

        GraphView graph = (GraphView)root.findViewById(R.id.graph);

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        //staticLabelsFormatter.setHorizontalLabels(new String[] {"0", "1", "2","3", "4", "5","6", "7", "8","9", "10", "11","12","13", "14", "15","16", "17", "18","19", "20", "21","22", "23", "24","25"});
        //staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1", "2","3", "4", "5","6", "7", "8","9", "10", "11","12"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        SharedPreferences pref = getContext().getSharedPreferences("Entries", Context.MODE_MULTI_PROCESS); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        EntriesCountStr = pref.getString("EntriesCount", EntriesCountStr);


        EntriesCountInt = Integer.parseInt(EntriesCountStr);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(data(EntriesCountInt));

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(15f);
        graph.addSeries(series);


        return root;
    }

    public DataPoint[] data(int size){
        String datapointString = "";
        String datapoint[];
        int n=size;     //to find out the no. of data-points
        DataPoint[] values = new DataPoint[n];     //creating an object of type DataPoint[] of size 'n'
        for(int i=0;i<n;i++){

            SharedPreferences pref = this.getActivity().getSharedPreferences("Entries", Context.MODE_WORLD_READABLE);
            datapointString = pref.getString("WorkEntry"+Integer.toString(i),datapointString);

            datapoint = datapointString.split(",");
            DataPoint v = new DataPoint(Double.parseDouble(datapoint[0]),Double.parseDouble(datapoint[1]));
            values[i] = v;
        }
        return values;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}