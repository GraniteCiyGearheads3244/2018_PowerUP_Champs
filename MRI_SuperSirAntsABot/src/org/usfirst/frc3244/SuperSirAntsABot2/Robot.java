
// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.
//


//
/**
*	3/3/2018 	Changed Winch from rotations to distance
*				Added Winch on target
*				CLIMB_Lift_Robot has two steps for winch one slack two climb
*
*	3/5/2018	added private double m_convertion = 877.714; //877.714 (5/8 screw) working //1097.143 (1/2 screw)
*
*
*	3/15/2018	ok Added Scissor Jog Motion Magic Commands
*				ok Changed Intake_Cube to Intake_Cube_n_Float
*				ok Added Intake_Cube_n_Open
*
*				Default Auto_99 now includes Auto_00_Reach_Baseline
*				Documented all the Autos
*				Added Left auto either scale
*				Need to check drive m_maxWheelSpeed = 417
*				removed Timer m_timer = new Timer(); from Drive for distance command Never used
*
*	3/17/2018	winchInit() added MyStop_Winch
*
*#5 added green machine get game data
*#6 Switch two cube right
*
*
*	3/28/2018 	1 BLocked out the Turn to scale after transitioning accross field till distance are tested
*				2 Added OI Winch Unwind Launch Pad #2
*				3 Commented out SmartDashboard Buttons
*				4 Commented out Demo Target Tracking
*
*
*	4/16/2018 	1 Added to the END() method Intake_Cube_n_Float, Intake_Cube_n_Open "Robot.intake.my_intake(.1);" to try and hold the cube better
*				2 Added Left/Right Auto Scale Switch
*				3 Added auto scale ingnor FMS
*/

package org.usfirst.frc3244.SuperSirAntsABot2;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3244.SuperSirAntsABot2.util.Utils;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_00_Reach_BaseLine;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_01_Reach_BaseLine_NoGameData;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_11_1_Start_Left_Deliver_IF_Lswitch_Lscale;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_31_1_Start_Right_Deliver_IF_Rswitch_Rscale;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_32_1_Start_Right_Deliver_IF_Rscale;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_32_3_0_Conditional_Approach_Right_Scale;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_33_1_Start_Right_Deliver_IF_Rscale_Lscale;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_33_4_Deliver_Left_Scale_From_Right_00_To_Plate;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_34_1_Start_Right_Deliver_IF_Rswitch_Rscale_Lscale;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_35_1_Start_Right_Deliver_IF_Rswitch_Lswitch;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_36_1_Start_Right_Deliver_IF_Rscale_Rswitch;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_12_1_Start_Left_Deliver_IF_Lscale;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_12_3_0_Conditional_Approach_Left_Scale;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_13_1_Start_Left_Deliver_IF_Lscale_Rscale;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_14_1_Start_Left_Deliver_IF_Lswitch_Lscale_Rscale;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_15_1_Start_Left_Deliver_IF_Lswitch_Rswitch;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_16_1_Start_Left_Deliver_IF_Lscale_Lswitch;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_21_1_Start_Center_Deliver_Switch;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_99;
import org.usfirst.frc3244.SuberSirAntsABot2.autonomousroutines.Auto_Util_TimeDelay;
import org.usfirst.frc3244.SuperSirAntsABot2.commands.*;
import org.usfirst.frc3244.SuperSirAntsABot2.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in 
 * the project.
 */
public class Robot extends TimedRobot {

	public static final boolean DEBUG = false;
	boolean isWeek0 = false;
	
	//Disabled variables
    private Integer scancount = 0 ;
	private Integer sequence = 0 ;
	private Integer count = 0;
   
	Command autonomousCommand;
    private String autonomousSelected;
	public static SendableChooser autonomousChooser;
	public static SendableChooser SwichApproachChooser;
	public static SendableChooser multiCubeChooser;
	public static SendableChooser crossFieldChooser;
	//public static SendableChooser StartUpChooser;
	private int m_auto = 0;

    public static OI oi;
    public static Drive drive;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static Intake intake;
    public static Winch winch;
    public static Scissor scissor;
    public static Wrist wrist;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    
    public static Vision_Hardware vision_hardware;
    
    public NetworkTableInstance offSeasonNetworkTable;
    public static NetworkTable limeLighttable;
    
