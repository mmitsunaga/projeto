package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ParametroComutacaoExecucaoDt;
import br.gov.go.tj.projudi.ps.ParametroComutacaoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class ParametroComutacaoExecucaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -913207415247553097L;
	protected  ParametroComutacaoExecucaoDt obDados;


	public ParametroComutacaoExecucaoNeGen() {
		

		obLog = new LogNe(); 

		obDados = new ParametroComutacaoExecucaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ParametroComutacaoExecucaoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;
		//System.out.println("..neParametroComutacaoExecucaosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ParametroComutacaoExecucaoPs obPersistencia = new ParametroComutacaoExecucaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ParametroComutacaoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ParametroComutacaoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ParametroComutacaoExecucaoDt dados ); 


//---------------------------------------------------------

	public void excluir(ParametroComutacaoExecucaoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ParametroComutacaoExecucaoPs obPersistencia = new ParametroComutacaoExecucaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ParametroComutacaoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ParametroComutacaoExecucaoDt consultarId(String id_parametrocomutacaoexecucao ) throws Exception {

		ParametroComutacaoExecucaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//System.out.println("..ne-ConsultaId_ParametroComutacaoExecucao" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ParametroComutacaoExecucaoPs obPersistencia = new ParametroComutacaoExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_parametrocomutacaoexecucao ); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;
		//System.out.println("..ne-ConsultaParametroComutacaoExecucao" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ParametroComutacaoExecucaoPs obPersistencia = new ParametroComutacaoExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
}
