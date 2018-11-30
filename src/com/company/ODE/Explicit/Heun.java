package com.company.ODE.Explicit;

import com.company.ODE.ODESolver;

public class Heun extends ODESolver {
    @Override
    protected double[][] coefficients() {
        return new double[][]{
                {0, 0, 0},
                {1, 1, 0},
                {0, 0.5, 0.5}
        };
    }
}
