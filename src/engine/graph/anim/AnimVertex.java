/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.graph.anim;

/**
 *
 * @author Max
 */
import org.joml.Vector2f;
import org.joml.Vector3f;

public class AnimVertex {

    public Vector3f position;

    public Vector2f textCoords;

    public Vector3f normal;

    public float[] weights;

    public int[] jointIndices;

    public AnimVertex() {
        super();
        normal = new Vector3f(0, 0, 0);
    }
}
