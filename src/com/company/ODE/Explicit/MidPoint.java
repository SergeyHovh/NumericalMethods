package com.company.ODE.Explicit;

import com.company.ODE.ODESolver;

public class MidPoint extends ODESolver {
    @Override
    protected double[][] coefficients() {
        return new double[][]{
                {0, 0, 0},
                {0.5, 0.5, 0},
                {0, 0, 1}
        };
    }
}
