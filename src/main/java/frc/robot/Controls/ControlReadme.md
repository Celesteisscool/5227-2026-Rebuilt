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

### Optional:
A perk of using these control interfaces is that you can have *dynamic* controls! This allows you to swap what control set you are using in real time!

For example: "Shooting set" and "Intake set". you can have 2 "Contol Set" files, and only override what you need to change between sets. Then, in a third file (Which will be the one you reference in robot code), you can dynamicly update which control is being used!

Set up your control sets like this:
```java
public static ControlInterface GetDynamicControl() { 
    if (Condition) {
        return new ControlSetA();
    } else {
        return new ControlSetB();
    }
}
```
And reference them like this:
```java
@Override
public boolean getSuperFunButton() {
    return GetDynamicControl().getSuperFunButton();
}
```