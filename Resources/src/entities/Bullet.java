package entities;

import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

public class Bullet extends Entity {

    private final float SPEED = 200;
    private final int creatorID;

    public int getCreatorID() {
        return creatorID;
    }

    public Bullet(World world, Player player) {
        super(world.bulletModel, new Vector3f(player.getPosition()), player.getRotX(), player.getRotY(), player.getRotZ(), 1);
        this.creatorID = player.getID();
    }

    public void move() {
        float distance = SPEED * DisplayManager.getFarmeTimeSeconds();

        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));

        super.increasePosition(dx, 0, dz);
    }
}
