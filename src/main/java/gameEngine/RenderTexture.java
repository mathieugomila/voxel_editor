package gameEngine;

import org.lwjgl.opengl.GL33;

public class RenderTexture {
    int framebufferName;
    int renderedTexture;
    int depthrenderbuffer;

    int width, height;

    public RenderTexture(int width, int height) {
        this.width = width;
        this.height = height;

        framebufferName = GL33.glGenFramebuffers();
        GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, framebufferName);

        // The texture we're going to render to
        renderedTexture = GL33.glGenTextures();

        // "Bind" the newly created texture : all future texture functions will modify
        // this texture
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, renderedTexture);

        // Give an empty image to OpenGL ( the last "0" )
        GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGB, width, height, 0, GL33.GL_RGB, GL33.GL_UNSIGNED_BYTE, 0);

        // Poor filtering. Needed !
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);

        // The depth buffer

        depthrenderbuffer = GL33.glGenRenderbuffers();
        GL33.glBindRenderbuffer(GL33.GL_RENDERBUFFER, depthrenderbuffer);
        GL33.glRenderbufferStorage(GL33.GL_RENDERBUFFER, GL33.GL_DEPTH_COMPONENT, width, height);
        GL33.glFramebufferRenderbuffer(GL33.GL_FRAMEBUFFER, GL33.GL_DEPTH_ATTACHMENT,
                GL33.GL_RENDERBUFFER, depthrenderbuffer);

        // Set "renderedTexture" as our colour attachement #0
        GL33.glFramebufferTexture(GL33.GL_FRAMEBUFFER, GL33.GL_COLOR_ATTACHMENT0, renderedTexture, 0);
    }

    public void bind() {
        GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, framebufferName);
        GL33.glEnable(GL33.GL_DEPTH_TEST);
        GL33.glEnable(GL33.GL_CULL_FACE);
        GL33.glFrontFace(GL33.GL_CW);
        GL33.glViewport(0, 0, width, height);
        GL33.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);
    }

    public void unbind() {
        GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, 0);
    }
}
