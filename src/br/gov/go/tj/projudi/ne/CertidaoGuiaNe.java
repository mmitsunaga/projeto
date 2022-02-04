package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPraticaForenseDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.utils.Funcoes;

public class CertidaoGuiaNe extends Negocio {
		
	private static final long serialVersionUID = 8884790484530344139L;

	public CertidaoGuiaDt consultarCertidaoProjudi(String numeroGuiaCompleto) throws Exception {
		CertidaoGuiaDt certidaoGuiaDt = new CertidaoGuiaDt();
    	GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
    	
    	String metadados = guiaEmissaoNe.consultaMetadadosGuia(numeroGuiaCompleto);
    	
    	if(metadados != null){    		
    		JSONObject jsonObject = new JSONObject(metadados);
    		
    		if(jsonObject.has("Id_Processo")){
    			
    			if(!(jsonObject.getString("Id_Processo").isEmpty()) && (jsonObject.getString("Id_Processo") != null)){
    				
    				certidaoGuiaDt = consultarDadosProcesso(jsonObject.getString("Id_Processo"));
    				
    				if (certidaoGuiaDt == null) {
    					certidaoGuiaDt = new CertidaoGuiaDt();
    				}
    			}
    		}
    		
    		GuiaEmissaoDt guiaEmissaoDt = this.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto);
        	
        	certidaoGuiaDt.setGuiaEmissaoDt(guiaEmissaoDt);
    		
    		// Identificação do Requerente
    		if(jsonObject.has("Nome")){ certidaoGuiaDt.setNome(jsonObject.getString("Nome"));}
    		if(jsonObject.has("Cpf")){ certidaoGuiaDt.setCpf(jsonObject.getString("Cpf"));}
    		if(jsonObject.has("TipoPessoa")){ certidaoGuiaDt.setTipoPessoa(jsonObject.getString("TipoPessoa"));}
    		if(jsonObject.has("Id_Naturalidade")){ certidaoGuiaDt.setId_Naturalidade(jsonObject.getString("Id_Naturalidade"));}
    		if(jsonObject.has("Naturalidade")){ certidaoGuiaDt.setNaturalidade(jsonObject.getString("Naturalidade"));}
    		if(jsonObject.has("Sexo")){ certidaoGuiaDt.setSexo(jsonObject.getString("Sexo"));}
    		if(jsonObject.has("DataNascimento")){ certidaoGuiaDt.setDataNascimento(jsonObject.getString("DataNascimento"));}
    		if(jsonObject.has("Rg")){ certidaoGuiaDt.setRg(jsonObject.getString("Rg"));}
    		if(jsonObject.has("Id_RgOrgaoExpedidor")){ certidaoGuiaDt.setId_RgOrgaoExpedidor(jsonObject.getString("Id_RgOrgaoExpedidor"));}
    		if(jsonObject.has("RgOrgaoExpedidor")){ certidaoGuiaDt.setRgOrgaoExpedidor(jsonObject.getString("RgOrgaoExpedidor"));}
    		if(jsonObject.has("RgOrgaoExpedidorSigla")){ certidaoGuiaDt.setRgOrgaoExpedidorSigla(jsonObject.getString("RgOrgaoExpedidorSigla"));}
    		if(jsonObject.has("RgDataExpedicao")){ certidaoGuiaDt.setRgDataExpedicao(jsonObject.getString("RgDataExpedicao"));}
    		if(jsonObject.has("Id_EstadoCivil")){ certidaoGuiaDt.setId_EstadoCivil(jsonObject.getString("Id_EstadoCivil"));}
    		if(jsonObject.has("EstadoCivil")){ certidaoGuiaDt.setEstadoCivil(jsonObject.getString("EstadoCivil"));}
    		if(jsonObject.has("Id_Profissao")){ certidaoGuiaDt.setId_Profissao(jsonObject.getString("Id_Profissao"));}
    		if(jsonObject.has("Profissao")){ certidaoGuiaDt.setProfissao(jsonObject.getString("Profissao"));}
    		if(jsonObject.has("Domicilio")){ certidaoGuiaDt.setDomicilio(jsonObject.getString("Domicilio"));}
    		
    		// Certidão de Interdição
    		if(jsonObject.has("NomePai")){ certidaoGuiaDt.setNomePai(jsonObject.getString("NomePai"));}
    		if(jsonObject.has("NomeMae")){ certidaoGuiaDt.setNomeMae(jsonObject.getString("NomeMae"));}
    		if(jsonObject.has("RegistroGeral")){ certidaoGuiaDt.setRegistroGeral(jsonObject.getString("RegistroGeral"));}
    		if(jsonObject.has("Patente")){ certidaoGuiaDt.setPatente(jsonObject.getString("Patente"));}
    		if(jsonObject.has("Id_Nacionalidade")){ certidaoGuiaDt.setId_Nacionalidade(jsonObject.getString("Id_Nacionalidade"));}
    		if(jsonObject.has("Nacionalidade")){ certidaoGuiaDt.setNacionalidade(jsonObject.getString("Nacionalidade"));}

    		if(jsonObject.has("NumeroGuiaCompleto")){ certidaoGuiaDt.setNumeroGuia(jsonObject.getString("NumeroGuiaCompleto"));}
    		if(jsonObject.has("Id_Comarca")){ certidaoGuiaDt.setId_Comarca(jsonObject.getString("Id_Comarca"));}
    		if(jsonObject.has("Id_Modelo")){ certidaoGuiaDt.setId_Modelo(jsonObject.getString("Id_Modelo"));}
    		
    		if(jsonObject.has("Id_Guia")){ certidaoGuiaDt.setId_Guia(jsonObject.getString("Id_Guia"));}
    		if(jsonObject.has("Id_GuiaTipo")){ certidaoGuiaDt.setId_GuiaTipo(jsonObject.getString("Id_GuiaTipo"));}
    		
    		if(jsonObject.has("Fase")){ certidaoGuiaDt.setFase(jsonObject.getString("Fase"));}
    		if(jsonObject.has("DataSentenca")){ certidaoGuiaDt.setDataSentenca(jsonObject.getString("DataSentenca"));}
    		if(jsonObject.has("DataBaixa")){ certidaoGuiaDt.setDataBaixa(jsonObject.getString("DataBaixa"));}
    		
    		// Certidão de Prática Forense
    		if(jsonObject.has("Oab")){ certidaoGuiaDt.setOab(jsonObject.getString("Oab"));}
    		if(jsonObject.has("OabComplemento")){ certidaoGuiaDt.setOabComplemento(jsonObject.getString("OabComplemento"));}
    		if(jsonObject.has("OabUf")){ certidaoGuiaDt.setOabUf(jsonObject.getString("OabUf"));}
    		if(jsonObject.has("OabUfCodigo")){ certidaoGuiaDt.setOabUfCodigo(jsonObject.getString("OabUfCodigo"));}
    		if(jsonObject.has("TextoForense")){ certidaoGuiaDt.setTextoForense(jsonObject.getString("TextoForense"));}
    		if(jsonObject.has("AnoInicial")){ certidaoGuiaDt.setAnoInicial(jsonObject.getString("AnoInicial"));}
    		if(jsonObject.has("AnoFinal")){ certidaoGuiaDt.setAnoFinal(jsonObject.getString("AnoFinal"));}
    		if(jsonObject.has("MesInicial")){ certidaoGuiaDt.setMesInicial(jsonObject.getString("MesInicial"));}
    		if(jsonObject.has("MesFinal")){ certidaoGuiaDt.setMesFinal(jsonObject.getString("MesFinal"));}
//    		if(jsonObject.has("ListaProcesso")){ certidaoGuiaDt.setListaProcesso((List<String>) jsonObject.get("ListaProcesso"));}
//    		System.out.println(jsonObject.get("ListaProcesso"));
    		if(jsonObject.has("Quantidade")){ certidaoGuiaDt.setQuantidade(jsonObject.getInt("Quantidade"));}
    		if(jsonObject.has("ModeloCodigo")){ certidaoGuiaDt.setModeloCodigo(jsonObject.getInt("ModeloCodigo"));}
    		if(jsonObject.has("Tipo")){ certidaoGuiaDt.setTipo(jsonObject.getString("Tipo"));}
    		if(jsonObject.has("InfoArea")) { 
    			certidaoGuiaDt.setInfoArea(jsonObject.getString("InfoArea"));
    		} else {
    			if (guiaEmissaoDt.isAreaCivil()) {
    				certidaoGuiaDt.setInfoArea(AreaDt.CIVEL);
    			} else if (guiaEmissaoDt.isAreaCriminal()) {
    				certidaoGuiaDt.setInfoArea(AreaDt.CRIMINAL);
    			} else {
    				certidaoGuiaDt.setInfoArea("3");
    			}
    		}
    		
    		if(jsonObject.has("CustaCertidao")){ certidaoGuiaDt.setCustaCertidao(jsonObject.getString("CustaCertidao"));}
    		if(jsonObject.has("CustaTaxaJudiciaria")){ certidaoGuiaDt.setCustaTaxaJudiciaria(jsonObject.getString("CustaTaxaJudiciaria"));}
    		if(jsonObject.has("CustaTotal")){ certidaoGuiaDt.setCustaTotal(jsonObject.getString("CustaTotal"));}
    		
    		if (Funcoes.StringToDouble(certidaoGuiaDt.getCustaTotal()) == 0) {
    			certidaoGuiaDt.setCustaTotal(String.valueOf(Funcoes.StringToDouble(certidaoGuiaDt.getCustaCertidao()) + Funcoes.StringToDouble(certidaoGuiaDt.getCustaTaxaJudiciaria())));
    		} 
    	}
    	
