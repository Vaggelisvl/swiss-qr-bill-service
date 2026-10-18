package dev.vrailas.qrbill.adapter.out.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import dev.vrailas.qrbill.application.port.out.QrCodeRenderer;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * ZXing based renderer: error correction level M, UTF-8, no quiet zone, with the Swiss cross
 * (white cross on a black square, 7/46 of the width) in the centre.
 */
public final class ZxingQrCodeRenderer implements QrCodeRenderer {

    /** 46 mm at 300 dpi, the mandatory QR code size of the specification. */
    static final int SIZE_PX = 543;
    private static final double CROSS_RATIO = 7.0 / 46.0;

    private static final Map<EncodeHintType, Object> HINTS = Map.of(
            EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M,
            EncodeHintType.CHARACTER_SET, "UTF-8",
            EncodeHintType.MARGIN, 0);

    @Override
    public byte[] renderPng(String payload) {
        try {
            BitMatrix matrix = new QRCodeWriter().encode(payload, BarcodeFormat.QR_CODE, SIZE_PX, SIZE_PX, HINTS);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            drawSwissCross(image);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            return out.toByteArray();
        } catch (WriterException | IOException e) {
            throw new QrCodeRenderingException("Could not render QR code", e);
        }
    }

    private static void drawSwissCross(BufferedImage image) {
        int size = image.getWidth();
        int box = (int) Math.round(size * CROSS_RATIO);
        int origin = (size - box) / 2;
        int arm = Math.round(box * 0.2f);
        int length = Math.round(box * 0.6f);
        int mid = origin + box / 2;
        Graphics2D g = image.createGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(origin - arm / 2, origin - arm / 2, box + arm, box + arm); // white border around the symbol
            g.setColor(Color.BLACK);
            g.fillRect(origin, origin, box, box);
            g.setColor(Color.WHITE);
            g.fillRect(mid - arm / 2, mid - length / 2, arm, length);
            g.fillRect(mid - length / 2, mid - arm / 2, length, arm);
        } finally {
            g.dispose();
        }
    }
}
