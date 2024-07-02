package com.thlink.sinacorpdfparser;

public class ItemNotaNegociacao {
	
	private String compraOuVenda;
	private String titulo;
	private String valorOperacao;
	private String valorUnitario;
	private String quantidade;
	public String getCompraOuVenda() {
		return compraOuVenda;
	}
	public void setCompraOuVenda(String compraOuVenda) {
		this.compraOuVenda = compraOuVenda;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getValorOperacao() {
		return valorOperacao;
	}
	public void setValorOperacao(String valorOperacao) {
		this.valorOperacao = valorOperacao;
	}
	public String getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(String valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public String getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}
	public ItemNotaNegociacao(String compraOuVenda, String titulo, String valorOperacao, String valorUnitario,
			String quantidade) {
		super();
		this.compraOuVenda = compraOuVenda;
		this.titulo = titulo;
		this.valorOperacao = valorOperacao;
		this.valorUnitario = valorUnitario;
		this.quantidade = quantidade;
	}
}
