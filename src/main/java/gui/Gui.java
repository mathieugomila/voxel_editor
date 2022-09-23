package gui;

import org.lwjgl.opengl.GL33;

public class Gui {
	protected int vaoID;
	protected int[] vboID = new int[2];

	protected int nbrOfVertices;

	public Gui() {
		createBuffers();
	}

	public void createBuffers() {
		// Create VAO
		vaoID = GL33.glGenVertexArrays();
		bindVAO();

		// Create VBO
		GL33.glGenBuffers(vboID);
	}

	public void draw() {
		if (nbrOfVertices == 0) {
			return;
		}
		bindVAO();

		GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, nbrOfVertices);

		unbindVAO();
	}

	public void bindVAO() {
		GL33.glBindVertexArray(vaoID);
	}

	public void unbindVAO() {
		GL33.glBindVertexArray(0);
	}

	public void bindVBO(int index) {
		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboID[index]);
	}

	public void unbindVBO() {
		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
	}
}
