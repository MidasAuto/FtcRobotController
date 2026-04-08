package org.firstinspires.ftc.teamcode.pedroPathing.autoOPMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "AutoTest1")
public class AutoTest1 extends OpMode {

    /// ----------* Variables *------------

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    private double ballSpeed = 100;

    /// ---------* Poses *----------

    public Pose
    startPose = new Pose(0,0, Math.toRadians(90));

    /// ---------* Actions *------------

    public Runnable shootAction(Pose targetPose, Pose currentPose, int amount) {
        DcMotorEx shoot1 = hardwareMap.get(DcMotorEx.class, "launch1");

        double distance;
        double xDis, yDis;
        int counter = amount;

        xDis = currentPose.getX() - targetPose.getX();
        yDis = currentPose.getY() - targetPose.getY();

        distance = Math.atan2((xDis * xDis), (yDis * yDis));

        double velocity = distance * 1.83;

        shoot1.setVelocity(velocity);

        return null;
    }


    /// ---------* Path Builder *----------

    public void buildPaths() {

    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                setPathState(1);
                break;
            case 1:

                if(!follower.isBusy()) {
                    setPathState(-1);
                }
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {
        follower.update();
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);
    }

    @Override
    public void init_loop() {

    }


    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }


    @Override
    public void stop() {

    }
}