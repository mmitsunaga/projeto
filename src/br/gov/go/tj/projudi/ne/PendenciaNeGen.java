package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.ps.PendenciaPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class PendenciaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4649634953564107513L;	
	protected  PendenciaDt obDados;


	public PendenciaNeGen() {
		
		obLog = new LogNe(); 

		obDados = new PendenciaDt(); 

	}

	// jvosantos - 13/11/2020 11:50 - Criar método de salvar que aceita uma conexão	
	public void salvar(PendenciaDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		/* use o iu do objeto para saber se os dados ja estão ou não salvos */
		if (dados.getId().equalsIgnoreCase("" ) ) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("Pendencia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("Pendencia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	}

//---------------------------------------------------------
	public void salvar(PendenciaDt dados ) throws Exception {

		FabricaConexao obFabricaConexao = null;
		
		//////System.out.println("..nePendenciasalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			// jvosantos - 13/11/2020 11:50 - Chamar método de salvar que aceita uma conexão
			salvar(dados, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(PendenciaDt dados ); 


//---------------------------------------------------------

	public void excluir(PendenciaDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Pendencia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------
	public PendenciaDt consultarId(String id_pendencia, FabricaConexao obFabricaConexao ) throws Exception {

		PendenciaDt dtRetorno=null;
							
		PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.consultarId(id_pendencia );
		
		if( dtRetorno != null )	obDados.copiar(dtRetorno);
		
		
		return dtRetorno;
	}
	
	
	public PendenciaDt consultarId(String id_pendencia ) throws Exception {

		PendenciaDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_Pendencia" );
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_pendencia );
			if( dtRetorno != null )
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
		//////System.out.println("..ne-ConsultaPendencia" ); 
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public List consultarDescricaoPendenciaTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		PendenciaTipoNe PendenciaTipone = new PendenciaTipoNe(); 
		tempList = PendenciaTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = PendenciaTipone.getQuantidadePaginas();
		PendenciaTipone = null;
		return tempList;
	}
	
	public List consultarDescricaoMovimentacao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		MovimentacaoNe Movimentacaone = new MovimentacaoNe(); 
		tempList = Movimentacaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Movimentacaone.getQuantidadePaginas();
		Movimentacaone = null;
		
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

	public List consultarDescricaoProcessoPrioridade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ProcessoPrioridadeNe ProcessoPrioridadene = new ProcessoPrioridadeNe(); 
		tempList = ProcessoPrioridadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoPrioridadene.getQuantidadePaginas();
		ProcessoPrioridadene = null;
		
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

	public List consultarDescricaoPendenciaStatus(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		PendenciaStatusNe PendenciaStatusne = new PendenciaStatusNe(); 
		tempList = PendenciaStatusne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = PendenciaStatusne.getQuantidadePaginas();
		PendenciaStatusne = null;
		
		return tempList;
	}


}
