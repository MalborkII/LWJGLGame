package renderEngine;


import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

    private static final int FPS_CAP = 120;

    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            Mouse.setGrabbed(true);
            Display.setFullscreen(true);
            DisplayMode mode = Display.getDesktopDisplayMode();
            Display.setDisplayMode(mode);
            Display.create(new PixelFormat(), attribs);
            
            Display.setTitle("Course Project");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        lastFrameTime = getCurrentTime();

    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/500f;
        lastFrameTime = currentFrameTime;
    }
    
    public static float getFarmeTimeSeconds(){
        return delta;
    }
    
    public static void closeDisplay() {
        Display.destroy();
    }
    
    private static long getCurrentTime(){
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }
}
