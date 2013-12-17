/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.control.ElevatorControl;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import java.util.ArrayList;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;

/**
 *
 * @author martin
 */
public class Elevator {

    public MaterialDistributor md;
    public TextureDistributor td;
    private ArrayList<Spatial> worldSpatialParts = null;
    private Node elevatorNode = new Node("elevator");
    private Main.MainContainer mc;
    private ArrayList<Spatial> elevatorSpatialParts;
    private final AssetManager am;
    private InputManager im;
    private MassFactory mf;
    public ElevatorControl sc;

    public Elevator(Main.MainContainer mainContainer) {
        this.mc = mainContainer;
        this.am = mc.assetManager;
        this.elevatorSpatialParts = new ArrayList<Spatial>();
        this.md = new MaterialDistributor(am);
        this.td = new TextureDistributor(am);
        this.im = mc.im;
        this.mf = new MassFactory(mc.bas);
    }

    public ArrayList<Spatial> createElevatorSpatials() {
        elevatorSpatialParts.add(drawElevator());
        elevatorSpatialParts.add(drawElevatorDoors());
        elevatorSpatialParts.add(drawInsideOfElevator());
        return elevatorSpatialParts;
    }

    public Node drawUndergroundElevatorDoors() {
        Node elevatorDoorsUnderground = new Node("elevatorDoorsUnderground");

        Spatial leftDoor = drawWall("leftElevatorDoorUnderground", 10, 15, 1, TextureDistributor.TEXTURE_ALUMINIUM, true, 1, 1, false, 90, new Vector3f(0, 1, 0));
        leftDoor.setLocalTranslation(new Vector3f(452, -165, 325f));
        // elevatorDoorsUnderground.attachChild(leftDoor);

        Spatial rightDoor = drawWall("rightElevatorDoorUnderground", 10, 15, 1, TextureDistributor.TEXTURE_ALUMINIUM, true, 1, 1, false, 90, new Vector3f(0, 1, 0));
        rightDoor.setLocalTranslation(new Vector3f(452, -165, 294f));
        //  elevatorDoorsUnderground.attachChild(rightDoor);

        return elevatorDoorsUnderground;
    }

    private Spatial drawElevator() {
        Node tempElevatorNode = new Node("tempElevatorNode");

        Spatial leftWall = drawWall("leftElevatorWallUnderground", 30, 120, 1, TextureDistributor.TEXTURE_GARAGEDOOR, true, 1, 2, false, 0, new Vector3f(0, 0, 0));
        leftWall.setLocalTranslation(new Vector3f(482, -85, 290));
        tempElevatorNode.attachChild(leftWall);

        Spatial rightWall = drawWall("rightElevatorWallUnderground", 30, 120, 1, TextureDistributor.TEXTURE_GARAGEDOOR, true, 1, 2, false, 0, new Vector3f(0, 0, 0));
        rightWall.setLocalTranslation(new Vector3f(482, -85, 330));
        tempElevatorNode.attachChild(rightWall);

        Spatial backWall = drawWall("backElevatorWallUnderground", 20, 120, 1, TextureDistributor.TEXTURE_GARAGEDOOR, true, 1, 2, false, 90, new Vector3f(0, 1, 0));
        backWall.setLocalTranslation(new Vector3f(510, -125, 310));
        tempElevatorNode.attachChild(backWall);

        Spatial frontWall = drawWall("frontElevatorWallUnderground", 20, 92.5f, 1, TextureDistributor.TEXTURE_GARAGEDOOR, true, 1, 2, false, 90, new Vector3f(0, 1, 0));
        frontWall.setLocalTranslation(new Vector3f(452, -57.5f, 310));
        tempElevatorNode.attachChild(frontWall);

        //Button to start and stop

        Spatial upButton = drawWall("buttonDown", 3f, 3f, 0.1f, TextureDistributor.TEXTURE_DOWN, false, 1, 1, false, 90, new Vector3f(0, 1, 0));
        upButton.setLocalTranslation(new Vector3f(507, 13, 331));
        tempElevatorNode.attachChild(upButton);

        Spatial downButton = drawWall("buttonDown", 3f, 3f, 0.1f, TextureDistributor.TEXTURE_DOWN, false, 1, 1, false, 90, new Vector3f(0, 1, 0));
        downButton.setLocalTranslation(new Vector3f(507, 13, 289));
        tempElevatorNode.attachChild(downButton);

        //secret special button

        Spatial specialButton = drawWall("specialButton", 0.1f, 5f, 5f, TextureDistributor.TEXTURE_COMET, false, 1, 1, false, 90, new Vector3f(0, 1, 0));
        specialButton.setLocalTranslation(new Vector3f(451, 13, 312));
        tempElevatorNode.attachChild(specialButton);

        //roof
        Spatial roof = drawWindow(tempElevatorNode);
        return tempElevatorNode;
    }

