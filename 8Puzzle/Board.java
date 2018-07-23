import java.util.LinkedList;

public class Board {
    private final int[][] board;
    private final int n;

    /*
    construct a board from an n-by-n array of blocks
    (where blocks[i][j] = block in row i, column j)
    */
    public Board(int[][] blocks) {
        n = blocks.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++)
            System.arraycopy(blocks[i], 0, board[i], 0, n);
    }

    public int dimension() {                 // board dimension n
        return n;
    }

    public int hamming() {                   // number of blocks out of place
        int count = 0;
        for (int i = 0; i < n; i++) {
            int mult = i * n + 1;
            for (int j = 0; j < n; j++) {
                int boardVal = board[i][j];
                if (boardVal == 0)
                    continue;
                if (boardVal != mult + j)
                    count++;
            }
        }
        return count;
    }

    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        int dist = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                int boardVal = board[i][j];
                if (boardVal == 0)
                    continue;
                int row = (boardVal - 1) / n;
                int col = (boardVal - 1) % n;
                dist += Math.abs(row - i) + Math.abs(col - j);
            }
        return dist;
    }

    public boolean isGoal() {               // is this board the goal board?
        for (int i = 0; i < n; i++) {
            int mult = i * n + 1;
            for (int j = 0; j < n; j++) {
                int boardVal = board[i][j], value = (boardVal > 0) ? boardVal : n * n;
                if (value != mult + j)
                    return false;
            }
        }
        return true;
    }

    public Board twin() {                 // a board that is obtained by exchanging any pair of blocks
        Board b = new Board(board);
        if (board[0][0] == 0 || board[0][1] == 0)
            swap(b, 1, 0, 1, 1);
        else
            swap(b, 0, 0, 0, 1);
        return b;
    }

    public boolean equals(Object y) {      // does this board equal y?
        if (y == null)
            return false;
        if (getClass() != y.getClass())
            return false;
        int[][] b = ((Board) y).board;
        if (b.length != n || b[0].length != n)
            return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (board[i][j] != b[i][j])
                    return false;
        return true;
    }

    private void swap(Board b, int r1, int c1, int r2, int c2) {
        int p = b.board[r1][c1];
        b.board[r1][c1] = b.board[r2][c2];
        b.board[r2][c2] = p;
    }

    private void addNeighbor(LinkedList<Board> neighList, int zR, int zC, int rowMove, int colMove) {
        Board b = new Board(board);
        b.swap(b, zR, zC, zR + rowMove, zC + colMove);
        neighList.addFirst(b);
    }

    public Iterable<Board> neighbors() {    // all neighboring boards
        LinkedList<Board> neighList = new LinkedList<>();
        int zerosRow = -1, zerosCol = -1;
        boolean finished = false;
        for (int i = 0; i < n && !finished; i++) // finding zeros coordinates
            for (int j = 0; j < n; j++)
                if (board[i][j] == 0) {
                    zerosRow = i;
                    zerosCol = j;
                    finished = true;
                    break;
                }

        if (zerosRow - 1 >= 0)
            addNeighbor(neighList, zerosRow, zerosCol, -1, 0);
        if (zerosRow + 1 < n)
            addNeighbor(neighList, zerosRow, zerosCol, 1, 0);
        if (zerosCol - 1 >= 0)
            addNeighbor(neighList, zerosRow, zerosCol, 0, -1);
        if (zerosCol + 1 < n)
            addNeighbor(neighList, zerosRow, zerosCol, 0, 1);
        return neighList;
    }

    public String toString() {              // string representation of this board (in the output format specified below)
        String s = n + "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                s += " " + board[i][j];
            s += "\n";
        }
        return s;
    }
}
