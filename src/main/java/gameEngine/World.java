package gameEngine;

import java.util.ArrayList;
import java.util.List;

import util.Vector3f;
import util.Vector3i;

public class World {
    private List<Vector3i> listOfBlocs;
    private Mesh mesh;

    private static Vector3i[] cubeNormals = new Vector3i[] {
            new Vector3i(1, 0, 0), new Vector3i(-1, 0, 0), new Vector3i(0, 1, 0), new Vector3i(0, -1, 0),
            new Vector3i(0, 0, 1), new Vector3i(0, 0, -1),
    };

    public World() {
        listOfBlocs = new ArrayList<>();
        mesh = new Mesh();
    }

    public void addBloc(Vector3i position) {
        for (int i = 0; i < listOfBlocs.size(); i++) {
            if (position.x == listOfBlocs.get(i).x && position.y == listOfBlocs.get(i).y
                    && position.z == listOfBlocs.get(i).z) {
                return;
            }
        }
        listOfBlocs.add(position);
    }

    public void addBlocAbove(int index, int face) {
        Vector3i position = listOfBlocs.get(index);
        Vector3i normal = cubeNormals[face];

        listOfBlocs.add(position.add(normal));
    }

    public void removeBloc(Vector3i position) {
        listOfBlocs.remove(position);
    }

    public void removeBloc(int index) {
        listOfBlocs.remove(index);
    }

    public void draw() {
        mesh.draw();
    }

    public Vector3f getCenter() {
        Vector3f center = new Vector3f(0, 0, 0);
        for (int i = 0; i < listOfBlocs.size(); i++) {
            center.x += listOfBlocs.get(i).x + 0.5f;
            center.y += listOfBlocs.get(i).y + 0.5f;
            center.z += listOfBlocs.get(i).z + 0.5f;
        }

        if (listOfBlocs.size() != 0) {

            center = center.multiply(1.0f / (float) listOfBlocs.size());
        }

        return center;
    }

    public void generateMesh() {
        mesh.generateMesh(listOfBlocs);
    }

}
