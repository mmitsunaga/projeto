package br.gov.go.tj.projudi.ne;

import java.util.List;

import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GuiaCertidaoGeralDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.ModeloDt;

public class GuiaCertidaoGeralNe extends GuiaEmissaoNe {
		
	private static final long serialVersionUID = -2597359315691167151L;

	public String consultarDescricaoCidadeJSON(String descricao, String uf, String posicao) throws Exception {
		
		String stTemp = "";
		CidadeNe Naturalidadene = new CidadeNe();
		stTemp = Naturalidadene.consultarDescricaoJSON(descricao, uf, posicao);
		return stTemp;
	}
	
	public String consultarDescricaoPaisJSON(String descricao, String posicao) throws Exception {
		
		String stTemp = "";
		PaisNe NacionalidadeNe = new PaisNe();
		stTemp = NacionalidadeNe.consultarDescricaoJSON(descricao, posicao);
		return stTemp;
	}
	
	public String consultarDescricaoRgOrgaoExpedidorJSON(String sigla, String descricao, String posicao) throws Exception {
		
		String tempList = null;
		RgOrgaoExpedidorNe rgOrgaoExpedidorne = new RgOrgaoExpedidorNe();
		tempList = rgOrgaoExpedidorne.consultarDescricaoJSON(sigla, descricao, posicao);
		QuantidadePaginas = rgOrgaoExpedidorne.getQuantidadePaginas();
		rgOrgaoExpedidorne = null;
		return tempList;
	}
	
	public String consultarDescricaoProfissaoJSON(String descricao, String posicao) throws Exception {
		
		String stTemp = "";
		ProfissaoNe Profissaone = new ProfissaoNe();
		stTemp = Profissaone.consultarDescricaoJSON(descricao, posicao);
		return stTemp;
	}
	
	public String consultarDescricaoEstadoCivilJSON(String descricao, String posicao) throws Exception {
		
		String stTemp = "";
		EstadoCivilNe EstadoCivilne = new EstadoCivilNe();
		stTemp = EstadoCivilne.consultarDescricaoJSON(descricao, posicao);
		return stTemp;
	}
	
	public String consultarDescricaoComarcaJSON(String descricao, String posicao) throws Exception {
		
		String stTemp = "";
		ComarcaNe Comarcane = new ComarcaNe();
		stTemp = Comarcane.consultarDescricaoJSON(descricao, posicao);
		return stTemp;
	}

	public String consultarServentiasDistribuidorAtivoJSON(String descricao, String posicao, String idComarca) throws Exception {
		
		String stTemp = "";
		ServentiaNe Serventiane = new ServentiaNe();
		stTemp = Serventiane.consultarServentiasDistribuidorAtivoJSON(descricao, posicao, idComarca);
		return stTemp;
	}
	
	public void salvar(GuiaEmissaoDt guiaEmissaoDt, GuiaCertidaoGeralDt guiaCertidaoGeralDt, List<GuiaItemDt> listaGuiaItemDt, boolean gerarPendencia, String idUsuarioServentia, String modeloCodigo) throws Exception {
		
		if( guiaEmissaoDt != null && guiaCertidaoGeralDt != null ) {
			guiaEmissaoDt.setMetadadosGuia( this.getJsonCertidao(guiaCertidaoGeralDt, guiaEmissaoDt, modeloCodigo) );
		}
		
		this.salvar(guiaEmissaoDt, listaGuiaItemDt, gerarPendencia, idUsuarioServentia);
	}
	
	public String getJsonCertidao(GuiaCertidaoGeralDt guiaCertidaoGeralDt, GuiaEmissaoDt guiaEmissaoDt, String modeloCodigo) throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		String retorno = "";
		
		ComarcaDt comarcaDt = new ComarcaNe().consultarComarcaCodigo(guiaEmissaoDt.getComarcaCodigo());
		String id_Comarca = "";
		if(comarcaDt != null){
			if(comarcaDt.getId() != null && !comarcaDt.getId().isEmpty()){
				id_Comarca = comarcaDt.getId();			
			}			
		}
		
		ModeloDt modeloDt = new ModeloNe().consultarModeloCodigo(modeloCodigo);
		String id_Modelo = "";
		if(modeloDt.getId() != null && !modeloDt.getId().isEmpty()){
			id_Modelo = modeloDt.getId();			
		}
		
		jsonObject.put("Id_Comarca", id_Comarca);
		jsonObject.put("Id_Modelo", id_Modelo);
		
		jsonObject.put("NumeroGuiaCompleto", guiaEmissaoDt.getNumeroGuiaCompleto());
		jsonObject.put("Id_Guia", guiaEmissaoDt.getId());
		jsonObject.put("Id_GuiaTipo", guiaEmissaoDt.getId_GuiaTipo());
		if(guiaEmissaoDt.getId_Processo() != null){
			jsonObject.put("Id_Processo", guiaEmissaoDt.getId_Processo());			
		} else {
			jsonObject.put("Id_Processo", "");
		}
		
