package edu.ik_01.ui;

import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.ik_01.ConfigurationControllers;
import edu.ik_01.service.ContactService;
import javax.annotation.PostConstruct;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

@SuppressWarnings({ "SpringJavaAutowiringInspection", "restriction", "unused" })
public class MainController {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(MainController.class);
	@FXML
	private AnchorPane mainAnchor;

	@Autowired
	private ContactService contactService;
	private List<JointFx> joints = new ArrayList<>();

	@Qualifier("controlPanelView")
	@Autowired

	private ConfigurationControllers.View controlView;

	
	private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    private JointFx target;

    private Bone bone;
    private Bone bone2;
    private Cylinder cylinder = new Cylinder(3d, 3d);
    
	@FXML
	public void initialize() {
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		Group grp = new Group();
		ControlView control = ((ControlPanelController) controlView.getController()).getControlView();

		Skeleton skeleton = new Skeleton();
		target = new JointFx("Target", "joint", 0d, 0d, 0d, .5d, Color.GREEN);

		target.translateXProperty().bind(control.getS_mxx().getValue());
		target.translateYProperty().bind(control.getS_mxy().getValue());
		target.translateZProperty().bind(control.getS_mxz().getValue());

		target.translateXProperty().addListener(new ChangeListener(){
 
     		@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				listener();
			}
		});
		
