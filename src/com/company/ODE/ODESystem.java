package com.company.ODE;

public interface ODESystem {
    /**
     * @param x0 initial position
     * @param y0 initial values - y[0][0] = y(x0), y[0][1] = y'(x0), y[1][0] = z(x0), y[1][1] = z'(x0), ...
     */
    double derivative(double x0, double[][] y0);

    default boolean equal(ODESystem ode) {
        for (double i = 0; i < 100; i++) {
            if (!(this.derivative(i / 100, new double[][]{{1, 0}}) == ode.derivative(i / 100, new double[][]{{1, 0}}))) {
                return false;
            }
        }
        return true;
    }
}
