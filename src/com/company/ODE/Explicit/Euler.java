package com.company.ODE.Explicit;

import com.company.ODE.ODESolver;

public class Euler extends ODESolver {
    @Override
    protected double[][] coefficients() {
        return new double[][]{
                {0, 0},
                {0, 1}
        };
    }
}
