package gui;

import org.lwjgl.opengl.GL33;

import shaders.Shader;

public class BackgroundGUI extends Gui {
	private float xOffset;
	private float yOffset;

	private float width;
	private float height;

	private Shader backGroundShader;

	public BackgroundGUI(float xOffset, float yOffset, float width, float height) {
		super();

		backGroundShader = new Shader("backgroundGUI");

		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;

		generateBuffers();
	}

	public void generateBuffers() {
		float[] vertices = new float[] {
				xOffset * 2 - 1, yOffset * 2 - 1,
				xOffset * 2 - 1, (yOffset + height) * 2 - 1,
				(xOffset + width) * 2 - 1, yOffset * 2 - 1,
				(xOffset + width) * 2 - 1, yOffset * 2 - 1,
				xOffset * 2 - 1, (yOffset + height) * 2 - 1,
				(xOffset + width) * 2 - 1, (yOffset + height) * 2 - 1,
		};

		nbrOfVertices = 6;

		// bind VAO
		bindVAO();

		// Fill VBO n1
		bindVBO(0);
		GL33.glEnableVertexAttribArray(0);
		GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW);
		GL33.glVertexAttribPointer(0, 2, GL33.GL_FLOAT, false, 0, 0);
		unbindVBO();

		// Unbind everything
		unbindVAO();
	}

	public void draw() {
		backGroundShader.start();
		GL33.glEnable(GL33.GL_BLEND);
		GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);
		super.draw();
		GL33.glDisable(GL33.GL_BLEND);
		backGroundShader.stop();
	}
}
