package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class PendenciaResponsavelDt extends PendenciaResponsavelDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = 5984762681859756354L;

    public static final int CodigoPermissao = 394;
	
	private String cargoTipoCodigo;
    private String usuarioServentiaCargo;
	private String nomeUsuarioServentiaCargo;
	private String nomeUsuarioResponsavel;
	private String id_ServentiaGrupo;
	private String serventiaGrupo;
	
	private String id_pendencia_Ementa;
	
	private boolean novoResponsavel;
	private boolean conclusoMagistrado;
	private boolean semRegra;
	private List listaHistoricoPendencia;
	private List<PendenciaDt> listaPendenciaDt;
	
	public PendenciaResponsavelDt() {
		super.limpar();
		limpar();

	}
	
	public void limpar()  {
		cargoTipoCodigo="";
		usuarioServentiaCargo="";
		nomeUsuarioServentiaCargo="";
		nomeUsuarioResponsavel="";
		id_ServentiaGrupo="";
		serventiaGrupo="";
		novoResponsavel = false;
		conclusoMagistrado = false;
		semRegra = false;
		this.listaHistoricoPendencia = null;
		listaPendenciaDt = new ArrayList<PendenciaDt>();
		id_pendencia_Ementa="";
	}

	public String getCargoTipoCodigo() {
		return cargoTipoCodigo;
	}

	public void setCargoTipoCodigo(String cargoTipoCodigo) {
		this.cargoTipoCodigo = cargoTipoCodigo;
	}
	
	public String getUsuarioServentiaCargo() {
		return usuarioServentiaCargo;
	}

	public void setUsuarioServentiaCargo(String usuarioServentiaCargo) {
		this.usuarioServentiaCargo = usuarioServentiaCargo;
	}

	public String getNomeUsuarioServentiaCargo() {
		return nomeUsuarioServentiaCargo;
	}

	public void setNomeUsuarioServentiaCargo(String nomeUsuarioServentiaCargo) {
		this.nomeUsuarioServentiaCargo = nomeUsuarioServentiaCargo;
	}

	public String getNomeUsuarioResponsavel() {
		return nomeUsuarioResponsavel;
	}

	public void setNomeUsuarioResponsavel(String nomeUsuarioResponsavel) {
		this.nomeUsuarioResponsavel = nomeUsuarioResponsavel;
	}
	
	public void setNovoResponsavel(boolean valor) {		
			this.novoResponsavel = valor;				
	}
	
	public void setNovoResponsavel(String novoResponsavel) {
		if (novoResponsavel != null && novoResponsavel.trim().length() > 0){
			this.novoResponsavel = Funcoes.BancoLogicoBoolean(novoResponsavel);		
		}
		
	}

	public boolean isNovoResponsavel() {
		return novoResponsavel;
	}
	
	public void setConclusoMagistrado(boolean conclusoDesembargador) {
		this.conclusoMagistrado = conclusoDesembargador;		
	}

	public boolean isConclusoMagistrado() {
		return conclusoMagistrado;
	}
	
	public boolean isSemRegra() {
		return semRegra;
	}

	public void setSemRegra(boolean semRegra) {
		this.semRegra = semRegra;
	}

	public String getId_ServentiaGrupo() {
		return id_ServentiaGrupo;
	}

	public void setId_ServentiaGrupo(String idServentiaGrupo) {
		 if(idServentiaGrupo!=null)	this.id_ServentiaGrupo = idServentiaGrupo;
	}

	public String getServentiaGrupo() {
		return serventiaGrupo;
	}

	public void setServentiaGrupo(String serventiaGrupo) {
		if(serventiaGrupo!=null)this.serventiaGrupo = serventiaGrupo;
	}
	
	public List getListaHistoricoPendencia() {
		return listaHistoricoPendencia;
	}

	public void setListaHistoricoPendencia(List listaHistoricoPendencia) {
		if (listaHistoricoPendencia != null) this.listaHistoricoPendencia = listaHistoricoPendencia;
	}
	
	public List<PendenciaDt> getlistaPendenciaDt() {
		return  listaPendenciaDt;
	}

	public void setlistaPendenciaDt(List<PendenciaDt> listaPendenciaDt) {
		this.listaPendenciaDt = listaPendenciaDt;
	}
	
	public String getId_pendencia_Ementa()  {
		return id_pendencia_Ementa;
	}
	
	public void setId_pendencia_Ementa(String valor ) {
		if (valor!=null && valor.equalsIgnoreCase("null")) {
			id_pendencia_Ementa = "";
		} else if (!valor.equalsIgnoreCase("")){
			id_pendencia_Ementa = valor;
		}
	}
	
	protected void associarDt( PendenciaResponsavelDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setNomeUsuarioServentiaCargo(rs.getString("NOME_USU_SERV_CARGO"));
		Dados.setNomeUsuarioResponsavel(rs.getString("NOME_USU_RESP"));
		Dados.setCargoTipoCodigo(rs.getString("CARGO_TIPO_CODIGO"));
		Dados.setServentia(rs.getString("SERV_CARGO"));
		
		Dados.setId(rs.getString("ID_PEND_RESP"));
		Dados.setPendencia(rs.getString("PEND"));
		Dados.setId_Pendencia( rs.getString("ID_PEND"));
		Dados.setId_Serventia( rs.getString("ID_SERV"));
		Dados.setServentia( rs.getString("SERV"));
		Dados.setId_ServentiaTipo( rs.getString("ID_SERV_TIPO"));
		Dados.setServentiaTipo( rs.getString("SERV_TIPO"));
		
		Dados.setId_UsuarioResponsavel( rs.getString("ID_USU_RESP"));
		
		Dados.setUsuarioResponsavel( rs.getString("USU_RESP"));
		Dados.setId_ServentiaCargo( rs.getString("ID_SERV_CARGO"));
		Dados.setServentiaCargo( rs.getString("SERV_CARGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}
	
}
