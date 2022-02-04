package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.ps.ProcessoPartePs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoParteNeGen extends Negocio{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4674974430111036314L;
	protected  ProcessoParteDt obDados;


	public ProcessoParteNeGen() {
			
		obLog = new LogNe(); 

		obDados = new ProcessoParteDt(); 

	}


//---------------------------------------------------------
	public void salvar(ProcessoParteDt dados ) throws Exception {

		LogDt obLogDt;

		//////System.out.println("..neProcessoPartesalvar()");
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoParte",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoParte",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){
			obFabricaConexao.cancelarTransacao();
//			////System.out.println("..ne-salvar"+ e.getMessage()); 
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoParteDt dados )  throws Exception; 


//---------------------------------------------------------

	public void excluir(ProcessoParteDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParte",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoParteDt consultarId(String id_processoparte ) throws Exception {

		ProcessoParteDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_ProcessoParte" );
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processoparte ); 
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
		//////System.out.println("..ne-ConsultaProcessoParte" ); 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPartePs obPersistencia = new ProcessoPartePs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoProcessoParteTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProcessoParteTipoNe ProcessoParteTipone = new ProcessoParteTipoNe(); 
		tempList = ProcessoParteTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoParteTipone.getQuantidadePaginas();
		ProcessoParteTipone = null;
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

	public List consultarDescricaoProcessoParteAusencia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProcessoParteAusenciaNe ProcessoParteAusenciane = new ProcessoParteAusenciaNe(); 
		tempList = ProcessoParteAusenciane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoParteAusenciane.getQuantidadePaginas();
		ProcessoParteAusenciane = null;
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

	public List consultarDescricaoEstadoCivil(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		EstadoCivilNe EstadoCivilne = new EstadoCivilNe(); 
		tempList = EstadoCivilne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = EstadoCivilne.getQuantidadePaginas();
		EstadoCivilne = null;
		return tempList;
	}

	public List consultarDescricaoProfissao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProfissaoNe Profissaone = new ProfissaoNe(); 
		tempList = Profissaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Profissaone.getQuantidadePaginas();
		Profissaone = null;
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

	public List consultarDescricaoRgOrgaoExpedidor(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		RgOrgaoExpedidorNe RgOrgaoExpedidorne = new RgOrgaoExpedidorNe(); 
		tempList = RgOrgaoExpedidorne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = RgOrgaoExpedidorne.getQuantidadePaginas();
		RgOrgaoExpedidorne = null;
		return tempList;
	}

	public List consultarDescricaoEscolaridade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		EscolaridadeNe Escolaridadene = new EscolaridadeNe(); 
		tempList = Escolaridadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Escolaridadene.getQuantidadePaginas();
		Escolaridadene = null;
		return tempList;
	}

	public List consultarDescricaoEstado(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		EstadoNe Estadone = new EstadoNe(); 
		tempList = Estadone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Estadone.getQuantidadePaginas();
		Estadone = null;
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

	public List consultarDescricaoGovernoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		GovernoTipoNe GovernoTipone = new GovernoTipoNe(); 
		tempList = GovernoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = GovernoTipone.getQuantidadePaginas();
		GovernoTipone = null;
		return tempList;
	}

	public List consultarDescricaoEmpresaTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		EmpresaTipoNe EmpresaTipone = new EmpresaTipoNe(); 
		tempList = EmpresaTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = EmpresaTipone.getQuantidadePaginas();
		EmpresaTipone = null;
		return tempList;
	}
}
