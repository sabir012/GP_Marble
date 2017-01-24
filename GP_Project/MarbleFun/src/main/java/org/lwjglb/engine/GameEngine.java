package org.lwjglb.engine;

public class GameEngine implements Runnable {

    public static final int TARGET_FPS = 75;

    public static final int TARGET_UPS = 30;

    
    private final Window window;

    private final Thread gameThread;

    private final Timer timer;

    private final IGameLogic gameLogic;

    private final MouseInput mouseInput;

    
    public GameEngine(String title, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
        gameThread = new Thread(this, "Game_Thread");
        window = new Window(title, width, height, vSync);
        timer = new Timer();
        mouseInput = new MouseInput();
        
        this.gameLogic = gameLogic;        
    }

    public void start() {
        String osName = System.getProperty("os.name");
        
        if ( osName.contains("Mac") ) {
            gameThread.run();
        } else {
            gameThread.start();
        }
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        mouseInput.init(window);
        gameLogic.init(window);
    }

    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if ( !window.isvSync() ) {
                sync();
            }
        }
    }

    protected void cleanup() {
        gameLogic.cleanup();                
    }
    
    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
    }

    protected void input() {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    protected void update(float interval) {
        gameLogic.update(interval, mouseInput);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}
