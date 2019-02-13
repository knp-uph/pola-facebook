package com.domain.process.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class BarCodeService {
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

        LuminanceSource source = new BufferedImageLuminanceSource(image);
        bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Map<DecodeHintType, Object> tmpHintsMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        tmpHintsMap.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.of(BarcodeFormat.EAN_13, BarcodeFormat.EAN_8));
        tmpHintsMap.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

        Reader reader = new MultiFormatReader();
        try {
            result = reader.decode(bitmap, tmpHintsMap);
        } catch (FormatException | ChecksumException | NotFoundException e) {
            return null;
        }

        return result != null ? result.getText() : null;
    }

    public static void main(String... args) throws IOException {
        AtomicInteger successful = new AtomicInteger();
        AtomicInteger total = new AtomicInteger();

        final BarCodeService barCodeService = new BarCodeService();

        try (Stream<Path> paths = Files.walk(Paths.get("D:\\Piotr\\Documents\\C Folder\\NAUKA TAM I WTEDY\\Pola\\Materiały Testowe"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach((f) -> {
                        try {
                            total.getAndIncrement();
                            String barCodeFound = barCodeService.processBarCode(new FileInputStream(f.toFile()));
                            System.out.println(f.getFileName() + ": " + barCodeFound);
                            if (barCodeFound != null) {
                                successful.getAndIncrement();
                            }
                        } catch (FileNotFoundException e) {
                        }
                    });
        }
        System.out.println("=================RESULTS=================");
        System.out.println("Successful: " + successful.intValue());
        System.out.println("Total: " + total.intValue());
        System.out.println("Total accuracy: " + (double) successful.intValue() / total.intValue() * 100 + "%");
    }
}
