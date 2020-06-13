package com.xyz.druber.flight.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dji.ux.druber.R;
import com.xyz.druber.flight.adapter.MissionsListAdapter;
import com.xyz.druber.flight.constant.Constant;
import com.xyz.druber.flight.network.model.Mission;
import com.xyz.druber.flight.network.model.MissionSteps;
import com.xyz.druber.flight.utils.ItemOffsetDecoration;

import java.util.ArrayList;


/**
  Created by SANDEEP
 **/


public class PendingStatusFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pending_layout, container, false);
        ArrayList<Mission> adapterList = new ArrayList<>();
        ArrayList<Mission> pendingmissions;
        Bundle extras = getArguments();
        ArrayList<Mission> missions = extras.getParcelableArrayList("missions");
        RecyclerView missions_recycler_view = rootView.findViewById(R.id.missions_recyler_view);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.nav_header_vertical_spacing);
        missions_recycler_view.addItemDecoration(itemDecoration);
        missions_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (missions.size()>0) {

            pendingmissions = new ArrayList<>(missions);
            for (Mission pendingmission : pendingmissions) {
                MissionSteps missionStep = pendingmission.getMissionSteps().get(0);
                if (missionStep.getStatus() == null)
                    adapterList.add(pendingmission);
            }
            missions_recycler_view.setAdapter(new MissionsListAdapter(getActivity(), missions, adapterList, Constant.PENDING));
        }
        return rootView;
    }
}
