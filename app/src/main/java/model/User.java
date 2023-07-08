package model;

import javax.imageio.ImageIO; 
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class User {
    String username;
    String email;
    String id;
    String jobTitle;
    String displayName;
    BufferedImage picture;
    int[] readmail, notreadmail, pickedup, notpickedup;
    
    public User(String username, String email, String id, String jobTitle, String displayName, BufferedImage picture, 
    		int[] readmail, int[] notreadmail, int[] pickedup, int[] notpickedup) {
        this.username = username;
        this.email = email;
        this.id = id;
        this.jobTitle = jobTitle;
        this.displayName = displayName;
        this.picture = picture;
        this.readmail = readmail;
        this.notreadmail = notreadmail;
        this.pickedup = pickedup;
        this.notpickedup = notpickedup;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
    
    public String getJobTitle() {
    	return jobTitle;
    }
    
    public String getDisplayName() {
    	return displayName;
    }

    public int[] getRead() {
        return readmail;
    }

    public int[] getNotRead() {
        return notreadmail;
    }

    public int[] getPickedUp() {
        return pickedup;
    }

    public int[] getMissedCalls() {
        return notpickedup;
    }

    public ImageIcon getIcon() {
    	// if no picture from API is found, return a default image
    	if (picture == null) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(getClass().getResource("/user.png"));
                picture = image;
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}
    	Image icon = picture.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
    	return new ImageIcon(icon);
    }


}
