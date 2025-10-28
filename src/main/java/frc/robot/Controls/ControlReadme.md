# Control Interface
- [Introduction](#introduction)
- [Usage](#usage)
- [Examples](#examples)

## Introduction
 Hello Team Eagle Elite 5227! I am here to showcase my invention in Controls. This "project" is made for the purpose of potentially getting the "Innovation in Control" award. This is primarily a developer document, but should be easy enough for others to understand. 
 
 The Control Interface is the idea that there is a separation between Controls and Robot. Instead of putting your references to Controls directly in the Robot code, you have a wrapper that provides the Robot code with your Controls.

 The control Interface is composed of two parts: The "Header" File and the "Controls" File(s). The "Header" file defines all the inputs that will be avaliable to bind, while the "Controls"
  file defines what gets returned by those inputs.

## Usage
Your "Header" should be constructed with simple template functions like this:
```java
public interface ControlInterface {
    double inputDouble();
    boolean inputBoolean();
}
```
Your functions should specify a name and a variable type. In your robot code, this is what would be inputed to use a control

Your "Controls" should be comprised of overrides, overriding the template functions. 
```java
@Override
    public double inputDouble() {
        return 0.0;
    } 
```
This is where you could input stuff like joystick buttons. The "Controls" file should also include any functions needed to properly use an input! (For example, clamping a input to be in a range)

## Examples
- [Interface Header](src/main/java/frc/robot/Controls/ControlInterface.java)
- [Interface Controls](src/main/java/frc/robot/Controls/RebindDemo.java)

These are some functioning Control Headers / Controls for reference. Every robot is not the same, so be sure to modify these files and use them as reference for later. 
