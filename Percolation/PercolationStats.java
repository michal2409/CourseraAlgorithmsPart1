import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private final double[] prob;
    private final static double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double stddev;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new java.lang.IllegalArgumentException();

        prob = new double[trials];
        double nSquare = n*n;

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                perc.open(row, col);
            }
            prob[i] = perc.numberOfOpenSites() / nSquare;
        }
        mean = StdStats.mean(prob);
        stddev = StdStats.stddev(prob);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return mean - CONFIDENCE_95 * stddev / Math.sqrt(prob.length);
    }

    public double confidenceHi() {
        return mean + CONFIDENCE_95 * stddev / Math.sqrt(prob.length);
    }

    public static void main(String[] args) {
        PercolationStats p = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + p.mean());
        System.out.println("stddev                  = " + p.stddev());
        System.out.println("95% confidence interval = [" + p.confidenceLo() + ", " + p.confidenceHi() + "]");
    }
}
