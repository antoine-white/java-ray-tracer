package fr.ablanco.raytracing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import fr.ablanco.raytracing.Utils.Vec3f;

/**
 *
 * @author P. Meseure based on a Java Adaptation of a C code by B. Debouchages
 *         (M1, 2018-2019)
 */
public class JavaTga {
    /**
     * 
     * @param fout : output file stream
     * @param s    : short to write to disc in little endian
     */
    private static void writeShort(FileOutputStream fout, int n) throws IOException {
        fout.write(n & 255);
        fout.write((n >> 8) & 255);
    }

    /**
     * 
     * @param filename name of final TGA file
     * @param buffer   buffer that contains the image. 3 bytes per pixel ordered
     *                 this way : Blue, Green, Red
     * @param width    Width of the image
     * @param height   Height of the image
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private static void saveTGA(String filename, byte buffer[], int width, int height)
            throws IOException, UnsupportedEncodingException {

        FileOutputStream fout = new FileOutputStream(new File(filename));

        fout.write(0); // Comment size, no comment
        fout.write(0); // Colormap type: No colormap
        fout.write(2); // Image type
        writeShort(fout, 0); // Origin
        writeShort(fout, 0); // Length
        fout.write(0); // Depth
        writeShort(fout, 0); // X origin
        writeShort(fout, 0); // Y origin
        writeShort(fout, width); // Width of the image
        writeShort(fout, height); // Height of the image
        fout.write(24); // Pixel size in bits (24bpp)
        fout.write(0); // Descriptor

        /* Write the buffer */
        fout.write(buffer);

        fout.close();
    }

    /**
     * @param args no command line arguments
     */
    public static void main(String[] args) {
        var rayTracer = new RayTracer();
        final int w = 1080;
        final int h = 720;
        final float D = 1;
        byte buffer[] = new byte[3 * w * h];
        Vec3f P = new Vec3f(0, 0, 0);
        for (int row = 0; row < h; row++) { // for each row of the image
            for (int col = 0; col < w; col++) { // for each column of the image

                int index = 3 * ((row * w) + col); // compute index of color for pixel (x,y) in the buffer

                Color c = rayTracer.findColor(P,new Vec3f(
                    ((float)(col-w/2)/((float)h)),
                    ((float)(row-h/2)/((float)h)),
                    -D
                ));

                buffer[index] = (byte) (int)(c.b() * 255); // Blue in the left part of the image
                buffer[index + 1] = (byte) (int)(c.g() * 255); // Green in the middle
                buffer[index + 2] = (byte) (int)(c.r() * 255); // Red in the right part
            }
        }
        System.out.println("Ray casting finished!\nAtempting to save result!");
        try {
            String filename = "../res/res.tga";
            if(args.length > 0)
                filename = args[0];
            saveTGA(filename, buffer, w, h);
        } catch (Exception e) {
            System.err.println("TGA file not created :" + e);
        }
    }
}
