package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.InterrupcaoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.InterrupcaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class InterrupcaoTipoNe extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9082937064504533701L;
	
	protected  InterrupcaoTipoDt obDados;


	public InterrupcaoTipoNe() {
		obLog = new LogNe(); 

		obDados = new InterrupcaoTipoDt();
	}

	public void salvar(InterrupcaoTipoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			InterrupcaoTipoPs obPersistencia = new InterrupcaoTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("InterrupcaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("InterrupcaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String VerificarExclusao(InterrupcaoTipoDt dados) {
		String stRetorno = "";
		
		if (dados.getId().equalsIgnoreCase("") || dados.getInterrupcaoTipo().equalsIgnoreCase("") || dados.getInterrupcaoTotal().equalsIgnoreCase("")) {
			stRetorno += "O Tipo de Interrupção deve ser consultado primeiro.";
		}
				
		return stRetorno;
	}

	public String Verificar(InterrupcaoTipoDt dados) {
		String stRetorno = "";
		if (dados.getInterrupcaoTipo().equalsIgnoreCase("")) {
			stRetorno += "O campo Descrição obrigatório.";
		}
		
		if (dados.getInterrupcaoTotal().equalsIgnoreCase("")) {
			stRetorno += "O campo Interrupção Total é obrigatório.";
		}
		
		return stRetorno;
	}

	public void excluir(InterrupcaoTipoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			InterrupcaoTipoPs obPersistencia = new InterrupcaoTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("InterrupcaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public InterrupcaoTipoDt consultarId(String id_interrupcaotipo ) throws Exception {

		InterrupcaoTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			InterrupcaoTipoPs obPersistencia = new InterrupcaoTipoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_interrupcaotipo ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}	
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			InterrupcaoTipoPs obPersistencia = new InterrupcaoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
}
