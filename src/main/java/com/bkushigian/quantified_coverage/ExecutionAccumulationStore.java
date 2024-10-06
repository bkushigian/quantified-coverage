package com.bkushigian.quantified_coverage;

import java.util.HashMap;
import java.util.Map;
import org.checkerframework.dataflow.analysis.Store;
import org.checkerframework.dataflow.cfg.node.Node;
import org.checkerframework.dataflow.cfg.visualize.CFGVisualizer;
import org.checkerframework.dataflow.expression.JavaExpression;

public class ExecutionAccumulationStore implements Store<ExecutionAccumulationStore> {

    private final Map<Node, ExecutionAccumulation> contents = new HashMap<>();

    public ExecutionAccumulationStore(Map<Node, ExecutionAccumulation> contents) {
        this.contents.putAll(contents);
    }

    @Override
    public ExecutionAccumulationStore copy() {
        return new ExecutionAccumulationStore(this.contents);
    }

    @Override
    public ExecutionAccumulationStore leastUpperBound(ExecutionAccumulationStore other) {
        Map<Node, ExecutionAccumulation> newContents = new HashMap<>(this.contents.size() + other.contents.size());

        for (Map.Entry<Node, ExecutionAccumulation> entry : other.contents.entrySet()) {
            Node n = entry.getKey();
            ExecutionAccumulation otherVal = entry.getValue();
            if (contents.containsKey(n)) {
                newContents.put(n, otherVal.leastUpperBound(contents.get(n)));
            } else {
                newContents.put(n, otherVal);
            }
        }

        for (Map.Entry<Node, ExecutionAccumulation> entry : this.contents.entrySet()) {
            Node n = entry.getKey();
            ExecutionAccumulation thisVal = entry.getValue();
            if (!other.contents.containsKey((n))) {
                newContents.put(n, thisVal);
            }
        }
        return new ExecutionAccumulationStore(newContents);
    }

    @Override
    public ExecutionAccumulationStore widenedUpperBound(ExecutionAccumulationStore previous) {
        return leastUpperBound(previous);
    }

    @Override
    public boolean canAlias(JavaExpression a, JavaExpression b) {
        return true;
    }

    @Override
    public String visualize(CFGVisualizer<?, ExecutionAccumulationStore, ?> viz) {
        return viz.visualizeStoreKeyVal("execution accumulation", toString());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o instanceof ExecutionAccumulationStore) {
            return this.contents.equals(((ExecutionAccumulationStore) o).contents);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return contents.hashCode();
    }

}
