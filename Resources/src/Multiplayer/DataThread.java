package Multiplayer;

import entities.Bullet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.lwjgl.util.vector.Vector3f;

public class DataThread extends Thread {

    ObjectInputStream in;
    ObjectOutputStream out;
    Multiplayer world;

    public DataThread(Multiplayer world, ObjectOutputStream out, ObjectInputStream in) {
        this.in = in;
        this.out = out;
        this.world = world;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!isInterrupted()) {
                    out.writeObject("" + world.players.get(0).getPosition().x);
                    out.writeObject("" + world.players.get(0).getPosition().y);
                    out.writeObject("" + world.players.get(0).getPosition().z);
                    out.writeObject("" + world.players.get(0).getRotX());
                    out.writeObject("" + world.players.get(0).getRotY());
                    out.writeObject("" + world.players.get(0).getRotZ());
                    out.writeObject("" + world.players.get(0).getXP());
                    out.writeObject("" + world.bulletCreate);

                    float x = Float.parseFloat((String) in.readObject());
                    float y = Float.parseFloat((String) in.readObject());
                    float z = Float.parseFloat((String) in.readObject());
                    world.players.get(1).setRotX(Float.parseFloat((String) in.readObject()));
                    world.players.get(1).setRotY(Float.parseFloat((String) in.readObject()));
                    world.players.get(1).setRotZ(Float.parseFloat((String) in.readObject()));
                    world.players.get(1).setXP(Integer.parseInt((String) in.readObject()));
                    world.players.get(1).setPosition(new Vector3f(x, y, z));
                    String s = (String) in.readObject();
                    if (Boolean.parseBoolean(s)) {
                        world.bullets.add(new Bullet(world, world.players.get(1)));
                    }
                } else {
                    throw new InterruptedException();
                }
            } catch (IOException | NumberFormatException | ClassNotFoundException | InterruptedException e) {

            }
        }
    }
}
