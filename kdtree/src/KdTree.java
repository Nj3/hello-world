
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * @author Nj3
 * This class represents a set of points in the unit square and is implemented using 2d Tree which is a 
 * generalization of Binary Search Tree(BST). The idea is to build a BST with points in the nodes, 
 * using the x- and y-coordinates of the points as keys in strictly alternating sequence. 
 * Data structure used is SET from algs4.jar provided in the course.
 * It has following methods:
 * 1. isEmpty - to check if the set is empty.
 * 2. size - number of points in the set.
 * 3. insert - add the point to the set if it's already not in the set.
 * 4. contains - checks whether the given point is already in the set.
 * 5. draw - draw all points to standard draw.
 * 6. range - all points that are inside the rectangle.
 * 7. nearest - a nearest neighbor in the set to given point p.
 */
public class KdTree {
	
	private int size = 0;
	private Node root; 
	
	public KdTree() {
	}
	
	private static class Node {
		private final Point2D point; // point
		private final RectHV rect; // the axis aligned rectangle corresponding to this node.
		private Node lb; // left/bottom subtree
		private Node rt; // right/top subtree
		
		public Node(Point2D pt, Point2D minPt, Point2D maxPt) {
			this.point = pt;
			this.rect = new RectHV(minPt.x(), minPt.y(), maxPt.x(), maxPt.y());
		}
	}
	
	/*
	 * is the tree empty?
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/*
	 * number of points in the tree
	 */
	public int size() {
		return size;
	}
	
	/*
	 * Add the point to the tree if it's already not in the set.
	 * At every level alternate between x and y coordinates while inserting.
	 * 
	 * Construct a rectangle by finding the minimum(minPt) and maximum(maxPt) Points while creating a node.
	 * minPt and maxPt is calculated based on whether it's present in left 
	 * or right subtree.
	 * While traversing through the tree, minPt and maxPt is calculated recursively
	 * with the help of current node's point(n.point) forming the boundary and previous minPt 
	 * and maxPt.
	 *  
	 * @param p - point to be inserted in the set.
	 * @throws IllegalArgumentException if p is null.
	 */
	public void insert(Point2D p) {
		if ( p == null ) throw new java.lang.IllegalArgumentException();
		
		Point2D minPt = new Point2D(0,0);
		Point2D maxPt = new Point2D(1,1);
		root = insertByX(root, p, minPt, maxPt);
	}
	
	private Node insertByX(Node n, Point2D p, Point2D minPt, Point2D maxPt) {
		if ( n == null ) {
			size++;
			return new Node(p, minPt, maxPt);
		}
		
		if ( p.equals(n.point) ) return n;
		
		if ( p.x() < n.point.x() ) {
			// left subtree
			double newMaxPtX = n.point.x();
			double newMaxPtY = maxPt.y();
			Point2D newMaxPt = new Point2D(newMaxPtX, newMaxPtY);
			n.lb = insertByY(n.lb, p, minPt, newMaxPt);
		}
		else if ( p.x() >= n.point.x() ) {
			// right subtree
			double newMinPtX = n.point.x();
			double newMinPtY = minPt.y();
			Point2D newMinPt = new Point2D(newMinPtX, newMinPtY);
			n.rt = insertByY(n.rt, p, newMinPt, maxPt);
		}

		return n;
	}
	
	private Node insertByY(Node n, Point2D p, Point2D minPt, Point2D maxPt) {
		if ( n == null ) {
			size++;
			return new Node(p, minPt, maxPt);
		}
		
		if ( p.equals(n.point) ) return n;
		
		if ( p.y() < n.point.y() ) {
			// left subtree
			double newMaxPtY = n.point.y();
			double newMaxPtX = maxPt.x();
			Point2D newMaxPt = new Point2D(newMaxPtX, newMaxPtY);
			n.lb = insertByX(n.lb, p, minPt, newMaxPt);
		}
		else if ( p.y() >= n.point.y() ) {
			// right subtree
			double newMinPtY = n.point.y();
			double newMinPtX = minPt.x();
			Point2D newMinPt = new Point2D(newMinPtX, newMinPtY);
			n.rt = insertByX(n.rt, p, newMinPt, maxPt);
		}
		
		return n;
	}
	
	/*
	 * Does the tree contain point p
	 * 
	 * @param p - point to check whether it's already in the set.
	 * @throws IllegalArgumentException if p is null.
	 */
	public boolean contains(Point2D p) {
		if ( p == null ) throw new java.lang.IllegalArgumentException();
		
		return get(p);
	}
	
	private boolean get(Point2D p) {
		return searchByX(root, p);
	}
	
	private boolean searchByX(Node n, Point2D p) {
		if ( n == null || p == null ) return false;
		
		if ( p.equals(n.point) ) return true;
		
		if ( p.x() < n.point.x() ) return searchByY(n.lb, p);
		else if ( p.x() >= n.point.x() ) return searchByY(n.rt, p);
		
		return false;
	}
	
