package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL33;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Font {
	private String name;
	private int numberOfFontCharacters;
	private FontCharacter[] fontCharacters;

	private float size;
	private int interline;
	private int fontImageID;

	public Font(String name, float size, int interline) {
		this.name = name;
		this.size = size;
		this.interline = interline;
		readingFontFile();
		loadFontTexture();
	}

	public void readingFontFile() {
		int fontIndex = 0;
		int ID = 0;
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		int xOffset = 0;
		int yOffset = 0;
		int xAdvance = 0;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File fontFile = new File(classLoader.getResource("fonts/" + name + ".fnt").getFile());
			Scanner reader = new Scanner(fontFile);
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				String[] lineSplit = line.split("\\s+");

				// Find number of font chars
				if (lineSplit.length == 2 && lineSplit[0].equals("chars")) {
					numberOfFontCharacters = Integer.parseInt(lineSplit[1].split("=")[1]) + 1;
					fontCharacters = new FontCharacter[numberOfFontCharacters];
				}

				// Fill information of font
				if (lineSplit.length == 11 && lineSplit[0].equals("char")) {
					// numberOfFontCharacters = Integer.parseInt(lineSplit[1].split("=")[1]);

					ID = Integer.parseInt(lineSplit[1].split("=")[1]);
					x = Integer.parseInt(lineSplit[2].split("=")[1]);
					y = Integer.parseInt(lineSplit[3].split("=")[1]);
					width = Integer.parseInt(lineSplit[4].split("=")[1]);
					height = Integer.parseInt(lineSplit[5].split("=")[1]);
					xOffset = Integer.parseInt(lineSplit[6].split("=")[1]);
					yOffset = Integer.parseInt(lineSplit[7].split("=")[1]);
					xAdvance = Integer.parseInt(lineSplit[8].split("=")[1]);

					fontCharacters[fontIndex] = new FontCharacter();
					fontCharacters[fontIndex].ID = ID;
					fontCharacters[fontIndex].x = x;
					fontCharacters[fontIndex].y = y;
					fontCharacters[fontIndex].width = width;
					fontCharacters[fontIndex].height = height;
					fontCharacters[fontIndex].xOffset = xOffset;
					fontCharacters[fontIndex].yOffset = yOffset;
					fontCharacters[fontIndex].xAdvance = xAdvance;
					fontIndex++;
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void loadFontTexture() {
		int width;
		int height;
		ByteBuffer buffer;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);

			String fontFileImageName = name + ".png";

			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("fonts/" + fontFileImageName).getFile());
			String filePath = file.getAbsolutePath();
			buffer = STBImage.stbi_load(filePath, w, h, channels, 4);
			if (buffer == null) {
				throw new Exception("Can't load file " + fontFileImageName + " " + STBImage.stbi_failure_reason());
			}
			width = w.get();
			height = h.get();

			fontImageID = GL33.glGenTextures();

			GL33.glBindTexture(GL33.GL_TEXTURE_2D, fontImageID);
			GL33.glPixelStorei(GL33.GL_UNPACK_ALIGNMENT, 1);

			GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA, width, height, 0, GL33.GL_RGBA,
					GL33.GL_UNSIGNED_BYTE, buffer);

			GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D);
			STBImage.stbi_image_free(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FontCharacter findFromAscii(int ascii) {
		for (FontCharacter fc : fontCharacters) {
			if (fc.ID == ascii) {
				return fc;
			}
		}
		return null;
	}

	public int getInterline() {
		return interline;
	}

	public float getSize() {
		return size;
	}

	public int getFontImageID() {
		return fontImageID;
	}
}
