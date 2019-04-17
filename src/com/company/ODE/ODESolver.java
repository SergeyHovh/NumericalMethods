package com.company.ODE;

public abstract class ODESolver {

    private static int ITERATION_COUNT;

    private ODESolver(int STEP_SIZE) {
        ITERATION_COUNT = STEP_SIZE;
    }

    public ODESolver() {
        this(1000);
    }

    public double[][] solveFirstOrder(double x0, double[] y0, double x, ODESystem[] odeSystem) {
        double[][] initialValues = new double[odeSystem.length][1];
        for (int i = 0; i < initialValues.length; i++) {
            initialValues[i] = new double[]{y0[i]};
        }
        return solveHighOrder(x0, initialValues, x, odeSystem);
    }

    public double[][] solveSecondOrder(double x0, double[] y0, double[] yPrime0, double x, ODESystem[] odeSystem) {
        double[][] initialValues = new double[odeSystem.length][2];
        for (int i = 0, initialValuesLength = initialValues.length; i < initialValuesLength; i++) {
            initialValues[i] = new double[]{y0[i], yPrime0[i]};
        }
        return solveHighOrder(x0, initialValues, x, odeSystem);
    }

    public double[][] solveHighOrder(double x0, double[][] y0, double x, ODESystem odeSystem) {
        return solveHighOrder(x0, y0, x, new ODESystem[]{odeSystem});
    }

    /**
     * @param x0  initial position - x0
     * @param y0  initial value - f(x0)
     * @param x   desired point
     * @param ode y' = f(x, y) ode = f(x, y)
     * @return value of the function at point x
     */
    public double[][] solveFirstOrder(double x0, double y0, double x, ODESystem ode) {
        double[][] initialValues = {
                {y0}
        };
        return solveHighOrder(x0, initialValues, x, ode);
    }

    /**
     * @param x0      initial position - x0
     * @param y0      initial value - y(x0)
     * @param yPrime0 initial value - y'(x0)
     * @param x       desired point
     * @param ode     y'' = f(x, y, y') ode = f(x, y, y')
     * @return value of the function at point x
     */
    public double[][] solveSecondOrder(double x0, double y0, double yPrime0, double x, ODESystem ode) {
        double[][] initialValues = {
                {y0, yPrime0}
        };
        return solveHighOrder(x0, initialValues, x, ode);
    }

    public double[][] solveHighOrder(double x0, double[][] y0, double x, ODESystem[] odeSystem) {
        return solveHighOrder(x0, y0, x,
                highOrderSystemToFirstOrderSystem(odeSystem, y0[0].length, y0.length));
    }

    /**
     * @param x0        initial position - x0
     * @param y0        initial values of y(x0), y'(x0), y''(x0), ...
     * @param x         desired position
     * @param odeSystem system of first order ODEs
     * @return value of the all derivatives at point x - y(x), y'(x), y''(x), ...
     */
    protected double[][] solveHighOrder(double x0, double[][] y0, double x, ODESystem[][] odeSystem) {
        int numberOfEquations = y0.length;
        int order = y0[0].length;
        double[][] before = new double[numberOfEquations][order];
        double h = (x - x0) / ITERATION_COUNT;
        for (int j = 0; j < numberOfEquations; j++) {
            System.arraycopy(y0[j], 0, before[j], 0, order); // update
        }

        for (int i = 0; i < ITERATION_COUNT; i++) {
            double[][][] doubles = generateKeys(generateUnweighted(odeSystem, x0, y0, before, h, coefficients()), coefficients(), false);
            updateY(y0, doubles);
            for (int j = 0; j < numberOfEquations; j++) {
                System.arraycopy(y0[j], 0, before[j], 0, order); // update
            }
            x0 += h;
        }
        return y0;
    }

    protected abstract double[][] coefficients();

    protected double[][][] generateKeys(double[][][] K, double[][] coefficients, boolean f) {
        for (int i = 0; i < K.length; i++) {
            double[][] aK = K[i];
            for (int j = 0; j < aK.length; j++) {
                double[] doubles = aK[j];
                for (int k = 0; k < doubles.length; k++) {
                    double multiplier = coefficients[coefficients.length - 1][i + 1];
                    K[i][j][k] *= multiplier;
                }
            }
        }
        return K;
    }

    protected double[][][] generateUnweighted(
            ODESystem[][] system, double x0, double[][] y0, double[][] before, double h, double[][] coefficients) {
        int numberOfEquations = y0.length;
        int order = y0[0].length;
        double[][][] K = new double[coefficients.length - 1][numberOfEquations][order];
        for (int j = 0; j < numberOfEquations; j++) {
            System.arraycopy(y0[j], 0, before[j], 0, order); // update
        }
        convertFromMatrix(system, x0, y0, before, h, coefficients, numberOfEquations, order, K);
        return K;
    }

    private ODESystem[][] highOrderSystemToFirstOrderSystem(ODESystem[] system, int order, int numberOfEquations) {
        ODESystem[][] result = new ODESystem[numberOfEquations][order];
        for (int i = 0; i < numberOfEquations; i++) {
            for (int j = 0; j < order; j++) {
                if (j == order - 1) result[i][j] = system[i];
                else {
                    final int finalI = i;
                    final int finalJ = j;
                    result[i][j] = (x0, y0) -> y0[finalI][finalJ + 1];
                }
            }
        }
        return result;
    }

    void updateY(double[][] y0, double[][][] keys) {
        for (double[][] aDouble : keys) {
            for (int j = 0; j < aDouble.length; j++) {
                double[] doubles1 = aDouble[j];
                for (int k = 0; k < doubles1.length; k++) {
                    y0[j][k] += aDouble[j][k];
                }
            }
        }
    }

    void convertFromMatrix(ODESystem[][] system, double x0, double[][] y0, double[][] before, double h, double[][] coefficients, int numberOfEquations, int order, double[][][] K) {
        for (int i = 0; i < K.length; i++) {
            for (int j = 0; j < numberOfEquations; j++) {
                for (int k = 0; k < order; k++) {
                    for (int m = 0; m < K.length; m++) {
                        y0[j][k] += coefficients[i][m + 1] * K[m][j][k];
                    }
                }
            }
            for (int j = 0; j < numberOfEquations; j++) {
                for (int k = 0; k < order; k++) {
                    K[i][j][k] = h * system[j][k].derivative(x0 + coefficients[i][0] * h, y0);
                }
            }
            for (int j = 0; j < numberOfEquations; j++) {
                System.arraycopy(before[j], 0, y0[j], 0, order); // reset
            }
        }
    }
}