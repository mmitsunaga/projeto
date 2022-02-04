package br.gov.go.tj.projudi.ne;

import java.util.List;
import java.util.ArrayList;

import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.ps.AssuntoPs;

 import br.gov.go.tj.projudi.ne.AssuntoNe;
//---------------------------------------------------------
public abstract class AssuntoNeGen extends Negocio{


	protected  AssuntoDt obDados;


	public AssuntoNeGen() {

		obLog = new LogNe(); 

		obDados = new AssuntoDt(); 

	}


//---------------------------------------------------------
	public void salvar(AssuntoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AssuntoPs obPersistencia = new AssuntoPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Assunto",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("Assunto",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			System.out.println("..ne-salvar"+ e.getMessage()); 
			throw new Exception(" <{Erro:.....}> AssuntoNeGen.salvar() " + e.getMessage() );
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(AssuntoDt dados ); 


//---------------------------------------------------------

	public void excluir(AssuntoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AssuntoPs obPersistencia = new AssuntoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("Assunto",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			System.out.println("..ne-excluir"+ e.getMessage()); 
			throw new Exception(" <{Erro:.....}> AssuntoNeGen.salvar() " + e.getMessage() );
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
			AssuntoPs obPersistencia = new AssuntoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public AssuntoDt consultarId(String id_assunto ) throws Exception {

		AssuntoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AssuntoPs obPersistencia = new AssuntoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_assunto ); 
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
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

		AssuntoPs obPersistencia = new AssuntoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoAssunto(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			AssuntoNe Assuntone = new AssuntoNe(); 
			tempList = Assuntone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Assuntone.getQuantidadePaginas();
			Assuntone = null;
		return tempList;
	}

	public String consultarDescricaoAssuntoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new AssuntoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	}
