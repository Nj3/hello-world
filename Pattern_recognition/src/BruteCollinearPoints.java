import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
	/* constructor will sort the point in ascending order
	 * and it will list the line segments which has 4 points 
	 * from left to right.
	 * @param points - list of points in (x,y) form.
	 */
	private int num_of_lines = 0;
	private ArrayList<LineSegment> ls = new ArrayList<LineSegment>();
	
	// have to change logic
	
	public BruteCollinearPoints(Point[] points) {
		// finds all line segments containing 4 points
		// Arrays.sort(points);
		
		/* Sorted output to check
		int N = points.length;
		for ( int i=0; i<N; ++i ) {
			System.out.println(points[i]);
		} */
		
		int N = points.length;
		for (int p=0; p<N; ++p) {
			for (int q=0; q<N; ++q) {
				if ( p==q || p>q ) continue;
				double slope_p2q = points[p].slopeTo(points[q]);
				for (int r=0; r<N; ++r) {
					if ( p==r || q==r || p==q || q>r) continue;
					double slope_p2r = points[p].slopeTo(points[r]);
					if ( slope_p2q != slope_p2r ) continue;
					for (int s=0;  s<N; ++s) {
						if ( r==s || p==s || q==s || p==r || q==r || p==q || r>s ) continue;
						double slope_p2s = points[p].slopeTo(points[s]);
						if ( slope_p2q == slope_p2r && slope_p2q == slope_p2s ) {
							Point[] tmp = new Point[] {points[p], points[q], points[r], points[s]};
							Arrays.sort(tmp);
							System.out.println(Arrays.toString(tmp));
							ls.add(new LineSegment(tmp[0], tmp[3]));
							num_of_lines++;
						}
					}
				}
			}
		}
		
	}
	

	public int numberOfSegments() {
		// the number of line segments
		return num_of_lines;
	}
	
	public LineSegment[] segments() {
		// the line segments which is collinear
		ArrayList<LineSegment> temp = new ArrayList<LineSegment>();
		for( LineSegment ln : ls ) {
			if ( !temp.contains(ln) ) temp.add(ln);
		}
		LineSegment[] result = new LineSegment[num_of_lines];
		temp.toArray(result);
		return result;
	}

}