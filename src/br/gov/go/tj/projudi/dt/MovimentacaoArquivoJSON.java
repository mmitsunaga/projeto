package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

public class MovimentacaoArquivoJSON {
	
	private List lisMovimentacoes = new ArrayList();
	private Movimentacao movi = null;
	
	public void addMovimentacao(String titulo, String complemento){
		movi = new Movimentacao(titulo, complemento);
		lisMovimentacoes.add(movi);
	}
			
	public void addArquivo(String id, String nome, String hash, String id_movi_arq){
		movi.addArquivo(new Arquivo(id,nome,hash, id_movi_arq));	
	}
	
	public String getJSON(){
		StringBuilder stTemp = new StringBuilder();
		stTemp.append("[");
		if (lisMovimentacoes.size()>=1){
			Movimentacao movTemp = (Movimentacao)lisMovimentacoes.get(0);
			stTemp.append(movTemp.getJSON());
			for (int i =1; i<lisMovimentacoes.size();i++){
				movTemp = (Movimentacao)lisMovimentacoes.get(i);
				stTemp.append(",").append(movTemp.getJSON());
			}
		}
		
		stTemp.append("]");
		
		return stTemp.toString();
	}
	
	public class Arquivo{
		String Id="";
		String Nome ="";
		String Hash ="";
		String Id_MovimentacaoArquivo="";
		
		public Arquivo(String id, String nome, String hash, String id_movi_arq) {
			Id=id;
			Nome = nome.replace("\"","");
			Hash = hash;
			Id_MovimentacaoArquivo= id_movi_arq;
		}
		public String getId() {
			return Id;
		}

		public String getNome() {
			return Nome;
		}
		
		public String getHash() {
			return Hash;
		}
	
		public String getId_MovimentacaoArquivo() {
			return Id_MovimentacaoArquivo;
		}

		
		public String getJSON(){
			StringBuilder stTemp = new StringBuilder();
			stTemp.append("{");
		
			stTemp.append("\"id\":\"").append(Id).append("\",");
			stTemp.append("\"nome\":\"").append(Nome).append("\",");
			stTemp.append("\"hash\":\"").append(Hash).append("\",");
			stTemp.append("\"id_movi_arq\":\"").append(Id_MovimentacaoArquivo).append("\"");
			
			stTemp.append("}");
						
			return stTemp.toString();
		}
					
	}
	
	public class Movimentacao{
		String Titulo="";
		String Complemento="";
		List Arquivos=null;
		
		public Movimentacao(String titulo, String complemento ){
			if (titulo!=null)
				Titulo = titulo.replace("\"","");
			if (complemento!=null)
				Complemento = complemento.replace("\"","");
			Arquivos = null;
		}
				
		public void addArquivo(Arquivo arq){
			if (Arquivos==null) Arquivos = new ArrayList();
			Arquivos.add(arq);
		}
		
		public String getJSON(){
			StringBuilder stTemp = new StringBuilder();
			stTemp.append("{");
			stTemp.append("\"titulo\":\"").append(Titulo).append("\",");
			stTemp.append("\"complemento\":\"").append(Complemento).append("\",");
			if (Arquivos!=null && Arquivos.size()>=1){
				StringBuilder stTemp2 = new StringBuilder();
				
				Arquivo arqTemp = (Arquivo)Arquivos.get(0);
				stTemp2.append(arqTemp.getJSON());
				for(int i=1; i<Arquivos.size();i++){
					arqTemp = (Arquivo)Arquivos.get(i);
					stTemp2.append(",").append(arqTemp.getJSON());
				
				}
				stTemp.append("\"arquivos\":[").append(stTemp2.toString()).append("]");
			}else{
				stTemp.append("\"arquivos\":null");
			}
			
			stTemp.append("}");
						
			return stTemp.toString();
			
		}
		
	}

}
