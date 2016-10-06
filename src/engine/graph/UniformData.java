/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.graph;

import java.nio.FloatBuffer;

/**
 *
 * @author Max
 */
public class UniformData {
    private final int uniformLocation;
    
    private FloatBuffer floatBuffer;
    
    public UniformData(int uniformLocation) {
        this.uniformLocation = uniformLocation;
    }

    public int getUniformLocation() {
        return uniformLocation;
    }

    public FloatBuffer getFloatBuffer() {
        return floatBuffer;
    }

    public void setFloatBuffer(FloatBuffer floatBuffer) {
        this.floatBuffer = floatBuffer;
    }
}
