package org.firstinspires.ftc.teamcode.pedroPathing.telOPMode;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Methods;


public class TelopTest1 {
    @TeleOp(name = "Test")
    public class Telop extends OpMode{

        DcMotor fR, fL, bR, bL, intake;
        DcMotorEx shooter;
        Servo cameraServo;
        private HuskyLens huskyLens;
        int ID;

        GoBildaPinpointDriver pinpoint;

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

        @Override
        public void init() {
            fR = hardwareMap.get(DcMotor.class, "frontRight");
            fL = hardwareMap.get(DcMotor.class, "frontLeft");
            bR = hardwareMap.get(DcMotor.class, "backRight");
            bL = hardwareMap.get(DcMotor.class, "backLeft");
            intake = hardwareMap.get(DcMotor.class, "intakeMotor");
            shooter = hardwareMap.get(DcMotorEx.class, "shooter");
            cameraServo = hardwareMap.get(Servo.class, "cameraServo");
            huskyLens = hardwareMap.get(HuskyLens.class, "huskylens");
            imu.initialize(parameters);
            pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
            pinpoint.setOffsets(-4.25 ,2.5, DistanceUnit.INCH);
            pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        }

        @Override
        public void loop() {
            pinpoint.update();
            new Methods.MethodsInside();

            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            fR.setPower(frontLeftPower);
            bL.setPower(backLeftPower);
            fL.setPower(frontRightPower);
            bR.setPower(backRightPower);

            huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);

            HuskyLens.Block[] blocks = huskyLens.blocks();

            for (int i = 0; i < blocks.length; i++) {
            }

            cameraServo.setPosition(Methods.MethodsInside.servoPoseControl());

            Methods.MethodsInside.trackServoControl().get(0);
        }
    }
}
