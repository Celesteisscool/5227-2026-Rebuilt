package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
    public static void addEntry(String name, Class<?> dataType) {
        if (dataType == Boolean.class) {
            SmartDashboard.putBoolean(name, false);
        } else if (dataType == Double.class) {
            SmartDashboard.putNumber(name, 0.0);
        } else if (dataType == String.class) {
            SmartDashboard.putString(name, "");
        }
    }

    public static void updateEntry(String name, Object value) {
        if (value.getClass() == Boolean.class) { 
            SmartDashboard.getEntry(name).setBoolean((Boolean) value);
        } else if (value.getClass() == Double.class) { 
            SmartDashboard.getEntry(name).setDouble((Double) value);
        } else if (value.getClass() == String.class) { 
            SmartDashboard.getEntry(name).setString((String) value);
        } else {
            System.out.print("Didnt update shit :( " + value.getClass());
        }
    }

    public static Object getEntry(String name, Class<?> dataType) {
        if (dataType == Boolean.class) {
            return SmartDashboard.getEntry(name).getBoolean(false);
        } else if (dataType == Double.class) {
            return SmartDashboard.getEntry(name).getDouble(0.0);
        } else if (dataType == String.class) {
            return SmartDashboard.getEntry(name).getString("");
        } else {
            return null;
        }
    }

}