    public static String gameData;

    public static final PowerDistributionPanel pdp = new PowerDistributionPanel();
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
    	limeLighttable = NetworkTableInstance.getDefault().getTable("limelight");
        RobotMap.init();
        drive = new Drive(); //Robotbuilder No longer Manages this
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        intake = new Intake();
        winch = new Winch();
        scissor = new Scissor();
        wrist = new Wrist();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        vision_hardware = new Vision_Hardware();
        
        
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

     // Initialize the subsystems that need it
        drive.init();
        scissor.init();
        intake.init();
        wrist.init();
        winch.init();
        
      
        
     //Set up Choosers
        setupAutomousChooser();
        setupSwitchApproach();
        setupMultiCubeChooser();
     //setupStartUpChooser();
     //Add a Number Input box to use to manualy select the Autonomous
        SmartDashboard.putNumber("Manualy Selected Autonoums: ", m_auto);
      
     //Add Subsystem Owners to SmartDashboard
        SmartDashboard.putData("DriveTrain", drive);
        SmartDashboard.putData("Scissor", scissor);
        SmartDashboard.putData("Intake", intake);
        SmartDashboard.putData("Wrist", wrist);
        SmartDashboard.putData("Winch", winch);
        
        
        
        DriverStation.reportError("Configuring Network Tables", false);
     //Network Tables
        if(isWeek0) {	
        	DriverStation.reportError("Configuring Week0 Network Tables", false);
	        offSeasonNetworkTable = NetworkTableInstance.create();
	        offSeasonNetworkTable.startClient("10.0.100.5");
        }
        
