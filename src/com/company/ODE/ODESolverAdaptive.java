package com.company.ODE;

import java.util.Arrays;
import java.util.Hashtable;

public abstract class ODESolverAdaptive extends ODESolver {
    private final double min = 1.0E-03;
    private final double max = 9 * min;
    private final double minErr = 1.0E-10;
    private final double maxErr = 2 * minErr;
    private double h = 0;
    private Hashtable<IVP, Hashtable<Double, Value>> odeValues = new Hashtable<>();
    private Value value = new Value();


    @Override
    protected double[][] solveHighOrder(double x0, double[][] y0, double x, ODESystem[][] system) {
        int numberOfEquations = y0.length;
        int order = y0[0].length;
        double[][] before = new double[numberOfEquations][order];
        double[][] high = new double[numberOfEquations][order];
        double[][] low = new double[numberOfEquations][order];
        double[][] current = new double[numberOfEquations][order];
        for (int j = 0; j < numberOfEquations; j++) {
            System.arraycopy(y0[j], 0, before[j], 0, order); // update
            System.arraycopy(y0[j], 0, current[j], 0, order);
        }
        IVP ivp = new IVP(system, x0, current);
        odeValues.putIfAbsent(ivp, new Hashtable<>());
        int multiplier = 3;
        while (x0 < x) {
            adjustH();
            if (odeValues.get(ivp).containsKey(x0)) {
                System.arraycopy(odeValues.get(ivp).get(x0).value, 0, y0, 0, order);
                System.arraycopy(y0, 0, before, 0, order); // update
                x0 += odeValues.get(ivp).get(x0).h;
            } else { // computation
                double[][][] keys = generateUnweighted(system, x0, y0, before, h, coefficients());
                for (int j = 0; j < numberOfEquations; j++) {
                    System.arraycopy(rungeKutta(y0, keys, true)[j], 0, high[j], 0, order);
                }
                for (int j = 0; j < numberOfEquations; j++) {
                    System.arraycopy(before[j], 0, y0[j], 0, order); // reset
                }
                for (int j = 0; j < numberOfEquations; j++) {
                    System.arraycopy(rungeKutta(y0, keys, false)[j], 0, low[j], 0, order);
                }
                double error = Math.abs(high[0][0] - low[0][0]);
                if (error > maxErr && h > min) {
                    h /= multiplier;
                } else {
                    for (int j = 0; j < numberOfEquations; j++) {
                        System.arraycopy(high[j], 0, y0[j], 0, order);
                    }
                    for (int j = 0; j < numberOfEquations; j++) {
                        System.arraycopy(y0[j], 0, before[j], 0, order);
                    }
                    value.setValue(high);
                    value.setH(h);
                    odeValues.get(ivp).put(x0, value);
                    x0 += h;
                    if (error < minErr && h <= max) h *= multiplier;
                }
            }
        }
        return y0;
    }

    @Override
    protected double[][][] generateKeys(double[][][] K, double[][] coefficients, boolean high) {
        // adjust weights
        for (int i = 0; i < K.length; i++) {
            double[][] aK = K[i];
            for (int j = 0; j < aK.length; j++) {
                double[] doubles = aK[j];
                for (int k = 0; k < doubles.length; k++) {
                    double multiplier;
                    if (high) {
                        multiplier = coefficients[coefficients.length - 2][i + 1];
                    } else {
                        multiplier = coefficients[coefficients.length - 1][i + 1];
                    }
                    K[i][j][k] *= multiplier;
                }
            }
        }
        return K;
    }

    @Override
    protected double[][][] generateUnweighted(
            ODESystem[][] system, double x0, double[][] y0, double[][] before, double h, double[][] coefficients) {
        int numberOfEquations = y0.length;
        int order = y0[0].length;
        double[][][] K = new double[coefficients.length - 2][numberOfEquations][order];
        // init Ks
        for (int j = 0; j < numberOfEquations; j++) {
            System.arraycopy(y0[j], 0, before[j], 0, order); // update
        }
        convertFromMatrix(system, x0, y0, before, h, coefficients, numberOfEquations, order, K);
        return K;
    }

    private double[][] rungeKutta(double[][] y0, double[][][] keys, boolean high) {
        double[][][] generateKeys = generateKeys(keys, coefficients(), high);
        updateY(y0, generateKeys);
        return y0;
    }

    private void adjustH() {
        if (h < min) h = min;
        else if (h > max) h = max;
    }

    private class Value {
        double[][] value;
        double h;

        void setValue(double[][] value) {
            this.value = value;
        }

        void setH(double h) {
            this.h = h;
        }
    }

    private class IVP {
        ODESystem[][] ode;
        double x0;
        double[][] y0;

        IVP(ODESystem[][] ode, double x0, double[][] y0) {
            this.ode = ode;
            this.x0 = x0;
            this.y0 = y0;
        }

        @Override
        public String toString() {
            return x0 + " " + Arrays.toString(y0);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof IVP) {
                return
                        this.x0 == ((IVP) obj).x0
                                && Arrays.equals(this.y0, ((IVP) obj).y0)
                                && Arrays.equals(this.ode, ((IVP) obj).ode);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            double Ys = 0, vals = 0;
            for (double[] v : y0) {
                for (double v1 : v) {
                    Ys += v1;
                }
            }
            for (ODESystem[] ode1 : ode) {
                for (ODESystem odeSystem : ode1) {
                    vals += odeSystem.derivative(x0, y0);
                }
            }
            return 2 * (int) vals + 3 * (int) x0 + 7 * (int) Ys;
        }
    }
}
