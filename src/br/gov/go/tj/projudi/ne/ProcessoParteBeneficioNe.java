package br.gov.go.tj.projudi.ne;

import java.util.Calendar;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoBeneficioDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteBeneficioDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.ps.ProcessoParteBeneficioPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

//---------------------------------------------------------
public class ProcessoParteBeneficioNe extends ProcessoParteBeneficioNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = -3552584772792334142L;

    /**
	 * Verifica dados obrigatórios para o cadastro de benefícios
	 */
	public String Verificar(ProcessoParteBeneficioDt dados) {
		String stRetorno = "";

		if (dados.getId_ProcessoParte() == null || dados.getId_ProcessoParte().length() == 0) stRetorno += "Selecione uma Parte para o Benefício. \n";
		if (dados.getId_ProcessoBeneficio() == null || dados.getId_ProcessoBeneficio().length() == 0) stRetorno += "Selecione o Tipo de Benefício. \n ";
		if (dados.getDataInicial() == null || dados.getDataInicial().equals("")) stRetorno += "Insira a Data Início para o Benefício.";
		return stRetorno;

	}

	/**
	 * Salva benefícios para partes de processo
	 * 
	 * @param processoParteBeneficioDt, objeto com dados dos benefícios
	 * @author msapaula
	 */
	public void salvarBeneficioPartes(ProcessoParteBeneficioDt processoParteBeneficioDt) throws Exception {
	    FabricaConexao obFabricaConexao = null;
	    ProcessoParteNe processoParteNe = new ProcessoParteNe();
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();	
			
			ProcessoParteBeneficioPs obPersistencia = new ProcessoParteBeneficioPs(obFabricaConexao.getConexao());

			for (int i = 0; i < processoParteBeneficioDt.getPartesBeneficio().length; i++) {
				// Verifica se existe um benefício ativo já concedido para a parte
				valideBeneficioJaConcedido(processoParteNe, processoParteBeneficioDt.getPartesBeneficio()[i], processoParteBeneficioDt.getId_ProcessoBeneficio(), obPersistencia, obFabricaConexao);
				
				processoParteBeneficioDt.setId_ProcessoParte(processoParteBeneficioDt.getPartesBeneficio()[i]);

				//Captura dados do benefício selecionado
				ProcessoBeneficioDt beneficio = new ProcessoBeneficioNe().consultarId(processoParteBeneficioDt.getId_ProcessoBeneficio());

				//Calcula data final do benefício
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(Funcoes.StringToDate(processoParteBeneficioDt.getDataInicial()));
				calendar.add(Calendar.DAY_OF_MONTH, Funcoes.StringToInt(beneficio.getPrazo()));
				processoParteBeneficioDt.setDataFinal(Funcoes.DataHora(calendar.getTime()));

				this.salvar(processoParteBeneficioDt, obPersistencia, obFabricaConexao);
				processoParteBeneficioDt.setId("");
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			processoParteNe = null;
			obFabricaConexao.fecharConexao();
		}
	}
	
	private void valideBeneficioJaConcedido(ProcessoParteNe processoParteNe, String id_processoparte, String id_ProcessoBeneficio, ProcessoParteBeneficioPs obPersistencia, FabricaConexao obFabricaConexao) throws Exception
	{
		ProcessoParteDt processoParteDt = processoParteNe.consultarId(id_processoparte, obFabricaConexao);
		List listaBeneficios = null;
		
		if (processoParteDt != null && processoParteDt.getCpf() != null && processoParteDt.getCpf().trim().length() > 0) {
			listaBeneficios = obPersistencia.consultarBeneficiosProcessoParte(processoParteDt.getCpf().trim());
			if (listaBeneficios != null && listaBeneficios.size() > 0){
				String mensagem = "";
				for (int i = 0; i < listaBeneficios.size(); i++) {
					ProcessoParteBeneficioDt obTemp = (ProcessoParteBeneficioDt) listaBeneficios.get(i);
					if (obTemp.getId_ProcessoBeneficio().equalsIgnoreCase(id_ProcessoBeneficio))
					{
						if (mensagem.length() == 0) mensagem = "Benefício já cadastrado para a parte CPF: " + processoParteDt.getCpfFormatado() + " - Nome: " + processoParteDt.getNome();
						mensagem += "\n Processo: " + obTemp.getProcessoNumero() + " - Benefício: " + obTemp.getProcessoBeneficio() + " - Período: " + obTemp.getDataInicial() + " a " + obTemp.getDataFinal();
					}
				}
				if (mensagem.length() > 0) throw new MensagemException(mensagem);				
			}
		}
	}

	/**
	 * Salva um beneficio baseado em uma conexão já existente
	 */
	public void salvar(ProcessoParteBeneficioDt dados, ProcessoParteBeneficioPs obPersistencia, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
				
		if (dados.getId().length() == 0) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("ProcessoParteBeneficio", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("ProcessoParteBeneficio", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
		
	}

	/**
	 * Consulta os benefícios cadastrados baseado nos parâmetros passados, onde a data final do benefício
	 * seja maior ou igual a data atual
	 * 
	 * @param nomeParte, filtro para nome da parte
	 * @param cpfParte, filtro para cpf da parte
	 * @param posicao, parâmetro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarBeneficiosProcessoParte(String nomeParte, String cpfParte, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteBeneficioPs obPersistencia = new ProcessoParteBeneficioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarBeneficiosProcessoParte(nomeParte, cpfParte, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Consulta os benefícios cadastrados baseado nos parâmetros passados, onde a data final do benefício
	 * seja maior ou igual a data atual usando json
	 * 
	 * @param nomeParte, filtro para nome da parte
	 * @param cpfParte, filtro para cpf da parte
	 * @param posicao, parâmetro para paginação
	 * 
	 * @author kbsriccioppo
	 */
	public String consultarBeneficiosProcessoParteJSON(String idProcesso, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteBeneficioPs obPersistencia = new ProcessoParteBeneficioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarPartesComBeneficioJSON(idProcesso, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Método para fazer consulta de benefícios usando JSON
	 * @param nomeParte - nome da parte a ser consultada
	 * @param cpfParte - cpf da parte
	 * @param posicaoPaginaAtual
	 * @return lista de benefícios
	 * @throws Exception
	 */
	public String consultarBeneficiosProcessoParteJSON(String nomeParte, String cpfParte, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteBeneficioPs obPersistencia = new ProcessoParteBeneficioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarBeneficiosProcessoParteJSON(nomeParte, cpfParte, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Consulta os benefícios cadastrados baseado nos parâmetros passado
	 * 
	 * @param idProcesso, identificador do processo
	 * 
	 * @author lsbernardes
	 */
	public List consultarPartesComBeneficio(String idProcesso) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteBeneficioPs obPersistencia = new ProcessoParteBeneficioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarPartesComBeneficio(idProcesso);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Chama método para consultar dados de processo
	 * @param processoNumero
	 * @return processoBuscaDt, objeto com dados básicos de processo
	 */
	public ProcessoDt consultarProcessoNumero(String processoNumero) throws Exception {
		
		ProcessoNe processoNe = new ProcessoNe();
		return processoNe.consultarProcessoNumeroCompleto(processoNumero,null);
		
	}
	
	public String consultarDescricaoProcessoBeneficioJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ProcessoBeneficioNe neObjeto = new ProcessoBeneficioNe();

		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	/**
	 * Chama método para consultar dados de processo
	 * @param processoNumero
	 * @return processoBuscaDt, objeto com dados básicos de processo
	 */
	public ProcessoDt consultarIdCompleto(String id_processo) throws Exception {
		
		ProcessoNe processoNe = new ProcessoNe();
		return processoNe.consultarIdCompleto(id_processo);		
	}
	
	public List consultarPartesComBeneficios(String id_Processo ) throws Exception {
		
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteBeneficioPs obPersistencia = new ProcessoParteBeneficioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarPartesComBeneficios(id_Processo);		
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public List consultarGuias(String id_Processo) throws Exception {
		
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		GuiaEmissaoNe guiaEmissaone = new GuiaEmissaoNe();

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			tempList = guiaEmissaone.consultarGuiaEmissao(obFabricaConexao, id_Processo, guiaEmissaone.consultarListaId_GuiaTipo(null));;			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public String consultarBeneficiosProcessoParteJSON(String id_processo_parte) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				if( id_processo_parte != null ) {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					ProcessoParteBeneficioPs obPersistencia = new ProcessoParteBeneficioPs(obFabricaConexao.getConexao()); 
					
					stTemp = obPersistencia.consultarBeneficiosProcessoParteJSON(id_processo_parte);
				}
			} finally {
				if(obFabricaConexao != null) {
					obFabricaConexao.fecharConexao();
				}
			}
		return stTemp;
	}
	
}
