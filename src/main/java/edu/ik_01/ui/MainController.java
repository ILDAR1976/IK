package edu.ik_01.ui;

import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.transform.Rotate;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.ik_01.ConfigurationControllers;
import edu.ik_01.service.ContactService;
import javax.annotation.PostConstruct;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
/**
 * Date: 27.08.15 Time: 11:10
 *
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 * @author http://mruslan.com
 */
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

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		Group grp = new Group();
		ControlView control = ((ControlPanelController) controlView.getController()).getControlView();

		Skeleton skeleton = new Skeleton();
		
		Bone bone = new Bone("First", new SimpleDoubleProperty(0d), new SimpleDoubleProperty(0d), new SimpleDoubleProperty(0d), 
							 new SimpleDoubleProperty(50d), new SimpleDoubleProperty(0d), new SimpleDoubleProperty(0d));
	
		
	       
		joints.add(bone.getpJoint());
		joints.add(bone.getsJoint());
		
		skeleton.addChildren(bone);
		
		Label[][] lables = control.getLabel();

		grp.getChildren().addAll(skeleton);
		
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
}
