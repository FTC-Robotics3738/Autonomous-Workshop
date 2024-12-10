package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class  Mechanisms {
    //class to create a claw
    public static class Claw {
        private Servo claw;
        //create the claw object from hardware map

        public Claw(HardwareMap hardwareMap) {
            claw = hardwareMap.get(Servo.class, "claw");
        }

        //implement action class in our close claw function.

        public class CloseClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                //when closeclaw is run, set the claw to closed position
                claw.setPosition(0.55);
                return false;
            }
        }
        //allow the function to be able to called from other files
        public Action closeClaw() {
            return new Claw.CloseClaw();
        }
        //create an openclaw function by implementing action class

        public class OpenClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                //when openclaw is run, set the claw to the open position
                claw.setPosition(1.0);
                return false;
            }
        }
        //allow the function to be able to be called from other files
        public Action openClaw() {
            return new Claw.OpenClaw();
        }
    }

    //lift class (this will require an encoder plugged into the motor)
    public static class Lift {
        private Motor leftArm;
        private Motor rightArm;
        //create lift from hardwaremap and initialize it

        public Lift(HardwareMap hardwareMap) {
            //initialize our lift from hardwareMap
            leftArm = new Motor(hardwareMap, "armLeft");
            //set the braking mode to brake when theres no power given so it better holds target position
            leftArm.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
            //put it into position control so it automatically flips direction
            leftArm.setRunMode(Motor.RunMode.PositionControl);
            //set the lift motor direction
            //leftArm.setInverted(true);
            //set position coefficient of the lift, (p value)
            leftArm.setPositionCoefficient(0.001);
            leftArm.resetEncoder();


            rightArm = new Motor(hardwareMap, "armRight");
            //set the braking mode to brake when theres no power given so it better holds target position
            rightArm.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
            //put it into position control so it automatically flips direction
            rightArm.setRunMode(Motor.RunMode.PositionControl);
            //set the lift motor direction
            rightArm.setInverted(true);
            //set position coefficient of the lift, (p value)
            rightArm.setPositionCoefficient(0.001);
        }

        public class LiftUp implements Action {
            // checks if the lift motor has been powered on
            private boolean initialized = false;
            // actions are formatted via telemetry packets as below

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                // powers on motor, if it is not on
                if (!initialized) {
                    leftArm.set(0.8);
                    initialized = true;
                }
                //set the target position of the lift to 3000 ticks
                leftArm.setTargetPosition(1000);
                if (leftArm.getCurrentPosition() <= 1000) {
                    // true causes the action to rerun
                    return true;
                } else {
                    // false stops action rerun and stops the lift
                    leftArm.set(0);
                    return false;
                }
                // overall, the action powers the lift until it surpasses
                // 3000 encoder ticks, then powers it off2
            }
        }
        public Action liftUp() {
            return new LiftUp();
        }

        public class LiftDown implements Action {
            // checks if the lift motor has been powered on
            private boolean initialized = false;
            // actions are formatted via telemetry packets as below

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                //set the lifts target position to down position
                leftArm.setTargetPosition(300);
                // powers on motor, if it is not on
                if (!initialized) {
                    leftArm.set(0.2);
                    initialized = true;
                }

                //if the lift isn't at the target position then repeat the loop
                if (leftArm.getCurrentPosition() >= 300) {
                    // true causes the action to rerun
                    return true;
                } else {
                    // false stops action rerun and stops the lift
                    leftArm.set(0);
                    return false;
                }
                // overall, the action powers the lift down until it goes below
                // 100 encoder ticks, then powers it off
            }
        }
        public Action liftDown(){
            return new LiftDown();
        }
    }
    public static class Intake {
        private Motor intake;
        //create the claw object from hardware map

        public Intake(HardwareMap hardwareMap) {
            //initialize our intake from hardwareMap
            intake = new Motor(hardwareMap, "intake", Motor.GoBILDA.RPM_435);
            //set the braking mode to float when theres no power given so it doesn't do anything
            intake.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
            //set the runmode to raw power
            intake.setRunMode(Motor.RunMode.RawPower);
            //set the direction of the motor
            intake.setInverted(false);
        }

        //implement action class in our spin intake forward function.

        public class spinForward implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                //when intake spinforward is run, spin the intake forward
                intake.set(0.8);
                return false;
            }
        }
        //allow the function to be able to called from other files
        public Action spinForward() {
            return new Intake.spinForward();
        }
        //create an spin backward function by implementing action class

        public class spinBackward implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                //when spin backward is run, spin the intake backwards
                intake.set(-0.8);
                return false;
            }
        }
        //allow the function to be able to be called from other files
        public Action spinBackward() {
            return new Intake.spinBackward();
        }
    }
}
