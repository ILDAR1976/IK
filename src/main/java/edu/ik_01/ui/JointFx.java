package edu.ik_01.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.paint.Color;

@SuppressWarnings({ "restriction", "unused" })
public final class JointFx extends Group {
    private int meshId;
    private int jointId;
    private String name;
    
    private Affine affine = new Affine();
	
    final Translate t = new Translate();

    final Rotate jox = new Rotate();
    final Rotate joy = new Rotate();
    final Rotate joz = new Rotate();

    final Rotate rx = new Rotate();
    final Rotate ry = new Rotate();
    final Rotate rz = new Rotate();

    final Scale s = new Scale();
    final Scale is = new Scale();
    
    // should bind "is" to be in the inverse of the parent's "s"
	    
    private List<Affine> transformations = new ArrayList<>();
    private Affine offsetMatrix;
    private double scale = .4d;

    public JointFx(String id, String name) {
        super();

        this.setId(id);
        this.name = name;
        this.offsetMatrix = new Affine();
        this.affine = new Affine();
      	this.affine.setMxx(1d);
    	this.affine.setMyy(1d);
    	this.affine.setMzz(1d);
        
        this.joz.setAxis(Rotate.Z_AXIS);
        this.joy.setAxis(Rotate.Y_AXIS);
        this.jox.setAxis(Rotate.X_AXIS);

        this.rx.setAxis(Rotate.X_AXIS);
        this.ry.setAxis(Rotate.Y_AXIS);
        this.rz.setAxis(Rotate.Z_AXIS);

        this.getTransforms().addAll(t, is, joz, joy, jox, rz, ry, rx, s, affine);
	
        this.addMeshView();
        
    }
    
    public JointFx(String id, String name, double x, double y, double z, double scale) {
    	this(id, name);
    	this.scale = scale;
    	this.affine.setMxx(scale);
    	this.affine.setMyy(scale);
    	this.affine.setMzz(scale);
    	
    	this.affine.setTx(x);
    	this.affine.setTy(y);
    	this.affine.setTz(z);
    	
    }
    
    public JointFx(String id, String name, double x, double y, double z, double scale, Color color) {
    	this(id, name, x, y, z, scale);
    	
    	((PhongMaterial)((MeshView)this.getChildren().get(0)).getMaterial()).setDiffuseColor(color);
    	
    }

    public JointFx(String id, String name, Affine offsetMatrix) {
        super();

        this.setId(id);
        this.name = name;
        this.offsetMatrix = offsetMatrix;
        this.affine = new Affine();
        
        this.joz.setAxis(Rotate.Z_AXIS);
        this.joy.setAxis(Rotate.Y_AXIS);
        this.jox.setAxis(Rotate.X_AXIS);

        this.rx.setAxis(Rotate.X_AXIS);
        this.ry.setAxis(Rotate.Y_AXIS);
        this.rz.setAxis(Rotate.Z_AXIS);

        this.getTransforms().addAll(t, is, joz, joy, jox, rz, ry, rx, s, affine);
	}

	public TriangleMesh createCubeMesh() {

        float width = 20f;
        float points[] = {
                -width, -width, -width,
                width, -width, -width,
                width, width, -width,
                -width, width, -width,
                -width, -width, width,
                width, -width, width,
                width, width, width,
                -width, width, width};

        float texCoords[] = {0, 0, 1, 0, 1, 1, 0, 1};

        int faceSmoothingGroups[] = {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        int faces[] = {
                0, 0, 2, 2, 1, 1,
                2, 2, 0, 0, 3, 3,
                1, 0, 6, 2, 5, 1,
                6, 2, 1, 0, 2, 3,
                5, 0, 7, 2, 4, 1,
                7, 2, 5, 0, 6, 3,
                4, 0, 3, 2, 0, 1,
                3, 2, 4, 0, 7, 3,
                3, 0, 6, 2, 2, 1,
                6, 2, 3, 0, 7, 3,
                4, 0, 1, 2, 5, 1,
                1, 2, 4, 0, 0, 3,
        };

        final TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().setAll(points);
        mesh.getTexCoords().setAll(texCoords);
        mesh.getFaces().setAll(faces);
        mesh.getFaceSmoothingGroups().setAll(faceSmoothingGroups);

        return mesh;
    }

    public void addMeshView() {
        final MeshView meshView = new MeshView(createCubeMesh());
		PhongMaterial material = new PhongMaterial(Color.BISQUE);
		meshView.setMaterial(material);
		meshView.setDrawMode(DrawMode.FILL);
		meshView.setCullFace(CullFace.BACK);

        getChildren().add(meshView);
    }

	public List<Affine> getTransformations() {
		return transformations;
	}

	public void setTransformations(List<Affine> transformations) {
		this.transformations = transformations;
	}

	public Affine getOffsetMatrix() {
		return offsetMatrix;
	}

	public void setOffsetMatrix(Affine offsetMatrix) {
		this.offsetMatrix = offsetMatrix;
	}
	
	public Affine getAffine() {
		return affine;
	}
	
	public void setAffine(Affine affine) {
		this.affine = affine;
	}

	public int getMeshId() {
		return meshId;
	}

	public void setMeshId(int meshId) {
		this.meshId = meshId;
	}

	public String getJointName() {
		return name;
	}

	public void setJointName(String name) {
		this.name = name;
	}

	public int getJointId() {
		return jointId;
	}

	public void setJointId(int jointId) {
		this.jointId = jointId;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	
	
}
