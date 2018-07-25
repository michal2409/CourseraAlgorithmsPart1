import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        private final Point2D p;
        private final int level;
        private final RectHV rectHV;
        private Node left, right;

        public Node(Point2D p, RectHV rectHV, int level) {
            this.p = p;
            this.level = level;
            this.rectHV = rectHV;
            left = null;
            right = null;
        }
    }

    public KdTree() { // construct an empty set of points
        size = 0;
    }

    public boolean isEmpty() { // is the set empty?
        return size == 0;
    }

    public int size() { // number of points in the set
        return size;
    }

    private Node addNode(Node node, Point2D p, int level, double minX, double minY, double maxX, double maxY) {
        if (node == null) {
            size++;
            return new Node(p, new RectHV(minX, minY, maxX, maxY), level);
        }
        if (p.equals(node.p))
            return node;

        int cmp = (level % 2 == 0) ? (int) Math.signum(p.x() - node.p.x()) : (int) Math.signum(p.y() - node.p.y());
        if (cmp < 0 && level % 2 == 0) // left vertical line
            node.left = addNode(node.left, p, level + 1, node.rectHV.xmin(), node.rectHV.ymin(), node.p.x(), node.rectHV.ymax());
        else if (cmp < 0 && level % 2 != 0) // down horizontal line
            node.left = addNode(node.left, p, level + 1, node.rectHV.xmin(), node.rectHV.ymin(), node.rectHV.xmax(), node.p.y());
        else if (level % 2 == 0) // right vertical
            node.right = addNode(node.right, p, level + 1, node.p.x(), node.rectHV.ymin(), node.rectHV.xmax(), node.rectHV.ymax());
        else
            node.right = addNode(node.right, p, level + 1, node.rectHV.xmin(), node.p.y(), node.rectHV.xmax(), node.rectHV.ymax());
        return node;
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new java.lang.IllegalArgumentException();
        root = addNode(root, p, 0, 0,0,1,1);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null)
            throw new java.lang.IllegalArgumentException();
        Node node = root;
        while (node != null) {
            if (p.equals(node.p))
                return true;
            int cmp = (node.level % 2 == 0) ? (int) Math.signum(p.x() - node.p.x()) : (int) Math.signum(p.y() - node.p.y());
            if (cmp < 0)
                node = node.left;
            else
                node = node.right;
        }
        return false;
    }

    public void draw() {}

    private void searchRange(Node node, RectHV rectHV, LinkedList<Point2D> points) {
        if (node == null)
            return;
        if (rectHV.contains(node.p)) {
            points.addFirst(node.p);
            searchRange(node.left, rectHV, points);
            searchRange(node.right, rectHV, points);
        }
        else {
            if (node.level % 2 == 0) { // vertical line
                if (rectHV.xmax() < node.p.x()) // rect is to the left
                    searchRange(node.left, rectHV, points);
                else if (rectHV.xmin() > node.p.x()) // rect is to the right
                    searchRange(node.right, rectHV, points);
                else { // rect intersect segment
                    searchRange(node.left, rectHV, points);
                    searchRange(node.right, rectHV, points);
                }
            }
            else { // horizontal line
                if (rectHV.ymax() < node.p.y()) // rect is below
                    searchRange(node.left, rectHV, points);
                else if (rectHV.ymin() > node.p.y()) // rect is above
                    searchRange(node.right, rectHV, points);
                else { // rect intersect segment
                    searchRange(node.left, rectHV, points);
                    searchRange(node.right, rectHV, points);
                }
            }
        }
    }

    public Iterable<Point2D> range(RectHV rectHV) { // all points that are inside the rectangle (or on the boundary)
        if (rectHV == null)
            throw new java.lang.IllegalArgumentException();
        LinkedList<Point2D> points = new LinkedList<>();
        searchRange(root, rectHV, points);
        return points;
    }

    private boolean exploreLeftSubTree(Node node, Point2D queryPoint) {
        if (node.level % 2 == 0) // vertical line
            return node.p.x() - queryPoint.x() > 0;
        return node.p.y() - queryPoint.y() > 0;
    }

    private Point2D findNearest(Node node, Point2D nearestPoint, Point2D queryPoint) {
        if (node == null)
            return nearestPoint;
        double minDist = nearestPoint.distanceSquaredTo(queryPoint);
        if (minDist < node.rectHV.distanceSquaredTo(queryPoint))
            return nearestPoint;
        if (node.p.distanceSquaredTo(queryPoint) < minDist)
            nearestPoint = node.p;

        Point2D p1, p2;
        if (exploreLeftSubTree(node, queryPoint)) {
            p1 = findNearest(node.left, nearestPoint, queryPoint);
            p2 = findNearest(node.right, p1, queryPoint);
        }
        else {
            p1 = findNearest(node.right, nearestPoint, queryPoint);
            p2 = findNearest(node.left, p1, queryPoint);
        }
        if (p1.distanceSquaredTo(queryPoint) < p2.distanceSquaredTo(queryPoint))
            return p1;
        return p2;
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new java.lang.IllegalArgumentException();
        return (root == null) ? null : findNearest(root, root.p, p);
    }
}