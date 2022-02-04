package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.EscolaridadeDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.EventoLocalDt;
import br.gov.go.tj.projudi.dt.EventoRegimeDt;
import br.gov.go.tj.projudi.dt.ForumDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteSinalDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ProfissaoDt;
import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualExecucaoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualModalidadeDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualTipoPenaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoExecucaoBeanDt;
import br.gov.go.tj.projudi.ps.CidadePs;
import br.gov.go.tj.projudi.ps.MovimentacaoPs;
import br.gov.go.tj.projudi.ps.ProcessoExecucaoPs;
import br.gov.go.tj.projudi.ps.RgOrgaoExpedidorPs;
import br.gov.go.tj.projudi.util.DistribuicaoProcesso;
import br.gov.go.tj.projudi.util.ProcessoNumero;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.pdf.InterfaceJasper;


//---------------------------------------------------------
public class ProcessoExecucaoNe extends ProcessoExecucaoNeGen {

//

/**
     * 
     */
    private static final long serialVersionUID = -303900261697942741L;

    //---------------------------------------------------------
	public String Verificar(ProcessoExecucaoDt dados) {

		String stRetorno = "";

		if (dados.getNumeroAcaoPenal().length() == 0) stRetorno += "O campo N�mero do Processo de A��o Penal � obrigat�rio.";
		//if (dados.getDataDistribuicao().length() == 0) stRetorno += "O campo Data da Distribui��o � obrigat�rio.";
		if (dados.getDataDenuncia().length() == 0) stRetorno += "O campo Data de Den�ncia � obrigat�rio.";
		if (dados.getDataSentenca().length() == 0) stRetorno += "O campo Data da Senten�a � obrigat�rio.";
		if (dados.getDataTransitoJulgado().length() == 0) stRetorno += "O campo Data do Tr�nsito em Julgado � obrigat�rio.";

		////System.out.println("..neProcessoExecucaoVerificar()");
		return stRetorno;

	}
	
	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 */
	public ProcessoParteDt consultarParte(String tempNomeBusca) throws Exception {
		ProcessoParteDt tempList = null;
		ProcessoParteNe neObjeto = new ProcessoParteNe();
		
		tempList = neObjeto.consultarIdCompleto(tempNomeBusca);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();

		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoComarca(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		ComarcaNe neObjeto = new ComarcaNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}
	
	public ComarcaDt consultarIdComarca(String idComarca) throws Exception{
		ComarcaNe neObjeto = new ComarcaNe();
		ComarcaDt comarcaDt = null;
		
		comarcaDt = neObjeto.consultarId(idComarca);
				
		neObjeto = null;
		return comarcaDt;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 */
	public String consultarCondenacaoSituacaoId(String id_Situacao) throws Exception {
		String descricaoSituacao = "";
		CondenacaoExecucaoNe condenacaoNe = new CondenacaoExecucaoNe();
		
		descricaoSituacao = condenacaoNe.consultarCondenacaoSituacao(id_Situacao);
				
		condenacaoNe = null;
		return descricaoSituacao;
	}


	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 */
	public ModeloDt consultarModeloId(String id_Modelo, ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		ModeloDt modeloDt = null;
		ModeloNe modeloNe = new ModeloNe();
		
		modeloDt = modeloNe.consultarId(id_Modelo);
		modeloDt.setTexto(modeloNe.montaConteudo(id_Modelo, processoDt, usuarioDt, ""));		
		
		modeloNe = null;
		return modeloDt;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
//	public List consultarDescricaoPenaExecucaoTipo(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
//		List tempList = null;
//		PenaExecucaoTipoNe neObjeto = new PenaExecucaoTipoNe();
//	tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
//	QuantidadePaginas = neObjeto.getQuantidadePaginas();
//		neObjeto = null;
//		return tempList;
//	}
	
	public String consultarIdsPenaExecucaoTipoJSON(String id_opcoes) throws Exception{
        String stTemp ="";               
                               
        stTemp = new PenaExecucaoTipoNe().consultarIdsJSON(id_opcoes);                       
                
        return stTemp;
    }

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarIdsPenaExecucaoTipo(String id_opcoes) throws Exception{
		List tempList = null;
		PenaExecucaoTipoNe neObjeto = new PenaExecucaoTipoNe();
		
		tempList = neObjeto.consultarIds(id_opcoes);
		
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoRegimeExecucao(String id_PenaExecucaoTipo) throws Exception{
		List tempList = null;
		RegimeExecucaoNe neObjeto = new RegimeExecucaoNe();
		
		tempList = neObjeto.consultarDescricao("", id_PenaExecucaoTipo, "");
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoCrimeExecucao(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		CrimeExecucaoNe neObjeto = new CrimeExecucaoNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}
	
	public String consultarDescricaoCrimeExecucaoJSON(String crime, String lei, String artigo, String posicao ) throws Exception{
        
		String stTemp = new CrimeExecucaoNe().consultarDescricaoJSON(crime, lei, artigo, posicao);                       
                
        return stTemp;
    }

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoLocalCumprimentoPena() throws Exception{
		
		List tempList = new LocalCumprimentoPenaNe().consultarDescricao("", "");
				
		return tempList;
	}

	public List consultarDescricaoFormaCumprimentoExecucao() throws Exception{
		
		List tempList = new FormaCumprimentoExecucaoNe().consultarDescricao("", "");
				
		return tempList;
	}
	
	public List consultarDescricaoEventoExecucaoStatus(boolean isMostrarNaoAplica) throws Exception{
		
		 List	tempList = new EventoExecucaoStatusNe().consultarDescricao("", "", isMostrarNaoAplica);
				
		return tempList;
	}
	
	public String consultarDescricaoLocalCumprimentoPenaJSON(String descricao, String posicao ) throws Exception{
                      
                            
         String  stTemp = new LocalCumprimentoPenaNe().consultarDescricaoJSON(descricao, posicao);                       
        
        
        return stTemp;
    }
	
	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoBairro(String tempNomeBusca, String cidade, String uf, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		BairroNe neObjeto = new BairroNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, cidade, uf, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;

	}
	
	public String consultarDescricaoBairroJSON(String descricao, String cidade, String uf, String posicao) throws Exception {
		String stTemp = "";
		
		BairroNe Bairrone = new BairroNe();
		stTemp = Bairrone.consultarDescricaoJSON(descricao, cidade, uf, "", posicao);
		
		return stTemp;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoEstado(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		EstadoNe neObjeto = new EstadoNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarAreasDistribuicaoExecucao(String tempNomeBusca, String id_Comarca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		
		tempList = neObjeto.consultarAreasDistribuicaoExecucao(tempNomeBusca, id_Comarca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}
	
	public String consultarAreasDistribuicaoExecucaoJSON(String tempNomeBusca, String id_Comarca, String posicaoPaginaAtual) throws Exception{
		String stTemp ="";
            AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
    		stTemp = neObjeto.consultarAreasDistribuicaoExecucaoJSON(tempNomeBusca, id_Comarca, posicaoPaginaAtual);
		return stTemp;
	}

	/**
	 * M�todo respons�vel por consultar a Area de Distribui��o a partir do id_areaDistribuicao 
	 * @param id_areaDistribuicao - Id da Area de Distribuicao 
	 * @throws Exception 
	 */
	public AreaDistribuicaoDt consultarAreaDistribuicao(String id_areaDistribuicao) throws Exception{
		AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
		try{
			return areaDistribuicaoNe.consultarId(id_areaDistribuicao);
		
		} finally{
			areaDistribuicaoNe = null;
		}
	}
	
	/**
	 * M�todo respons�vel por consultar a serventia a partir do id_serventia 
	 * @param idServentia - Id da Serventia 
	 * @throws Exception 
	 */
	public ServentiaDt consultarServentia(String id_serventia) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe();
		try{
			return serventiaNe.consultarId(id_serventia);
		
		} finally{
			serventiaNe = null;
		}
	}
	
	/**
	 * Chama m�todo que realizar� a consulta
	 */
	public List consultarServentiaCargos(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		List tempList = null;
		
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		tempList = ServentiaCargone.consultarServentiaCargos(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubtipoCodigo);
		QuantidadePaginas = ServentiaCargone.getQuantidadePaginas();
		ServentiaCargone = null;
				
		return tempList;
	}
	
	public String consultarServentiaCargosJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		String stTemp = "";
		
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		stTemp = ServentiaCargone.consultarServentiaCargosJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubtipoCodigo);
		ServentiaCargone = null;
				
		return stTemp;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoProcessoTipo(String tempNomeBusca, String id_AreaDistribuicao, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		ServentiaSubtipoProcessoTipoNe neObjeto = new ServentiaSubtipoProcessoTipoNe();
		
		tempList = neObjeto.consultarProcessoTipos(id_AreaDistribuicao, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarModelo(UsuarioDt usuarioDt, String id_ArquivoTipo, String descricao, String posicao) throws Exception{
		List tempList = null;
		ModeloNe neObjeto = new ModeloNe();
		
		tempList = neObjeto.consultarModelos(descricao, posicao, id_ArquivoTipo, usuarioDt);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 */
	public List consultarGrupoArquivoTipo(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		
		tempList = neObjeto.consultarGrupoArquivoTipo(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	public String consultarGrupoArquivoTipoJSON(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		
		stTemp = neObjeto.consultarGrupoArquivoTipoJSON(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);			
				
		neObjeto = null;
		return stTemp;
	}
	
	/**
	 * Lista as partes que possuem processo de execu��o penal em qualquer serventia.
	 * @throws Exception 
	 */
	public List listarPartes(String cpf, String nome, String mae, String dataNascimento, String numeroProcesso, String posicaoPaginaAtual) throws Exception{
		List processoParteDt = null;
		ProcessoParteNe neObjeto = new ProcessoParteNe();
		
		processoParteDt = neObjeto.listarPartesProcessoExecucao(cpf, nome, mae, dataNascimento, numeroProcesso, "", posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return processoParteDt;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoOrgaoExpedidor(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		RgOrgaoExpedidorNe neObjeto = new RgOrgaoExpedidorNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}
	
	public String consultarDescricaoOrgaoExpedidorJSON(String sigla, String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RgOrgaoExpedidorPs obPersistencia = new RgOrgaoExpedidorPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(sigla, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoProfissao(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		ProfissaoNe neObjeto = new ProfissaoNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	public String consultarDescricaoProfissaoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		String stTemp ="";
		ProfissaoNe Profissaone = new ProfissaoNe(); 
		stTemp = Profissaone.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		return stTemp;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoEscolaridade(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		EscolaridadeNe neObjeto = new EscolaridadeNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoSinalParticular(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		SinalNe neObjeto = new SinalNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoAlcunha(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		AlcunhaNe neObjeto = new AlcunhaNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoEstadoCivil(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		EstadoCivilNe neObjeto = new EstadoCivilNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoCidade(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		CidadeNe neObjeto = new CidadeNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoCidade(String tempNomeBusca, String uf, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		CidadeNe neObjeto = new CidadeNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, uf, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoProcessoPrioridade(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception{
		List tempList = null;
		ProcessoPrioridadeNe neObjeto = new ProcessoPrioridadeNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();

		neObjeto = null;
		return tempList;
	}
	
	public String consultarDescricaoProcessoPrioridadeJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = "";
		ProcessoPrioridadeNe ProcessoPrioridadene = new ProcessoPrioridadeNe(); 
		stTemp = ProcessoPrioridadene.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		ProcessoPrioridadene = null;
		return stTemp;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoAssunto(String tempNomeBusca, String id_AreaDistribuicao, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();
		
		tempList = neObjeto.consultarAssuntosAreaDistribuicao(id_AreaDistribuicao, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Verifica dados dos processos que ser�o vinculados no momento do cadastro.
	 * @param processoSelecionado
	 * @param processoCadastro
	 * @return
	 * @throws Exception
	 */
	public String verificarVinculoProcesso(ProcessoDt processoSelecionado, ProcessoExecucaoDt processoCadastro, String id_ServentiaDoUsuario){
		String retorno = "";
		
		if (!(processoSelecionado.getProcessoStatusCodigo().equals(String.valueOf(ProcessoStatusDt.ATIVO))
		|| processoSelecionado.getProcessoStatusCodigo().equals(String.valueOf(ProcessoStatusDt.CALCULO)))){
			return ("\nN�o � poss�vel concluir o cadastro! (Motivo: Processo " + processoSelecionado.getProcessoStatus() + ")");
		}
		
		if (!processoSelecionado.getId_Serventia().equals(id_ServentiaDoUsuario)){
			return ("N�o � poss�vel concluir o cadastro! (Motivo: Processo de outra serventia.)");
		}
		
		//verifica se o processo encontrado � do tipo do cadastro escolhido
		else if (processoCadastro.getCadastroTipo() == ProcessoExecucaoDt.PROCESSO_FISICO && processoSelecionado.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
			return ("N�o � poss�vel concluir o cadastro! (Motivo: Processo cadastrado como C�lculo de Liquida��o de Pena. Para migrar o processo f�sico, basta alterar a classe CNJ deste processo no projudi.)");
		}
		else if (processoCadastro.getCadastroTipo() == ProcessoExecucaoDt.PROCESSO_CALCULO && processoSelecionado.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.EXECUCAO_DA_PENA))){
			return ("N�o � poss�vel concluir o cadastro! (Motivo: Processo Digital cadastrado no Projudi. \n Para efutar o cadastro, selecione a op��o: Processo -> Execu��o Penal -> Cadastrar Guia)");
		}
		else if (processoCadastro.getCadastroTipo() == ProcessoExecucaoDt.PROCESSO_PROJUDI && processoSelecionado.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
			return ("N�o � poss�vel concluir o cadastro! (Motivo: Sentenciado cadastrado em Processo do Execpenweb.)");
		}
				
		return retorno;
	}
	
	/**
	 * Verifica se n�mero do processo f�sico foi informado corretamente
	 * @param dados: processo execu��o
	 * @return mensagem: String
	 * @throws Exception
	 */
	public String verificarNumeroProcessoFisico(int cadastroTipo, String processoFisicoNumero){
		String retorno = "";
		
		if (cadastroTipo == ProcessoExecucaoDt.PROCESSO_FISICO || cadastroTipo == ProcessoExecucaoDt.PROCESSO_CALCULO){//pricesso f�sico ou processo para c�lculo
			if (processoFisicoNumero.length() == 0) retorno = "Informe o n�mero do processo e clique em \"Consultar Processo Cadastrado\"";
			else if (processoFisicoNumero.length() != 25) retorno = "O N�mero do Processo F�sico informado n�o � v�lido. \n";
		}	
		
		return retorno;
	}
	
	/**
	 * M�todo respons�vel em validar os dados de um Processo de Execu��o Penal
	 * @param dados: dados do Processo de Execu��o Penal (ProcessoExecucaoDt)
	 * @throws Exception 
	 */
	public String VerificarProcessoExecucao(ProcessoExecucaoDt dados) throws Exception{
		String stRetorno = "";
		
		if (dados.getCadastroTipo() == ProcessoExecucaoDt.PROCESSO_CALCULO){//processo de c�lculo
			if (!dados.isPodeCadastrarProcessoFisico()){
				stRetorno = "Consulte o n�mero do processo para prosseguir o cadastro!";
			}
		} 
		//passo 1 - cadastro dos dados da origem do processo
		if (dados.getPasso1().equals("Passo 1")) {
//				stRetorno = verificarNumeroProcessoFisico(dados); //verifica se os n�mero do processo f�sico foi informado corretamente
			
//				if (stRetorno.length() > 0)
//					return stRetorno;
//				else {
				if (dados.getGuiaRecolhimento().length() == 0) stRetorno += "O campo Tipo da Guia de Recolhimento � obrigat�rio. \n";
				if (dados.getProcessoDt().getListaPolosPassivos() == null || dados.getProcessoDt().getListaPolosPassivos().size() == 0 || dados.getProcessoDt().getListaPolosPassivos().size() > 1) stRetorno += "O Processo deve ter um Sentenciado. \n";
				if (dados.getId_CidadeOrigem().length() == 0 || dados.getCidadeOrigem().length() == 0) stRetorno += "O campo Comarca de Origem � obrigat�rio. \n";
				if (dados.getVaraOrigem().length() == 0) stRetorno += "O campo Vara de Origem � obrigat�rio. \n";
				if (dados.getNumeroAcaoPenal().equals("")) stRetorno += "O campo N�mero do Processo de A��o Penal � obrigat�rio. \n";
//					if (dados.isProcessoFisico()){
				if (dados.getCadastroTipo() == ProcessoExecucaoDt.PROCESSO_FISICO){//processo f�sico
					if (dados.getId_ServentiaCargo().length() == 0) stRetorno += "O campo Juiz Respons�vel � obrigat�rio. \n";
				} else if (dados.getCadastroTipo() != ProcessoExecucaoDt.PROCESSO_CALCULO //processo de c�lculo
						&& (dados.getId_Comarca().length() == 0 || dados.getComarca().length() == 0)) stRetorno += "O campo Comarca � obrigat�rio. \n";
				if (dados.getProcessoDt().getId_Serventia().equals("") && (dados.getProcessoDt().getId_AreaDistribuicao().length() == 0 || dados.getProcessoDt().getAreaDistribuicao().length() == 0)) stRetorno += "O campo �rea de Distribui��o do Processo � obrigat�rio. \n";
				if (dados.getProcessoDt().getId_ProcessoTipo().length() == 0 || dados.getProcessoDt().getProcessoTipo().length() == 0) stRetorno += "O campo Classe � obrigat�rio. \n";
				if (dados.getProcessoDt().getListaAssuntos() == null || dados.getProcessoDt().getListaAssuntos().size() == 0) stRetorno += "O Processo deve ter pelo menos um Assunto. \n";	
//				}
		}

		//cadastro dos dados da origem do processo ok. Passo 2 - cadastro da condena��o
		if (dados.getPasso1().equals("Passo 1 OK")) {
			stRetorno += verificarDadosSentenca(dados, true);
			
			//a data da pris�o definitiva (Primeiro Regime) � informada somente para o cadastro da primeira a��o penal
			if (!dados.getId_PenaExecucaoTipo().equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA)) 
					&& dados.getId_ProcessoExecucaoPenal().length() == 0
					&& dados.isIniciouCumprimentoPena()){
				if (dados.getDataPrimeiroRegime().length() == 0) stRetorno += "O campo Data do Primeiro Regime � obrigat�rio. \n";	
			}
		}
		
		
		return stRetorno;
	}

	/**
	 * valida os dados de senten�a (a��o penal) de um processo de execu��o penal.
	 * @param dados: ProcessoExecucaoDt
	 * @return mensagem de erro.
	 * @throws Exception
	 */
	public String verificarDadosSentenca(ProcessoExecucaoDt dados, boolean cadastroProcesso) throws Exception{
		String stRetorno = "";
		
		if (dados.getNumeroAcaoPenal().equals("")) stRetorno += "O campo N�mero do Processo de A��o Penal � obrigat�rio. \n";
		if (dados.getVaraOrigem().length() == 0) stRetorno += "O campo Vara de Origem � obrigat�rio. \n";
		if (dados.getId_PenaExecucaoTipo().length() == 0) stRetorno += "O campo Pena � obrigat�rio. \n";
		if (dados.getId_RegimeExecucao().length() == 0) stRetorno += "O campo Regime � obrigat�rio. \n";
		if (dados.getId_LocalCumprimentoPena().length() == 0) stRetorno += "O campo Estabelecimento Penal � obrigat�rio. \n";
		if (dados.getDataDenuncia().length() == 0) stRetorno += "O campo Data da Den�ncia � obrigat�rio. \n";
		else if (!Funcoes.validaData(dados.getDataDenuncia())) stRetorno += "A data do Recebimento da Den�ncia n�o � v�lida. Verifique! \n";
		if (dados.getDataSentenca().length() == 0) stRetorno += "O campo Data da Senten�a � obrigat�rio. \n";
		else if (!Funcoes.validaData(dados.getDataSentenca())) stRetorno += "A data da Senten�a n�o � v�lida. Verifique! \n";
		 
		if (dados.getDataTransitoJulgado().length() == 0){
			if (cadastroProcesso){
				if (dados.getGuiaRecolhimento().equalsIgnoreCase("D")) stRetorno += "O campo Data do Tr�nsito em Julgado � obrigat�rio. \n"; 
				if (dados.getGuiaRecolhimento().equalsIgnoreCase("P")) stRetorno += "O campo Data de Emiss�o da Guia de Recolhimento Provis�ria � obrigat�rio. \n";	
			
			} else stRetorno += "O campo Data do Tr�nsito em Julgado � obrigat�rio. \n";
		} else if (!Funcoes.validaData(dados.getDataTransitoJulgado())) stRetorno += "A data do TJ ou a data da Emiss�o da GRP n�o � v�lida. Verifique! \n";
		
		if (!dados.getId_PenaExecucaoTipo().equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA))){
			if (dados.getListaCondenacoes() == null || dados.getListaCondenacoes().size() == 0) stRetorno += "O Processo deve ter pelo menos uma Condena��o. \n";
			else {
				for (int i=0; i<dados.getListaCondenacoes().size(); i++){
					CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt)dados.getListaCondenacoes().get(i);
					if (condenacao.getDataFato().length() == 0)
						stRetorno += "A Data do Fato do crime " + (i+1) +" deve ser informada. \n";
					else if (!Funcoes.validaData(condenacao.getDataFato())) stRetorno += "A data do Fato do crime " + (i+1) + " informada n�o � v�lida. Verifique! \n";
					else if (dados.getDataDistribuicao().length() > 0){
						if (Funcoes.StringToDate(condenacao.getDataFato()).after(Funcoes.StringToDate(dados.getDataDistribuicao())))
						stRetorno += "A Data do Fato do crime " + (i+1) +" deve ser anterior � Data da Distribui��o do Processo de A��o Penal. \n";
					}
					if (condenacao.getTempoPenaEmDias().length() == 0)
						stRetorno += "O tempo de pena do crime " + (i+1) +" deve ser informado. \n";
				}
			}	
//				if (dados.getListaModalidade() != null && dados.getListaModalidade().size() > 0){
//					if (dados.getDataAdmonitoria().length() == 0) stRetorno += "O campo Data da Audi�ncia Admonit�ria (Motivo: Processo com substitui��o de pena e/ou SURSIS)\n";
//					else if (!Funcoes.validaData(dados.getDataAdmonitoria())) stRetorno += "A data da Audi�ncia Admonit�ria n�o � v�lida. Verifique! \n";
//				}
		} else {
			if (dados.getListaCondenacoes() != null && dados.getListaCondenacoes().size() > 0){
				stRetorno = "Exclua as condena��es. Motivo: Pena Medida de Seguran�a! \n";
			}
			if (dados.getListaModalidade() != null && dados.getListaModalidade().size() > 0){
				stRetorno = "Exclua as modalidades. Motivo: Pena Medida de Seguran�a! \n";
			}
		}
							
		return stRetorno;
	}
	
	/**
	 * valida os dados de condena��o.
	 * @param dados: ProcessoExecucaoDt
	 * @return mensagem de erro.
	 * @throws Exception
	 */
	public String verificarDadosCondenacao(ProcessoExecucaoDt dados) throws Exception{
		String stRetorno = "";
		
		if (dados.getId_CrimeExecucao().equals("")) stRetorno += "O campo Crime � obrigat�rio. \n";
		if (dados.getTempoPenaEmDias().equals("")) stRetorno += "O campo Pena Impostas � obrigat�rio. \n";
		if (dados.getReincidente().equals("")) stRetorno += "O campo Reincidente do crime � obrigat�rio. \n";

		if (dados.getDataFato().equals("")) stRetorno += "O campo Data do Fato do crime � obrigat�rio. \n";
		else if (!Funcoes.validaData(dados.getDataFato())) stRetorno += "A data do Fato n�o � v�lida. Verifique! \n";
		//else if (dados.getDataDistribuicao().equals("")) stRetorno = "O campo Data da Distribui��o do Processo de A��o Penal. \n";
		else if (dados.getDataDistribuicao().length() > 0 && Funcoes.StringToDate(dados.getDataFato()).after(Funcoes.StringToDate(dados.getDataDistribuicao())))
			stRetorno = "A Data do Fato do crime deve ser anterior � Data da Distribui��o do Processo de A��o Penal. \n";
				
		return stRetorno;
	}
	
	/**
	 * verifica dados para inclus�o da modalidade no processo.
	 * @param dados: ProcessoExecucaoDt
	 * @return mensagem de erro
	 * @throws Exception
	 */
	public String verificarDadosModalidade(ProcessoExecucaoDt dados) throws Exception{
		String stRetorno = "";
		
//			if (dados.getListaCondenacoes() == null || dados.getListaCondenacoes().size() == 0) 
//				stRetorno += "Insira pelo menos uma condena��o. \n";
		
		
		return stRetorno;
	}

	
	/**
	 * Verifica se os dados das pris�es provis�rias foram informados corretamente
	 * @param dados do processo de execu��o penal
	 * @return mensagem de erro.
	 * @throws Exception
	 */
	public String verificarDadosPrisoes(ProcessoExecucaoDt dados) throws Exception{
		String stRetorno = "";
		
		if (dados.getDataPrisaoProvisoria().length() == 0) {
			stRetorno = "O campo Data da Pris�o Provis�ria � obrigat�rio. \n";
		
		} else if (!Funcoes.validaData(dados.getDataPrisaoProvisoria())) stRetorno += "A data da Pris�o Provis�ria n�o � v�lida. Verifique! \n";
		else if (dados.getDataLiberdadeProvisoria().length() > 0 && !Funcoes.validaData(dados.getDataLiberdadeProvisoria())) stRetorno += "A data da Interrup��o da Pris�o Provis�ria n�o � v�lida. Verifique! \n";
		else{
			if (dados.getListaPrisoesProvisorias() != null && dados.getListaPrisoesProvisorias().size() > 0){
				if (((String)dados.getListaLiberdadesProvisorias().get(dados.getListaPrisoesProvisorias().size()-1)).length() <= 0){
					stRetorno = "A Data da �ltima Interrup��o da Provis�ria est� em aberto, n�o � poss�vel incluir nova Pris�o Provis�ria.";
				}
			}
			if (dados.getDataLiberdadeProvisoria().length() > 0){
				if (Funcoes.StringToDate(dados.getDataPrisaoProvisoria()).after(Funcoes.StringToDate(dados.getDataLiberdadeProvisoria()))){
					stRetorno = "A Data da Pris�o Provis�ria n�o pode ser posterior � Data da Interrup��o da Provis�ria. \n";
				} 
			}
		}
						
		return stRetorno;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 */
	public String verificaCpfParte(String cpf){
		String stMensagem = "";
		ProcessoParteNe neObjeto = new ProcessoParteNe();
		
		stMensagem = neObjeto.VerificarCpfCnpjParte(cpf);
				
		neObjeto = null;
		return stMensagem;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public String VerificarParteProcessoExecucao(ProcessoParteDt processoPartedt) throws Exception{
		String stRetorno = "";
			
		stRetorno = new ProcessoParteNe().VerificarParteProcessoExecucao(processoPartedt);
				
		return stRetorno;
	}

	/**
	 * Efetua cadastro de um processo de execu��o penal
	 * 
	 * @param processoExecu��odt: dt com os dados do processo
	 * @param usuarioDt: usuario que est� cadastrando o processo
	 * @author wcsilva
	 * @throws Exception 
	 */
	public void cadastrarProcessoExecucao(ProcessoExecucaoDt processoExecucaodt, UsuarioDt usuarioDt, boolean isImportacaoExecpen, SituacaoAtualExecucaoDt situacaoAtualExecucaoDt) throws Exception{
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
		ProcessoAssuntoNe processoAssuntoNe = new ProcessoAssuntoNe();
		CondenacaoExecucaoNe condenacaoExecucaoNe = new CondenacaoExecucaoNe();
		ProcessoEventoExecucaoNe processoEventoExecucaoNe = new ProcessoEventoExecucaoNe();
		String id_Serventia = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

			LogDt logDt = new LogDt(processoExecucaodt.getId_UsuarioLog(), processoExecucaodt.getIpComputadorLog());
			
			boolean isProcessoFisico = false;
			boolean isProcessoCalculo = false;
			if (processoExecucaodt.getCadastroTipo() == ProcessoExecucaoDt.PROCESSO_FISICO) isProcessoFisico = true;
			else if (processoExecucaodt.getCadastroTipo() == ProcessoExecucaoDt.PROCESSO_CALCULO) isProcessoCalculo = true;

			String descricaoServentia = "";
			
			// Distribui�ao processo
			if (isProcessoFisico){
				id_Serventia = processoExecucaodt.getProcessoDt().getId_Serventia();
				descricaoServentia = processoExecucaodt.getProcessoDt().getServentia();
			} else if (isProcessoCalculo){
				List listaServentia = new ServentiaNe().listarServentiasAreaDistribuicao(processoExecucaodt.getProcessoDt().getId_AreaDistribuicao());
				if (listaServentia != null && listaServentia.size() > 0){
					id_Serventia = (String)listaServentia.get(0);	
					descricaoServentia = new ServentiaNe().consultarServentiaDescricao(id_Serventia, obFabricaConexao);
					processoExecucaodt.getProcessoDt().setId_Serventia(id_Serventia);
					processoExecucaodt.getProcessoDt().setServentia(descricaoServentia);
				}
			} else {
				//em cumprimento ao provimento 16/2012 de CGJ
				//id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoExecucaodt.getProcessoDt().getId_AreaDistribuicao(), processoExecucaodt.getProcessoDt().getId_ProcessoTipo());
				id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoExecucaodt.getProcessoDt().getId_AreaDistribuicao());
				descricaoServentia = new ServentiaNe().consultarServentiaDescricao(id_Serventia, obFabricaConexao);
				processoExecucaodt.getProcessoDt().setId_Serventia(id_Serventia);
				processoExecucaodt.getProcessoDt().setServentia(descricaoServentia);
			}

			//verifica se o Id do processo est� setado corretamente
			if (!processoExecucaodt.isProcessoNovo()){
				if (processoExecucaodt.getProcessoDt().getId().length() == 0 && processoExecucaodt.getId_ProcessoExecucaoPenal().length() > 0)
					processoExecucaodt.getProcessoDt().setId(processoExecucaodt.getId_ProcessoExecucaoPenal());
				else if (processoExecucaodt.getProcessoDt().getId().length() > 0 && processoExecucaodt.getId_ProcessoExecucaoPenal().length() == 0)
					processoExecucaodt.setId_ProcessoExecucaoPenal(processoExecucaodt.getProcessoDt().getId());	
			}
			
			//processo fisico ou processo para c�lculo - Atualiza o n�mero do processo de acordo com o informado pelo usu�rio. 
			if (isProcessoFisico || isProcessoCalculo){
				atualizarNumeroProcessoFisico(processoExecucaodt);
			}
			//novo processo de execu��o - gera n�mero de processo
			else if (processoExecucaodt.getProcessoDt().getId().equals("")) {
				// Captura pr�xima numera��o de processo, baseado no c�digo do f�rum
				String numero = ProcessoNumero.getInstance().getProcessoNumero(processoExecucaodt.getProcessoDt().getForumCodigo());
				processoExecucaodt.getProcessoDt().setAnoProcessoNumero(numero);
			}

			// Seta fase do processo
			processoExecucaodt.getProcessoDt().setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.EXECUCAO));

			// Seta status do processo
			if (isProcessoCalculo)
				processoExecucaodt.getProcessoDt().setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.CALCULO));
			else processoExecucaodt.getProcessoDt().setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));

			// Seta �rea do processo
			processoExecucaodt.getProcessoDt().setAreaCodigo(String.valueOf(AreaDt.CRIMINAL));

			// Salva processo - processoExecucaodt.getProcessoDt.getId() == processoExecucaodt.getId_ProcessoExecucaoPenal
			salvarProcessoExecucaoCompleto(processoExecucaodt, obFabricaConexao);

			// Salva partes do Processo
			if (processoExecucaodt.isProcessoNovo()) 
				new ProcessoParteNe().salvarPartesProcesso(processoExecucaodt.getProcessoDt().getListaPolosPassivos(), processoExecucaodt.getProcessoDt().getId(), logDt, obFabricaConexao);

			// Salva condena��es do Processo
			if (!processoExecucaodt.isMedidaSeguranca())
				condenacaoExecucaoNe.salvarListaCondenacao(processoExecucaodt.getListaCondenacoes(), processoExecucaodt.getId(), logDt, obFabricaConexao);
			else
				processoExecucaodt.setListaModalidade(null); //n�o salva modalidade para os processos de medida de seguran�a
			
			// Gera movimenta��o GUIA DE RECOLHIMENTO INSERIDA
			MovimentacaoDt movimentacaoGR;
			if (isImportacaoExecpen){
				String complemento = descricaoServentia + " (Importado do Execpen)"; ;
				movimentacaoGR = movimentacaoNe.gerarMovimentacaoGuiaRecolhimentoInserida_UsuarioSistemaProjudi(processoExecucaodt.getProcessoDt().getId(), UsuarioServentiaDt.SistemaProjudi, logDt, obFabricaConexao, complemento);
			} else movimentacaoGR = movimentacaoNe.gerarMovimentacaoGuiaRecolhimentoInserida(processoExecucaodt.getProcessoDt().getId(), usuarioDt, logDt, obFabricaConexao);

			// Salva Arquivos j� com recibo
			if (!isProcessoCalculo){
				MovimentacaoArquivoNe arquivoNe = new MovimentacaoArquivoNe();
				arquivoNe.inserirArquivosSemRecibo(movimentacaoGR.getId(), processoExecucaodt.getProcessoDt().getProcessoNumeroCompleto(), processoExecucaodt.getListaArquivos(), logDt, obFabricaConexao);
			}

			// salva eventos
			List listaEventos = montarListaEventosProcessoNovo(processoExecucaodt, movimentacaoGR, usuarioDt.getId_UsuarioServentia());
			processoEventoExecucaoNe.salvarListaEvento(listaEventos, logDt, obFabricaConexao);

			// corrige primeiro regime, prisao provisoria e liberdade provisoria para pris�o e fuga se necess�rio.
			//tem que ser depois de salvas os eventos, pois ser�o listados todos os eventos do processo (os que foram cadastrados e os que j� existem)
			if (!isProcessoCalculo && !processoExecucaodt.isProcessoNovo() && !processoExecucaodt.isMedidaSeguranca())
				processoEventoExecucaoNe.corrigirPrisaoProvisoria(processoExecucaodt.getProcessoDt().getId(), usuarioDt.getId_UsuarioServentia(), logDt, obFabricaConexao);
			
			if (processoExecucaodt.isProcessoNovo()){
				//salva situa��o atual do processo
				situacaoAtualExecucaoDt.setIdProcesso(processoExecucaodt.getId_ProcessoExecucaoPenal());
				situacaoAtualExecucaoDt.setId_UsuarioLog(logDt.getId_Usuario());
				situacaoAtualExecucaoDt.setIpComputadorLog(logDt.getIpComputador());
				situacaoAtualExecucaoDt.setIdLocalCumprimentoPena(processoExecucaodt.getId_LocalCumprimentoPena());
				situacaoAtualExecucaoDt.setIdRegime(processoExecucaodt.getId_RegimeExecucao());
				
				if (processoExecucaodt.getListaModalidade() != null && processoExecucaodt.getListaModalidade().size() > 0){
					for (ProcessoEventoExecucaoDt modalidade : (List<ProcessoEventoExecucaoDt>)processoExecucaodt.getListaModalidade()) {
						SituacaoAtualModalidadeDt sitMod = new SituacaoAtualModalidadeDt();
						sitMod.setIpComputadorLog(logDt.getIpComputador());
						sitMod.setId_UsuarioLog(logDt.getId_Usuario());
						sitMod.setIdModalidade(modalidade.getEventoRegimeDt().getId_RegimeExecucao());
						situacaoAtualExecucaoDt.addListaSituacaoAtualModalidadeDt(sitMod);
					}
				}
				SituacaoAtualTipoPenaDt sitTipoPena = new SituacaoAtualTipoPenaDt();
				sitTipoPena.setIpComputadorLog(logDt.getIpComputador());
				sitTipoPena.setId_UsuarioLog(logDt.getId_Usuario());
				if (processoExecucaodt.getListaModalidade() != null && processoExecucaodt.getListaModalidade().size() > 0){
					sitTipoPena.setIdPenaExecucaoTipo(String.valueOf(PenaExecucaoTipoDt.PENA_RESTRITIVA_DIREITO));
				} else sitTipoPena.setIdPenaExecucaoTipo(processoExecucaodt.getId_PenaExecucaoTipo());
				situacaoAtualExecucaoDt.addListaSituacaoAtualTipoPenaDt(sitTipoPena);
				new SituacaoAtualExecucaoNe().salvar(situacaoAtualExecucaoDt, obFabricaConexao);
				
				// Salva assuntos do processo
				processoAssuntoNe.salvarProcessoAssunto(processoExecucaodt.getProcessoDt().getListaAssuntos(), processoExecucaodt.getId_ProcessoExecucaoPenal(), logDt, obFabricaConexao);

				String id_ServentiaCargo = "";
				
				// processo f�sico - obt�m o juiz respons�vel do processo f�sico
				if (isProcessoFisico){
					id_ServentiaCargo = processoExecucaodt.getId_ServentiaCargo();

				}else{
					//para atender o provimento 16/2012
					// Distribui processo para um juiz
					//id_ServentiaCargo = DistribuicaoProcessoServentiaCargo.getInstance().getDistribuicao1Grau(id_Serventia,processoExecucaodt.getProcessoDt().getId_ProcessoTipo() );
					id_ServentiaCargo = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(id_Serventia,processoExecucaodt.getProcessoDt().getId_AreaDistribuicao());
				}

				// Salva o juiz como respons�vel
				responsavelNe.salvarProcessoResponsavel(processoExecucaodt.getId_ProcessoExecucaoPenal(), id_ServentiaCargo,true, CargoTipoDt.JUIZ_1_GRAU, logDt, obFabricaConexao);
				
				// Gera movimenta��o PROCESSO DISTRIBU�DO
				String complemento = descricaoServentia;
				if (isProcessoFisico) complemento += " (Sem Regra de Redistribui��o - Processo F�sico)";
				else if (isProcessoCalculo) complemento += " (Sem Regra de Redistribui��o - Processo F�sico para C�lculo de Liquida��o de Pena)";
				movimentacaoNe.gerarMovimentacaoProcessoDistribuido(processoExecucaodt.getProcessoDt().getId(), UsuarioServentiaDt.SistemaProjudi, complemento, logDt, obFabricaConexao);
			} 
			
			// Gera pend�ncia VERIFICAR NOVO PROCESSO
			if (!isProcessoCalculo)
				pendenciaNe.gerarPendenciaVerificarNovoProcesso(processoExecucaodt.getProcessoDt(), UsuarioServentiaDt.SistemaProjudi, id_Serventia, movimentacaoGR.getId(), processoExecucaodt.getListaArquivos(), logDt, obFabricaConexao);
			
//			//recalcula a reincid�ncia de todas as condena��es do processo
//			if (!processoExecucaodt.isProcessoNovo()){
//				 recalcularReincidencia(processoExecucaodt.getId_ProcessoExecucaoPenal(), obFabricaConexao, logDt);
//			}
			obFabricaConexao.finalizarTransacao();
					
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			cancelarCadastroProcessoExecucao(processoExecucaodt);
			throw e;
			
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Salva os dados do ProcessoExecucaoDt e ProcessoDt
	 * @throws Exception 
	 */
	public void salvarProcessoExecucaoCompleto(ProcessoExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception{
		LogDt obLogDt;
		
		// salva dados do processo (ProcessoDt)
		new ProcessoNe().salvar(dados.getProcessoDt(), obFabricaConexao);
		dados.setId_ProcessoExecucaoPenal(dados.getProcessoDt().getId());

		//salva demais dados do processo (ProcessoExecucaoDt)
		ProcessoExecucaoPs obPersistencia = new  ProcessoExecucaoPs(obFabricaConexao.getConexao());
		
		if (dados.getId().equalsIgnoreCase("")) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("ProcessoExecucao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("ProcessoExecucao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	
	}


	/**
	 * M�todo respons�vel em cancelar o cadastro de um processo. Apaga os id's que tenham sido
	 * setados para os objetos
	 */
	private void cancelarCadastroProcessoExecucao(ProcessoExecucaoDt processoExecucaoDt){
		// Limpa id's dos objetos
		processoExecucaoDt.setId("");
		processoExecucaoDt.getProcessoDt().setId("");
		processoExecucaoDt.setId_ProcessoExecucaoPenal("null");
		if (processoExecucaoDt.getProcessoDt().getPartesProcesso() != null) {
			List lista = processoExecucaoDt.getProcessoDt().getPartesProcesso();
			for (int i = 0; i < lista.size(); i++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) lista.get(i);
				parteDt.setId("");
				if (parteDt.getEnderecoParte() != null) parteDt.getEnderecoParte().setId("");
				if (parteDt.getAdvogadoDt() != null) parteDt.getAdvogadoDt().setId("");
			}
		}
	}
	
//	/**
//	 * verifica se h� reincid�ncia para a condena��o informada.
//	 * @param idProcesso: identifica��o do processo de execu��o penal para consulta dos Tr�nsitos em Julgado existentes
//	 * @param condenacaoExecucaoDt: condena��o que ser� verificada a reincid�ncia
//	 * @throws Exception
//	 */
//	/* (Utilizado na inclus�o e exclus�o de condena��o de um processo de execu��o penal existente.
//	 	N�o alterar� a reincid�ncia das condena��es de outras a��es penais deste processo, uma vez que: 
//	 	Inclus�o: existe pelo menos uma condena��o no processo, 
//	 	Exclus�o: n�o exclui todas as condena��es do processo. */ 
//	public void calcularReincidencia(String idProcesso, CondenacaoExecucaoDt condenacaoExecucaoDt){
//	if (idProcesso.length() == 0){//ProcessoNovo
//	condenacaoExecucaoDt.setReincidente("false");
//	
//} else{
//	boolean reincidente = false;
//	List listaAcoesPenais = listarAcoesPenais(idProcesso);
//
//	if (listaAcoesPenais == null){//Processo novo
//		condenacaoExecucaoDt.setReincidente("false");
//		
//	} else{//Processo existente
//		for (int i=0; i<listaAcoesPenais.size(); i++){
//			if (Funcoes.StringToDate(((ProcessoExecucaoDt)listaAcoesPenais.get(i)).getDataTransitoJulgado()).before(Funcoes.StringToDate(condenacaoExecucaoDt.getDataFato()))){
//				reincidente = true;
//			}
//		}
//		if (reincidente) condenacaoExecucaoDt.setReincidente("true");
//		else condenacaoExecucaoDt.setReincidente("false");
//	}
//}
//	}
	
//	/**
//	 * recalcula a reincid�ncia para todas as condena��es do processo de execu��o informado.
//	 * @param idProcesso: identifica��o do processo para consulta dos Tr�nsitos em Julgado (a��es penais) e condena��es existentes
//	 * @param fabricaConexao: objeto de conex�o.
//	 * @param logDt: dados do usu�rio e computador.
//	 * @throws Exception
//	 */
//	/* utilizado no cadastro de nova a��o penal para um processo de execu��o existente */
//	public void recalcularReincidencia(String idProcesso, FabricaConexao obFabricaConexao, LogDt logDt){
//		CondenacaoExecucaoNe condenacaoExecucaoNe = new CondenacaoExecucaoNe();
//	//lista as a��es penais do processo de execu��o
//	List listaAcoesPenais = listarAcoesPenais(idProcesso, obFabricaConexao);
//	
//	//lista todas as condena��es do processo de execu��o
//	List listaCondenacao = condenacaoExecucaoNe.listarCondenacaoDoProcesso(idProcesso, obFabricaConexao);
//	
//	boolean reincidente = false;
//	
//	if (listaAcoesPenais == null){//Processo novo
//		for (int i=0; i<listaCondenacao.size(); i++)
//			((CondenacaoExecucaoDt)listaCondenacao.get(i)).setReincidente("false");
//		
//	} else{//Processo existente
//		for (int i=0; i<listaCondenacao.size(); i++){
//			CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt) listaCondenacao.get(i);
//			reincidente = false;
//			for (int j=0; j<listaAcoesPenais.size(); j++){
//				if (Funcoes.StringToDate(((ProcessoExecucaoDt)listaAcoesPenais.get(j)).getDataTransitoJulgado()).before(Funcoes.StringToDate(condenacao.getDataFato()))){
//					reincidente = true;
//				}
//			}
//			if (reincidente) condenacao.setReincidente("true");
//			else condenacao.setReincidente("false");
//		}
//	}
//	
//	condenacaoExecucaoNe.salvarListaCondenacao(listaCondenacao, null, logDt, obFabricaConexao);
//	}
	

	/**
	 * M�todo respons�vel em montar os eventos que s�o gerados ao cadastrar o processo
	 */
	public List montarListaEventosProcessoNovo(ProcessoExecucaoDt processoExecucaoDt, MovimentacaoDt movimentacaoDt, String idUsuarioServentia){
		List lista = new ArrayList();
		ProcessoEventoExecucaoDt processoEventoExecucaoDt = null;
		
		// acrescenta na lista as pris�es provis�rias e liberdades provis�rias
		if (!processoExecucaoDt.isMedidaSeguranca() && processoExecucaoDt.getListaPrisoesProvisorias() != null) {
			for (int i = 0; i < processoExecucaoDt.getListaPrisoesProvisorias().size(); i++) {
				//adiciona pris�o provis�ria
				processoEventoExecucaoDt = new ProcessoEventoExecucaoDt();
				processoEventoExecucaoDt.setId_Movimentacao(movimentacaoDt.getId());
				processoEventoExecucaoDt.setId_ProcessoExecucao(processoExecucaoDt.getId());
				processoEventoExecucaoDt.setDataInicio((String)processoExecucaoDt.getListaPrisoesProvisorias().get(i));
				processoEventoExecucaoDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.PRISAO_PROVISORIA));
				processoEventoExecucaoDt.setId_UsuarioServentia(idUsuarioServentia);
				processoEventoExecucaoDt.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
				lista.add(processoEventoExecucaoDt);
				processoEventoExecucaoDt = null;

				//adiciona liberdade provis�ria
				if (processoExecucaoDt.getListaLiberdadesProvisorias().size() >= i && ((String)processoExecucaoDt.getListaLiberdadesProvisorias().get(i)).length() > 0) {
					processoEventoExecucaoDt = new ProcessoEventoExecucaoDt();
					processoEventoExecucaoDt.setId_Movimentacao(movimentacaoDt.getId());
					processoEventoExecucaoDt.setDataInicio((String)processoExecucaoDt.getListaLiberdadesProvisorias().get(i));
					processoEventoExecucaoDt.setId_ProcessoExecucao(processoExecucaoDt.getId());
					processoEventoExecucaoDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.INTERRUPCAO_PRISAO_PROVISORIA));
					processoEventoExecucaoDt.setId_UsuarioServentia(idUsuarioServentia);
					processoEventoExecucaoDt.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
					lista.add(processoEventoExecucaoDt);
					processoEventoExecucaoDt = null;
				}
			}
		}

		// adiciona transito em julgado ou guia de recolhimento provos�ria
		processoEventoExecucaoDt = new ProcessoEventoExecucaoDt();
		processoEventoExecucaoDt.setId_Movimentacao(movimentacaoDt.getId());
		processoEventoExecucaoDt.setDataInicio(processoExecucaoDt.getDataTransitoJulgado());
		processoEventoExecucaoDt.setId_ProcessoExecucao(processoExecucaoDt.getId());
		processoEventoExecucaoDt.getEventoLocalDt().setId_LocalCumprimentoPena(processoExecucaoDt.getId_LocalCumprimentoPena());
		processoEventoExecucaoDt.getEventoRegimeDt().setId_RegimeExecucao(processoExecucaoDt.getId_RegimeExecucao());
		processoEventoExecucaoDt.setId_UsuarioServentia(idUsuarioServentia);
		processoEventoExecucaoDt.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
		if (processoExecucaoDt.getGuiaRecolhimento().equalsIgnoreCase("D"))
			processoEventoExecucaoDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO));
		else if (processoExecucaoDt.getGuiaRecolhimento().equalsIgnoreCase("P"))
			processoEventoExecucaoDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA));
		lista.add(processoEventoExecucaoDt);
		processoEventoExecucaoDt = null;
		
		//adiciona primeiro regime
		if (!processoExecucaoDt.isMedidaSeguranca()){
			if (processoExecucaoDt.getDataPrimeiroRegime().length() > 0) {
				processoEventoExecucaoDt = new ProcessoEventoExecucaoDt();
				processoEventoExecucaoDt.setId_Movimentacao(movimentacaoDt.getId());
				processoEventoExecucaoDt.setDataInicio(processoExecucaoDt.getDataPrimeiroRegime());
				processoEventoExecucaoDt.setId_ProcessoExecucao(processoExecucaoDt.getId());
				if (processoExecucaoDt.getListaModalidade() != null && processoExecucaoDt.getListaModalidade().size() > 0)
					processoEventoExecucaoDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.INICIO_PENA_RESTRITIVA_DIREITO));
				else processoEventoExecucaoDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.PRIMEIRO_REGIME));
				processoEventoExecucaoDt.getEventoLocalDt().setId_LocalCumprimentoPena(processoExecucaoDt.getId_LocalCumprimentoPena());
				processoEventoExecucaoDt.getEventoRegimeDt().setId_RegimeExecucao(processoExecucaoDt.getId_RegimeExecucao());
				processoEventoExecucaoDt.setId_UsuarioServentia(idUsuarioServentia);
				processoEventoExecucaoDt.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
				lista.add(processoEventoExecucaoDt);
				processoEventoExecucaoDt = null;
			}	
		
			//adiciona sursis, se houver
			if (processoExecucaoDt.getEventoSursisDt() != null){
				if (processoExecucaoDt.getEventoSursisDt().getQuantidade().length() > 0 && Funcoes.StringToInt(processoExecucaoDt.getEventoSursisDt().getQuantidade()) > 0){
					processoEventoExecucaoDt = processoExecucaoDt.getEventoSursisDt();
					processoEventoExecucaoDt.setId_Movimentacao(movimentacaoDt.getId());
					processoEventoExecucaoDt.setDataInicio(processoExecucaoDt.getDataTransitoJulgado());
					processoEventoExecucaoDt.setDataInicioSursis(processoExecucaoDt.getDataInicioSursis());
					processoEventoExecucaoDt.setId_ProcessoExecucao(processoExecucaoDt.getId());
					processoEventoExecucaoDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.CONCESSAO_SURSIS));
					processoEventoExecucaoDt.setId_UsuarioServentia(idUsuarioServentia);
					processoEventoExecucaoDt.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
					lista.add(processoEventoExecucaoDt); //sursis
					processoEventoExecucaoDt = null;	
				}
			}
		
			//adiciona as modalidades, se houver
			if (processoExecucaoDt.getListaModalidade() != null && processoExecucaoDt.getListaModalidade().size() > 0){
				for (ProcessoEventoExecucaoDt modalidade : (List<ProcessoEventoExecucaoDt>)processoExecucaoDt.getListaModalidade()) {
					modalidade.setId_Movimentacao(movimentacaoDt.getId());
					modalidade.setId_ProcessoExecucao(processoExecucaoDt.getId());
					modalidade.setDataInicio(processoExecucaoDt.getDataTransitoJulgado());
					modalidade.setId_UsuarioServentia(idUsuarioServentia);
					modalidade.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());

					//salva o regime vinculado ao evento para permitir a consulta de cumprimento da pena por modalidade
//						vincularIdEvento_Quantidade_Modalidade(modalidade, processoExecucaoDt.getListaCondenacoes());
					
					lista.add(modalidade); 
				}
			}
		}
		
		return lista;
	}
	
