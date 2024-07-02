package com.thlink.controller;

import com.thlink.sinacorpdfparser.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/converter")
public class ConverterController {

    @PostMapping
    public ResponseEntity<?> convert(MultipartFile[] notas, OpcaoExportacao formatoSaida, NotaNegociacao.Tipos tipo){

        try {
            List<File> arquivos = new ArrayList<>();
            for (MultipartFile file : notas) {
                File finalFile = File.createTempFile("temp-"+ file.getOriginalFilename(), "");
                file.transferTo(finalFile);
                arquivos.add(finalFile);
            }
            String path = APICliente.builder()
                    .arquivos(arquivos)
                    .tipo(tipo)
                    .opcaoExportacao(formatoSaida)
                    .parser(tipo == NotaNegociacao.Tipos.BMF ? new ParserBMF() : new ParserBovespa())
                    .senha("")
                    .build()
                    .executar();
            File file = new File(path);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
