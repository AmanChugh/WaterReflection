package com.ammy.waterreflection.haoran.ImageFilter;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import java.nio.IntBuffer;

/**
 *
 */
public class Image {
    //original bitmap image
    public Bitmap image;
    public Bitmap destImage;
    
    //format of image (jpg/png)
    private String formatName;
    //dimensions of image
    private int width, height;
    // RGB Array Color
    protected int[] colorArray;
    
    public Image(Bitmap img){
        this.image =  img;
        formatName = "jpg";
        width = img.getWidth();
        height = img.getHeight();
        destImage = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        updateColorArray();
    }
    
    public Image clone(){
    	return new Image(this.image);
    }
    
    /**
     * Set color array for image - called on initialisation
     * by constructor
     * @param
     */
    private void updateColorArray(){
        colorArray = new int[width * height];
        image.getPixels(colorArray, 0, width, 0, 0, width, height);
        int r, g, b;
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                int index = y * width + x;
                r = (colorArray[index] >> 16) & 0xff;
                g = (colorArray[index] >> 8) & 0xff;
                b = colorArray[index] & 0xff;
                colorArray[index] = 0xff000000 | (b << 16) | (g << 8) | r;
            }
        }               
    }

    /**
     * Set the color of a specified pixel from an RGB combo
     */
    public void setPixelColor(int x, int y, int c0, int c1, int c2){
	    int rgbcolor = (255 << 24) + (c0 << 16) + (c1 << 8) + c2;
        colorArray[((y*image.getWidth()+x))] = rgbcolor;
    }
    
    public void copyPixelsFromBuffer() {
    	IntBuffer vbb = IntBuffer.wrap(colorArray);
        destImage.copyPixelsFromBuffer(vbb);
        vbb.clear();
    }
    
    /**
     * Method to get the RED color for the specified 
     * pixel
     * @return color of R
     */
    public int getRComponent(int x, int y){
         return (getColorArray()[((y*width+x))]& 0x00FF0000) >>> 16;
    }

    /**
     * Method to get the GREEN color for the specified 
     * pixel
     * @return color of G
     */
    public int getGComponent(int x, int y){
         return (getColorArray()[((y*width+x))]& 0x0000FF00) >>> 8;
    }

    /**
     * Method to get the BLUE color for the specified 
     * pixel
     * @return color of B
     */
    public int getBComponent(int x, int y){
         return (getColorArray()[((y*width+x))] & 0x000000FF);
    }

    /**
     * @return the image
     */
    public Bitmap getImage() {
    	return destImage;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the colorArray
     */
    public int[] getColorArray() {
        return colorArray;
    }

}
