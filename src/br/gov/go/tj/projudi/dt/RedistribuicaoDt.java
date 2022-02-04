package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.utils.Funcoes;

/**
 * Dt criado para auxiliar na redistribuição de processos
 * 
 * @author msapaula
 *
 */
public class RedistribuicaoDt extends Dados {

	private static final long serialVersionUID = -3156349624252183308L;

	public static final int CodigoPermissao = 344;
	public static final String REMESSA_APENSO = "4";

	private List listaProcessos = null;
	private String id_AreaDistribuicao;
	private String areaDistribuicao;
	private String id_Serventia;
	private String serventia;
	private String id_ServentiaCargo;
	private String serventiaCargo;
	private String Id_Classificador;
	private String Classificador;
	private String id_ProcessoTipo;
	private String processoTipo;
	private String processoNumeroDependente;
	private String opcaoRedistribuicao = "1";

	//Variável para controlar a porcentagem de repasse de custas
	private String porcentagemRepasse;
	private Map listaIdProcessoPorcentagemRepasse;
	private Map listaMostrarMenuPorcentagemRepasseProcesso;
	private Map listaPorcentagemRepasseProcesso;

	private boolean RedistribuirTodosProcessoClassificado = false;

	public RedistribuicaoDt() {
		limpar();
	}

	public void limpar() {
		listaProcessos = new ArrayList();
		listaIdProcessoPorcentagemRepasse = new HashMap();
		listaMostrarMenuPorcentagemRepasseProcesso = new HashMap();
		listaPorcentagemRepasseProcesso = new HashMap();
		id_AreaDistribuicao = "";
		areaDistribuicao = "";
		id_Serventia = "";
		serventia = "";
		id_ServentiaCargo = "";
		serventiaCargo = "";
		porcentagemRepasse = "";
		Id_Classificador = "";
		Classificador = "";
		id_ProcessoTipo = "";
		processoTipo = "";
		processoNumeroDependente = "";
		opcaoRedistribuicao = "1";
		RedistribuirTodosProcessoClassificado = false;
	}

	public List getListaProcessos() {
		return listaProcessos;
	}

	public void setListaProcessos(List listaProcessos) {
		this.listaProcessos = listaProcessos;
	}

	public void addListaProcessos(ProcessoDt processo) {
		
		if (listaProcessos.size()==0){
			listaProcessos.add(processo);
		}else{
			if (isListaProcesso(processo.getId()))
				return;				
			
			listaProcessos.add(processo);
		}
	}

	public String getId_AreaDistribuicao() {
		return id_AreaDistribuicao;
	}

	public void setId_AreaDistribuicao(String id_AreaDistribuicao) {
		if (id_AreaDistribuicao != null) if (id_AreaDistribuicao.equalsIgnoreCase("null")) {
			this.id_AreaDistribuicao = "";
			this.areaDistribuicao = "";
		} else if (!id_AreaDistribuicao.equalsIgnoreCase("")) this.id_AreaDistribuicao = id_AreaDistribuicao;
	}

	public String getId_Serventia() {
		return id_Serventia;
	}

