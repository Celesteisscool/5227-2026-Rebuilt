package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Elastic.Elastic;

/**
 * Utility for reading and writing values on the "SmartDashboard" (We use Elastic).
 */
public class Dashboard {

    /**
     * Adds an entry to the SmartDashboard and sets its initial value. The
     * initial value determines the entry type (Boolean, Double, or String).
     *
     * @param name       the dashboard entry name
     * @param defaultVal initial value used to determine the entry's type
     */
    public static void addEntry(String name, Object defaultVal) {
        if (defaultVal.getClass() == Boolean.class) {
            SmartDashboard.putBoolean(name, (boolean) defaultVal);
        } else if (defaultVal.getClass() == Double.class) {
            SmartDashboard.putNumber(name, (double) defaultVal);
        } else if (defaultVal.getClass() == String.class) {
            SmartDashboard.putString(name, (String) defaultVal);
        }
    }

    /**
     * Updates the value of an existing dashboard entry.
     *
     * @param name  the entry name
     * @param value the new value to set
     */
    public static void updateEntry(String name, Object value) {
        SmartDashboard.getEntry(name).setValue(value);
    }

    /**
     * Returns the current value of a dashboard entry.
     *
     * @param name the entry name
     * @return the stored value (may be Boolean, Double, String, etc.)
     */
    public static Object getEntry(String name) {
        return SmartDashboard.getEntry(name).getValue().getValue();
    }

    /**
     * Selects and opens a tab on the dashboard.
     *
     * @param name the tab name
     */
    public static void openTab(String name) {
        Elastic.selectTab(name);
    }
}
