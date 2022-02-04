package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoCadinDt;
import br.gov.go.tj.projudi.ps.ProcessoParteDebitoCadinPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProcessoParteDebitoCadinNe extends Negocio {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 585706033592543745L;
	protected  ProcessoParteDebitoCadinDt obDados;

	public ProcessoParteDebitoCadinNe() {
		obLog = new LogNe(); 

		obDados = new ProcessoParteDebitoCadinDt(); 
	}
	
	public String Verificar(ProcessoParteDebitoCadinDt dados) {
		String stRetorno = "";

		if (dados.getId_ProcessoParteDebito() == null || dados.getId_ProcessoParteDebito().length() == 0) stRetorno += "Selecione uma Débito. \n";
		if (dados.getNumeroLote() == null || dados.getNumeroLote().length() == 0) stRetorno += "Informe o Número do Lote. \n ";
		if (dados.getDataGeracao() == null || dados.getDataGeracao().length() == 0) stRetorno += "Informe a Data da Geração. \n ";
		if (dados.getTipoLogradouro() == null || dados.getTipoLogradouro().length() == 0) stRetorno += "Informe o Tipo de Documento. \n ";
		if (dados.getNumeroDocumento() == null || dados.getNumeroDocumento().length() == 0) stRetorno += "Informe o Número do Documento. \n ";
		if (dados.getNomePessoa() == null || dados.getNomePessoa().length() == 0) stRetorno += "Informe o Nome da Pessoa. \n ";
		if (dados.getNumeroProcesso() == null || dados.getNumeroProcesso().length() == 0) stRetorno += "Informe o Número do Processo. \n ";
		if (dados.getNaturezaPendencia() == null || dados.getNaturezaPendencia().length() == 0) stRetorno += "Informe a Natureza da Pendência. \n ";
		if (dados.getCategoriaPendencia() == null || dados.getCategoriaPendencia().length() == 0) stRetorno += "Informe a Categoria da Pendência. \n ";
		if (dados.getDataVencimentoDebito() == null || dados.getDataVencimentoDebito().length() == 0) stRetorno += "Informe a Data de Vencimento do Débito. \n ";
		if (dados.getValorDevido() == null || dados.getValorDevido().length() == 0) stRetorno += "Informe o Valor do Débito. \n ";
		if (dados.getMeioEnvioComunicado() == null || dados.getMeioEnvioComunicado().length() == 0) stRetorno += "Informe o Meio de Envio do Comunicado. \n ";
		if (dados.getTipoLogradouro() == null || dados.getTipoLogradouro().length() == 0) stRetorno += "Informe o Tipo de Logradouro. \n ";
		if (dados.getNomeLogradouro() == null || dados.getNomeLogradouro().length() == 0) stRetorno += "Informe o Nome do Logradouro. \n ";
		if (dados.getCodigoCep() == null || dados.getCodigoCep().length() == 0) stRetorno += "Informe o CEP do Logradouro. \n ";
		
		return stRetorno;
	}

	public void salvar(ProcessoParteDebitoCadinDt dados ) throws Exception {
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			this.salvar(dados, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void salvar(ProcessoParteDebitoCadinDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());

		if (dados.getId().length()==0) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt(dados.isFisico() ? "ProcessoParteDebitoFisicoCadin" : "ProcessoParteDebitoCadin", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt(dados.isFisico() ? "ProcessoParteDebitoFisicoCadin" : "ProcessoParteDebitoCadin", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	}

	
	public void excluir(ProcessoParteDebitoCadinDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt(dados.isFisico() ? "ProcessoParteDebitoFisicoCadin" : "ProcessoParteDebitoCadin", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados); 
			dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluir(ProcessoParteDebitoCadinDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt(dados.isFisico() ? "ProcessoParteDebitoFisicoCadin" : "ProcessoParteDebitoCadin", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluir(dados); 
		dados.limpar();

		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public boolean lotePossuiRegistros(String numeroLote) throws Exception {
		FabricaConexao obFabricaConexao = null;
		boolean retorno = false;
			
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			
			ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.lotePossuiRegistros(numeroLote);
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	public boolean dataGeracaoPossuiRegistros(String data) throws Exception {
		FabricaConexao obFabricaConexao = null;
		boolean retorno = false;
			
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			
			ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.dataGeracaoPossuiRegistros(data);
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	public List<ProcessoParteDebitoCadinDt> obtenhaRegistros(String numeroLote, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
		return obPersistencia.obtenhaRegistros(numeroLote);
	}
	
	public long obtenhaNumeroDoProximoLote(FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
		return obPersistencia.obtenhaNumeroDoProximoLote();
	}
	
	public List<ProcessoParteDebitoCadinDt> obtenhaRegistrosPelaDataDeGeracao(String dataDeGeracao, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
		return obPersistencia.obtenhaRegistrosPelaDataDeGeracao(dataDeGeracao);
	}
	
	public ProcessoParteDebitoCadinDt obtenhaRegistroPJD(String id_ProcessoParteDebito, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
		return obPersistencia.obtenhaRegistro(id_ProcessoParteDebito, false);
	}
	
	public ProcessoParteDebitoCadinDt obtenhaRegistroFisico(String id_ProcessoParteDebito, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
		return obPersistencia.obtenhaRegistro(id_ProcessoParteDebito, true);
	}

 	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}
 	
 	/**
 	 * Método para verificar se id guia está presente na tabela de envio ao CADIN.
 	 * 
 	 * @param String idGuiaEmissao
 	 * @return boolean
 	 * @throws Exception
 	 */
 	public boolean isGuiaEnviadaCadin(String idGuiaEmissao) throws Exception {
 		boolean retorno = false;
 		
 		if( idGuiaEmissao != null && !idGuiaEmissao.isEmpty() ) {
 			
 			FabricaConexao obFabricaConexao = null;
 			
 			try{
 				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
 				
 				ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
 				
 				retorno = obPersistencia.isGuiaEnviadaCadin(idGuiaEmissao);
 			}
 			finally {
 				if( obFabricaConexao != null ) {
 					obFabricaConexao.fecharConexao();
 				}
 			} 			
 		}
 		
 		return retorno;
 	}
 	
 	/**
 	 * Método para verificar se o número da guia está presente na tabela de envio ao CADIN.
 	 * 
 	 * @param String numeroGuia
 	 * @return boolean
 	 * @throws Exception
 	 */
 	public boolean isGuiaSPGSSGEnviadaCadin(String numeroGuia) throws Exception {
 		boolean retorno = false;
 		
 		if(numeroGuia != null && !numeroGuia.isEmpty() ) {
 			
 			FabricaConexao obFabricaConexao = null;
 			
 			try{
 				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
 				
 				ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
 				
 				retorno = obPersistencia.isGuiaSPGSSGEnviadaCadin(numeroGuia);
 			}
 			finally {
 				if( obFabricaConexao != null ) {
 					obFabricaConexao.fecharConexao();
 				}
 			}
 			
 		}
 		
 		return retorno;
 	}
 	
 	/**
 	 * Método para verificar se id processo está presente na tabela de envio ao CADIN.
 	 * 
 	 * @param String idGuiaEmissao
 	 * @return boolean
 	 * @throws Exception
 	 */
 	public boolean isProcessoPossuiGuiaEnviadaCadin(String idProcesso) throws Exception {
 		boolean retorno = false;
 		
 		if( idProcesso != null && !idProcesso.isEmpty() ) {
 			
 			FabricaConexao obFabricaConexao = null;
 			
 			try{
 				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
 				
 				ProcessoParteDebitoCadinPs obPersistencia = new ProcessoParteDebitoCadinPs(obFabricaConexao.getConexao());
 				
 				retorno = obPersistencia.isProcessoPossuiGuiaEnviadaCadin(idProcesso);
 			}
 			finally {
 				if( obFabricaConexao != null ) {
 					obFabricaConexao.fecharConexao();
 				}
 			} 			
 		}
 		
 		return retorno;
 	}
}
