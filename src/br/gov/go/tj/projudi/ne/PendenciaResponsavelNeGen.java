package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.ps.PendenciaResponsavelPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class PendenciaResponsavelNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -1248935309808904587L;
	
	
	protected  PendenciaResponsavelDt obDados;
		

	public PendenciaResponsavelNeGen() {
		
		obLog = new LogNe(); 

		obDados = new PendenciaResponsavelDt(); 

	}


//---------------------------------------------------------
	public void salvar(PendenciaResponsavelDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		//////System.out.println("..nePendenciaResponsavelsalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("PendenciaResponsavel", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("PendenciaResponsavel", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(PendenciaResponsavelDt dados ); 


//---------------------------------------------------------

	public void excluir(PendenciaResponsavelDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("PendenciaResponsavel", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public PendenciaResponsavelDt consultarId(String id_pendenciaresponsavel ) throws Exception {

		PendenciaResponsavelDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_PendenciaResponsavel" );
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_pendenciaresponsavel ); 
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
		//////System.out.println("..ne-ConsultaPendenciaResponsavel" ); 
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelPs obPersistencia = new PendenciaResponsavelPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			//stUltimaConsulta=descricao;
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
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

	public List consultarDescricaoServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ServentiaNe Serventiane = new ServentiaNe(); 
			tempList = Serventiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Serventiane.getQuantidadePaginas();
			Serventiane = null;
		
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

	public List consultarDescricaoUsuarioServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			UsuarioServentiaNe UsuarioServentiane = new UsuarioServentiaNe(); 
			tempList = UsuarioServentiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = UsuarioServentiane.getQuantidadePaginas();
			UsuarioServentiane = null;
		
		return tempList;
	}

	public List consultarDescricaoServentiaCargo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe(); 
			tempList = ServentiaCargone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ServentiaCargone.getQuantidadePaginas();
			ServentiaCargone = null;
		
		return tempList;
	}

	}
