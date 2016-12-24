package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

public class Camera {

    private float distanceFromPlayer = 15;
    private float angleAroundPlayer = 0;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;

    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public void move() {

        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizantalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizantalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setAngleAroundPlayer(float angleAroundPlayer) {
        this.angleAroundPlayer = angleAroundPlayer;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public float getAngleAroundPlayer() {
        return angleAroundPlayer;
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance + 2;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f * DisplayManager.getFarmeTimeSeconds();
        distanceFromPlayer -= zoomLevel;
    }

    private void calculatePitch() {
        float pitchChange = Mouse.getDY() * 0.1f;
        pitch -= pitchChange;
    }

    private void calculateAngleAroundPlayer() {
        float angleChange = Mouse.getDX() * 0.3f;
        angleAroundPlayer -= angleChange;
    }
}

