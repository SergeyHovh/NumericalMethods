package com.company.ODE.Explicit;

import com.company.ODE.ODESolver;

public class Heun3 extends ODESolver {

    @Override
    protected double[][] coefficients() {
        return new double[][]{
                {0, 0, 0, 0},
                {1.0 / 3, 1.0 / 3, 0, 0},
                {2.0 / 3, 0, 2.0 / 3, 0},
                {0, 0.25, 0, 0.75}
        };
    }
}
