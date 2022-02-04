package br.gov.go.tj.projudi.dt.relatorios;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.UsuarioDt;

public class EstatisticaMovimentacaoDt extends Dados {
    /**
     * 
     */
    private static final long serialVersionUID = 4052063057753833671L;

    public static final int CodigoPermissao = 273;
  
    //VARIAVEIS DE CONTROLE
    private String dataInicial;
    private String dataFinal;
    private String id_Serventia;
	private String serventia;
	private UsuarioDt usuario;
	private List detalhesMovimentacao;

	
	//---------------------------------------------------------   
    public EstatisticaMovimentacaoDt()  {
		limpar();
	}
    
    public void limpar() {
		// VARIAVEIS DE CONTROLE
		id_Serventia = "";
		serventia = "";
		usuario = new UsuarioDt();
		detalhesMovimentacao = null;
		dataInicial = "";
		dataFinal = "";

	}
    
    public void limparParametrosConsulta(){
    	id_Serventia = "";
		serventia = "";
		usuario = new UsuarioDt();
		detalhesMovimentacao = null;
    }

	//GET E SET VARIAVEIS DE CONTROLE DO RELATÓRIO***********************************************

	public String getId_Serventia() {
		return id_Serventia;
	}

	public void setId_Serventia(String id_Serventia) {
		if (id_Serventia != null)
			this.id_Serventia = id_Serventia;
	}
	
	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		if (serventia != null)
			this.serventia = serventia;
	}

	public UsuarioDt getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDt usuario) {
		if (usuario != null)
			this.usuario = usuario;
	}

	public List getListaDetalhesMovimentacao() {
		if (detalhesMovimentacao == null) {
			detalhesMovimentacao = new ArrayList();
		}
		return detalhesMovimentacao;
	}

	public void setListaDetalhesMovimentacao(List detalhesMovimentacao) {
		this.detalhesMovimentacao = detalhesMovimentacao;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		if (dataInicial != null)
			this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		if (dataFinal != null)
			this.dataFinal = dataFinal;
	}
    
    public String getId() {
        
        return null;
    }
    
    public void setId(String id) {
        
        
    }
	
}
