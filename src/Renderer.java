import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.io.*;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Renderer extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {

    Camera camera;
    World world;
    
    private final double MAX_REACH = 5;
    
    private int prevMouseX, prevMouseY;
    private boolean firstMouse = true;
    private Robot robot;
    private boolean recentering = false;

    private int centerX;
    private int centerY;
    
    private final double MOVE_SPEED = 0.15;
    private boolean forwardPressed;
    private boolean backwardPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;
    
    private boolean mouseLock;
    private Cursor defaultCursor;
    private Cursor invisibleCursor;
    
    private boolean editMode;
    private boolean addPressed;
    private boolean subtractPressed;
    private boolean holdToEdit;
    
    private Cube lookingAtCube = null;
    private Face lookingAtFace = null;
    
    int currCube = 0;
    
    
    BufferedImage editModeFalse;
    BufferedImage editModeTrue;
    BufferedImage holdToEditFalse;
    BufferedImage holdToEditTrue;
    BufferedImage saveWorld;
    BufferedImage loadWorld;
    
    BufferedImage grass;
    BufferedImage white;
    BufferedImage gray;
    BufferedImage black;
    BufferedImage red;
    BufferedImage orange;
    BufferedImage yellow;
    BufferedImage green;
    BufferedImage blue;
    BufferedImage purple;
    BufferedImage pink;
    BufferedImage brown;
    


        
    public Renderer(int width, int height, int worldSize){
        world = new World(worldSize);
        camera = new Camera(new Vector(0, 0, -5), world);
        setDoubleBuffered(true);
        //world.printWorld();
        setFocusable(true);
        requestFocusInWindow();
        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    
        centerX = width / 2;
        centerY = height / 2;
    
        // center the mouse
        robot.mouseMove(centerX, centerY);
        defaultCursor = Cursor.getDefaultCursor();
        BufferedImage blankImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankImg, new Point(0, 0), "blank cursor");
        setCursor(invisibleCursor);
        setupKeyBindings();
        mouseLock = true;
        //editMode = true;
        
        initializeImages();
        
        
        
        // update loop
        Timer timer = new Timer(16, e -> {
            if (forwardPressed) {
                camera.moveForward(MOVE_SPEED);
            }
            if (backwardPressed) {
                camera.moveForward(-MOVE_SPEED);
            }
            if (leftPressed) {
                camera.moveSideways(-MOVE_SPEED);
            }
            if (rightPressed) {
                camera.moveSideways(MOVE_SPEED);
            }
            if (upPressed) {
                camera.moveUp(MOVE_SPEED);
            }
            if (downPressed){
                camera.moveUp(-MOVE_SPEED);
            }
            
            subtractCube();
            addCube();
        
            repaint();
        });
        timer.start();
    }
    
    
    public void initializeImages(){
        try {
            editModeFalse = ImageIO.read(new File("assets/editModeFalse.png"));
            editModeFalse = resizeImage(editModeFalse, 100, 100); 
            editModeTrue = ImageIO.read(new File("assets/editModeTrue.png"));
            editModeTrue = resizeImage(editModeTrue, 100, 100); 
            holdToEditFalse = ImageIO.read(new File("assets/holdToEditFalse.png"));
            holdToEditFalse = resizeImage(holdToEditFalse, 100, 100); 
            holdToEditTrue = ImageIO.read(new File("assets/holdToEditTrue.png"));
            holdToEditTrue = resizeImage(holdToEditTrue, 100, 100); 
            saveWorld = ImageIO.read(new File("assets/saveWorld.png"));
            saveWorld = resizeImage(saveWorld, 50, 100); 
            loadWorld = ImageIO.read(new File("assets/loadWorld.png"));
            loadWorld = resizeImage(loadWorld, 50, 100); 
            
            grass = ImageIO.read(new File("assets/grass.png"));
            grass = resizeImage(grass, 100, 100); 
            red = ImageIO.read(new File("assets/red.png"));
            red = resizeImage(red, 100, 100); 
            orange = ImageIO.read(new File("assets/orange.png"));
            orange = resizeImage(orange, 100, 100); 
            yellow = ImageIO.read(new File("assets/yellow.png"));
            yellow = resizeImage(yellow, 100, 100); 
            green = ImageIO.read(new File("assets/green.png"));
            green = resizeImage(green, 100, 100); 
            blue = ImageIO.read(new File("assets/blue.png"));
            blue = resizeImage(blue, 100, 100); 
            purple = ImageIO.read(new File("assets/purple.png"));
            purple = resizeImage(purple, 100, 100); 
            pink = ImageIO.read(new File("assets/pink.png"));
            pink = resizeImage(pink, 100, 100); 
            white = ImageIO.read(new File("assets/white.png"));
            white = resizeImage(white, 100, 100);
            brown = ImageIO.read(new File("assets/brown.png"));
            brown = resizeImage(brown, 100, 100); 
            black = ImageIO.read(new File("assets/black.png"));
            black = resizeImage(black, 100, 100); 
            gray = ImageIO.read(new File("assets/gray.png"));
            gray = resizeImage(gray, 100, 100); 


        } catch (IOException e) {
            e.printStackTrace();
        }
    
    
    }
    
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }
    
       
    public void setupKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
    
        // Key pressed
        im.put(KeyStroke.getKeyStroke("pressed W"), "forwardPressed");
        im.put(KeyStroke.getKeyStroke("pressed S"), "backwardPressed");
        im.put(KeyStroke.getKeyStroke("pressed A"), "leftPressed");
        im.put(KeyStroke.getKeyStroke("pressed D"), "rightPressed");
        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "upPressed");
        im.put(KeyStroke.getKeyStroke("pressed C"), "downPressed");
        im.put(KeyStroke.getKeyStroke("pressed L"), "toggleMouseLock");
        im.put(KeyStroke.getKeyStroke("pressed F"), "toggleHoldToEdit");
        im.put(KeyStroke.getKeyStroke("pressed P"), "saveWorld");
        im.put(KeyStroke.getKeyStroke("pressed O"), "loadWorld");
        //im.put(KeyStroke.getKeyStroke("pressed 1"), "cycleBack");
        //im.put(KeyStroke.getKeyStroke("pressed 2"), "cycleForward");

        // Key released
        im.put(KeyStroke.getKeyStroke("released W"), "forwardReleased");
        im.put(KeyStroke.getKeyStroke("released S"), "backwardReleased");
        im.put(KeyStroke.getKeyStroke("released A"), "leftReleased");
        im.put(KeyStroke.getKeyStroke("released D"), "rightReleased");
        im.put(KeyStroke.getKeyStroke("released SPACE"), "upReleased");
        im.put(KeyStroke.getKeyStroke("released C"), "downReleased");
    
        am.put("forwardPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                forwardPressed = true;
            }
        });
        am.put("forwardReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                forwardPressed = false;
            }
        });
    
        am.put("backwardPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                backwardPressed = true;
            }
        });
        am.put("backwardReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                backwardPressed = false;
            }
        });
    
        am.put("leftPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                leftPressed = true;
            }
        });
        am.put("leftReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                leftPressed = false;
            }
        });
    
        am.put("rightPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                rightPressed = true;
            }
        });
        am.put("rightReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                rightPressed = false;
            }
        });
        
        am.put("upPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                upPressed = true;
            }
        });
        am.put("upReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                upPressed = false;
            }
        });
        
        am.put("downPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                downPressed = true;
            }
        });
        am.put("downReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                downPressed = false;
            }
        });
        
        am.put("toggleMouseLock", new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                if (mouseLock){
                    mouseLock = false;
                    setCursor(defaultCursor);
                } else {
                    mouseLock = true;
                    setCursor(invisibleCursor);
                }
            }
        });
        
        am.put("toggleHoldToEdit", new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                holdToEdit = !holdToEdit;
                subtractPressed = false;
            }
        });
        
        am.put("saveWorld", new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                System.out.println("save world");
                saveWorld("saves/savefile.dat");

            }
        });
        
        am.put("loadWorld", new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                System.out.println("load world");
                loadWorld("saves/savefile.dat");

            }
        });
        /*am.put("cycleBack", new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                updateCurrCube(-1);
            }
        });
        
        am.put("cycleForward", new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                updateCurrCube(1);
            }
        });*/
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        if (recentering) return;
        if (!mouseLock) return;

        int dx = e.getXOnScreen() - centerX;
        int dy = e.getYOnScreen() - centerY;
    
        // Sensitivity
        double sensitivity = 0.005;
        camera.setYaw(camera.getYaw() + dx * sensitivity);
        camera.setPitch(camera.getPitch() + dy * sensitivity);
        
        recentering = true;
        robot.mouseMove(centerX, centerY);
        recentering = false;
    
        repaint();
    }
    
    public void mouseClicked(MouseEvent e){}
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            
            if (editMode){
                addPressed = true;
                if (!holdToEdit){
                     if (lookingAtCube != null && lookingAtFace != null){
                        addCube();
                    }
                    addPressed = false;
                }
            }

        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (editMode){
                subtractPressed = true;
                if (!holdToEdit){
                    subtractCube();
                    subtractPressed = false;
                }
            }
            
        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            editMode = !editMode;
        }    
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (holdToEdit && SwingUtilities.isLeftMouseButton(e)) {
            subtractPressed = false;
        } else if (holdToEdit && SwingUtilities.isRightMouseButton(e)){
            addPressed = false;
        }  
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
      @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        if (editMode){
            if (rotation < 0) {
                updateCurrCube(1);
            } else {
                updateCurrCube(-1);
            }
        }
    }
    
    
    public void saveWorld(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(world);
            System.out.println("World saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadWorld(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Save file not found. Skipping load.");
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            
            World newWorld = (World) in.readObject();

            Cube[][][] newCubeArray = newWorld.getWorld();
            Cube[][][] oldCubeArray = world.getWorld();
        
            for (int x = 0; x < world.getSize(); x++) {
                for (int y = 0; y < world.getSize(); y++) {
                    for (int z = 0; z < world.getSize(); z++) {
                        oldCubeArray[x][y][z] = newCubeArray[x][y][z];
                    }
                }
            }
        
            world.createColors();
            camera.resetPosition();
            System.out.println("World loaded from " + filename);
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
        
    
    public void updateCurrCube(int a){
        currCube += a;
        if (currCube == -1){
            currCube = 11;
            return;   
        }
        if (currCube == 12){
            currCube = 0;
            return;
        }       
    }
    

    public String getCameraDirection(){
        double yaw = -Math.toDegrees(camera.getYaw()); // camera yaw, not world yaw
        if (yaw < -157.5){
            return "S";
        }
        if (yaw < -112.5){
            return "SE";
        }
        if (yaw < -67.5){
            return "E";
        } 
        if (yaw < -22.5){
            return "NE";
        }
        if (yaw < 22.5){
            return "N";
        }
        if (yaw < 67.5){
            return "NW";
        } 
        if (yaw < 112.5){
            return "W";
        }
        if (yaw < 157.5){
            return "SW";
        }
        return "S";
    
    }
    
    public void addCube() {
        if (addPressed && lookingAtFace != null){
            
            Vector gridDirection = lookingAtFace.getDirection(); 
            int offsetX = (int) gridDirection.getX();
            int offsetY = (int) gridDirection.getY();
            int offsetZ = (int) gridDirection.getZ();
        
            int newX = (int)lookingAtCube.getX() + offsetX;
            int newY = (int)lookingAtCube.getY() + offsetY;
            int newZ = (int)lookingAtCube.getZ() + offsetZ;
            
            if (newX>=0 && newX<world.getSize() && newY>=0 && newY<world.getSize() && newZ>=0 && newZ<world.getSize()){
                Cube targetCube = world.getWorld()[newX][newY][newZ];
                if (!targetCube.hasCube()) {
                    targetCube.setCube(true);
                    Vector cameraPos = camera.getPosition();
                    if (world.isSolidNear(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ(), 0.2)){
                        targetCube.setCube(false);
                       // System.out.println("cannot place cube over camera");
                    } else {
                        targetCube.setType(getCubeType());
                        targetCube.createCorners();
                        targetCube.createFaces();
                    }
                }
            } else {
               // System.out.println("Out of bounds. Cannot add cubes");
            }  
        }  
    }
    
    public Type getCubeType(){
        if (currCube == 0){
            return Type.GRASS;
        } else if (currCube == 1) {
            return Type.RED;
        } else if (currCube == 2) {
             return Type.ORANGE;
        } else if (currCube == 3) {
            return Type.YELLOW;       
        } else if (currCube == 4) {
            return Type.GREEN;        
        } else if (currCube == 5) {
            return Type.BLUE;        
        } else if (currCube == 6) {
            return Type.PURPLE;        
        } else if (currCube == 7) {
            return Type.PINK;        
        } else if (currCube == 8) {
            return Type.WHITE;       
        } else if (currCube == 9) {
            return Type.BLACK;        
        } else if (currCube == 10) {
            return Type.BROWN;
        } else{
            return Type.GRAY;
        }
    }

    
    public void subtractCube(){
        if (subtractPressed){
            for (Cube[][] row : world.getWorld()) {
                for (Cube[] col : row) {
                    for (Cube cube : col) {
                        if (cube.getLooking()){
                            cube.setCube(false);
                            cube.setType(null);
                            cube.clearCorners();
                            cube.clearFaces();
                        }
                    }
                    
                }
            }
        }
    }
    
    
     public void paintComponent(Graphics g) {
    
        // camera frustum parameters
        double near = 0.1;
        double far = 1000.0;
        double fov = Math.toRadians(90);
        double aspect = (double) getWidth() / getHeight();

        
        Color floorColor = new Color(90,90,90);
        Color skyColor = new Color(180,214,255);
        
        super.paintComponent(g);
        setBackground(floorColor);
        
        
        // calculating horizon
        Vector horizon = new Vector(0, 0, far);  // horizon vector in camera space
        horizon.rotateX(camera.getPitch());
        Point horizonPoint = horizon.project(getWidth(), getHeight(), 90, getWidth() / getHeight(), 0.1, 1000);        
        
        if (horizonPoint.y < getHeight()){
            g.setColor(skyColor);
            g.fillRect(0,0,getWidth(),horizonPoint.y);

        } else {
            g.setColor(skyColor);
            g.fillRect(0,0,getWidth(),getHeight());     
        }     
        
        
        g.setColor(Color.WHITE);
        
        // drawing cube faces
        ArrayList<RenderableFace> visibleFaces = new ArrayList<>();
        lookingAtCube = null;
        double closestDist = MAX_REACH;
        
        for (Cube[][] row : world.getWorld()) {
            for (Cube[] col : row) {
                for (Cube cube : col) {
                    if (cube.hasCube()) {
                    
                        // draw cube corners
                       /* Vector[] corners = cube.getCorners();
                        for (Vector v : corners) {           
                            Vector temp = new Vector(v.getX(), v.getY(), v.getZ()); // clone
                            temp.transform(camera); // transforming the clone
                            Point p = temp.project(getWidth(), getHeight(), 90, getWidth()/getHeight(), 0.1, 1000);
                            if (p!=null){
                                g.fillRect(p.x, p.y, 2, 2);
                            }
                        }*/
                        
  
                        // transforming and finding which faces to draw
                        Face[] faces = cube.getTransformedFaces(camera);
                        for (Face f : faces) {
                           if (f.isFacingCamera()) {
                                visibleFaces.add(new RenderableFace(f, cube));
                           }
                        }
                        
                        if (editMode){
                            // figuring out which cube the camera is looking at currently
                            Cube tempCube = new Cube(cube.getX(), cube.getY(), cube.getZ());
                            tempCube.setCube(true);
                            tempCube.createCorners();
                            
                            //tempCube.transformCorners(camera);
                            Vector rayOrigin = camera.getPosition();
                            Vector rayDir = new Vector(
                                Math.cos(camera.getPitch()) * Math.sin(camera.getYaw()),
                                -Math.sin(camera.getPitch()),
                                Math.cos(camera.getPitch()) * Math.cos(camera.getYaw()) 
                            ).normalize();
                            rayDir = rayDir.normalize();
                                                     
                            double distance = tempCube.rayBoxIntersection(rayOrigin, rayDir);
    
                            if (distance > 0 && distance < closestDist) {
                                closestDist = distance;
                                lookingAtCube = cube;
                            }
                            
                        }
                        
                    }
                }
            }
        }
        
        lookingAtFace = null; 
        
        if (editMode){
            // mark closest cube
            for (Cube[][] row : world.getWorld()) {
                for (Cube[] col : row) {
                    for (Cube cube : col) {
                        cube.setLooking(false);
                        if (cube == lookingAtCube){
                        
                            cube.setLooking(true);
                            
                            // mark the closest face that is being looked at
                            double closestFaceDist = Double.POSITIVE_INFINITY;
                            Vector rayDir = camera.getPointer();
                            
                            for (Face f : cube.getTransformedFaces(camera)) {
                                Vector normal = f.getNormal();
                                Vector pointOnPlane = f.getCorners()[0];
                                
                                // avoid faces facing away
                                if (normal.dot(rayDir) >= 0) continue;
                            
                                // ray-plane intersection
                                double denom = normal.dot(rayDir);
                                if (Math.abs(denom) < 1e-6) continue; // nearly parallel
                                double t = pointOnPlane.dot(normal) / denom;
                                if (t < 0) continue; // intersection behind camera
                            
                                Vector hitPoint = rayDir.multiply(t);
                                if (f.containsPoint(hitPoint)) { // check if hit point is inside face
                                    if (t < closestFaceDist) {
                                        closestFaceDist = t;
                                        lookingAtFace = f;
                                    }
                                }
                            }

                                
                            
                            
                            
                        }
                    }
                }
            }
        }
        
        // sort faces by depth, then draw them
        Collections.sort(visibleFaces, (r1, r2) -> {
            double dist1 = r1.face.getCenter().magnitude();
            double dist2 = r2.face.getCenter().magnitude();
            return Double.compare(dist2, dist1);
        });
        
        
        
        ArrayList<ArrayList<Point>> outlinedFacesScreenPoints = new ArrayList<>(); // tracks which faces are part of the outlined cube
        
        
        for (RenderableFace rFace : visibleFaces) {
            Face face = rFace.face;
            Cube cube = rFace.sourceCube;
            
            // transfer Vector[] of corners to ArrayList
            Vector[] faceCorners = face.getCorners();
            ArrayList<Vector> polygon = new ArrayList<>();
            for (int a = 0; a < faceCorners.length; a++){
                polygon.add(faceCorners[a]);            
            }
            
            // clip the resulting polygon
            polygon = clipAgainstFrustum(polygon, 90, (double)getWidth() / getHeight(), 0.1);
                
            // if nothing remains, skip drawing
            if (polygon.size() < 3){
                continue;
            } 
            
            
            // contains 2D points after screen projection
            ArrayList<Point> screenPoints = new ArrayList<>();
            
            for (Vector v : polygon) {
     
                Point p = v.project(getWidth(), getHeight(), 90, getWidth()/getHeight(), near, far);
                if (p != null) {
                    screenPoints.add(p);
                }
            }
            
            // draw faces + wireframes
            if (screenPoints.size() >= 3) {
                int[] x = screenPoints.stream().mapToInt(p -> p.x).toArray();
                int[] y = screenPoints.stream().mapToInt(p -> p.y).toArray();
                
                // special outlined face gets saved for later
                if (cube.getLooking()){
                    outlinedFacesScreenPoints.add(screenPoints);
                }
           
                // fill in faces
                g.setColor(face.getColor());
                g.fillPolygon(x, y, screenPoints.size());
                
                // draw wireframes
                g.setColor(Color.BLACK);
                g.drawPolygon(x, y, screenPoints.size());
               
                
                
            }
            
            
         
        }
        if (editMode){
            // draw wireframe for outlined cube
            Graphics2D g2 = (Graphics2D) g.create(); 
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            
            for (ArrayList<Point> screenPoints : outlinedFacesScreenPoints){
                int[] x = screenPoints.stream().mapToInt(p -> p.x).toArray();
                int[] y = screenPoints.stream().mapToInt(p -> p.y).toArray();
                g2.drawPolygon(x, y, screenPoints.size());
            }
            // draw green wireframe for whatever face is being looked at
            
            if (lookingAtFace != null){
                g2.setColor(Color.BLUE);
                Vector[] faceCorners = lookingAtFace.getCorners();
                if (faceCorners == null) return;
                
                ArrayList<Point> facePoints = new ArrayList<>();
                for (Vector v : faceCorners){
                    Point p = v.project(getWidth(), getHeight(), 90, getWidth()/getHeight(), near, far);
                    if (p != null) {
                        facePoints.add(p);
                    }
                }
                if (facePoints!=null){
                    int[] x = facePoints.stream().mapToInt(p -> p.x).toArray();
                    int[] y = facePoints.stream().mapToInt(p -> p.y).toArray();
                    g2.drawPolygon(x, y, facePoints.size());
                }
            }
            g2.dispose();  
        }  
        

        
        g.setColor(Color.WHITE);
        
        if (mouseLock){
            // draw the "cursor" (a white dot)
            int centerX = getWidth()/2;
            int centerY = getHeight()/2;
            g.fillRect(centerX, centerY, 3, 3);
        }
        
        g.setColor(Color.YELLOW);
        
        String dir = getCameraDirection();
        
        // camera info in upper-left corner
        Vector pos = camera.getPosition();
        g.drawString(String.format("Position: (%.1f, %.1f, %.1f) (" + dir + ")", pos.getX(), pos.getY(), pos.getZ()), 10, 20);
        g.drawString(String.format("Yaw: %.1f degrees", -Math.toDegrees(camera.getYaw())), 10, 35);
        g.drawString(String.format("Pitch: %.1f degrees", -Math.toDegrees(camera.getPitch())), 10, 50);
        
        // other UI elements
        if (editMode){
            if (editModeTrue != null) {
                int editModeX = getWidth() - 120;
                int editModeY = 10;
                g.drawImage(editModeTrue, editModeX, editModeY, null);
            }             
            if (holdToEdit && holdToEditTrue != null) {
                int editModeX = getWidth() - 240;
                int editModeY = 10;
                g.drawImage(holdToEditTrue, editModeX, editModeY, null);
            } else if (!holdToEdit && holdToEditFalse != null){
                int editModeX = getWidth() - 240;
                int editModeY = 10;
                g.drawImage(holdToEditFalse, editModeX, editModeY, null);
            }
            
            int cubeX = getWidth()-120;
            int cubeY = getHeight()-120;
            
            
            if (currCube == 0){
                g.drawImage(grass, cubeX, cubeY, null);
            } else if (currCube == 1) {
                g.drawImage(red, cubeX, cubeY, null);
            } else if (currCube == 2) {
                g.drawImage(orange, cubeX, cubeY, null);
            } else if (currCube == 3) {
                g.drawImage(yellow, cubeX, cubeY, null);
            } else if (currCube == 4) {
                g.drawImage(green, cubeX, cubeY, null);
            } else if (currCube == 5) {
                g.drawImage(blue, cubeX, cubeY, null);
            } else if (currCube == 6) {
                g.drawImage(purple, cubeX, cubeY, null);
            } else if (currCube == 7) {
                g.drawImage(pink, cubeX, cubeY, null);
            } else if (currCube == 8) {
                g.drawImage(white, cubeX, cubeY, null);
            } else if (currCube == 9) {
                g.drawImage(black, cubeX, cubeY, null);
            } else if (currCube == 10) {
                g.drawImage(brown, cubeX, cubeY, null);
            } else{
                g.drawImage(gray, cubeX, cubeY, null);
            }

            
            
            
        } else {
            if (editModeFalse != null){
                int editModeX = getWidth() - 120;
                int editModeY = 10;
                g.drawImage(editModeFalse, editModeX, editModeY, null);
            }

        }
        
        if (saveWorld != null){
            int imgX = 10;
            int imgY = getHeight()-120;
            g.drawImage(saveWorld, imgX, imgY, null); 
        }
        if (loadWorld != null){
            int imgX = 80;
            int imgY = getHeight()-120;
            g.drawImage(loadWorld, imgX, imgY, null); 
        }

    }
    
    
    // clips polygon against 5 frustum planes (leaving out far plane)
    public static ArrayList<Vector> clipAgainstFrustum(ArrayList<Vector> polygon, double fovDegrees, double aspectRatio, double near) {
       
        double nearPlaneD = -near;
        double halfFOV = Math.toRadians(fovDegrees) / 2;
        double cosHalfFOV = Math.cos(halfFOV);
        double sinHalfFOV = Math.sin(halfFOV);
    
        // define clipping planes
        Vector nearPlaneNormal = new Vector(0, 0, 1);
        Vector rightPlaneNormal = new Vector(-cosHalfFOV, 0, sinHalfFOV).normalize();
        Vector leftPlaneNormal  = new Vector(cosHalfFOV, 0, sinHalfFOV).normalize();
        Vector topPlaneNormal   = new Vector(0, -cosHalfFOV, sinHalfFOV).normalize();
        Vector bottomPlaneNormal= new Vector(0, cosHalfFOV, sinHalfFOV).normalize();

    
        // clipping the polygon
        polygon = clipPolygonAgainstPlane(polygon, nearPlaneNormal, nearPlaneD);
        polygon = clipPolygonAgainstPlane(polygon, rightPlaneNormal, 0);
        polygon = clipPolygonAgainstPlane(polygon, leftPlaneNormal, 0);
        polygon = clipPolygonAgainstPlane(polygon, topPlaneNormal, 0);
        polygon = clipPolygonAgainstPlane(polygon, bottomPlaneNormal, 0);        
    
        return polygon;
    }
    
    // Sutherland-Hodgman clipping algorithm
    public static ArrayList<Vector> clipPolygonAgainstPlane(ArrayList<Vector> polygon, Vector planeNormal, double planeD) {
        ArrayList<Vector> clipped = new ArrayList<>();
    
        int count = polygon.size();
        for (int i = 0; i < count; i++) {
            Vector current = polygon.get(i);
            Vector previous = polygon.get((i - 1 + count) % count);
    
            double currentDist = planeNormal.dot(current) + planeD;
            double previousDist = planeNormal.dot(previous) + planeD;
    
            boolean currentInside = currentDist >= 0;
            boolean previousInside = previousDist >= 0;
    
            if (previousInside && currentInside) {
                clipped.add(current);
            } else if (previousInside && !currentInside) {
                clipped.add(intersectPlane(previous, current, planeNormal, planeD));
            } else if (!previousInside && currentInside) {
                clipped.add(intersectPlane(previous, current, planeNormal, planeD));
                clipped.add(current);
            }
        }
    
        return clipped;
    }
    
    // finding intersection between line and plane
    public static Vector intersectPlane(Vector a, Vector b, Vector planeNormal, double planeD) {
        Vector ab = b.subV(a);
        double t = -(planeNormal.dot(a) + planeD) / planeNormal.dot(ab);
        return a.addV(ab.multiply(t));
    }
    
        
    
}