    public Spatial drawElevatorDoors() {

        //HACK Assigning mass here isn't ideal, I believe.
        //above ground
        Node elevatorDoors = new Node("elevatorDoors");
        ArrayList controlSpatials = new ArrayList<Spatial>();
        ArrayList controlNodes = new ArrayList<Node>();
        Spatial leftDoor = drawWall("leftElevatorDoor", 10, 20, 1, TextureDistributor.TEXTURE_ALUMINIUM, true, 2, 2, false, 90, new Vector3f(0, 1, 0));
        leftDoor.setLocalTranslation(new Vector3f(513, 15, 320.5f));
        mf.assignMass(leftDoor, 0, Vector3f.ZERO, true);
        elevatorDoors.attachChild(leftDoor);
        controlSpatials.add(leftDoor);

        Spatial rightDoor = drawWall("rightElevatorDoor", 10, 20, 1, TextureDistributor.TEXTURE_ALUMINIUM, true, 2, 2, false, 90, new Vector3f(0, 1, 0));
        rightDoor.setLocalTranslation(new Vector3f(513, 15, 299.5f));
        mf.assignMass(rightDoor, 0, Vector3f.ZERO, true);
        elevatorDoors.attachChild(rightDoor);
        controlSpatials.add(rightDoor);


        //Underground

        Spatial leftDoorUnderground = drawWall("leftElevatorDoorUnderground", 10, 15, 1, TextureDistributor.TEXTURE_ALUMINIUM, true, 1, 1, false, 90, new Vector3f(0, 1, 0));
        leftDoorUnderground.setLocalTranslation(new Vector3f(452, -165, 334.5f));
        mf.assignMass(leftDoorUnderground, 0, Vector3f.ZERO);
        elevatorDoors.attachChild(leftDoorUnderground);
        elevatorDoors.attachChild(leftDoorUnderground);
        controlSpatials.add(leftDoorUnderground);

        Spatial rightDoorUnderground = drawWall("rightElevatorDoorUnderground", 10, 15, 1, TextureDistributor.TEXTURE_ALUMINIUM, true, 1, 1, false, 90, new Vector3f(0, 1, 0));
        rightDoorUnderground.setLocalTranslation(new Vector3f(452, -165, 299.5f));
        Quaternion deg = new Quaternion();
        deg.fromAngleAxis(90 * FastMath.DEG_TO_RAD, new Vector3f(0, 1, 0.16f));
        rightDoorUnderground.setLocalRotation(deg);
        mf.assignMass(rightDoorUnderground, 0, Vector3f.ZERO);
        elevatorDoors.attachChild(rightDoorUnderground);
        elevatorDoors.attachChild(rightDoorUnderground);
        controlSpatials.add(rightDoorUnderground);

        //This node contains all the elements moving on the y-axis to move the elevators (doors don't move on y, only x or z).


        //Uncomment the line below to add the elevator back in.
        //Node elementsOfElevator = buildInsideOfElevator();
        Node elementsOfElevator = new Node("elementsOfElevator");
        Node bottomElevatorDoors = drawUndergroundElevatorDoors();
        //adding the elements moving on the y-axis.
        controlNodes.add(elementsOfElevator);
        controlNodes.add(bottomElevatorDoors);
        //creating control, sending all spatials of the entire elevator and doors.
        elementsOfElevator.setLocalTranslation(new Vector3f(0, 0, 0));
        sc = new ElevatorControl(controlSpatials, controlNodes, true, mc.im);
        elevatorDoors.addControl(sc);
        //im.addListener(sc, KeyMapper.BRING_ELEVATOR_UP);
        //The elements of the elevator is now a sub-node to the elevator-node (contains walls etc).
        elevatorDoors.attachChild(elementsOfElevator);
        return elevatorDoors;
    }

