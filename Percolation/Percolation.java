import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF backwashUf;
    private boolean[] open;
    private final int n;
    private final int virtualTopIdx;
    private final int virtualBottomIdx;
    private int openSites;

    public Percolation(int n) {
        if (n <= 0)
            throw new java.lang.IllegalArgumentException();
        this.n = n;
        openSites = 0;
        int nSquare = n*n;
        uf = new WeightedQuickUnionUF(nSquare + 2);
        backwashUf = new WeightedQuickUnionUF(nSquare + 1);
        open = new boolean[nSquare];
        virtualTopIdx = nSquare;
        virtualBottomIdx = nSquare + 1;
    }

    private int convertToInt(int row, int col) {
        return n * (row - 1) + col - 1;
    }

    private void checkParams(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) // row, col are from 1..n
            throw new java.lang.IllegalArgumentException();
    }

    public void open(int row, int col) {
        checkParams(row, col);
        if (isOpen(row, col))
            return;

        int p = convertToInt(row, col);
        open[p] = true;
        openSites++;

        if (col != n && isOpen(row, col + 1)) {
            uf.union(p, p + 1);
            backwashUf.union(p, p + 1);
        }
        if (col != 1 && isOpen(row, col - 1)) {
            uf.union(p, p - 1);
            backwashUf.union(p, p - 1);
        }
        if (row != n && isOpen(row+1, col)) {
            uf.union(p, p + n);
            backwashUf.union(p, p + n);
        }
        if (row != 1 && isOpen(row-1, col)) {
            uf.union(p, p - n);
            backwashUf.union(p, p - n);
        }

        if (row == n)
            uf.union(p, virtualBottomIdx);
        if (row == 1) {
            uf.union(p, virtualTopIdx);
            backwashUf.union(p, virtualTopIdx);
        }
    }

    public boolean isOpen(int row, int col) {
        checkParams(row, col);
        return open[convertToInt(row, col)];
    }

    public boolean isFull(int row, int col) {
        checkParams(row, col);
        return backwashUf.connected(convertToInt(row, col), virtualTopIdx);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return uf.connected(virtualTopIdx, virtualBottomIdx);
    }
}
