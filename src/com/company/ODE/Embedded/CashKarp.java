package com.company.ODE.Embedded;

import com.company.ODE.ODESolverAdaptive;

public class CashKarp extends ODESolverAdaptive {
    @Override
    protected double[][] coefficients() {
        return new double[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0.2, 0.2, 0, 0, 0, 0, 0},
                {0.3, 3.0 / 40, 9.0 / 40, 0, 0, 0, 0},
                {0.6, 0.3, -0.9, 1.2, 0, 0, 0},
                {1, -11.0 / 54, 2.5, -70.0 / 27, 35.0 / 27, 0, 0},
                {7.0 / 8, 1631.0 / 55296, 175.0 / 512, 575.0 / 13824, 44275.0 / 110592, 253.0 / 4096, 0},
                {0, 37.0 / 378, 0, 250.0 / 621, 125.0 / 594, 0, 512.0 / 1771},
                {0, 2825.0 / 27648, 0, 18575.0 / 48384, 13525.0 / 55296, 277.0 / 14336, 0.25}
        };
    }
}