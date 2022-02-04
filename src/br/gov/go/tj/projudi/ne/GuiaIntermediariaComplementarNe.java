package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.utils.Funcoes;

public class GuiaIntermediariaComplementarNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = -5812544993480798178L;
	
	public static String OFICIAL_COMPANHEIRO_SIM = "1";
	public static String OFICIAL_COMPANHEIRO_NAO = "0";
	
	public static String CITACAO_HORA_CERTA_SIM = "1";
	public static String CITACAO_HORA_CERTA_NAO = "0";
	
	public static String FORA_HORARIO_NORMAL_SIM = "1";
	public static String FORA_HORARIO_NORMAL_NAO = "0";
	
	public static String LOCOMOCAO_PENHORA_SIM = "1";
	public static String LOCOMOCAO_PENHORA_NAO = "0";
	
	public static String LOCOMOCAO_AVALICAO_SIM = "1";
	public static String LOCOMOCAO_AVALICAO_NAO = "0";

	private LocomocaoNe locomocaoNe;
	private BairroNe bairroNe;
	
	/**
	 * Construtor defautlt
	 */
	public GuiaIntermediariaComplementarNe() {
		locomocaoNe = new LocomocaoNe();
		bairroNe = new BairroNe();
	}
	
	/**
	 * Método para extrair as locomoções da guia paga.
	 * @param List listaGuiaItemDt_GuiaLocomocaoPaga
	 * @param int tipoLocomocao
	 * @return List listaBairroDt
	 * @throws Exception
	 */
	public List extrairListaBairroDt(List listaGuiaItemDt_GuiaLocomocaoPaga, int tipoLocomocao) throws Exception{
		List listaBairroDt = null;
				
		if( listaGuiaItemDt_GuiaLocomocaoPaga != null && listaGuiaItemDt_GuiaLocomocaoPaga.size() > 0 ) {
			
			locomocaoNe = new LocomocaoNe();
			bairroNe = new BairroNe();
			
			for( int i = 0; i < listaGuiaItemDt_GuiaLocomocaoPaga.size(); i++ ) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt_GuiaLocomocaoPaga.get(i);
				
				if( Funcoes.StringToInt( guiaItemDt.getCustaDt().getId() ) == tipoLocomocao ) {
					
					if( listaBairroDt == null ) {
						listaBairroDt = new ArrayList();
					}
					
					LocomocaoDt locomocaoDt = (LocomocaoDt) locomocaoNe.consultarIdGuiaItem( guiaItemDt.getId() );
					BairroDt bairroDt = bairroNe.consultarId(locomocaoDt.getBairroDt().getId());
					
					listaBairroDt.add( bairroDt );
				}
			}
		}
		
		
		return listaBairroDt;
	}
	
	/**
	 * Método para extrair os bairros das locomoções de intimação, citação, avaliação e penhora.
	 * @param List listaGuiaItemDt_GuiaLocomocaoPaga
	 * @return List listaBairroDtLocomocaoContaVinculada
	 * @throws Exception
	 */
	public List extrairListaBairroDtContaVinculada(List listaGuiaItemDt_GuiaLocomocaoPaga) throws Exception{
		List listaBairroDtLocomocaoContaVinculada = null;
				
		if( listaGuiaItemDt_GuiaLocomocaoPaga != null && listaGuiaItemDt_GuiaLocomocaoPaga.size() > 0 ) {
			
			locomocaoNe = new LocomocaoNe();
			bairroNe = new BairroNe();
			
			for( int i = 0; i < listaGuiaItemDt_GuiaLocomocaoPaga.size(); i++ ) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt_GuiaLocomocaoPaga.get(i);
				
				if( Funcoes.StringToInt( guiaItemDt.getCustaDt().getId() ) == CustaDt.LOCOMOCAO_PARA_TRIBUNAL ) {
					
					LocomocaoDt locomocaoDt = (LocomocaoDt) locomocaoNe.consultarIdGuiaItem( guiaItemDt.getId() );
					if( locomocaoDt != null ) {
						BairroDt bairroDt = bairroNe.consultarId(locomocaoDt.getBairroDt().getId());
						if( bairroDt != null ) {
							if( listaBairroDtLocomocaoContaVinculada == null ) {
								listaBairroDtLocomocaoContaVinculada = new ArrayList();
							}
							
							listaBairroDtLocomocaoContaVinculada.add( bairroDt );
						}
					}
				}
			}
		}
					
		return listaBairroDtLocomocaoContaVinculada;
	}

	/**
	 * Método para consultar a guia complementar emitida da guia de locomoção.
	 * @param String idGuiaLocomocao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaComplementarGuiaLocomocao(String idGuiaLocomocao){
		return consultarGuiaComplementarGuiaLocomocao(idGuiaLocomocao);
	}
}
