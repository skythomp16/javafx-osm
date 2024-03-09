package edu.uca.screens.linear;

import javafx.util.StringConverter;

import java.util.List;

public class Row {

    private String constraint;
    private List<Double> decisions;
    private Operator operator;
    private double rhs;

    public enum Operator {
        GREATER(">="),
        LESS("<="),
        EMPTY("");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public Row() {
    }

    public Row(String constraint, List<Double> decisions, Operator operator, double rhs) {
        this.constraint = constraint;
        this.decisions = decisions;
        this.operator = operator;
        this.rhs = rhs;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    public List<Double> getDecisions() {
        return decisions;
    }

    public void setDecisions(List<Double> decisions) {
        this.decisions = decisions;
    }

    public void setDecisionAtLocation(int index, Double value) {
        if (index >= 0 && index < this.decisions.size()) {
            this.decisions.set(index, value);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for the list");
        }
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public double getRhs() {
        return rhs;
    }

    public void setRhs(double rhs) {
        this.rhs = rhs;
    }

    public static class OperatorStringConverter extends StringConverter<Operator> {
        @Override
        public String toString(Operator operator) {
            return operator == null ? "" : operator.getSymbol();
        }


        @Override
        public Operator fromString(String string) {
            return string == null || string.isEmpty() ? null : Operator.valueOf(string.toUpperCase());
        }
    }
}
