package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.EscalaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class EscalaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7930158045218316809L;
	
	protected  EscalaDt obDados;


	public EscalaNeGen() {
		
		obLog = new LogNe(); 

		obDados = new EscalaDt(); 

	}


//---------------------------------------------------------
	public void salvar(EscalaDt dados ) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neEscalasalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Escala", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Escala", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(EscalaDt dados ); 


//---------------------------------------------------------

	public void excluir(EscalaDt dados) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Escala", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public EscalaDt consultarId(String id_escala ) throws Exception{

		EscalaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_Escala" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_escala ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaEscala" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				//stUltimaConsulta=descricao;
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
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

	public List consultarDescricaoArea(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			AreaNe Areane = new AreaNe(); 
			tempList = Areane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Areane.getQuantidadePaginas();
			Areane = null;
		
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

	}
