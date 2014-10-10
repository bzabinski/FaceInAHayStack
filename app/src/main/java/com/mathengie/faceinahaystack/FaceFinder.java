package com.mathengie.faceinahaystack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;

import org.opencv.android.Utils;
import org.opencv.contrib.FaceRecognizer;
import org.opencv.core.Algorithm;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Era on 9/23/2014.
 */
public class FaceFinder {

    List<Mat> matrices = new ArrayList<Mat>();
    List<Integer> labels = new ArrayList<Integer>();
    Mat labelsVector = null;

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
                Bitmap lilFace = Bitmap.createBitmap(photoBM, (int) (midPoint.x - eyeDist), (int) (midPoint.y - eyeDist), (int) eyeDist * 2, (int) eyeDist * 2);
                Bitmap tempFace = lilFace.copy(Bitmap.Config.RGB_565, true);
                Mat tmpMat = new Mat (lilFace.getWidth(), lilFace.getHeight(), CvType.CV_8UC1);
                Utils.bitmapToMat(tempFace, tmpMat);
                matrices.add(tmpMat);
                labels.add(labelID);
                counter++;

            }

            labelID++;
        }

        labelsVector = new Mat(labels.size(), 1, CvType.CV_8U);

        for(int i = 0; i < labels.size(); i++)
        {
            labelsVector.put(i, 1, labels.get(i));
        }

        Algorithm.
    }
}
