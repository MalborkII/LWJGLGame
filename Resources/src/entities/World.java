package entities;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import models.TexturedModel;
import org.apache.commons.collections4.ListUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import toolbox.Collisions;

public class World {

    public Loader loader;
    private MasterRenderer renderer;
    private Collisions collision;
    private List<Light> lights;
    public List<Bullet> bullets;
    private Chunks chunks;
    private FontType font;
    private Camera camera;

    private boolean closeRequested = false;
    public List<Player> players;

    private GUIText xp;
    private GUIText gameOver;
    private GUIText gameRule;
    private List<Entity> chunk;
    public TexturedModel bulletModel;
    public boolean bulletCreate = false;

    public void worldInit() {

        // Init block
        DisplayManager.createDisplay();
        loader = new Loader();
        renderer = new MasterRenderer(loader);
        collision = new Collisions();
        players = new ArrayList<>();
        lights = new ArrayList<>();
        bullets = new ArrayList<>();
        chunks = new Chunks(loader);
        font = new FontType(loader.loadTexture("Castellar"), new File("res/Castellar.fnt"));
        TextMaster.init(loader);

        // Player block
        TexturedModel playerShip = new TexturedModel(OBJLoader.loadObjModel("space_ship_1", loader),
                new ModelTexture(loader.loadTexture("space_ship_1")));
        ModelTexture playerShipTexture = playerShip.getTexture();
        playerShipTexture.setShineDamper(10);
        playerShipTexture.setReflectivity(1);
        players.add(new Player(playerShip, new Vector3f(0, 0, 0), 0, 0, 0, 1, players.size()));
        camera = new Camera(players.get(0));

        // Ligths
        lights.add(new Light(new Vector3f(7000, 20, 0), new Vector3f(1f, 1f, 1f)));
        lights.add(new Light(new Vector3f(-7000, 20, 0), new Vector3f(1f, 1f, 1f)));
        // Bullets block
        bulletModel = new TexturedModel(OBJLoader.loadObjModel("bullet_1", loader),
                new ModelTexture(loader.loadTexture("bullet_1")));

        // Asterods block
        chunk = chunks.generateChunk();

        // Texts
        xp = new GUIText(Integer.toString(players.get(0).getXP()), 2, font, new Vector2f(-0.47f, 0), 1f, true);
        gameOver = new GUIText("", 5, font, new Vector2f(0, 0.45f), 1f, true);
    }
    
    public void worldCleanUp() {
        TextMaster.cleanUp();
        loader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeDisplay();
    }

    public void worldLoopRenderer() {
        while (!Display.isCloseRequested() && !closeRequested) {

            renderer.render(lights, camera);
            camera.move();
            players.get(0).move();

            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                closeRequested = !closeRequested;
            }

            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getXP() > 0) {
                    renderer.processEntity(players.get(i));
                }
            }

            xp.remove();

            if (players.get(0).getXP() <= 0) {
                xp = new GUIText("0", 2, font, new Vector2f(-0.47f, 0), 1f, true);
                xp.setColour(0, 1, 0);
                gameOver.remove();
                gameOver = new GUIText("GAME OVER", 5, font, new Vector2f(0, 0.45f), 1f, true);
                gameOver.setColour(1, 0, 0);
            } else {
                xp = new GUIText(Integer.toString(players.get(0).getXP()), 2, font, new Vector2f(-0.47f, 0), 1f, true);
                xp.setColour(0, 1, 0);
            }

            bulletCreate = false;
            if (Mouse.isButtonDown(0) && players.get(0).checkReload()) {
                bullets.add(new Bullet(World.this, players.get(0)));
                bulletCreate = true;
            }

            chunk = ListUtils.union(chunk, chunks.chunkBuilder(players.get(0).getPosition()));
            for (int j = 0; j < players.size(); j++) {
                for (int i = 0; i < bullets.size(); i++) {
                    if (collision.getCollision(bullets.get(i), players.get(j)) && players.get(j).getID() != bullets.get(i).getCreatorID()) {
                        players.get(j).setXP(players.get(j).getXP() - 40);
                        bullets.remove(i);
                    }
                }
            }

            //Asteroids render and collisions
            for (int i = 0; i < chunk.size(); i++) {

                if (collision.getCollision(chunk.get(i), players.get(0))) {
                    players.get(0).setXP(players.get(0).getXP() - 20);
                    chunk.remove(i);
                    continue;
                }

                if (i < bullets.size()) {
                    bullets.get(i).move();
                    renderer.processEntity(bullets.get(i));
                    for (int j = 0; j < chunk.size(); j++) {
                        if (collision.getCollision(chunk.get(j), bullets.get(i))) {
                            chunk.remove(j);
                            bullets.remove(i);
                            break;
                        }
                    }
                }
                renderer.processEntity(chunk.get(i));
            }

            TextMaster.render();
            DisplayManager.updateDisplay();
        }
    }
}
