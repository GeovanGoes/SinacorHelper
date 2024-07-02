package com.thlink.sinacorpdfparser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;import javax.swing.text.AbstractDocument.LeafElement;

public class ParserBovespa implements Parser {
	
	private ArrayList<NotaNegociacao> notas;

	public ParserBovespa() {
		notas = new ArrayList<NotaNegociacao>();
	}
	
	public Parser find(String text) {
		String [] documentLines = text.split("\r\n|\r|\n");
		
		notas = new ArrayList<NotaNegociacao>();
		
		NotaNegociacaoBovespa notaBovespa = new NotaNegociacaoBovespa();
		for (int i = 0; i < documentLines.length; i++) 
                {
                    System.out.println(documentLines[i]);
                    
                    if (documentLines[i].contains("1-BOVESPA")) {
                    	String[] splited = documentLines[i].split(" ");
                        
                        int length = splited.length;
						if (length >= 7) {
							notaBovespa.addItemNegociacao(new ItemNotaNegociacao(
									splited[1], 
									splited[3] + " " + splited[4] + " " + splited[5] + " " + splited[6] + " " + splited[7], 
									splited[length-2], 
									splited[length-3], 
									splited[length-4]));
							System.out.println("C/V "+splited[1]);
							System.out.println("Titulo " + splited[3]);
                        	System.out.println("Ajuste Valor Operação " + splited[length-2]);
                        	System.out.println("Preço " + splited[length-3]);
                        	System.out.println("Quantidade " + splited[length-4]);
                        	
                        } 
                    }
                     
                    
                    if (documentLines[i].contains("Resumo dos Negócios")) 
                    {
                    	int shift = 0;
                    	
                    	if (documentLines[i-40].equalsIgnoreCase("Nr. nota"))
                    		shift = -5;
                    		
                        notaBovespa.setDataPregao(documentLines[i-40-shift]);
                        notaBovespa.setCorretora(documentLines[i-39-shift]);
                        notaBovespa.setNrNotaNegociacao(documentLines[i-44-shift]);
                        notaBovespa.setDebentures(extract(documentLines[i-8]));
                        notaBovespa.setVendasAVista(extract(documentLines[i-7]));
                        notaBovespa.setComprasAVista(extract(documentLines[i-6]));
                        notaBovespa.setOpcoesCompras(extract(documentLines[i-5]));
                        notaBovespa.setOpcoesVendas(extract(documentLines[i-4]));
                        notaBovespa.setOperacoesATermo(extract(documentLines[i-3]));
                        notaBovespa.setValorDasOpTitulosPublicos(extract(documentLines[i-2]));
                        notaBovespa.setValorDasOperacaoes(extract(documentLines[i-1]));
                    }
			
                    if(documentLines[i].contains("Resumo Financeiro")) 
                    {
                            notaBovespa.setTotalCBLC(extract(tokenizeBov(documentLines[i-18])));
                            notaBovespa.setValorLiquidoDasOperacoes(extract(tokenizeBov(documentLines[i-17])));
                            notaBovespa.setTaxaDeLiquidacao(extract(tokenizeBov(documentLines[i-16])));
                            notaBovespa.setTaxaDeRegistro(extract(tokenizeBov(documentLines[i-15])));
                            notaBovespa.setTotalBovespa(extract(tokenizeBov(documentLines[i-14])));
                            notaBovespa.setTaxaDeTermo(extract(tokenizeBov(documentLines[i-13])));
                            notaBovespa.setTaxaANA(extract(tokenizeBov(documentLines[i-12])));
                            notaBovespa.setEmolumentos(extract(tokenizeBov(documentLines[i-11])));
                            notaBovespa.setTotalCustos(extract(tokenizeBov(documentLines[i-9])));
                            notaBovespa.setTaxaOperacional(extract(tokenizeBov(documentLines[i-7])));
                            notaBovespa.setExecucao(extract(tokenizeBov(documentLines[i-6])));
                            notaBovespa.setTaxaDeCustodia(extract(tokenizeBov(documentLines[i-5])));
                            notaBovespa.setImposto(extract(tokenizeBov(documentLines[i-4])));
                            notaBovespa.setIRRF(extract(tokenizeBov(documentLines[i-3])));
                            notaBovespa.setOutros(extract(tokenizeBov(documentLines[i-2])));
                            notaBovespa.setLiquido(extract(tokenizeBov(documentLines[i-1])));
                            notas.add(notaBovespa);
                            notaBovespa = new NotaNegociacaoBovespa();
                    }
		}
		return this;
	}
	
	private String tokenizeBov(String line) {
		Pattern pattern = Pattern.compile("\\d+([\\.\\,]\\d+)+");
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        return matcher.group();
	}
	
	private Double extract(String text) {
		String [] textSplitted = text.replace(".", "").replace(",", ".").split("\\|") ;
		Double amount = Double.parseDouble(textSplitted[0].trim());
		if(textSplitted.length >= 2) {
			amount = (textSplitted[1].trim()).equalsIgnoreCase("D") ? amount * (-1) : amount;
		}
		return amount;
	}
	
	public ArrayList<NotaNegociacao> getNotas() {
		return notas;
	}

}