	public void setId_Serventia(String id_Serventia) {
		if (id_Serventia != null) if (id_Serventia.equalsIgnoreCase("null")) {
			this.id_Serventia = "";
			this.serventia = "";
		} else if (!id_Serventia.equalsIgnoreCase("")) this.id_Serventia = id_Serventia;
	}
	
	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		if (serventia != null) if (serventia.equalsIgnoreCase("null")) this.serventia = "";
		else if (!serventia.equalsIgnoreCase("")) this.serventia = serventia;
	}
	
	public String getId_ServentiaCargo() {
		return id_ServentiaCargo;
	}

	public void setId_ServentiaCargo(String idServentiaCargo) {
		if (idServentiaCargo != null) if (idServentiaCargo.equalsIgnoreCase("null")) {
			this.id_ServentiaCargo = "";
			this.serventiaCargo = "";
		} else if (!idServentiaCargo.equalsIgnoreCase("")) this.id_ServentiaCargo = idServentiaCargo;
	}

	public String getServentiaCargo() {
		return serventiaCargo;
	}

	public void setServentiaCargo(String serventiaCargo) {
		if (serventiaCargo != null) if (serventiaCargo.equalsIgnoreCase("null")) this.serventiaCargo = "";
		else if (!serventiaCargo.equalsIgnoreCase("")) this.serventiaCargo = serventiaCargo;
	}

	public String getAreaDistribuicao() {
		return areaDistribuicao;
	}

	public void setAreaDistribuicao(String areaDistribuicao) {
		if (areaDistribuicao != null) if (areaDistribuicao.equalsIgnoreCase("null")) this.areaDistribuicao = "";
		else if (!areaDistribuicao.equalsIgnoreCase("")) this.areaDistribuicao = areaDistribuicao;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

	public String getPorcentagemRepasse() {
		return porcentagemRepasse;
	}

	public void setPorcentagemRepasse(String porcentagemRepasse) {
		if (porcentagemRepasse != null) this.porcentagemRepasse = porcentagemRepasse;
	}
	
	public String getId_Classificador()  {return Id_Classificador;}
	public void setId_Classificador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Classificador = ""; Classificador = "";}else if (!valor.equalsIgnoreCase("")) Id_Classificador = valor;}
	
	public String getClassificador()  {return Classificador;}
	public void setClassificador(String valor ) {if (valor!=null) Classificador = valor;}

	public boolean isListaProcesso(String id_processo) {
			
		for (int i=0;i <listaProcessos.size();i++){
			ProcessoDt dt = (ProcessoDt)listaProcessos.get(i);
			if(dt.getId().equalsIgnoreCase(id_processo))
				return true;				
		}
		
		return false;
		
	}

	public String getId_ProcessoTipo() {
		return id_ProcessoTipo;
	}

	public void setId_ProcessoTipo(String id_ProcessoTipo) {
		if (id_ProcessoTipo != null) if (id_ProcessoTipo.equalsIgnoreCase("null")) {
			this.id_ProcessoTipo = "";
			this.processoTipo = "";
		} else if (!id_ProcessoTipo.equalsIgnoreCase("")) this.id_ProcessoTipo = id_ProcessoTipo;
	}
	
	public String getProcessoTipo() {
		return processoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		if (processoTipo != null) if (processoTipo.equalsIgnoreCase("null")) this.processoTipo = "";
		else if (!processoTipo.equalsIgnoreCase("")) this.processoTipo = processoTipo;
	}

	public String getProcessoNumeroDependente() {
		return processoNumeroDependente;
	}

	public void setProcessoNumeroDependente(String processoNumeroDependente) {
		if (processoNumeroDependente != null && !processoNumeroDependente.equalsIgnoreCase("null"))
			this.processoNumeroDependente = processoNumeroDependente;
	}

	public String getOpcaoRedistribuicao() {
		return opcaoRedistribuicao;
	}

	public void setOpcaoRedistribuicao(String opcaoRedistribuicao) {
		if (opcaoRedistribuicao != null && !opcaoRedistribuicao.equalsIgnoreCase("null")) {
			this.opcaoRedistribuicao = opcaoRedistribuicao;
		}
	}
	
	public boolean isOpcaoRedistribuicao(String valor){
		boolean boRetorno = false;
		if (this.opcaoRedistribuicao!=null && this.opcaoRedistribuicao.equals(valor)){
			boRetorno = true;
		}
		return boRetorno;
	}

	public Map getListaIdProcessoPorcentagemRepasse() {
		return listaIdProcessoPorcentagemRepasse;
	}

	public void setListaIdProcessoPorcentagemRepasse(Map listaIdProcessoPorcentagemRepasse) {
		this.listaIdProcessoPorcentagemRepasse = listaIdProcessoPorcentagemRepasse;
	}

	public Map getListaMostrarMenuPorcentagemRepasseProcesso() {
		return listaMostrarMenuPorcentagemRepasseProcesso;
	}

	public void setListaMostrarMenuPorcentagemRepasseProcesso(Map listaMostrarMenuPorcentagemRepasseProcesso) {
		this.listaMostrarMenuPorcentagemRepasseProcesso = listaMostrarMenuPorcentagemRepasseProcesso;
	}
	
	public Map getListaPorcentagemRepasseProcesso() {
		return listaPorcentagemRepasseProcesso;
	}

	public void setListaPorcentagemRepasseProcesso(Map listaPorcentagemRepasseProcesso) {
		this.listaPorcentagemRepasseProcesso = listaPorcentagemRepasseProcesso;
	}

	public void setRedistribuirTodosProcessoClassificado(String valor) {
		RedistribuirTodosProcessoClassificado  = Funcoes.StringToBoolean(valor);
		
	}
	
	public boolean isRedistribuirTodosProcessosClassificados() {
		return RedistribuirTodosProcessoClassificado;
	}
}