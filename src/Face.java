import java.awt.Color;
import java.io.Serializable;

public class Face implements Serializable{
    private static final long serialVersionUID = 1L;

    
    private Vector corner1;
    private Vector corner2;
    private Vector corner3;
    private Vector corner4;
    private Color color;
    private final Vector direction;

    
    public Face(Vector v1, Vector v2, Vector v3, Vector v4){
        corner1 = v1;
        corner2 = v2;
        corner3 = v3;
        corner4 = v4;
        direction = null;

    }
    public Face(Vector v1, Vector v2, Vector v3, Vector v4, Color c, Vector d){
        corner1 = v1;
        corner2 = v2;
        corner3 = v3;
        corner4 = v4;
        color = c;
        direction = d;

    }
    
    // call transform() on each corner vector
    public void transform(Camera cam){
        corner1.transform(cam);
        corner2.transform(cam);
        corner3.transform(cam);
        corner4.transform(cam);    
    }
    
    public double getAverageZ() {
        return (corner1.getZ() + corner2.getZ() + corner3.getZ() + corner4.getZ()) / 4.0;
    }
    
    public double getMaxZ(){
    
        return Math.max(corner1.getZ(), Math.max(corner2.getZ(), Math.max(corner3.getZ(), corner4.getZ())));
    }

    public Vector[] getCorners() {
        return new Vector[]{corner1, corner2, corner3, corner4};
    }
    
    public void setCorners(Vector[] v){
        corner1 = v[0];
        corner2 = v[1];
        corner3 = v[2];
        corner4 = v[3];
    
    }
    public Vector getDirection() {
        return direction;
    }
        
    public boolean isFacingCamera() {
        Vector a = new Vector(corner2);
        Vector b = new Vector(corner4);
        a.subtract(corner1); // edge vector
        b.subtract(corner1); // edge vector
    
        Vector normal = a.cross(b); 
 
        return normal.dot(corner1) < 0;
    }
    
    public Color getColor(){
        return color;
    }
    
    public Vector getCenter() {
        double centerX = (corner1.getX() + corner2.getX() + corner3.getX() + corner4.getX()) / 4.0;
        double centerY = (corner1.getY() + corner2.getY() + corner3.getY() + corner4.getY()) / 4.0;
        double centerZ = (corner1.getZ() + corner2.getZ() + corner3.getZ() + corner4.getZ()) / 4.0;
    
        return new Vector(centerX, centerY, centerZ);
    }
    
    public Vector getNormal() {
        Vector edge1 = corner2.subV(corner1);
        Vector edge2 = corner3.subV(corner1);
        return edge1.cross(edge2).normalize(); // normalized for direction only
    }
    
    public boolean containsPoint(Vector point) {
        // Break the quad into two triangles
        return pointInTriangle(point, corner1, corner2, corner3) || pointInTriangle(point, corner1, corner3, corner4);
    }
    // Checks if point P lies in triangle ABC using barycentric technique
    public static boolean pointInTriangle(Vector P, Vector A, Vector B, Vector C) {
        Vector v0 = C.subV(A);
        Vector v1 = B.subV(A);
        Vector v2 = P.subV(A);
    
        double dot00 = v0.dot(v0);
        double dot01 = v0.dot(v1);
        double dot02 = v0.dot(v2);
        double dot11 = v1.dot(v1);
        double dot12 = v1.dot(v2);
    
        double denom = dot00 * dot11 - dot01 * dot01;
        if (denom == 0) return false;
    
        double u = (dot11 * dot02 - dot01 * dot12) / denom;
        double v = (dot00 * dot12 - dot01 * dot02) / denom;
    
        return (u >= 0) && (v >= 0) && (u + v <= 1);
    }
        
    public double intersectsCameraForwardDistance() {
        Vector forward = new Vector(0, 0, 1); // in camera space
        Vector p0 = corner1;
        Vector p1 = corner2;
        Vector p2 = corner3;
    
        Vector edge1 = new Vector(p1).subV(p0);
        Vector edge2 = new Vector(p2).subV(p0);
        Vector normal = edge1.cross(edge2).normalize();
    
        double denom = normal.dot(forward);
        if (Math.abs(denom) < 1e-6) return -1;
    
        double t = -normal.dot(p0) / denom;
        if (t < 0) return -1; // intersection behind the camera
    
        Vector intersection = forward.multiply(t);
    
        // test if point is in a quad
        Vector[] corners = getCorners();
        for (int i = 0; i < 4; i++) {
            Vector a = corners[i];
            Vector b = corners[(i + 1) % 4];
            Vector edge = new Vector(b).subV(a);
            Vector toPoint = new Vector(intersection).subV(a);
            Vector cross = edge.cross(toPoint);
            if (normal.dot(cross) < 0) return -1;
        }
    
        return t;
    }  
}