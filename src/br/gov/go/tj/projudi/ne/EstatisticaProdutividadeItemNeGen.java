package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.EstatisticaProdutividadeItemPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class EstatisticaProdutividadeItemNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6593393779489652864L;
	
	protected  EstatisticaProdutividadeItemDt obDados;


	public EstatisticaProdutividadeItemNeGen() {
		
		obLog = new LogNe(); 

		obDados = new EstatisticaProdutividadeItemDt(); 

	}


//---------------------------------------------------------
	public void salvar(EstatisticaProdutividadeItemDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neEstatisticaProdutividadeItemsalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			EstatisticaProdutividadeItemPs obPersistencia = new EstatisticaProdutividadeItemPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("EstatisticaProdutividadeItem",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("EstatisticaProdutividadeItem",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(EstatisticaProdutividadeItemDt dados ); 


//---------------------------------------------------------

	public void excluir(EstatisticaProdutividadeItemDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			EstatisticaProdutividadeItemPs obPersistencia = new EstatisticaProdutividadeItemPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("EstatisticaProdutividadeItem",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public EstatisticaProdutividadeItemDt consultarId(String id_estatisticaprodutividadeitem ) throws Exception {

		EstatisticaProdutividadeItemDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_EstatisticaProdutividadeItem" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaProdutividadeItemPs obPersistencia = new EstatisticaProdutividadeItemPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_estatisticaprodutividadeitem ); 
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
		//////System.out.println("..ne-ConsultaEstatisticaProdutividadeItem" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EstatisticaProdutividadeItemPs obPersistencia = new EstatisticaProdutividadeItemPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	}
