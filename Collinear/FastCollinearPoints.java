import java.util.Arrays;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
    private final LinkedList<LineSegment> segmentsList;

    public FastCollinearPoints(Point[] points) { // finds all line segments containing 4 or more points
        if (points == null)
            throw new java.lang.IllegalArgumentException();
        int n = points.length;
        for (int i = 0; i < n; i++)
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException();

        Point[] pointsCpy = new Point[n];
        System.arraycopy(points, 0, pointsCpy, 0, n);
        segmentsList = new LinkedList<>();
        Arrays.sort(pointsCpy);

        for (int i = 1; i < n; i++)
            if (pointsCpy[i].compareTo(pointsCpy[i-1]) == 0)
                throw new java.lang.IllegalArgumentException();

        for (int i = 0; i < n - 3; i++) {
            Arrays.sort(pointsCpy);
            Point p = pointsCpy[i];
            Arrays.sort(pointsCpy, p.slopeOrder());

            for (int j = 0; j < n; j++) {
                double slopeToP = p.slopeTo(pointsCpy[j]);

                if (j+2 < n && Double.compare(slopeToP, p.slopeTo(pointsCpy[j+2])) == 0) {
                    int idx = j+3;
                    while (idx < n && Double.compare(slopeToP, p.slopeTo(pointsCpy[idx])) == 0)
                        idx++;
                    if (p.compareTo(pointsCpy[j]) < 0)
                        segmentsList.addFirst(new LineSegment(p, pointsCpy[idx-1]));
                    j = idx-1;
                }
            }
        }
    }

    public int numberOfSegments() { // the number of line segments
        return segmentsList.size();
    }

    public LineSegment[] segments() { // the line segments
        return segmentsList.toArray(new LineSegment[segmentsList.size()]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}