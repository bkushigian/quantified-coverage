package com.bkushigian.quantified_coverage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.type.TypeMirror;

import org.checkerframework.dataflow.cfg.ControlFlowGraph;
import org.checkerframework.dataflow.cfg.block.Block;
import org.checkerframework.dataflow.cfg.block.ConditionalBlock;
import org.checkerframework.dataflow.cfg.block.ExceptionBlock;
import org.checkerframework.dataflow.cfg.node.Node;
import org.checkerframework.dataflow.cfg.visualize.CFGVisualizeLauncher;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        String file = args[0];
        String clas = args[1];
        String method = args[2];
        ControlFlowGraph cfg = CFGVisualizeLauncher.generateMethodCFG(file, clas, method);
        List<Block> workList = new ArrayList<>();

        Set<Block> visited = new HashSet<>();
        workList.add(cfg.getEntryBlock());
        while (!workList.isEmpty()) {
            Block b = workList.remove(0);
            System.out.println("---------------------");
            System.out.println(b);
            if (b instanceof ExceptionBlock) {
                ExceptionBlock eb = (ExceptionBlock) b;
                System.out.println("  - Exception Types:");
                for (Map.Entry<TypeMirror, Set<Block>> e : eb.getExceptionalSuccessors().entrySet()) {
                    System.out.println("     + " + e.getKey() + " -> " + e.getValue());
                }
            } else if (b instanceof ConditionalBlock) {
                ConditionalBlock cb = (ConditionalBlock) b;
                Set<Block> preds = cb.getPredecessors();
                if (preds.size() != 1) {
                    throw new RuntimeException("conditional block with more than one predecessor!");
                }
                Block pred = preds.iterator().next();
                List<Node> nodes = pred.getNodes();
                if (nodes.size() < 1) {
                    throw new RuntimeException("predecessor block has no condition node");
                }
                Node conditionalNode = nodes.get(nodes.size() - 1);
                Block thenSucc = cb.getThenSuccessor();
                Block elseSucc = cb.getElseSuccessor();
                System.out.println("- Cond: " + conditionalNode);
                System.out.println("- Then: " + thenSucc);
                System.out.println("- Else: " + elseSucc);

            }
            visited.add(b);
            System.out.print("- Predecessors: ");
            for (Block sb : b.getPredecessors()) {
                System.out.print(" " + sb.toString());
            }
            System.out.println();
            System.out.print("- Successors: ");
            for (Block sb : b.getSuccessors()) {
                System.out.print(" " + sb.toString());
            }
            System.out.println();
            for (Block sb : b.getSuccessors()) {
                if (visited.contains(sb)) {
                    continue;
                }
                workList.add(sb);
            }
        }

        // System.out.println(cfg);
    }
}
