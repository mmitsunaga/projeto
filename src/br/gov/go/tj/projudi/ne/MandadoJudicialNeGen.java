package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.ps.MandadoJudicialPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class MandadoJudicialNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 5759789838249316711L;	
	protected  MandadoJudicialDt obDados;


	public MandadoJudicialNeGen() {	

		obLog = new LogNe(); 

		obDados = new MandadoJudicialDt(); 

	}


//---------------------------------------------------------
	public void salvar(MandadoJudicialDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MandadoJudicial",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("MandadoJudicial",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(MandadoJudicialDt dados ); 


//---------------------------------------------------------

	public void excluir(MandadoJudicialDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("MandadoJudicial",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public MandadoJudicialDt consultarId(String id_mandadojudicial ) throws Exception {

		MandadoJudicialDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_mandadojudicial ); 
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

			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoMandadoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		MandadoTipoNe MandadoTipone = new MandadoTipoNe(); 
		tempList = MandadoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = MandadoTipone.getQuantidadePaginas();
		MandadoTipone = null;
		return tempList;
	}

	public List consultarDescricaoMandadoJudicialStatus(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		MandadoJudicialStatusNe MandadoJudicialStatusne = new MandadoJudicialStatusNe(); 
		tempList = MandadoJudicialStatusne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = MandadoJudicialStatusne.getQuantidadePaginas();
		MandadoJudicialStatusne = null;
		return tempList;
	}

	public List consultarDescricaoArea(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		AreaNe Areane = new AreaNe(); 
		tempList = Areane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Areane.getQuantidadePaginas();
		Areane = null;
		return tempList;
	}

	public List consultarDescricaoZona(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ZonaNe Zonane = new ZonaNe(); 
		tempList = Zonane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Zonane.getQuantidadePaginas();
		Zonane = null;
		return tempList;
	}

	public List consultarDescricaoRegiao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		RegiaoNe Regiaone = new RegiaoNe(); 
		tempList = Regiaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Regiaone.getQuantidadePaginas();
		Regiaone = null;
		return tempList;
	}

	public List consultarDescricaoBairro(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		BairroNe Bairrone = new BairroNe(); 
		tempList = Bairrone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Bairrone.getQuantidadePaginas();
		Bairrone = null;
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

	public List consultarDescricaoEndereco(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		EnderecoNe Enderecone = new EnderecoNe(); 
		tempList = Enderecone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Enderecone.getQuantidadePaginas();
		Enderecone = null;
		return tempList;
	}

	public List consultarDescricaoPendencia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		PendenciaNe Pendenciane = new PendenciaNe(); 
		tempList = Pendenciane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Pendenciane.getQuantidadePaginas();
		Pendenciane = null;
		return tempList;
	}

	public List consultarDescricaoEscala(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		EscalaNe Escalane = new EscalaNe(); 
		tempList = Escalane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Escalane.getQuantidadePaginas();
		Escalane = null;
		return tempList;
	}

	public List consultarDescricaoUsuarioServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		UsuarioServentiaNe UsuarioServentiane = new UsuarioServentiaNe(); 
		tempList = UsuarioServentiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = UsuarioServentiane.getQuantidadePaginas();
		UsuarioServentiane = null;
		return tempList;
	}
}
