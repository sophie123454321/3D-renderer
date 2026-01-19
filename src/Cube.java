import java.awt.Color;
import java.io.Serializable;

public class Cube implements Serializable{
    private static final long serialVersionUID = 1L;

    
    // Cube's grid location within the 3D array
    private double x;
    private double y;
    private double z;
    
    // corners
    private Vector v1;
    private Vector v2;
    private Vector v3;
    private Vector v4;
    private Vector v5;
    private Vector v6;
    private Vector v7;
    private Vector v8;
    
    // faces
    private Face f1;
    private Face f2;
    private Face f3;
    private Face f4;
    private Face f5;
    private Face f6;
    
    private Color faceColor;
    
    private boolean hasCube;
    private boolean isLooking;
    
    Type type;
    
    Color dirt;
    Color grass;
    Color orange;
    Color red;
    Color blue;
    Color purple;
    Color pink;
    Color yellow;
    
    public Cube(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        createColors();
    }
    
    public void createColors(){
        dirt = new Color(100,75,50);
        grass = new Color(25,120,50);
        orange = new Color(222,134,2);
        red = new Color(176,36,36);
        blue = new Color(34,106,219);
        purple = new Color(130,0,230);
        pink = new Color(197,55,200);
        yellow = new Color(255,217,37);   
    };
    
    public void setType(Type t){
        type = t;
    }
    
    
    public void createCorners(){
    
        if (hasCube){
            v1 = new Vector(x,y,z);
            v2 = new Vector(x+1,y,z);
            v3 = new Vector(x+1,y,z+1);
            v4 = new Vector(x,y,z+1);
            v5 = new Vector(x,y+1,z);
            v6 = new Vector(x+1,y+1,z);
            v7 = new Vector(x+1,y+1,z+1);
            v8 = new Vector(x,y+1,z+1); 
        }
    
    }
    
    public void createFaces(){
        if (hasCube){ // winding order is CCW when looking from outside
            
            if (type == Type.GRASS){
                f1 = new Face(v1, v2, v3, v4, dirt, new Vector(0,-1,0)); // bottom
                f2 = new Face(v7, v6, v5, v8, grass, new Vector(0,1,0)); // top
                f3 = new Face(v3, v2, v6, v7, dirt, new Vector(1,0,0));
                f4 = new Face(v4, v3, v7, v8, dirt, new Vector(0,0,1));
                f5 = new Face(v1, v4, v8, v5, dirt, new Vector(-1,0,0));
                f6 = new Face(v1, v5, v6, v2, dirt, new Vector(-1,0,0));
            } else {
            
                if (type == Type.GRAY){
                    faceColor = Color.GRAY;
                } else if (type == Type.WHITE) {
                    faceColor = Color.WHITE;
                } else if (type == Type.BLACK) {
                    faceColor = Color.BLACK;
                } else if (type == Type.RED) {
                    faceColor = red;
                } else if (type == Type.ORANGE) {
                    faceColor = orange;
                } else if (type == Type.YELLOW) {
                    faceColor = yellow;
                } else if (type == Type.GREEN) {
                    faceColor = grass;               
                } else if (type == Type.BLUE) {
                    faceColor = blue;               
                } else if (type == Type.PURPLE) {
                    faceColor = purple;          
                } else if (type == Type.PINK) {
                    faceColor = pink;              
                } else if (type == Type.BROWN) {
                    faceColor = dirt;
                }
                
                f1 = new Face(v1, v2, v3, v4, faceColor, new Vector(0,-1,0)); // bottom
                f2 = new Face(v7, v6, v5, v8, faceColor, new Vector(0,1,0)); // top
                f3 = new Face(v3, v2, v6, v7, faceColor, new Vector(1,0,0));
                f4 = new Face(v4, v3, v7, v8, faceColor, new Vector(0,0,1));
                f5 = new Face(v1, v4, v8, v5, faceColor, new Vector(-1,0,0));
                f6 = new Face(v1, v5, v6, v2, faceColor, new Vector(-1,0,0));

            }
        
        }
    
    }
    
    public Face[] getFaces(){
        return new Face[]{f1, f2, f3, f4, f5, f6};
    }
    
