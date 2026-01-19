public class Camera {
    private Vector position;
    private double yaw;   // left/right
    private double pitch; // up/down
    private final double size = 0.2;
    private final Vector pointer = new Vector(0,0,1);
    private World world;

    public Camera(Vector p, World w) {
        position = p;
        yaw = 0;
        pitch = 0;
        world = w;
    }
    
    // move forward/backward along the direction the camera is facing
    public void moveForward(double amount) {
        double newX = position.getX() + Math.sin(yaw) * amount;
        double y = position.getY();
        double newZ= position.getZ() + Math.cos(yaw) * amount;

        if (!world.isSolidNear(newX, y, newZ, size)){
            position = new Vector(newX,y,newZ);
        }
    }

    // Move left/right perpendicular to the facing direction (strafe)
    public void moveSideways(double amount) {
        double newX = position.getX() + Math.cos(yaw) * amount;
        double y = position.getY();
        double newZ = position.getZ() - Math.sin(yaw) * amount;
        
        if (!world.isSolidNear(newX, y, newZ, size)){
            position = new Vector(newX,y,newZ);
        }
    }
    
    public void moveUp(double amount){
        double x = position.getX();
        double newY = position.getY() + amount;
        double z = position.getZ();
       
        if (!world.isSolidNear(x, newY, z, size)){
            position = new Vector(x, newY, z);
        }
    }
    
    public void resetPosition(){
        position.setX(0);
        position.setY(0);
        position.setZ(-5);
        yaw = 0;
        pitch = 0;
    }
    
    
    public void setYaw(double d) {
        yaw = d % (2 * Math.PI);
        if (yaw < -Math.PI) {
            yaw += 2 * Math.PI;
        }
        if (yaw > Math.PI) {
            yaw -= 2 * Math.PI;
        }
    }
    

    
    public void setPitch(double d){
        if (d > Math.PI / 2){
            d = Math.PI / 2;
        }
        if (d < -Math.PI / 2){
            d = -Math.PI / 2;
        }
        pitch = d;
    }
    
    public Vector getPosition(){
        return position;
    }
    
    public double getYaw(){
        return yaw;
    }
    
    public double getPitch(){
        return pitch;
    }
    
    public Vector getPointer(){
        return pointer;
    }
}