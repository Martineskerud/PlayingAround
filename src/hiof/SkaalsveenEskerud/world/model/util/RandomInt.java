/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model.util;

import java.util.Random;

/**
 *
 * @author Martin
 */
public class RandomInt {

    public static int rangedRandomInt(int min, int max, Random rand) {

        int num = rand.nextInt((max - min) + 1) + min;

        return num;
    }
}
