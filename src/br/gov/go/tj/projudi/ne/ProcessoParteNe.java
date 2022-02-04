package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;

import org.apache.axis.utils.StringUtils;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteSinalDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.ProcessoPartePs;
import br.gov.go.tj.projudi.ps.RgOrgaoExpedidorPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public class ProcessoParteNe extends ProcessoParteNeGen {

	private static final long serialVersionUID = -7087017371926407505L;

	
	/**
	 * Verifica dados obrigatórios da parte
	 * @throws Exception 
	 */
	public String Verificar(ProcessoParteDt dados, String areaCodigo) throws Exception {

		String stRetorno = "";

		if (dados.getProcessoParteTipoCodigo().length() == 0 || (dados.getId_ProcessoParteTipo().length() == 0 && dados.getProcessoParteTipoCodigo().equals("-1"))) stRetorno += "Selecione o Tipo da Parte. \n";

		if (dados.getNome().length() == 0) stRetorno += "Nome é campo obrigatório. \n";
		else dados.setNome(dados.getNome().replace("'", " "));

		if (dados.getCpf().length() > 0){
			if (dados.getCpf().length() != 11 || !Funcoes.testaCPFCNPJ(dados.getCpf())) {
				stRetorno += "CPF inválido. \n";
			}
		}

		if (dados.getCnpj().length() > 0){
			if (Funcoes.completaCNPJZeros(dados.getCnpj()).length() != 14 || !Funcoes.testaCPFCNPJ(Funcoes.completaCNPJZeros(dados.getCnpj()))) {
				stRetorno += "CNPJ inválido. \n";
			}
		}
		
		if(dados.getCpf().length() > 0 && dados.getCnpj().length() > 0){
			stRetorno += "CPF e CNPJ não podem ser informados ao mesmo tempo. \n";
		}

		if ((dados.getRg().length() > 0) && (!Funcoes.validaNumerico(dados.getRg()))) {
			stRetorno += "Número do RG deve conter apenas algarismos. \n";
		}

		if ((dados.getDataNascimento().length() > 0) && (!Funcoes.validaData(dados.getDataNascimento()))) {
			stRetorno += "Data de Nascimento em formato incorreto. \n";
		}

		if (dados.getCtps().length() > 0) {
			if (!Funcoes.validaNumerico(dados.getCtps())) {
				stRetorno += "Número CTPS devem conter apenas algarismos. \n";
			}
		}
		
	    if ((dados.getCtpsSerie().length() > 0)) {
	            if (!Funcoes.validaNumerico(dados.getCtpsSerie())) {
	                stRetorno += "A Série da CTPS devem conter apenas algarismos. \n";
	            }
	        }

		if (dados.getPis().length() > 0) {
			if (!Funcoes.validaNumerico(dados.getPis())) {
				stRetorno += "Número de inscrição no INSS deve conter apenas algarismos. \n";
			} else {
				if (!Funcoes.validaPIS(dados.getPis())) stRetorno += "Número de Inscrição no INSS inválido. \n";
			}
		}

		if (dados.getTituloEleitor().length() > 0) {
			if (!Funcoes.validaNumerico(dados.getTituloEleitor())) {
				stRetorno += "Número de inscrição do título de eleitor deve conter apenas algarismos. \n";
			} else {
				if (!Funcoes.validaTituloEleitor(dados.getTituloEleitor())) stRetorno += "Número de inscrição do título de eleitor inválido. \n";
			}
		}

		/**
		 * Valida endereço da parte em questão
		 */
		EnderecoNe enderecoNe = new EnderecoNe();
		stRetorno += enderecoNe.verificarEnderecoParte(dados.getEnderecoParte(), dados.ParteNaoPersonificavel(), areaCodigo, dados.isParteEnderecoDesconhecido(), dados.getLocomocaoDt());
		return stRetorno;

	}

	public void simplificarNome() throws Exception{
	       List tempList = null;
	        FabricaConexao obFabricaConexao = null;

	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	            ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
	            tempList = obPersistencia.consultarParteSimplificar();
	            do{
	                for (int i=0;i<tempList.size();i++){
	                    ProcessoParteDt parte = (ProcessoParteDt)tempList.get(i);
	                    obPersistencia.alterar(parte);
	                }
	                tempList = obPersistencia.consultarParteSimplificar();
	            }while (tempList.size()>0);
	        
	        } finally{
	            obFabricaConexao.fecharConexao();
	        }
	}
	
	/**
	 * Valida dados obrigatórios da parte e seu endereço para os casos cíveis
	 * @throws Exception 
	 */
	public String Verificar(ProcessoParteDt dados) throws Exception {
		return this.Verificar(dados, null);
	}

	/**
	 * Valida dados obrigatórios da parte e seu endereço para os casos criminais
	 * CEP não é obrigatório
	 * @throws Exception 
	 */
	public String VerificarParteCriminal(ProcessoParteDt dados) throws Exception {
		return this.Verificar(dados, String.valueOf(AreaDt.CRIMINAL));
	}

	/**
	 * Valida os dados da parte para os processos de execução penal
	 */
	public String VerificarParteProcessoExecucao(ProcessoParteDt processoPartedt) throws Exception {
		String stRetorno = "";
		
		if (processoPartedt.getNome() == null || processoPartedt.getNome().trim().length() == 0) stRetorno += "O campo Nome é obrigatório. \n";
		
		if (processoPartedt.getRaca() == null || processoPartedt.getRaca().trim().length() == 0) stRetorno += "O campo Raca é obrigatório. \n";
		
		if (processoPartedt.getNomeMae() == null || processoPartedt.getNomeMae().trim().length() == 0) stRetorno += "O campo Nome da Mãe é obrigatório. \n";
		if (processoPartedt.getDataNascimento() == null || processoPartedt.getDataNascimento().trim().length() == 0) stRetorno += "O campo Data de Nascimento é obrigatório. \n";

		if ((processoPartedt.getCpf() != null && processoPartedt.getCpf().trim().length() > 0) && 
		    (!Funcoes.validaNumerico(processoPartedt.getCpf().trim()) || !Funcoes.testaCPFCNPJ(processoPartedt.getCpf().trim()))) {
			stRetorno += "O número do CPF é inválido. \n";
		}

		if ((processoPartedt.getRg() != null && processoPartedt.getRg().trim().length() > 0) && (!Funcoes.validaNumerico(processoPartedt.getRg()))) {
			stRetorno += "Verifique! O Número do RG deve conter apenas algarismos. \n";
		}
		
		if ((processoPartedt.getDataNascimento() != null && processoPartedt.getDataNascimento().trim().length() > 0) && (!Funcoes.validaData(processoPartedt.getDataNascimento().trim()))) {
			stRetorno += "Verifique! A Data de Nascimento não está no formato dd/mm/yyyy. \n";
		}

		//Valida endereço da parte, se preenchido.
		if (processoPartedt.getEnderecoParte() != null && processoPartedt.getEnderecoParte().getLogradouro() != null && processoPartedt.getEnderecoParte().getLogradouro().trim().length() > 0){
			EnderecoNe enderecoNe = new EnderecoNe();
			stRetorno += enderecoNe.verificarEnderecoParte(processoPartedt.getEnderecoParte(), false, String.valueOf(AreaDt.CRIMINAL), false);
		}
		return stRetorno;		
	}
	
	/**
	 * Sobrescrevendo método salvar, para permitir salvar o Endereço da parte
	 */
	public void salvar(ProcessoParteDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			
			// Salva endereço da parte somente se essa for personificável
			if (!dados.ParteNaoPersonificavel()) {
				//verifica se o endereço foi preenchido (no caso do processo de execução penal e endereço não localizado)
				if (dados.getEnderecoParte().getLogradouro().length() > 0 && !dados.isParteEnderecoDesconhecido()){
					
					// Se existirem mais de uma parte utilizando o mesmo endereço, teremos que cadastrar um novo...
					if(variasPartesUtilizamEsseEndereco(dados.getEnderecoParte().getId(), obPersistencia)) dados.getEnderecoParte().setId("");
					
					new EnderecoNe().salvar(dados.getEnderecoParte(), obFabricaConexao);
					dados.setId_Endereco(dados.getEnderecoParte().getId());	
				}
			} else dados.setId_Endereco(dados.getEnderecoParte().getId());		
			
			// Salva/Altera dados da parte
			if (dados.getId_ProcessoParte().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoParte", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoParte", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}
			
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			
			if (dados.getListaSinalParte() != null && dados.getListaSinalParte().size() > 0){
				for (ProcessoParteSinalDt sinal : (List<ProcessoParteSinalDt>) dados.getListaSinalParte()) {
					sinal.setId_ProcessoParte(dados.getId());
					sinal.setIpComputadorLog(dados.getIpComputadorLog());
					sinal.setId_UsuarioLog(dados.getId_UsuarioLog());
					new ProcessoParteSinalNe().salvar(sinal, obFabricaConexao);	
				}
			}
			
			if (dados.getListaAlcunhaParte() != null && dados.getListaAlcunhaParte().size() > 0){
				for (ProcessoParteAlcunhaDt alcunha : (List<ProcessoParteAlcunhaDt>) dados.getListaAlcunhaParte()) {
					alcunha.setId_ProcessoParte(dados.getId());
					alcunha.setIpComputadorLog(dados.getIpComputadorLog());
					alcunha.setId_UsuarioLog(dados.getId_UsuarioLog());
					new ProcessoParteAlcunhaNe().salvar(alcunha, obFabricaConexao);	
				}
			}
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Insere dados de uma parte durante o cadastro e alteração de processos
	 * 
	 * @param dados, objeto com dados da parte e endereço
	 * @param obfabricaconexao, conexão ativa
	 * @throws Exception
	 */
	public void salvar(ProcessoParteDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		try{
			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			// Salva endereço da parte somente se essa for personificável
			if (!dados.ParteNaoPersonificavel()) {
				//verifica se o endereço foi preenchido (no caso do processo de execução penal e endereço não localizado)
				if (dados.getEnderecoParte().getLogradouro().length() > 0 && !dados.isParteEnderecoDesconhecido()){
					// Se existirem mais de uma parte utilizando o mesmo endereço, teremos que cadastrar um novo...
					if(variasPartesUtilizamEsseEndereco(dados.getEnderecoParte().getId(), obPersistencia)) dados.getEnderecoParte().setId("");					
					new EnderecoNe().salvar(dados.getEnderecoParte(), obFabricaConexao);
					dados.setId_Endereco(dados.getEnderecoParte().getId());	
				}
			} else dados.setId_Endereco(dados.getEnderecoParte().getId());	
			
			// Salva/Altera dados da parte			
			if (dados.getId_ProcessoParte().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoParte", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				//Dados anteriores da parte
				obDados = this.consultarId(dados.getId(), obFabricaConexao);
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoParte", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}
			
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			
//			if (dados.getListaSinalParte() != null && dados.getListaSinalParte().size() > 0){
//				for (ProcessoParteSinalDt sinal : (List<ProcessoParteSinalDt>) dados.getListaSinalParte()) {
//					sinal.setId_ProcessoParte(dados.getId());
//					sinal.setIpComputadorLog(dados.getIpComputadorLog());
//					sinal.setId_UsuarioLog(dados.getId_UsuarioLog());
//					new ProcessoParteSinalNe().salvar(sinal, obFabricaConexao);	
//				}
//			}
//			
//			if (dados.getListaAlcunhaParte() != null && dados.getListaAlcunhaParte().size() > 0){
//				for (ProcessoParteAlcunhaDt alcunha : (List<ProcessoParteAlcunhaDt>) dados.getListaAlcunhaParte()) {
//					alcunha.setId_ProcessoParte(dados.getId());
//					alcunha.setIpComputadorLog(dados.getIpComputadorLog());
//					alcunha.setId_UsuarioLog(dados.getId_UsuarioLog());
//					new ProcessoParteAlcunhaNe().salvar(alcunha, obFabricaConexao);	
//				}
//			}
			
		} catch(Exception e) {
			dados.setId("");
			throw e;
		}
	}

	/**
	 * Método que verifica se usuário pode modificar dados das partes do processo
	 * 
	 * @param processoDt dt de processo
	 * @param usuarioDt usuário que vai modificar dados
	 * 
	 * @author msapaula
	 */
	public String podeModificarDados(ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		String stMensagem = "";
		
		//Verifica se o usuário é responsável pelo processo.
		if (!new ProcessoResponsavelNe().isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), processoDt.getId())) {
			// Se o usuário não for responsável pelo processo e for de serventia diferente do processo, não poderá modificar dados
			if (!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia())) {
				stMensagem += "Sem permissão para modificar dados das partes do processo.";
			}
		}
		/**
        * Solicitação de verificação de permissão para alteração quando um processo está arquivado somente para processos cíveis
        * feito por Jesus para Cássio Nogueira em 8/9/2020
        */
		if (processoDt.isArquivado() && !processoDt.isCriminal()){
			stMensagem += " Não é possível executar essa ação, processo está arquivado. \n";
		}else if (processoDt.isErroMigracao()){
			stMensagem += " Não é possível executar essa ação, processo está com ERRO DE MIGRAÇÃO. \n";
		}

		return stMensagem;
	}
	
	public List listarAlcunha(String idProcessoParte) throws Exception{
		return new ProcessoParteAlcunhaNe().listarAlcunha(idProcessoParte);
	}
	
	public List listarSinal(String idProcessoParte) throws Exception{
		return new ProcessoParteSinalNe().listarSinal(idProcessoParte);
	}

	/**
	 * Chama método para consultar dados da parte, sem uso de uma conexão já iniciada
	 */
	public ProcessoParteDt consultarId(String id_processoparte) throws Exception {
		return this.consultarId(id_processoparte, null);
	}

	/**
	 * Consulta dados da parte
	 * @param id_processoparte, identificação da parte
	 * @param conexao, conexão ativa
	 */
	public ProcessoParteDt consultarId(String id_processoparte, FabricaConexao conexao) throws Exception {
		ProcessoParteDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_processoparte);
			obDados.copiar(dtRetorno);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consulta partes de um processo de um tipo especificado
	 * @author Ronneesley Moura Teles
	 * @since 14/04/2008 - 15:43
	 * @param String id_processo, id do processo
	 * @param String processoParteTipoCodigo, codigo do tipo da parte
	 * @return List
	 * @throws Exception
	 */
	public List consultarPartes(String id_processo, String processoParteTipoCodigo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarPartes(id_processo, processoParteTipoCodigo);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consultar partes de um determinado processo, levando em consideração o tipo e status da parte.
	 * Consulta simples para listagem dos dados do processo.
	 * 
	 * @param id_Processo: identificação do processo
	 * @param parteTipoCodigo: tipo da parte
	 * @param ativa: booleano que especifica se devem ser retornadas somente as partes ativas
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private List getListaPartes(String id_processo, int parteTipoCodigo, boolean ativa) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			tempList = obPersistencia.getListaPartes(id_processo, parteTipoCodigo, ativa);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consulta todas as partes promoventes de um processo, ativas e baixadas.
	 * @param id_Processo: identificação do processo
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarPromoventes(String id_processo) throws Exception{
		return this.getListaPartes(id_processo, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, false);
	}

	/**
	 * Consulta somente os promoventes ativos de um processo
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarPromoventesAtivos(String id_Processo) throws Exception{
		return this.getListaPartes(id_Processo, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, true);
	}

	/**
	 * Consultar todas as partes promovidas de um processo, ativas e baixadas
	 * @param id_processo: identificação do processo
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarPromovidos(String id_processo) throws Exception{
		return this.getListaPartes(id_processo, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, false);
	}

	/**
	 * Consultar todas as outras partes de um processo, ativas e baixadas
	 * @param id_processo: identificação do processo
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarOutrasPartes(String id_processo) throws Exception{
		return this.getListaPartes(id_processo, -1, false);
	}

	/**
	 * Consulta somente os promovidos ativos de um processo
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarPromovidosAtivos(String id_Processo) throws Exception{
		return this.getListaPartes(id_Processo, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, true);
	}

	/**
	 * Consulta somente as outras partes ativas de um processo
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarOutrasPartesAtivas(String id_Processo) throws Exception{
		return this.getListaPartes(id_Processo, -1, true);
	}
	
	/**
	 * Método para consultar partes do tipo litisconsorte ATIVO
	 * @param String id_processo
	 * @return java.util.List
	 * @throws Exception
	 */
	public List consultarLitisconsorteAtivo(String id_processo) throws Exception{
		return this.getListaPartes(id_processo, ProcessoParteTipoDt.LITIS_CONSORTE_ATIVO, true);
	}
	
	/**
	 * Método para consultar partes do tipo litisconsorte PASSIVO
	 * @param String id_processo
	 * @return java.util.List
	 * @throws Exception
	 */
	public List consultarLitisconsortePassivo(String id_processo) throws Exception{
		return this.getListaPartes(id_processo, ProcessoParteTipoDt.LITIS_CONSORTE_PASSIVO, true);
	}

	/**
	 * Método responsável em consultar uma parte de acordo com os parâmetro passados
	 */
	public ProcessoParteDt consultarParte(String cpfCnpj, String rg, String ctps, String tituloEleitor, String pis) throws Exception {
		ProcessoParteDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarParte(cpfCnpj, rg, ctps, tituloEleitor, pis);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Método responsável por consultar todas as partes que possuem processo de execução penal
	 */
	public List listarPartesProcessoExecucao(String cpfCnpj, String nome, String mae, String dataNascimento, String numeroProcesso, String idServentia, String posicao) throws Exception {
		List dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			String stDigito = "";
			if (numeroProcesso != null && numeroProcesso.length() > 0) {
				String[] stTemp = numeroProcesso.split("\\.");
				if (stTemp.length >= 1) numeroProcesso = stTemp[0];
				else
					numeroProcesso = "";
				if (stTemp.length >= 2) stDigito = stTemp[1];
				if (stTemp.length >= 3) {
				}
			}
			
			dtRetorno = obPersistencia.listarPartesProcessoExecucao(cpfCnpj, nome, mae, dataNascimento, numeroProcesso, stDigito, idServentia, posicao);
			//posicao == null: quando não é necessário a paginação
			if (posicao != null){
				QuantidadePaginas = (Long) dtRetorno.get(dtRetorno.size() - 1);
				dtRetorno.remove(dtRetorno.size() - 1);
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Método responsável em consultar uma parte de acordo com os parâmetro passados
	 */
	public String VerificarCpfCnpjParte(String cpfCnpj) {
		String Mensagem = "";
		if (cpfCnpj != null && cpfCnpj.length() > 0) {
			if (!Funcoes.testaCPFCNPJ(cpfCnpj)) Mensagem = "CPF/CNPJ inválido.";
		}
		return Mensagem;
	}

	/**
	 * Chama método responsável em desabilitar uma parte de processo, criando uma nova conexão
	 * 
	 * @param processoPartedt
	 * @throws Exception
	 */
	public void desabilitaParteProcesso(ProcessoParteDt processoPartedt) throws Exception{
		this.desabilitaParteProcesso(processoPartedt, null);
	}

	/**
	 * Desabilita uma determinada parte de um processo (seta data da exclusão em DataBaixa)
	 * 
	 * @param processoPartedt, dados da parte a ser baixada
	 * @param conexao, conexão ativa
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private void desabilitaParteProcesso(ProcessoParteDt processoPartedt, FabricaConexao conexao) throws Exception{
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else obFabricaConexao = conexao;
			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParte", processoPartedt.getId(), processoPartedt.getId_UsuarioLog(), processoPartedt.getIpComputadorLog(), String.valueOf(LogTipoDt.DesabilitarParte), processoPartedt.getPropriedades(), "");
			obPersistencia.desabilitaParteProcesso(processoPartedt.getId_ProcessoParte());

			//Se a parte possuir advogados habilitados, esses também serão baixados
			ProcessoParteAdvogadoNe parteAdvogadoNe = new ProcessoParteAdvogadoNe();
			List listaAdvogadosParte = parteAdvogadoNe.consultarAdvogadosParte(processoPartedt.getId());

			for (int i = 0; i < listaAdvogadosParte.size(); i++) {
				ProcessoParteAdvogadoDt processoParteAdvogadoDt = (ProcessoParteAdvogadoDt) listaAdvogadosParte.get(i);
				processoParteAdvogadoDt.setId_UsuarioLog(processoPartedt.getId_UsuarioLog());
				processoParteAdvogadoDt.setIpComputadorLog(processoPartedt.getIpComputadorLog());
				parteAdvogadoNe.desabilitaAdvogado(processoParteAdvogadoDt, obFabricaConexao);
			}

			obLog.salvar(obLogDt, obFabricaConexao);
			if (conexao == null) obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			if (conexao == null) obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Realiza a exclusão de uma parte de um determinado processo.
	 * @param processoParteDt - parte a ser excluída
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void excluirParteProcesso(ProcessoParteDt processoPartedt) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new  ProcessoPartePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParte", processoPartedt.getId(), processoPartedt.getId_UsuarioLog(), processoPartedt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), processoPartedt.getPropriedades(), "");
			obPersistencia.excluirParteProcesso(processoPartedt.getId_ProcessoParte());

			//Se a parte possuir advogados habilitados, esses também serão baixados
			ProcessoParteAdvogadoNe parteAdvogadoNe = new ProcessoParteAdvogadoNe();
			List listaAdvogadosParte = parteAdvogadoNe.consultarAdvogadosParte(processoPartedt.getId());

			for (int i = 0; i < listaAdvogadosParte.size(); i++) {
				ProcessoParteAdvogadoDt processoParteAdvogadoDt = (ProcessoParteAdvogadoDt) listaAdvogadosParte.get(i);
				processoParteAdvogadoDt.setId_UsuarioLog(processoPartedt.getId_UsuarioLog());
				processoParteAdvogadoDt.setIpComputadorLog(processoPartedt.getIpComputadorLog());
				parteAdvogadoNe.desabilitaAdvogado(processoParteAdvogadoDt, obFabricaConexao);
			}

			obLog.salvar(obLogDt, obFabricaConexao);
				
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método que vincula uma Parte ao Usuario cadastrado para a mesma.
	 * Atualiza coluna Id_UsuarioServentia em Parte
	 * 
	 * @param processoParteDt dt de parte
	 * 
	 * @author msapaula
	 */
	public void habilitaUsuarioParte(ProcessoParteDt processoParteDt, LogDt logDt, FabricaConexao conexao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else obFabricaConexao = conexao;
			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParte", processoParteDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), "", processoParteDt.getPropriedades());
			obPersistencia.vinculaUsuarioParte(processoParteDt);

			obLog.salvar(obLogDt, obFabricaConexao);
			if (conexao == null) obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			if (conexao == null) obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método utilizado no cadastro de processo para marcar a citação de partes de processo quando
	 * uma audiência é marcada, para que na próxima marcação de audiência sejam intimadas
	 * 
	 * @param listaPartes, lista das parte do processo
	 * @param obFabricaConexao, conexão ativa
	 * @throws Exception
	 */
	public void marcarCitacaoPartesProcesso(List listaPartes, FabricaConexao obFabricaConexao) throws Exception {
		
		if (listaPartes != null && listaPartes.size() > 0) {
			for (int i = 0; i < listaPartes.size(); i++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaPartes.get(i);
				this.marcarCitacaoParteProcesso(parteDt.getId(), obFabricaConexao);
			}
		}
		
	}

	/**
	 * Método que marca a citação para um parte de processo, para que da próxima vez essa seja intimada.
	 * 
	 * @param id_ProcessoParte, identificação da parte
	 * @param conexao, conexão ativa
	 * 
	 * @author msapaula
	 */
	public void marcarCitacaoParteProcesso(String id_ProcessoParte, FabricaConexao obFabricaConexao) throws Exception {
		
		ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
		obPersistencia.marcarCitacaoParteProcesso(id_ProcessoParte);

	}

	/**
	 * Método que desvincula uma Parte de um Usuário cadastrado no sistema.
	 * Assim a parte não receberá mais as intimações/citações on-line.
	 * Retira o Id_UsuarioServentia em Parte
	 */
	public void desabilitaUsuarioParte(ProcessoParteDt processoParteDt, LogDt logDt) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParte", processoParteDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Excluir), processoParteDt.getPropriedades(), "");
			obPersistencia.vinculaUsuarioParte(processoParteDt);

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Restaura uma determinada parte que foi baixada de um processo
	 * (seta DataBaixa como null)
	 */
	public void restauraParteProcesso(ProcessoParteDt processoPartedt) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParte", processoPartedt.getId(), processoPartedt.getId_UsuarioLog(), processoPartedt.getIpComputadorLog(), String.valueOf(LogTipoDt.RestaurarParte), processoPartedt.getPropriedades(), "");
			obPersistencia.restauraParteProcesso(processoPartedt.getId_ProcessoParte());

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Consulta apenas um provomente, para substituicao no modelo
	 * @author Ronneesley Moura Teles
	 * @since 11/04/2008
	 * @param String id_processo, id do processo onde a parte esta envolvida
	 * @return ProcessoParteDt
	 */
	public ProcessoParteDt consultarUmPromovente(String id_processo) throws Exception {
		ProcessoParteDt processoParte = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			processoParte = obPersistencia.consultarUmPromovente(id_processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return processoParte;
	}

	/**
	 * Consulta apenas um promovido, para substituicao no modelo
	 * @param String id_processo, id do processo onde a parte esta envolvida
	 * @return ProcessoParteDt
	 * @throws Exception
	 */
	public ProcessoParteDt consultarUmPromovido(String id_processo) throws Exception {
		ProcessoParteDt processoParte = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			processoParte = obPersistencia.consultarUmPromovido(id_processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return processoParte;
	}

	/**
	 * Consulta dados completos de uma Parte, inclusive dados referentes à Alcunha e Sinal Particular.
	 * 
	 * @param id_processoParte, identificação da parte
	 * @author msapaula
	 **/
	public ProcessoParteDt consultarIdCompleto(String id_processoParte) throws Exception {
		ProcessoParteDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdCompleto(id_processoParte);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public ProcessoParteDt consultarIdCompleto(String id_processoParte, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDt dtRetorno = null;
		ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.consultarIdCompleto(id_processoParte);
		obDados.copiar(dtRetorno);
		return dtRetorno;
	}

	/**
	 * Consulta o rg completo da parte do processo
	 * @author Ronneesley Moura Teles
	 * @since 11/04/2008 - 16:41
	 * @param String id_processoparte, id da parte
	 * @return String
	 * @throws Exception
	 */
	public String consultarRgCompleto(String id_processoparte) throws Exception {
		String rgCompleto = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			rgCompleto = obPersistencia.consultarRgCompleto(id_processoparte);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return rgCompleto;
	}

	/**
	 * Insere partes promoventes de um processo
	 * @param listaPartes lista de partes promoventes
	 * @param id_Processo identificação do processo
	 * @param logDt objeto de log contendo o id do usuário
	 * @param obFabricaConexao fábrica de conexão
	 * 
	 * @author msapaula
	 */
	protected void inserirPromoventes(List listaPartes, String id_Processo, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		this.inserirProcessoPartes(listaPartes, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, id_Processo, false, logDt, obFabricaConexao);
	}

	/**
	 * Insere partes promovidas de um processo
	 * @param listaPartes lista de partes promovidas
	 * @param id_Processo identificação do processo
	 * @param logDt objeto de log contendo o id do usuário
	 * @param obFabricaConexao fábrica de conexão
	 * 
	 * @author msapaula
	 */
	protected void inserirPromovidos(List listaPartes, String id_Processo, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		this.inserirProcessoPartes(listaPartes, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, id_Processo, false, logDt, obFabricaConexao);
	}

	/**
	 * Altera as partes recorrentes de um processo de 2º grau
	 * 
	 * @param processoDt
	 * @param listaPartesAnterior
	 */
	public void alterarPartesRecorrentesProcessoSegundoGrau(ProcessoDt processoDt, List listaPartesAnterior, LogDt logDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());

			List listaAtual = processoDt.getListaPolosAtivos();
			List listaExcluir = new ArrayList(); //Lista das partes a serem excluídas
			List listaIncluir = new ArrayList(); //Lista das partes a serem incluídas

			//Pega a lista anterior das partes e procura se alguma foi excluída
			for (int i = 0; i < listaPartesAnterior.size(); i++) {
				ProcessoParteDt parteAnterior = (ProcessoParteDt) listaPartesAnterior.get(i);
				if (parteAnterior.getDataBaixa().length() == 0) {
					boolean boEncontrado = false;
					//Verifica qual parte saiu da lista
					for (int j = 0; j < listaAtual.size(); j++) {
						ProcessoParteDt parteAtual = (ProcessoParteDt) listaAtual.get(j);
						if (parteAnterior.getNome().equalsIgnoreCase(parteAtual.getNome())) {
							boEncontrado = true;
							break;
						}
					}
					//se o id não foi encontrado na lista editada coloco o objeto para ser excluido
					if (!boEncontrado) listaExcluir.add(parteAnterior);
				}
			}

			//Verifica as partes a serem incluídas
			for (int i = 0; i < listaAtual.size(); i++) {
				ProcessoParteDt parteAtual = (ProcessoParteDt) listaAtual.get(i);
				boolean boEncontrado = false;
				for (int j = 0; j < listaPartesAnterior.size(); j++) {
					ProcessoParteDt parteAnterior = (ProcessoParteDt) listaPartesAnterior.get(j);
					if (parteAnterior.getNome().equals(parteAtual.getNome()) && parteAnterior.getDataBaixa().length() == 0) {
						boEncontrado = true;
						break;
					}
				}
				if (!boEncontrado) listaIncluir.add(parteAtual);
			}

			for (int i = 0; i < listaIncluir.size(); i++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaIncluir.get(i);
				parteDt.setId_Processo(processoDt.getId());
				parteDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));
				new EnderecoNe().inserir(parteDt.getEnderecoParte(), obFabricaConexao);
				parteDt.setId_Endereco(parteDt.getEnderecoParte().getId());

				LogDt obLogDt = new LogDt("ProcessoParte", parteDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Incluir), "", parteDt.getPropriedades());
				obPersistencia.inserir(parteDt);
				obLog.salvar(obLogDt, obFabricaConexao);
			}

			for (int i = 0; i < listaExcluir.size(); i++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaExcluir.get(i);
				parteDt.setId_UsuarioLog(logDt.getId_Usuario());
				parteDt.setIpComputadorLog(logDt.getIpComputador());
				this.desabilitaParteProcesso(parteDt, obFabricaConexao);
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método utilizado no cadastro de processo para inserir várias partes de um processo e seus respectivos advogados.
	 * Faz tratamento para verificar se será necessário incluir um novo registro em endereço
	 * 
	 * @param listaPartes, lista das parte do processo
	 * @param tipoParte, tipo da parte
	 * @param id_Processo, id_processo da parte
	 * @param Citada, define se a parte já foi citada
	 * 
	 * @author msapaula
	 */
	private void inserirProcessoPartes(List listaPartes, int tipoParte, String id_Processo, boolean Citada, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		if (listaPartes != null && listaPartes.size() > 0) {
			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());

			//Salvando partes da lista
			for (int i = 0; i < listaPartes.size(); i++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaPartes.get(i);
				
				// Salva endereço da parte somente se essa for personificável
				if (!parteDt.ParteNaoPersonificavel()) {
					//verifica se o endereço foi preenchido (no caso do processo de execução penal e endereço não localizado)
					if (parteDt.getEnderecoParte().getLogradouro().length() > 0 && !parteDt.isParteEnderecoDesconhecido()){
						
						parteDt.getEnderecoParte().setId_UsuarioLog(logDt.getId_UsuarioLog());
						parteDt.getEnderecoParte().setIpComputadorLog(logDt.getIpComputadorLog());
						
						// Se existirem mais de uma parte utilizando o mesmo endereço, teremos que cadastrar um novo...
						if(variasPartesUtilizamEsseEndereco(parteDt.getEnderecoParte().getId(), obPersistencia)) parteDt.getEnderecoParte().setId("");
						
						new EnderecoNe().salvar(parteDt.getEnderecoParte(), obFabricaConexao);
						parteDt.setId_Endereco(parteDt.getEnderecoParte().getId());	
					}
				} else parteDt.setId_Endereco(parteDt.getEnderecoParte().getId());			

				//Salva parte
				parteDt.setProcessoParteTipoCodigo(String.valueOf(tipoParte));
				parteDt.setId_Processo(id_Processo);
				parteDt.setCitada(String.valueOf(Citada));
				parteDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
				parteDt.setIpComputadorLog(logDt.getIpComputadorLog());
				obPersistencia.inserir(parteDt);

				ProcessoParteAdvogadoNe advogadoNe = new ProcessoParteAdvogadoNe();
				//Salva advogado da parte
				if (parteDt.getAdvogadoDt() != null && parteDt.getAdvogadoDt().getId_UsuarioServentiaAdvogado().length() > 0) {
					ProcessoParteAdvogadoDt advogadoDt = parteDt.getAdvogadoDt();
					advogadoDt.setId_ProcessoParte(parteDt.getId_ProcessoParte());
					advogadoDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
					advogadoDt.setIpComputadorLog(logDt.getIpComputadorLog());
					advogadoNe.salvar(advogadoDt, obFabricaConexao);
				}
			}
		}
	}

	/**
	 * Método utilizado no cadastro de processo para salvar (inserir) todas as partes
	 * as partes de um processo e seus respectivos advogados.
	 * Por padrão, todas as partes promoventes já são inseridas como "Citadas", pois deverão ser Intimadas ao marcar audiência.
	 * 
	 * @param listaPartes, lista das parte do processo
	 * @param id_Processo, id_processo da parte
	 * @param obFabricaConexao, conexão ativa
	 * @throws Exception
	 */
	public void salvarPartesProcesso(List listaPartes, String id_Processo, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		if (listaPartes != null && listaPartes.size() > 0) {

			//Salvando partes da lista
			for (int i = 0; i < listaPartes.size(); i++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaPartes.get(i);
				//Salva parte
				parteDt.setId_Processo(id_Processo);
				if (parteDt.getProcessoParteTipoCodigo().length() > 0 && Funcoes.StringToInt(parteDt.getProcessoParteTipoCodigo()) == ProcessoParteTipoDt.POLO_ATIVO_CODIGO){
					parteDt.setCitada("true");
				}
				parteDt.setId_UsuarioLog(logDt.getId_Usuario());
				parteDt.setIpComputadorLog(logDt.getIpComputador());

				parteDt.getEnderecoParte().setId_UsuarioLog(logDt.getId_Usuario());
				parteDt.getEnderecoParte().setIpComputadorLog(logDt.getIpComputador());

				this.salvar(parteDt, obFabricaConexao);
				List lisAdvogados = parteDt.getAdvogados();
				//Salva advogado da parte -- nesse caso será para as partes com advogado específico (Importação Processo)
				if(lisAdvogados != null)
					for(int j = 0; j < lisAdvogados.size(); j++) {						
						ProcessoParteAdvogadoNe advogadoNe = new ProcessoParteAdvogadoNe();
						ProcessoParteAdvogadoDt advogadoDt = (ProcessoParteAdvogadoDt) lisAdvogados.get(j);
						advogadoDt.setId_ProcessoParte(parteDt.getId_ProcessoParte());
						advogadoDt.setId_UsuarioLog(logDt.getId_Usuario());
						advogadoDt.setIpComputadorLog(logDt.getIpComputador());
						advogadoNe.salvar(advogadoDt, obFabricaConexao);
					}

				//Salva alcunha da parte
				if (parteDt.getListaAlcunhaParte() != null) {
					for (int w = 0; w < parteDt.getListaAlcunhaParte().size(); w++) {
						ProcessoParteAlcunhaNe alcunhaNe = new ProcessoParteAlcunhaNe();
						ProcessoParteAlcunhaDt alcunhaDt = (ProcessoParteAlcunhaDt) parteDt.getListaAlcunhaParte().get(w);

						alcunhaDt.setId_ProcessoParte(parteDt.getId_ProcessoParte());
						alcunhaDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
						alcunhaDt.setIpComputadorLog(logDt.getIpComputador());
						alcunhaNe.salvar(alcunhaDt, obFabricaConexao);
					}
				}

				//Salva sinal particular da parte
				if (parteDt.getListaSinalParte() != null) {
					for (int w = 0; w < parteDt.getListaSinalParte().size(); w++) {
						ProcessoParteSinalNe sinalNe = new ProcessoParteSinalNe();

						ProcessoParteSinalDt sinalDt = (ProcessoParteSinalDt) parteDt.getListaSinalParte().get(w);
						sinalDt.setId_ProcessoParte(parteDt.getId_ProcessoParte());
						sinalDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
						sinalDt.setIpComputadorLog(logDt.getIpComputador());
						sinalNe.salvar(sinalDt, obFabricaConexao);
					}
				}
			}
		} else {
			throw new MensagemException("Processo não possui parte cadastrada. Favor rever o Passo 1 do cadastro do processo.");
		}
	}
	
	/**
	 * Método responsável em consultar se uma parte é cadastrada como usuário.
	 * Retorna o Id_UsuarioServentia da parte
	 * @param String id_processoparte, id da parte
	 */
	public String consultarUsuarioServentiaParte(String id_ProcessoParte) throws Exception {
		String id_UsuarioServentia = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			id_UsuarioServentia = obPersistencia.consultarUsuarioServentiaParte(id_ProcessoParte);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return id_UsuarioServentia;
	}

	/**
	 * Verifica se determinado usuário é parte ativa em um processo
	 * @param id_UsuarioServentia, identificação do usuário
	 * @param id_Processo, identificação do processo
	 * @author msapaula
	 */
	public boolean isParteProcesso(String id_UsuarioServentia, String id_Processo) throws Exception {
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.isParteProcesso(id_UsuarioServentia, id_Processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	/**
	 * Retorna o processo com todas as partes setadas de acordo com cada tipo de parte,
	 * sendo essas ativas ou baixadas
	 * 
	 * @param processoDt, dt de processo
	 * @return List
	 * @author msapaula
	 */
	public void setPartesProcesso(ProcessoDt processoDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());

			processoDt.setListaPolosAtivos(new ArrayList());
			processoDt.setListaPolosPassivos(new ArrayList());
			processoDt.setListaOutrasPartes(new ArrayList());

			List tempList = obPersistencia.consultarPartesProcesso(processoDt.getId(), false);
			for (int i = 0; i < tempList.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) tempList.get(i);
				
				//verificando se a parte é isenta
				processoParteDt.setParteIsenta(this.isParteIsenta(processoParteDt));

				switch (Funcoes.StringToInt(processoParteDt.getProcessoParteTipoCodigo())) {
					case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
						processoDt.addListaPoloAtivo(processoParteDt);
						break;
					case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
						processoDt.addListaPolosPassivos(processoParteDt);
						break;
					default:
						processoDt.addListaOutrasPartes(processoParteDt);
						break;
				}
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	
	/**
	 * Retorna o processo com todas as partes setadas de acordo com cada tipo de parte,
	 * sendo essas ativas ou baixadas
	 * 
	 * @param processoDt, dt de processo
	 * @return List
	 * @author msapaula
	 */
	public void setPartesProcesso(ProcessoDt processoDt, UsuarioDt usuarioDt, int grupo) throws Exception {
		if (processoDt == null) return;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			
			ProcessoNe processoNe = new ProcessoNe();
			
			boolean podeAcessarProcesso = processoNe.podeAcessarProcesso(usuarioDt, processoDt,obFabricaConexao);
			
			processoDt.setListaPolosAtivos(new ArrayList());
			processoDt.setListaPolosPassivos(new ArrayList());
			processoDt.setListaOutrasPartes(new ArrayList());

			List tempList = obPersistencia.consultarPartesProcesso(processoDt.getId(), false);
			for (int i = 0; i < tempList.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) tempList.get(i);
				
				if (!podeAcessarProcesso && 
						(processoDt.isSegredoJustica() || 
						 processoDt.isSigiloso() ||
						 processoParteDt.isVitima())
					) {
					processoParteDt.setNome(Funcoes.iniciaisNome(processoParteDt.getNome()));
				}
				
				if (!podeAcessarProcesso && ((processoParteDt.getDataBaixa() != null && 
						                      processoParteDt.getDataBaixa().trim().length() > 0)
						                      ||
						                      (processoParteDt.getExcluido() != null && 
						                      processoParteDt.getExcluido().trim().equalsIgnoreCase(String.valueOf(ProcessoParteDt.EXCLUIDO)))))
					continue;

				switch (Funcoes.StringToInt(processoParteDt.getProcessoParteTipoCodigo())) {
					case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
						processoDt.addListaPoloAtivo(processoParteDt);
						break;
					case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
						processoDt.addListaPolosPassivos(processoParteDt);
						break;
					default:
						processoDt.addListaOutrasPartes(processoParteDt);
						break;
				}
			}	
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Retorna o processo com somente as partes ativas setadas de acordo com cada tipo de parte
	 * 
	 * @param processoDt, dt de processo
	 * @return List
	 * @author msapaula
	 */
	public void setPartesProcessoAtivas(ProcessoDt processoDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());

			processoDt.setListaPolosAtivos(new ArrayList());
			processoDt.setListaPolosPassivos(new ArrayList());
			processoDt.setListaOutrasPartes(new ArrayList());

			List tempList = obPersistencia.consultarPartesProcesso(processoDt.getId(), true);
			for (int i = 0; i < tempList.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) tempList.get(i);

				switch (Funcoes.StringToInt(processoParteDt.getProcessoParteTipoCodigo())) {
					case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
						processoDt.addListaPoloAtivo(processoParteDt);
						break;
					case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
						processoDt.addListaPolosPassivos(processoParteDt);
						break;
					default:
						processoDt.addListaOutrasPartes(processoParteDt);
						break;
				}
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoOrgaoExpedidor(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		RgOrgaoExpedidorNe neObjeto = new RgOrgaoExpedidorNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();

		neObjeto = null;
		return tempList;
	}
	
	public String consultarDescricaoOrgaoExpedidorJSON(String sigla, String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RgOrgaoExpedidorPs obPersistencia = new RgOrgaoExpedidorPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(sigla, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoBairro(String tempNomeBusca, String cidade, String uf, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		BairroNe neObjeto = new BairroNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, cidade, uf, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;

	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 * @throws Exception 
	 */
	public void retirarAusenciaProcessoParte(ProcessoParteDt processoParteDt) throws Exception{
		ProcessoParteAusenciaNe neObjeto = new ProcessoParteAusenciaNe();
		
		neObjeto.retirarAusenciaProcessoParte(processoParteDt);
		
		neObjeto = null;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarOutrosTiposPartes(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ProcessoParteTipoNe neObjeto = new ProcessoParteTipoNe();
		
		tempList = neObjeto.consultarOutrosTiposPartes(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	public String consultarOutrosTiposPartesJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		ProcessoParteTipoNe neObjeto = new ProcessoParteTipoNe();
		stTemp = neObjeto.consultarOutrosTiposPartesJSON(tempNomeBusca, PosicaoPaginaAtual);		
		return stTemp;
	}

	public ProcessoDt consultarProcessoIdCompleto(String id_Processo) throws Exception {
		ProcessoDt processodt = null;
		ProcessoNe processoNe = new ProcessoNe();
		
		processodt = processoNe.consultarIdCompleto(id_Processo);
		
		processoNe = null;
		return processodt;
	}
	
	public String consultarParteNome(String nome, String comarcaCodigo, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarParteNome(nome, comarcaCodigo, posicao);
			QuantidadePaginas = Funcoes.StringToLong(stTemp.substring(stTemp.length()-5));
			stTemp = stTemp.substring(0, stTemp.length()-5);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarParteCpf(String cpf, String comarcaCodigo, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarParteCpf(cpf, comarcaCodigo, posicao);
			QuantidadePaginas = Funcoes.StringToLong(stTemp.substring(stTemp.length()-5));
			stTemp = stTemp.substring(0, stTemp.length()-5);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Verifica se existem mais de uma parte utilizando o mesmo id, foi necessário criar esse método para corrigir as várias partes que estão compartilhando o endereço com id 1.
	 * 
	 * @param id_Endereco_Parte
	 * @param obPersistencia
	 * @return
	 * @throws Exception
	 */
	private boolean variasPartesUtilizamEsseEndereco(String id_Endereco_Parte, ProcessoPartePs obPersistencia) throws Exception
	{
		boolean retorno = false;
		if (id_Endereco_Parte != null && id_Endereco_Parte.trim().length() > 0)
			retorno = (obPersistencia.consultarQuantidadePartesUtilizamEsseEndereco(id_Endereco_Parte) > 1);
		 
		return retorno;
	}
	
	/**
	 * Altera os dados de um processo parte.
	 * 
	 * @param dados
	 * @param obFabricaConexao
	 * @author mmgomes
	 * @throws Exception
	 */
	public void alterar(ProcessoParteDt processoParteDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;		
			
		ProcessoPartePs obPersistencia = new   ProcessoPartePs(obFabricaConexao.getConexao());
							
		obDados = obPersistencia.consultarId(processoParteDt.getId());
		obPersistencia.alterar(processoParteDt);
		obLogDt = new LogDt("ProcessoParte", processoParteDt.getId(), processoParteDt.getId_UsuarioLog(), processoParteDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), processoParteDt.getPropriedades());			

		obDados.copiar(processoParteDt);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public String consultarDescricaoBairroJSON(String descricao, String cidade, String uf, String posicao) throws Exception {
		String stTemp = "";
		
		BairroNe Bairrone = new BairroNe();
		stTemp = Bairrone.consultarDescricaoJSON(descricao, cidade, uf, "", posicao);
				
		return stTemp;
	}
	
	public String consultarDescricaoCidadeJSON(String tempNomeBusca, String uf, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		CidadeNe Naturalidadene = new CidadeNe();
		stTemp = Naturalidadene.consultarDescricaoJSON(tempNomeBusca, uf, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public void excluirAlcunha(ProcessoParteAlcunhaDt alcunha) throws Exception{		
		new ProcessoParteAlcunhaNe().excluir(alcunha);
	}
	
	public void excluirSinal(ProcessoParteSinalDt sinal) throws Exception{		
		new ProcessoParteSinalNe().excluir(sinal);		
	}

	public String consultarDescricaoEscolaridadeJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		EscolaridadeNe neObjeto = new EscolaridadeNe();
		
		stTemp = neObjeto.consultarDescricaoEscolaridadeJSON( tempNomeBusca, posicaoPaginaAtual);
		
		neObjeto = null;
		return stTemp;
	}

	public String consultarDescricaoGovernoTipoJSON(String stNomeBusca1, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		GovernoTipoNe neObjeto = new GovernoTipoNe();
		
		stTemp = neObjeto.consultarDescricaoJSON( stNomeBusca1, posicaoPaginaAtual);
		
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoEstadoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception{
		String stTemp ="";
		EstadoNe Estadone = new EstadoNe(); 
		stTemp = Estadone.consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
	return stTemp;   
	}
	
	public String consultarDescricaoEstadoCivilJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		EstadoCivilNe neObjeto = new EstadoCivilNe();
		
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		
		neObjeto = null;
		return stTemp;
	}

	public String consultarDescricaoEmpresaTipoJSON(String stNomeBusca1, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		EmpresaTipoNe neObjeto = new EmpresaTipoNe();
		
		stTemp = neObjeto.consultarDescricaoJSON(stNomeBusca1, posicaoPaginaAtual);
		
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoRacaJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		RacaNe neObjeto = new RacaNe();
		
		stTemp = neObjeto.consultarDescricaoJSON( tempNomeBusca, posicaoPaginaAtual);
		
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoProfissaoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		String stTemp ="";
		ProfissaoNe Profissaone = new ProfissaoNe(); 
		stTemp = Profissaone.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		return stTemp;
	}

	public String consultarIdBairro(String id_ProcessoParte) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPartePs processoParte = new  ProcessoPartePs(obFabricaConexao.getConexao());
			stTemp = processoParte.consultarIdBairro(id_ProcessoParte);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	
		return stTemp;
	}
	
	public void atualizarNomeSimplificado() throws Exception{
				
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			int id_inicial = 1545000;
			int id_final = 5189643;
			long time1=0;
			List lisPartes = null;
			long timeGeral = System.currentTimeMillis();
			do{
				time1 = System.currentTimeMillis();
				lisPartes = obPersistencia.consultarPartes(id_inicial);
				
				for (int i=0; i<lisPartes.size(); i++){
					obPersistencia.salvarNomeSimmplificado((ProcessoParteDt) lisPartes.get(i));
				}
				
				System.out.println("> " + (id_inicial ) + " <=" + (id_inicial+1000) + " em " + (System.currentTimeMillis() - time1) );
				id_inicial += 1000;
			}while (id_inicial<(id_final+1000) );
			System.out.println("finalizou em  "  + (System.currentTimeMillis() - timeGeral) );
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
			
	}
	
	public void salvarWebservice(String conteudoArquivo, LogDt logDt) throws Exception{
		ProcessoParteDt processoParteDt = new ProcessoParteDt();
		String[] dadosParte = conteudoArquivo.split(";",-1);
		//[0]IdProcessoParte;[1]IdProcessoParteTipo;[2]CPF/CNPJ;[3]Nome;[4]RG;[5]Órgao Expedidor RG;[6] Uf RG;[7]Data de Nascimento;[8]Número Carteira de Trabalho(CTPS);[9]UF CTPS;[10]Série CTPS;[11]PIS;[12]Titulo de Eleitor;[13]Nome da Mãe;[14]Sexo;
		//[15]Logradouro;[16]Numero;[17]Complemento;[18]IdBairro;[19]Telefone;[20]CEP;[21]E-Mail;
		
		if(!dadosParte[0].equalsIgnoreCase("")) {
			processoParteDt = consultarId(dadosParte[0]);
		} 
		
		if( processoParteDt != null) {
			// [1]IdProcessoParteTipo
			if(!dadosParte[1].equalsIgnoreCase("")) { 
				ProcessoParteTipoDt processoParteTipoDt = new ProcessoParteTipoNe().consultarId(dadosParte[1]);
				if(processoParteTipoDt != null) {
					processoParteDt.setId_ProcessoParteTipo(processoParteTipoDt.getId());
					processoParteDt.setProcessoParteTipo(processoParteTipoDt.getProcessoParteTipo());
				}
			}
			if (dadosParte[1].length() == 14) processoParteDt.setCnpj(dadosParte[2]);
			else processoParteDt.setCpf(dadosParte[2]);
			processoParteDt.setNome(dadosParte[3]);
			processoParteDt.setRg(dadosParte[4]);
			// [5]Órgão Expedidor RG , [6]Uf RG
			if(!dadosParte[5].equalsIgnoreCase("") && !dadosParte[6].equalsIgnoreCase("")) { 
				RgOrgaoExpedidorDt rgOrgaoExpedidorDt = new RgOrgaoExpedidorNe().buscaOrgaoExpedidor(dadosParte[5], dadosParte[6]);
				if(rgOrgaoExpedidorDt != null) {
					processoParteDt.setId_RgOrgaoExpedidor(rgOrgaoExpedidorDt.getId());
					processoParteDt.setRgOrgaoExpedidor(rgOrgaoExpedidorDt.getRgOrgaoExpedidor());
				}
			}
			processoParteDt.setDataNascimento(dadosParte[7]);
			processoParteDt.setCtps(dadosParte[8]);
			processoParteDt.setCtpsSerie(dadosParte[10]);
			// [9]Uf CTPS
			if(!dadosParte[9].equalsIgnoreCase("")) {
				EstadoDt estadoDt = new EstadoNe().buscaEstado(dadosParte[9]);
				if (estadoDt != null)  {
					processoParteDt.setId_CtpsUf(estadoDt.getId());
					processoParteDt.setEstadoCtpsUf(estadoDt.getEstado());
					processoParteDt.setSiglaCtpsUf(estadoDt.getUf());
				}
			}
			processoParteDt.setPis(dadosParte[11]);
			processoParteDt.setTituloEleitor(dadosParte[12]);
			processoParteDt.setNomeMae(dadosParte[13]);
			processoParteDt.setSexo(dadosParte[14]);
			processoParteDt.setTelefone(dadosParte[19]);
			processoParteDt.setEMail(dadosParte[21]);
			processoParteDt.setId_UsuarioLog(logDt.getId_Usuario());
			processoParteDt.setIpComputadorLog(logDt.getIpComputador());
			
//			processoParteDt.setId_Naturalidade(dadosParte[3]);
//			processoParteDt.setCidadeNaturalidade(dadosParte[3]);
//			processoParteDt.setId_EstadoCivil(dadosParte[3]);
//			processoParteDt.setEstadoCivil(dadosParte[3]);
//			processoParteDt.setId_Profissao(dadosParte[3]);
//			processoParteDt.setProfissao(dadosParte[3]);
//			processoParteDt.setCelular(dadosParte[3]);
//			processoParteDt.setRgDataExpedicao(dadosParte[3]);
//			processoParteDt.setTituloEleitorZona(dadosParte[3]);
//			processoParteDt.setTituloEleitorSecao(dadosParte[3]);
//			processoParteDt.setId_Processo();
//			processoParteDt.setCulpado();
//			processoParteDt.setNomePai();
//			processoParteDt.setId_EmpresaTipo();
//			processoParteDt.setEmpresaTipo();
//			processoParteDt.setId_GovernoTipo();
//			processoParteDt.setGovernoTipo();
//			processoParteDt.setAlcunha();
//			processoParteDt.setSinal();
//			processoParteDt.setEscolaridade();
//			processoParteDt.setId_Escolaridade();
//			processoParteDt.setRaca();
//			processoParteDt.setId_Raca();
			
			// Endereço da Parte
//			processoParteDt.getEnderecoParte().setId();
			processoParteDt.getEnderecoParte().setLogradouro(dadosParte[15]);
			processoParteDt.getEnderecoParte().setNumero(dadosParte[16]);
			processoParteDt.getEnderecoParte().setComplemento(dadosParte[17]);
			processoParteDt.getEnderecoParte().setCep(dadosParte[20]);
			processoParteDt.getEnderecoParte().setId_UsuarioLog(logDt.getId_Usuario());
			processoParteDt.getEnderecoParte().setIpComputadorLog(logDt.getIpComputador());
			if (!dadosParte[18].equals("")) {
				BairroDt bairroDt =  new BairroNe().consultarId(dadosParte[18]);
				if (bairroDt != null) {
					processoParteDt.getEnderecoParte().setId_Bairro(bairroDt.getId());
					processoParteDt.getEnderecoParte().setBairro(bairroDt.getBairro());
					processoParteDt.getEnderecoParte().setId_Cidade(bairroDt.getId_Cidade());
					processoParteDt.getEnderecoParte().setCidade(bairroDt.getCidade());
					processoParteDt.getEnderecoParte().setUf(bairroDt.getUf());
				} else {
					processoParteDt.getEnderecoParte().setId_Bairro("6"); //Se não encontrar o bairro passado define como padrão Bairro Central de Goiania
				}
			}
		}
	}
	
	/**
	 * Verifica se parte passada é isenta.
	 */
	public boolean isParteIsenta(ProcessoParteDt processoParteDt) throws Exception {
		PartesIsentaNe parteIsentaNe = new PartesIsentaNe();
		return parteIsentaNe.isParteIsenta(processoParteDt.getCnpj());		
	}
	
	/**
	 * Verifica se o processo possui outra parte isenta além da passada para o método ou uma classe isenta.
	 */
	public boolean possuiPartesIsentasClasseIsenta(ProcessoDt processoDt, ProcessoParteDt processoParteDt) throws Exception {
		boolean retorno = false;
		//se a parte a ser baixada for do tipo diferente de POLO ATIVO, não precisa fazer as validações de baixa.
		if(!processoParteDt.getProcessoParteTipoCodigo().equals(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO))){
			return true;
		}
		
		if (processoDt.getId_Custa_Tipo() != null && processoDt.getId_Custa_Tipo().equalsIgnoreCase(String.valueOf(ProcessoDt.ISENTO))){
			PartesIsentaNe partesIsentaNe = new PartesIsentaNe();
			boolean possuiParteIsenta = false;
			for (Iterator iterator = processoDt.getListaPolosAtivos().iterator(); iterator.hasNext();) {
				ProcessoParteDt procParteDt = (ProcessoParteDt) iterator.next();
				if ( ( processoParteDt.getId() != null && !processoParteDt.getId().equalsIgnoreCase(procParteDt.getId()) ) 
						&& procParteDt.getCnpj() != null && procParteDt.getCnpj().length()>0 && partesIsentaNe.isParteIsenta(procParteDt.getCnpj())
						&& (procParteDt.getDataBaixa() == null || procParteDt.getDataBaixa().length()==0)
					){
					possuiParteIsenta = true;
					break;
				}
			}
			
			//Validação da modelo da guia
			boolean classeComCusta;
			GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
			if( guiaEmissaoNe.consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU, processoDt.getId_ProcessoTipo()) != null ) {
				classeComCusta = true;
			} if( guiaEmissaoNe.consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, processoDt.getId_ProcessoTipo()) != null ) {
				classeComCusta = true;
			} else {
				classeComCusta = false;
			}
			
			//O processo com custa Tipo Isento deve possuir pelo menos uma parte isenta ou uma classe sem custa processual
			if (!possuiParteIsenta && classeComCusta){
				retorno = false;
			} else {
				retorno = true;
			}
			
		} else {
			retorno = true;
		}
		return retorno;
	}
	
	
	public ServentiaDt consultarServentia(String id_Serventia) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe();
		ServentiaDt serventiaDt =  serventiaNe.consultarId(id_Serventia);
		return serventiaDt;
	}
	
	// lrcampos - 31/01/2020 10:04 - Consultar classe da Audiencia Processo
	public String consultaClasseProcessoIdAudiProc(String idAudiProc) throws Exception {
		if (StringUtils.isEmpty(idAudiProc))
			return null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultaClasseProcessoIdAudiProc(idAudiProc, obFabricaConexao);
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

		
	// mrbatista - 11/10/2019 14:49 - Consultar classe da Audiencia Processo
	public String consultaClasseProcessoIdAudiProc(String idAudiProc, FabricaConexao fabricaConexao) throws Exception {

		Integer tipoRecursoProcesso = consultaTipoRecursoProcesso(idAudiProc, fabricaConexao);
		ProcessoPartePs processoPartePs = new ProcessoPartePs(fabricaConexao.getConexao());

		return processoPartePs.consultaClasseProcesso(idAudiProc, tipoRecursoProcesso);
	}
	
	// mrbatista - 11/10/2019 14:49 - Consultar tipo de recurso Processo
	private Integer consultaTipoRecursoProcesso(String idAudiProc, FabricaConexao fabricaConexao) throws Exception {

		ProcessoPartePs processoPartePs = new ProcessoPartePs(fabricaConexao.getConexao());
		return processoPartePs.consultaTipoRecursoProcessoIdAudiProc(idAudiProc);
	}
	
	public String consisteDataSentenca(String dataSentenca, String dataRecebimento) throws Exception { 
    	TJDataHora dataAtual = new TJDataHora();
	    
	    if (Funcoes.diferencaDatas(Funcoes.StringToDate(dataSentenca), Funcoes.DataHora(dataRecebimento))[0] < 0)
            return "Data da sentença menor que data de distribuição do processo";

	    if (Funcoes.diferencaDatas(Funcoes.StringToDate(dataAtual.getDataFormatadaddMMyyyy()), Funcoes.StringToDate(dataSentenca))[0] < 0)
            return "Data da sentença maior que data atual";
   	    
   	    return "";
	}

	public String consisteDataPronuncia(String dataPronuncia, String dataRecebimento) throws Exception { 
    	TJDataHora dataAtual = new TJDataHora();
	    
	    if (Funcoes.diferencaDatas(Funcoes.StringToDate(dataPronuncia), Funcoes.DataHora(dataRecebimento))[0] < 0)
            return "Data da pronúncia menor que data de distribuição do processo";

	    if (Funcoes.diferencaDatas(Funcoes.StringToDate(dataAtual.getDataFormatadaddMMyyyy()), Funcoes.StringToDate(dataPronuncia))[0] < 0)
            return "Data da pronúncia maior que data atual";
   	    
   	    return "";
	}
	
	
	// mrbatista - 16/06/2020 14:49 - Consultar processoTipo da Audiencia Processo
	public Integer consultaCodigoProcessoTipoPeloIdAudiProc(String idAudiProc) throws Exception {
		if (StringUtils.isEmpty(idAudiProc))
			return null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			return consultaCodigoProcessoTipoPeloIdAudiProc(idAudiProc, obFabricaConexao);
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	
	// mrbatista - 16/06/2020 14:49 - Consultar processoTipo da Audiencia Processo
	public Integer consultaCodigoProcessoTipoPeloIdAudiProc(String idAudiProc, FabricaConexao fabricaConexao) throws Exception {

		Integer tipoRecursoProcesso = consultaTipoRecursoProcesso(idAudiProc, fabricaConexao);
		ProcessoPartePs processoPartePs = new ProcessoPartePs(fabricaConexao.getConexao());

		return processoPartePs.consultaCodigoProcessoTipoPeloIdAudiProc(idAudiProc, tipoRecursoProcesso);
	}

}
