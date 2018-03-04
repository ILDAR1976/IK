package edu.ik_01.ui;

import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Cylinder;
import javafx.scene.text.Text;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

@SuppressWarnings("restriction")
public class Bone extends Group {
	private Logger logger = LoggerFactory.getLogger(Bone.class);
	private final double SCALE = 1d;
	private DoubleProperty px;
	private DoubleProperty py;
	private DoubleProperty pz;
	private DoubleProperty sx;
	private DoubleProperty sy;
	private DoubleProperty sz;
	private JointFx pJoint;
	private JointFx sJoint;
	private Cylinder line;
	private BorderPane borderPane;
	private String name;
	
	public Bone(String name, DoubleProperty px, DoubleProperty py, DoubleProperty pz, DoubleProperty sx, DoubleProperty sy, DoubleProperty sz) {
		this.px = px;
		this.py = py;
		this.pz = pz;
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
		this.name = name;
		line = Line(new Point3D(px.get(),py.get(),pz.get()),new Point3D(sx.get(),sy.get(),sz.get()));
		pJoint = new JointFx("","joint", px.get(), py.get(), pz.get(), SCALE);
		sJoint = new JointFx("","joint", sx.get(), sy.get(), sz.get(), SCALE);
		
	        this.getChildren().addAll(pJoint, sJoint, line, borderPane);
	}
	
	public JointFx getpJoint() {
		return pJoint;
	}

	public JointFx getsJoint() {
		return sJoint;
	}

	private Cylinder Line(Point3D A, Point3D B) {
	    Point3D temp = A.subtract(B);
	    double Y = temp.getX() != 0 || temp.getZ() != 0 ? B.getY() : B.getY() > A.getY() ? B.getY() : A.getY();
	    Point3D dir = A.subtract(B).crossProduct(new Point3D(0, -1, 0));
	    double angle = Math.acos(A.subtract(B).normalize().dotProduct(new Point3D(0, -1, 0)));
	    double h1 = A.distance(B);
	    Cylinder cylinder = new Cylinder(5d, h1);
	    final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.RED);
        greyMaterial.setSpecularColor(Color.RED);
	    cylinder.setMaterial(greyMaterial);
	    cylinder.getTransforms().addAll(new Translate(B.getX(), Y - h1 / 2d, B.getZ()),
	            new Rotate(-Math.toDegrees(angle), 0d, h1 / 2d, 0d, new Point3D(dir.getX(), -dir.getY(), dir.getZ())));
		
	    Text text = new Text();
        text.setText(name);
        text.setCache(true);
        text.setFont(Font.font("Arial Narrow", FontWeight.BOLD, 8));
        Rotate rotateTextZ = new Rotate();
        { rotateTextZ.setAxis(Rotate.Z_AXIS); }
        rotateTextZ.setAngle(90d);
        text.setTranslateX(-10d);
        text.setTranslateY(5d);
        //text.getTransforms().addAll(rotateTextZ);
        this.borderPane = new BorderPane();
        this.borderPane.setStyle("-fx-background-color: transparent; ");
        this.borderPane.setTop(text);
        this.borderPane.setCache(true);
        this.borderPane.getTransforms().addAll(cylinder.getTransforms());
        this.borderPane.getTransforms().add(rotateTextZ);
	    
        return cylinder;
	}
}
