package br.gov.go.tj.projudi.dt.relatorios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioDt;




public class EstatisticaPendenciaDt implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1197922867080694317L;

    public static final int CodigoPermissao = 270;
  
    //VARIAVEIS DE CONTROLE
    private String dataInicial;
    private String dataFinal;
    private String id_Serventia;
	private String serventia;
	private UsuarioDt usuario;
	private List detalhesPendenciaServentia;

	
	//---------------------------------------------------------   
    public EstatisticaPendenciaDt() {
		limpar();
	}
    
    public void limpar() {
		// VARIAVEIS DE CONTROLE
		id_Serventia = "";
		serventia = "";
		usuario = new UsuarioDt();
		detalhesPendenciaServentia = null;
		dataInicial = "";
		dataFinal = "";

	}
    
    public void limparParametrosConsulta(){
    	id_Serventia = "";
		serventia = "";
		usuario = new UsuarioDt();
		detalhesPendenciaServentia = null;
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

	public List getListaDetalhesPendenciaServentia() {
		if (detalhesPendenciaServentia == null) {
			detalhesPendenciaServentia = new ArrayList();
		}
		return detalhesPendenciaServentia;
	}

	public void setListaDetalhesPendenciaServentia(List detalhesPendenciaServentia) {
		this.detalhesPendenciaServentia = detalhesPendenciaServentia;
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
	
}