		// Dados do Requerente da Guia
		jsonObject.put("Nome", guiaCertidaoGeralDt.getNome());
		jsonObject.put("Cpf", guiaCertidaoGeralDt.getCpf());
		jsonObject.put("TipoPessoa", guiaCertidaoGeralDt.getTipoPessoa());
		jsonObject.put("Id_Naturalidade", guiaCertidaoGeralDt.getId_Naturalidade());
		jsonObject.put("Naturalidade", guiaCertidaoGeralDt.getNaturalidade());
		jsonObject.put("Sexo", guiaCertidaoGeralDt.getSexo());
		jsonObject.put("DataNascimento", guiaCertidaoGeralDt.getDataNascimento());
		jsonObject.put("Rg", guiaCertidaoGeralDt.getRg());
		jsonObject.put("Id_RgOrgaoExpedidor", guiaCertidaoGeralDt.getId_RgOrgaoExpedidor());
		jsonObject.put("RgOrgaoExpedidor", guiaCertidaoGeralDt.getRgOrgaoExpedidor());
		jsonObject.put("RgOrgaoExpedidorSigla", guiaCertidaoGeralDt.getRgOrgaoExpedidorSigla());
		jsonObject.put("RgDataExpedicao", guiaCertidaoGeralDt.getRgDataExpedicao());
		jsonObject.put("Id_EstadoCivil", guiaCertidaoGeralDt.getId_EstadoCivil());
		jsonObject.put("EstadoCivil", guiaCertidaoGeralDt.getEstadoCivil());
		jsonObject.put("Id_Profissao", guiaCertidaoGeralDt.getId_Profissao());
		jsonObject.put("Profissao", guiaCertidaoGeralDt.getProfissao());
		jsonObject.put("Domicilio", guiaCertidaoGeralDt.getDomicilio());
		
		// Certidão de Interdição
		jsonObject.put("NomePai", guiaCertidaoGeralDt.getNomePai());
		jsonObject.put("NomeMae", guiaCertidaoGeralDt.getNomeMae());
		jsonObject.put("RegistroGeral", guiaCertidaoGeralDt.getRegistroGeral());
		jsonObject.put("Patente", guiaCertidaoGeralDt.getPatente());
		jsonObject.put("Id_Nacionalidade", guiaCertidaoGeralDt.getId_Nacionalidade());
		jsonObject.put("Nacionalidade", guiaCertidaoGeralDt.getNacionalidade());

//		jsonObject.put("NumeroGuia", guiaCertidaoGeralDt);
//		jsonObject.put("Id_Comarca", guiaCertidaoGeralDt);
//		jsonObject.put("Id_Modelo", guiaCertidaoGeralDt);
//
//		jsonObject.put("Id_Guia", guiaCertidaoGeralDt);
//		jsonObject.put("Id_GuiaTipo", guiaCertidaoGeralDt);
//
//		jsonObject.put("Protocolo", guiaCertidaoGeralDt);
//		jsonObject.put("Juizo", guiaCertidaoGeralDt);
//		jsonObject.put("Natureza", guiaCertidaoGeralDt);
//		jsonObject.put("ValorAcao", guiaCertidaoGeralDt);
//		jsonObject.put("RequerenteProcesso", guiaCertidaoGeralDt);
//		jsonObject.put("AdvogadoRequerente", guiaCertidaoGeralDt);
//		jsonObject.put("Requerido", guiaCertidaoGeralDt);
//
//		jsonObject.put("Fase", guiaCertidaoGeralDt);
//		jsonObject.put("DataSentenca", guiaCertidaoGeralDt);
//		jsonObject.put("DataBaixa", guiaCertidaoGeralDt);

		// Certidão de Prática Forense
		jsonObject.put("Oab", guiaCertidaoGeralDt.getOab());
		jsonObject.put("OabComplemento", guiaCertidaoGeralDt.getOabComplemento());
		jsonObject.put("OabUf", guiaCertidaoGeralDt.getOabUf());
		jsonObject.put("OabUfCodigo", guiaCertidaoGeralDt.getOabUfCodigo());
		jsonObject.put("TextoForense", guiaCertidaoGeralDt.getTextoForense());
		jsonObject.put("AnoInicial", guiaCertidaoGeralDt.getAnoInicial());
		jsonObject.put("AnoFinal", guiaCertidaoGeralDt.getAnoFinal());
		jsonObject.put("MesInicial", guiaCertidaoGeralDt.getMesInicial());
		jsonObject.put("MesFinal", guiaCertidaoGeralDt.getMesFinal());
		jsonObject.put("ListaProcesso", guiaCertidaoGeralDt.getListaProcesso());
		jsonObject.put("Quantidade", guiaCertidaoGeralDt.getQuantidade());
		jsonObject.put("ModeloCodigo", guiaCertidaoGeralDt.getModeloCodigo());
		jsonObject.put("Tipo", guiaCertidaoGeralDt.getTipo());
		
		jsonObject.put("Id_Comarca", guiaCertidaoGeralDt.getId_Comarca());
		jsonObject.put("Comarca", guiaCertidaoGeralDt.getComarca());
		jsonObject.put("Id_Serventia", guiaCertidaoGeralDt.getId_Serventia());
		jsonObject.put("Serventia", guiaCertidaoGeralDt.getServentia());
		
		jsonObject.put("CustaCertidao", guiaCertidaoGeralDt.getCustaCertidao());
		jsonObject.put("CustaTaxaJudiciaria", guiaCertidaoGeralDt.getCustaTaxaJudiciaria());
		jsonObject.put("CustaTotal", guiaCertidaoGeralDt.getCustaTotal());
		
		if( jsonObject != null )
			retorno = jsonObject.toString();
		
		return retorno;
	}
}