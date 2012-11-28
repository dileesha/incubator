package org.rl337.skynet.types;

import org.rl337.skynet.types.Matrix.MatrixOperation;

public class BinaryThreshold {

    public Matrix evaluate(Matrix m) {
        return Matrix.matrixOperation(m, new MatrixOperation() {
            public double operation(int row, int col, double val) {
                return val >= 0 ? 1.0 : 0.0;
            }
        });
    }
    
    public static final BinaryThreshold Instance = new BinaryThreshold();
}