package com.company.ODE.Embedded;

import com.company.ODE.ODESolverAdaptive;

public class RKF45 extends ODESolverAdaptive {
    @Override
    protected double[][] coefficients() {
        return new double[][]{
                {0, 0, 0, 0, 0, 0, 0}, // k1
                {0.25, 0.25, 0, 0, 0, 0, 0}, // k2
                {3.0 / 8, 3.0 / 32, 9.0 / 32, 0, 0, 0, 0}, // k3
                {12.0 / 13, 1932.0 / 2197, -7200.0 / 2197, 7296.0 / 2197, 0, 0, 0}, // k4
                {1, 439.0 / 216, -8, 3680.0 / 513, -845.0 / 4104, 0, 0}, // k5
                {0.5, -8.0 / 27, 2, -3544.0 / 2565, 1859.0 / 4104, -11.0 / 40, 0}, // k6
                {0, 16.0 / 135, 0, 6656.0 / 12825, 28561.0 / 56430, -9.0 / 50, 2.0 / 55}, // 5-th order
                {0, 25.0 / 216, 0, 1408.0 / 2565, 2197.0 / 4104, -0.2, 0} // 4-th order

        };
    }
}
