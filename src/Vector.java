import java.awt.Point;
import java.io.Serializable;


public class Vector implements Serializable{
    private static final long serialVersionUID = 1L;

    private double x;
    private double y;
    private double z;
    
    public Vector(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector (Vector v){
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
    }
    
    public void add(Vector add){
        x+=add.getX();
        y+=add.getY();
        z+=add.getZ();
    }
    
    public Vector addV(Vector a){
        return new Vector(x+a.getX(), y+a.getY(), z+a.getZ());
    
    }
    
    public void subtract(Vector sub){
        x-=sub.getX();
        y-=sub.getY();
        z-=sub.getZ();
    }
    
    public Vector subV(Vector a){
        return new Vector(x-a.getX(), y-a.getY(),z-a.getZ());
    }
    
    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    public double dot(Vector d){
        return x*d.getX()+y*d.getY()+z*d.getZ();    
    }
    
    public Vector cross(Vector cr){
        return new Vector(y*cr.getZ()-z*cr.getY(), z * cr.getX() - x * cr.getZ(), x*cr.getY()-y*cr.getX());
    }
    
    public Vector normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length == 0){
            return new Vector(0, 0, 0);
        }
        return new Vector(x / length, y / length, z / length);
    }
    
    public void rotateY(double theta){ // yaw
        double tempX=x*Math.cos(theta)-z*Math.sin(theta); 
        double tempZ=z*Math.cos(theta)+x*Math.sin(theta); 
        x=tempX;
        z=tempZ;
    }   
    
    public void rotateX(double theta){ // pitch
        double tempY=y*Math.cos(theta)+z*Math.sin(theta);
        double tempZ=-y*Math.sin(theta)+z*Math.cos(theta); 
        y=tempY;
        z=tempZ;
    }
    
    // converts from world space to camera space
    public void transform(Camera cam) { 
        subtract(cam.getPosition());
        rotateY(cam.getYaw());
        rotateX(cam.getPitch());
    }
    
    public Vector multiply(double d){
        return new Vector (x * d, y * d, z * d);
    }
    
    public String toString(){
        return new String("X: " + x + ", Y: " + y + ", Z: " + z);
    }
    
    
    public Point project(int screenWidth, int screenHeight, double fovDegrees, double aspectRatio, double near, double far) {
    
        if (z <= 0) {
            return null; // skip if point is behind the camera
        }
        // convert FOV from degrees to radians and compute scale
        double fovRad = Math.toRadians(fovDegrees);
        double scale = 1.0 / Math.tan(fovRad / 2);
    
        // prevent divide-by-zero or too close to camera
        double depth = z;
        if (depth == 0) depth = 0.0001;
    
        // perspective projection
        double projX = (x * scale / aspectRatio) / depth;
        double projY = (y * scale) / depth;
    
        // convert to screen space (from normalized -1 to 1 range)
        int screenX = (int) ((projX + 1) * 0.5 * screenWidth);
        int screenY = (int) ((1 - projY) * 0.5 * screenHeight); 
    
        return new Point(screenX, screenY);
    }
        
    
    public void setX(double d){
        x = d;
    }
    
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    
    public void setY(double d){
        y=d;
    }
    
    public void setZ(double d){
        z = d;
    }





}