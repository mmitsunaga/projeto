package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoDt;
import br.gov.go.tj.projudi.ps.MandadoPrisaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class MandadoPrisaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6038558899383973869L;
	protected  MandadoPrisaoDt obDados;


	public MandadoPrisaoNeGen() {

		obLog = new LogNe(); 

		obDados = new MandadoPrisaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(MandadoPrisaoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MandadoPrisao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("MandadoPrisao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(MandadoPrisaoDt dados ) throws Exception; 


//---------------------------------------------------------

	public void excluir(MandadoPrisaoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("MandadoPrisao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
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

		MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 

			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	

	public MandadoPrisaoDt consultarId(String id_mandadoprisao ) throws Exception {

		MandadoPrisaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_mandadoprisao ); 
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

		MandadoPrisaoPs obPersistencia = new  MandadoPrisaoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoMandadoPrisaoStatus(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		MandadoPrisaoStatusNe MandadoPrisaoStatusne = new MandadoPrisaoStatusNe(); 
		tempList = MandadoPrisaoStatusne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = MandadoPrisaoStatusne.getQuantidadePaginas();
		MandadoPrisaoStatusne = null;
		return tempList;
	}

	public List consultarDescricaoRegimeExecucao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		RegimeExecucaoNe RegimeExecucaone = new RegimeExecucaoNe(); 
		tempList = RegimeExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = RegimeExecucaone.getQuantidadePaginas();
		RegimeExecucaone = null;
		return tempList;
	}

	public List consultarDescricaoPrisaoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		PrisaoTipoNe PrisaoTipone = new PrisaoTipoNe(); 
		tempList = PrisaoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = PrisaoTipone.getQuantidadePaginas();
		PrisaoTipone = null;
		return tempList;
	}

	public List consultarDescricaoProcessoParte(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProcessoParteNe ProcessoPartene = new ProcessoParteNe(); 
		tempList = ProcessoPartene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoPartene.getQuantidadePaginas();
		ProcessoPartene = null;
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

	public List consultarDescricaoMandadoPrisaoOrigem(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		MandadoPrisaoOrigemNe MandadoPrisaoOrigemne = new MandadoPrisaoOrigemNe(); 
		tempList = MandadoPrisaoOrigemne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = MandadoPrisaoOrigemne.getQuantidadePaginas();
		MandadoPrisaoOrigemne = null;
		return tempList;
	}

	public List consultarDescricaoProcesso(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProcessoNe Processone = new ProcessoNe(); 
		tempList = Processone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Processone.getQuantidadePaginas();
		Processone = null;
		return tempList;
	}
}
