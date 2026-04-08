package org.firstinspires.ftc.teamcode.pedroPathing.telOPMode;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.List;


@TeleOp(name = "Talos2")
public class Talos2 extends OpMode {

    private Limelight3A limelight;

    DcMotor frontRightMotor, backLeftMotor, backRightMotor, frontLeftMotor, intakeMotor, holderMotor1, holderMotor2;
    DcMotorEx launch;
    ColorSensor slotSensor1,
            slotSensor2,
            slotSensor3,
            launchSensor;
    HuskyLens huskyLens;
    HuskyLens.Block[] blocks;

    double extra;
    //int power = 240;
    boolean upPressed = false, downPressed = false;

    //----------------------


    /// Start position

    @Override
    public void init() {
        frontRightMotor = hardwareMap.get(DcMotor.class, "rightFront");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "leftFront");
        backLeftMotor = hardwareMap.get(DcMotor.class, "leftBack");
        backRightMotor = hardwareMap.get(DcMotor.class, "rightBack");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        launch = hardwareMap.get(DcMotorEx.class, "launchMotor");
        holderMotor1 = hardwareMap.get(DcMotor.class, "holderMotor1");
        holderMotor2 = hardwareMap.get(DcMotor.class, "holderMotor2");

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        holderMotor1.setDirection(DcMotor.Direction.REVERSE);


        limelight.pipelineSwitch(0);
        limelight.start();

        ///sorterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void loop() {

        double verticle = gamepad1.left_stick_y; // forward/back
        double strafe = gamepad1.left_stick_x;  // left/right
        double turn = gamepad1.right_stick_x;   // rotation

        double fRightPower = verticle + turn + strafe;
        double fLeftPower = (verticle - turn - strafe);
        double bRightPower = verticle + turn - strafe;
        double bLeftPower = (verticle - turn + strafe);

        // apply power
        frontRightMotor.setPower(fRightPower);
        frontLeftMotor.setPower(fLeftPower);
        backRightMotor.setPower(bRightPower);
        backLeftMotor.setPower(bLeftPower);

        //holder1 = slotSensor1.green() > 200;
        //holder2 = slotSensor2.green() > 200;
        //holder3 = slotSensor3.green() > 200;

        double power = -5.3358 * powerCalc() + 238.0113;
        extra = power;

        if (gamepad2.right_trigger > 0) {
            launch.setVelocity(-power, AngleUnit.DEGREES);
        } else if (!gamepad2.dpad_left) {

            launch.setVelocity(0);
        }

        if (gamepad1.left_trigger > 0) {
            intakeMotor.setPower(-1);
        } else {
            intakeMotor.setPower(1);
        }

        if (gamepad2.dpad_up && !upPressed) {
            power += 1;
            upPressed = true;
        } else if (!gamepad2.dpad_up) {
            upPressed = false;
        }

        if (gamepad2.dpad_down && !downPressed) {
            power -= 1;
            downPressed = true;
        } else if (!gamepad2.dpad_down) {
            downPressed = false;
        }

        if (gamepad2.left_trigger > 0) {
            holderMotor1.setPower(1);
            holderMotor2.setPower(1);
        } else {
            holderMotor1.setPower(0);
            holderMotor2.setPower(0);
        }

        if (gamepad2.dpad_left) {
            launch.setPower(1);
        }

        //Light

        powerCalc();
        /*if (holder1) {
            holderServo1.setPower(0);
        } else {

        }
        if (holder1 && holder2) {
            holderServo2.setPower(0);
        } else {

        }
        if (holder1 && holder2 && holder3) {
            holderServo3.setPower(0);
        } else {

        }*/


        //telemetry();
    }

    //----------------------

    public void telemetry() {


        telemetry.update();
    }


    public double powerCalc() {
        LLResult result = limelight.getLatestResult();
        List<LLResultTypes.FiducialResult> tags = result.getFiducialResults();

        int id = -100;

        double distance;

        for (LLResultTypes.FiducialResult tag : tags) {
            id = tag.getFiducialId();
        }

        if (id == 20) {
            distance = result.getTx();
        } else distance = 0;

        telemetry.addData("Tag id", id);
        telemetry.addData("x: ", distance);
        telemetry.addData("Power: ", extra);
        telemetry.addData("real Power: ", launch.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("y: ", result.getTy());
        telemetry.update();

        return distance;

    }
}