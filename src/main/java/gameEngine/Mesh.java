package gameEngine;

import java.util.List;

import org.lwjgl.opengl.GL33;

import util.Vector3f;
import util.Vector3i;

public class Mesh {
    private int vaoID;
    private int[] vboID = new int[4]; // position, normal and colors

    private float[] positions;
    private float[] normals;
    private float[] colors;
    private float[] ID;

    private int nbrOfCubes = 0;

    private static Vector3f[] cubeVertices = new Vector3f[] {
            new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, 1, 0), new Vector3f(1, 1, 0),
            new Vector3f(0, 0, 1), new Vector3f(1, 0, 1), new Vector3f(0, 1, 1), new Vector3f(1, 1, 1)
    };

    private static int[] cubeIndices = new int[] {
            5, 3, 1, 5, 7, 3, 0, 6, 4, 0, 2, 6, // Faces X
            3, 6, 2, 3, 7, 6, 5, 0, 4, 5, 1, 0, // Faces Y
            4, 7, 5, 4, 6, 7, 1, 2, 0, 1, 3, 2, // Faces Z
    };

    private static Vector3f[] cubeNormals = new Vector3f[] {
            new Vector3f(1, 0, 0), new Vector3f(-1, 0, 0), new Vector3f(0, 1, 0), new Vector3f(0, -1, 0),
            new Vector3f(0, 0, 1), new Vector3f(0, 0, -1),
    };

    public static Vector3i[] nearCubesSSAO = new Vector3i[] {
            new Vector3i(1, -1, 0), new Vector3i(1, 0, 1), new Vector3i(1, -1, 1), // 5
            new Vector3i(1, 0, -1), new Vector3i(1, 1, 0), new Vector3i(1, 1, -1), // 3
            new Vector3i(1, 0, -1), new Vector3i(1, -1, 0), new Vector3i(1, -1, -1), // 1
            new Vector3i(1, -1, 0), new Vector3i(1, 0, 1), new Vector3i(1, -1, 1), // 5
            new Vector3i(1, 0, 1), new Vector3i(1, 1, 0), new Vector3i(1, 1, 1), // 7
            new Vector3i(1, 0, -1), new Vector3i(1, 1, 0), new Vector3i(1, 1, -1), // 3

            new Vector3i(-1, 0, -1), new Vector3i(-1, -1, 0), new Vector3i(-1, -1, -1), // 0
            new Vector3i(-1, 0, 1), new Vector3i(-1, 1, 0), new Vector3i(-1, 1, 1), // 6
            new Vector3i(-1, -1, 0), new Vector3i(-1, 0, 1), new Vector3i(-1, -1, 1), // 4
            new Vector3i(-1, 0, -1), new Vector3i(-1, -1, 0), new Vector3i(-1, -1, -1), // 0
            new Vector3i(-1, 0, -1), new Vector3i(-1, 1, 0), new Vector3i(-1, 1, -1), // 2
            new Vector3i(-1, 0, 1), new Vector3i(-1, 1, 0), new Vector3i(-1, 1, 1), // 6

            new Vector3i(0, 1, -1), new Vector3i(1, 1, 0), new Vector3i(1, 1, -1), // 3
            new Vector3i(0, 1, 1), new Vector3i(-1, 1, 0), new Vector3i(-1, 1, 1), // 6
            new Vector3i(-1, 1, 0), new Vector3i(0, 1, -1), new Vector3i(-1, 1, -1), // 2
            new Vector3i(0, 1, -1), new Vector3i(1, 1, 0), new Vector3i(1, 1, -1), // 3
            new Vector3i(0, 1, 1), new Vector3i(1, 1, 0), new Vector3i(1, 1, 1), // 7
            new Vector3i(0, 1, 1), new Vector3i(-1, 1, 0), new Vector3i(-1, 1, 1), // 6

            new Vector3i(0, -1, 1), new Vector3i(1, -1, 0), new Vector3i(1, -1, 1), // 5
            new Vector3i(-1, -1, 0), new Vector3i(0, -1, -1), new Vector3i(-1, -1, -1), // 0
            new Vector3i(0, -1, 1), new Vector3i(-1, -1, 0), new Vector3i(-1, -1, 1), // 4
            new Vector3i(0, -1, 1), new Vector3i(1, -1, 0), new Vector3i(1, -1, 1), // 5
            new Vector3i(0, -1, -1), new Vector3i(1, -1, 0), new Vector3i(1, -1, -1), // 1
            new Vector3i(-1, -1, 0), new Vector3i(0, -1, -1), new Vector3i(-1, -1, -1), // 0

            new Vector3i(0, -1, 1), new Vector3i(-1, 0, 1), new Vector3i(-1, -1, 1), // 4
            new Vector3i(0, 1, 1), new Vector3i(1, 0, 1), new Vector3i(1, 1, 1), // 7
            new Vector3i(0, -1, 1), new Vector3i(1, 0, 1), new Vector3i(1, -1, 1), // 5
            new Vector3i(0, -1, 1), new Vector3i(-1, 0, 1), new Vector3i(-1, -1, 1), // 4
            new Vector3i(0, 1, 1), new Vector3i(-1, 0, 1), new Vector3i(-1, 1, 1), // 6
            new Vector3i(0, 1, 1), new Vector3i(1, 0, 1), new Vector3i(1, 1, 1), // 7

            new Vector3i(0, -1, -1), new Vector3i(1, 0, -1), new Vector3i(1, -1, -1), // 1
            new Vector3i(0, 1, -1), new Vector3i(-1, 0, -1), new Vector3i(-1, 1, -1), // 2
            new Vector3i(0, -1, -1), new Vector3i(-1, 0, -1), new Vector3i(-1, -1, -1), // 0
            new Vector3i(0, -1, -1), new Vector3i(1, 0, -1), new Vector3i(1, -1, -1), // 1
            new Vector3i(0, 1, -1), new Vector3i(1, 0, -1), new Vector3i(1, 1, -1), // 3
            new Vector3i(0, 1, -1), new Vector3i(-1, 0, -1), new Vector3i(-1, 1, -1), // 2
    };

    public Mesh() {
        createBuffers();
    }

    public void generateMesh(List<Vector3i> listOfBlocs) {
        positions = new float[listOfBlocs.size() * 36 * 3];
        normals = new float[listOfBlocs.size() * 36 * 3];
        colors = new float[listOfBlocs.size() * 36 * 3];
        ID = new float[listOfBlocs.size() * 36];

        int length = 0;
        nbrOfCubes = 0;
        int idCounter = 1; // ID = 0 == void
        for (Vector3i pos : listOfBlocs) {
            for (int faces = 0; faces < 6; faces++) {
                for (int i = faces * 6; i < faces * 6 + 6; i++) {

                    Vector3f vertexPosition = cubeVertices[cubeIndices[i]];

                    positions[3 * length + 0] = (float) pos.x + vertexPosition.x;
                    positions[3 * length + 1] = (float) pos.y + vertexPosition.y;
                    positions[3 * length + 2] = (float) pos.z + vertexPosition.z;

                    normals[3 * length + 0] = (float) cubeNormals[faces].x;
                    normals[3 * length + 1] = (float) cubeNormals[faces].y;
                    normals[3 * length + 2] = (float) cubeNormals[faces].z;

                    colors[3 * length + 0] = (float) 0.9f;
                    colors[3 * length + 1] = (float) 0.0f;
                    colors[3 * length + 2] = (float) 0.7f;

                    ID[length] = (float) idCounter;

                    length++;

                }
                idCounter++;
            }

        }

        nbrOfCubes = listOfBlocs.size();
        System.out.println("Nbr of cubes :" + String.valueOf(nbrOfCubes));
        System.out.println("Quantity of data :" + String.valueOf(nbrOfCubes * 36 * 48));
        generateBuffers();
    }

    public void createBuffers() {
        // Create VAO
        vaoID = GL33.glGenVertexArrays();
        bindVAO();

        // Create VBO
        GL33.glGenBuffers(vboID);
    }

    public void generateBuffers() {
        if (vaoID == 0) {
            createBuffers();
        }

        // bind VAO
        bindVAO();

        // Fill VBO n 1
        bindVBO(0);
        GL33.glEnableVertexAttribArray(0);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, positions, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        unbindVBO();

        // Fill VBO n 2
        bindVBO(1);
        GL33.glEnableVertexAttribArray(1);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, normals, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 0, 0);
        unbindVBO();

        // Fill VBO n 3
        bindVBO(2);
        GL33.glEnableVertexAttribArray(2);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, colors, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(2, 3, GL33.GL_FLOAT, false, 0, 0);
        unbindVBO();

        // Fill VBO n 4
        bindVBO(3);
        GL33.glEnableVertexAttribArray(3);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, ID, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(3, 1, GL33.GL_FLOAT, false, 0, 0);
        unbindVBO();

        // Unbind everything
        unbindVAO();
    }

    public void draw() {
        // Draw
        bindVAO();
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, nbrOfCubes * 36);
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

    private Vector3i getAmbientOcclusionFactor(Vector3i blocPosition, Vector3i otherBlocPosition, int faces,
            int vertexIndex) {
        Vector3i ssaoFactor = new Vector3i(0, 0, 0);
        Vector3i differenceOfPosition = otherBlocPosition.sub(blocPosition);

        if (faces == 0 || faces == 1) {
            if (differenceOfPosition.x == nearCubesSSAO[3 * vertexIndex + 0].x
                    && differenceOfPosition.y == nearCubesSSAO[3
                            * vertexIndex + 0].y
                    && differenceOfPosition.z == nearCubesSSAO[3 * vertexIndex + 0].z) {
                if (nearCubesSSAO[3 * vertexIndex + 0].y != 0) {
                    ssaoFactor.z += 1;
                }
                if (nearCubesSSAO[3 * vertexIndex + 0].z != 0) {
                    ssaoFactor.y += 1;
                }
            }
            if (differenceOfPosition.x == nearCubesSSAO[3 * vertexIndex + 1].x
                    && differenceOfPosition.y == nearCubesSSAO[3
                            * vertexIndex + 1].y
                    && differenceOfPosition.z == nearCubesSSAO[3 * vertexIndex + 1].z) {
                if (nearCubesSSAO[3 * vertexIndex + 1].y != 0) {
                    ssaoFactor.z += 1;
                }
                if (nearCubesSSAO[3 * vertexIndex + 1].z != 0) {
                    ssaoFactor.y += 1;
                }
            }

        }
        if (faces == 2 || faces == 3) {
            if (differenceOfPosition.x == nearCubesSSAO[3 * vertexIndex + 0].x
                    && differenceOfPosition.y == nearCubesSSAO[3
                            * vertexIndex + 0].y
                    && differenceOfPosition.z == nearCubesSSAO[3 * vertexIndex + 0].z) {
                if (nearCubesSSAO[3 * vertexIndex + 0].x != 0) {
                    ssaoFactor.z += 1;
                }
                if (nearCubesSSAO[3 * vertexIndex + 0].z != 0) {
                    ssaoFactor.x += 1;
                }
            }
            if (differenceOfPosition.x == nearCubesSSAO[3 * vertexIndex + 1].x
                    && differenceOfPosition.y == nearCubesSSAO[3
                            * vertexIndex + 1].y
                    && differenceOfPosition.z == nearCubesSSAO[3 * vertexIndex + 1].z) {
                if (nearCubesSSAO[3 * vertexIndex + 1].x != 0) {
                    ssaoFactor.z += 1;
                }
                if (nearCubesSSAO[3 * vertexIndex + 1].z != 0) {
                    ssaoFactor.x += 1;
                }
            }

        }
        if (faces == 4 || faces == 5) {
            if (differenceOfPosition.x == nearCubesSSAO[3 * vertexIndex + 0].x
                    && differenceOfPosition.y == nearCubesSSAO[3
                            * vertexIndex + 0].y
                    && differenceOfPosition.z == nearCubesSSAO[3 * vertexIndex + 0].z) {
                if (nearCubesSSAO[3 * vertexIndex + 0].x != 0) {
                    ssaoFactor.y += 1;
                }
                if (nearCubesSSAO[3 * vertexIndex + 0].y != 0) {
                    ssaoFactor.x += 1;
                }
            }
            if (differenceOfPosition.x == nearCubesSSAO[3 * vertexIndex + 1].x
                    && differenceOfPosition.y == nearCubesSSAO[3
                            * vertexIndex + 1].y
                    && differenceOfPosition.z == nearCubesSSAO[3 * vertexIndex + 1].z) {
                if (nearCubesSSAO[3 * vertexIndex + 1].x != 0) {
                    ssaoFactor.y += 1;
                }
                if (nearCubesSSAO[3 * vertexIndex + 1].y != 0) {
                    ssaoFactor.x += 1;
                }
            }

        }

        return ssaoFactor;
    }
}
