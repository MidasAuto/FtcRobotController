package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@Autonomous(name = "TestAuto", group = "Tests")
public class Examples extends OpMode {
    /// ---------* Motors *---------------


    /// ----------* Variables *------------


    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    private double ballSpeed = 100;


    /// ----------* Poses *-----------

    public Pose startPose = new Pose(20, 10, Math.toRadians(90)),
    scorePose = new Pose(20, 30, Math.toRadians(90)),
    targetPose = new Pose(67, 67, Math.toRadians(90)),
    endPos = new Pose(50, 10, Math.toRadians(90));
    


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


    ///----------* Paths *-----------

    private Path
            scorePreload,
            score1preset;
    private PathChain
            score1,
            pathChain,
            ball9scoring;


    ///----------* Methods * -----------

    public void buildPaths() {
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        pathChain = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .addTemporalCallback(100, shootAction(Methods.MethodsInside.virtualShooterPose(), startPose, 3))
                .addParametricCallback(1, shootAction(targetPose, startPose, 3))
                .build();

        follower.followPath(pathChain);
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(scorePreload);
                setPathState(1);
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Set the state to a Case we won't use or define, so it just stops running an new paths */
                    setPathState(-1);
                }
                break;
        }
    }

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/

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
