package engineTester;

import entities.World;

public class SinglePlayer {
    public void Start(){
        World world = new World();
        world.worldInit();
        world.worldLoopRenderer();
        world.worldCleanUp();
    }
}
