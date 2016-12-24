package entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.ModelTexture;
import renderEngine.OBJLoader;

public class Chunks {

    private final int ZONE = 200;
    private final int NUMBER = 50;
    private Vector3f CURRENT_CHUNK = new Vector3f(0, 0, 0);
    private final TexturedModel asteroid;

    private static final String CHUNK_LOG = "src/logs/chunkLog.txt";

    public Chunks(Loader loader) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(CHUNK_LOG, false));
        } catch (IOException ex) {
            Logger.getLogger(Chunks.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.asteroid = new TexturedModel(OBJLoader.loadObjModel("astr_1", loader),
                new ModelTexture(loader.loadTexture("astr_1")));
        ModelTexture texture1 = this.asteroid.getTexture();
        texture1.setShineDamper(1);
        texture1.setReflectivity((float) 0.1);
    }

    public List<Entity> chunkBuilder(Vector3f playerPosition) {

        List<Entity> chunk = new ArrayList<>();

        if (playerPosition.z - CURRENT_CHUNK.z * ZONE >= ZONE / 2) {
            CURRENT_CHUNK.z += 1;
        } 
        if (playerPosition.z - CURRENT_CHUNK.z  * ZONE <= -ZONE / 2) {
            CURRENT_CHUNK.z -= 1;
        }

        if (playerPosition.x - CURRENT_CHUNK.x  * ZONE >= ZONE / 2) {
            CURRENT_CHUNK.x += 1;
        } 
        if (playerPosition.x - CURRENT_CHUNK.x  * ZONE <= -ZONE / 2) {
            CURRENT_CHUNK.x -= 1;
        }

        if (playerPosition.y - CURRENT_CHUNK.y  * ZONE >= ZONE / 2) {
            CURRENT_CHUNK.y += 1;
        } 
        if (playerPosition.y - CURRENT_CHUNK.y  * ZONE <= -ZONE / 2) {
            CURRENT_CHUNK.y -= 1;
        }

        CURRENT_CHUNK = new Vector3f(CURRENT_CHUNK.x, CURRENT_CHUNK.y, CURRENT_CHUNK.z);
        
        try {
            String chunkLogLine;
            BufferedReader reader = new BufferedReader(new FileReader(CHUNK_LOG));
            BufferedWriter writer = new BufferedWriter(new FileWriter(CHUNK_LOG, true));

            while ((chunkLogLine = reader.readLine()) != null) {
                if (chunkLogLine.equals(CURRENT_CHUNK.x + "/" + CURRENT_CHUNK.y + "/" + CURRENT_CHUNK.z)) {
                    return chunk;
                }
            }

            chunk = generateChunk();
            writer.write(CURRENT_CHUNK.x + "/" + CURRENT_CHUNK.y + "/" + CURRENT_CHUNK.z + "\n");
            writer.close();
        } catch (IOException ex) {
            System.out.println("can't load log");
        }
        return chunk;
    }

    public List<Entity> generateChunk() {

        Random random = new Random();

        List<Entity> asteroids = new ArrayList<>();

        for (int i = 0; i < NUMBER; i++) {
            float x = 2 * ZONE * (random.nextFloat() + CURRENT_CHUNK.x - (float) 0.5);
            float y = 2 * ZONE * (random.nextFloat() + CURRENT_CHUNK.y - (float) 0.5);
            float z = 2 * ZONE * (random.nextFloat() + CURRENT_CHUNK.z - (float) 0.5);
            float scale = random.nextInt(30);
            asteroids.add(new Entity(this.asteroid, new Vector3f(x, y, z),
                    random.nextFloat() * 180f, random.nextFloat() * 180f, 0f, scale));
        }
        return asteroids;
    }
}
