package edu.ik_01.ui;

import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;
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

@SuppressWarnings({ "restriction", "unused" })

public class MainController {
	private Logger logger = LoggerFactory.getLogger(MainController.class);
	@FXML
	private AnchorPane mainAnchor;

	@Autowired
	private ContactService contactService;

	@Qualifier("controlPanelView")
	@Autowired
	private ConfigurationControllers.View controlView;

    private Joint target;
    
    
	@FXML
	public void initialize() {
	}
	
	@SuppressWarnings({ })
	@PostConstruct
	public void init() {
		Group grp = new Group();
		ControlView control = ((ControlPanelController) controlView.getController()).getControlView();
		target = new Joint("target", new Affine(new Translate(50d, 50d, 50d)),Color.GREEN);
		
		int i = 0;
		
		grp.getChildren().add(new Joint("joint_" + ++i, new Affine(new Translate(50d, -50d, 50d)),Color.BLUE));
		grp.getChildren().add(new Joint("joint_" + ++i, new Affine(new Translate(-50d, 50d, 50d)),Color.BLUE));
		grp.getChildren().add(new Joint("joint_" + ++i, new Affine(new Translate(50d, 50d, -50d)),Color.BLUE));
		
		grp.getChildren().add(new Bone(new Point3D(50d, -50d, 50d), new Point3D(-50d, 50d, 50d), Color.RED));
		grp.getChildren().add(new Bone(new Point3D(-50d, 50d, 50d), new Point3D(50d, 50d, -50d), Color.RED));
		
		mainAnchor.getChildren().addAll(target, grp);

		bindTarget(grp);
		bind(grp);
	}
	
	private void bindTarget(Group grp) {
		ControlView control = ((ControlPanelController) controlView.getController()).getControlView();

		target.getAffine().txProperty().bindBidirectional(control.getS_tx().getValue());
		target.getAffine().tyProperty().bindBidirectional(control.getS_ty().getValue());
		target.getAffine().tzProperty().bindBidirectional(control.getS_tz().getValue());
		
		((Joint)grp.getChildren().get(0)).getAffine().txProperty().bindBidirectional(control.getS_mxx().getValue());
		((Joint)grp.getChildren().get(0)).getAffine().tyProperty().bindBidirectional(control.getS_mxy().getValue());
		((Joint)grp.getChildren().get(0)).getAffine().tzProperty().bindBidirectional(control.getS_mxz().getValue());
		
		((Joint)grp.getChildren().get(1)).getAffine().txProperty().bindBidirectional(control.getS_myx().getValue());
		((Joint)grp.getChildren().get(1)).getAffine().tyProperty().bindBidirectional(control.getS_myy().getValue());
		((Joint)grp.getChildren().get(1)).getAffine().tzProperty().bindBidirectional(control.getS_myz().getValue());
		
		((Joint)grp.getChildren().get(2)).getAffine().txProperty().bindBidirectional(control.getS_mzx().getValue());
		((Joint)grp.getChildren().get(2)).getAffine().tyProperty().bindBidirectional(control.getS_mzy().getValue());
		((Joint)grp.getChildren().get(2)).getAffine().tzProperty().bindBidirectional(control.getS_mzz().getValue());
	}
	
	private void bind(Group grp) {
		((Bone) grp.getChildren().get(3)).getPointAX().bind(((Joint) grp.getChildren().get(0)).getAffine().txProperty());
		((Bone) grp.getChildren().get(3)).getPointAY().bind(((Joint) grp.getChildren().get(0)).getAffine().tyProperty());
		((Bone) grp.getChildren().get(3)).getPointAZ().bind(((Joint) grp.getChildren().get(0)).getAffine().tzProperty());

		((Bone) grp.getChildren().get(3)).getPointBX().bind(((Joint) grp.getChildren().get(1)).getAffine().txProperty());
		((Bone) grp.getChildren().get(3)).getPointBY().bind(((Joint) grp.getChildren().get(1)).getAffine().tyProperty());
		((Bone) grp.getChildren().get(3)).getPointBZ().bind(((Joint) grp.getChildren().get(1)).getAffine().tzProperty());
	
		((Bone) grp.getChildren().get(4)).getPointAX().bind(((Joint) grp.getChildren().get(1)).getAffine().txProperty());
		((Bone) grp.getChildren().get(4)).getPointAY().bind(((Joint) grp.getChildren().get(1)).getAffine().tyProperty());
		((Bone) grp.getChildren().get(4)).getPointAZ().bind(((Joint) grp.getChildren().get(1)).getAffine().tzProperty());

		((Bone) grp.getChildren().get(4)).getPointBX().bind(((Joint) grp.getChildren().get(2)).getAffine().txProperty());
		((Bone) grp.getChildren().get(4)).getPointBY().bind(((Joint) grp.getChildren().get(2)).getAffine().tyProperty());
		((Bone) grp.getChildren().get(4)).getPointBZ().bind(((Joint) grp.getChildren().get(2)).getAffine().tzProperty());
	}
}