	/**
	 * vincula o Id_EventoExecucao e Quantidade (tempo total da condena��o) a partir do RegimeExecu��o da modalidade
	 * @param modalidade: objeto ProcessoEventoExecu��o
	 * @param listaCondenacoes: lista de condena��es da a��o penal
	 */
	public void vincularIdEvento_Quantidade_Modalidade(ProcessoEventoExecucaoDt modalidade, List listaCondenacoes){

		//faz o v�nculo da quantidade com o tempo total da condena��o, pois � regra que o tempo de cumprimento da modalidade sempre ser� o tempo total da condena��o
		switch(Funcoes.StringToInt(modalidade.getEventoRegimeDt().getId_RegimeExecucao())){//regime = modalidade
			case RegimeExecucaoDt.INTERDICAO_TEMPORARIA_DIREITOS:
				modalidade.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.INTERDICAO_TEMPORARIA_DIREITOS));
//				modalidade.setQuantidade(getTempoTotalCondenacao(listaCondenacoes));
				break;
			case RegimeExecucaoDt.LIMITACAO_FIM_SEMANA:
				modalidade.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.LIMITACAO_FIM_SEMANA));
//				modalidade.setQuantidade(getTempoTotalCondenacao(listaCondenacoes));
				break;
			case RegimeExecucaoDt.PERDA_BENS_VALORES:
				modalidade.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.PERDA_BENS_VALORES));
