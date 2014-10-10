package com.mathengie.faceinahaystack;

import org.opencv.contrib.FaceRecognizer;
import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;

import java.util.List;

/**
 * Created by Era on 10/7/2014.
 */
public class MyFaceRecognizer extends FaceRecognizer
{
    public MyFaceRecognizer()
    {

    }

    public void train(List<Mat> src, Mat labels)
    {
        Mat src_mat = Converters.vector_Mat_to_Mat(src);
        train_0(nativeObj, src_mat.nativeObj, labels.nativeObj);

        return;
    }

    @Override
    public  void predict(Mat src, int[] label, double[] confidence)
    {
        double[] label_out = new double[label.length];
        double[] confidence_out = new double[confidence.length];
        predict_0(nativeObj, src.nativeObj, label_out, confidence_out);
        if(label!=null) label[0] = (int)label_out[0];
        if(confidence!=null) confidence[0] = (double)confidence_out[0];
        return;
    }

    private static native void train_0(long nativeObj, long src_mat_nativeObj, long labels_nativeObj);

    private static native void predict_0(long nativeObj, long src_nativeObj, double[] label_out, double[] confidence_out);
}
