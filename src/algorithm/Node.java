package algorithm;

import reversi.Move;
import reversi.Player;
import reversi.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Node {

    private State state;
    private Node parent;
    List<Node> children;
    int visitNum;
    int winScore;
    private int evaScore;

    private static int WIN_SCORE = 1;
    private static int interval = 15000;

    Node(State state) {
        this.state = state;
        evaScore = state.evaluate();
    }

    Move nextMove () {
        long beginTime = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() < beginTime + interval) {
//        for (int i = 0; i < 1000; ++i) {
            count++;
            // 1. select
            Node curNode = select();
            // 2. expand
            curNode.expand();
            // 3. simulation
            if ( !curNode.state.gameOver() ) {
                curNode = curNode.getRandomChild();
            }
            Player winner = curNode.simulate();
            // 4. back propagation
            backPropagation(curNode, winner);
        }
        int maxVisitNum = 0;
        int index = -1;
        for (int i = 0; i < children.size(); ++i) {
            if (children.get(i).visitNum > maxVisitNum) {
                maxVisitNum = children.get(i).visitNum;
                index = i;
            }
        }
        System.out.println("count = " + count);
        return this.state.possibleMoves().get(index);
    }

    private Node select () {
        Node curNode = this;
        while (curNode.children != null && curNode.children.size() > 0) {
            curNode = MCTS.findBestNodeWithUCT(curNode);
        }
        return curNode;
    }

    private void expand () {
        List<Move> moves = this.state.possibleMoves();
        this.children = new ArrayList<>();
        for (Move move : moves) {
            State curState = this.state.clone();
            try {
                curState.execMove(move);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Node childNode = new Node(curState);
            childNode.parent = this;
            this.children.add(childNode);
        }
    }

    private Node getRandomChild () {
        int count = (int) ( Math.random() * children.size() );
        return children.get(count);
    }

    private Player simulate () {
        Node tempNode = new Node(this.state.clone());
        while (!tempNode.state.gameOver()) {
            tempNode.expand();
            if (Math.random() < 0.5) {
                int maxScore = Collections.max(tempNode.children, Comparator.comparing(child -> child.evaScore)).evaScore;
                tempNode.children = tempNode.children.stream()
                        .filter(child -> child.evaScore == maxScore)
                        .collect(Collectors.toList());
            }
            tempNode = tempNode.getRandomChild();
        }
        return tempNode.state.currWinner();
    }

    private void backPropagation(Node node, Player winner) {
        Node temp = node;
        while (temp != null) {
            temp.visitNum++;
            if (winner != temp.state.getCurrPlayer()) {
                temp.winScore += WIN_SCORE;
            }
            temp = temp.parent;
        }
    }

//    public static void main(String[] args) {
//        Node node = new Node(null);
//        node.children = new ArrayList<>();
//        Node node1 = new Node(null);
//        node1.evaScore = 1;
//        node1.visitNum = 1;
//        Node node2 = new Node(null);
//        node2.evaScore = 1;
//        node2.visitNum = 2;
//        node.children.add(node1);
//        node.children.add(node2);
//        for (int i = 0; i < 10; ++i) {
////            int maxScore = Collections.max(node.children, Comparator.comparing(child -> child.evaScore)).evaScore;
//            int maxScore = node.children.stream()
//                    .map(child -> child.evaScore)
//                    .max(Comparator.comparing(Integer::valueOf))
//                    .get();
//            node.children = node.children.stream()
//                    .filter(child -> child.evaScore == maxScore)
//                    .collect(Collectors.toList());
//            System.out.println(node.children.size());
//        }
//    }

}
