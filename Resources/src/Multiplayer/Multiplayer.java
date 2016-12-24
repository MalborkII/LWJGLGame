package Multiplayer;

import entities.Player;
import entities.World;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class Multiplayer extends World {

    static ObjectOutputStream out;
    static ObjectInputStream in;
    static ServerSocket server;
    static Socket client;
    static Socket socket;
    static String IP;
    static int port = 25566;
    static DataThread dataThread;
    
    public Multiplayer(boolean host, String IP) {
        this.IP = IP;
        worldInit();

        TexturedModel playerShip = new TexturedModel(OBJLoader.loadObjModel("space_ship_1", loader),
                new ModelTexture(loader.loadTexture("space_ship_1")));
        ModelTexture playerShipTexture = playerShip.getTexture();
        playerShipTexture.setShineDamper(10);
        playerShipTexture.setReflectivity(1);
        players.add(new Player(playerShip, new Vector3f(0, 0, 0), 0, 0, 0, 1, players.size()));

        if (host) {
            try {
                server = new ServerSocket(port, 4, InetAddress.getByName(IP));
                client = server.accept();

                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());

                dataThread = new DataThread(Multiplayer.this, out, in);
                dataThread.start();

            } catch (IOException e) {
            }
        } else {
            try {
                socket = new Socket(IP, port);

                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

                dataThread = new DataThread(Multiplayer.this, out, in);
                dataThread.start();
            } catch (IOException e) {
            }
        }
        
        worldLoopRenderer();      
        worldCleanUp();
        dataThread.interrupt();
        dataThread.stop();
    }
}
