package dev.vrailas.qrbill.adapter;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import dev.vrailas.qrbill.adapter.out.qr.ZxingQrCodeRenderer;
import dev.vrailas.qrbill.domain.QrBillPayloadEncoder;
import dev.vrailas.qrbill.domain.QrBillTestFixtures;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ZxingQrCodeRendererTest {

    @Test
    void renderedCodeCanBeDecodedBackToThePayloadDespiteTheSwissCross() throws Exception {
        String payload = new QrBillPayloadEncoder().encode(QrBillTestFixtures.qrIbanBill());

        byte[] png = new ZxingQrCodeRenderer().renderPng(payload);

        BufferedImage rendered = ImageIO.read(new ByteArrayInputStream(png));
        assertThat(rendered.getWidth()).isEqualTo(543);
        BufferedImage withQuietZone = addQuietZone(rendered, 60); // scanners need a margin; the spec adds it around the symbol
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(withQuietZone)));
        String decoded = new MultiFormatReader().decode(bitmap, Map.of(DecodeHintType.TRY_HARDER, Boolean.TRUE)).getText();

        assertThat(decoded).isEqualTo(payload);
    }

    private static BufferedImage addQuietZone(BufferedImage source, int margin) {
        BufferedImage result = new BufferedImage(source.getWidth() + 2 * margin, source.getHeight() + 2 * margin, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, result.getWidth(), result.getHeight());
        g.drawImage(source, margin, margin, null);
        g.dispose();
        return result;
    }
}
