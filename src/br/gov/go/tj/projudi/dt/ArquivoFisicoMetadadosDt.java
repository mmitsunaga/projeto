package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que encapsula os dados do arquivo de metadados.json de um processo.
 * Esse arquivo armazena a listagem de arquivos por volume e lista de peças
 * @author mmitsunaga
 *
 */
@JsonIgnoreProperties({"completo", "versao", "duracao_processamento", "data_processamento", "datahora_processamento", "pdf"})
public class ArquivoFisicoMetadadosDt implements Serializable {

	private static final long serialVersionUID = 6260758502218320921L;
	
	private static final int KBYTE = 1024;
	
	@JsonProperty("numero")
	private String numero;
	
	@JsonProperty("pecas")
	private List <Pecas> pecas;
	
	@JsonIgnore
	private Set<Pecas> hashPecas;
	
	@JsonIgnore
	private Map<String, Integer> volumes;
	
	@JsonIgnoreProperties({"sha256_hash", "md5_hash"})
	public static class Pecas {
		
		@JsonProperty
		private String ordem;
		
		@JsonProperty
		private String nome;
		
		@JsonProperty
		private long tamanho;
		
		@JsonProperty
		private String pagina_inicial;
		
		@JsonProperty
		private String ordem_volume;
				
		@JsonProperty
		private String arquivo_origem;
		
		@JsonProperty
		private String pagina_final;
		
		@JsonProperty
		private String qtd_paginas;
		
		@JsonProperty("tipo_peca")
		private String tipo;
		
		public Pecas() {
			
		}
		
		public String getOrdem() {
			return ordem;
		}

		public String getNome() {
			return nome;
		}

		public long getTamanho() {
			return tamanho;
		}
		
		public long getTamanhoKB(){
			return tamanho / KBYTE;
		}

		public String getPagina_inicial() {
			return pagina_inicial;
		}

		public String getOrdem_volume() {
			return ordem_volume;
		}

		public String getArquivo_origem() {
			return arquivo_origem;
		}

		public String getPagina_final() {
			return pagina_final;
		}

		public String getQtd_paginas() {
			return qtd_paginas;
		}

		public String getTipo() {
			return tipo;
		}

		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		
	}
	
	public String getNumero() {
		return numero;
	}
	
	public List<Pecas> getPecas() {
		return pecas;
	}
	
	/**
	 * Retorna o tamanho de um arquivo específico. 
	 * Utiliza um hashset para melhorar o desempenho na pesquisa
	 * @param nomeArquivo
	 * @return
	 */
	public long getTamanhoPorNomeArquivo(String nomeArquivo){
		long tamanho = 0;
		if (this.getPecas() != null){
			if (this.hashPecas == null) this.hashPecas = new HashSet<>(this.pecas);
			Optional <Pecas> op = this.hashPecas.stream().filter(p -> p.getNome().equalsIgnoreCase(nomeArquivo)).findFirst();
			if (op.isPresent()) tamanho = op.get().getTamanho();			
		}
		return tamanho;
	}
	
	/**
	 * Percorre a lista de arquivos e faz o agrupamento por um tamanho específico
	 * @param tamanhoEmBytes
	 */
	public List<String> getVolumesPorTamanho(long tamanhoEmKbytes){
		long soma = 0;
		long tamanho = 0;
		String nome = "";
		String lista = "";
		List<String> volumes = new ArrayList<>();
		for (int i = 0; i < this.pecas.size(); i++){
			tamanho = this.pecas.get(i).getTamanho();
			nome = this.pecas.get(i).getNome();
			if (tamanho < tamanhoEmKbytes){
				if (soma + tamanho < tamanhoEmKbytes){
					lista += ";" + nome;
				} else {					
					volumes.add(lista); // Salva os arquivos agrupados numa nova posição dos volumes
					soma = 0;
					lista = ";" + nome; // coloca o item atual na lista de acumuladores
				}
			} else {
				System.out.println("Arquivo maior que o tamanho padrão - " + nome + "(" + getTamanhoPorNomeArquivo(nome) + ")");
			}						
			soma += tamanho;
		}
		return volumes;
	}
	
	/**
	 * 
	 * @param tamanhoVolume - tamanho do volume em Kbytes
	 * @return
	 */
	public long getTotalVolumes(long tamanhoVolume){
		long tamanhoTotal = 0;
		for (int i=0; i< this.pecas.size(); i++) tamanhoTotal += this.pecas.get(i).getTamanhoKB();			
		long resto = tamanhoTotal % tamanhoVolume;
		long qtdeVolumes = (tamanhoTotal / tamanhoVolume);
		return resto > 0 ? ++qtdeVolumes : qtdeVolumes;
	}
	
	/**
	 * Libera a memória alocada pelas estruturas
	 */
	public void clear() {
		if (this.pecas != null) this.pecas.clear();
		if (this.hashPecas != null) this.hashPecas.clear();
		this.pecas = null;		
		this.hashPecas = null;
	}
	
}
