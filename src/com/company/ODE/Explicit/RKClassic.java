package com.company.ODE.Explicit;

import com.company.ODE.ODESolver;

public class RKClassic extends ODESolver {

    @Override
    protected double[][] coefficients() {
        return new double[][]{
                {0, 0, 0, 0, 0},
                {0.5, 0.5, 0, 0, 0},
                {0.5, 0, 0.5, 0, 0},
                {1, 0, 0, 1, 0},
                {0, 1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0}
        };
    }
}
