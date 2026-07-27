package com.paccanaro.gateway.pagamento.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.paccanaro.gateway.pagamento.model.Pagamento;
import com.paccanaro.gateway.pagamento.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
public class QrCodeController {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping("/api/pagamentos/{id}/qrcode")
    public ResponseEntity<byte[]> gerarQrCode(@PathVariable Integer id) throws WriterException, java.io.IOException {

        Pagamento pagamento = pagamentoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalStateException("Pagamento não encontrado"));

        if (pagamento.getDadosPix() == null) {
            return ResponseEntity.badRequest().build();
        }

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(pagamento.getDadosPix(), BarcodeFormat.QR_CODE, 280, 280);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(out.toByteArray());
    }
}