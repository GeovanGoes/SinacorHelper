package com.thlink.sinacorpdfparser;

import com.thlink.PDF.PDFToText;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
@Slf4j
public class APICliente {
    private final static String separator = File.separator;

    private PDFToText pdf2Text;

    /** Identifica BMF ou Bovespa. */
    private NotaNegociacao.Tipos tipo;

    private ArrayList<NotaNegociacao> notasNegociacao;

    private OpcaoExportacao opcaoExportacao;

    private Parser parser;

    private String caminho;

    private String senha;

    private long timestampArquivo;

    private List<File> arquivos;

    private final static String OUTPUT_FOLDER = FileUtils.getTempDirectoryPath();



    private ArrayList<NotaNegociacao> getNotasNegociacao() throws IOException
    {
        notasNegociacao = new ArrayList<NotaNegociacao>();
        if(arquivos != null && arquivos.size() > 0) {
            arquivos.forEach((arquivo) -> {
                try {
                    pdf2Text = new PDFToText(arquivo, senha);
                    parser.find(pdf2Text.getText());
                    notasNegociacao.addAll(parser.getNotas());

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    log.error("Erro ao processar arquivo: " + arquivo.getName());
                }
            });
        }

        return notasNegociacao;
    }



    public String executar() throws IOException, IllegalArgumentException, IllegalAccessException
    {
        getNotasNegociacao();

        if (opcaoExportacao == OpcaoExportacao.JSON) {
            exportarJson(notasNegociacao);
        } else if(opcaoExportacao == OpcaoExportacao.CSV) {
            exportarCustomCSV(notasNegociacao);
        } else {
            throw new IllegalArgumentException("Opção de exportação não suportada.");
        }

        log.info("Total de nota(s) encontradas: " + notasNegociacao.size());
        log.info("Arquivos gerados: ");

        if (opcaoExportacao == OpcaoExportacao.JSON) {
            String path = OUTPUT_FOLDER + separator + "notasNegociacao-" + tipo.toString() + "-" + timestampArquivo + ".json";
            log.info(path);
            return path;
        } else if (opcaoExportacao == OpcaoExportacao.CSV) {
            String path = OUTPUT_FOLDER + separator + "notasNegociacao-" + tipo.toString() + "-" + timestampArquivo + ".csv";
            log.info(path);
            return path;
        } else
            throw new IllegalArgumentException("Opção de exportação não suportada.");
    }

    private void exportarJson(ArrayList<NotaNegociacao> notas) throws IllegalArgumentException, IllegalAccessException, IOException{
        Writer fileWriter = new FileWriter(new File(OUTPUT_FOLDER + separator + "notasNegociacao-" + tipo.toString() + "-" + timestampArquivo + ".json"));
        fileWriter.append(JsonExporter.export(notas));
        fileWriter.close();
    }

    private void exportarCSV(ArrayList<NotaNegociacao> notas) throws IllegalArgumentException, IllegalAccessException, IOException{
        Writer fileWriter = new FileWriter(new File(OUTPUT_FOLDER + separator + "notasNegociacao-" + tipo.toString() + "-" + timestampArquivo + ".csv"));
        fileWriter.append(CsvExporter.export(notasNegociacao));
        fileWriter.close();
    }

    private void exportarCustomCSV(ArrayList<NotaNegociacao> notas) throws IllegalArgumentException, IllegalAccessException, IOException{
        Writer fileWriter = new FileWriter(new File(OUTPUT_FOLDER + separator + "notasNegociacao-" + tipo.toString() + "-" + timestampArquivo + ".csv"));
        fileWriter.append(CustomCsvExporter.export(notasNegociacao));
        fileWriter.close();
    }
}
