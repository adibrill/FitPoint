package com.sport2gether11.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sport2gether11.R;
import com.sport2gether11.WorkoutItem;
import com.sport2gether11.WorkoutsListAdapter;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        ArrayList<WorkoutItem> workoutlog = new ArrayList<>();

        // TODO
        //add firebase data!!!


        workoutlog.add(new WorkoutItem(R.drawable.ic_menu_camera,"Jenny","2019/10/15 15:00"));
        workoutlog.add(new WorkoutItem(R.drawable.ic_menu_camera,"Andrew","2019/10/17 18:30"));
        workoutlog.add(new WorkoutItem(R.drawable.ic_menu_camera,"Jenny","2019/10/18 20:00"));







        mRecyclerView = (RecyclerView)root.findViewById(R.id.workoutsrecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mAdapter = new WorkoutsListAdapter(workoutlog);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }
}