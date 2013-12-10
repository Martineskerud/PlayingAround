/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network.msg;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.Arrays;

/**
 *
 * @author anskaal
 */
@Serializable
public class PositionMessage extends AbstractMessage {

    public String subject;
    public float[] translation;
    public float[] rotation;

    public PositionMessage() {
    }
    
    public PositionMessage(Vector3f pos, Quaternion rot, String subject){
        System.out.println("creating pos msg : "+ subject);
        this.subject = subject;
        setRotPos(pos, rot);
    }
    
    public PositionMessage(Vector3f pos, Quaternion rot){
        this.subject = null;
        setRotPos(pos, rot);
    }

    public void setTranslation(Vector3f pos) {
        translation = new float[]{
            pos.x, pos.y, pos.z
        };
    }

    public Vector3f getTranslation() {
        if (translation != null && translation.length > 2) {
            return new Vector3f(translation[0], translation[1], translation[2]);
        }
        return null;
    }

    public void setRotation(Quaternion rot) {
        rotation = new float[]{
            rot.getX(), rot.getY(), rot.getZ(), rot.getW()
        };
    }

    public Quaternion getRotation() {
        if (rotation != null && rotation.length > 3) {
            return new Quaternion(rotation[0], rotation[1], rotation[2], rotation[3]);
        }
        return null;
    }

    private void setRotPos(Vector3f pos, Quaternion rot) {
        setTranslation(pos);
        setRotation(rot);
    }

    @Override
    public String toString() {
        return "POS: " + Arrays.toString(translation) + " ROT: " + Arrays.toString(rotation);
    }
}
