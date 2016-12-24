package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

public class Player extends Entity {
    
    private int ID;
    
    public float RELOAD = 500;
    public float MAX_SPEED = 100;
    public float SIDE_MAX_SPEED = 3;
    public float ROTATION_SPEED = 50;
    public float BOOST = (float) 0.5;
    public float ROTATION_BOOST = 30;
    public float BRAKING = (float) 0.5;

    public long currentReloadTime = 1;
    private float rotationY = 0;
    private float currentForwardSpeed = 0;
    private float currentSideSpeed = 0;
    private float currentUpSpeed = 0;
    private boolean check = true;

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getID() {
        return ID;
    }
    
    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, int ID) {
        super(model, position, rotX, rotY, rotZ, scale);
        this.ID = ID;
    }
    
    public boolean checkReload() {

        long end = System.currentTimeMillis();
        if ((end - currentReloadTime) >= RELOAD) {
            this.currentReloadTime = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    public void move() {

        checkInputs();
        float distanceZ = (float) (currentSideSpeed * Math.cos(Math.toRadians(super.getRotY() - 90)));
        float distanceX = (float) (currentSideSpeed * Math.sin(Math.toRadians(super.getRotY() - 90)));
        float dz = (float) (currentForwardSpeed * Math.cos(Math.toRadians(super.getRotY())));
        float dx = (float) (currentForwardSpeed * Math.sin(Math.toRadians(super.getRotY())));

        super.increaseRotation(0, rotationY, 0);
        super.increasePosition(dx + distanceX, currentUpSpeed, dz + distanceZ);
    }

    private float boost(float currentSpeed, float maxSpeed, float boostSpeed, float direction) {
        if (direction * currentSpeed <= maxSpeed) {
            currentSpeed += direction * boostSpeed * DisplayManager.getFarmeTimeSeconds();
        }
        return currentSpeed;
    }

    private float braking(float currentSpeed, float brakingSpeed) {
        float direction = Math.abs(currentSpeed) / currentSpeed;
        if (direction * currentSpeed >= brakingSpeed * DisplayManager.getFarmeTimeSeconds()) {
            currentSpeed -= direction * brakingSpeed * DisplayManager.getFarmeTimeSeconds();
        } else {
            currentSpeed = 0;
        }
        return currentSpeed;
    }

    private void checkInputs() {
        // Key Space, LControl
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && check) {
            currentUpSpeed = boost(currentUpSpeed, SIDE_MAX_SPEED, BOOST, 1);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && check) {
            currentUpSpeed = boost(currentUpSpeed, SIDE_MAX_SPEED, BOOST, -1);
        } else {
            currentUpSpeed = braking(currentUpSpeed, BRAKING);
        }

        //Key W
        if (Keyboard.isKeyDown(Keyboard.KEY_W) && check) {
            currentForwardSpeed = boost(currentForwardSpeed, MAX_SPEED, BOOST, 1);
        } else {
            currentForwardSpeed = braking(currentForwardSpeed, BRAKING);
        }

        //Keys A, D
        if (Keyboard.isKeyDown(Keyboard.KEY_A) && check) {
            rotationY = ROTATION_SPEED * DisplayManager.getFarmeTimeSeconds();
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D) && check) {
            rotationY = -ROTATION_SPEED * DisplayManager.getFarmeTimeSeconds();
        } else {
            rotationY = 0;
        }

        //Keys E, Q
        if (Keyboard.isKeyDown(Keyboard.KEY_E) && check) {
            currentSideSpeed = boost(currentSideSpeed, SIDE_MAX_SPEED, BOOST, 1);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_Q) && check) {
            currentSideSpeed = boost(currentSideSpeed, SIDE_MAX_SPEED, BOOST, -1);
        } else {
            currentSideSpeed = braking(currentSideSpeed, BRAKING);
        }
    }
}