        //hot fix to disable the CTRE Can Receive Timeout errors
        LiveWindow.disableTelemetry(pdp);
        
    }
    
    private void setupAutomousChooser(){
    	//Create the Auto Chooser
    	//SmartDashboard.putString("autonomous Title", "Autonomous Choice");
        autonomousChooser = new SendableChooser();
        
        // ******* Default Auto
        autonomousChooser.addDefault("99: Auto_99", new Auto_99());					
        
        // ******* Basic Auto
        autonomousChooser.addObject("0: Auto_00 Reach BaseLine", new Auto_00_Reach_BaseLine());					
        
        // ******* Left Auto
        autonomousChooser.addObject("1: Auto_11 Deliver From Left Switch-Scale ", 		new Auto_11_1_Start_Left_Deliver_IF_Lswitch_Lscale());							
        autonomousChooser.addObject("2: Auto_14 Deliver From Left Switch-Scale-Scale ", new Auto_14_1_Start_Left_Deliver_IF_Lswitch_Lscale_Rscale());
        autonomousChooser.addObject("3: Auto_15 Deliver From Left Switch-Switch", 		new Auto_15_1_Start_Left_Deliver_IF_Lswitch_Rswitch());
        autonomousChooser.addObject("4: Auto_12 Deliver From Left Scale ", 				new Auto_12_1_Start_Left_Deliver_IF_Lscale());								
        autonomousChooser.addObject("5: Auto_13 Deliver From Left Scale-Scale ", 		new Auto_13_1_Start_Left_Deliver_IF_Lscale_Rscale());
        autonomousChooser.addObject("6: Auto_16 Deliver From Left Scale-Switch ", 		new Auto_16_1_Start_Left_Deliver_IF_Lscale_Lswitch());
        
        // ******* Center Auto
        autonomousChooser.addObject("7: Auto_21 Deliver Switch From Center", new Auto_21_1_Start_Center_Deliver_Switch());
        
        // ******* Right Auto
        autonomousChooser.addObject("8: Auto_31 Deliver From Right Switch Scale", 		new Auto_31_1_Start_Right_Deliver_IF_Rswitch_Rscale());
        autonomousChooser.addObject("9: Auto_34 Deliver From Right Switch-Scale-Scale ",new Auto_34_1_Start_Right_Deliver_IF_Rswitch_Rscale_Lscale());
        autonomousChooser.addObject("10: Auto_35 Deliver From Right Switch-Switch", 	new Auto_35_1_Start_Right_Deliver_IF_Rswitch_Lswitch());
        autonomousChooser.addObject("11: Auto_32 Deliver From Right Scale", 			new Auto_32_1_Start_Right_Deliver_IF_Rscale());
        autonomousChooser.addObject("12: Auto_33 Deliver From Right Scale-Scale ", 		new Auto_33_1_Start_Right_Deliver_IF_Rscale_Lscale());
        autonomousChooser.addObject("13: Auto_36 Deliver From Right Scale-Switch ", 	new Auto_36_1_Start_Right_Deliver_IF_Rscale_Rswitch());
        
        // ******* Ignore FMS Data
        autonomousChooser.addObject("xx: Auto_12_3_0 Deliver From Left Scale ", 				new Auto_12_3_0_Conditional_Approach_Left_Scale());	
        autonomousChooser.addObject("xx: Auto_32_3_0 Deliver From Left Scale ", 				new Auto_32_3_0_Conditional_Approach_Right_Scale());
        autonomousChooser.addObject("xx: Auto_33_4_Deliver_Left_Scale_From_Right_00_To_Plate ", new Auto_33_4_Deliver_Left_Scale_From_Right_00_To_Plate());
  
        //autonomousChooser.addObject("x: abc", new Auto_10());
        //Add More Options
        
        //Place autonomousChooser on the SmartDashboard
        SmartDashboard.putData("Autonomous Chooser", autonomousChooser);
    }
    
    private void setupSwitchApproach() {
    	SwichApproachChooser = new SendableChooser<>();
    	
    	SwichApproachChooser.addDefault("From Side 90 deg", 0);
    	SwichApproachChooser.addObject("45 Off Corner", 1);
    	SmartDashboard.putData("Swich Approach Chooser",SwichApproachChooser);
    }
    
    private void setupMultiCubeChooser() {
    	multiCubeChooser = new SendableChooser<>();
    	
    	multiCubeChooser.addDefault("No", 0);
    	multiCubeChooser.addObject("Yes Same Plate", 1);
    	multiCubeChooser.addObject("Yes Other Plate", 2);
    	
    	SmartDashboard.putData("Multi Cube Chooser", multiCubeChooser);
    	
    }
    
    private void setupCrossFieldChooser() {
    	crossFieldChooser = new SendableChooser<>();
    	
    	crossFieldChooser.addDefault("Stay Home", 0);
    	crossFieldChooser.addObject("Cross to Prep", 1);
    	
    	SmartDashboard.putData("Cross Field Chooser", crossFieldChooser);
    }
    

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit(){
    	Robot.oi.launchPad.setOutputs(0);
    	//limeLighttable.getEntry("ledMode").setValue(1);

    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
        updateSmartDashboard();
        
        scancount  = scancount+1;
        if (RobotMap.ahrs.isConnected()){
        	Robot.oi.launchPad.setOutputs(sequence);
        }
		
		if (scancount > 10){
        	sequence = sequence<<1;
        	scancount = 0;
        	count =count +1;
        }
		if (count == 11){
        	sequence = sequence+1;
        	count = 0;
        	//Test SmartDashboar Send the current AutoChoice
        	autonomousSelected = autonomousChooser.getSelected().toString();
        	//Put the selected name on the smartdashboard
            SmartDashboard.putString("Auto Choice", autonomousSelected);
        }
    }
 

    @Override
    public void autonomousInit() {
    	limeLighttable.getEntry("camMode").setValue(1);
    	Robot.wrist.my_wristUp();
    	Robot.intake.my_claw_close();
    	//Zero the Gyro
    	Robot.drive.recalibrateHeadingGyro();
    	Robot.drive.set_PreserveHeading(true);// When Testing climb we forget to re-enable
    	
    	DriverStation.reportWarning("Setting Autionomous", false);
    	
    	double initTime = System.currentTimeMillis();
    	gameData = null;
    	
    	// get Selected Autonomous
    	if(isWeek0) {
    		DriverStation.reportError("Getting GameData", false);
	    	gameData = offSeasonNetworkTable
	    			.getTable("OffseasongFMSInfo")
	    			.getEntry("GameData")
	    			.getString("defaultValue");
    	}else {
    		//gameData = DriverStation.getInstance().getGameSpecificMessage();
    		// do - try - while from 1816 - "The Green Machine"
    		do {
                try {
                	gameData = DriverStation.getInstance().getGameSpecificMessage();
                    System.out.println("Waiting For FMS Data");

                } catch (Exception e) {
                    System.out.println("FMS Data not found");
                }
            } while ((gameData == null || gameData.equals("")) && System.currentTimeMillis() - initTime < 1000);
    	}
    
    	if(gameData.length() > 0){
    		DriverStation.reportError("Game Data: " + gameData.toString(), false);
    		autonomousCommand = (Command) autonomousChooser.getSelected();
    		autonomousSelected = autonomousChooser.getSelected().toString();
		
    		// schedule the autonomous command (example)
    		if (autonomousCommand != null) autonomousCommand.start();
    	}else {
    		DriverStation.reportError("No Game Data @ Auto Init:", false);
    		autonomousCommand = new Auto_01_Reach_BaseLine_NoGameData();
     
    		// schedule the autonomous command (example)
    		if (autonomousCommand != null) autonomousCommand.start();
    	}
    }

    
    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateSmartDashboard();
    }

    @Override
    public void teleopInit() {
    	//Turn off all the Launchpad LEDs
    	Robot.oi.launchPad.setOutputs(0);
    	
    	//limeLighttable.getEntry("ledMode").setValue(1);
    	limeLighttable.getEntry("camMode").setValue(1);
    	Robot.drive.set_PreserveHeading(true);// When Testing climb we forget to re-enable
    	
    	
    	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        
        DriverStation.reportError("My Entering Teleop.", false);
        
        Robot.scissor.my_ScissorSetpositionToCurrent();
        Robot.scissor.my_ScissorStop();
        
        Robot.winch.my_WinchSetpositionToCurrent();
        Robot.winch.my_WinchStop();
        
        Robot.drive.clearDesiredHeading();
        Robot.drive.set_PreserveHeading(true);// When Testing climb we forget to re-enable
        
        //Robot.vision_hardware.setCamMode(1);
    }

    boolean teleopOnce = false;
    
    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	if (!teleopOnce)
    	{
    	  DriverStation.reportError("My Teleop Periodic is running!", false);
    	}
    	teleopOnce = true;
        Scheduler.getInstance().run();
            
        // update sensors in drive that need periodic update
        //drive.periodic();
        //elevator.periodic();
        
        drive.mecanumDriveTeleop(oi.driveX(), oi.driveY(), oi.driveRotation()); 
        
        updateSmartDashboard();
        
    }
    
    private long SMART_DASHBOARD_UPDATE_INTERVAL = 250;
    private long nextSmartDashboardUpdate = System.currentTimeMillis();
    
    public void updateSmartDashboard() {
        try {
            if (System.currentTimeMillis() > nextSmartDashboardUpdate) {
                // display free memory for the JVM
            	//double freeMemoryInKB = runtime.freeMemory() / 1024;
                //SmartDashboard.putNumber("Free Memory", freeMemoryInKB); 
                
                //SmartDashboard.putNumber("Battery Voltage", pdp.getVoltage());
                
                // Interesting Gyro Stuff
                SmartDashboard.putNumber("Gyro Angle", Utils.twoDecimalPlaces(Robot.drive.getHeading()));
                SmartDashboard.putNumber("BCK Gyro Angle", Utils.twoDecimalPlaces(RobotMap.adrxs450_Gyro.getAngle()));
                SmartDashboard.putBoolean(  "IMU_Connected",        RobotMap.ahrs.isConnected());
                SmartDashboard.putBoolean(  "IMU_IsCalibrating",    RobotMap.ahrs.isCalibrating());
            	// display mode information
//                SmartDashboard.putBoolean("Is Teleop", DriverStation.getInstance().isOperatorControl());
//                SmartDashboard.putBoolean("Is Autonomous", DriverStation.getInstance().isAutonomous());
//                SmartDashboard.putBoolean("Is Enabled", DriverStation.getInstance().isEnabled());

            	// display interesting OI information
//                SmartDashboard.putNumber("DriveX", oi.driveX());  
//                SmartDashboard.putNumber("DriveY", oi.driveY());  
//                SmartDashboard.putNumber("DriveRotation", oi.driveRotation());  
                
            	drive.updateSmartDashboard();
            	
                nextSmartDashboardUpdate += SMART_DASHBOARD_UPDATE_INTERVAL;
            }
        } catch (Exception e) {
           return;
        }
    }
}