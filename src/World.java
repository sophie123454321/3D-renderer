import java.io.Serializable;

public class World implements Serializable{
    private static final long serialVersionUID = 1L;
    private int worldSize;
    private Cube[][][] world;

    public World(int w){
        worldSize = w;
        // populating the world with Cubes
        world = new Cube[worldSize][worldSize][worldSize];
        for (int x = 0; x < worldSize; x++){
            for (int y = 0; y < worldSize; y++){
                for (int z = 0; z < worldSize; z++){
                    world[x][y][z] = new Cube(x,y,z);
                    
                    if (y == 0){
                        world[x][y][z].setCube(true);
                        world[x][y][z].setType(Type.GRASS);
                        world[x][y][z].createCorners();
                        world[x][y][z].createFaces();
                    }
                    
                    /*int i = (int)(Math.random()*10); // test for now
                    if (i == 1){
                        world[x][y][z].setCube(true);
                        world[x][y][z].createCorners();
                        world[x][y][z].createFaces();
                    } */
                }
            }
        } 
        
       /* world[0][0][0].setCube(true);   
        world[0][0][0].createCorners();
        world[0][0][0].createFaces();*/
    }
    
    public Cube[][][] getWorld() {
        return world;
    }
    
    public void createColors(){
        
        for (int x = 0; x < worldSize; x++){
            for (int y = 0; y < worldSize; y++){
                for (int z = 0; z < worldSize; z++){
                    world[x][y][z].createColors();
                }
            }
        } 

    }
    

    public boolean isSolidNear(double x, double y, double z, double padding) {
        int minX = (int)Math.floor(x - padding);
        int maxX = (int)Math.floor(x + padding);
        int minY = (int)Math.floor(y - padding);
        int maxY = (int)Math.floor(y + padding);
        int minZ = (int)Math.floor(z - padding);
        int maxZ = (int)Math.floor(z + padding);
    
        for (int xi = minX; xi <= maxX; xi++) {
            for (int yi = minY; yi <= maxY; yi++) {
                for (int zi = minZ; zi <= maxZ; zi++) {
                    if (xi >= 0 && yi >= 0 && zi >= 0 &&
                        xi < worldSize && yi < worldSize && zi < worldSize) {
                        
                        if (world[xi][yi][zi].hasCube()) {
                            double cubeCenterX = xi + 0.5;
                            double cubeCenterY = yi + 0.5;
                            double cubeCenterZ = zi + 0.5;
    
                            double dx = Math.abs(cubeCenterX - x);
                            double dy = Math.abs(cubeCenterY - y);
                            double dz = Math.abs(cubeCenterZ - z);
    
                            if (dx < 0.5 + padding && dy < 0.5 + padding && dz < 0.5 + padding) {
                                return true; // too close to a wall
                            }
                        }
                    }
                }
            }
        }
    
        return false;
    }

    
    public void printWorld(){
    
        for (int z = worldSize-1; z >=0 ; z--){
            System.out.print("z = " + z + "     ");
            for (int y = 0; y < worldSize; y++){
                for (int x =0; x < worldSize; x++){
                    if (world[x][y][z].hasCube()){
                        System.out.print("[X]");
                    } else {
                        System.out.print("[ ]");
                    }
                }
                System.out.print("     ");
            }
            System.out.println("");
        }
        
    }
    
    public Cube getCube(int x, int y, int z){
    
        return world[x][y][z];
    }
    
    public int getSize(){
        return worldSize;
    }


}