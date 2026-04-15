package org.firstinspires.ftc.teamcode.pedroPathing.telOPMode;

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

import java.util.List;


@TeleOp(name = "New Robot")
public class Talos2 extends OpMode {

    private Limelight3A limelight;

    DcMotor frontRightMotor, backLeftMotor, backRightMotor, frontLeftMotor, intakeMotor;
    DcMotorEx launch;
    ColorSensor slotSensor1,
            slotSensor2,
            slotSensor3,
            launchSensor;
    CRServo holderServo1,
            holderServo2,
            holderServo3;
    HuskyLens huskyLens;
    HuskyLens.Block[] blocks;



    boolean holder1, holder2, holder3;

    //----------------------


    /// Start position

    @Override
    public void init() {
        frontRightMotor = hardwareMap.get(DcMotor.class, "rightFront");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "leftFront");
        backLeftMotor = hardwareMap.get(DcMotor.class, "leftBack");
        backRightMotor = hardwareMap.get(DcMotor.class, "rightBack");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        //launch = hardwareMap.get(DcMotorEx.class, "launch");
        //servos
        holderServo1 = hardwareMap.get(CRServo.class, "holderServo1");
        holderServo2 = hardwareMap.get(CRServo.class, "holderServo2");
        holderServo3 = hardwareMap.get(CRServo.class, "holderServo3");
        //Color sensor
        /*slotSensor1 = hardwareMap.get(ColorSensor.class, "slotSensor1");
        slotSensor2 = hardwareMap.get(ColorSensor.class, "slotSensor2");
        slotSensor3 = hardwareMap.get(ColorSensor.class, "slotSensor3");
        launchSensor = hardwareMap.get(ColorSensor.class, "launchSensor");
        //Camra*/
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        // Brake so robot stops instead of coasting
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



        ///sorterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void loop() {

        double verticle = -gamepad1.left_stick_y; // forward/back
        double strafe = -gamepad1.left_stick_x;  // left/right
        double turn = -gamepad1.right_stick_x;   // rotation

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

        if (gamepad2.right_trigger > 0) {
            holderServo1.setPower(1);
            holderServo2.setPower(1);
            holderServo3.setPower(1);

            launch.setVelocity(180);
        }

        if (gamepad1.left_trigger > 0) {
            intakeMotor.setPower(-.5);
        } else {
            intakeMotor.setPower(1);
        }
        holderServo1.setPower(-4);
        holderServo2.setPower(-4);
        holderServo3.setPower(-4);
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


        telemetry();
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

        if (id == 25) {
            distance = result.getTa();
            telemetry.addData("distance: ", distance);
            telemetry.update();
        } else distance = 0;

        return distance;

    }

}