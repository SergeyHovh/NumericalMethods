package com.company;

import com.company.ODE.Explicit.RKClassic;
import com.company.ODE.ODESolver;
import com.company.ODE.ODESystem;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        ODESolver solve = new RKClassic();
        double[][] initialValues = new double[][]{
                {1, 0}
        };
        ODESystem[] odeSystems = new ODESystem[]{
                (x0, y0) -> y0[0][0]
        };
        double[][] solutions = solve.solveHighOrder(0, initialValues, 1, odeSystems);
        System.out.println(Arrays.deepToString(solutions));
    }
}
