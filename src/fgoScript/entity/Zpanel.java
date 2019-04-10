package fgoScript.entity;

import java.awt.Graphics;  
import java.awt.Image;  
  
import javax.swing.Icon;  
import javax.swing.ImageIcon;  
import javax.swing.JPanel;  

public class Zpanel extends JPanel{
	 private static final long serialVersionUID = 6702278957072713279L;  
	  
	    public Zpanel() {  
	    }  
	  
	    private Icon wallpaper;  
	    @Override
		protected void paintComponent(Graphics g) {
	        if (null != wallpaper) {  
	            processBackground(g);  
	        }  
	    }  
	  
	    public void setBackground(Icon wallpaper) {  
	        this.wallpaper = wallpaper;  
	        this.repaint();  
	    }  
	  
	    private void processBackground(Graphics g) {  
	        ImageIcon icon = (ImageIcon) wallpaper;  
	        Image image = icon.getImage();  
	        int cw = getWidth();  
	        int ch = getHeight();  
	        int iw = image.getWidth(this);  
	        int ih = image.getHeight(this);  
	        int x = 0;  
	        int y = 0;  
	        while (y <= ch) {  
	            g.drawImage(image, x, y, this);  
	            x += iw;  
	            if (x >= cw) {  
	                x = 0;  
	                y += ih;  
	            }  
	        }  
	    }  
}