	private boolean searchByY(Node n, Point2D p) {
		if ( n == null || p == null) return false;
		
		if ( p.equals(n.point) ) return true;
		
		if ( p.y() < n.point.y() ) return searchByX(n.lb, p);
		else if ( p.y() >= n.point.y() ) return searchByX(n.rt, p);

		return false;
	}
	
	/*
	 * Draw all points and lines to standard draw.
	 */
	public void draw() {	
		draw(root, true); // since root starts at vertical splitting.
	}
	
	private void draw(Node n, boolean isVertical) {
		if ( n == null ) return;
		
		// draw all points with black color
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.point(n.point.x(), n.point.y());
		
		// Vertical line ( red color ) , horizonal line ( blue color)
		if ( isVertical ) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius();
			StdDraw.line(n.point.x(), n.rect.ymin(), n.point.x(), n.rect.ymax());
		}else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.setPenRadius();
			StdDraw.line(n.rect.xmin(), n.point.y(), n.rect.xmax(), n.point.y());
		}
		
		// Recursively call each depth.
		draw(n.lb, !isVertical);
		draw(n.rt, !isVertical);
	}
	
	/*
	 * all points that are inside the rectangle (or on the boundary)
	 * 
	 *  @param rect - rectangle in 2D plane which may contains some points
	 *  @throws IllegalArgumentException if rect is null
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if ( rect == null ) throw new java.lang.IllegalArgumentException();
		
		ArrayList<Point2D> ptsInsideRect = new ArrayList<Point2D>();
	
		return range(root, rect, ptsInsideRect);
	}
	
	/* 
	 * Helper function to do range search.
	 * If query rectangle doesn't intersect the rectangle corresponding to the current point.
	 * skip all it's subtrees.
	 * 
	 * If it intersect, check whether it contains the point. If yes, add it to the list.
	 */
	private ArrayList<Point2D> range(Node n, RectHV queryRect, ArrayList<Point2D> ptsInRect) {
		if ( n == null ) return ptsInRect;
		
		if ( n.rect.intersects(queryRect) ) {
			if ( queryRect.contains(n.point) ) {
				if ( !ptsInRect.contains(n.point) ) ptsInRect.add(n.point);
			}
		}else return ptsInRect;
		
		ptsInRect = range(n.lb, queryRect, ptsInRect);
		ptsInRect = range(n.rt, queryRect, ptsInRect);
		
		return ptsInRect;
	}
	
	/*
	 * a nearest neighbor in the set to point p; null if the set is empty 
	 * 
	 * @param p - reference point from which we will find the nearest point.
	 * @throws IllegalArgumentException if p is null.
	 */
	public Point2D nearest(Point2D p) {
		if ( p == null ) throw new java.lang.IllegalArgumentException("There must be a query point");
		
		if ( isEmpty() ) return null;
		
		// Initialize nearestNeighbor -> root point and closestDist to distance between root and query pt.
		Point2D nearestNeighbor = root.point;
		double closestDist = nearestNeighbor.distanceSquaredTo(p);
		
		nearestNeighbor = nearest(root, p, nearestNeighbor, closestDist);

		return nearestNeighbor;
	}
	
	/*
	 * Only traverse the subtree which is closer to the query point.
	 * After first subtree is calculated, calculate the closestdist again and 
	 * if new closestDist is greater than the second subtree distance to the queryPT.
	 * then evaluate the second subtree else don't call it.\
	 * 
	 * @param n - node which is going to be explored.
	 * @param queryPt - point for which we are going to find the closest point to it.
	 * @param nearestNeighbor - closest point to the query point found
	 */
	private Point2D nearest(Node n, Point2D queryPt,  Point2D nearestNeighbor, double closestDist) {
		// Base case
		if ( n == null ) return nearestNeighbor;
		
		// check whether node point is closer
		double currentDistance = n.point.distanceSquaredTo(queryPt);
		if ( currentDistance < closestDist ) {
			closestDist = currentDistance;
			nearestNeighbor = n.point;
		}

		// Identify which subtree to traverse first.
		Node first = n.lb;
		Node second = n.rt;
		
		if ( n.lb != null && n.rt != null && n.lb.rect.distanceSquaredTo(queryPt) < n.rt.rect.distanceSquaredTo(queryPt) ) {
			first = n.lb;
			second = n.rt;
		}else {
			first = n.rt;
			second = n.lb;
		}
		// Traverse through the first subtree and identify which is the nearest neighbor and then check second subtree.
		// for traversing the second subtree, follow the pruning rule:
		// If closestDist found so far is > distance between rect and queryPt, then explore it.
		// It is achieved by [Node.]rect.distanceSquaredTo(queryPt) < closestDist
		if ( first != null && first.rect.distanceSquaredTo(queryPt) < closestDist ) {
			nearestNeighbor = nearest(first, queryPt, nearestNeighbor, closestDist);
			closestDist = nearestNeighbor.distanceSquaredTo(queryPt);
		}
		if ( second != null && second.rect.distanceSquaredTo(queryPt) < closestDist ) {
			nearestNeighbor = nearest(second, queryPt, nearestNeighbor, closestDist);
		}
		
		return nearestNeighbor;
	}
	
}
