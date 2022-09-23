import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import devTools.ProgramTime;
import gameEngine.World;
import shaders.Shader;
import util.Vector3i;
import camera.Player;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {

	// The window handle
	private long window;

	// The window parameters
	private int WINDOW_WIDTH = 1500;
	private int WINDOW_HEIGHT = 800;
	private String WINDOW_NAME = "Simple game";
	private boolean isFullScreen = false;

	// Graphical options
	private boolean vSyncEnable = false;

	// Time
	private ProgramTime programTime;

	// Player
	private Player player;

	// World
	private World world;

	// Shader
	private Shader basicShader;

	int previousSecond = -1;

	public void initialize() {
		System.out.println("----Initialize----");
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		System.out.println("Glfw has correctly been initialized");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will not be resizable

		// Using OpenGL 3.3
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

		// Create the window
		if (!isFullScreen) {
			window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_NAME, NULL, NULL);
			if (window == NULL)
				throw new RuntimeException("Failed to create the GLFW window");
		} else {
			var primary = GLFW.glfwGetPrimaryMonitor();
			var videoMode = GLFW.glfwGetVideoMode(primary);
			WINDOW_WIDTH = videoMode.width();
			WINDOW_HEIGHT = videoMode.height();
			window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_NAME, primary, NULL);
			if (window == NULL)
				throw new RuntimeException("Failed to create the GLFW window");
		}

		System.out.println("Window has correctly been created");

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		// Enable v-sync ?
		if (vSyncEnable) {
			glfwSwapInterval(1);
		} else {
			glfwSwapInterval(0);
		}

		// Make the window visible
		glfwShowWindow(window);
	}

	private void loadContent() {
		System.out.println("----Load content----");

		// time
		programTime = new ProgramTime();

		// player update
		player = new Player();

		// The world
		world = new World();

		// Shader
		basicShader = new Shader("basic");

		return;
	}

	private boolean update() {
		GL.createCapabilities();
		glfwPollEvents();

		// Check if window has to be closed
		if (glfwWindowShouldClose(window) || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
			return false;
		}

		// Time update
		float timeSinceLastUpdate = programTime.update();

		// player update
		player.update(window, world.getCenter(), timeSinceLastUpdate);

		int y = (int) programTime.getTimeSinceBeginning();
		if (previousSecond < y) {
			previousSecond = y;
			for (int x = -y; x <= y; x++) {
				for (int z = -y; z <= y; z++) {
					world.addBloc(new Vector3i(x, y, z));
				}
			}
			world.generateMesh();
		}

		return true;
	}

	private void draw() {

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glFrontFace(GL_CW);

		glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Here
		basicShader.start();
		basicShader.setUniformMatrix4fValue("MVP", player.getCamera().getMVPMatrix());
		world.draw();
		basicShader.stop();

		glfwSwapBuffers(window); // swap the color buffers
	}

	public void terminate() {
		System.out.println("----Terminate----");

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public static void main(String[] args) {
		// mvn clean compile exec:java
		boolean gameShouldContinue = true;
		Game game = new Game();

		// Initializing program and loading some contents
		game.initialize();
		game.loadContent();

		// Updating game and drawing elements
		System.out.println("----Update & Draw----");
		while (gameShouldContinue) {
			// Updating and drawing game
			gameShouldContinue = game.update();
			game.draw();

			// If game has to stop : break the while loop
			if (!gameShouldContinue)
				break;
		}
		game.terminate();
	}
}
