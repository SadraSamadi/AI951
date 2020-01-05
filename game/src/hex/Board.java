package hex;


import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import hex.exceptions.*;


public class Board implements Cloneable{
    private final static int size = 7;
    private int board[][];
    private int numberOfMoves = 0;

    public Board(int[][] board, int numberOfMoves) {
        this.board = board;
        this.numberOfMoves = numberOfMoves;
    }

    //ADDING SET CELL

    public void setCell(Cell cell, int color){
        board[cell.getR()][cell.getC()] = color;
    }

    public Board() {
        this(new int[size][size], 0);
    }

    public Board(Board board) {
        this.board = new int[size][size];
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) this.board[i][j] = board.board[i][j];
        this.numberOfMoves = board.numberOfMoves;
    }

    public int getSize() {
        return size;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board1 = (Board) o;

        if (numberOfMoves != board1.numberOfMoves) return false;
        return Arrays.deepEquals(board, board1.board);

    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(board);
        result = 31 * result + numberOfMoves;
        return result;
    }

    public int get(Cell cell) {
        return board[cell.getR()][cell.getC()];
    }

    public void move(Move move, int player) throws BadMoveException {
        numberOfMoves++;
        if (move.isSwap() && move.getNewCell() != null) throw new BadMoveException();
        if (move.isSwap()) {
            if (numberOfMoves != 2) throw new BadMoveException();
            int[][] temp = new int[size][size];
            for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) temp[i][j] = board[i][j];
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    if (temp[j][i] == 0)
                        board[i][j] = 0;
                    else
                        board[i][j] = 3 - temp[j][i];

        } else if (move.getNewCell() != null) {
            if (move.getNewCell().getR() < 0 || move.getNewCell().getR() >= size || move.getNewCell().getC() < 0 || move.getNewCell().getC() >= size)
                throw new BadMoveException();
            if (board[move.getNewCell().getR()][move.getNewCell().getC()] != 0) throw new BadMoveException();
            board[move.getNewCell().getR()][move.getNewCell().getC()] = player;
        }
    }

    public boolean isSwapAvailable() {
        return numberOfMoves == 1;
    }

    public ArrayList<Cell> getAdjacents(Cell cell) {
        ArrayList<Cell> result = new ArrayList<>();
        for (int r = cell.getR() - 1; r <= cell.getR() + 1; r++)
            if (r >= 0 && r < size) {
                int min = (r <= cell.getR() ? -1 : 0);
                int max = (r >= cell.getR() ? +1 : 0);
                for (int c = cell.getC() + min; c <= cell.getC() + max; c++)
                    if (c >= 0 && c < size) if (!new Cell(r, c).equals(cell))
                        result.add(new Cell(r, c));
            }
        return result;
    }

    public int win() {
        boolean mark[][] = new boolean[size][size];
        for (int i = 0; i < size; i++)
            if (!mark[0][i] && board[0][i] == 1)
                dfs(new Cell(0, i), mark, 1);
        for (int i = 0; i < size; i++) if (mark[size - 1][i]) return 1;

        mark = new boolean[size][size];
        for (int i = 0; i < size; i++)
            if (!mark[i][0] && board[i][0] == 2)
                dfs(new Cell(i, 0), mark, 2);
        for (int i = 0; i < size; i++) if (mark[i][size - 1]) return 2;

        return 0;

    }

    private void dfs(Cell cell, boolean[][] mark, int player) {
        mark[cell.getR()][cell.getC()] = true;
        ArrayList<Cell> cells = getAdjacents(cell);
        for (Cell c : cells)
            if (!mark[c.getR()][c.getC()] && get(c) == player)
                dfs(c, mark, player);
    }

    @Override
    public Board clone() {
        try {
            return (Board) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
