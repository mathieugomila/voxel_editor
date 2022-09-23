package gui;

import org.lwjgl.opengl.GL33;

import shaders.Shader;

public class TextGui extends Gui
{
	private float    xOffset;
	private float    yOffset;
	private float    width;
	private float    height;


	private Shader   textGUIShader;

	private String   message	     = "Console hasn't been set up yet";
	private String[] lastMessages	     = new String[20];

	private Font     font;

	public TextGui(float xOffset, float yOffset, float width, float height, Font font)
	{
		super();

		textGUIShader = new Shader("textGUI");

		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;

		this.font = font;

		for (int i = 0; i < lastMessages.length; i++)
		{
			lastMessages[i] = "";
		}

		generateBuffers();
	}

	public void addMessage(String newMessage)
	{
		for (int i = lastMessages.length - 1; i > 0; i--)
		{
			lastMessages[i] = lastMessages[i - 1];
		}
		lastMessages[0] = newMessage;
		
		createMessage();
		generateBuffers();
	}

	public void generateBuffers()
	{
		float[] vertices	= new float[message.length() * 6 * 2];
		float[] texCoords	= new float[message.length() * 6 * 2];

		int	  cursorX	= 0;
		int	  cursorY	= -(int) (font.getInterline() * (int) (height / ((float) font.getInterline() * font.getSize()))) + font.getInterline();

		int	  index	= 0;
		for (char ch : message.toCharArray())
		{
			int		  ascii = (int) ch;
			FontCharacter fc	  = font.findFromAscii(ascii);
			if (ascii == 10)
			{
				cursorY += font.getInterline();
				cursorX = 0;
			}
			else if (ascii == 9)
			{
				cursorX += 50;
			}
			else if (fc != null)
			{
				float	xOffsetLetter = xOffset + ((float) (cursorX) * font.getSize() + (fc.xOffset * font.getSize()));
				float	yOffsetLetter = yOffset + height + ((float) (cursorY) * font.getSize() - (fc.yOffset + fc.height) * font.getSize());

				float	widthLetter	  = ((float) fc.width * font.getSize());
				float	heightLetter  = ((float) fc.height * font.getSize());

				vertices[index * 12 + 0] = (xOffsetLetter + 0) * 2.0f - 1.0f;
				vertices[index * 12 + 1] = (yOffsetLetter + 0) * 2.0f - 1.0f;
				vertices[index * 12 + 2] = (xOffsetLetter + 0) * 2.0f - 1.0f;
				vertices[index * 12 + 3] = (yOffsetLetter + heightLetter) * 2.0f - 1.0f;
				vertices[index * 12 + 4] = (xOffsetLetter + widthLetter) * 2.0f - 1.0f;
				vertices[index * 12 + 5] = (yOffsetLetter + 0) * 2.0f - 1.0f;
				vertices[index * 12 + 6] = (xOffsetLetter + widthLetter) * 2.0f - 1.0f;
				vertices[index * 12 + 7] = (yOffsetLetter + 0) * 2.0f - 1.0f;
				vertices[index * 12 + 8] = (xOffsetLetter + 0) * 2.0f - 1.0f;
				vertices[index * 12 + 9] = (yOffsetLetter + heightLetter) * 2.0f - 1.0f;
				vertices[index * 12 + 10] = (xOffsetLetter + widthLetter) * 2.0f - 1.0f;
				vertices[index * 12 + 11] = (yOffsetLetter + heightLetter) * 2.0f - 1.0f;
				texCoords[index * 12 + 0] = (float) (fc.x) / 512.0f;
				texCoords[index * 12 + 1] = (float) (fc.y + fc.height) / 512.0f;
				texCoords[index * 12 + 2] = (float) (fc.x) / 512.0f;
				texCoords[index * 12 + 3] = (float) (fc.y) / 512.0f;
				texCoords[index * 12 + 4] = (float) (fc.x + fc.width) / 512.0f;
				texCoords[index * 12 + 5] = (float) (fc.y + fc.height) / 512.0f;
				texCoords[index * 12 + 6] = (float) (fc.x + fc.width) / 512.0f;
				texCoords[index * 12 + 7] = (float) (fc.y + fc.height) / 512.0f;
				texCoords[index * 12 + 8] = (float) (fc.x) / 512.0f;
				texCoords[index * 12 + 9] = (float) (fc.y) / 512.0f;
				texCoords[index * 12 + 10] = (float) (fc.x + fc.width) / 512.0f;
				texCoords[index * 12 + 11] = (float) (fc.y) / 512.0f;

				cursorX += fc.xAdvance;
				index++;
			}
			else
			{
				System.out.println("TextGUI : Unknown character, code ascii " + ascii);
			}

		}

		nbrOfVertices = vertices.length / 2;

		// bind VAO
		bindVAO();

		// Fill VBO n1 : vertices
		bindVBO(0);
		GL33.glEnableVertexAttribArray(0);
		GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW);
		GL33.glVertexAttribPointer(0, 2, GL33.GL_FLOAT, false, 0, 0);
		unbindVBO();

		// Fill VBO n2 : texCoord
		bindVBO(1);
		GL33.glEnableVertexAttribArray(1);
		GL33.glBufferData(GL33.GL_ARRAY_BUFFER, texCoords, GL33.GL_STATIC_DRAW);
		GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 0, 0);
		unbindVBO();

		// Unbind everything
		unbindVAO();
	}

	public void draw()
	{
		textGUIShader.start();
		GL33.glActiveTexture(GL33.GL_TEXTURE0);
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, font.getFontImageID());

		textGUIShader.setUniformFloat4Value("area", xOffset, yOffset, width, height);
		
		GL33.glEnable(GL33.GL_BLEND);
		GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);
		super.draw();
		GL33.glDisable(GL33.GL_BLEND);
		textGUIShader.stop();
	}

	public void createMessage()
	{
		String str = new String();
		for (int i = 0; i < lastMessages.length; i++)
		{
			str += lastMessages[i].trim() + "\n";
		}
		message = str;
	}
}
