package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.EventoLocalDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.EventoLocalPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class EventoLocalNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -294123688423666907L;
	protected  EventoLocalDt obDados;


	public EventoLocalNeGen() {
		
		obLog = new LogNe(); 

		obDados = new EventoLocalDt(); 

	}


//---------------------------------------------------------
	public void salvar(EventoLocalDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neEventoLocalsalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			EventoLocalPs obPersistencia = new EventoLocalPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("EventoLocal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());				
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("EventoLocal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(EventoLocalDt dados ); 


//---------------------------------------------------------

	public void excluir(EventoLocalDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			EventoLocalPs obPersistencia = new EventoLocalPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("EventoLocal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public EventoLocalDt consultarId(String id_eventolocal ) throws Exception {

		EventoLocalDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_EventoLocal" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoLocalPs obPersistencia = new EventoLocalPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_eventolocal ); 
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
		//////System.out.println("..ne-ConsultaEventoLocal" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoLocalPs obPersistencia = new EventoLocalPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoProcessoEventoExecucao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ProcessoEventoExecucaoNe ProcessoEventoExecucaone = new ProcessoEventoExecucaoNe(); 
		tempList = ProcessoEventoExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoEventoExecucaone.getQuantidadePaginas();
		ProcessoEventoExecucaone = null;
		
		return tempList;
	}

	public List consultarDescricaoLocalCumprimentoPena(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			LocalCumprimentoPenaNe LocalCumprimentoPenane = new LocalCumprimentoPenaNe(); 
			tempList = LocalCumprimentoPenane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = LocalCumprimentoPenane.getQuantidadePaginas();
			LocalCumprimentoPenane = null;
		
		return tempList;
	}

	}
