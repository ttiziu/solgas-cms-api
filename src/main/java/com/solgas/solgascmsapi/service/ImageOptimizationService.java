package com.solgas.solgascmsapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
public class ImageOptimizationService {

    private static final String JPEG_MIME = "image/jpeg";

    private final int maxDimension;
    private final float jpegQuality;

    public ImageOptimizationService(
            @Value("${app.images.max-dimension:1080}") int maxDimension,
            @Value("${app.images.jpeg-quality:0.88}") float jpegQuality) {
        this.maxDimension = maxDimension;
        this.jpegQuality = jpegQuality;
    }

    public OptimizedImage optimize(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío.");
        }

        String contentType = file.getContentType();
        if (contentType != null && contentType.contains("webp")) {
            return new OptimizedImage(file.getBytes(), "image/webp", ".webp");
        }

        BufferedImage source = ImageIO.read(file.getInputStream());
        if (source == null) {
            throw new IllegalArgumentException("El archivo no es una imagen válida (usa JPG o PNG).");
        }

        BufferedImage resized = resize(source, maxDimension);
        byte[] jpegBytes = encodeJpeg(resized);
        return new OptimizedImage(jpegBytes, JPEG_MIME, ".jpg");
    }

    private BufferedImage resize(BufferedImage source, int maxDimension) {
        int width = source.getWidth();
        int height = source.getHeight();
        if (width <= maxDimension && height <= maxDimension) {
            return toRgb(source);
        }

        double scale = (double) maxDimension / Math.max(width, height);
        int targetWidth = Math.max(1, (int) Math.round(width * scale));
        int targetHeight = Math.max(1, (int) Math.round(height * scale));

        BufferedImage target = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = target.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.drawImage(source, 0, 0, targetWidth, targetHeight, null);
        graphics.dispose();
        return target;
    }

    private BufferedImage toRgb(BufferedImage source) {
        if (source.getType() == BufferedImage.TYPE_INT_RGB) {
            return source;
        }
        BufferedImage rgb = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = rgb.createGraphics();
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        return rgb;
    }

    private byte[] encodeJpeg(BufferedImage image) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No hay encoder JPEG disponible.");
        }

        ImageWriter writer = writers.next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        if (writeParam.canWriteCompressed()) {
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(jpegQuality);
        }

        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             ImageOutputStream imageOutput = ImageIO.createImageOutputStream(output)) {
            writer.setOutput(imageOutput);
            writer.write(null, new IIOImage(image, null, null), writeParam);
            writer.dispose();
            return output.toByteArray();
        }
    }

    public record OptimizedImage(byte[] data, String contentType, String extension) {
    }
}
