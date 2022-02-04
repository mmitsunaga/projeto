package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.HistoricoDt;
import br.gov.go.tj.projudi.dt.InterlocutoriaDt;
import br.gov.go.tj.projudi.dt.IntimacaoDt;
import br.gov.go.tj.projudi.dt.LigacaoDt;
import br.gov.go.tj.projudi.dt.MandadoDt;
import br.gov.go.tj.projudi.dt.ProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.RedistribuicaoFisicoDt;
import br.gov.go.tj.projudi.dt.SentencaDt;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class ProcessoFisicoNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5946815597343241404L;
	
	public boolean verificarAcesso(String processo_numero, String oab)throws Exception {
		boolean boTemp = false;
		String stRetorno = Funcoes.lerURL(ProjudiPropriedades.getInstance().getPropriedade(ProjudiPropriedades.SPG_VERIFICAR_ACESSO).replaceAll("<numero_processo>", processo_numero).replace("<oab>", oab));
		String[] campos = stRetorno.split(";@0;",-1);
		if(campos==null){
			throw new MensagemException("Não foi possível obter os dados Gerais do SPG");				
		}else if ( campos.length>0 && campos[0].trim().equals("0")){
			throw new MensagemException("Erro SPG: " + campos[1]);								
		}else if ( campos.length>0 && campos[0].trim().equals("1")){		
			boTemp=true;
		}
		return boTemp;
	}
	private  ProcessoFisicoDt getDadosGerais(String processoNumero) throws Exception {
				
		ProcessoFisicoDt pf = new ProcessoFisicoDt();	
				
		String stRetorno = Funcoes.lerURL(ProjudiPropriedades.getInstance().getPropriedade(ProjudiPropriedades.SPG_DADOS_GERAIS).replaceAll("<numero_processo>", processoNumero));
		String[] campos = stRetorno.split(";@0;",-1);										
		
		if(campos==null){
			throw new MensagemException("Não foi possível obter os dados Gerais do SPG");				
		}else if ( campos.length>0 && campos[0].trim().equals("0")){
			throw new MensagemException("Erro SPG: " + campos[1]);								
		}else if ( campos.length>0 && campos[0].trim().equals("1")){				
			pf.setNumeroProcesso(campos[2]);
			pf.setNatureza(campos[8]);
			pf.setDataProtocolo(campos[6]);
			pf.setDataAutuacao(campos[10] + " - " + campos[9] );
			pf.setDataDistribuicao(campos[11] + " - " + campos[12] + " &agrave;s " + campos[13]);
			pf.setPromovente(campos[16]);
			pf.setPromovido(campos[18]);
			pf.setFase(campos[19]+" - "+ campos[20] + " &agrave;s	" +	campos[21]);
			pf.setDescricaoFase(campos[22]);
			pf.setComarca(campos[23]);
			pf.setEscrivania(campos[24]);
			pf.setJuiz(campos[25]);
			pf.setDataAudiencia(campos[26]);
			pf.setDataSentenca(campos[27]);
			pf.setDataTransitoJulgado(campos[28]);
			pf.setPromotor(campos[29]);
			if (campos.length > 31) pf.setEscrivaniaCodigo(campos[30]);
			
			// Certifica-se de que o processo tenha ao menos um promovente ou um promovido
			if ( (pf.getPromovido()  == null || (pf.getPromovido()  != null && (pf.getPromovido().equals("null")  || pf.getPromovido().equals("")))) &&
				 (pf.getPromovente() == null || (pf.getPromovente() != null && (pf.getPromovente().equals("null") || pf.getPromovente().equals(""))))	 ) {
				throw new MensagemException("Não foi possível obter a informação das partes no SPG através do serviço SPG_DADOS_GERAIS.");
			}
			
		}
		
		
		return pf;
	}
	
	private void  incluirInterlocutorias(ProcessoFisicoDt pf) throws Exception {
							
		String stRetorno = Funcoes.lerURL(ProjudiPropriedades.getInstance().getPropriedade(ProjudiPropriedades.SPG_INTERLOCUTORIAS).replaceAll("<numero_processo>", pf.getNumeroProcesso()));
		String[] campos = stRetorno.split(";@0;",-1);										
		
		if(campos==null){
			return;				
		}else if ( campos.length>0 && campos[0].trim().equals("0")){
			throw new MensagemException("Erro SPG: " + campos[1]);								
		}else if ( campos.length>0 && campos[0].trim().equals("1")){
										
			List<InterlocutoriaDt> interlocutoriaList = new ArrayList<InterlocutoriaDt>();
			String[] stIntelocutorias = campos[2].split(";@1;");
			for (int i=0;i<stIntelocutorias.length;i++){
				String[] stIntelocutoriasCampos = stIntelocutorias[i].split(";@2;",-1);
				if (stIntelocutoriasCampos!=null && stIntelocutoriasCampos.length>=7){
					interlocutoriaList.add(new InterlocutoriaDt(stIntelocutoriasCampos[0],stIntelocutoriasCampos[1],stIntelocutoriasCampos[3],stIntelocutoriasCampos[6]));
				}
			}
			pf.setInterlocutorias(interlocutoriaList);
			
		}
													
				
	}
	
	public void  incluirMandados(ProcessoFisicoDt pf) throws Exception{
		
		String stRetorno = Funcoes.lerURL(ProjudiPropriedades.getInstance().getPropriedade(ProjudiPropriedades.SPG_MANDADOS).replaceAll("<numero_processo>", pf.getNumeroProcesso()));
		
		String[] campos = stRetorno.split(";@0;",-1);										
		
		if(campos==null){
			return;				
		}else if ( campos.length>0 && campos[0].trim().equals("0")){
			throw new MensagemException("Erro SPG: " + campos[1]);								
		} else if ( campos.length>0 && campos[0].trim().equals("1")){
					
			List<MandadoDt> mandadoList = new ArrayList<MandadoDt>();
			String[] stMandados = campos[2].split(";@1;");
			for (int i=0;i<stMandados.length;i++){
				String[] stMandadoCampos = stMandados[i].split(";@2;",-1);				
				if (stMandadoCampos!=null && stMandadoCampos.length>=17){
			
					//MandadoDt mandado = new MandadoDt(numero,                         tipo,        emissaoData,    recebimentoData,   distribuicaoData,      audienciaData,         partesNome,         escrivania, devolucaoEscrivaniaData,        oficial, recebimentoOficialData,         situacao, prazoCumprimentoData, devolucaoOficialData, motivoDevolucao);
					MandadoDt mandado = new MandadoDt(stMandadoCampos[0], stMandadoCampos[1], stMandadoCampos[3], stMandadoCampos[4], stMandadoCampos[5], stMandadoCampos[8], stMandadoCampos[9], stMandadoCampos[10], stMandadoCampos[11], stMandadoCampos[12], stMandadoCampos[13], stMandadoCampos[14], stMandadoCampos[15], stMandadoCampos[16], stMandadoCampos[17]);
					mandadoList.add(mandado);
				}
				
			}
			pf.setMandadoList(mandadoList);
		}
		
	}
	
	public void incluirHistorico(ProcessoFisicoDt pf) throws Exception{
		
		String stRetorno = Funcoes.lerURL(ProjudiPropriedades.getInstance().getPropriedade(ProjudiPropriedades.SPG_HISTORICO).replaceAll("<numero_processo>", pf.getNumeroProcesso()));
		
		String[] campos = stRetorno.split(";@0;",-1);										
		
		if(campos==null){
			return;				
		}else if ( campos.length>0 && campos[0].trim().equals("0")){
			throw new MensagemException("Erro SPG: " + campos[1]);								
		} else if ( campos.length>0 && campos[0].trim().equals("1")){
			
				List<HistoricoDt> historicoList = new ArrayList<HistoricoDt>();
				String[] stHistorico = campos[2].split(";@1;");
				for (int i=0;i<stHistorico.length;i++){
					String[] stHistoricoCampos = stHistorico[i].split(";@2;",-1);	
					if (stHistoricoCampos!=null && stHistoricoCampos.length>=4){
																			//data                hora                  fase               desc
						HistoricoDt historico = new HistoricoDt(stHistoricoCampos[0],stHistoricoCampos[1],stHistoricoCampos[2],stHistoricoCampos[3]);
						historicoList.add(historico);
					}
			}
			pf.setHistoricoList(historicoList);
		}
		
	}
	
	public void incluirSentenca(ProcessoFisicoDt pf) throws Exception{
		
		String stRetorno = Funcoes.lerURL(ProjudiPropriedades.getInstance().getPropriedade(ProjudiPropriedades.SPG_SENTENCA).replaceAll("<numero_processo>", pf.getNumeroProcesso()));
		
		String[] campos = stRetorno.split(";@0;",-1);										
		
		if(campos==null){
			return;				
		}else if ( campos.length>0 && campos[0].trim().equals("0")){
			throw new MensagemException("Erro SPG: " + campos[1]);								
		} else if ( campos.length>0 && campos[0].trim().equals("1")){
			List<SentencaDt> sentencaList = new ArrayList<SentencaDt>();
			
			String[] stSentenca = campos[2].split(";@1;");
			for (int i=0;i<stSentenca.length;i++){
				String[] stSentencaCampos = stSentenca[i].split(";@2;",-1);
				if (stSentencaCampos!=null && stSentencaCampos.length>=4){
				                                                    //data                tipo              info      transito_julgado
					SentencaDt sentenca = new SentencaDt(stSentencaCampos[0],stSentencaCampos[1],stSentencaCampos[2],stSentencaCampos[3]);
					sentencaList.add(sentenca);
				}
			}
			pf.setSentencas(sentencaList);
		}
		
	}
	

	public void incluirIntimacao(ProcessoFisicoDt pf) throws Exception{
		
		String stRetorno = Funcoes.lerURL(ProjudiPropriedades.getInstance().getPropriedade(ProjudiPropriedades.SPG_INTIMACAO).replaceAll("<numero_processo>", pf.getNumeroProcesso()));
		
		String[] campos = stRetorno.split(";@0;",-1);										
		
		if(campos==null){
			return;				
		}else if ( campos.length>0 && campos[0].trim().equals("0")){
			throw new MensagemException("Erro SPG: " + campos[1]);								
		} else if ( campos.length>0 && campos[0].trim().equals("1")){
			List<IntimacaoDt> intimacaoList = new ArrayList<IntimacaoDt>();
			
			String[] stIntimacao = campos[2].split(";@1;");
			for (int i=0;i<stIntimacao.length;i++){
				String[] stIntimacaoCampos = stIntimacao[i].split(";@2;",-1);
				if (stIntimacaoCampos!=null && stIntimacaoCampos.length>=4){								
					                                             //extracaoData, diarioJusticaNumero,       publicadoData,           circulado,               folha,            despacho
					IntimacaoDt intimacao = new IntimacaoDt(stIntimacaoCampos[0],stIntimacaoCampos[1],stIntimacaoCampos[2],stIntimacaoCampos[3],stIntimacaoCampos[4],stIntimacaoCampos[5]);
					intimacaoList.add(intimacao);
				}
			}
			pf.setIntimacaoList(intimacaoList);
		}

	}
	

	public void  incluirLigacao(ProcessoFisicoDt pf) throws Exception{
		String stRetorno = Funcoes.lerURL(ProjudiPropriedades.getInstance().getPropriedade(ProjudiPropriedades.SPG_LIGACOES).replaceAll("<numero_processo>", pf.getNumeroProcesso()));
		
		String[] campos = stRetorno.split(";@0;",-1);										
		
		if(campos==null){
			return;				
		}else if ( campos.length>0 && campos[0].trim().equals("0")){
			throw new MensagemException("Erro SPG: " + campos[1]);								
		} else if ( campos.length>0 && campos[0].trim().equals("1")){
														
			if(campos.length>=12){
				                                  //00 Data Desmembramento,	01 Processo Desmembrado Atual,02 Lista de Processos Desmembrados,	03 Data Apensamento,04 Numero do Processo Apensado,	05 Lista de Processos Apensados,06 Processo em Andamento,07 Fase, 08 Número de processo dos recursos								
				LigacaoDt ligacao = new LigacaoDt(campos[2],campos[3],campos[4],campos[5],campos[6],campos[7],campos[8],campos[9],campos[10]);
			
				pf.setLigacoes(ligacao);
			}
		}
		
		
	}
	

	public void  incluirRedistribuicao(ProcessoFisicoDt pf) throws Exception{
		
		String stRetorno = Funcoes.lerURL(ProjudiPropriedades.getInstance().getPropriedade(ProjudiPropriedades.SPG_REDISTRIBUICAO).replaceAll("<numero_processo>", pf.getNumeroProcesso()));
		
		String[] campos = stRetorno.split(";@0;",-1);										
		
		if(campos==null){
			return;				
		}else if ( campos.length>0 && campos[0].trim().equals("0")){
			throw new MensagemException("Erro SPG: " + campos[1]);								
		} else if ( campos.length>0 && campos[0].trim().equals("1")){
			List<RedistribuicaoFisicoDt> redistribuicaoList = new ArrayList<RedistribuicaoFisicoDt>();
			
			String[] stRedistribuicao = campos[2].split(";@1;");
			
			for (int i=0;i<stRedistribuicao.length;i++){
				String[] stRedistribuicaCampos = stRedistribuicao[i].split(";@2;",-1);
				if (stRedistribuicaCampos!=null && stRedistribuicaCampos.length>=12){	
																						// data,                    hora,               serventia,                   forum,                 comarca,                    juiz,                 repasse,                natureza,         autuacaoNumero,            autuacaoData,     processoDependencia,        mandadoOrdenatorio);
					RedistribuicaoFisicoDt redistribuicao = new RedistribuicaoFisicoDt(stRedistribuicaCampos[0],stRedistribuicaCampos[1],stRedistribuicaCampos[2],stRedistribuicaCampos[3],stRedistribuicaCampos[4],stRedistribuicaCampos[5],stRedistribuicaCampos[6],stRedistribuicaCampos[7],stRedistribuicaCampos[8],stRedistribuicaCampos[9],stRedistribuicaCampos[10],stRedistribuicaCampos[11]);
					redistribuicaoList.add(redistribuicao);
				}
			}
			
			pf.setRedistribuicaoList(redistribuicaoList);
		}

	}

	public ProcessoFisicoDt getProcessoFisico(String numero) throws Exception {
	
		ProcessoFisicoDt pf = getDadosGerais(Funcoes.obtenhaSomenteNumeros(numero));
		
		incluirInterlocutorias(pf);
		
		incluirMandados(pf);
		
		incluirHistorico(pf);		

		incluirSentenca(pf);
		
		incluirIntimacao(pf);

		incluirLigacao(pf);
		
		incluirRedistribuicao(pf);

		return pf;
	}
	
	public ProcessoFisicoDt getProcessoFisicoDadosGerais(String numero) throws Exception {		
		return getDadosGerais(Funcoes.obtenhaSomenteNumeros(numero));				
	}
	
}
