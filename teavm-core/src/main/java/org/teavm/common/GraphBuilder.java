/*
 *  Copyright 2013 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Alexey Andreev
 */
public class GraphBuilder {
    private GraphImpl builtGraph;
    private List<IntegerArray> addedEdges = new ArrayList<>();
    private int sz;

    public GraphBuilder() {
    }

    public GraphBuilder(int sz) {
        addedEdges.addAll(Collections.<IntegerArray>nCopies(sz, null));
        this.sz = sz;
    }

    public void clear() {
        addedEdges.clear();
        sz = 0;
    }

    public void addEdge(int from, int to) {
        if (to < 0 || from < 0) {
            throw new IllegalArgumentException();
        }
        sz = Math.max(sz, Math.max(from, to) + 1);
        builtGraph = null;
        if (addedEdges.size() == from) {
            addedEdges.add(IntegerArray.of(to));
        } else if (addedEdges.size() <= from) {
            addedEdges.addAll(Collections.<IntegerArray>nCopies(from - addedEdges.size(), null));
            addedEdges.add(IntegerArray.of(to));
        } else {
            IntegerArray set = addedEdges.get(from);
            if (set == null) {
                addedEdges.set(from, IntegerArray.of(to));
            } else {
                set.add(to);
            }
        }
    }

    public Graph build() {
        if (builtGraph == null) {
            IntegerArray[] incomingEdges = new IntegerArray[sz];
            for (int i = 0; i < sz; ++i) {
                incomingEdges[i] = new IntegerArray(1);
            }
            int[][] outgoingEdgeList = new int[sz][];
            for (int i = 0; i < addedEdges.size(); ++i) {
                IntegerArray edgeList = addedEdges.get(i);
                outgoingEdgeList[i] = edgeList != null ? edgeList.getAll() : new int[0];
                for (int j : outgoingEdgeList[i]) {
                    incomingEdges[j].add(i);
                }
            }
            for (int i = addedEdges.size(); i < sz; ++i) {
                outgoingEdgeList[i] = new int[0];
            }
            int[][] incomingEdgeList = new int[sz][];
            for (int i = 0; i < sz; ++i) {
                incomingEdgeList[i] = incomingEdges[i].getAll();
            }
            builtGraph = new GraphImpl(incomingEdgeList, outgoingEdgeList);
        }
        return builtGraph;
    }

    private static class GraphImpl implements Graph {
        private final int[][] incomingEdgeList;
        private final int[][] outgoingEdgeList;

        public GraphImpl(int[][] incomingEdgeList, int[][] outgoingEdgeList) {
            this.incomingEdgeList = incomingEdgeList;
            this.outgoingEdgeList = outgoingEdgeList;
        }

        @Override
        public int size() {
            return incomingEdgeList.length;
        }

        @Override
        public int[] incomingEdges(int node) {
            int[] result = incomingEdgeList[node];
            return Arrays.copyOf(result, result.length);
        }

        @Override
        public int copyIncomingEdges(int node, int[] target) {
            int[] result = incomingEdgeList[node];
            System.arraycopy(result, 0, target, 0, result.length);
            return result.length;
        }

        @Override
        public int[] outgoingEdges(int node) {
            int[] result = outgoingEdgeList[node];
            return Arrays.copyOf(result, result.length);
        }

        @Override
        public int copyOutgoingEdges(int node, int[] target) {
            int[] result = outgoingEdgeList[node];
            System.arraycopy(result, 0, target, 0, result.length);
            return result.length;
        }

        @Override
        public int incomingEdgesCount(int node) {
            return incomingEdgeList[node].length;
        }

        @Override
        public int outgoingEdgesCount(int node) {
            return outgoingEdgeList[node].length;
        }
    }
}
