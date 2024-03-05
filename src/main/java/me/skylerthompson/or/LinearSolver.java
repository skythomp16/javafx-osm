package me.skylerthompson.or;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.List;
import java.util.Map;

public class LinearSolver {

    public static void main(String[] args) {
        Loader.loadNativeLibraries();
        MPSolver solver = MPSolver.createSolver("GLOP");

        double infinity = java.lang.Double.POSITIVE_INFINITY;
        // x and y are continuous non-negative variables.
        MPVariable x = solver.makeNumVar(0.0, infinity, "x");
        MPVariable y = solver.makeNumVar(0.0, infinity, "y");
        System.out.println("Number of variables = " + solver.numVariables());

        // x + 2*y <= 14.
        MPConstraint c0 = solver.makeConstraint(-infinity, 700, "c0");
        c0.setCoefficient(x, 3.0);
        c0.setCoefficient(y, 5.0);

        // 3*x - y >= 0.
        MPConstraint c1 = solver.makeConstraint(-infinity, 600, "c1");
        c1.setCoefficient(x, 2.0);
        c1.setCoefficient(y, 3.0);

        System.out.println("Number of constraints = " + solver.numConstraints());

        // Maximize
        MPObjective objective = solver.objective();
        objective.setCoefficient(x, 90);
        objective.setCoefficient(y, 85);
        objective.setMaximization();

        final MPSolver.ResultStatus resultStatus = solver.solve();

        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Solution:");
            System.out.println("Objective value = " + objective.value());
            System.out.println("x = " + x.solutionValue());
            System.out.println("y = " + y.solutionValue());
        } else {
            System.err.println("The problem does not have an optimal solution!");
        }

        System.out.println("\nAdvanced usage:");
        System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");
        System.out.println("Problem solved in " + solver.iterations() + " iterations");
    }
}