//				modalidade.setQuantidade(getTempoTotalCondenacao(listaCondenacoes));
				break;
			case RegimeExecucaoDt.PRESTACAO_PECUNIARIA:
				modalidade.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.PRESTACAO_PECUNIARIA));
				if (modalidade.getQuantidade().length() == 0) modalidade.setQuantidade("0");
				break;
			case RegimeExecucaoDt.CESTA_BASICA:
				modalidade.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.CESTA_BASICA));
				if (modalidade.getQuantidade().length() == 0) modalidade.setQuantidade("0");
				break;
			case RegimeExecucaoDt.PRESTACAO_SERVICO_COMUNIDADE:
				modalidade.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.PRESTACAO_SERVICO_COMUNIDADE));
//				modalidade.setQuantidade(getTempoTotalCondenacao(listaCondenacoes));
				break;
			
		}
		if (modalidade.getQuantidade().length() == 0){
			modalidade.setQuantidade(getTempoTotalCondenacao(listaCondenacoes));
		}
	}
	
	/**
	 * calcula o tempo total das condena��es da lista
	 * @param listaCondenacao: lista com as condena��es
	 * @return String: tempo total da condena��o em dias
	 */
	public String getTempoTotalCondenacao(List listaCondenacao){
		int tempoCondenacao = 0;
		if (listaCondenacao != null && listaCondenacao.size() > 0){
			for (CondenacaoExecucaoDt condenacao : (List<CondenacaoExecucaoDt>)listaCondenacao) {
				tempoCondenacao += Funcoes.StringToInt(condenacao.getTempoPenaEmDias());
			}	
		}
		return String.valueOf(tempoCondenacao);
	}
	
	/**
	 * Lista os dados das a��es penais (com o tempo total de condena��o) do processo de execu��o informado pelo id_ProcessoExecucaoPenal
	 * @param id_Processo: id do processo de execu��o penal.
	 * @return List<ProcessoExecucaoDt> lista com os dados das a��es penais e o tempo total da condena��o
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarAcoesPenais(String id_Processo) throws Exception{
		List lista = new ArrayList(); 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

//			ob obPersistencia = new ob( obFabricaConexao);
			lista = listarAcoesPenais(id_Processo, obFabricaConexao); 

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Lista os dados das a��es penais (com o tempo total de condena��o) do processo de execu��o informado pelo id_ProcessoExecucaoPenal
	 * @param id_Processo: id do processo de execu��o penal.
	 * @param obFabricaConexao: objeto conex�o.
	 * @return List<ProcessoExecucaoDt> lista com os dados das a��es penais e o tempo total da condena��o
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarAcoesPenais(String id_Processo, FabricaConexao obFabricaConexao) throws Exception{
		List lista = new ArrayList(); 

		ProcessoExecucaoPs obPersistencia = new  ProcessoExecucaoPs(obFabricaConexao.getConexao());
		lista = obPersistencia.listarAcoesPenais(id_Processo); 
		
		if (lista != null && lista.size() > 0){
			for (ProcessoExecucaoDt processoExecucao : (List<ProcessoExecucaoDt>)lista) {
    			processoExecucao.setListaModalidade(this.listarModalidadesDaAcaoPenal(processoExecucao.getId(), obFabricaConexao));

    			//consulta modalidades
    			processoExecucao.setListaModalidade(this.listarModalidadesDaAcaoPenal(processoExecucao.getId(), obFabricaConexao));
//                if (processoExecucao.getListaModalidade() != null && processoExecucao.getListaModalidade().size() > 0){
//                	for (ProcessoEventoExecucaoDt modalidade : (List<ProcessoEventoExecucaoDt>)processoExecucao.getListaModalidade()) {
////                    	modalidade.setQuantidade(processoExecucao.getTempoTotalCondenacaoDias());
//                    	modalidade.setIdProcessoExecucaoPenal(processoExecucao.getId_ProcessoExecucaoPenal());
//    				}	
//                }
                //consulta sursis
                ProcessoEventoExecucaoDt eventoSursis = this.consultarSursisDaAcaoPenal(processoExecucao.getId(), String.valueOf(EventoExecucaoDt.CONCESSAO_SURSIS), obFabricaConexao);
                if (eventoSursis != null){
                	processoExecucao.setEventoSursisDt(eventoSursis);
                	processoExecucao.setTempoTotalSursisAnos(processoExecucao.getEventoSursisDt().getQuantidade());
                }
			}
		}
		
	
		return lista;
	}
	
	/**
	 * M�todo utilizado para salvar os dados do ProcessoExecu��o, Condena��es e Modalidades
	 * N�o salva dados do ProcessoDt
	 * @param ProcessoExecucaoDt, dados do ProcessoExecucao e lista de condena��es
	 * @author wcsilva
	 * @throws Exception 
	 */
	public void salvarComCondenacao(ProcessoExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception{
		salvar(dados, obFabricaConexao);
		
		// altera lista de condena��o
		new CondenacaoExecucaoNe().salvarListaCondenacao(dados.getListaCondenacoes(), dados.getId(), null, obFabricaConexao);
	}
	
	/**
	 * M�todo utilizado para salvar os dados do ProcessoExecu��o e Condena��es
	 * N�o salva dados do ProcessoDt
	 * @param ProcessoExecucaoDt, dados do ProcessoExecucao e lista de condena��es
	 * @author wcsilva
	 * @throws Exception 
	 */
	public void salvarComCondenacao(ProcessoExecucaoDt processoExecucaoDt, String idUsuarioServentia) throws Exception{
	    FabricaConexao obFabricaConexao = null;
	    String idMovimentacao = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			
			
			LogDt logDt = new LogDt(processoExecucaoDt.getId_UsuarioLog(), processoExecucaoDt.getIpComputadorLog());
			
			salvar(processoExecucaoDt, obFabricaConexao);
			
			//salva o local de cumprimento de pena
			String idPEE = "";
			if (processoExecucaoDt.getIdEventoLocal() == null || processoExecucaoDt.getIdEventoLocal().length() == 0){				
				idPEE = new ProcessoEventoExecucaoNe().consultarId_ProcessoEventoExecucao(processoExecucaoDt.getId(), EventoExecucaoDt.TRANSITO_JULGADO+","+EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA, obFabricaConexao);
			}
			new EventoLocalNe().salvar(new EventoLocalDt(processoExecucaoDt.getIdEventoLocal(), processoExecucaoDt.getId_LocalCumprimentoPena(), idPEE), obFabricaConexao, logDt);
			//salva o regime
			new EventoRegimeNe().salvar(new EventoRegimeDt(processoExecucaoDt.getIdEventoRegime(), processoExecucaoDt.getId_RegimeExecucao(), idPEE), obFabricaConexao, logDt);
			
			// altera lista de condena��o
			new CondenacaoExecucaoNe().salvarListaCondenacao(processoExecucaoDt.getListaCondenacoes(), processoExecucaoDt.getId(), null, obFabricaConexao);
//			recalcularReincidencia(dados.getId_ProcessoExecucaoPenal(), obFabricaConexao, logDt);

			//consulta o Id_Movimentacao para salvar a modalidade (evento) e sursis inclu�da no processo
			if ((processoExecucaoDt.getListaModalidade() != null && processoExecucaoDt.getListaModalidade().size() > 0)
					|| processoExecucaoDt.getEventoSursisDt() != null){
				idMovimentacao = new ProcessoEventoExecucaoNe().consultarIdMovimentacao(EventoExecucaoDt.TRANSITO_JULGADO + "," + EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA, processoExecucaoDt.getId());
			}
			
			// altera lista de modalidade
			if (processoExecucaoDt.getListaModalidade() != null && processoExecucaoDt.getListaModalidade().size() > 0) {
				for (ProcessoEventoExecucaoDt modalidade : (List<ProcessoEventoExecucaoDt>)processoExecucaoDt.getListaModalidade()) {
					modalidade.setId_UsuarioServentia(idUsuarioServentia);
					modalidade.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
					if (modalidade.getId().length() == 0){		//nova modalidade inclu�da no processo
						modalidade.setId_Movimentacao(idMovimentacao); 
						modalidade.setId_ProcessoExecucao(processoExecucaoDt.getId());
					
					} else {
						//a data in�cio da modalidade ser� a data do tr�nsito em julgado.
						modalidade.setDataInicio(processoExecucaoDt.getDataTransitoJulgado());
					}
				}
				//salva/altera as modalidades (evento e eventoRegime)
				new ProcessoEventoExecucaoNe().salvarListaEvento(processoExecucaoDt.getListaModalidade(), logDt, obFabricaConexao);
				
				//se houver modalidade, altera o evento "Primeiro Regime" relacionado, para "Substitui��o da PPL em PRD".
				ProcessoEventoExecucaoNe processoEventoExecucaoNe = new ProcessoEventoExecucaoNe();
				ProcessoEventoExecucaoDt processoEventoExecucaoDt = new ProcessoEventoExecucaoDt();
				String idEventoPrimeiroRegime = processoEventoExecucaoNe.consultarId_ProcessoEventoExecucao(processoExecucaoDt.getId(), String.valueOf(EventoExecucaoDt.PRIMEIRO_REGIME));
				if (idEventoPrimeiroRegime.length()>0){
					processoEventoExecucaoDt.setId(idEventoPrimeiroRegime);
					processoEventoExecucaoDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.INICIO_PENA_RESTRITIVA_DIREITO));
					processoEventoExecucaoDt.setId_UsuarioServentia(idUsuarioServentia);
					processoEventoExecucaoDt.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
					processoEventoExecucaoNe.salvarEventoCompleto(processoEventoExecucaoDt, logDt, obFabricaConexao);
					processoEventoExecucaoDt = null;
				}
			} else {
				//se n�o houver modalidade, altera o evento "Substitui��o da PPL em PRD" relacionado, para "Primeiro Regime".
				ProcessoEventoExecucaoNe processoEventoExecucaoNe = new ProcessoEventoExecucaoNe();
				ProcessoEventoExecucaoDt processoEventoExecucaoDt = new ProcessoEventoExecucaoDt();
				String idEvento = processoEventoExecucaoNe.consultarId_ProcessoEventoExecucao(processoExecucaoDt.getId(),String.valueOf(EventoExecucaoDt.INICIO_PENA_RESTRITIVA_DIREITO));
				if (idEvento.length()>0){
					processoEventoExecucaoDt.setId(idEvento);
					processoEventoExecucaoDt.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.PRIMEIRO_REGIME));
					processoEventoExecucaoDt.setId_UsuarioServentia(idUsuarioServentia);
					processoEventoExecucaoDt.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
					processoEventoExecucaoNe.salvarEventoCompleto(processoEventoExecucaoDt, logDt, obFabricaConexao);
					processoEventoExecucaoDt = null;
				}
			}
			
			//altera sursis
			if (processoExecucaoDt.getEventoSursisDt() != null && 
					(processoExecucaoDt.getEventoSursisDt().getQuantidade().length() > 0 
							|| processoExecucaoDt.getEventoSursisDt().getId().length() > 0)){
				if (processoExecucaoDt.getEventoSursisDt().getQuantidade().length() > 0 
						&& Funcoes.StringToInt(processoExecucaoDt.getEventoSursisDt().getQuantidade()) > 0){
					//caso a data do TJ esteja incorreta
					processoExecucaoDt.getEventoSursisDt().setDataInicio(processoExecucaoDt.getDataTransitoJulgado());
					processoExecucaoDt.getEventoSursisDt().setDataInicioSursis(processoExecucaoDt.getDataInicioSursis());
					if (processoExecucaoDt.getEventoSursisDt().getId_Movimentacao().length() == 0){
						processoExecucaoDt.getEventoSursisDt().setId_Movimentacao(idMovimentacao);
						processoExecucaoDt.getEventoSursisDt().setId_ProcessoExecucao(processoExecucaoDt.getId());
						processoExecucaoDt.getEventoSursisDt().setId_UsuarioServentia(idUsuarioServentia);
						processoExecucaoDt.getEventoSursisDt().setId_EventoExecucao(String.valueOf(EventoExecucaoDt.CONCESSAO_SURSIS));
						processoExecucaoDt.getEventoSursisDt().setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
					}
					new ProcessoEventoExecucaoNe().salvarEventoCompleto(processoExecucaoDt.getEventoSursisDt(), logDt, obFabricaConexao);
					
				//exclui o evento do sursis caso tenha zerado o tempo do sursis
				} else if (processoExecucaoDt.getEventoSursisDt().getId().length() > 0) {
					processoExecucaoDt.getEventoSursisDt().setId_UsuarioLog(processoExecucaoDt.getId_UsuarioLog());
					processoExecucaoDt.getEventoSursisDt().setIpComputadorLog(processoExecucaoDt.getIpComputadorLog());
					new ProcessoEventoExecucaoNe().excluirCompleto(processoExecucaoDt.getEventoSursisDt(), obFabricaConexao);
				}
			}
			
			//altera data in�cio do evento transito em julgado
			ProcessoEventoExecucaoNe processoEventoExecucaoNe = new ProcessoEventoExecucaoNe();
			ProcessoEventoExecucaoDt processoEventoExecucaoDt = null;
			processoEventoExecucaoDt = processoEventoExecucaoNe.consultarProcessoEventoExecucao(processoExecucaoDt.getId(), EventoExecucaoDt.TRANSITO_JULGADO +","+ EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA, obFabricaConexao);
			if (processoEventoExecucaoDt != null){
				processoEventoExecucaoDt.setDataInicio(processoExecucaoDt.getDataTransitoJulgado());
				processoEventoExecucaoDt.setId_UsuarioServentia(idUsuarioServentia);
				processoEventoExecucaoDt.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
				processoEventoExecucaoNe.salvarEventoCompleto(processoEventoExecucaoDt, logDt, obFabricaConexao);
				processoEventoExecucaoDt = null;
			}
			
			obFabricaConexao.finalizarTransacao();
		} catch(MensagemException e){
			obFabricaConexao.cancelarTransacao();
			throw e;
		}  catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	
	/**
	 * sobrescreve o m�todo do gen, salva dados do Processo
	 * @param dados, objeto ProcessoExecucaoDt
	 * @param obFabricaConexao, conex�o ativa
	 * @throws Exception
	 */
	public void salvar(ProcessoExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception{
		LogDt obLogDt;

		ProcessoExecucaoPs obPersistencia = new  ProcessoExecucaoPs(obFabricaConexao.getConexao());
		/* use o iu do objeto para saber se os dados ja est�o ou n�o salvos */
		if (dados.getId().length()==0) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("ProcessoExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("ProcessoExecucao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);	
	}
	
	/**
	 * Consulta os dados das condena��es do ProcessoExecu��o
	 * @param idProcessoExecucao: id do objeto ProcessoExecucao (que define a a��o penal correspondente)
	 * @return List<CondenacaoExecucaoDt> listaCondenacao
	 * @author wcsilva
	 * @throws Exception
	 */
	public List listarCondenacoesDaAcaoPenal(String idProcessoExecucao) throws Exception{
		List listaCondenacoes = null;
			listaCondenacoes = new CondenacaoExecucaoNe().listarCondenacaoDaAcaoPenal(idProcessoExecucao);
		
		
		return listaCondenacoes;
	}
	
	public String listarCondenacoesDaAcaoPenalJSON(String idProcessoExecucao, String posicao) throws Exception{
		String stTemp = "";
		stTemp = new CondenacaoExecucaoNe().listarCondenacaoDaAcaoPenalJSON(idProcessoExecucao, posicao);
		
		
		return stTemp;
	}

	/**
	 * Consulta os dados da a��o penal com a lista de condena��es
	 * @param idProcessoExecucao: id do processoExecu��o
	 * @return processoExecucaoDt
	 * @author wcsilva
	 * @throws Exception
	 */
	public ProcessoExecucaoDt consultarAcaoPenalComCondenacao(String idProcessoExecucao) throws Exception{
		ProcessoExecucaoDt processoExecucaoDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoExecucaoPs obPersistencia = new  ProcessoExecucaoPs(obFabricaConexao.getConexao());
			processoExecucaoDt = obPersistencia.consultarAcaoPenalComCondenacao(idProcessoExecucao);
		
			if (processoExecucaoDt != null){
				//consulta modalidades
	            processoExecucaoDt.setListaModalidade(this.listarModalidadesDaAcaoPenal(processoExecucaoDt.getId(), obFabricaConexao));
	            if (processoExecucaoDt.getListaModalidade() != null && processoExecucaoDt.getListaModalidade().size() > 0){
	            	for (ProcessoEventoExecucaoDt modalidade : (List<ProcessoEventoExecucaoDt>)processoExecucaoDt.getListaModalidade()) {
//	                	modalidade.setQuantidade(processoExecucaoDt.getTempoTotalCondenacaoDias());
	                	modalidade.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
					}	
	            }
	            //consulta sursis
	            ProcessoEventoExecucaoDt eventoSursis = this.consultarSursisDaAcaoPenal(processoExecucaoDt.getId(), String.valueOf(EventoExecucaoDt.CONCESSAO_SURSIS), obFabricaConexao);
	            if (eventoSursis != null){
	            	processoExecucaoDt.setEventoSursisDt(eventoSursis);
	            	processoExecucaoDt.setTempoTotalSursisAnos(processoExecucaoDt.getEventoSursisDt().getQuantidade());
	            	processoExecucaoDt.setDataInicioSursis(eventoSursis.getDataInicioSursis());
	            }				
			}
            
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return processoExecucaoDt;
	}

	/**
	 * lista todos os tr�nsitos em julgado (e guia de recolhimento provis�ria), com condena��es  do processo principal e apensos
	 * (HashMap com as chaves: Id_ProcessoExecucao, DataTransitoJulgado, TempoNaoExtintoDias, TempoExtintoDias, TempoTotalDias, Checked, Fracao, Id_TransitoJulgadoEvento)
	 * @param id_ProcessoExecucaoPenal: identificador do processo de execu��o penal
	 * @param id_ProcessoEventoExecucao: identificador do evento de comuta��o que est� sendo alterado.
	 * @return lista com o HashMap com as informa��es dos TJs
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarTransitoComTotalCondenacao_HashMap(String id_ProcessoExecucaoPenal, String id_ProcessoEventoExecucao, String id_EventoExecucao) throws Exception{
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoExecucaoPs obPersistencia = new  ProcessoExecucaoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarTransitoComTotalCondenacao_HashMap(id_ProcessoExecucaoPenal, id_ProcessoEventoExecucao, id_EventoExecucao);
		
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Lista os dados das a��es penais (com as condena��es respectivas) do processo de execu��o informado pelo id_ProcessoExecucaoPenal
	 * @param id_Processo: id do processo de execu��o penal.
	 * @return List(ProcessoExecucaoDt): lista com os dados das a��es penais e condena��es.
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarAcoesPenaisComCondenacoes(String id_Processo) throws Exception{
		List lista = new ArrayList(); 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoExecucaoPs obPersistencia = new  ProcessoExecucaoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarAcoesPenaisComCondenacoes(id_Processo); 

			if (lista != null && lista.size() > 0){
				for (ProcessoExecucaoDt processoExecucao : (List<ProcessoExecucaoDt>)lista) {
	    			processoExecucao.setListaModalidade(this.listarModalidadesDaAcaoPenal(processoExecucao.getId(), obFabricaConexao));

	    			//consulta modalidades
	    			processoExecucao.setListaModalidade(this.listarModalidadesDaAcaoPenal(processoExecucao.getId(), obFabricaConexao));
	                if (processoExecucao.getListaModalidade() != null && processoExecucao.getListaModalidade().size() > 0){
	                	for (ProcessoEventoExecucaoDt modalidade : (List<ProcessoEventoExecucaoDt>)processoExecucao.getListaModalidade()) {
	                    	modalidade.setQuantidade(processoExecucao.getTempoTotalCondenacaoDias());
	                    	modalidade.setIdProcessoExecucaoPenal(processoExecucao.getId_ProcessoExecucaoPenal());
	    				}	
	                }
	                //consulta sursis
	                ProcessoEventoExecucaoDt eventoSursis = this.consultarSursisDaAcaoPenal(processoExecucao.getId(), String.valueOf(EventoExecucaoDt.CONCESSAO_SURSIS), obFabricaConexao);
	                if (eventoSursis != null){
	                	processoExecucao.setEventoSursisDt(eventoSursis);
	                	processoExecucao.setTempoTotalSursisAnos(processoExecucao.getEventoSursisDt().getQuantidade());
	                	processoExecucao.setDataInicioSursis(eventoSursis.getDataInicioSursis());
	                }
				}
			}
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Salva a condena��o
	 * @param condenacaoExecucaodt
	 * @throws Exception
	 * @author wcsilva
	 */
	public void salvarCondenacao(CondenacaoExecucaoDt condenacaoExecucaodt) throws Exception{
			new CondenacaoExecucaoNe().salvar(condenacaoExecucaodt);
		
	}
	
	/**
	 * Exclui a condena��o
	 * @param condenacaoExecucaodt
	 * @throws Exception
	 * @author wcsilva
	 */
	public String excluirCondenacao(String posicaoLista, ProcessoExecucaoDt processoExecucaoDt, boolean isExcluirDefinitivo) throws Exception{
		String msg = "";

		if (posicaoLista != null && posicaoLista.length() > 0) {
			 if ((processoExecucaoDt.getListaCondenacoes() != null && processoExecucaoDt.getListaCondenacoes().size() > 1)
					 || processoExecucaoDt.getId_PenaExecucaoTipo().equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA))){
				 if (isExcluirDefinitivo) {
					 new CondenacaoExecucaoNe().excluir((CondenacaoExecucaoDt) processoExecucaoDt.getListaCondenacoes().get(Funcoes.StringToInt(posicaoLista)));	 
				 }
				 
				 processoExecucaoDt.getListaCondenacoes().remove(Funcoes.StringToInt(posicaoLista));
			 } else {
				 msg = "N�o � poss�vel excluir a condena��o. (Motivo: O processo deve conter pelo menos uma condena��o!)";
			 }
			 
        }
	
		return msg;
	}
	
	/**
	 * Consultar Processo de A��o Penal no SPG atrav�s no n�mero do Processo de A��o Penal informado pelo id_ProcessoAcaoPenal
	 * @param id_ProcessoAcaoPenal: id do processo de a��o penal.
	 * @return processoExecucaoDt
	 * @throws Exception
	 * @author kbsriccioppo
	 */
	public String consultarGuiaRecolhimento(String numeroGuiaRecolhimento, ProcessoExecucaoDt processoExecucaoDt, ProcessoParteDt processoParteDt) throws Exception{
		String mensagem = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoExecucaoPs obPersistencia = new  ProcessoExecucaoPs(obFabricaConexao.getConexao());
			
			mensagem = obPersistencia.consultarGuiaRecolhimento(numeroGuiaRecolhimento, processoExecucaoDt, processoParteDt );
			
			if (mensagem.equals("")){//se n�o retornou erro verificar dados no projudi
				//verifica se a profissao que veio do SPG tem na base do Projudi
				if (!processoParteDt.getId_Profissao().equals("")) {
					ProfissaoNe profissaoNe = new ProfissaoNe();
					ProfissaoDt profissaoDt = new ProfissaoDt();
					//pesquisa pelo codigo do SPG no projudi
					profissaoDt = profissaoNe.consultarCodigo(processoParteDt.getId_Profissao());
					if (profissaoDt!=null){//se encontrou busca a descricao no projudi da profissao
						processoParteDt.setId_Profissao(profissaoDt.getId());
						processoParteDt.setProfissao(profissaoDt.getProfissao());
					} else {
						processoParteDt.setId_Profissao("");
						processoParteDt.setProfissao("");
					}
				}
				//verifica se a escolaridade que veio do SPG tem na base do Projudi
				if (!processoParteDt.getId_Escolaridade().equals("")) {
					EscolaridadeNe escolaridadeNe = new EscolaridadeNe();
					EscolaridadeDt escolaridadeDt = new EscolaridadeDt();
					//pesquisa pelo codigo do SPG no projudi
					escolaridadeDt = escolaridadeNe.consultarCodigo(processoParteDt.getId_Escolaridade());
					if (escolaridadeDt!=null){//se encontrou busca a descricao no projudi da escolaridade
						processoParteDt.setId_Escolaridade(escolaridadeDt.getId());
						processoParteDt.setEscolaridade(escolaridadeDt.getEscolaridade());
					} else {
						processoParteDt.setId_Escolaridade("");
						processoParteDt.setEscolaridade("");
					}
				}
				//verifica se o tipo de pena que veio do SPG tem na base do Projudi
				if (!processoExecucaoDt.getId_PenaExecucaoTipo().equals("")) {
					PenaExecucaoTipoNe penaExecucaoTipoNe = new PenaExecucaoTipoNe();
					PenaExecucaoTipoDt penaExecucaoTipoDt = new PenaExecucaoTipoDt();
					//pesquisa pelo codigo do SPG no projudi
					penaExecucaoTipoDt = penaExecucaoTipoNe.consultarId(processoExecucaoDt.getId_PenaExecucaoTipo());
					if (penaExecucaoTipoDt!=null){//se encontrou busca a descricao no projudi do tipo de pena
						processoExecucaoDt.setId_PenaExecucaoTipo(penaExecucaoTipoDt.getId());
						processoExecucaoDt.setPenaExecucaoTipo(penaExecucaoTipoDt.getPenaExecucaoTipo());
					} else {
						processoExecucaoDt.setId_PenaExecucaoTipo("");
						processoExecucaoDt.setPenaExecucaoTipo("");
					}
				}
				//verifica se o regime que veio do SPG tem na base do Projudi
				if (!processoExecucaoDt.getId_RegimeExecucao().equals("")) {
					RegimeExecucaoNe regimeExecucaoNe = new RegimeExecucaoNe();
					RegimeExecucaoDt regimeExecucaoDt = new RegimeExecucaoDt();
					//pesquisa pelo codigo do SPG no projudi
					regimeExecucaoDt = regimeExecucaoNe.consultarId(processoExecucaoDt.getId_RegimeExecucao());
					if (regimeExecucaoDt!=null){//se encontrou busca a descricao no projudi o regime
						processoExecucaoDt.setId_RegimeExecucao(regimeExecucaoDt.getId());
						processoExecucaoDt.setRegimeExecucao(regimeExecucaoDt.getRegimeExecucao());
					} else {
						processoExecucaoDt.setId_RegimeExecucao("");
						processoExecucaoDt.setRegimeExecucao("");
					}
				}
				//verifica se o crime que veio da SPG tem na base do Projudi
	//			CondenacaoExecucaoDt condenacaoExecucaoDt = new CondenacaoExecucaoDt();
	//			List tempListaCondenacao = new ArrayList();
	//			tempListaCondenacao = processoExecucaoDt.getListaCondenacoes();
	//			processoExecucaoDt.setListaCondenacoes(null);
	//			for (int i=0;i<tempListaCondenacao.size();i++){
	//				condenacaoExecucaoDt = (CondenacaoExecucaoDt) tempListaCondenacao.get(i);
	//				if (!condenacaoExecucaoDt.getId_CrimeExecucao().equals("")){
	//					CrimeExecucaoDt crimeExecucaoDt = new CrimeExecucaoDt();
	//					CrimeExecucaoNe crimeExecucaoNe = new CrimeExecucaoNe();
	//					//pesquisa pelo codigo do SPG no projudi
	//					crimeExecucaoDt = crimeExecucaoNe.consultarCodigo(condenacaoExecucaoDt.getId_CrimeExecucao());
	//					if (crimeExecucaoDt!=null){
	//						condenacaoExecucaoDt.setId_CrimeExecucao(crimeExecucaoDt.getId());
	//						condenacaoExecucaoDt.setCrimeExecucao(crimeExecucaoDt.getCrimeExecucao());
	//						processoExecucaoDt.addListaCondenacoes(condenacaoExecucaoDt);
	//					}
	//				}
	//			}
			}
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return mensagem;
	}
	
