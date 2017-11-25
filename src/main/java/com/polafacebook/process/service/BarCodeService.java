package com.polafacebook.process.service;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.MultiFormatUPCEANReader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Jakub on 13.07.2017.
 */
@Service
public class BarCodeService {
//    public String processBarCode(URL fileURL) {
//        BufferedImage image = null;
//        BinaryBitmap bitmap = null;
//        Result result = null;
//        try {
//            image = ImageIO.read(fileURL);
//            int[] pixels = image
//                    .getRGB(
//                            0, 0,
//                            image.getWidth(), image.getHeight(),
//                            null, 0, image.getWidth());
//            RGBLuminanceSource source = new RGBLuminanceSource(image.getWidth(), image.getHeight(), pixels);
//            bitmap = new BinaryBitmap(new HybridBinarizer(source));
//        } catch (IOException e) {return null;}
//
//        Reader reader = new MultiFormatUPCEANReader(null);
//        try {
//            result = reader.decode(bitmap);
//        } catch (FormatException | ChecksumException | NotFoundException e) {return null;}
//
//        return result != null ? result.getText() : null;
//    }

    public String processBarCode(InputStream stream) {
        try {
            return processImage(ImageIO.read(stream));
        } catch (IOException e) {
            return null;
        }
    }

    public String processBarCode(URL fileURL) {
        try {
            return processImage(ImageIO.read(fileURL));
        } catch (IOException e) {
            return null;
        }
    }

    private String processImage(BufferedImage image) {
        BinaryBitmap bitmap = null;
        Result result = null;
        int[] pixels = image
                .getRGB(
                        0, 0,
                        image.getWidth(), image.getHeight(),
                        null, 0, image.getWidth());
        RGBLuminanceSource source = new RGBLuminanceSource(image.getWidth(), image.getHeight(), pixels);
        bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Reader reader = new MultiFormatUPCEANReader(null);
        try {
            result = reader.decode(bitmap);
        } catch (FormatException | ChecksumException | NotFoundException e) {
            return null;
        }

        return result != null ? result.getText() : null;
    }
}
