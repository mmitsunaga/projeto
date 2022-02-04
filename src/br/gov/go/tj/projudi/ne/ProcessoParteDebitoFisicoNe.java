package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PoloSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoFisicoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoSPGDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ps.ProcessoDebitoPs;
import br.gov.go.tj.projudi.ps.ProcessoParteDebitoFisicoPs;
import br.gov.go.tj.projudi.ps.ProcessoParteDebitoPs;
import br.gov.go.tj.projudi.ps.ProcessoSPGPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ProcessoParteDebitoFisicoNe extends ProcessoParteDebitoFisicoNeGen {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6048892638761583351L;

	public String Verificar(ProcessoParteDebitoFisicoDt dados) {
		String stRetorno = "";

		if (dados.getId_ProcessoParte() == null || dados.getId_ProcessoParte().trim().length() == 0 || dados.getNome() == null || dados.getNome().length() == 0) stRetorno += "Selecione uma Parte para o Débito. \n";
		if (dados.getId_ProcessoDebito() == null || dados.getId_ProcessoDebito().trim().length() == 0) stRetorno += "Selecione o Tipo de Débito. \n ";
		if (dados.getDescricaoServentia() == null || dados.getCodigoExternoServentia() == null || dados.getDescricaoServentia().trim().length() == 0 || dados.getCodigoExternoServentia().trim().length() == 0) stRetorno += "Processo SPG não possui serventia. \n";
		//if (dados.getStatus() == null || dados.getStatus().trim().length() == 0) stRetorno += "Selecione o Status do Débito. \n ";
		//if (dados.isBaixado() && (dados.getDataBaixa() == null || dados.getDataBaixa().length() == 0)) stRetorno += "Informe a Data Baixa do Débito. \n ";
		
		if (stRetorno.length() == 0) {
			List listaPartes = dados.getListaPartesPromoventes();
			if(listaPartes != null && !listaPartes.isEmpty()){
				for (int i=0;i < listaPartes.size();i++){
					PoloSPGDt parteDt = (PoloSPGDt)listaPartes.get(i);
					if (parteDt.getId().equals(dados.getId_ProcessoParte())) {
						dados.setCpfParte(parteDt.getCpf());
						dados.setNome(parteDt.getNome());
						dados.setTipoParte(String.valueOf(ProcessoSPGPs.PROMOVENTE_CODIGO_SPG_SSG));
						break;
					}
 				}
			}	
			
			if (dados.getCpfParte() == null || dados.getCpfParte().trim().length() == 0) {
				listaPartes = dados.getListaPartesPromovidas();
				if(listaPartes != null && !listaPartes.isEmpty()){
					for (int i=0;i < listaPartes.size();i++){
						PoloSPGDt parteDt = (PoloSPGDt)listaPartes.get(i);
						if (parteDt.getId().equals(dados.getId_ProcessoParte())) {
							dados.setCpfParte(parteDt.getCpf());
							dados.setNome(parteDt.getNome());
							dados.setTipoParte(String.valueOf(ProcessoSPGPs.PROMOVIDO_CODIGO_SPG_SSG));
							break;
						}								   				
	 				}
				}
			}
			
			if (dados.getCpfParte() == null || dados.getCpfParte().trim().length() == 0) stRetorno += "Não foi localizado o CPF da parte no SPG, essa é uma informação obrigatória. \n ";
			
			if (dados.getTipoParte() == null || dados.getTipoParte().trim().length() == 0) stRetorno += "Não foi localizado o Tipo da parte no SPG, essa é uma informação obrigatória. \n ";
		}
		
		return stRetorno;

	}
	
	public String VerificarCadastroServentia(ProcessoParteDebitoFisicoDt dados, UsuarioNe usuarioSessao) {
		String stRetorno = "";

		if (dados.getDividaSolidaria() == null || dados.getDividaSolidaria().length() == 0) stRetorno += "Selecione uma opção para Dívida Solidária. \n";
		if (dados.getTipoParte() == null || dados.getTipoParte().length() == 0) stRetorno += "Selecione uma opção para Tipo de Parte. \n";
		if (dados.NotIsDividaSolidaria() && (dados.getId_ProcessoParte() == null || dados.getId_ProcessoParte().length() == 0 || dados.getNome() == null || dados.getNome().length() == 0)) stRetorno += "Selecione uma Parte para o Débito. \n";
		if (dados.getId_ProcessoDebito() == null || dados.getId_ProcessoDebito().length() == 0) stRetorno += "Selecione o Tipo de Débito. \n ";
		if (dados.getNumeroGuia() == null || dados.getNumeroGuia().length() == 0) stRetorno += "Selecione uma Guia Final. \n ";
		
		if (stRetorno.trim().length() == 0) {
			if (!usuarioSessao.isAnalistaFinanceiro()) {
				if (Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) != ProcessoDebitoStatusDt.NENHUM &&
					Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) != ProcessoDebitoStatusDt.NOVO &&
					Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) != ProcessoDebitoStatusDt.DEVOLVIDO_PELO_FINANCEIRO) {
					stRetorno += "Somente status novo e devolvido pelo financeiro poderá ser atualizado pela serventia. \n ";
				}
			}
		}
		
		if (stRetorno.length() == 0) {
			List listaPartes = dados.getListaPartesPromoventes();
			if(listaPartes != null && !listaPartes.isEmpty()){
				for (int i=0;i < listaPartes.size();i++){
					PoloSPGDt parteDt = (PoloSPGDt)listaPartes.get(i);
					if (parteDt.getId().equals(dados.getId_ProcessoParte())) {
						dados.setCpfParte(parteDt.getCpf());
						dados.setNome(parteDt.getNome());
						dados.setTipoParte(String.valueOf(ProcessoSPGPs.PROMOVENTE_CODIGO_SPG_SSG));
						break;
					}
 				}
			}	
			
			if (dados.getCpfParte() == null || dados.getCpfParte().trim().length() == 0) {
				listaPartes = dados.getListaPartesPromovidas();
				if(listaPartes != null && !listaPartes.isEmpty()){
					for (int i=0;i < listaPartes.size();i++){
						PoloSPGDt parteDt = (PoloSPGDt)listaPartes.get(i);
						if (parteDt.getId().equals(dados.getId_ProcessoParte())) {
							dados.setCpfParte(parteDt.getCpf());
							dados.setNome(parteDt.getNome());
							dados.setTipoParte(String.valueOf(ProcessoSPGPs.PROMOVIDO_CODIGO_SPG_SSG));
							break;
						}								   				
	 				}
				}
			}
			
			if (dados.getCpfParte() == null || dados.getCpfParte().trim().length() == 0) stRetorno += "Não foi localizado o CPF da parte no SPG, essa é uma informação obrigatória. \n ";
			
			if (dados.getTipoParte() == null || dados.getTipoParte().trim().length() == 0) stRetorno += "Não foi localizado o Tipo da parte no SPG, essa é uma informação obrigatória. \n ";
		}
		
		if (stRetorno.length() == 0 && dados.getId() != null && dados.getId().trim().length() > 0) {
			if (dados.getListaPartesComDebito() == null) dados.setListaPartesComDebito(new ArrayList());
			
			List listaPartesPoloAtivoPassivo;
			if (dados.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoSPGPs.PROMOVENTE_CODIGO_SPG_SSG))) {
				listaPartesPoloAtivoPassivo = dados.getListaPartesPromoventes();
			} else {
				listaPartesPoloAtivoPassivo = dados.getListaPartesPromovidas();
			}				
			
			for (Object parteObj : listaPartesPoloAtivoPassivo) {
				PoloSPGDt parteDt = (PoloSPGDt)parteObj;
				
				boolean possuiDebito = false;
				for (Object processoParteObj : dados.getListaPartesComDebito()) {
					ProcessoParteDebitoFisicoDt processoParteDt = (ProcessoParteDebitoFisicoDt)processoParteObj;
					
					if (processoParteDt.getId_ProcessoParte() != null && parteDt.getId() != null &&
					    processoParteDt.getNumeroGuia() != null && dados.getNumeroGuia() != null &&
					    processoParteDt.getId_ProcessoParte().equalsIgnoreCase(parteDt.getId()) &&
					    processoParteDt.getNumeroGuia().equalsIgnoreCase(dados.getNumeroGuia())) {
						possuiDebito = true;
					}
				}
				
				if (possuiDebito) {
					stRetorno += "Débito já cadastrado para essa parte, favor verificar. \n ";
					break;
				}
			}
		}
		
		return stRetorno;
	}
	
	public String VerificarExclusao(ProcessoParteDebitoFisicoDt dados, UsuarioNe usuarioSessao) throws Exception {
		String stRetorno = "";
		
		if (dados == null) {
			stRetorno += "Nenhum Débito foi selecionado para exclusão ou não existe mais, favor consultar novamente. \n ";
		} else if (dados.getNumeroGuia() != null && dados.getNumeroGuia().trim().length() > 0) {
			ServentiaDt serventiaDt = (new ServentiaNe()).consultarId(usuarioSessao.getUsuarioDt().getId_Serventia());
			if (serventiaDt != null && serventiaDt.getServentiaCodigoExterno() != null && !(dados.getCodigoExternoServentia().equals(serventiaDt.getServentiaCodigoExterno()) ||
				 Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.DIRETORIA_FINANCEIRA)) {
				stRetorno += "Não é possível excluir um Débito de Processo de outra Serventia.";
			} else if ((new ProcessoParteDebitoCadinNe()).isGuiaSPGSSGEnviadaCadin(dados.getNumeroGuia())) {
				stRetorno += "Impossível excluir, pois o débito já foi enviado ao CADIN. \n ";	
			} else if (Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) != ProcessoDebitoStatusDt.NOVO &&
					   Funcoes.StringToInt(dados.getId_ProcessoDebitoStatus()) != ProcessoDebitoStatusDt.DEVOLVIDO_PELO_FINANCEIRO) {
				stRetorno += "Impossível excluir, somente status novo e devolvido pelo financeiro poderá ser excluído pela serventia. \n ";
			}					
		}
		return stRetorno;
	}


	public void salvarDebitoPartes(ProcessoParteDebitoFisicoDt processoParteDebitoFisicodt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			this.salvar(processoParteDebitoFisicodt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void salvar(ProcessoParteDebitoFisicoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		if (dados.getProcessoNumeroCompleto() != null && dados.getNumeroGuia() != null && dados.getNumeroGuia().trim().length() > 0) {
			GuiaEmissaoDt guiaEmissaoDt = (new GuiaSPGNe()).consultarGuiaEmissaoSPG(dados.getNumeroGuia());
			if (guiaEmissaoDt != null) {
				dados.setValorTotalGuia(guiaEmissaoDt.getValorTotalGuia());
			}
		}	
		
		dados.setProcessoNumeroCompleto(Funcoes.obtenhaSomenteNumeros(dados.getProcessoNumeroCompleto()));
		dados.setNomeSimplificado(Funcoes.converteNomeSimplificado(dados.getNome()));
		
		ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
		if (dados.getId().length() == 0) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("ProcessoParteBeneficioFisico", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("ProcessoParteBeneficioFisico",dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
		
	}

	public List consultarDebitosProcessoParte(String nomeParte, String cpfCnpjParte, String processoNumero, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
				
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDebitosProcessoParte(nomeParte, cpfCnpjParte, processoNumero, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarDebitosProcessoParteJSON(String nomeParte, String cpfCnpjParte, String processoNumero, String serventia, String numeroGuia, String posicao) throws Exception {
		String stTemp = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDebitosProcessoParteJSON(nomeParte, cpfCnpjParte, processoNumero, serventia, numeroGuia, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public ProcessoSPGDt consultarProcessoNumero(String numeroProcessoCompleto) throws Exception {		
		ProcessoSPGNe processoNe = new ProcessoSPGNe();
		return processoNe.consulteProcesso(numeroProcessoCompleto);		
	}
	
	public List consultarPartesComDebitos(String numeroProcesso ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarPartesComDebitos(numeroProcesso);		
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public List consultarPartesComDebitos(String numeroProcesso, FabricaConexao obFabricaConexao) throws Exception {
		List tempList=null;
		ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
		tempList=obPersistencia.consultarPartesComDebitos(numeroProcesso);
		return tempList;   
	}
	
	public List consultarGuias(String numeroProcessoSPG) throws Exception {
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		return guiaSPGNe.consultarGuiasSPGProcessoSPG(numeroProcessoSPG);   
	}
	
	public List consultarGuias(String numeroProcessoSPG, FabricaConexao obFabricaConexao ) throws Exception {
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		return guiaSPGNe.consultarGuiasSPGProcessoSPG(numeroProcessoSPG, obFabricaConexao);   
	}
	
	public ProcessoParteDebitoFisicoDt consultarProcessoNumeroProad(String numeroProcessoProad) throws Exception {
		ProcessoParteDebitoFisicoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarProcessoNumeroProad(numeroProcessoProad ); 
			if (dtRetorno != null) obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public ProcessoParteDebitoFisicoDt consultarProcessoNumeroProad(String numeroProcessoProad, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoFisicoDt dtRetorno=null;
		
		ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarProcessoNumeroProad(numeroProcessoProad ); 
		if (dtRetorno != null) obDados.copiar(dtRetorno);
		
		return dtRetorno;
	}
	
	public ProcessoDebitoStatusDt consultarProcessoDebitoStatusId(String idProcessoDebitoStatus) throws Exception {
		FabricaConexao obFabricaConexao = null;
		ProcessoDebitoStatusNe processoDebitoStatusNe = new ProcessoDebitoStatusNe();

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return processoDebitoStatusNe.consultarId(idProcessoDebitoStatus);
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarDescricaoProcessoDebitoJSON(String descricao, String posicao) throws Exception {
		return new ProcessoDebitoNe().consultarDescricaoProcessoDebitoJSON(descricao, posicao);
	}
	
	public List consultarProcessoDebito(String descricao) throws Exception {
		return new ProcessoDebitoNe().consultarProcessoDebito(descricao);
	}
	
	public ServentiaDt consultarServentiaCodigoExterno(String serventiaCodigo) throws Exception {
		return new ServentiaNe().consultarServentiaCodigoExterno(serventiaCodigo);
	}
	
	public List<ProcessoParteDebitoFisicoDt> consultarListaLiberadoCadinAindaNaoEnvido(FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoFisicoPs obPersistencia = new ProcessoParteDebitoFisicoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarListaLiberadoCadinAindaNaoEnvido();
	}
}
