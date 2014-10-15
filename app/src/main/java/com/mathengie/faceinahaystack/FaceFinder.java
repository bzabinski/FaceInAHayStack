package com.mathengie.faceinahaystack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.Log;

import org.ejml.simple.SimpleMatrix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Era on 9/23/2014.
 */
public class FaceFinder {

    List<double[]> pixelsList = new ArrayList<double[]>();
    SimpleMatrix totalMatrix;
    SimpleMatrix testMatrix;
    List<Integer> labels = new ArrayList<Integer>();

    public FaceFinder(File imagesDir)
    {
        File photos[] = imagesDir.listFiles();

        int labelID = 0;
        for(File photo: photos)
        {
            Bitmap photoBM = BitmapFactory.decodeFile(photo.getAbsolutePath());
            FaceDetector fr = new FaceDetector(photoBM.getWidth(),photoBM.getHeight(), 10);
            Face[] foundFaces = new Face[10];
            fr.findFaces(photoBM, foundFaces);

            int counter = 0;
            while(foundFaces[counter] != null)
            {
                PointF midPoint = new PointF();
                foundFaces[counter].getMidPoint(midPoint);
                float eyeDist = foundFaces[counter].eyesDistance();
                Bitmap lilFace = Bitmap.createBitmap(photoBM, (int) (midPoint.x - 31), (int) (midPoint.y - 32), 64, 64);
                Bitmap tempFace = lilFace.copy(Bitmap.Config.ARGB_8888, true);
                int[] pixels = new int[tempFace.getHeight() * tempFace.getWidth()];
                tempFace.getPixels(pixels, 0, tempFace.getWidth(), 0, 0, tempFace.getWidth(), tempFace.getHeight());

                double[] pixelsD = new double[pixels.length];

                for (int i = 0; i < pixels.length; i++)
                {
                    int red = Color.red(pixels[i]);
                    int green = Color.green(pixels[i]);
                    int blue = Color.blue(pixels[i]);
                    double gray = (red * 0.3 + green * 0.59 + blue * 0.11);

                    pixelsD[i] = gray;
                }

                pixelsList.add(pixelsD);
                labels.add(labelID);
                counter++;

            }

            labelID++;
        }

        totalMatrix = new SimpleMatrix(pixelsList.toArray(new double[pixelsList.size()][64 * 64]));
    }

    public void find(Bitmap face)
    {
        int[] pixels = new int[face.getHeight() * face.getWidth()];
        face.getPixels(pixels, 0, face.getWidth(), 0, 0, face.getWidth(), face.getHeight());

        double[] pixelsD = new double[pixels.length];

        for (int i = 0; i < pixels.length; i++)
        {
            int red = Color.red(pixels[i]);
            int green = Color.green(pixels[i]);
            int blue = Color.blue(pixels[i]);
            double gray = (red * 0.3 + green * 0.59 + blue * 0.11);

            pixelsD[i] = gray;
        }

        testMatrix = new SimpleMatrix(64 * 64, 1, true, pixelsD);

        int N;
        int K;
        SimpleMatrix gamma_x;
        int[] z_x;
        SimpleMatrix xk_temp;
        SimpleMatrix del_x_vec;
        SimpleMatrix pk_temp;
        SimpleMatrix dk;
        int epsilon;
        boolean isNonnegative = true;
        final int eps = 1;
        double tolerance = 0.001;

    }
}
