package model;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.microsoft.graph.models.Image;

public class Utils {
	
	public static void chooseDay(String date, int[] array) {
		switch (date) {
		case "MONDAY":
			array[0] = array[0] + 1;
			break;
		case "TUESDAY":
			array[1] = array[1] + 1;
			break;
		case "WEDNESDAY":
			array[2] = array[2] + 1;
			break;
		case "THURSDAY":
			array[3] = array[3] + 1;
			break;
		case "FRIDAY":
			array[4] = array[4] + 1;
			break;
		case "SATURDAY":
			array[5] = array[5] + 1;
			break;
		case "SUNDAY":
			array[6] = array[6] + 1;
			break;
		default:
			throw new NullPointerException("Invalid day");
		}
		
	}
	
	/*
	 * Decoding and encoding Images (https://javapointers.com/java/java-core/java-convert-image-to-base64-string-and-base64-to-image/)
	 */
	
	public static BufferedImage decodeToImage(String imageString) {
		 
        BufferedImage image = null;
        byte[] imageByte;
        try {
            imageByte = Base64.getDecoder().decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
	
	public static int[] mergeArrays(int[] arr1, int[] arr2) {
		//assume arr1 and arr2 have the same length
		int[] result = new int[arr1.length];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = arr1[i] + arr2[i];
		}
		
		return result;
	}
	
}
