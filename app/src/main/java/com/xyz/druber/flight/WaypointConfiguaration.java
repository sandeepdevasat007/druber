package com.xyz.druber.flight;

import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;

class WaypointConfiguaration {
    public WaypointConfiguaration(WaypointMissionHeadingMode mHeadingMode, float mSpeed, WaypointMissionFlightPathMode curved, WaypointMission.Builder waypointMissionBuilder) {


        WaypointMissionFinishedAction mFinishedAction = WaypointMissionFinishedAction.GO_HOME;
        if (waypointMissionBuilder == null) {
            waypointMissionBuilder = new WaypointMission.Builder().finishedAction(mFinishedAction)
                    .headingMode(mHeadingMode)
                    .autoFlightSpeed(mSpeed)
                    .maxFlightSpeed(mSpeed)
                    .flightPathMode(curved);

        } else {
            mFinishedAction = WaypointMissionFinishedAction.GO_HOME;
            waypointMissionBuilder.finishedAction(mFinishedAction)
                    .headingMode(mHeadingMode)
                    .autoFlightSpeed(mSpeed)
                    .maxFlightSpeed(mSpeed)
                    .flightPathMode(curved);

        }

    }
}
