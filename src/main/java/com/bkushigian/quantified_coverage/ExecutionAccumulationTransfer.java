package com.bkushigian.quantified_coverage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.checkerframework.dataflow.analysis.ForwardTransferFunction;
import org.checkerframework.dataflow.analysis.RegularTransferResult;
import org.checkerframework.dataflow.analysis.TransferInput;
import org.checkerframework.dataflow.analysis.TransferResult;
import org.checkerframework.dataflow.cfg.UnderlyingAST;
import org.checkerframework.dataflow.cfg.node.AbstractNodeVisitor;
import org.checkerframework.dataflow.cfg.node.LocalVariableNode;
import org.checkerframework.dataflow.cfg.node.Node;

/** A live variable transfer function. */
public class ExecutionAccumulationTransfer
        extends
        AbstractNodeVisitor<TransferResult<ExecutionAccumulation, ExecutionAccumulationStore>, TransferInput<ExecutionAccumulation, ExecutionAccumulationStore>>
        implements ForwardTransferFunction<ExecutionAccumulation, ExecutionAccumulationStore> {

    List<Node> nodesToTrack = new ArrayList<>();

    public ExecutionAccumulationTransfer(List<Node> nodesToTrack) {
        this.nodesToTrack.addAll(nodesToTrack);

    }

    @Override
    public ExecutionAccumulationStore initialStore(UnderlyingAST underlyingAST, List<LocalVariableNode> parameters) {
        Map<Node, ExecutionAccumulation> initialStoreContents = new HashMap<>();
        for (Node n : nodesToTrack) {
            initialStoreContents.put(n, new ExecutionAccumulation());
        }
        return new ExecutionAccumulationStore(initialStoreContents);
    }

    @Override
    public TransferResult<ExecutionAccumulation, ExecutionAccumulationStore> visitNode(Node n,
            TransferInput<ExecutionAccumulation, ExecutionAccumulationStore> p) {
        // TODO: double check this
        return new RegularTransferResult<>(null, p.getRegularStore());
    }

}
