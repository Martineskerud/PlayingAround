/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.FaceCullMode;
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
import hiof.SkaalsveenEskerud.physics.MassFactory;
import java.util.ArrayList;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;

/**
 *
 * @author martin
 */
public class Shack {

    private final AssetManager am;
    private ArrayList<Spatial> shackSpatialParts = null;
    private MaterialDistributor md;
    private TextureDistributor td;
    private MassFactory mf;

    public Shack(AssetManager am, MaterialDistributor md, TextureDistributor td, Main.MainContainer mc) {
        this.am = am;
        this.shackSpatialParts = new ArrayList<Spatial>();
        this.md = md;
        this.td = td;
        this.mf = mc.massFactory;
    }

    public ArrayList<Spatial> createShackSpatials() {
        shackSpatialParts.add(drawFence());
        shackSpatialParts.add(drawShack());


        return shackSpatialParts;
    }

    private Spatial drawFence() {

        Node fenceNode = new Node("fence");

        //Back wall
        Spatial backWall = drawWall("backWall", 500f, 20f, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 10, 1, true);
        backWall.setLocalTranslation(350, 10, -150);

        //mf.assignMass(backWall, 0.1f);
        fenceNode.attachChild(backWall);


        //Front wall
        Spatial frontWall = drawWall("frontWall", 500f, 20f, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 10, 1, false);
        frontWall.setLocalTranslation(350, 10, 450);
        fenceNode.attachChild(frontWall);



        //Left wall
        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0f, 1f, 0f));
        Spatial leftwall = drawWall("leftwall", 270f, 20f, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 10, 1, false);
        leftwall.setLocalRotation(deg90);
        leftwall.setLocalTranslation(-150, 10, 120);
        fenceNode.attachChild(leftwall);

        //Right wall
        Spatial rightWall = drawWall("rightWall", 300f, 20f, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 10, 1, false, 90, new Vector3f(0f, 1f, 0f));
        rightWall.setLocalTranslation(850, 10, 150);
        fenceNode.attachChild(rightWall);


        //Middle wall 1
        Spatial middleWall = drawWall("middleWall", 200, 20f, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 10, 1, false, 90, new Vector3f(0f, 1f, 0f));
        middleWall.setLocalTranslation(400, 10, 250);
        fenceNode.attachChild(middleWall);


        //Underground pathway corner
        //These walls are too big and could be made smaller to reduce the amount of textures and cpu-load.

        Spatial cornerBack1 = drawWall("cornerBack1", 100, 200f, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 4, 6, false);
        cornerBack1.setLocalTranslation(-251, -170, 389);
        fenceNode.attachChild(cornerBack1);

        Spatial cornerLeft1 = drawWall("cornerLeft1", 100, 200f, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 4, 6, false, 90, new Vector3f(0f, 1f, 0f));
        cornerLeft1.setLocalTranslation(-350, -170, 440);
        fenceNode.attachChild(cornerLeft1);

        //tunnel wall front
        Spatial tunnelFront = drawWall("tunnelFront", 100, 200, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 2, 6, false);
        tunnelFront.setLocalTranslation(-250, -170, 500);
        fenceNode.attachChild(tunnelFront);

        //tunnel-lid
        Spatial tunnelLid = drawWall("tunnelLid", 30, 16, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 1, 1, false, 90, new Vector3f(0f, 1f, 0f));
        tunnelLid.setLocalTranslation(-150, 13, 480);
        fenceNode.attachChild(tunnelLid);

        Spatial tunnelLid2 = drawWall("tunnelLid2", 60, 62, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 2, 2, false, 90, new Vector3f(0f, 1f, 0f));
        tunnelLid2.setLocalTranslation(-150, -65, 450);
        fenceNode.attachChild(tunnelLid2);

        //tunnel wall underground
        Spatial tunnelWallFront = drawWall("tunnelWallFront", 125, 43, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 2, 2, false, 45, new Vector3f(0f, 1f, 0f));
        tunnelWallFront.setLocalTranslation(-65, -137, 300);
        fenceNode.attachChild(tunnelWallFront);

        Spatial tunnelWallBack = drawWall("tunnelWallBack", 150, 43, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 2, 2, false, 45, new Vector3f(0f, 1f, 0f));
        tunnelWallBack.setLocalTranslation(-50, -137, 400);
        fenceNode.attachChild(tunnelWallBack);



        //tunnel wall underground - the straight stretch
        Spatial tunnelWallFront2 = drawWall("tunnelWallFront2", 150, 43, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 3, 3, false);
        tunnelWallFront2.setLocalTranslation(205, -137, 293);
        fenceNode.attachChild(tunnelWallFront2);

        Spatial tunnelWallBack2 = drawWall("tunnelWallBack2", 250, 43, 1f, TextureDistributor.TEXTURE_BRICKWALL, true, 5, 3, false);
        tunnelWallBack2.setLocalTranslation(220, -137, 250);
        fenceNode.attachChild(tunnelWallBack2);
        Geometry crateGeo = createBox(4, 4, 4, 0, null);
        crateGeo.setLocalTranslation(70f, -175, 257);
        Quaternion crateRotationDeg = new Quaternion();
        crateRotationDeg.fromAngleAxis(90 * FastMath.DEG_TO_RAD, new Vector3f(1f, 0f, 0f));
        crateGeo.setLocalRotation(crateRotationDeg);
        fenceNode.attachChild(crateGeo);
        Geometry crateGeo2 = createBox(3, 5, 3, 45, new Vector3f(0, 1f, 0f));
        crateGeo2.setLocalTranslation(70f, -166, 257);
        fenceNode.attachChild(crateGeo2);
        fenceNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);


        //the roof in the underground tunnel
        Spatial tunnelRoof = drawWall("tunnelRoof", 150, 1, 300, TextureDistributor.TEXTURE_CEILING, true, 20, 8, false);
        tunnelRoof.setLocalRotation(deg90);
        tunnelRoof.setLocalTranslation(150, -128, 353);
        fenceNode.attachChild(tunnelRoof);

        //small corner in front of elevator in tunnel
        Spatial tunnelCorner = drawWall("tunnelCorner", 20, 43, 1, TextureDistributor.TEXTURE_BRICKWALL, true, 2, 3, false);
        tunnelCorner.setLocalRotation(deg90);
        tunnelCorner.setLocalTranslation(356, -137, 312);
        fenceNode.attachChild(tunnelCorner);

        //small corner in front of elevator in tunnel
        Spatial tunnelCorner2 = drawWall("tunnelCorner2", 60, 43, 1, TextureDistributor.TEXTURE_BRICKWALL, true, 2, 3, false);
        tunnelCorner2.setLocalTranslation(406, -137, 332);
        fenceNode.attachChild(tunnelCorner2);

        //wall beside the elevator doors
        Spatial tunnelByElevator = drawWall("tunnelByElevator", 30, 43, 1, TextureDistributor.TEXTURE_BRICKWALL, true, 2, 3, false);
        tunnelByElevator.setLocalTranslation(450, -137, 260);
        tunnelByElevator.setLocalRotation(deg90);
        fenceNode.attachChild(tunnelByElevator);
        mf.assignMass(fenceNode, 0, Vector3f.ZERO);
        return fenceNode;
    }

    private Spatial drawShack() {


        Node shackNode = new Node("shack");

        //Back wall
        Spatial shackBack = drawWall("shackBack", 45, 30f, 1f, TextureDistributor.TEXTURE_TINROOF, true, 2, 1, false);
        shackBack.setLocalTranslation(-80, 26, -149f);
        mf.assignMass(shackBack, 0, Vector3f.ZERO);
        shackNode.attachChild(shackBack);




        //Front wall
        Spatial shackFront = drawWall("shackFront", 45, 30f, 1f, TextureDistributor.TEXTURE_TINROOF, true, 2, 1, false);
        shackFront.setLocalTranslation(-80, 26, -90);
        mf.assignMass(shackFront, 0, Vector3f.ZERO);
        shackNode.attachChild(shackFront);



        //Left wall
        Spatial shackLeft = drawWall("shackLeft", 20, 30f, 1f, TextureDistributor.TEXTURE_TINROOF, true, 2, 1, false);
        shackLeft.setLocalTranslation(-125, 26, -130);

        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0f, 1f, 0f));
        shackLeft.setLocalRotation(deg90);
        mf.assignMass(shackLeft, 0, Vector3f.ZERO);
        shackNode.attachChild(shackLeft);
        Spatial shackLeft2 = drawWall("shackLeft2", 10, 10f, 1f, TextureDistributor.TEXTURE_TINROOF, true, 2, 1, false);
        shackLeft2.setLocalTranslation(-125, 46, -100);
        shackLeft2.setLocalRotation(deg90);
        mf.assignMass(shackLeft2, 0, Vector3f.ZERO);
        shackNode.attachChild(shackLeft2);



        //Right wall
        Spatial shackRight = drawWall("shackRight", 30, 10f, 1f, TextureDistributor.TEXTURE_TINROOF, true, 2, 1, false);
        shackRight.setLocalTranslation(-35, 6, -120);
        shackRight.setLocalRotation(deg90);
        mf.assignMass(shackRight, 0, Vector3f.ZERO);
        shackNode.attachChild(shackRight);


        Spatial shackRight2 = drawWall("shackRight2", 15, 20f, 1f, TextureDistributor.TEXTURE_TINROOF, true, 1, 1, false);
        shackRight2.setLocalTranslation(-35, 36, -135);
        shackRight2.setLocalRotation(deg90);
        mf.assignMass(shackRight2, 0, Vector3f.ZERO);
        shackNode.attachChild(shackRight2);

        Spatial shackRight3 = drawWall("shackRight3", 15, 10f, 1f, TextureDistributor.TEXTURE_TINROOF, true, 2, 1, false);
        shackRight3.setLocalTranslation(-35, 46, -105);
        shackRight3.setLocalRotation(deg90);
        mf.assignMass(shackRight3, 0, Vector3f.ZERO);
        shackNode.attachChild(shackRight3);

        shackNode.attachChild(drawWindow(shackNode));
        //bench

        Spatial shackBench = drawWall("shackBench", 45, 0.1f, 10f, TextureDistributor.TEXTURE_GREYWOOD, true, 1, 1, false, 0, new Vector3f(0f, 1f, 0f));
        shackBench.setLocalTranslation(-80, 6, -140);
        mf.assignMass(shackBench, 0, Vector3f.ZERO);
        shackNode.attachChild(shackBench);

        Spatial shackBench2 = drawWall("shackBench2", 45, 0.1f, 5f, TextureDistributor.TEXTURE_GREYWOOD, true, 1, 1, false, 90, new Vector3f(1f, 0f, 0f));
        shackBench2.setLocalTranslation(-80, 1, -130);
        mf.assignMass(shackBench2, 0, Vector3f.ZERO);
        shackNode.attachChild(shackBench2);


        //roof

        Spatial roof = drawWall("roof", 60, 0.1f, 40, TextureDistributor.TEXTURE_GREYWOOD, true, 1, 1, false);
        roof.setLocalTranslation(-80, 57, -110);
        mf.assignMass(roof, 0, Vector3f.ZERO);
        shackNode.attachChild(roof);

        shackNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        return shackNode;
    }

    public Spatial drawWall(String name, float x, float y, float z, String tex, boolean repeat, int repx, int repy, boolean culling) {
        Box wall = new Box(Vector3f.ZERO, x, y, z);
        Geometry wallGeo = new Geometry(name, wall);
        Texture wallTex = this.am.loadTexture(tex);



        if (repeat) {
            wallTex.setWrap(Texture.WrapMode.Repeat);
            Material wallMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
            wallMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
            wallMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
            wallMat.setColor("Specular", ColorRGBA.White.mult(0.65f));

            wallMat.setBoolean("UseMaterialColors", true);
            wallMat.setTexture("DiffuseMap", wallTex);

            if (culling == true) {
                wallMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);

            }


            wall.scaleTextureCoordinates(new Vector2f(repx, repy));
            wallGeo.setMaterial(wallMat);
            return wallGeo;
        } else {
            Material wallMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
            wallGeo.setMaterial(wallMat);
            wallMat.setTexture("ColorMap", wallTex);
            if (culling == true) {
                wallMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
            }

            return wallGeo;
        }
    }

    public Spatial drawWall(String name, float x, float y, float z, String tex, boolean repeat, int repx, int repy, boolean transparent, int deg, Vector3f rotationAxis) {
        Box wall = new Box(Vector3f.ZERO, x, y, z);
        Geometry wallGeo = new Geometry(name, wall);
        Texture wallTex = this.am.loadTexture(tex);

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

    private Spatial drawWindow(Node input) {
        Box windowMesh = new Box(15, 10, 1f);
        Material mat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
        // mat.setTexture("DiffuseMap", null);
        mat.setBoolean("UseAlpha", true);
        mat.setFloat("Shininess", 120);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
        mat.setColor("Diffuse", ColorRGBA.White.mult(0.35f));
        mat.setColor("Specular", ColorRGBA.White.mult(0.6f));
        Geometry windowGeo = new Geometry("Window", windowMesh);
        windowGeo.setMaterial(mat);

        windowGeo.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Additive);
        windowGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
        windowGeo.setLocalTranslation(new Vector3f(-35, 26, -105));
        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0f, 1f, 0f));
        windowGeo.setLocalRotation(deg90);
        //specifically assigning to geometry to remove the window.
        mf.assignMass(windowGeo, 0, Vector3f.ZERO);
        input.attachChild(windowGeo);

        return input;

    }

    private Geometry createBox(int x, int y, int z, int deg, Vector3f rotationAxis) {
        //Box for cover/ to set that cargo moode  ~~

        //some type of input validation
        if (rotationAxis == null) {
            rotationAxis = new Vector3f(0, 0, 0);
        }
        Quaternion rotationDeg = new Quaternion();
        rotationDeg.fromAngleAxis(deg * FastMath.DEG_TO_RAD, rotationAxis);
        Box crate = new Box(Vector3f.ZERO, x, y, z);
        Geometry crateGeo = new Geometry("Box", crate);
        Material crateMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
        Texture crateTex = td.getAsset(TextureDistributor.TEXTURE_GREYWOOD);
        crateMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
        crateMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
        crateMat.setColor("Specular", ColorRGBA.White.mult(0.65f));
        crateMat.setBoolean("UseMaterialColors", true);
        crateMat.setTexture("DiffuseMap", crateTex);
        crateGeo.setMaterial(crateMat);
        crateGeo.setLocalRotation(rotationDeg);
        return crateGeo;
    }
}