//	/**
//	 * Verifica os dados do c�lculo de liquida��o antes de salvar
//	 * @param calculoLiquidacaodt
//	 * @throws Exception
//	 * @author kbsriccioppo
//	 */
//	public String VerificarCalculoLiquidacao(CalculoLiquidacaoDt dados) {
//		String stRetorno = "";
//
//		if (dados.getIdProcesso().length() == 0) stRetorno += "O campo N�mero do Processo � obrigat�rio.";
////		if (dados.getDataTerminoPena().length() == 0) stRetorno += "O campo Data T�rmino da Pena � obrigat�rio.";
//		
//		return stRetorno;
//	}
	
	
	/**
	 * Consulta os dados b�sicos de um processo e devolve um objeto do tipo ProcessoDt com esses dados
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @author kbsriccioppo
	 */
	public ProcessoDt consultarIdSimples(String id_Processo) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();
		ProcessoDt dtRetorno = null;
		
		dtRetorno = processoNe.consultarIdSimples(id_Processo);
				
		return dtRetorno;
	}
	

	public ServentiaDt consultarIdServentia(String id_serventia ) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe();
		ServentiaDt dtRetorno=null;
		 
		dtRetorno= serventiaNe.consultarId(id_serventia ); 
		
		return dtRetorno;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 * @throws Exception 
	 */
	public List getAssuntosProcesso(String id_Processo) throws Exception{
		List tempList = null;
		ProcessoAssuntoNe neObjeto = new ProcessoAssuntoNe();
		
		tempList = neObjeto.consultarAssuntosProcesso(id_Processo);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Lista os eventos do processo de origem exceto Guia de Recolhimento e Tr�nsito em Julgado
	 * @param id_Processo: id do processo de execu��o penal origem.
	 * @return List<ProcessoExecucaoDt> lista os eventos
	 * @throws Exception
	 * @author kbsriccioppo
	 */
	public List listarEventoProcessoOrigem(String id_Processo) throws Exception{
		
		List listaEvento = new ArrayList();
		ProcessoEventoExecucaoNe neObjeto = new ProcessoEventoExecucaoNe();
		
		listaEvento = neObjeto.listarEventos(id_Processo, "");
				
		neObjeto = null;
		return listaEvento;
	}

	/**
     * Verifica se o usu�rio pode modificar dados do ProcessoExecucao.
     * @param processoDt
     * @param usuarioDt
     * @return mensagem
     * @author wcsilva
     */
    public String podeModificarDados(ProcessoDt processoDt, UsuarioDt usuarioDt) {
		String stMensagem = "";
		// Se usu�rio for de serventia diferente do processo, n�o poder� modificar dados
		if (!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia())) stMensagem += "Sem permiss�o para modificar dados do processo. (Motivo: Processo de outra serventia.) \n";

		if (processoDt.isArquivado()) {
			stMensagem += " N�o � poss�vel executar essa a��o, este processo est� arquivado. \n";
		}else if (processoDt.isArquivado()) {
			stMensagem += " N�o � poss�vel executar essa a��o, este processo est� com ERRO DE MIGRA��O.\n";
		}

		return stMensagem;
	}
    
    /**
     * Verifica se o npumero do processo informado j� est� cadastrado no projudi
     * @param processoExecucaoDt: processo de execu��o
     * @return processoDt: objeto com os dados do processoDt
     * @throws Exception
     */
    public List existeProcessoCadastrado(String numeroCompletoProcesso) throws Exception{
//    	atualizarNumeroProcessoFisico(processoExecucaoDt);
    	List lista = null;
    	
   		lista = new ProcessoNe().listarProcessoNumeroCompleto(numeroCompletoProcesso, false);	

    	return lista;
    }
    
    /**
     * Lista as modalidades do processo execu��o (a��o penal)
     * @param idProcessoExecucao
     * @return lista de modalidades
     * @throws Exception
     */
	public List listarModalidadesDaAcaoPenal(String idProcessoExecucao, FabricaConexao obFabricaConexao) throws Exception{
		List lista = null; 
		lista = new ProcessoEventoExecucaoNe().listarModalidadesDaAcaoPenal(idProcessoExecucao, obFabricaConexao);
		return lista;
	}
	
	/**
	 * Exclui a modalidade (processoEventoExecucao, eventoExecucao)
	 * @param modalidade: ProcessoEventoExecucaoDt
	 * @throws Exception
	 */
	public void excluirModalidade(ProcessoEventoExecucaoDt modalidade) throws Exception{
			new ProcessoEventoExecucaoNe().excluirCompleto(modalidade);
		
	}
	
	public void excluirAlcunha(ProcessoParteAlcunhaDt alcunha) throws Exception{
			new ProcessoParteAlcunhaNe().excluir(alcunha);
		
	}
	
	public void excluirSinal(ProcessoParteSinalDt sinal) throws Exception{
			new ProcessoParteSinalNe().excluir(sinal);
		
	}
	
	/**
	 * Rela��o dos eventos de pena restritiva de direito e sursis que s�o inseridos, alterados e exclu�dos pela a��o penal
	 * @param idEventoExecucao: id do evento para verificar se ele consta na lista de eventos do m�todo
	 * @return boolean.
	 */
	public boolean isModalidade_Sursis(String idEventoExecucao){
		return new ProcessoEventoExecucaoNe().isEventoPenaRestritivaDireito(idEventoExecucao);
	}
	
	/**
	 * consulta o ProcessoEventoExecucao, tendo como filtro o Id_ProcessoExecucao e Id_EventoExecucao
	 * @param Id_ProcessoExecucao: idProcessoExecucao relacionado ao id_ProcessoEventoExecucao que ser� consultado
	 * @param Id_EventoExecucao: idEventoExecucao relacionado ao id_ProcessoEventoExecucao que ser� consultado
	 * @return ProcessoEventoExecucao
	 * @throws Exception
	 * @author wcsilva
	 */
	public ProcessoEventoExecucaoDt consultarSursisDaAcaoPenal(String idProcessoExecucao, String idEventoExecucao) throws Exception{
		ProcessoEventoExecucaoDt processoEventoExecucaoDt = null;
			processoEventoExecucaoDt = new ProcessoEventoExecucaoNe().consultarProcessoEventoExecucao(idProcessoExecucao, idEventoExecucao);
		
		return processoEventoExecucaoDt;
	}
	
	
	public ProcessoEventoExecucaoDt consultarSursisDaAcaoPenal(String idProcessoExecucao, String idEventoExecucao, FabricaConexao obFabricaConexao) throws Exception{
		ProcessoEventoExecucaoDt processoEventoExecucaoDt = null;
			processoEventoExecucaoDt = new ProcessoEventoExecucaoNe().consultarProcessoEventoExecucao(idProcessoExecucao, idEventoExecucao, obFabricaConexao);
		
		return processoEventoExecucaoDt;
	}
	
	
	public ProcessoTipoDt consultarProcessoTipo(String processoTipoCodigo) throws Exception{
		ProcessoTipoDt processoTipoDt = null;
			processoTipoDt = new ProcessoTipoNe().consultarProcessoTipoCodigo(processoTipoCodigo);
		
		return processoTipoDt;
	}
	
	/**
	 * Extrai do N�mero do Processo F�sico a composi��o do processo, sendo:
	 * ano, n�mero do processo, d�gito do processo e c�digo do F�rum. 
	 */
	public void atualizarNumeroProcessoFisico(ProcessoExecucaoDt processoExecucaoDt){
		if (processoExecucaoDt.getProcessoFisicoNumero() == null || processoExecucaoDt.getProcessoFisicoNumero().length() != 25) 
			return;
		
		String ano = processoExecucaoDt.getProcessoFisicoNumero().substring(11, 15);
		String numero = processoExecucaoDt.getProcessoFisicoNumero().substring(0, 7);
		String digito = processoExecucaoDt.getProcessoFisicoNumero().substring(8, 10);
		
		processoExecucaoDt.getProcessoDt().setAnoProcessoNumero(ano + Funcoes.completarZeros(numero,7) + digito);
		processoExecucaoDt.getProcessoDt().setForumCodigo(getNumeroProcessoFisicoForum(processoExecucaoDt));
	}
	
	//Extrai do N�mero do processo f�sico o c�digo do f�rum.
	private String getNumeroProcessoFisicoForum(ProcessoExecucaoDt processoExecucaoDt) {
		return String.valueOf(Funcoes.StringToLong(processoExecucaoDt.getProcessoFisicoNumero().substring(21, 25)));
	}
	
	/**
	 * M�todo utilizado para os processos eletr�nicos, portanto todas as serventias est�o com o subtipo Vara de Execu��o Penal
	 * @param id_Processo
	 * @param grupoCodigo
	 * @return
	 * @throws Exception
	 * @author wcsilva
	 */
	public ServentiaCargoDt consultarServentiCargoResponsavelProcesso(String id_Processo, String grupoCodigo) throws Exception{
		ServentiaCargoDt tempList = null;
		ProcessoResponsavelNe neObjeto = new ProcessoResponsavelNe();
		
//			consulta utilizada para os processos eletr�nicos, portanto todas as serventias est�o com o subtipo Vara de Execu��o Penal
		tempList = neObjeto.consultarResponsavelProcesso(id_Processo, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL));
				
		neObjeto = null;
		return tempList;
	}
	
	public String consultarIdJSON(String idProcessoexecucao) throws Exception{
		String retorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoExecucaoPs obPersistencia = new  ProcessoExecucaoPs(obFabricaConexao.getConexao());
			retorno= obPersistencia.consultarIdJSON(idProcessoexecucao ); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public byte[] relDadosAcaoPenal(String diretorioProjetos, List listaAcaoPenal, UsuarioDt usuarioDt) throws Exception{
		byte[] temp = null;
		ByteArrayOutputStream baos = null;

		try{
			if (listaAcaoPenal != null && listaAcaoPenal.size() > 0){

				List listaBean = new ArrayList();
				for (ProcessoExecucaoDt pee : (List<ProcessoExecucaoDt>)listaAcaoPenal) {
					ProcessoExecucaoBeanDt bean = new ProcessoExecucaoBeanDt();
					bean.setComarcaOrigem(pee.getCidadeOrigem() + "-" + pee.getUfOrigem());
					bean.setDataAcordao(pee.getDataAcordao());
					bean.setDataAdmonitoria(pee.getDataAdmonitoria());
					bean.setDataDenuncia(pee.getDataDenuncia());
					bean.setDataDistribuicao(pee.getDataDistribuicao());
					bean.setDataInicioCumprimentoPena(pee.getDataInicioCumprimentoPena());
					bean.setDataPronuncia(pee.getDataPronuncia());
					bean.setDataSentenca(pee.getDataSentenca());
					bean.setDataTransitoJulgado(pee.getDataTransitoJulgado());
					bean.setDataTransitoJulgadoMP(pee.getDataTransitoJulgadoMP());
					bean.setNumeroAcaoPenal(pee.getNumeroAcaoPenal());
					bean.setVaraOrigem(pee.getVaraOrigem());
					bean.setPena(pee.getPenaExecucaoTipo());
					bean.setRegime(pee.getRegimeExecucao());
					bean.setLocalCumprimentoPena(pee.getLocalCumprimentoPena());
					String texto = "";
					if (pee.getListaCondenacoes() != null && pee.getListaCondenacoes().size() > 0){
						texto = "Condena��es:\n";
						for (CondenacaoExecucaoDt cond : (List<CondenacaoExecucaoDt>)pee.getListaCondenacoes()) {
							String reinc = cond.isReincidente()?"Sim":"N�o";
							texto += "Crime: " + cond.getCrimeExecucao() + " * Data do Fato: " + cond.getDataFato() + " * Reincidente: " + reinc + " * Extin��o: " + cond.getCondenacaoExecucaoSituacao() + "\n"; 
						}
					} else texto = "Condena��es: - ";
					bean.setTextoCondenacao(texto);
					
					if (pee.getListaModalidade() != null && pee.getListaModalidade().size() > 0){
						texto = "Modalidades: ";
						for (int i=0; i<pee.getListaModalidade().size(); i++){
							ProcessoEventoExecucaoDt mod = (ProcessoEventoExecucaoDt)pee.getListaModalidade().get(i);
							texto += mod.getEventoRegimeDt().getRegimeExecucao();
							if (i < pee.getListaModalidade().size() -1) texto += ", ";
						}
					} else texto = "Modalidades: - ";
					bean.setTextoModalidade(texto);
					bean.setTextoSursis(pee.getTempoTotalSursisAnos());
					listaBean.add(bean);
				}
				
				String titulo = "DADOS DAS A��ES PENAIS\nProcesso: " + ((ProcessoExecucaoDt)listaAcaoPenal.get(0)).getProcessoDt().getProcessoNumeroCompleto() + "\n\n" +
								"Sentenciado: " + ((ProcessoParteDt)((ProcessoExecucaoDt)listaAcaoPenal.get(0)).getProcessoDt().getListaPolosPassivos().get(0)).getNome() + " - " +
								"Nome da M�e: " + ((ProcessoParteDt)((ProcessoExecucaoDt)listaAcaoPenal.get(0)).getProcessoDt().getListaPolosPassivos().get(0)).getNomeMae() + " - " +
								"Data de Nascimento: " + ((ProcessoParteDt)((ProcessoExecucaoDt)listaAcaoPenal.get(0)).getProcessoDt().getListaPolosPassivos().get(0)).getDataNascimento();
				
				String pathJasper = diretorioProjetos + "WEB-INF" + File.separator + "relatorios" + File.separator + "processoExecucao" + File.separator + "dadosAcaoPenal.jasper";
				
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", diretorioProjetos + "imagens" + File.separator + "logoTJ.jpg");
				parametros.put("serventia", usuarioDt.getServentia());
				parametros.put("titulo", titulo);
				
				InterfaceJasper ei = new InterfaceJasper(listaBean);

				JasperPrint jp = JasperFillManager.fillReport(pathJasper, parametros, ei);
				JRPdfExporter jr = new JRPdfExporter();
				jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
				baos = new ByteArrayOutputStream();
				jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
				jr.exportReport();

				temp = baos.toByteArray();
				baos.close();
			}
			
		} catch(Exception e) {
			try{if (baos!=null) baos.close();  }catch(Exception e2) {		}
		} finally{
			baos = null;
		}

		return temp;
	}
	
	public boolean validarForumCodigo(String forumCodigo) throws Exception{
		
		ForumDt forum = new ForumNe().consultarForumCodigo(forumCodigo);
		if (forum != null) return true;
		else return false;
		
	}
	
	public String validarProcessoFisicoNumero(String numeroProcesso, boolean isVerificarProcessoCadastrado)throws Exception{
		String msgRetorno = "O N�mero do Processo F�sico informado n�o � v�lido. Motivo: \n";
		String msg = "";
		
		String[] num = numeroProcesso.split("\\.");
		//posi��o 0: numero
		//posi��o 1: digito
		//posi��o 2: ano
		//posi��o 3: identifica��o da Justi�a Estadual
		//posi��o 4: identifica��o do TJGO
		//posi��o 5: c�digo do f�rum
		if ((num[3] != null && !num[3].equals("8")) || (num[4] != null && !num[4].equals("09")) ) {
			msg += "- Verifique d�gitos \"8.09\". \n";
		}
		if (num[5] != null && !validarForumCodigo(num[5])) {
			msg += "- C�digo do F�rum inv�lido. \n";
		}
		if (msg.length() > 0){
			msgRetorno += msg;
		} else {
			msgRetorno = "";
		}
		
		if (isVerificarProcessoCadastrado && msgRetorno.length() == 0){
			ProcessoDt proc = new ProcessoNe().consultarProcessoNumeroCompleto(numeroProcesso,null);
			if (proc != null)
				msgRetorno = "Este n�mero de processo j� est� cadastrado!";
		}
	
		return msgRetorno;
	}
	
	public void alterarNumeroProcesso(ProcessoDt processoDt) throws Exception{		 
			new ProcessoNe().alterarNumeroProcesso(processoDt);				
	}
	
	/**
     * adicona a modalidade num processo de execu��o (a��o penal)
     */
    public String adicionarModalidadeProcessoExecucao(ProcessoExecucaoDt processoExecucaoDt) throws Exception{
    	
    	String mensagemRetorno = "";
    	
		mensagemRetorno = this.verificarDadosModalidade(processoExecucaoDt);
		if (mensagemRetorno.length() == 0) {
        	ProcessoEventoExecucaoDt pee = new ProcessoEventoExecucaoDt();
        	
        	//vincula o regime � modalidade
        	pee.getEventoRegimeDt().setId_RegimeExecucao(processoExecucaoDt.getId_Modalidade()); 
        	pee.getEventoRegimeDt().setRegimeExecucao(processoExecucaoDt.getModalidade());

        	//informa a data in�cio qdo for altera��o da a��o penal
        	if (processoExecucaoDt.getId().length() > 0) pee.setDataInicio(processoExecucaoDt.getDataTransitoJulgado());
        	pee.setIdProcessoExecucaoPenal(processoExecucaoDt.getId_ProcessoExecucaoPenal());
   			this.vincularIdEvento_Quantidade_Modalidade(pee, processoExecucaoDt.getListaCondenacoes());

   			processoExecucaoDt.addListaModalidade(pee);
        }
    	processoExecucaoDt.setId_Modalidade("null");
        processoExecucaoDt.setModalidade("null");
    			
        return mensagemRetorno;
    }
    
    /**
     * M�todo respons�vel em adicionar condena��es a um processo de execu��o (a��o penal)
     * @throws Exception 
     */
    public String adicionarCondenacaoProcesso(HttpServletRequest request, UsuarioDt usuarioDt, int paginaAtual, ProcessoExecucaoDt processoExecucaoDt) throws Exception{
    	String mensagemRetorno = "";
    	
		mensagemRetorno = verificarDadosCondenacao(processoExecucaoDt);;
    	if (mensagemRetorno.length() == 0){
    		 CondenacaoExecucaoDt condenacaoExecucaoDt = new CondenacaoExecucaoDt();
             condenacaoExecucaoDt.setId_CrimeExecucao(processoExecucaoDt.getId_CrimeExecucao());
             condenacaoExecucaoDt.setCrimeExecucao(processoExecucaoDt.getCrimeExecucao());
             condenacaoExecucaoDt.setDataFato(processoExecucaoDt.getDataFato());
 			 condenacaoExecucaoDt.setReincidente(processoExecucaoDt.getReincidente());
             condenacaoExecucaoDt.setTempoPenaEmDias(processoExecucaoDt.getTempoPenaEmDias());
             condenacaoExecucaoDt.setTempoPena(processoExecucaoDt.getTempoPenaEmDias());
             condenacaoExecucaoDt.setId_CondenacaoExecucaoSituacao(processoExecucaoDt.getIdCondenacaoExecucaoSituacao());
             condenacaoExecucaoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
             condenacaoExecucaoDt.setId_UsuarioLog(usuarioDt.getId());
             condenacaoExecucaoDt.setObservacao(processoExecucaoDt.getObservacaoCondenacao());
             request.getSession().setAttribute("displayNovaCondenacao", "true");
             
             if (paginaAtual == Configuracao.Curinga6) {
                 condenacaoExecucaoDt.setId_ProcessoExecucao(processoExecucaoDt.getId());
                 salvarCondenacao(condenacaoExecucaoDt);
             }
             processoExecucaoDt.addListaCondenacoes(condenacaoExecucaoDt);
             processoExecucaoDt.setId_CrimeExecucao("null");
             processoExecucaoDt.setCrimeExecucao("null");
             processoExecucaoDt.setDataFato("null");
 			 processoExecucaoDt.setReincidente("");
             processoExecucaoDt.setTempoPenaEmDias("null");
             processoExecucaoDt.setTempoCondenacaoAno("null");
             processoExecucaoDt.setTempoCondenacaoMes("null");
             processoExecucaoDt.setTempoCondenacaoDia("null");
             processoExecucaoDt.setObservacaoCondenacao("null");
    	}
    	
		
    	return mensagemRetorno;
    }
    
    public void excluirGuiaRecolhimento(ProcessoExecucaoDt processoExecucaoDt, String idUsuarioLog, String ipComputadorLog) throws Exception{
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

			LogDt logDt = new LogDt(idUsuarioLog, ipComputadorLog);
			
			//consulta o evento TJ ou GRP para pegar o id_movimenta��o
			ProcessoEventoExecucaoNe peeNe = new ProcessoEventoExecucaoNe();
			String idMovimentacao = peeNe.consultarIdMovimentacao(EventoExecucaoDt.TRANSITO_JULGADO +","+ EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA, processoExecucaoDt.getId(), obFabricaConexao);

			//exclui eventos relacionados � movimenta��o, inclusive eventoLocal e eventoRegime
			List listaEvento = peeNe.listarEventos("", idMovimentacao, obFabricaConexao);
			for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>) listaEvento) {
				evento.setId_UsuarioLog(logDt.getId_UsuarioLog());
				evento.setIpComputadorLog(logDt.getIpComputadorLog());
				peeNe.excluirCompleto(evento, obFabricaConexao);	
			}
			
			//exclui movimenta��o
			this.excluirMovimentacao(idMovimentacao, logDt, obFabricaConexao);
			
			//exclui condena��es
			CondenacaoExecucaoNe condNe = new CondenacaoExecucaoNe(); 
			List listaCondenacoes = condNe.listarCondenacaoDaAcaoPenal(processoExecucaoDt.getId());
			condNe.excluirListaCondenacao(listaCondenacoes, logDt, obFabricaConexao);
			
			//exclui processoExecucao
			processoExecucaoDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
			processoExecucaoDt.setIpComputadorLog(logDt.getIpComputadorLog());
			this.excluir(processoExecucaoDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
    
    public void excluirMovimentacao(String idMovimentacao, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
    	LogDt obLogDt = new LogDt();
		
		MovimentacaoPs obPersistencia = new MovimentacaoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("Movimentacao", idMovimentacao, logDt.getId_UsuarioLog(), logDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),"Id_Movimentacao: " + idMovimentacao,"");
		obPersistencia.excluir(idMovimentacao); 

		obLog.salvar(obLogDt, obFabricaConexao);
		
	}
    
    public void excluir(ProcessoExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		ProcessoExecucaoPs obPersistencia = new ProcessoExecucaoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("ProcessoExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluir(dados.getId());

		obLog.salvar(obLogDt, obFabricaConexao);


	}
    
    public String consultarDescricaoAlcunhaJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		String stTemp = "";
		AlcunhaNe neObjeto = new AlcunhaNe();
		
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
				
		neObjeto = null;
		return stTemp;
	}
    
    public String consultarDataSentenca(String idProcessoExecucao) throws Exception{
		String dataSentenca = "" ;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoExecucaoPs obPersistencia = new ProcessoExecucaoPs(obFabricaConexao.getConexao());
			dataSentenca = obPersistencia.consultarDataSentenca(idProcessoExecucao); 

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dataSentenca;
	}
    
    public String consultarDescricaoAssuntoJSON(String tempNomeBusca, String codigoCNJ, String id_AreaDistribuicao, String posicaoPaginaAtual) throws Exception{
		String stTemp = "";
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();
		
		stTemp = neObjeto.consultarAssuntosAreaDistribuicaoJSON(id_AreaDistribuicao, tempNomeBusca, codigoCNJ, posicaoPaginaAtual);
				
		neObjeto = null;
		return stTemp;
	}
    
    public String consultarDescricaoComarcaJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		String stTemp = "";
		ComarcaNe neObjeto = new ComarcaNe();
		
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);		
		
		neObjeto = null;
		return stTemp;
	}

	public String consultarDescricaoEscolaridadeJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		String stTemp = "";
		EscolaridadeNe neObjeto = new EscolaridadeNe();
		
		stTemp = neObjeto.consultarDescricaoEscolaridadeJSON( tempNomeBusca, posicaoPaginaAtual);
				
		neObjeto = null;
		return stTemp;
	}

	public String consultarDescricaoRacaJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		String stTemp = "";
		RacaNe neObjeto = new RacaNe();
		
		stTemp = neObjeto.consultarDescricaoJSON( tempNomeBusca, posicaoPaginaAtual);		
		
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoCidadeJSON(String tempNomeBusca, String uf, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		CidadeNe neObjeto = new CidadeNe();
		
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, uf, posicaoPaginaAtual);		
		
		neObjeto = null;
		return stTemp;
	}

	 public String consultarDescricaoEstadoCivilJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		EstadoCivilNe neObjeto = new EstadoCivilNe();
		
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);		
		
		neObjeto = null;
		return stTemp;
	}

	public String consultarDescricaoArquivoTipoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
			
		neObjeto = null;
		return stTemp;
	}

	public void setListaRegimeRequest(HttpServletRequest request) throws Exception{
		new ProcessoEventoExecucaoNe().setListaRegimeRequest(request);
	}
	
	public String consultarDescricaoAssuntoPjdJSON(String tempNomeBusca, String id_AreaDistribuicao, String posicaoPaginaAtual) throws Exception{
		String stTemp = "";
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();
		
		stTemp = neObjeto.consultarAssuntosAreaDistribuicaoPjdJSON(id_AreaDistribuicao, tempNomeBusca, posicaoPaginaAtual);
				
		neObjeto = null;
		return stTemp;
	}
}

