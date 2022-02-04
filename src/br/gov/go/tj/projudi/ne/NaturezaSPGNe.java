package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.NaturezaSPGDt;
import br.gov.go.tj.projudi.ps.NaturezaSPGPs;
import br.gov.go.tj.utils.FabricaConexao;

public class NaturezaSPGNe extends Negocio {

	private static final long serialVersionUID = 6007634980654694120L;
	
	private NaturezaSPGDt obDados;
	
	public NaturezaSPGNe() {
		obLog = new LogNe();
		obDados = new NaturezaSPGDt();
	}
	
	/**
	 * Método para consultar NaturezaSPGDt pelo id.
	 * @param String id
	 * @return NaturezaSPGDt
	 * @throws Exception
	 */
	public NaturezaSPGDt consultarId(String id) throws Exception {
		
		NaturezaSPGDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null; 
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			NaturezaSPGPs obPersistencia = new  NaturezaSPGPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id); 
			obDados.copiar(dtRetorno);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return dtRetorno;
	}
	
	/**
	 * Método para consultar JSON da página de localizar.
	 * 
	 * @param String tempNomeBusca
	 * @param String PosicaoPaginaAtual
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoNaturezaSPGJSON(String descricao, String codigoSPG, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			NaturezaSPGPs obPersistencia = new  NaturezaSPGPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoNaturezaSPGJSON(descricao, codigoSPG, PosicaoPaginaAtual);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return stTemp;
	}
	
	/**
	 * Método responsável por validar o dt.
	 * @param dados
	 * @return
	 */
	public  String Verificar(NaturezaSPGDt dados ) {
		String stRetorno="";
		
		if (dados.getNaturezaSPG().equalsIgnoreCase("")){
			stRetorno += "Campo Natureza SPG é obrigatório.";
		}
		if (dados.getNaturezaSPGCodigo().equalsIgnoreCase("")){
			stRetorno += "Campo Código é obrigatório.";
		}		
		return stRetorno;
	}
	
	/**
	 * Método para inserir ou atualizar a NaturezaSPGDt.
	 * @param NaturezaSPGDt dados
	 * @throws Exception
	 */
	public void salvar(NaturezaSPGDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			NaturezaSPGPs obPersistencia = new NaturezaSPGPs(obFabricaConexao.getConexao());
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("NaturezaSPG",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("NaturezaSPG",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método para excluir a NaturezaSPGDt.
	 * @param dados
	 * @throws Exception
	 */
	public void excluir(NaturezaSPGDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			NaturezaSPGPs obPersistencia = new NaturezaSPGPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("NaturezaSPG",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método para consultar NaturezaSPGDt pelo código.
	 * @param String codigo
	 * @return NaturezaSPGDt
	 * @throws Exception
	 */
	public NaturezaSPGDt consultarCodigo(String codigo) throws Exception {
		
		NaturezaSPGDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null; 
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			if( codigo != null ) {
				NaturezaSPGPs obPersistencia = new  NaturezaSPGPs(obFabricaConexao.getConexao());
				dtRetorno = obPersistencia.consultarCodigo(codigo);
				if (obDados != null) obDados.copiar(dtRetorno);
			}
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return dtRetorno;
	}
}
