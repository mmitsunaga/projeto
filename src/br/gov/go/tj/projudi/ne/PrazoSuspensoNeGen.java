package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PrazoSuspensoDt;
import br.gov.go.tj.projudi.ps.PrazoSuspensoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class PrazoSuspensoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3104536164823776554L;

	
	protected  PrazoSuspensoDt obDados;
		 

	public PrazoSuspensoNeGen() {
			

		obLog = new LogNe(); 

		obDados = new PrazoSuspensoDt(); 

	}


//---------------------------------------------------------
	public void salvar(PrazoSuspensoDt dados ) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..nePrazoSuspensosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("PrazoSuspenso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("PrazoSuspenso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(PrazoSuspensoDt dados ) throws Exception; 


//---------------------------------------------------------

	public void excluir(PrazoSuspensoDt dados) throws Exception{

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("PrazoSuspenso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public PrazoSuspensoDt consultarId(String id_prazosuspenso ) throws Exception{

		PrazoSuspensoDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_PrazoSuspenso" );

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_prazosuspenso ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception{
		List tempList=null;
		//////System.out.println("..ne-ConsultaPrazoSuspenso" ); 

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			//stUltimaConsulta=descricao;
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoPrazoSuspensoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		PrazoSuspensoTipoNe PrazoSuspensoTipone = new PrazoSuspensoTipoNe(); 
		tempList = PrazoSuspensoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = PrazoSuspensoTipone.getQuantidadePaginas();
		PrazoSuspensoTipone = null;
		
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

	public List consultarDescricaoServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ServentiaNe Serventiane = new ServentiaNe(); 
			tempList = Serventiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Serventiane.getQuantidadePaginas();
			Serventiane = null;
		
		return tempList;
	}

	public List consultarDescricaoCidade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			CidadeNe Cidadene = new CidadeNe(); 
			tempList = Cidadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Cidadene.getQuantidadePaginas();
			Cidadene = null;
		
		return tempList;
	}

	}
