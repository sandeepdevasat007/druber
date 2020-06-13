package com.xyz.druber.flight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dji.ux.druber.R;
import com.xyz.druber.flight.RegistrationActivity;
import com.xyz.druber.flight.utils.DataUtils;

/**
 * Created by SANDEEP
 */

public class MenuFragment extends Fragment  {

    GestureDetector gestureDetector;
    private View view;
    private TextView prf_nameTv;
    private TextView logout_Tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.slide_menu, container, false);
        LinearLayout root = (LinearLayout) rootView.findViewById(R.id.rootLayout);
        prf_nameTv = (TextView) rootView.findViewById(R.id.prf_Name);
        logout_Tv = (TextView) rootView.findViewById(R.id.logout_Btn);

        prf_nameTv.setText(DataUtils.getName(getActivity()));
        /*gestureDetector=new GestureDetector(getActivity(),new OnSwipeListener(){

            @Override
            public boolean onSwipe(Direction direction) {
                if (direction==Direction.up){
                    //do your stuff
                    ((MainActivity )  getActivity()).hideFragment();

                }

                if (direction==Direction.down){
                    //do your stuff

                }
                return true;
            }


        });
        root.setOnTouchListener(this);*/
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }


    public TextView logout() {

        return logout_Tv;
    }

    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.logout_Btn:
                DataUtils.logout(getActivity());
                Intent goto_Login = new Intent (getActivity(), RegistrationActivity.class);
                startActivity(goto_Login);
                getActivity().finish();
                break;
        }
    }


}
