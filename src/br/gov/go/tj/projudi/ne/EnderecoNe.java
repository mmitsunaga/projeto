package br.gov.go.tj.projudi.ne;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.EnderecoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class EnderecoNe extends EnderecoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8089615638367031022L;

	/**
	 * Valida dados de Endere�o
	 */
	public String Verificar(EnderecoDt dados) {
		String stRetorno = "";

		if (dados.getLogradouro().length() == 0) stRetorno += "Logradouro � campo requerido. \n";
		if (dados.getId_Bairro().length() == 0) stRetorno += "Bairro � campo requerido. \n";
		if (dados.getCep().length() == 0) stRetorno += "CEP � campo requerido. \n";
		else if (!Funcoes.validaNumerico(dados.getCep())) stRetorno += "Digite apenas n�meros para o CEP. \n";
		else if (dados.getCep().length() != 8) stRetorno += "O CEP deve ter no m�nimo oito d�gitos. \n";
		return stRetorno;
	}

	/**
	 * Valida dados de Endere�o de uma parte do processo.
	 * 
	 * @param dados, dt de endere�o
	 * @param parteNaoPersonificavel, define se parte � n�o personific�vel pois nesse caso n�o deve validar o endere�o
	 * 
	 * @author msapaula
	 */
	public String verificarEnderecoParte(EnderecoDt dados, boolean parteNaoPersonificavel, String areaCodigo, boolean parteEnderecoDesconhecido) {
		String stRetorno = "";

		if (!parteNaoPersonificavel && !parteEnderecoDesconhecido) {
			if (dados.getLogradouro().trim().length() == 0) stRetorno += "Logradouro � campo requerido. \n";
			if (dados.getId_Bairro().trim().length() == 0) stRetorno += "Bairro � campo requerido. \n";
			if (areaCodigo == null || areaCodigo.equals(AreaDt.CRIMINAL)) {
				if (dados.getCep().trim().length() == 0) stRetorno += "CEP � campo requerido. \n";
				else if (!Funcoes.validaNumerico(dados.getCep().trim()) ) stRetorno += "Digite apenas n�meros para o CEP. \n";
				else if (dados.getCep().trim().length() != 8) stRetorno += "O CEP deve ter no m�nimo oito d�gitos. \n";
			}
		}
		return stRetorno;
	}
	
	/**
	 * Valida dados de Endere�o de uma parte do processo.
	 * 
	 * @param dados, dt de endere�o
	 * @param parteNaoPersonificavel, define se parte � n�o personific�vel pois nesse caso n�o deve validar o endere�o
	 * 
	 * @author lsberndes
	 * @throws Exception 
	 */
	public String verificarEnderecoParte(EnderecoDt dados, boolean parteNaoPersonificavel, String areaCodigo, boolean parteEnderecoDesconhecido, LocomocaoDt locomocaoDt) throws Exception {
		String stRetorno = "";

		if (!parteNaoPersonificavel && !parteEnderecoDesconhecido) {
			if (dados.getLogradouro().trim().length() == 0) stRetorno += "Logradouro � campo requerido. \n";
			if (dados.getId_Bairro().trim().length() == 0) stRetorno += "Bairro � campo requerido. \n";
			if (areaCodigo == null || areaCodigo.equals(AreaDt.CRIMINAL)) {
				if (dados.getCep().trim().length() == 0) stRetorno += "CEP � campo requerido. \n";
				else if (!Funcoes.validaNumerico(dados.getCep().trim()) ) stRetorno += "Digite apenas n�meros para o CEP. \n";
				else if (dados.getCep().trim().length() != 8) stRetorno += "O CEP deve ter no m�nimo oito d�gitos. \n";
			}
			if (locomocaoDt != null && locomocaoDt.getQtdLocomocao()>0 && dados.getId_Bairro().trim().length() > 0 && !(new GuiaEmissaoNe().isBairroZoneado(dados.getId_Bairro())) ){
				stRetorno += "Bairro n�o est� zoneado, n�o � poss�vel inserir locomo��o. \n";
			}
		}
		return stRetorno;
	}
	
	public void salvar(EnderecoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		EnderecoPs obPersistencia = new EnderecoPs(obFabricaConexao.getConexao());
		
		EnderecoDt dadosConsultados = consultarCompleto(dados.getId_Bairro(),dados.getLogradouro(), dados.getNumero(), dados.getComplemento(), dados.getCep(), obFabricaConexao );
		if (dadosConsultados==null) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("Endereco", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());    		
    		obLog.salvar(obLogDt, obFabricaConexao);
		}else {
			dados.copiar(dadosConsultados);
		}
		obDados.copiar(dados);		
	}

	/**
	 * Consulta dados do endere�o, baseado em uma conex�o j� existente
	 * @param id_Endereco
	 * @param conexao
	 * @return
	 */
	private EnderecoDt consultarId(String id_Endereco, FabricaConexao obFabricaConexao) throws Exception {
		EnderecoDt dtRetorno = null;
		
		EnderecoPs obPersistencia = new EnderecoPs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.consultarId(id_Endereco);
		obDados.copiar(dtRetorno);
		
		return dtRetorno;
	}

	/**
	 * Insere um novo endere�o
	 * @param dados
	 * @param obfabricaconexao
	 * @throws Exception
	 */
	public void inserir(EnderecoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		EnderecoPs obPersistencia = new EnderecoPs(obFabricaConexao.getConexao());
		if (dados.getId().equalsIgnoreCase("")) {
			obLogDt = new LogDt("Endereco", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			obPersistencia.inserir(dados);

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
		}
		
	}

	/**
	 * Salva um endere�o baseado nos par�metros passados
	 * @param enderecoDt
	 * @param logDt
	 * @param conexao
	 */
	public String salvar(EnderecoDt enderecoDt, LogDt logDt, FabricaConexao conexao) throws Exception {
		enderecoDt.setId_UsuarioLog(logDt.getId_Usuario());
		enderecoDt.setIpComputadorLog(logDt.getIpComputador());
		this.salvar(enderecoDt, conexao);
		return enderecoDt.getId();
	}
	
	/**
	 * M�todo que exclui um endere�o. A diferen�a entre esse m�todo excluir e o padr�o � que este utilizar� uma conex�o passada pelo 
	 * sistema e n�o criar� uma conex�o.
	 * @param enderecoDt - Endere�o a ser exclu�do
	 * @param obFabricaConexao - conex�o a ser utilizada
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void excluir(EnderecoDt enderecoDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		EnderecoPs obPersistencia = new EnderecoPs(obFabricaConexao.getConexao());
		if (!enderecoDt.getId().equalsIgnoreCase("")) {
			obPersistencia.excluir(enderecoDt.getId());
			obLogDt = new LogDt("Endereco", enderecoDt.getId(), enderecoDt.getId_UsuarioLog(), enderecoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", enderecoDt.getPropriedades());
			obLog.salvar(obLogDt, obFabricaConexao);
		}
		
	}

	public EnderecoDt consultarCompleto(String id_Bairro, String logradouro, String numero, String complemento, String cep , FabricaConexao obFabricaConexao) throws Exception {
		EnderecoPs obPersistencia = new EnderecoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarCompleto( id_Bairro,  logradouro,  numero,  complemento, cep);
	}
	
	public void salvar(EnderecoDt dados, Connection connection) throws Exception {
		LogDt obLogDt;
		
		EnderecoPs obPersistencia = new EnderecoPs(connection);
		if (dados.getId().equalsIgnoreCase("")) {
			
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("Endereco", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {
			//Dados anteriores da parte
			obDados = this.consultarId(dados.getId(), connection);
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("Endereco", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, connection);
		
	}
	
	private EnderecoDt consultarId(String id_Endereco, Connection connection) throws Exception {
		EnderecoDt dtRetorno = null;
		
		EnderecoPs obPersistencia = new EnderecoPs(connection);
		dtRetorno = obPersistencia.consultarId(id_Endereco);
		obDados.copiar(dtRetorno);
		
		return dtRetorno;
	}
}
