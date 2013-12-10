/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
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
import hiof.SkaalsveenEskerud.Main.MainContainer;
import hiof.SkaalsveenEskerud.MainComponent;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.model.Bench;
import hiof.SkaalsveenEskerud.world.model.Billboard;
import hiof.SkaalsveenEskerud.world.model.BoxingRing;
import hiof.SkaalsveenEskerud.world.model.Crate;
import hiof.SkaalsveenEskerud.world.model.OilDrum;
import hiof.SkaalsveenEskerud.world.model.Shack;
import hiof.SkaalsveenEskerud.world.model.TrashCan;
import hiof.SkaalsveenEskerud.world.model.component.ArrayListNode;
import java.util.ArrayList;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.SpawnCargo;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;
import java.util.Random;

/**
 *
 * @author anskaal
 */
public class World extends MainComponent {

    public MaterialDistributor md;
    public TextureDistributor textureDistributor;
    private ArrayList<Spatial> worldSpatialParts = null;
    public Node worldNode = new Node("world");
    private MainContainer mc;
    //private final Camera cam;
    private WorldData worldData;
    public Node characters;
    private MassFactory massFactory;
    public Elevator elevator;

    public World(MainContainer mainContainer) {
        super(mainContainer);
        this.mc = mainContainer;
        this.worldSpatialParts = new ArrayList<Spatial>();
        this.md = new MaterialDistributor(assetManager);
        this.textureDistributor = new TextureDistributor(assetManager);
        this.massFactory = new MassFactory(bas);
        worldData = new WorldData();

        this.characters = new Node("characters");
        attach(characters);
    }

    public void init() {

        Light light = new Light(worldNode);
        light.makeSpotLight(worldNode);

        // Create Skybox
        Skybox.create(rootNode, assetManager, Skybox.BRIGHT_SKY);

        // Create stadium and attach spatials
        BoxingRing boxingRing = new BoxingRing(assetManager, md);
        ArrayListNode boxingRingNode = new ArrayListNode("boxingRingNode");
        boxingRingNode.attach(boxingRing.createBoxingRingSpatials());


        //  worldNode.attachChild(boxingRingNode);

        // Create shack and attach spatials
        Shack shack = new Shack(assetManager, md, textureDistributor, mc);
        ArrayListNode shackNode = new ArrayListNode("shackNode");
        shackNode.attach(shack.createShackSpatials());

        worldNode.attachChild(shackNode);

        //Create elevator and attach spatials

        elevator = new Elevator(mc);
        ArrayListNode elevatorNode = new ArrayListNode("elevatorNode");
        elevatorNode.attach(elevator.createElevatorSpatials());
        massFactory.assignMass(elevatorNode.getChildren().get(0), 0, Vector3f.ZERO);
        massFactory.assignMass(elevatorNode.getChildren().get(2), 0, Vector3f.ZERO);
        worldNode.attachChild(elevatorNode);

        Random rand = new Random();
        //yarr here be the treasure chests...
        SpawnCargo spawnCargo = new SpawnCargo(assetManager, massFactory, textureDistributor, rand);

        worldNode.attachChild(spawnCargo);

        //billboards for ads
        Billboard billboardHiof = new Billboard(assetManager, massFactory, textureDistributor, new Vector3f(275, 0, 475), 4, "hiof");
        Billboard billboardEnigma = new Billboard(assetManager, massFactory, textureDistributor, new Vector3f(650, 0, 100), 1, "enigma");

        worldNode.attachChild(billboardHiof);
        worldNode.attachChild(billboardEnigma);

        //a crate
        Crate crateByBillboardEnigma = new Crate(6.5f, 6.5f, 6.5f, assetManager, massFactory, textureDistributor, new Vector3f(-95, 3, 115));
        worldNode.attachChild(crateByBillboardEnigma);

        //benches!
        Bench bench = new Bench(assetManager, massFactory, textureDistributor, md, new Vector3f(-100, 0, 420));
        worldNode.attachChild(bench);
        Bench bench2 = new Bench(assetManager, massFactory, textureDistributor, md, new Vector3f(-150, 0, 420));
        worldNode.attachChild(bench2);

        //oildrums with fire!
        for (int i = 0; i < 3; i++) {
            OilDrum oilDrum = new OilDrum(assetManager, new Vector3f(400, 1.5f, -50 * i ), textureDistributor, md, massFactory, true);
            worldNode.attachChild(oilDrum);
        }

        TrashCan trashCan = new TrashCan(assetManager, massFactory, textureDistributor, md, new Vector3f(420, 1, 75));
        worldNode.attachChild(trashCan);

        InvisibleWall invisiWall = new InvisibleWall(assetManager, massFactory, md);
        worldNode.attachChild(invisiWall);
        

        for (Spatial s : createWorldSpatials()) {
            worldNode.attachChild(s);
        }

        rootNode.attachChild(worldNode);

    }

    public void update(float tpf) {
    }

    public ArrayList<Spatial> createWorldSpatials() {

        worldSpatialParts.add(createRoom());
        worldSpatialParts.add(drawCorner());
        return worldSpatialParts;
    }

