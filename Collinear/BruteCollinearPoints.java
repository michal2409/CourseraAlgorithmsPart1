import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;

public class BruteCollinearPoints {
    private final LinkedList<LineSegment> segmentsList;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
        if (points == null)
            throw new java.lang.IllegalArgumentException();
        int n = points.length;
        for (int i = 0; i < n; i++)
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException();

        segmentsList = new LinkedList<>();
        Point[] pointsCpy = new Point[n];
        System.arraycopy(points, 0, pointsCpy, 0, n);
        Arrays.sort(pointsCpy);

        for (int i = 1; i < n; i++)
            if (pointsCpy[i].compareTo(pointsCpy[i-1]) == 0)
                throw new java.lang.IllegalArgumentException();

        if (n < 4)
            return;

        for (int i1 = 0; i1 < n - 3; i1++)
            for (int i2 = i1 + 1; i2 < n - 2; i2++)
                for (int i3 = i2 + 1; i3 < n - 1; i3++)
                    for (int i4 = i3 + 1; i4 < n; i4++)
                        if (areCollinear(pointsCpy[i1], pointsCpy[i2], pointsCpy[i3], pointsCpy[i4]))
                            segmentsList.addFirst(new LineSegment(pointsCpy[i1], pointsCpy[i4]));
    }

    private boolean areCollinear(Point p1, Point p2, Point p3, Point p4) {
        return Double.compare(p1.slopeTo(p2), p1.slopeTo(p3)) == 0 && Double.compare(p1.slopeTo(p2), p1.slopeTo(p4)) == 0;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}