package com.thlink.sinacorpdfparser;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class CustomCsvExporter {

	public static String export(ArrayList<NotaNegociacao> notas) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder content = new StringBuilder();
		int qtdCampos = 0;

		/** Header **/
		content.append(getHeader());

		/** Conteúdo das notas **/
		for(NotaNegociacao nota : notas) {
			content.append(nota.toCustomCSV());
		}
		
		return content.toString();
	}
	
	public static String getHeader() {
		StringBuilder header = new StringBuilder();
		header.append("data;numero da nota;titulo;valor unitario;quantidade;compra ou venda;corretora");
		header.append("\n");
		return header.toString();
	}
	
}
