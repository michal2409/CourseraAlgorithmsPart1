import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> pointSet;

    public PointSET() { // construct an empty set of points
        pointSet = new TreeSet<>();
    }

    public boolean isEmpty() { // is the set empty?
        return pointSet.isEmpty();
    }

    public int size() { // number of points in the set
        return pointSet.size();
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new java.lang.IllegalArgumentException();
        pointSet.add(p);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null)
            throw new java.lang.IllegalArgumentException();
        return pointSet.contains(p);
    }

    public void draw() { // draw all points to standard draw
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        for (Point2D p : pointSet) {
            p.draw();
        }
        StdDraw.show();
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        LinkedList<Point2D> pointsInRect = new LinkedList<>();
        for (Point2D pt : pointSet)
            if (rect.contains(pt))
                pointsInRect.addFirst(pt);
        return pointsInRect;
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (pointSet.isEmpty())
            return null;
        Point2D minPoint = pointSet.first();
        double minDist = p.distanceSquaredTo(minPoint);
        for (Point2D pt : pointSet) {
            double dist = p.distanceSquaredTo(pt);
            if (dist < minDist) {
                minDist = dist;
                minPoint = pt;
            }
        }
        return minPoint;
    }
}