    	return certidaoGuiaDt;
	}
	
	/**
	 * Verificar se a guia está paga.
	 * 
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaPaga(String numeroGuiaCompleto) throws Exception {
		
		boolean retorno = new GuiaEmissaoNe().isGuiaPaga(numeroGuiaCompleto);
		
		return retorno;
	}
	
	public CertidaoGuiaDt consultarDadosProcesso(String idProcesso) throws Exception {
		
		CertidaoGuiaDt certidaoGuiaDt = new CertidaoGuiaDt();
	    
		ProcessoDt processoDt = new ProcessoDt();    				
		processoDt = new ProcessoNe().consultarIdCompleto(idProcesso);
		
		certidaoGuiaDt.setProtocolo(processoDt.getProcessoNumeroCompleto());
		certidaoGuiaDt.setJuizo(processoDt.getServentia());
		certidaoGuiaDt.setNatureza(processoDt.getProcessoTipo());
		certidaoGuiaDt.setValorAcao(processoDt.getValor());
		certidaoGuiaDt.setIdProcesso(processoDt.getId());
		
		List listaRequerentes = new ArrayList();
		List listaProcessoParteAdvogado = new ArrayList();
		List listaAdvogados = new ArrayList();		
		List liTemp = new ArrayList();
		
		if(processoDt.getListaPolosAtivos() != null){
			liTemp = processoDt.getListaPolosAtivos();
		}
		
		if(liTemp != null){
			for(int i = 0; i < liTemp.size(); i++){
				
				ProcessoParteDt parte = (ProcessoParteDt) liTemp.get(i);
				
				if(parte.getNome() != null){
					listaRequerentes.add(parte.getNome().toString());				
				}
				
				if(parte.getId_ProcessoParte() != null){
					ProcessoParteAdvogadoNe processoParteAdvogadoNe = new ProcessoParteAdvogadoNe();
					try {
						listaProcessoParteAdvogado.addAll(processoParteAdvogadoNe.consultarAdvogadosParte(parte.getId_ProcessoParte())); // CORRIGIR ERRO DO ADVOGADO DO REQUERENTE
						for(int j=0; j < listaProcessoParteAdvogado.size(); j++){
							ProcessoParteAdvogadoDt processoParteAdvogadoDt = new ProcessoParteAdvogadoDt();
							processoParteAdvogadoDt = (ProcessoParteAdvogadoDt) listaProcessoParteAdvogado.get(j);
							listaAdvogados.add(processoParteAdvogadoDt.getNomeAdvogado());
						}
						
					} catch (Exception e) {}
				}
			}
		}
		
		certidaoGuiaDt.setRequerenteProcesso(listaRequerentes.toString().replace("[", "").replace("]", ""));
		certidaoGuiaDt.setAdvogadoRequerente(listaAdvogados.toString().replace("[", "").replace("]", ""));
		
		List listaRequeridos = new ArrayList();
		List liTemp2 = processoDt.getListaPolosPassivos();
		
		for(int i = 0;liTemp2!=null && i < liTemp2.size(); i++){
			
			ProcessoParteDt parte = (ProcessoParteDt) liTemp2.get(i);
			
			if(parte.getNome() != null){
				listaRequeridos.add(parte.getNome().toString());				
			}
		}
		certidaoGuiaDt.setRequerido(listaRequeridos.toString().replace("[", "").replace("]", ""));

		return certidaoGuiaDt;
	}
	
	public ProcessoDt consultarProcessoId(String id_processo ) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();
		return processoNe.consultarId(id_processo);
	}
	
	public ServentiaDt consultarServentiaId(String id_serventia ) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe();
		return serventiaNe.consultarId(id_serventia);
	}
	
	public CertidaoValidacaoDt consultarCertidaoValidacaoNumeroGuia(String numeroGuiaCompleto) throws Exception {
		CertidaoNe certidaoNe = new CertidaoNe();
		return certidaoNe.consultarNumeroGuia(numeroGuiaCompleto);
	}
	
	public byte[] gerarPdfPublicacao(String diretorioProjeto, CertidaoValidacaoDt certidaoValidacao) throws Exception {
		CertidaoNe certidaoNe = new CertidaoNe();
		return certidaoNe.gerarPdfPublicacao(diretorioProjeto, certidaoValidacao);
	}
	
	public void salvarCertidaoValidacao(CertidaoValidacaoDt certidaoValidacao) throws Exception {
		CertidaoNe certidaoNe = new CertidaoNe();
		certidaoNe.salvar(certidaoValidacao);
	}
	
	public String montaConteudoPorModeloPF(CertidaoGuiaDt certidaoGuiaDt, UsuarioDt servidor, String id_modelo, ServentiaDt serventiaDt) throws Exception {
		ModeloNe modeloNe = new ModeloNe();
		return modeloNe.montaConteudoPorModeloPF(certidaoGuiaDt, servidor, id_modelo, serventiaDt);
	}
	
	public String montaConteudoPorModelo(CertidaoGuiaDt certidao, UsuarioDt servidor, int modeloCodigo) throws Exception {
		ModeloNe modeloNe = new ModeloNe();
		return modeloNe.montaConteudoPorModelo(certidao, servidor, modeloCodigo);
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoNumeroGuia(String numeroGuiaCompleto) throws Exception {
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		return guiaEmissaoNe.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto);
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoSPG(String numeroCompletoGuia) throws Exception {
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		return guiaSPGNe.consultarGuiaEmissaoSPG(numeroCompletoGuia);
	}
	
	public EstadoDt buscaEstado(String ufEstado) throws Exception {
		EstadoNe estadoNe = new EstadoNe();
		return estadoNe.buscaEstado(ufEstado);
	}
	
	public String consultarStringMovimentacoesProcesso(String id_Processo) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();
				
		// INSERÇÃO DAS MOVIMENTAÇÕES DO PROCESSO COM A DATA DO OCORRIDO E O ANDAMENTO/TIPO DE MOVIMENTAÇÃO (SEPARADO POR ; E ESPAÇO)								
		List<MovimentacaoDt> listaMovimentacoes = new ArrayList<MovimentacaoDt>();
		listaMovimentacoes = processoNe.consultarMovimentacoesProcesso(id_Processo);
		
		String strMovimentacoes = "";
		MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
		
		if(!listaMovimentacoes.isEmpty()){
			for(int i = 0; i < listaMovimentacoes.size(); i++){
				movimentacaoDt = (MovimentacaoDt) listaMovimentacoes.get(i);
				if(!movimentacaoDt.getComplemento().isEmpty()){
					strMovimentacoes += "Em " + movimentacaoDt.getDataRealizacao() + ", " + movimentacaoDt.getMovimentacaoTipo() + " - " + movimentacaoDt.getComplemento();					
				} else {
					strMovimentacoes += "Em " + movimentacaoDt.getDataRealizacao() + ", " + movimentacaoDt.getMovimentacaoTipo();
				}
				if(i == listaMovimentacoes.size() - 1){
					strMovimentacoes += ".";
				} else {
					strMovimentacoes += ";  ";
				}
			}
		}						
		return strMovimentacoes;
	}
	
	public int consultarQuantidadeProcessosProjudiPraticaForenseAdvogado(String oabNumero, String oabComplemento, String id_estadoOABUF, String dataInicial, String dataFinal, String id_Comarca, String id_areaServentiaLogada) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();
		return processoNe.consultarQuantidadeProcessosProjudiPraticaForenseAdvogado(oabNumero, oabComplemento, id_estadoOABUF, dataInicial, dataFinal, id_Comarca, id_areaServentiaLogada);
	}		
	
	public int consultarQuantidadeProcessosSPGPraticaForenseAdvogado(CertidaoGuiaDt certidaoGuiaDt) throws Exception {
		return certidaoGuiaDt.getQuantidadeSPG();
	}
	
	public List<ProcessoCertidaoPraticaForenseDt> consultarProcessosProjudiPraticaForenseAdvogado(String oabNumero, String oabComplemento, String id_estadoOABUF, String dataInicial, String dataFinal, String id_Comarca, String id_areaServentiaLogada) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();
		return processoNe.consultarProcessosProjudiPraticaForenseAdvogado(oabNumero, oabComplemento, id_estadoOABUF, dataInicial, dataFinal, id_Comarca, id_areaServentiaLogada);		
	}
	
	public List<ProcessoCertidaoPraticaForenseDt> consultarProcessosSPGPraticaForenseAdvogado(CertidaoGuiaDt certidaoGuiaDt) throws Exception {
		if (certidaoGuiaDt == null || 
			certidaoGuiaDt.getGuiaEmissaoDt() == null || 
			!GuiaTipoDt.isGuiaCertidaoPraticaForense(certidaoGuiaDt.getGuiaEmissaoDt()) ||
			certidaoGuiaDt.getTextoRetornoSPG() == null ||
			certidaoGuiaDt.getTextoRetornoSPG().length() == 0) {
			return new ArrayList<>();
		}
		CertidaoNe certidaoNe = new CertidaoNe();
		return certidaoNe.consultarProcessosSPGPraticaForenseAdvogado(certidaoGuiaDt);		
	}
	
	public CertidaoGuiaDt consultarCertidaoSPG(String numeroCompletoGuia, ServentiaDt serventiaDt) throws Exception {
		CertidaoGuiaDt certidaoGuiaDt = new CertidaoGuiaDt();
		
		GuiaEmissaoDt guiaEmissaoSPG = this.consultarGuiaEmissaoSPG(numeroCompletoGuia);
		
		if (guiaEmissaoSPG != null) {
			certidaoGuiaDt.setGuiaEmissaoDt(guiaEmissaoSPG);
			certidaoGuiaDt.setNumeroGuia(numeroCompletoGuia);
			
			if (!certidaoGuiaDt.isGuiaJaUtilizada() && GuiaTipoDt.isGuiaCertidaoPraticaForense(guiaEmissaoSPG)) {
				this.preenchaDadosCertidaoPraticaForenseSPG(certidaoGuiaDt, serventiaDt);
			}
		}		
		
    	return certidaoGuiaDt;
	}
	
	public void preenchaDadosCertidaoPraticaForenseSPG(CertidaoGuiaDt certidaoGuiaDt, ServentiaDt serventiaDt) throws Exception {
		CertidaoNe certidaoNe = new CertidaoNe();
		certidaoNe.preenchaDadosCertidaoPraticaForenseSPG(certidaoGuiaDt, serventiaDt);
	}
	
	public String consultarUsuariosServentiaJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		String stTemp = "";
		
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		stTemp = usuarioServentiaNe.consultarUsuariosServentiaTodosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);			
		usuarioServentiaNe = null;
				
		return stTemp;
	}
}