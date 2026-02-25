package org.firstinspires.ftc.teamcode.pedroPathing;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.ArrayList;
import java.util.List;

public class Methods {


    public static class MethodsInside {
        DcMotor fR, fL, bR, bL, intake;
        static DcMotorEx shooter;
        public static ColorSensor colorSensor;
        public static TouchSensor touchSensor;
        public static Limelight3A limelight;
        public static int id;

        public void init(HardwareMap hardwareMap) {
            fR = hardwareMap.get(DcMotor.class, "frontRight");
            fL = hardwareMap.get(DcMotor.class, "frontLeft");
            bR = hardwareMap.get(DcMotor.class, "backRight");
            bL = hardwareMap.get(DcMotor.class, "backLeft");
            intake = hardwareMap.get(DcMotor.class, "intakeMotor");
            shooter = hardwareMap.get(DcMotorEx.class, "shooter");
            colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
            touchSensor = hardwareMap.get(TouchSensor.class, "touchSensor");
            limelight = hardwareMap.get(Limelight3A.class, "limelight");
            limelight.pipelineSwitch(0);
            limelight.start();
        }

        public void loop() {
            LLResult result = limelight.getLatestResult();

            List<LLResultTypes.FiducialResult> tags = result.getFiducialResults();

            for (LLResultTypes.FiducialResult tag : tags) {
                id = tag.getFiducialId();
            }


        }

        public static Pose angularRadianPoseTranslator(Pose currentPos, double offset, double angleInRadians) {
            double angleDegrees = Math.toDegrees(angleInRadians) + 180;

            double y = currentPos.getY() - Math.sin(angleDegrees) * offset;
            double x = currentPos.getX() - Math.cos(angleDegrees) * offset;

            return new Pose(x, y, Math.toRadians(90));
        }

        public static double offsetCalc() {
            double ballSpeed = shooter.getVelocity();

            return 0;
        }

        public static double angleRadianCalc() {
            return 0;
        }

        public static Pose virtualShooterPose() {
            return angularRadianPoseTranslator(follower.getPose(), offsetCalc(), angleRadianCalc());
        }

        public static double servoPoseControl() {
            double pose = 0;

            return 0;
        }

        public static List<Double> trackServoControl() {
            List<Double> servoPowerList = new ArrayList<>();

            if (touchSensor.isPressed()) {
                servoPowerList.set(2, (double) 0);
            } else servoPowerList.set(2, 0.8);

            if (colorSensor.green() > 80) {
                servoPowerList.set(0, (double) 0);
            } else servoPowerList.set(0, 0.8);

            return servoPowerList;
        }

        public static double hasShot() {



            return 0;
        }
    }
}
