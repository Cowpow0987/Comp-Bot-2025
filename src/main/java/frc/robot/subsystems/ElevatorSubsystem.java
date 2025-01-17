// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Calibrations;
import frc.robot.Constants;

public class ElevatorSubsystem extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */

  // Creates the TalonFX motors for the elevator.
  private final TalonFX m_elevator1;
  private final TalonFX m_elevator2;
  private final TalonFX m_elevator3;
  private final TalonFX m_elevator4;

  // Creates two followers for running the motors in sync.
  private final Follower m_follower;
  private final Follower m_followerInv;

  // Creates the class for the motion profiler.
  private final MotionMagicTorqueCurrentFOC m_motionMagicTorqueCurrentFOC;

  public ElevatorSubsystem() {

    // Initializes the TalonFX motors for the elevator.
    m_elevator1 = new TalonFX(Constants.ElevatorConstants.kElevator1CANID);
    m_elevator2 = new TalonFX(Constants.ElevatorConstants.kElevator2CANID);
    m_elevator3 = new TalonFX(Constants.ElevatorConstants.kElevator3CANID);
    m_elevator4 = new TalonFX(Constants.ElevatorConstants.kElevator4CANID);

    // initializes the motion magic motion profiler.
    m_motionMagicTorqueCurrentFOC = new MotionMagicTorqueCurrentFOC(0);

    // Creates a configurator for the motors in this subsystem.
    TalonFXConfiguration config = new TalonFXConfiguration();

    // Gravity type for this subsystem.
    config.Slot0.GravityType = GravityTypeValue.Elevator_Static;

    // Feedforward and PID settings for the motors in this subsystem.
    config.Slot0.kG = Calibrations.ElevatorCalibrations.kElevatorkG;
    config.Slot0.kS = Calibrations.ElevatorCalibrations.kElevatorkS;
    config.Slot0.kV = Calibrations.ElevatorCalibrations.kElevatorkV;
    config.Slot0.kA = Calibrations.ElevatorCalibrations.kElevatorkA;
    config.Slot0.kP = Calibrations.ElevatorCalibrations.kElevatorkP;
    config.Slot0.kD = Calibrations.ElevatorCalibrations.kElevatorkD;

    // Configs to be used by the MotionMagicConfigs class
    config.MotionMagic.MotionMagicCruiseVelocity = Calibrations.ElevatorCalibrations.kMaxSpeedMotionMagic;
    config.MotionMagic.MotionMagicAcceleration = Calibrations.ElevatorCalibrations.kMaxAccelerationMotionMagic;
    config.TorqueCurrent.PeakForwardTorqueCurrent = Calibrations.ElevatorCalibrations.kMaxElevatorCurrentPerMotor;
    config.TorqueCurrent.PeakReverseTorqueCurrent = -Calibrations.ElevatorCalibrations.kMaxElevatorCurrentPerMotor;
    
    // Applies the configs to all the motors in this subsystem.
    m_elevator1.getConfigurator().apply(config.Slot0);  
    m_elevator2.getConfigurator().apply(config.Slot0);
    m_elevator3.getConfigurator().apply(config.Slot0);
    m_elevator4.getConfigurator().apply(config.Slot0);

    // Declares elevator1 as lead motor. Other motors are set to follow.
    m_follower = new Follower(Constants.ElevatorConstants.kElevator1CANID, false);
    m_followerInv = new Follower(Constants.ElevatorConstants.kElevator1CANID, true);

    m_elevator2.setControl(m_follower);
    m_elevator3.setControl(m_follower);
    m_elevator4.setControl(m_follower);
    //TODO: Set motors to follow followerInv if they need to be reversed
  }

  /**
   * Passes in a value in degrees for the Motion Magic Motion Profiler to use.
   * 
   * @param newElevatorSetpoint - New setpoint for the elevator in inches.
   */
  public void setElevatorSetpoint(double newElevatorSetpoint) {

    //Sets the setpoint of elevator1 motor using the MotionMagic Motion Profiler.
    m_elevator1.setControl(m_motionMagicTorqueCurrentFOC.withPosition(newElevatorSetpoint * Constants.ElevatorConstants.kPulleyGearRatio));

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
