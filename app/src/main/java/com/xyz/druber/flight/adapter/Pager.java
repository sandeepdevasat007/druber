package com.xyz.druber.flight.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xyz.druber.flight.fragment.CompletedStatusFragment;
import com.xyz.druber.flight.fragment.InprogressStatusFragment;
import com.xyz.druber.flight.fragment.PendingStatusFragment;
import com.xyz.druber.flight.network.model.Mission;

import java.util.ArrayList;


public class Pager extends FragmentStatePagerAdapter {
    private ArrayList<Mission> missions;
    private String [] tabTitles=new String[]{"Pending","Inprogress","Completed"};
    public Pager(FragmentManager fm, int tabCount, ArrayList<Mission> missions) {
        super(fm);
        this.missions = missions;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PendingStatusFragment pendingFragment = new PendingStatusFragment();
                Bundle args0 = new Bundle();
                args0.putParcelableArrayList("missions", missions);
                pendingFragment.setArguments(args0);
                return pendingFragment;
            case 1:
                InprogressStatusFragment inprogressFragment = new InprogressStatusFragment();
                Bundle args1 = new Bundle();
                args1.putParcelableArrayList("missions", missions);
                inprogressFragment.setArguments(args1);
                return inprogressFragment;
            case 2:
                CompletedStatusFragment completedFragment = new CompletedStatusFragment();
                Bundle args2 = new Bundle();
                args2.putParcelableArrayList("missions", missions);
                completedFragment.setArguments(args2);
                return completedFragment;
            default:
                return null;

        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}