    public Face[] getTransformedFaces(Camera cam) {
        // transform corners
        Vector[] originalCorners = this.getCorners(); // original 8 corners
        Vector[] transformedCorners = new Vector[8];
        for (int i = 0; i < 8; i++) {
            transformedCorners[i] = new Vector(originalCorners[i]);
            transformedCorners[i].transform(cam);
        }
        
        
    
        // build faces from the transformed corners
        
        if (type == Type.GRASS){
            return new Face[] {
                new Face(transformedCorners[0], transformedCorners[1], transformedCorners[2], transformedCorners[3], dirt, new Vector(0,-1,0)),
                new Face(transformedCorners[7], transformedCorners[6], transformedCorners[5], transformedCorners[4], grass, new Vector(0,1,0)),
                new Face(transformedCorners[2], transformedCorners[1], transformedCorners[5], transformedCorners[6], dirt, new Vector(1,0,0)),
                new Face(transformedCorners[3], transformedCorners[2], transformedCorners[6], transformedCorners[7], dirt, new Vector(0,0,1)),
                new Face(transformedCorners[0], transformedCorners[3], transformedCorners[7], transformedCorners[4], dirt, new Vector(-1,0,0)),
                new Face(transformedCorners[0], transformedCorners[4], transformedCorners[5], transformedCorners[1], dirt, new Vector(0,0,-1))
            };
        } else {
        
            if (type == Type.GRAY){
                faceColor = Color.GRAY;
            } else if (type == Type.WHITE) {
                faceColor = Color.WHITE;
            } else if (type == Type.BLACK) {
                faceColor = Color.BLACK;
            } else if (type == Type.RED) {
                faceColor = red;
            } else if (type == Type.ORANGE) {
                faceColor = orange;
            } else if (type == Type.YELLOW) {
                faceColor = yellow;
            } else if (type == Type.GREEN) {
                faceColor = grass;               
            } else if (type == Type.BLUE) {
                faceColor = blue;               
            } else if (type == Type.PURPLE) {
                faceColor = purple;          
            } else if (type == Type.PINK) {
                faceColor = pink;              
            } else if (type == Type.BROWN) {
                faceColor = dirt;
            }
            
            return new Face[] {
                new Face(transformedCorners[0], transformedCorners[1], transformedCorners[2], transformedCorners[3], faceColor, new Vector(0,-1,0)),
                new Face(transformedCorners[7], transformedCorners[6], transformedCorners[5], transformedCorners[4], faceColor, new Vector(0,1,0)),
                new Face(transformedCorners[2], transformedCorners[1], transformedCorners[5], transformedCorners[6], faceColor, new Vector(1,0,0)),
                new Face(transformedCorners[3], transformedCorners[2], transformedCorners[6], transformedCorners[7], faceColor, new Vector(0,0,1)),
                new Face(transformedCorners[0], transformedCorners[3], transformedCorners[7], transformedCorners[4], faceColor, new Vector(-1,0,0)),
                new Face(transformedCorners[0], transformedCorners[4], transformedCorners[5], transformedCorners[1], faceColor, new Vector(0,0,-1))
            };
        
        }
    }
    
    public void transformCorners(Camera cam){
        v1.transform(cam);
        v2.transform(cam);
        v3.transform(cam);
        v4.transform(cam);
        v5.transform(cam);
        v6.transform(cam);
        v7.transform(cam);
        v8.transform(cam);

    }
    
    public Vector[] getCorners(){
        return new Vector[]{v1, v2, v3, v4, v5, v6, v7, v8};
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
    
    public boolean hasCube(){
        return hasCube;
    }
    public void setCube(boolean b){
        hasCube = b;
    }
    
    
    // distance from cube center to (0,0,0)
    public double distanceToCam(Camera cam){  
        Vector cubeCenter = new Vector(x+0.5, y+0.5, z+0.5); // in world space
        cubeCenter.subtract(cam.getPosition());
        return cubeCenter.magnitude();
    }
    
    public void setLooking(boolean b){
        isLooking = b;
    }
    
    public boolean getLooking(){
        return isLooking;
    }
    
    public void clearCorners(){
        v1 = null;
        v2 = null;
        v3 = null;
        v4 = null;
        v5 = null;
        v6 = null;
        v7 = null;
        v8 = null;
    }
    
    public void clearFaces(){
        f1 = null;
        f2 = null;
        f3 = null;
        f4 = null;
        f5 = null;
        f6 = null;
    }
    
    public double rayBoxIntersection(Vector rayOrigin, Vector rayDir) {
        Vector[] bounds = getCorners();
    
        Vector min = new Vector(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Vector max = new Vector(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    
        for (Vector corner : bounds) {
            min.setX(Math.min(min.getX(), corner.getX()));
            min.setY(Math.min(min.getY(), corner.getY()));
            min.setZ(Math.min(min.getZ(), corner.getZ()));
    
            max.setX(Math.max(max.getX(), corner.getX()));
            max.setY(Math.max(max.getY(), corner.getY()));
            max.setZ(Math.max(max.getZ(), corner.getZ()));
        }
    
        double tmin = (min.getX() - rayOrigin.getX()) / rayDir.getX();
        double tmax = (max.getX() - rayOrigin.getX()) / rayDir.getX();
        if (tmin > tmax) { 
            double t = tmin; 
            tmin = tmax; 
            tmax = t; 
        }
    
        double tymin = (min.getY() - rayOrigin.getY()) / rayDir.getY();
        double tymax = (max.getY() - rayOrigin.getY()) / rayDir.getY();
        if (tymin > tymax) { 
            double t = tymin; 
            tymin = tymax; 
            tymax = t; 
        }
    
        if ((tmin > tymax) || (tymin > tmax)){
            return -1;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }
    
        double tzmin = (min.getZ() - rayOrigin.getZ()) / rayDir.getZ();
        double tzmax = (max.getZ() - rayOrigin.getZ()) / rayDir.getZ();
        if (tzmin > tzmax) { 
            double t = tzmin; 
            tzmin = tzmax; 
            tzmax = t; 
        }
    
        if ((tmin > tzmax) || (tzmin > tmax)) {
            return -1;
        }
        
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        
        if (tzmax < tmax) {
            tmax = tzmax;
        }
    
        if (tmin>=0){
            return tmin;
        } else {
            return -1;
        }
    }


   
    
       
    

}