		target.translateYProperty().addListener(new ChangeListener(){
			 
     		@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				listener();
			}
		});

		target.translateZProperty().addListener(new ChangeListener(){
			 
     		@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				listener();
			}
		});

		
		bone = new Bone("First", new SimpleDoubleProperty(0d), new SimpleDoubleProperty(0d),
				new SimpleDoubleProperty(0d), new SimpleDoubleProperty(100d), new SimpleDoubleProperty(0d),
				new SimpleDoubleProperty(0d));
		
		skeleton.addChildren(bone);
		

		Label[][] lables = control.getLabel();

		grp.getChildren().addAll(skeleton, target, cylinder);

		mainAnchor.getChildren().add(grp);
	}

	private void globalBinding(Group grp, ControlView control, Label[][] lables) {
		JointFx jointFx1 = (JointFx) grp.getChildren().get(0);
		lables[0][0].textProperty().bind(jointFx1.getAffine().mxxProperty().asString());
		lables[1][0].textProperty().bind(jointFx1.getAffine().mxyProperty().asString());
		lables[2][0].textProperty().bind(jointFx1.getAffine().mxzProperty().asString());
		lables[3][0].textProperty().bind(jointFx1.getAffine().txProperty().asString());

		lables[0][1].textProperty().bind(jointFx1.getAffine().myxProperty().asString());
		lables[1][1].textProperty().bind(jointFx1.getAffine().myyProperty().asString());
		lables[2][1].textProperty().bind(jointFx1.getAffine().myzProperty().asString());
		lables[3][1].textProperty().bind(jointFx1.getAffine().tyProperty().asString());

		lables[0][2].textProperty().bind(jointFx1.getAffine().mzxProperty().asString());
		lables[1][2].textProperty().bind(jointFx1.getAffine().mzyProperty().asString());
		lables[2][2].textProperty().bind(jointFx1.getAffine().mzzProperty().asString());
		lables[3][2].textProperty().bind(jointFx1.getAffine().tzProperty().asString());

		jointFx1.getAffine().mxxProperty().bindBidirectional(control.getS_mxx().getValue());
		jointFx1.getAffine().mxyProperty().bindBidirectional(control.getS_mxy().getValue());
		jointFx1.getAffine().mxzProperty().bindBidirectional(control.getS_mxz().getValue());

		jointFx1.getAffine().myxProperty().bindBidirectional(control.getS_myx().getValue());
		jointFx1.getAffine().myyProperty().bindBidirectional(control.getS_myy().getValue());
		jointFx1.getAffine().myzProperty().bindBidirectional(control.getS_myz().getValue());

		jointFx1.getAffine().mzxProperty().bindBidirectional(control.getS_mzx().getValue());
		jointFx1.getAffine().mzyProperty().bindBidirectional(control.getS_mzy().getValue());
		jointFx1.getAffine().mzzProperty().bindBidirectional(control.getS_mzz().getValue());

		jointFx1.getAffine().txProperty().bindBidirectional(control.getS_tx().getValue());
		jointFx1.getAffine().tyProperty().bindBidirectional(control.getS_ty().getValue());
		jointFx1.getAffine().tzProperty().bindBidirectional(control.getS_tz().getValue());

		jointFx1.scaleXProperty().set(15d);
		jointFx1.scaleYProperty().set(15d);
		jointFx1.scaleZProperty().set(15d);

		// line 1
		DoubleBinding p_mxx = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.cos(control.getRotateX().get()) * Math.cos(control.getRotateY().get())
						- Math.sin(control.getRotateX().get()) * Math.cos(control.getRotateZ().get())
								* Math.sin(control.getRotateY().get()));
			}
		};

		DoubleBinding p_mxy = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.sin(control.getRotateX().get()) * Math.cos(control.getRotateY().get())
						+ Math.cos(control.getRotateX().get()) * Math.cos(control.getRotateZ().get())
								* Math.sin(control.getRotateY().get()));
			}
		};

		DoubleBinding p_mxz = new DoubleBinding() {
			{
				super.bind(control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.sin(control.getRotateZ().get()) * Math.sin(control.getRotateY().get()));
			}
		};

		// line 2
		DoubleBinding p_myx = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (-Math.cos(control.getRotateX().get()) * Math.sin(control.getRotateY().get())
						- Math.sin(control.getRotateX().get()) * Math.cos(control.getRotateZ().get())
								* Math.cos(control.getRotateY().get()));
			}
		};

		DoubleBinding p_myy = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (-Math.sin(control.getRotateX().get()) * Math.sin(control.getRotateY().get())
						+ Math.cos(control.getRotateX().get()) * Math.cos(control.getRotateZ().get())
								* Math.cos(control.getRotateY().get()));
			}
		};

		DoubleBinding p_myz = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.sin(control.getRotateZ().get()) * Math.cos(control.getRotateY().get()));
			}
		};

		// line 3
		DoubleBinding p_mzx = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.sin(control.getRotateX().get()) * Math.sin(control.getRotateZ().get()));
			}
		};

		DoubleBinding p_mzy = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (-Math.cos(control.getRotateX().get()) * Math.sin(control.getRotateZ().get()));
			}
		};

		DoubleBinding p_mzz = new DoubleBinding() {
			{
				super.bind(control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.cos(control.getRotateZ().get()));
			}
		};

		control.getS_mxx().getValue().bind(p_mxx);
		control.getS_mxy().getValue().bind(p_mxy);
		control.getS_mxz().getValue().bind(p_mxz);

		control.getS_myx().getValue().bind(p_myx);
		control.getS_myy().getValue().bind(p_myy);
		control.getS_myz().getValue().bind(p_myz);

		control.getS_mzx().getValue().bind(p_mxx);
		control.getS_mzy().getValue().bind(p_mxy);
		control.getS_mzz().getValue().bind(p_mzz);

		control.getRotateX().bind(control.getS_rx().getValue());
		control.getRotateY().bind(control.getS_ry().getValue());
		control.getRotateZ().bind(control.getS_rz().getValue());

	}

	private void globalBinding2(JointFx jointFx1, ControlView control) {
		// line 1
		DoubleBinding p_mxx = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.cos(control.getRotateX().get()) * Math.cos(control.getRotateY().get())
						- Math.sin(control.getRotateX().get()) * Math.cos(control.getRotateZ().get())
								* Math.sin(control.getRotateY().get()));
			}
		};

		DoubleBinding p_mxy = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.sin(control.getRotateX().get()) * Math.cos(control.getRotateY().get())
						+ Math.cos(control.getRotateX().get()) * Math.cos(control.getRotateZ().get())
								* Math.sin(control.getRotateY().get()));
			}
		};

		DoubleBinding p_mxz = new DoubleBinding() {
			{
				super.bind(control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.sin(control.getRotateZ().get()) * Math.sin(control.getRotateY().get()));
			}
		};

		// line 2
		DoubleBinding p_myx = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (-Math.cos(control.getRotateX().get()) * Math.sin(control.getRotateY().get())
						- Math.sin(control.getRotateX().get()) * Math.cos(control.getRotateZ().get())
								* Math.cos(control.getRotateY().get()));
			}
		};

		DoubleBinding p_myy = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (-Math.sin(control.getRotateX().get()) * Math.sin(control.getRotateY().get())
						+ Math.cos(control.getRotateX().get()) * Math.cos(control.getRotateZ().get())
								* Math.cos(control.getRotateY().get()));
			}
		};

		DoubleBinding p_myz = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateY(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.sin(control.getRotateZ().get()) * Math.cos(control.getRotateY().get()));
			}
		};

		// line 3
		DoubleBinding p_mzx = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.sin(control.getRotateX().get()) * Math.sin(control.getRotateZ().get()));
			}
		};

		DoubleBinding p_mzy = new DoubleBinding() {
			{
				super.bind(control.getRotateX(), control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (-Math.cos(control.getRotateX().get()) * Math.sin(control.getRotateZ().get()));
			}
		};

		DoubleBinding p_mzz = new DoubleBinding() {
			{
				super.bind(control.getRotateZ());
			}

			@Override
			protected double computeValue() {
				return (Math.cos(control.getRotateZ().get()));
			}
		};

		jointFx1.getAffine().mxxProperty().bind(p_mxx);
		jointFx1.getAffine().mxyProperty().bind(p_mxy);
		jointFx1.getAffine().mxzProperty().bind(p_mxz);

		jointFx1.getAffine().myxProperty().bind(p_myx);
		jointFx1.getAffine().myyProperty().bind(p_myy);
		jointFx1.getAffine().myzProperty().bind(p_myz);

		jointFx1.getAffine().mzxProperty().bind(p_mzx);
		jointFx1.getAffine().mzyProperty().bind(p_mzy);
		jointFx1.getAffine().mzzProperty().bind(p_mzz);

		control.getRotateX().bind(control.getS_rx().getValue());
		control.getRotateY().bind(control.getS_ry().getValue());
		control.getRotateZ().bind(control.getS_rz().getValue());

	}

	private void globalBinding3(JointFx jointFx1, JointFx jointFx2, ControlView control) {
		jointFx1.getAffine().txProperty().bindBidirectional(control.getS_mxx().getValue());
		jointFx1.getAffine().tyProperty().bindBidirectional(control.getS_mxy().getValue());
		jointFx1.getAffine().tzProperty().bindBidirectional(control.getS_mxz().getValue());

		jointFx2.getAffine().txProperty().bindBidirectional(control.getS_myx().getValue());
		jointFx2.getAffine().tyProperty().bindBidirectional(control.getS_myy().getValue());
		jointFx2.getAffine().tzProperty().bindBidirectional(control.getS_myz().getValue());
	}

	public void handleMouse(Scene scene) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;
                double modifierFactor = 0.1;

 
            }
        });
	
    }

	private synchronized void listener() {
		
	
	    final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.BLUE);
        greyMaterial.setSpecularColor(Color.BLUE);
	    cylinder.setMaterial(greyMaterial);
	    
