package com.bkushigian;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.type.TypeMirror;

import org.checkerframework.dataflow.cfg.ControlFlowGraph;
import org.checkerframework.dataflow.cfg.block.Block;
import org.checkerframework.dataflow.cfg.block.ExceptionBlock;
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
            if (b instanceof ExceptionBlock) {
                ExceptionBlock eb = (ExceptionBlock) b;
                for (Map.Entry<TypeMirror, Set<Block>> e : eb.getExceptionalSuccessors().entrySet()) {
                    System.err.println("  - Exception Types: " + e.getKey() + " -> " + e.getValue());
                }
            }
            visited.add(b);
            System.out.println("---------------------");
            System.out.println(b);
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
