package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.TJDataHora;

public class ArquivoBancoDt extends Dados {

	private static final long serialVersionUID = 6105159437532608268L;
	
	public static final int ARQUIVO_LIDO_COM_SUCESSO 	= 1;
	public static final int ARQUIVO_LIDO_COM_ERRO 		= 0;
	public static final int ARQUIVO_LIDO_COM_ERRO_QTDE_REGISTROS = 2;
	
	private String Id_ArquivoBanco;
	private String ArquivoBanco;
	private String Id_Banco;
	private String Erro;
	private String CodigoTemp;
	private String Ativo;
	private String Reprocessado;
	private TJDataHora DataRealizacao;
	private String Mensagem;
	private String Conteudo;

	@Override
	public String getId() {
		return Id_ArquivoBanco;
	}

	@Override
	public void setId(String id) {
		Id_ArquivoBanco = id;
	}

	public String getArquivoBanco() {
		return ArquivoBanco;
	}

	public void setArquivoBanco(String arquivoBanco) {
		ArquivoBanco = arquivoBanco;
	}

	public String getId_Banco() {
		return Id_Banco;
	}

	public void setId_Banco(String id_Banco) {
		Id_Banco = id_Banco;
	}

	public String getErro() {
		return Erro;
	}

	public void setErro(String erro) {
		Erro = erro;
	}

	public String getCodigoTemp() {
		return CodigoTemp;
	}

	public void setCodigoTemp(String codigoTemp) {
		CodigoTemp = codigoTemp;
	}

	public String getAtivo() {
		return Ativo;
	}

	public void setAtivo(String ativo) {
		Ativo = ativo;
	}
	
	public String getReprocessado() {
		return Reprocessado;
	}

	public void setReprocessado(String reprocessado) {
		Reprocessado = reprocessado;
	}

	public TJDataHora getDataRealizacao() {
		return DataRealizacao;
	}

	public void setDataRealizacao(TJDataHora dataRealizacao) {
		DataRealizacao = dataRealizacao;
	}

	public String getMensagem() {
		return Mensagem;
	}

	public void setMensagem(String mensagem) {
		Mensagem = mensagem;
	}

	public String getConteudo() {
		return Conteudo;
	}

	public void setConteudo(String conteudo) {
		Conteudo = conteudo;
	}
	
	public String getLabelErroProcessamento() {
		if( this.Erro != null && this.Erro.equals(String.valueOf(ARQUIVO_LIDO_COM_SUCESSO)) ) {
			return "Sim";
		}
		return "Não";
	}
}