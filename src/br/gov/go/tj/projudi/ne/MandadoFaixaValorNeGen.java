package br.gov.go.tj.projudi.ne;

import java.util.List;
import java.util.ArrayList;

import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoFaixaValorDt;
import br.gov.go.tj.projudi.ps.MandadoFaixaValorPs;

//---------------------------------------------------------
public abstract class MandadoFaixaValorNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3317543703773655460L;
	protected  MandadoFaixaValorDt obDados;


	public MandadoFaixaValorNeGen() {

		obLog = new LogNe(); 

		obDados = new MandadoFaixaValorDt(); 

	}


//---------------------------------------------------------
	public void salvar(MandadoFaixaValorDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MandadoFaixaValorPs obPersistencia = new MandadoFaixaValorPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MandadoFaixaValor",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("MandadoFaixaValor",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			System.out.println("..ne-salvar"+ e.getMessage()); 
			throw new Exception(" <{Erro:.....}> MandadoFaixaValorNeGen.salvar() " + e.getMessage() );
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(MandadoFaixaValorDt dados ); 


//---------------------------------------------------------

	public void excluir(MandadoFaixaValorDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MandadoFaixaValorPs obPersistencia = new MandadoFaixaValorPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("MandadoFaixaValor",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

 /**

  * Método para lista as area processuais

 * @author jrcorrea

 */

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			MandadoFaixaValorPs obPersistencia = new MandadoFaixaValorPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public MandadoFaixaValorDt consultarId(String id_mandadofaixavalor ) throws Exception {

		MandadoFaixaValorDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			MandadoFaixaValorPs obPersistencia = new MandadoFaixaValorPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_mandadofaixavalor ); 
			obDados.copiar(dtRetorno);
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


			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

			MandadoFaixaValorPs obPersistencia = new MandadoFaixaValorPs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	}
