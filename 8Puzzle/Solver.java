import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class Solver {
    private final int moves;
    private final boolean isSolvable;
    private final LinkedList<Board> solution;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode prev;
        private final int manhScore;

        public SearchNode(Board board, int moves, SearchNode prev, int manhScore) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.manhScore = manhScore;
        }

        @Override
        public int compareTo(SearchNode that) {
            return this.manhScore + this.moves - that.manhScore - that.moves;
        }
    }

    public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
        if (initial == null)
            throw new java.lang.IllegalArgumentException();
        MinPQ<SearchNode> pq = new MinPQ<>(), pqSwap = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null, initial.manhattan()));
        Board swapedInitial = initial.twin();
        pqSwap.insert(new SearchNode(swapedInitial, 0, null, swapedInitial.manhattan()));

        while (true) {
            SearchNode pqNode = pq.delMin();
            SearchNode pqSwapNode = pqSwap.delMin();

            if (pqNode.board.isGoal()) {
                moves = pqNode.moves;
                isSolvable = true;
                solution = new LinkedList<>();

                while (pqNode != null) {
                    solution.addFirst(pqNode.board);
                    pqNode = pqNode.prev;
                }
                break;
            }
            if (pqSwapNode.board.isGoal()) {
                moves = -1;
                isSolvable = false;
                solution = null;
                break;
            }
            for (Board b : pqNode.board.neighbors())
                if (pqNode.prev == null || !b.equals(pqNode.prev.board))
                    pq.insert(new SearchNode(b, pqNode.moves + 1, pqNode, b.manhattan()));
            for (Board b : pqSwapNode.board.neighbors())
                if (pqSwapNode.prev == null || !b.equals(pqSwapNode.prev.board))
                    pqSwap.insert(new SearchNode(b, pqSwapNode.moves + 1, pqSwapNode, b.manhattan()));
        }
    }

    public boolean isSolvable() {           // is the initial board solvable?
        return isSolvable;
    }

    public int moves() {                     // min number of moves to solve initial board; -1 if unsolvable
        return moves;
    }

    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
        return solution;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}