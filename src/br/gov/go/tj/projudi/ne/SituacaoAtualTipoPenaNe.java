package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualTipoPenaDt;
import br.gov.go.tj.projudi.ps.SituacaoAtualTipoPenaPs;
import br.gov.go.tj.utils.FabricaConexao;

public class SituacaoAtualTipoPenaNe extends Negocio{

	private static final long serialVersionUID = 6265703084102951999L;
	protected  SituacaoAtualTipoPenaDt obDados;

	public SituacaoAtualTipoPenaNe() {
		obLog = new LogNe(); 
		obDados = new SituacaoAtualTipoPenaDt(); 
	}

	public void salvar(SituacaoAtualTipoPenaDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;
		
		SituacaoAtualTipoPenaPs obPersistencia = new  SituacaoAtualTipoPenaPs(obFabricaConexao.getConexao());
		if (dados.getId().equalsIgnoreCase("" ) ) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("SituacaoAtualTipoPena", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());				
		}else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("SituacaoAtualTipoPena", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);

	}

	public void salvar(SituacaoAtualTipoPenaDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			SituacaoAtualTipoPenaPs obPersistencia = new  SituacaoAtualTipoPenaPs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("SituacaoAtualTipoPena", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());				
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("SituacaoAtualTipoPena", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluir(SituacaoAtualTipoPenaDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			SituacaoAtualTipoPenaPs obPersistencia = new SituacaoAtualTipoPenaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("SituacaoAtualTipoPena", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluir(SituacaoAtualTipoPenaDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		SituacaoAtualTipoPenaPs obPersistencia = new SituacaoAtualTipoPenaPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("SituacaoAtualTipoPena", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluir(dados.getId()); 
		dados.limpar();

		obLog.salvar(obLogDt, obFabricaConexao);

	}

//	public void excluirIdSituacaoAtualExecucao(String idSituacaoAtualExecucao, FabricaConexao obFabricaConexao, UsuarioDt usuarioDt){
//		LogDt obLogDt;
//	SituacaoAtualTipoPenaPs obPersistencia = new SituacaoAtualTipoPenaPs(obFabricaConexao.getConexao());
//	obLogDt = new LogDt("SituacaoAtualTipoPena", "", usuarioDt.getId_UsuarioLog(),usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),"idSituacaoAtualExecucao: " + idSituacaoAtualExecucao,"");
//	obPersistencia.excluirIdSituacaoAtualExecucao(idSituacaoAtualExecucao); 
//	obLog.salvar(obLogDt, obFabricaConexao);
//	}
	
	public SituacaoAtualTipoPenaDt consultarId(String idSituacaoAtualTipoPena) throws Exception {

		SituacaoAtualTipoPenaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			SituacaoAtualTipoPenaPs obPersistencia = new  SituacaoAtualTipoPenaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(idSituacaoAtualTipoPena); 
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
