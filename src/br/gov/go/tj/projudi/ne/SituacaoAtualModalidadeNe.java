package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualModalidadeDt;
import br.gov.go.tj.projudi.ps.SituacaoAtualModalidadePs;
import br.gov.go.tj.utils.FabricaConexao;

public class SituacaoAtualModalidadeNe extends Negocio{

	private static final long serialVersionUID = 6265703084102951999L;
	protected  SituacaoAtualModalidadeDt obDados;

	public SituacaoAtualModalidadeNe() {
		obLog = new LogNe(); 
		obDados = new SituacaoAtualModalidadeDt(); 
	}

	public void salvar(SituacaoAtualModalidadeDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;
		
		SituacaoAtualModalidadePs obPersistencia = new  SituacaoAtualModalidadePs(obFabricaConexao.getConexao());
		if (dados.getId().equalsIgnoreCase("") ) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("SituacaoAtualModalidade", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());				
		}else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("SituacaoAtualModalidade", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);

		
	}

	public void salvar(SituacaoAtualModalidadeDt dados) throws Exception {

		FabricaConexao obFabricaConexao = null;
		LogDt obLogDt;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			SituacaoAtualModalidadePs obPersistencia = new  SituacaoAtualModalidadePs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("") ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("SituacaoAtualModalidade", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());				
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("SituacaoAtualModalidade", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluir(SituacaoAtualModalidadeDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			SituacaoAtualModalidadePs obPersistencia = new SituacaoAtualModalidadePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("SituacaoAtualModalidade", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void excluir(SituacaoAtualModalidadeDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		SituacaoAtualModalidadePs obPersistencia = new SituacaoAtualModalidadePs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("SituacaoAtualModalidade", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluir(dados.getId()); 
		dados.limpar();
		obLog.salvar(obLogDt, obFabricaConexao);

	}
	
//	public void excluirIdSituacaoAtualExecucao(String idSituacaoAtualExecucao, FabricaConexao obFabricaConexao, UsuarioDt usuarioDt){
//		LogDt obLogDt;
//	SituacaoAtualModalidadePs obPersistencia = new SituacaoAtualModalidadePs(obFabricaConexao.getConexao());
//	obLogDt = new LogDt("SituacaoAtualModalidade", "", usuarioDt.getId_UsuarioLog(),usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),"idSituacaoAtualExecucao: " + idSituacaoAtualExecucao,"");
//	obPersistencia.excluirIdSituacaoAtualExecucao(idSituacaoAtualExecucao); 
//	obLog.salvar(obLogDt, obFabricaConexao);
//	}
	
	public SituacaoAtualModalidadeDt consultarId(String idSituacaoAtualModalidade) throws Exception {

		SituacaoAtualModalidadeDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			SituacaoAtualModalidadePs obPersistencia = new  SituacaoAtualModalidadePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(idSituacaoAtualModalidade); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}

}
