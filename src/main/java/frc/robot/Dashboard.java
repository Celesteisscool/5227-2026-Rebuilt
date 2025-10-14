package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
    public static void addEntry(String name, Object defaultVal) {
        
        System.out.println(defaultVal.getClass());
        if (defaultVal.getClass() == Boolean.class) {
            SmartDashboard.putBoolean(name, (boolean) defaultVal);
        } else if (defaultVal.getClass() == Double.class) {
            SmartDashboard.putNumber(name, (double) defaultVal);
        } else if (defaultVal.getClass() == String.class) {
            SmartDashboard.putString(name, (String) defaultVal);
        }
        
    }

    public static void updateEntry(String name, Object value) {
        SmartDashboard.getEntry(name).setValue(value);
    }

    public static Object getEntry(String name) {
        return SmartDashboard.getEntry(name).getValue().getValue();
        // me when .getValue().getValue()
    }
}
