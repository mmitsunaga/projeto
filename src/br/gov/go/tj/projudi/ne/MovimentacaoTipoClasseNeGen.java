package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoClasseDt;
import br.gov.go.tj.projudi.ps.MovimentacaoTipoClassePs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class MovimentacaoTipoClasseNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7443377723715963075L;
	protected  MovimentacaoTipoClasseDt obDados;


	public MovimentacaoTipoClasseNeGen() {
		
		obLog = new LogNe(); 

		obDados = new MovimentacaoTipoClasseDt(); 

	}


//---------------------------------------------------------
	public void salvar(MovimentacaoTipoClasseDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;
		//System.out.println("..neMovimentacaoTipoClassesalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoClassePs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja est�o ou n�o salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MovimentacaoTipoClasse",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("MovimentacaoTipoClasse",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(MovimentacaoTipoClasseDt dados ); 


//---------------------------------------------------------

	public void excluir(MovimentacaoTipoClasseDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoClassePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("MovimentacaoTipoClasse",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public MovimentacaoTipoClasseDt consultarId(String id_movimentacaotipoclasse ) throws Exception {

		MovimentacaoTipoClasseDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//System.out.println("..ne-ConsultaId_MovimentacaoTipoClasse" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoClassePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_movimentacaotipoclasse ); 
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
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoClassePs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

}
