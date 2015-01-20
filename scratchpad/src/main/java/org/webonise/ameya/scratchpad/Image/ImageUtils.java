package org.webonise.ameya.scratchpad.image;

import java.io.IOException;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageUtils {

    /**
     * Decode string to image
     * @param imageString The string to decode
     * @return decoded image
     */
    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Encode image to string
     * @param image The image to encode
     * @param type jpeg, bmp, ...
     * @return encoded string
     */
    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public static void main (String args[]) throws IOException {
        /* Test image to string and string to image start */
        File file = new File("/Users/Webonise/2.png");
        BufferedImage newImg;
       // String imgstr;
        //imgstr = encodeToString(img, "png");
       // System.out.println(imgstr);
        String base64Temp = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAXoAAAEHCAYAAACgHI2PAAAIzUlEQVR4Xu3UAQ0AIAwDQfCLUrTgARJkfG4Odm061z53OAIECBDICkxDn83WYwQIEPgChl4RCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFDH08YO8RIEDA0OsAAQIE4gKGPh6w9wgQIGDodYAAAQJxAUMfD9h7BAgQMPQ6QIAAgbiAoY8H7D0CBAgYeh0gQIBAXMDQxwP2HgECBAy9DhAgQCAuYOjjAXuPAAEChl4HCBAgEBcw9PGAvUeAAAFDrwMECBCICxj6eMDeI0CAgKHXAQIECMQFHkH6OBxUv0sUAAAAAElFTkSuQmCC";
        newImg = decodeToImage(sanitiseBase64(base64Temp));
        ImageIO.write(newImg, "png", file);
        /* Test image to string and string to image finish */
    }

    private  static String sanitiseBase64(String baseString){
        return baseString.replaceFirst("^data:image/[^;]*;base64,?","");
    }
}