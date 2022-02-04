package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.TemaAssuntoDt;
import br.gov.go.tj.projudi.ps.TemaAssuntoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class TemaAssuntoNeGen extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7231078773503002837L;
	protected  TemaAssuntoDt obDados;


	public TemaAssuntoNeGen() {

		obLog = new LogNe(); 

		obDados = new TemaAssuntoDt(); 

	}


//---------------------------------------------------------
	public void salvar(TemaAssuntoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TemaAssuntoPs obPersistencia = new  TemaAssuntoPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("TemaAssunto",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("TemaAssunto",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
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


//---------------------------------------------------------
	 public abstract String Verificar(TemaAssuntoDt dados ); 


//---------------------------------------------------------

	public void excluir(TemaAssuntoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TemaAssuntoPs obPersistencia = new  TemaAssuntoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("TemaAssunto",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){
			obFabricaConexao.cancelarTransacao();
			throw  e;
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
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				TemaAssuntoPs obPersistencia = new  TemaAssuntoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			
			} finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public TemaAssuntoDt consultarId(String id_temaassunto ) throws Exception {

		TemaAssuntoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TemaAssuntoPs obPersistencia = new  TemaAssuntoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_temaassunto ); 
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

				TemaAssuntoPs obPersistencia = new  TemaAssuntoPs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoTema(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		TemaNe Temane = new TemaNe(); 
		tempList = Temane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Temane.getQuantidadePaginas();
		Temane = null;
		
		return tempList;
	}

	public String consultarDescricaoTemaJSON(String descricao, String PosicaoPaginaAtual ) throws Exception{
		String stTemp="";
		
		stTemp = (new TemaNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		
		return stTemp;
	}

	public List consultarDescricaoAssunto(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;

		AssuntoNe Assuntone = new AssuntoNe(); 
		tempList = Assuntone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Assuntone.getQuantidadePaginas();
		Assuntone = null;

		return tempList;
	}

	}
