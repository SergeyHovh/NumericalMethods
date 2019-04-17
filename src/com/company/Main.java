package com.company;

import com.company.ODE.Explicit.RKClassic;
import com.company.ODE.ODESolver;
import com.company.ODE.ODESystem;

import java.util.Arrays;

import static java.lang.Math.pow;

public class Main {

    public static void main(String[] args) {
        System.out.println("example 1");
        // Example 1
        /*
         * solving the y'' - y = 0 equation with y(0) = 1 and y'(0) = 0 initial conditions
         * and getting y(1) with classical RK4 algorithm
         */
        ODESolver solve = new RKClassic(); // don't use embedded methods for exact solutions

        double[][] initialValues = new double[][]{
                {1, 0} // y(x0) = 1, y'(x0) = 0
        };

        // 2 initial values means that the equation is second order
        ODESystem[] odeSystems = new ODESystem[]{
                (x0, y0) -> y0[0][0] // y'' = y
        };

        // returns all lower degree derivatives at point x - y(x) and y'(x)
        double[][] solutions = solve.solveHighOrder(0, initialValues, 1, odeSystems);
        System.out.println(Arrays.deepToString(solutions));

        System.out.println("example 2");
        // Example 2

        /*
         * solving the f'' = 4 * sqrt(g) and g'' = 16 * f^2
         *
         * f = e^(2x), g = e^(4x)
         * */

        // returns all lower degree derivatives of f and g at point x

        double[][] initials = new double[][]{
                {1, 2}, // f(x0) and f'(x0)
                {1, 4}  // g(x0) and g'(x0)
        };
        ODESystem[] ode = new ODESystem[]{
                (x0, y01) -> 4 * pow(y01[1][0], 0.5),
                (x0, y01) -> 16 * pow(y01[0][0], 2)
        };

        double[][] sol = solve.solveHighOrder(0, initials, 1, ode);
        System.out.println(Arrays.deepToString(sol));
        // f = e^2x, f' = 2*e^2x
        // g = e^4x, g' = 4*e^4x
        double f = Math.exp(2);
        double g = Math.exp(4);
        System.out.println(f + " " + (2 * f) + "   " + g + " " + (4 * g));

        // errors
        System.out.println("errors");
        double fError = f - sol[0][0];
        double fPrimeError = 2 * f - sol[0][1];
        double gError = g - sol[1][0];
        double gPrimeError = 4 * g - sol[1][1];
        System.out.println("error of f : " + fError);
        System.out.println("error of f' : " + fPrimeError);
        System.out.println("error of g : " + gError);
        System.out.println("error of f' : " + gPrimeError);

        /*
         * first index of the output of solveHighOrder(...) is the subscript of the function
         * y[0][0] is f and y[1][0] is g
         *
         * second index of the output of solveHighOrder(...) is the derivative degree of the function
         * y[0][0] is f, y[0][1] is f' and y[0][2] is f''
         * */

    }
}