    private Spatial createRoom() {
        Node floor = new Node("floor");
        //creating the top level floor.
        Spatial floorGeo = drawFloor(300, 0.1f, 300f, TextureDistributor.TEXTURE_CONCRETE, 4, 4);
        floorGeo.setLocalTranslation(new Vector3f(150, -4, 150));
        floor.attachChild(floorGeo);

        Spatial floorGeo2 = drawFloor(170, 0.1f, 300, TextureDistributor.TEXTURE_CONCRETE, 4, 4);
        floorGeo2.setLocalTranslation(new Vector3f(680, -4, 150));
        floor.attachChild(floorGeo2);

        Spatial floorGeo3 = drawFloor(30, 0.1f, 220, TextureDistributor.TEXTURE_CONCRETE, 3, 1);
        floorGeo3.setLocalTranslation(new Vector3f(480, -4, 70));
        floor.attachChild(floorGeo3);

        Spatial floorGeo4 = drawFloor(30, 0.1f, 60, TextureDistributor.TEXTURE_CONCRETE, 1, 1);
        floorGeo4.setLocalTranslation(new Vector3f(480, -4, 390));
        floor.attachChild(floorGeo4);
        massFactory.assignMass(floor, 0, null);
        return floor;
    }

    private Spatial drawCorner() {

        Spatial stair1 = drawFloor(100, 0.1f, 30, TextureDistributor.TEXTURE_CONCRETE, 30, new Vector3f(0f, 0f, 1f));
        stair1.setLocalTranslation(-235, -52.5f, 420);
        massFactory.assignMass(stair1, 0, null);
        worldNode.attachChild(stair1);

        Spatial stair2 = drawFloor(100, 0.1f, 30, TextureDistributor.TEXTURE_CONCRETE, -30, new Vector3f(0f, 0f, 1f));
        stair2.setLocalTranslation(-230, -152.5f, 480);
        massFactory.assignMass(stair2, 0, null);
        worldNode.attachChild(stair2);

        Spatial stair3 = drawFloor(100, 0.1f, 30, TextureDistributor.TEXTURE_CONCRETE, 90, new Vector3f(0f, 1f, 0f));
        stair3.setLocalTranslation(-345, -102.5f, 480);
        massFactory.assignMass(stair3, 0, null);
        worldNode.attachChild(stair3);

        //THIS IS THE LOW LEVEL FLOOR

        Spatial undergroundFloor = drawFloor(500, 0.1f, 300, TextureDistributor.TEXTURE_CONCRETE, 4, 4);
        undergroundFloor.setLocalTranslation(150, -180f, 200);
        worldNode.attachChild(undergroundFloor);
        massFactory.assignMass(undergroundFloor, 0, null);





        return worldNode;

    }

    public Spatial drawBoard() {
        Node boardNode = new Node("boardNode");

        Spatial board = drawFloor(500, 1f, 300, TextureDistributor.TEXTURE_CONCRETE, 4, 4);

        return boardNode;

    }

    private Spatial drawFloor(float x, float y, float z, String tex, int deg, Vector3f rotationAxis) {
        Box wall = new Box(Vector3f.ZERO, x, y, z);
        Geometry wallGeo = new Geometry("Box", wall);
        Texture wallTex = this.assetManager.loadTexture(tex);

        Quaternion rotationDeg = new Quaternion();
        rotationDeg.fromAngleAxis(deg * FastMath.DEG_TO_RAD, rotationAxis);

        wallTex.setWrap(Texture.WrapMode.Repeat);
        Material wallMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);

        setWhiteColorIntensity(wallMat, 0.3f, 0.6f, 0.65f);

        wallMat.setBoolean("UseMaterialColors", true);
        wallMat.setTexture("DiffuseMap", wallTex);

        wall.scaleTextureCoordinates(new Vector2f(4, 4));
        wallGeo.setMaterial(wallMat);
        wallGeo.setLocalRotation(rotationDeg);
        wallGeo.setLocalTranslation(350f, -4f, 150f);
        return wallGeo;

    }

    private Spatial drawFloor(float x, float y, float z, String tex, int repx, int repy) {


        Box wall = new Box(Vector3f.ZERO, x, y, z);
        Geometry wallGeo = new Geometry("Box", wall);

        Texture wallTex = this.assetManager.loadTexture(tex);

        wallTex.setWrap(Texture.WrapMode.Repeat);
        Material wallMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
        setWhiteColorIntensity(wallMat, 0.3f, 0.6f, 0.65f);

        wallMat.setBoolean("UseMaterialColors", true);
        wallMat.setTexture("DiffuseMap", wallTex);

        wall.scaleTextureCoordinates(new Vector2f(repx, repy));
        wallGeo.setMaterial(wallMat);
        wallGeo.setLocalTranslation(350f, -4f, 150f);
        return wallGeo;

    }

    private void setWhiteColorIntensity(Material wallMat, float ambient, float diffuse, float specular) {

        wallMat.setColor("Ambient", ColorRGBA.White.mult(ambient));
        wallMat.setColor("Diffuse", ColorRGBA.White.mult(diffuse));
        wallMat.setColor("Specular", ColorRGBA.White.mult(specular));

    }

    public Spatial drawWall(String name, float x, float y, float z, String tex, boolean repeat, int repx, int repy, boolean transparent, int deg, Vector3f rotationAxis) {
        Box wall = new Box(Vector3f.ZERO, x, y, z);
        Geometry wallGeo = new Geometry(name, wall);

        Texture wallTex = this.assetManager.loadTexture(tex);

        Quaternion rotationDeg = new Quaternion();
        rotationDeg.fromAngleAxis(deg * FastMath.DEG_TO_RAD, rotationAxis);

        if (repeat) {
            wallTex.setWrap(Texture.WrapMode.Repeat);
            Material wallMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
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

    public Node getWorldNode() {
        return worldNode;
    }

    public void attach(Node node) {
        worldNode.attachChild(node);
    }
}
