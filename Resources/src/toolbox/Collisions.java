package toolbox;

import entities.Entity;

public class Collisions {

    public boolean getCollision(Entity objectA, Entity objectB) {

        float distance = (float) Math.sqrt(Math.pow(objectA.getPosition().x - objectB.getPosition().x, 2)
                + Math.pow(objectA.getPosition().y - objectB.getPosition().y, 2)
                + Math.pow(objectA.getPosition().z - objectB.getPosition().z, 2));
        
        return distance <= (objectA.getScale() + objectB.getScale()) * 1.5;
    }
}
