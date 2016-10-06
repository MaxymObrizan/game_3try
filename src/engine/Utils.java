/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;


/**
 *
 * @author Max
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Utils {
    
    static Random rand = new Random(getCurrentMachineTime());
    private static long  lastFrameTime;
    private static float delta;
    
    public static String loadResource(String fileName) throws Exception {
        String result = "";
        try (InputStream in = Utils.class.getClass().getResourceAsStream(fileName)) {
            result = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
        }
        return result;
    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Utils.class.getClass().getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    public static int[] listIntToArray(List<Integer> list) {
        int[] result = list.stream().mapToInt((Integer v) -> v).toArray();
        return result;
    }

    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    public static boolean existsResourceFile(String fileName) {
        boolean result;
        try (InputStream is = Utils.class.getResourceAsStream(fileName)) {
            result = is != null;
        } catch (Exception excp) {
            result = false;
        }
        return result;
    }
    
    /**
     * return current time in millis
     * @return long  
     */
    public static long getCurrentMachineTime()
    {
        return System.currentTimeMillis();
    }
    
    /**
     * 
     * @return float  
     */
    public static float getNextFloat()
    {
	return rand.nextFloat();
    }
	
    public static int getNextInt()
    {
        return rand.nextInt();
    }
	
    public static float getPseudoGaussianRand()
    {
        int iter = 3;
        float sum = 0;
        for(int i =0; i<iter; i++)
        {
            sum += rand.nextFloat();
        }
        sum /= iter;
        return sum*2;
    }
}