    public Node drawInsideOfElevator() {
        Node insideOfElevatorNode = new Node("insideOfElevatorNode");
        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(90 * FastMath.DEG_TO_RAD, new Vector3f(0, 1, 0));

        Spatial floor = drawWall("elevatorFloor", 28, 1, 20, TextureDistributor.TEXTURE_SILVER, true, 1, 1, false, 0, new Vector3f(0, 0, 0));
        floor.setLocalTranslation(new Vector3f(482, -180, 310));
        insideOfElevatorNode.attachChild(floor);

        Spatial roof = drawWall("elevatorRoof", 28, 1, 20, TextureDistributor.TEXTURE_SILVER, true, 1, 1, false, 0, new Vector3f(0, 0, 0));
        roof.setLocalTranslation(new Vector3f(482, -143, 310));
        insideOfElevatorNode.attachChild(roof);

        Spatial rightWall = drawWall("insideElevatorRightWall", 30, 30, 0.1f, TextureDistributor.TEXTURE_SILVER, true, 1, 1, false, 0, new Vector3f(0, 0, 0));
        rightWall.setLocalTranslation(new Vector3f(482, -173, 291));
        insideOfElevatorNode.attachChild(rightWall);

        Spatial leftWall = rightWall.clone();
        leftWall.setLocalTranslation(new Vector3f(482, -173, 327));
        insideOfElevatorNode.attachChild(leftWall);

        Spatial railing1 = drawWall("railingRight", 24, 0.3f, 0.3f, TextureDistributor.TEXTURE_SILVER, true, 1, 1, false, 0, new Vector3f(0, 0, 0));
        railing1.setLocalTranslation(new Vector3f(482, -165, 325));
        insideOfElevatorNode.attachChild(railing1);

        Spatial railing2 = railing1.clone();
        railing2.setLocalTranslation(new Vector3f(482, -165, 293));
        insideOfElevatorNode.attachChild(railing2);

        //small railings connect the larger ones to the wall
        Spatial railing4 = drawWall("railingAids", 0.3f, 0.3f, 0.7f, TextureDistributor.TEXTURE_SILVER, true, 1, 1, false, 0, new Vector3f(0, 0, 0));
        for (int i = 0; i < 5; i++) {
            Spatial railing = railing4.clone();
            railing.setLocalTranslation(new Vector3f(462 + i * 10, -165, 292));
            insideOfElevatorNode.attachChild(railing);
        }
        for (int i = 0; i < 5; i++) {
            Spatial railing = railing4.clone();
            railing.setLocalTranslation(new Vector3f(462 + i * 10, -165, 326));
            insideOfElevatorNode.attachChild(railing);
        }
        insideOfElevatorNode.setLocalTranslation(new Vector3f(-200, 176, 100));
        return insideOfElevatorNode;
    }

    public Spatial drawWall(String name, float x, float y, float z, String tex, boolean repeat, int repx, int repy, boolean transparent, int deg, Vector3f rotationAxis) {
        Box wall = new Box(Vector3f.ZERO, x, y, z);
        Geometry wallGeo = new Geometry(name, wall);
        Texture wallTex = td.getAsset(tex);
        //mf.assignMass(wallGeo, 0, null);
        //Texture wallTex = this.am.loadTexture(tex);

        Quaternion rotationDeg = new Quaternion();
        rotationDeg.fromAngleAxis(deg * FastMath.DEG_TO_RAD, rotationAxis);

        if (repeat) {
            wallTex.setWrap(Texture.WrapMode.Repeat);
            Material wallMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
            wallMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
            wallMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
            wallMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
            wallMat.setColor("Specular", ColorRGBA.White.mult(0.65f));

            wallMat.setBoolean("UseMaterialColors", true);
            wallMat.setTexture("DiffuseMap", wallTex);


            if (transparent == true) {
                wallMat.setBoolean("UseAlpha", true);
                wallGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
            }

            wall.scaleTextureCoordinates(new Vector2f(repx, repy));
            wallGeo.setMaterial(wallMat);
            wallGeo.setLocalRotation(rotationDeg);
            return wallGeo;
        } else {
            Material wallMat = md.getAsset(MaterialDistributor.PATH_UNSHADED);
            wallGeo.setMaterial(wallMat);
            wallMat.setTexture("ColorMap", wallTex);
            if (transparent == true) {
                wallMat.setBoolean("UseAlpha", true);
                wallGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
            }
            return wallGeo;
        }
    }

    private Spatial drawWindow(Node input) {
        Box windowMesh = new Box(30, 2, 20f);
        Material mat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
        mat.setTexture("DiffuseMap", td.getAsset(TextureDistributor.TEXTURE_CEILING));
        mat.setBoolean("UseAlpha", true);

        Geometry windowGeo = new Geometry("elevatorSeeThroughRoof", windowMesh);

        windowGeo.setLocalTranslation(new Vector3f(482, 35, 310));
        mf.assignMass(windowGeo, 0, null);

        windowGeo.setMaterial(mat);

        input.attachChild(windowGeo);

        return input;

    }

    public ElevatorControl getElevatorControl() {

        return sc;
    }
}
