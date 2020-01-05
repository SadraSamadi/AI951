package players;

import hex.Board;
import hex.Cell;
import hex.Move;
import hex.exceptions.BadMoveException;

import java.util.*;

public class AIPlayer extends AbstractPlayer {

    private Random random = new Random();

    @Override
    public Move getMove(Board board) {
        Node tree = generateTree(board);
        Node goal = minMax(tree, board);
        return goal.move;
    }

    private Node generateTree(Board board) {
        Node root = new Node();
        List<Node> availableNodes = availableNodes(board, null);
        Collections.shuffle(availableNodes);
        root.children = availableNodes;
        for (Node node : availableNodes) {
            List<Node> leaves = availableNodes(board, node.move.getNewCell());
            Collections.shuffle(leaves);
            node.children = leaves;
        }
        return root;
    }

    private List<Node> availableNodes(Board board, Cell except) {
        List<Node> list = new ArrayList<>();
        List<Cell> emptyCells = getCellsByColor(board, 0);
        for (Cell cell : emptyCells) {
            if (!Objects.equals(cell, except)) {
                Node node = new Node();
                node.move = new Move(cell);
                list.add(node);
            }
        }
        return list;
    }

    private Node minMax(Node root, Board board) {
        Node goal = null;
        root.value = Integer.MIN_VALUE;
        for (Node me : root.children) {
            me.value = Integer.MAX_VALUE;
            for (Node opp : me.children) {
                int fitness = fitness(board, me.move, opp.move);
                opp.value = fitness;
                if (fitness < me.value)
                    me.value = fitness;
                if (me.value < root.value)
                    break;
            }
            boolean chance = (me.value == root.value) && random.nextBoolean();
            if (me.value > root.value || chance) {
                root.value = me.value;
                goal = me;
            }
        }
        return goal;
    }

    private int fitness(Board board, Move myMove, Move oppMove) {
        Board copy = new Board(board);
        try {
            copy.move(myMove, getColor());
            copy.move(oppMove, getColor() == 1 ? 2 : 1);
        } catch (BadMoveException e) {
            e.printStackTrace();
        }
        return twoPointEvaluation(copy, myMove.getNewCell(), oppMove.getNewCell());
    }

    private List<Cell> getCellsByColor(Board board, int color) {
        List<Cell> list = new ArrayList<>();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Cell cell = new Cell(i, j);
                if (board.get(cell) == color) {
                    list.add(cell);
                }
            }
        }
        return list;
    }

    private class Node {

        int value;

        Move move;

        List<Node> children;

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private ArrayList<Cell> getSameAdjacency(int color, Cell cell, Board board) {
        boolean seen[][] = new boolean[7][7];

        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
                seen[i][j] = false;

        ArrayList<Cell> sameAdjacency = new ArrayList<>();
        Stack<Cell> stack = new Stack<Cell>();
        stack.add(cell);
        sameAdjacency.add(cell);
        seen[cell.getR()][cell.getC()] = true;

        while (!stack.isEmpty()) {
            Cell popedCell = stack.pop();

            ArrayList<Cell> adjcents = new ArrayList<>();

            if (board.get(popedCell) == color)
                adjcents = board.getAdjacents(popedCell);

            for (Cell eachAdj : adjcents) {
                if (eachAdj != null && !seen[eachAdj.getR()][eachAdj.getC()]) {
                    stack.add(eachAdj);
                    seen[eachAdj.getR()][eachAdj.getC()] = true;

                    if (board.get(eachAdj) == color)
                        sameAdjacency.add(eachAdj);

                }
            }
        }

        return sameAdjacency;
    }

    private ArrayList<Integer> evaluation(Board board, ArrayList<Cell> adjacency, Cell AI) {
        ArrayList<Integer> lefts = new ArrayList<>();
        ArrayList<Integer> rights = new ArrayList<>();
        ArrayList<Integer> tops = new ArrayList<>();
        ArrayList<Integer> bottoms = new ArrayList<>();
        ArrayList<Integer> minLength = new ArrayList<>();

        if (board.get(AI) == 2) {
            for (Cell anAdjacency : adjacency) {
                //  length from left
                lefts.add(anAdjacency.getC());
                //  length from right
                rights.add(6 - anAdjacency.getC());
            }

            Collections.sort(rights);

            Collections.sort(lefts);
            minLength.add(rights.get(0));

            minLength.add(lefts.get(0));

        } else if (board.get(AI) == 1) {
            for (Cell anAdjacency : adjacency) {
                // length from top
                tops.add(anAdjacency.getR());
                // length from bottom
                bottoms.add(6 - anAdjacency.getR());
            }

            Collections.sort(bottoms);

            Collections.sort(tops);
            minLength.add(bottoms.get(0));
            minLength.add(tops.get(0));

        }

        return minLength;
    }

    private int twoPointEvaluation(Board board, Cell AI, Cell Random) {
        ArrayList<Cell> AIAdjacency = getSameAdjacency(board.get(AI), AI, board);
        ArrayList<Integer> lengthForAI = evaluation(board, AIAdjacency, AI);

        ArrayList<Cell> RandomAdjacency = getSameAdjacency(board.get(Random), Random, board );
        ArrayList<Integer> lengthForRandom = evaluation(board, RandomAdjacency, Random);

        return 20 - (lengthForAI.get(0) + lengthForAI.get(1) + 20 - (lengthForRandom.get(0) + lengthForRandom.get(1)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
