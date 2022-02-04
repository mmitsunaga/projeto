package br.gov.go.tj.projudi.ne;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoArquivoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoOrigemDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.PrisaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.MandadoPrisaoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

//---------------------------------------------------------
public class MandadoPrisaoNe extends MandadoPrisaoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 5730365439698406866L;

	public String Verificar (MandadoPrisaoDt dados) throws Exception {
		return "";
	}
	
	public  String Verificar(MandadoPrisaoDt dados, boolean isJuiz) throws Exception {
		String stRetorno="";

		if (dados.getMandadoPrisaoStatusCodigo().isEmpty() || dados.getMandadoPrisaoStatusCodigo().equals(String.valueOf(MandadoPrisaoStatusDt.EMITIDO))){
			if (dados.getDataValidade().length() == 0) stRetorno += "\nO campo DATA DE VALIDADE DO MANDADO é obrigatório.";
			else {
				try{
					Date dt =Funcoes.StringToDate(dados.getDataValidade());					
					if (dt.before(new Date())) stRetorno += "\nO campo DATA DE VALIDADE DO MANDADO deve ser posterior à data atual.";
				}catch(Exception e){
					stRetorno += "\nErro na DATA DE VALIDADE DO MANDADO";
				}
			}
			
			if (dados.getMandadoPrisaoOrigemCodigo().length() == 0) stRetorno += "\nSelecione a ORIGEM DO MANDADO DE PRISÃO.";
			if (dados.getNumeroOrigem().length() == 0) stRetorno += "\nO campo NÚMERO DO DOCUMENTO ORIGEM é obrigatório.";

			if (dados.getMandadoPrisaoOrigemCodigo().equals(String.valueOf(MandadoPrisaoOrigemDt.OUTRO))){
				if (dados.getOrigem().length() == 0) stRetorno += "\nInforme a descrição da ORIGEM do Mandado de Prisão.";
			}
			if (dados.getPrisaoTipoCodigo().length() == 0) stRetorno += "\nSelecione o TIPO DE PRISÃO.";
			if (dados.getId_ProcessoParte().length() == 0) stRetorno += "\nSelecione o PROMOVIDO.";
			if (dados.getId_Assunto().length() == 0) stRetorno += "\nSelecione o ASSUNTO DO PROCESSO.";
			
			if (dados.getPrisaoTipoCodigo().equals(String.valueOf(PrisaoTipoDt.PREVENTIVA_DECISAO_CONDENATORIA))
					|| dados.getPrisaoTipoCodigo().equals(String.valueOf(PrisaoTipoDt.CONDENACAO))){
				if (dados.getId_RegimeExecucao().length() == 0) stRetorno += "\nO campo REGIME é obrigatório.";
				if (dados.getTempoPenaTotalDias().length() == 0) stRetorno += "\nO campo PENA IMPOSTA é obrigatório";
			} else if (dados.getPrisaoTipoCodigo().equals(String.valueOf(PrisaoTipoDt.TEMPORARIA))){
				if (dados.getPrazoPrisao().length() == 0) stRetorno += "\nO campo PRAZO DA PRIZÃO é obrigatório";
			}
	//		if (dados.getId_MandadoPrisaoStatus().length() == 0) stRetorno += "\nSelecione o STATUS DO MANDADO DE PRISÃO.";
			if (isJuiz)
				if (dados.getSinteseDecisao().length() == 0) stRetorno += "\nO campo SÍNTESE DA DECISÃO é obrigatório.";

		} else stRetorno += "Não é possível salvar este Mandado. (Motivo: Mandado " + dados.getMandadoPrisaoStatus() + ")";
		return stRetorno;
	}
	
	public String verificarExcluir(MandadoPrisaoDt dados) throws Exception {
		if (dados.getMandadoPrisaoStatusCodigo().isEmpty() || dados.getMandadoPrisaoStatusCodigo().equals(String.valueOf(MandadoPrisaoStatusDt.EMITIDO))){
			return "";
		} else return "Não é possível exlcuir este Mandado. (Motivo: Mandado " + dados.getMandadoPrisaoStatus() + ")";
	}
	
	public String verificarCumprimento(List listaArquivos, MandadoPrisaoDt mandadoPrisaoDt) throws Exception{
		String stRetorno = "";
		if (listaArquivos == null || listaArquivos.size() == 0)	{
			stRetorno += "\nInsira o ofício de cumprimento do Mandado de Prisão!";
		}
		if (mandadoPrisaoDt.getDataPrisao().length() == 0) {
			stRetorno += "\nInforme a Data da Prisão!";
		}
		else if (new Date().before(Funcoes.StringToDate(mandadoPrisaoDt.getDataPrisao()))) {
			stRetorno += "\nA Data da Prisão deve ser igual ou anterior à Data Atual!";
		}
		
		//codigo novo -------------------------------------------------------------------------------------------------------------------------------
		if (mandadoPrisaoDt.getProcessoPartePrisaoDt() == null){
			stRetorno += "\nA Os dados do Mandado de Prisão da Parte são compos Obrigatório!";
		} else {
			if (mandadoPrisaoDt.getProcessoPartePrisaoDt().getId_PrisaoTipo().length() ==0){
				stRetorno += "\nInforme o tipo de prisão!";
			}
			if (mandadoPrisaoDt.getProcessoPartePrisaoDt().getId_LocalCumpPena().length() ==0){
				stRetorno += "\nInforme o Local de Cumprimento da Pena!";
			}
		}
		//codigo novo -------------------------------------------------------------------------------------------------------------------------------
		return stRetorno;
	}

	/**
	 * Lista os Mandados de Prisão referente ao processo
	 * @param idPorcesso: identificação do processo
	 * @return: Lista com mandados de prisão
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarMandadoPrisaoProcesso(String idPorcesso, boolean listarTodos) throws Exception{
		List listaMandado = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
			listaMandado = obPersistencia.listarMandadoPrisaoProcesso(idPorcesso, listarTodos);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return listaMandado;
	}
	
	/**
	 * Verifica se existe Mandado de Prisão Não Cumprido no processo
	 * @param idPorcesso: identificação do processo
	 * @return: boolean
	 * @throws Exception
	 * @author wcsilva
	 */
	public boolean isExisteMandadoPrisaoProcesso_NaoCumprido(String idPorcesso, FabricaConexao obFabricaConexao) throws Exception{
		boolean isExiste = false;

		MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
		isExiste = obPersistencia.existeMandadoPrisaoProcesso_NaoCumprido(idPorcesso);
		
		return isExiste;
	}
	
	/**
	 * Lista os Mandados de Prisão
	 * @param idPorcesso: identificação do processo
	 * @return: Lista com mandados de prisão
	 * @throws Exception
	 * @author wcsilva
	 */
	public String listarMandadoPrisaoServentiaJSON(String numeroProcesso, String dataInicial, String dataFinal, String mandadoPrisaoStatusCodigo, String mandadoPrisaoTipoCodigo, boolean listarTodos, String posicao, String idServentia) throws Exception{
		String stRetorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
			stRetorno = obPersistencia.listarMandadoPrisaoServentiaJSON(numeroProcesso, dataInicial, dataFinal, mandadoPrisaoStatusCodigo, mandadoPrisaoTipoCodigo, listarTodos, idServentia, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return stRetorno;
	}
	
	/**
	 * Lista todos os Status do Mandado de Prisão
	 * @return: Lista com MandadoPrisaoStatusDt
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarMandadoPrisaoStatus() throws Exception{
		List lista = null;

		lista = new MandadoPrisaoStatusNe().consultarDescricao("", "0");
		
		return lista;
	}
	
	/**
	 * Lista todos as Origens do Mandado de Prisão
	 * @return: Lista com MandadoPrisaoOrigemDt
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarMandadoPrisaoOrigem() throws Exception{
		List lista = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
			lista = obPersistencia.listarMandadoPrisaoOrigem();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return lista;
	}
	
	/**
	 * Lista todos os Tipos de Prisão
	 * @return: Lista com PrisaoTipoDt
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarPrisaoTipo() throws Exception{
		List lista = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
			lista = obPersistencia.listarPrisaoTipo();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return lista;
	}
	
	/**
	 * Lista todos os Regimes de Pena
	 * @return: Lista com RegimeExecucaoDt
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarRegime() throws Exception{
		List lista = null;

		lista = new RegimeExecucaoNe().consultarDescricao("", String.valueOf(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE), "0");
		
		return lista;
	}
	
	/**
	 * Gera o número do mandado de prisão.
	 * @param idProcesso: identificação do processo
	 * @return
	 * @throws Exception
	 */
	public String gerarNumeroMandadoPrisao(String idProcesso) throws Exception{
		String numero = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
			int ultimoNumero = obPersistencia.consultarMaiorNumeroMandadoProcesso(idProcesso);
			numero = Funcoes.completarZeros(String.valueOf(ultimoNumero+1), 4);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return numero;
	}
	
	/**
	 * salva os dados do mandado de prisão
	 * @param mandadoPrisaoDt: objeto com os dados do mandado de prisão
	 * @throws Exception
	 */
	public void salvarMandadoPrisao(MandadoPrisaoDt mandadoPrisaoDt) throws Exception{
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

//			LogDt logDt = new LogDt(mandadoPrisaoDt.getId_UsuarioLog(), mandadoPrisaoDt.getIpComputadorLog());
//			if (mandadoPrisaoDt.getId().length() == 0){
//				mandadoPrisaoDt.setMandadoPrisaoNumero(gerarNumeroMandadoPrisao(mandadoPrisaoDt.getId_Processo()));
//			}
			salvar(mandadoPrisaoDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
//	public void salvar MandadoPrisao(MandadoPrisaoDt mandadoPrisaoDt, FabricaConexao obFabricaConexao) throws Exception{
//		
//			new LogDt(mandadoPrisaoDt.getId_UsuarioLog(), mandadoPrisaoDt.getIpComputadorLog());
//			salvar(mandadoPrisaoDt, obFabricaConexao);
//		
//	}
	
	public void salvar(MandadoPrisaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;

		
		MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 

		if (dados.getId().length()==0) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("MandadoPrisao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {
			obPersistencia.alterar(dados); 
			obLogDt = new LogDt("MandadoPrisao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);


	}
	
	/**
	 * Calcula a validade do mandado de prisão, com base na data atual e no artigo 109.
	 * @param tempoEmDias
	 * @param dataNascimento
	 * @param dataDelito
	 * @return
	 * @throws Exception
	 */
	public String calcularValidadeMandadoPrisaoJSON(String tempoEmDias, String dataNascimento, String dataDelito) throws Exception{
		StringBuilder stTemp = new StringBuilder();
		
		ProcessoEventoExecucaoNe peeNe = new ProcessoEventoExecucaoNe();
		
		int tempoPrescricao =  peeNe.calcularTempoPrescricao(tempoEmDias, false, dataDelito, false, new HashMap(), peeNe.isMenoridadeMaioridadePrescricao(dataNascimento, dataDelito, Funcoes.dateToStringSoData(new Date())), false);
		String data = Funcoes.somaData(Funcoes.dateToStringSoData(new Date()), tempoPrescricao);
		
		stTemp.append("{");
		stTemp.append("\"DataValidade\":\"" + data + "\"");
		stTemp.append("}");
		
		return stTemp.toString();
	}

	/**
	 * Listar os mandados de prisão emitidos para o Juiz.
	 * @param idServentiaCargo: identificação do Juiz
	 * @return
	 */
	public List listarMandadoPrisaoEmitidoServentiaCargo(String idServentiaCargo) throws Exception{
		List lista = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
			lista = obPersistencia.listarMandadoPrisaoEmitidoServentiaCargo(idServentiaCargo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return lista;
	}
	
	/**
	 * Listar mandados de prisão para a serventia cargo, conforme o status 
	 * @param idServentiaCargo: identificação do usuário
	 * @param mandadoPrisaoStatusCodigo: código do status do mandado de prisão
	 * @return
	 */
	public List listarMandadoPrisaoServentiaCargo(String idServentiaCargo, List mandadoPrisaoStatusCodigo) throws Exception{
		List lista = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
			lista = obPersistencia.listarMandadoPrisaoServentiaCargo(idServentiaCargo, mandadoPrisaoStatusCodigo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return lista;
	}
	
	
	/**
	 * Consultar quantidade de mandados de prisão emitidos para o Juiz.
	 * @param idServentiaCargo: identificação do Juiz
	 * @return
	 */
	public int consultarQuantidadeMandadoPrisaoEmitidoServentiaCargo(String idServentiaCargo) throws Exception{
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeMandadoPrisaoEmitidoServentiaCargo(idServentiaCargo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Consultar quantidade de mandados de prisão para a serventia cargo, conforme o status 
	 * @param idServentiaCargo: identificação do Juiz
	 * @param mandadoPrisaoStatusCodigo: código do status do mandado de prisão
	 * @return
	 */
	public int consultarQuantidadeMandadoPrisaoServentiaCargo(String idServentiaCargo, List mandadoPrisaoStatusCodigo) throws Exception{
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeMandadoPrisaoServentiaCargo(idServentiaCargo, mandadoPrisaoStatusCodigo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Consultar quantidade de mandados de prisão para a serventia 
	 * @param idServentia: identificação da serventia
	 * @param mandadoPrisaoStatusCodigo: código do status do mandado de prisão
	 * @param consultaMandadoSigiloso: possibilita a consulta de mandado sigiloso
	 * @return
	 */
	public int consultarQuantidadeMandadoPrisaoServentia(String idServentia, List mandadoPrisaoStatusCodigo, boolean consultaMandadoSigiloso) throws Exception{
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
			retorno = obPersistencia.consultarQuantidadeMandadoPrisaoServentia(idServentia, mandadoPrisaoStatusCodigo, consultaMandadoSigiloso);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Expede os mandados de prisão
	 * @param mandadoPrisaoDt: objeto com os dados do mandado de prisão
	 * @throws Exception
	 */
	public MovimentacaoDt expedirMandadoPrisao(MandadoPrisaoDt mandadoPrisaoDt, List listaArquivos, UsuarioDt usuarioDt) throws Exception{
		FabricaConexao obFabricaConexao = null;
		MovimentacaoDt movimentacao = null;		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
			MovimentacaoArquivoNe arquivoNe = new MovimentacaoArquivoNe();

			LogDt logDt = new LogDt(mandadoPrisaoDt.getId_UsuarioLog(), mandadoPrisaoDt.getIpComputadorLog());
			
			//salva o mandado de prisão
			mandadoPrisaoDt.setMandadoPrisaoStatusCodigo(String.valueOf(MandadoPrisaoStatusDt.EXPEDIDO));
			mandadoPrisaoDt.setDataExpedicao(Funcoes.dateToStringSoData(new Date()));
			mandadoPrisaoDt.setIdUsuarioServentiaExpedicao(usuarioDt.getId_UsuarioServentia());
//			mandadoPrisaoDt.setDataAtualizacao(Funcoes.dateToStringSoData(new Date()));
			salvar(mandadoPrisaoDt, obFabricaConexao);
			
			if (!mandadoPrisaoDt.isSigilo()){
				// Gera movimentação MANDADO DE PRISÃO EXPEDIDO
				movimentacao = movimentacaoNe.gerarMovimentacaoMandadoPrisao(mandadoPrisaoDt.getId_Processo(), MovimentacaoTipoDt.MANDADO_PRISAO_EXPEDIDO, usuarioDt, "", logDt, obFabricaConexao);

				// Salva Arquivos já com recibo
				arquivoNe.inserirArquivosSemRecibo(movimentacao.getId(), mandadoPrisaoDt.getProcessoNumeroCompleto(), listaArquivos, logDt, obFabricaConexao);
				
			} else {
				//salva os arquivos sem vínculo com a movimentação.
				new ArquivoNe().inserirArquivos(listaArquivos, logDt, obFabricaConexao);
			}
			
			//vincula os arquivos ao mandado de prisão.
			for (ArquivoDt arquivoDt : (List<ArquivoDt>)listaArquivos) {
				MandadoPrisaoArquivoDt mandadoPrisaoArquivoDt = new MandadoPrisaoArquivoDt();
				mandadoPrisaoArquivoDt.setId_Arquivo(arquivoDt.getId());
				mandadoPrisaoArquivoDt.setId_MandadoPrisao(mandadoPrisaoDt.getId());
				mandadoPrisaoArquivoDt.setId_UsuarioLog(mandadoPrisaoDt.getId_UsuarioLog());
				mandadoPrisaoArquivoDt.setIpComputadorLog(mandadoPrisaoDt.getIpComputadorLog());
				new MandadoPrisaoArquivoNe().salvar(mandadoPrisaoArquivoDt, obFabricaConexao, logDt);
			}
			
			obFabricaConexao.finalizarTransacao();
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return movimentacao;
	}
	
	/**
	 * Emite o mandado de prisão
	 * @param mandadoPrisaoDt
	 * @param usuarioDt
	 * @throws Exception
	 */
	public void emitirMandadoPrisao(MandadoPrisaoDt mandadoPrisaoDt, String idUsuarioServentia) throws Exception{
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			//LogDt obLogDt = new LogDt(mandadoPrisaoDt.getId_UsuarioLog(), mandadoPrisaoDt.getIpComputadorLog());
			
			//salva o mandado de prisão
			mandadoPrisaoDt.setDataEmissao(Funcoes.dateToStringSoData(new Date()));
			mandadoPrisaoDt.setIdUsuarioServentiaEmissao(idUsuarioServentia);
			mandadoPrisaoDt.setMandadoPrisaoStatusCodigo(String.valueOf(MandadoPrisaoStatusDt.EMITIDO));
			mandadoPrisaoDt.setMandadoPrisaoStatus("Emitido");
			salvar(mandadoPrisaoDt, obFabricaConexao);
			
			//LogDt obLogDt = new LogDt("MandadoPrisao",mandadoPrisaoDt.getId(), mandadoPrisaoDt.getId_UsuarioLog(),mandadoPrisaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",mandadoPrisaoDt.getPropriedades());
			//obLog.salvar(obLogDt,obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}	
	
	/**
	 * Emite o mandado de prisão
	 * @param mandadoPrisaoDt
	 * @param usuarioDt
	 * @throws Exception
	 */
	public void imprimirMandadoPrisao(MandadoPrisaoDt mandadoPrisaoDt, String idUsuarioServentia) throws Exception{
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			//LogDt obLogDt = new LogDt(mandadoPrisaoDt.getId_UsuarioLog(), mandadoPrisaoDt.getIpComputadorLog());
			
			//salva o mandado de prisão
			mandadoPrisaoDt.setDataImpressao(Funcoes.dateToStringSoData(new Date()));
			mandadoPrisaoDt.setIdUsuarioServentiaImpressao(idUsuarioServentia);
			mandadoPrisaoDt.setMandadoPrisaoStatusCodigo(String.valueOf(MandadoPrisaoStatusDt.IMPRESSO));
			mandadoPrisaoDt.setMandadoPrisaoStatus("Impresso");
			salvar(mandadoPrisaoDt, obFabricaConexao);
			
			//LogDt obLogDt = new LogDt("MandadoPrisao",mandadoPrisaoDt.getId(), mandadoPrisaoDt.getId_UsuarioLog(),mandadoPrisaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",mandadoPrisaoDt.getPropriedades());
			//obLog.salvar(obLogDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}	
	
	/**
	 * Cumpre ou revoga o mandado de prisão
	 * @param mandadoPrisaoDt: objeto com os dados do mandado de prisão
	 * @param operacao: 1-CUMPRIR, 2-REVOGAR, 3-RETIRAR CARÁTER SIGILOSO
	 * @throws Exception
	 */
	public void cumprir_revogar_retirarSigilo_MandadoPrisao(MandadoPrisaoDt mandadoPrisaoDt, List listaArquivos, UsuarioDt usuarioDt, String idServentia, int operacao) throws Exception{
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
			MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
			int movimentacaoTipo = 0;
			LogDt logDt = new LogDt(mandadoPrisaoDt.getId_UsuarioLog(), mandadoPrisaoDt.getIpComputadorLog());
			
			String complemento = "(Ref. à Mov. Mandado de Prisão Expedido em " + mandadoPrisaoDt.getDataExpedicao() + ", para " + mandadoPrisaoDt.getProcessoParte() + ")";
			
			//salva o mandado de prisão
			if (operacao == 1){//cumprir mandado
				mandadoPrisaoDt.setMandadoPrisaoStatusCodigo(String.valueOf(MandadoPrisaoStatusDt.CUMPRIDO));
//				mandadoPrisaoDt.setDataCumprimento(Funcoes.dateToStringSoData(new Date()));
				mandadoPrisaoDt.setDataCumprimento(mandadoPrisaoDt.getDataPrisao());
				mandadoPrisaoDt.setIdUsuarioServentiaCumprimento(usuarioDt.getId_UsuarioServentia());
				movimentacaoTipo = MovimentacaoTipoDt.MANDADO_PRISAO_CUMPRIDO;
//				complemento = complemento.substring(0, complemento.length() - 1) + ", cumprido em: " +  + ")";
				String comp = "Em " + mandadoPrisaoDt.getDataPrisao() + " " + complemento;
				complemento = comp;
			} else if (operacao == 2){//revogar mandado
				mandadoPrisaoDt.setMandadoPrisaoStatusCodigo(String.valueOf(MandadoPrisaoStatusDt.REVOGADO));
				mandadoPrisaoDt.setDataCumprimento(Funcoes.dateToStringSoData(new Date()));
				mandadoPrisaoDt.setIdUsuarioServentiaCumprimento(usuarioDt.getId_UsuarioServentia());
				movimentacaoTipo = MovimentacaoTipoDt.MANDADO_PRISAO_REVOGADO;
			} else if (operacao == 3){//retirar caráter sigiloso
				mandadoPrisaoDt.setSigilo("false");
				movimentacaoTipo = MovimentacaoTipoDt.MANDADO_PRISAO_EXPEDIDO;
				complemento = "";
				//TODO ver se precisa alterar o status para expedido e salvar o usuário expedidor
			}
//			mandadoPrisaoDt.setDataAtualizacao(Funcoes.dateToStringSoData(new Date()));
			salvar(mandadoPrisaoDt, obFabricaConexao);
			
			// Gera movimentação
			MovimentacaoDt movimentacao = movimentacaoNe.gerarMovimentacaoMandadoPrisao(mandadoPrisaoDt.getId_Processo(), movimentacaoTipo, usuarioDt, complemento, logDt, obFabricaConexao);
			movimentacao.setProcessoNumero(mandadoPrisaoDt.getProcessoNumeroCompleto());

			// Salva Arquivos já com recibo
			movimentacaoArquivoNe.inserirArquivosSemRecibo(movimentacao.getId(), mandadoPrisaoDt.getProcessoNumeroCompleto(), listaArquivos, logDt, obFabricaConexao);
			
			//codigo novo***************************************************************************************************************************************************************************
			/*
			 * Alterações segundo BO 2020/9576
			 * Salvar registro em PROC_PARTE_PRISAO_NOVO em CUMPRIMENTO, RETIRAR_CARATER_SIGILOSO e REVOGAÇÃO (campos dataPrisao e Local Cumprimento estiverem preenchidos)
			 * Se os dois campos não estiverem preenchidos e a operação é REVOGAÇÃO, não gravar registro nessa tabela.
			 */
			if (verificarSalvarProcessoPartePrisaoNovo(operacao, mandadoPrisaoDt.getProcessoPartePrisaoDt())){							
				ProcessoPartePrisaoNe processoPartePrisaoNe = new ProcessoPartePrisaoNe();
				mandadoPrisaoDt.getProcessoPartePrisaoDt().setId_MoviPrisao(movimentacao.getId());
				String strVerificar = processoPartePrisaoNe.Verificar(mandadoPrisaoDt.getProcessoPartePrisaoDt());
				if (strVerificar.length()!= 0){
					throw new MensagemException( strVerificar );
				}
				processoPartePrisaoNe.salvar(mandadoPrisaoDt.getProcessoPartePrisaoDt(), obFabricaConexao);
			}
			//codigo novo***************************************************************************************************************************************************************************
			
			//consulta o mandado de prisão expedido, vincula à movimentação e gera recibo
			if (mandadoPrisaoDt.isSigilo() || operacao == 3){
				ArquivoDt arquivoMandado = new ArquivoNe().consultarArquivo(mandadoPrisaoDt.getId(), String.valueOf(ArquivoTipoDt.MANDADO_PRISAO));
				arquivoMandado.setArquivo(arquivoMandado.conteudoBytes());
				List listaMandado = new ArrayList();
				if (arquivoMandado != null) listaMandado.add(arquivoMandado);
				
				//vincula movimentação ao arquivo
				movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(listaMandado, movimentacao.getId(), String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL), logDt, obFabricaConexao);
				
				// Gera recibo para o arquivo do mandado na movimentação
				List movimentacoes = new ArrayList();
				movimentacoes.add(movimentacao);
				//movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(listaMandado, movimentacoes, obFabricaConexao);
			}
			
			obFabricaConexao.finalizarTransacao();
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	
	public boolean verificarSalvarProcessoPartePrisaoNovo(int operacao, ProcessoPartePrisaoDt dados){
		return (operacao != 2 || (operacao == 2 && dados.getDataPrisao().length() > 0 && dados.getId_LocalCumpPena().length() > 0)); 
	}
	
	public String montaModeloMandadoPrisao(MandadoPrisaoDt mandadoPrisaoDt, UsuarioDt usuarioDt) throws Exception{
		
		String titulo = "";
		if (!mandadoPrisaoDt.getPrisaoTipoCodigo().isEmpty()){
			switch(Funcoes.StringToInt(mandadoPrisaoDt.getPrisaoTipoCodigo())){
				case PrisaoTipoDt.CONDENACAO:
					titulo = "MANDADO DE PRISÃO DEFINITIVA";
					break;
				case PrisaoTipoDt.PREVENTIVA_DECISAO_CONDENATORIA:
					titulo = "MANDADO DE PRISÃO PREVENTIVA DECORRENTE DE DECISÃO CONDENATÓRIA RECORRÍVEL";
					break;
				case PrisaoTipoDt.TEMPORARIA:
					titulo = "MANDADO DE PRISÃO TEMPORÁRIA";
					break;
				case PrisaoTipoDt.PREVENTIVA:
					titulo = "MANDADO DE PRISÃO PREVENTIVA";
					break;
			}				
		}
		if (titulo.length() > 0)
			return new ModeloNe().montaModeloMandadoPrisao(mandadoPrisaoDt, usuarioDt, String.valueOf(ModeloDt.MANDADO_PRISAO), titulo);
		else return "";
		
	}
	
	public ProcessoParteDt consultarProcessoParte(String idProcessoParte) throws Exception{
		ProcessoParteDt processoParte = null;
		
		processoParte = new ProcessoParteNe().consultarId(idProcessoParte);
		
		return processoParte;
	}
	
	public ProcessoDt consultarDadosProcesso(String idProcesso) throws Exception{
		ProcessoDt processo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			processo = new ProcessoNe().consultarIdCompleto(idProcesso, obFabricaConexao);

			// Captura lista de assuntos
			processo.setListaAssuntos(new ProcessoAssuntoNe().consultarAssuntosProcesso(idProcesso, obFabricaConexao));
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return processo;
	}
	
	public String consultarIdArquivoTipo(String arquivoTipoCodigo) throws Exception{
		String id = "";
		id = new ArquivoTipoNe().consultarIdArquivoTipo(arquivoTipoCodigo);
		return id;
	}
	
	public String consultarIdArquivo(String idMandadoPrisao) throws Exception{
		String idArquivo = "";
		idArquivo = new MandadoPrisaoArquivoNe().consultarIdArquivo(idMandadoPrisao);
		return idArquivo;
	}
	
	public boolean baixarArquivo(String idMandadoPrisao, HttpServletResponse response, String idUsuarioLog, String ipComputadorLog) throws Exception {
		LogDt logDt = new LogDt(idUsuarioLog, ipComputadorLog);
		
		String idArquivo = new MandadoPrisaoArquivoNe().consultarIdArquivo(idMandadoPrisao);
		if (idArquivo.length() > 0){
			new ArquivoNe().baixarArquivo(idArquivo, response, logDt, false);
			return true;
		} else return false;
	}
	
	
	 /**
     * Gerar pdf de uma publicação
     * @param String
     *            stIdArquivo, id de um arquivo de uma publicação (pendencia do
     *            tipo publicação)
     * @return byte[] , retorna bytes contendo a publicação em pdf
     * @throws Exception
     * @author jrcorrea
     * 09/09/2014
     */
    public byte[] gerarPdfMAndadoPrissaoAtivo(String diretorioProjeto, String stIdArquivo) throws Exception{
        byte[] byTemp = null;
        byTemp = new PendenciaNe().gerarPdfMandadoPrissaoAtivo(diretorioProjeto, stIdArquivo);
        return byTemp;
    }
    
	/**
	 * Listar os mandados de prisão para a serventia 
	 * @param idServentia: identificação da serventia
	 * @param mandadoPrisaoStatusCodigo: código do status do mandado de prisão
	 * @param consultaMandadoSigiloso: possibilita a consulta de mandado sigiloso
	 * @return
	 */
	public List listarMandadoPrisaoServentia(String idServentia, List mandadoPrisaoStatusCodigo, boolean consultaMandadoSigiloso) throws Exception{
		List lista = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 
			lista = obPersistencia.listarMandadoPrisaoServentia(idServentia, mandadoPrisaoStatusCodigo, consultaMandadoSigiloso);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		
		return lista;
	}
	
	public String consultarGrupoArquivoTipoJSON(String grupoCodigo, String descricao, String posicao ) throws Exception{
		String stTemp = new ArquivoTipoNe().consultarGrupoArquivoTipoJSON(grupoCodigo, descricao, posicao);
		return stTemp;
	}
	
	public String consultarModeloJSON(UsuarioDt usuarioDt, String id_ArquivoTipo, String tempNomeBusca,  String posicaoPaginaAtual) throws Exception{
		String stRetorno = "";		
		
		stRetorno = new ModeloNe().consultarModelosJSON(tempNomeBusca,  posicaoPaginaAtual, id_ArquivoTipo, usuarioDt);
		
		return stRetorno;
	}
	
	public ModeloDt consultarModeloId(String id_Modelo, ProcessoDt processodt, UsuarioDt usuarioDt) throws Exception {
		ModeloDt modeloDt = null;
		ModeloNe modeloNe = new ModeloNe();

		modeloDt = modeloNe.consultarId(id_Modelo);
		modeloDt.setTexto(modeloNe.montaConteudo(id_Modelo, processodt, usuarioDt, ""));

		modeloNe = null;
		return modeloDt;
	}
	
}
