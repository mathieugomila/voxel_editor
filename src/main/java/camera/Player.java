package camera;

import util.Matrix4f;
import util.Vector3f;

import org.lwjgl.glfw.GLFW;

public class Player {
	private Vector3f position = new Vector3f(0.5f, 150, -2.0f);
	private Camera camera;
	private Vector3f cameraRotation = new Vector3f(0, 0, 0); // in degree, (0, 0, 0) == facing +z
	private Vector3f headForward;

	private double lastMousePositionX, lastMousePositionY;

	// Movement
	private float mouseSensibility = 0.1f;

	public Player() {
		camera = new Camera(position, cameraRotation);
	}

	public void update(long window, Vector3f centerPosition, float timeSinceLastUpdate) {

		// Update player body and head position using Mouse/Keyboards
		orientateCamera(window); // using mouse
		calculForwardVectors();
		moveCamera(centerPosition); // correctly position using forward vector

		// Update camera
		camera.update(position, cameraRotation, window);
	}

	public void moveCamera(Vector3f center) {
		position = center.add(headForward.multiply(-1).multiply(12));
	}

	public void orientateCamera(long window) {

		// get mouse position and memorize it
		double[] xpos = new double[1];
		double[] ypos = new double[1];
		GLFW.glfwGetCursorPos(window, xpos, ypos);

		// Change camera orientation only when right mouse button is clicked
		if (GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {

			// centering mouse

			// calculating mouse movement since last update
			cameraRotation.x += mouseSensibility * (lastMousePositionX - ypos[0]);
			cameraRotation.y += mouseSensibility * (lastMousePositionY - xpos[0]);

			if (cameraRotation.x > 90) {

				cameraRotation.x = 90;
			}
			if (cameraRotation.x < -90) {

				cameraRotation.x = -90;
			}

			// Reposition cursor if it goes out of screen
			int[] windowWidth = new int[1];
			int[] windowHeight = new int[1];
			GLFW.glfwGetWindowSize(window, windowWidth, windowHeight);
			if (xpos[0] <= 0 || xpos[0] > windowWidth[0]) {
				xpos[0] = xpos[0] <= 0 ? windowWidth[0] - 10 : 10;
				GLFW.glfwSetCursorPos(window, xpos[0], ypos[0]);
			}
			if (ypos[0] <= 0 || ypos[0] > windowHeight[0]) {
				ypos[0] = ypos[0] <= 0 ? windowHeight[0] - 10 : 10;
				GLFW.glfwSetCursorPos(window, xpos[0], ypos[0]);
			}
		}

		lastMousePositionX = ypos[0];
		lastMousePositionY = xpos[0];
	}

	public void calculForwardVectors() {

		headForward = Matrix4f.transpose(Matrix4f.view(position, cameraRotation)).multiply(new Vector3f(0, 0, 1));
		headForward = headForward.multiply(-1);
		headForward.normalize();
	}

	public Camera getCamera() {
		return camera;
	}
}
