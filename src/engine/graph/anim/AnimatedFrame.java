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
import java.util.Arrays;
import org.joml.Matrix4f;

public class AnimatedFrame {

    public static final int MAX_JOINTS = 150;
        
    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();
            
    private final Matrix4f[] localJointMatrices;

    private final Matrix4f[] jointMatrices;

    public AnimatedFrame() {
        localJointMatrices = new Matrix4f[MAX_JOINTS];
        Arrays.fill(localJointMatrices, IDENTITY_MATRIX);

        jointMatrices = new Matrix4f[MAX_JOINTS];
        Arrays.fill(jointMatrices, IDENTITY_MATRIX);
    }
    
    public Matrix4f[] getLocalJointMatrices() {
        return localJointMatrices;
    }

    public Matrix4f[] getJointMatrices() {
        return jointMatrices;
    }

    public void setMatrix(int pos, Matrix4f localJointMatrix, Matrix4f invJointMatrix) {
        localJointMatrices[pos] = localJointMatrix;
        Matrix4f mat = new Matrix4f(localJointMatrix);
        mat.mul(invJointMatrix);
        jointMatrices[pos] = mat;
    }
}
