import java.io.Serializable;

public class RenderableFace implements Serializable{
    private static final long serialVersionUID = 1L;
    
    Face face;
    Cube sourceCube;
    
    RenderableFace(Face f, Cube s){
        face = f;
        sourceCube = s;
    }

}