/*	    cylinder.getTransforms().clear();
	    cylinder.getTransforms().addAll(calcDirection(
	    		new Point3D(0d,
	    				    0d,
	    				    0d),
	    		new Point3D(target.getTranslateX(),target.getTranslateY(),target.getTranslateZ())));
*/	    
	    bone.getTransforms().clear();
	    bone.getTransforms().addAll(calcDirection2(
	    		new Point3D(bone.getpJoint().getAffine().getTx(),
    				    bone.getpJoint().getAffine().getTy(),
    				    bone.getpJoint().getAffine().getTz()),
    		    new Point3D(target.getTranslateX(),target.getTranslateY(),target.getTranslateZ())
				));
	}
	
	private ObservableList<Transform> calcDirection(Point3D A, Point3D B) {
		    Point3D temp = A.subtract(B);
		    double Y = temp.getX() != 0 || temp.getZ() != 0 ? B.getY() : B.getY() > A.getY() ? B.getY() : A.getY();
		    Point3D dir = A.subtract(B).crossProduct(new Point3D(0, -1, 0));
		    double angle = Math.acos(A.subtract(B).normalize().dotProduct(new Point3D(0, -1, 0)));
		    double h1 = A.distance(B);	
		    cylinder.setHeight(h1);
		    List<Transform> transformations = new ArrayList<>();
		    ObservableList<Transform> transforms = FXCollections.observableList(transformations);
		    transforms.addAll(new Translate(B.getX(), Y - h1 / 2d, B.getZ()),
		            new Rotate(-Math.toDegrees(angle), 0d, h1 / 2d, 0d, new Point3D(dir.getX(), -dir.getY(), dir.getZ())));
		    return transforms;
	}
	
	private ObservableList<Transform> calcDirection2(Point3D A, Point3D B) {
	    Point3D temp = A.subtract(B);
	    double Y = temp.getX() != 0 || temp.getZ() != 0 ? B.getY() : B.getY() > A.getY() ? B.getY() : A.getY();
	    Point3D dir = A.subtract(B).crossProduct(new Point3D(0, -1, 0));
	    double angle = Math.acos(A.subtract(B).normalize().dotProduct(new Point3D(0, 1, 0)));
	    double h1 = A.distance(B);	
	    
	    List<Transform> transformations = new ArrayList<>();
	    ObservableList<Transform> transforms = FXCollections.observableList(transformations);
	    transforms.addAll(new Translate(A.getX(),A.getY(),A.getZ()),
	            new Rotate(Math.toDegrees(angle), A.getX(),A.getY(),A.getZ(), new Point3D(dir.getX(), -dir.getY(), dir.getZ())),
	            new Rotate(-90, 0, 0, 0, Rotate.Z_AXIS));

	    return transforms;
}
}
