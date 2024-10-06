package com.bkushigian.quantified_coverage;

import java.util.HashSet;
import java.util.Set;

import org.checkerframework.dataflow.analysis.AbstractValue;

/**
 * We represent an execution accumulations as a set of bitfields, encoded as
 * Longs. Given an n-ary partition of the execution space of an expression,
 * where n <= 64), we || the bitfields together to compute the LUB.
 * 
 * For instance, for a boolean expression {@code e}, n would be 2, representing
 * "we've seen {@code e} evaluate to {@code true}" and "we've seen {@code e}
 * evaluate to {@code false}". Thus, initializing to {@code 0} means we have
 * neither seen {@code e} evaluate to {@code true} or {@code false}.
 */
public class ExecutionAccumulation implements AbstractValue<ExecutionAccumulation> {
    protected final Set<Long> executionAccumulations = new HashSet<>();

    public ExecutionAccumulation() {
        executionAccumulations.add(0l);
    }

    public ExecutionAccumulation(Set<Long> accumulations) {
        this.executionAccumulations.addAll(accumulations);
    }

    @Override
    public ExecutionAccumulation leastUpperBound(ExecutionAccumulation other) {
        Set<Long> lub = new HashSet<>();
        for (Long a1 : this.executionAccumulations) {
            for (Long a2 : other.executionAccumulations) {
                lub.add(a1 | a2);
            }
        }
        return new ExecutionAccumulation(lub);
    }

}
