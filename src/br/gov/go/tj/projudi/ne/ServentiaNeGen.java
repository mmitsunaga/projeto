package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ps.ServentiaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ServentiaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7393921435914594144L;
	
	protected LogNe obLog;
	protected  ServentiaDt obDados;
	protected String stUltimaConsulta ="%";
	protected long QuantidadePaginas = 0; 


	public ServentiaNeGen() {
	
		obLog = new LogNe(); 

		obDados = new ServentiaDt(); 

	}


//---------------------------------------------------------
	public void salvar(ServentiaDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..neServentiasalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Serventia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Serventia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ServentiaDt dados ); 


//---------------------------------------------------------

	public void excluir(ServentiaDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Serventia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ServentiaDt consultarId(String id_serventia ) throws Exception {

		ServentiaDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_Serventia" );
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_serventia ); 
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
		//////System.out.println("..ne-ConsultaServentia" ); 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaPs obPersistencia = new ServentiaPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			//stUltimaConsulta=descricao;
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoServentiaTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ServentiaTipoNe ServentiaTipone = new ServentiaTipoNe(); 
		tempList = ServentiaTipone.consultarDescricao(tempNomeBusca);
		QuantidadePaginas = ServentiaTipone.getQuantidadePaginas();
		ServentiaTipone = null;
		
		return tempList;
	}

	public List consultarDescricaoServentiaSubtipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ServentiaSubtipoNe ServentiaSubtipone = new ServentiaSubtipoNe(); 
			tempList = ServentiaSubtipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ServentiaSubtipone.getQuantidadePaginas();
			ServentiaSubtipone = null;
		
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

	public List consultarDescricaoComarca(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ComarcaNe Comarcane = new ComarcaNe(); 
			tempList = Comarcane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Comarcane.getQuantidadePaginas();
			Comarcane = null;
		
		return tempList;
	}

	public List consultarDescricaoAreaDistribuicao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			AreaDistribuicaoNe AreaDistribuicaone = new AreaDistribuicaoNe(); 
			tempList = AreaDistribuicaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = AreaDistribuicaone.getQuantidadePaginas();
			AreaDistribuicaone = null;
		
		return tempList;
	}

	public List consultarDescricaoEstadoRepresentacao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			EstadoNe Estadone = new EstadoNe(); 
			tempList = Estadone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Estadone.getQuantidadePaginas();
			Estadone = null;
		
		return tempList;
	}

	public List consultarDescricaoAudienciaTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			AudienciaTipoNe AudienciaTipone = new AudienciaTipoNe(); 
			tempList = AudienciaTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = AudienciaTipone.getQuantidadePaginas();
			AudienciaTipone = null;
		
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

	public List consultarDescricaoBairro(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			BairroNe Bairrone = new BairroNe(); 
			tempList = Bairrone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Bairrone.getQuantidadePaginas();
			Bairrone = null;
		
		return tempList;
	}

	}
