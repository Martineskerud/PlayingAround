/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import hiof.SkaalsveenEskerud.network.msg.PositionMessage;

/**
 *
 * @author root
 */
class Bot {

    private static int counter = 0;
    private final int id;
    private final ServerDataHandler dataHandler;
    private Vector3f pos;
    private Quaternion rot;

    public Bot(ServerDataHandler dataHandler) {
        this.dataHandler = dataHandler;
        id = counter++;

        pos = new Vector3f(0f, 50f, 0f);
        rot = Quaternion.ZERO;
    }

    public void reportPosition() {


        PositionMessage posMsg = new PositionMessage(pos, rot);
        dataHandler.handlePositionMessage(posMsg, getId());

    }

    public String getId() {
        return "Bot_" + id;
    }

    public void update() {

        float z = (float) (Math.random());
        z -= 0.5f;  // allow back and forward walking

        float x = (float) (Math.random());
        x -= 0.5f; // allow back and forward walking


        Vector3f randPosDelta = new Vector3f(x, 0f, z);

        pos = pos.add(randPosDelta);

        reportPosition();

    }
}
