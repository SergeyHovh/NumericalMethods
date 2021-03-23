# RungeKuttaMethods
Numerical Computing Methods for Ordinary Differential Equations

A custom implementation of Runge Kutta methods on java.

## How to use

First you need to intitialize the ODE Solver
```java
ODESolver solver = new RKClassic();
```
Here we are using the classic [4th order Runge Kutta](https://en.wikipedia.org/wiki/List_of_Runge%E2%80%93Kutta_methods#Classic_fourth-order_method)

Now we need to define the equation we are willing to solve. Let's say we want to solve  `y'' + 2y' - y = 0` with `y'(0) = 1, y(0) = 0`

### Initial values

```java
double[][] initialValues = new double[][]{
                {1, 0} // y(x0) = 1, y'(x0) = 0
        };
```

Than define the equation itself. The convention for `ODESystem` interface is following: It is a functional interface which represents the ODE. 
We need to make some modification to our equation before plugging it into code. Convert `y'' + 2y' - y = 0` into `y'' = y - 2y'`.
The `y - 2y'` is what we need to plug to the code

### ODE definition

```java
ODESystem[] odeSystems = new ODESystem[]{
                (x0, y0) -> y0[0][0] - 2 * y0[0][1]// y'' = y - 2y'
        };
```

The first dimention of y0 is showing the serial number of the function, since this implementation can solve systems of ODEs with different functions - `f(x), g(x), h(x), ...`
The second dimention of y0 is showing the order of derivation of the function - `y0[0][3]` is equivalent to `f'''(x)`, `y0[1][2]` is `g''(x)`

### Solving the ODE

```java
// returns all lower degree derivatives at point x1 - y(x1) and y'(x1)
        double[][] solutions = solver.solveHighOrder(0, initialValues, 1, odeSystems);
```

The first argument is the initial point - 0
The second argument is the values of the lower degree derivatives at that point
The third argument is the value at which we desire to have the value of the functions y(x1) and y'(x1)
The forth argument is the ODE itself

The result - `solutions` is a 2d double array that contains all lower degree derivatives at point x1.
```java
solutions[0][0] = 1.304677973964033
solutions[0][1] = 0.5033690243900145
```

# Other methods
More implementation of Runge Kutta methods can be found [here](https://github.com/SergeyHovh/RungeKuttaMethods/tree/master/src/com/company/ODE/Explicit).
The usage is very simple. At the beginning use
```java
ODESolver solver = new Heun();
```
to use the [Heun's method](https://en.wikipedia.org/wiki/List_of_Runge%E2%80%93Kutta_methods#Heun's_method).
