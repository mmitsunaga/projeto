package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.ps.MovimentacaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class MovimentacaoTipoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2532565497037136953L;

	
	protected  MovimentacaoTipoDt obDados;
		

	public MovimentacaoTipoNeGen() {
		
		obLog = new LogNe(); 

		obDados = new MovimentacaoTipoDt(); 

	}


//---------------------------------------------------------
	public void salvar(MovimentacaoTipoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neMovimentacaoTiposalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoTipoPs obPersistencia = new MovimentacaoTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MovimentacaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("MovimentacaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(MovimentacaoTipoDt dados ); 


//---------------------------------------------------------

	public void excluir(MovimentacaoTipoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoTipoPs obPersistencia = new MovimentacaoTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("MovimentacaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public MovimentacaoTipoDt consultarId(String id_movimentacaotipo ) throws Exception {

		MovimentacaoTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_MovimentacaoTipo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoTipoPs obPersistencia = new MovimentacaoTipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_movimentacaotipo ); 
			if(dtRetorno != null) {
				obDados.copiar(dtRetorno);	
			}
		}finally{
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
		//////System.out.println("..ne-ConsultaMovimentacaoTipo" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoTipoPs obPersistencia = new MovimentacaoTipoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			//stUltimaConsulta=descricao;
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	}
