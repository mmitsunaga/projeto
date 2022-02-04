package br.gov.go.tj.projudi.ne;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UfrValorDt;
import br.gov.go.tj.projudi.dt.ZonaBairroRegiaoDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Classe para abranger todo os cï¿½lculos genï¿½ricos das guias e seus itens.
 * 
 * @author fasoares
 */
public class GuiaCalculoNe extends Negocio {
	
	//TAXA JUDICIARIA: Atï¿½ UFR(32.67) ufr 1670.73 ou R$ 54582.22 ï¿½ 0.5%
	public static final BigDecimal VALOR_MEIO_PORCENTO = new BigDecimal(ProjudiPropriedades.getInstance().getTaxaJudiciariaValorMeioPorcento());//78181.91D;
	public static final BigDecimal MEIO_PORCENTO = new BigDecimal(0.005D);
	
	//TAXA JUDICIARIA: Atï¿½ UFR(32.67) ufr 8353.66 ou R$ 272914.22 ï¿½ 1%
	public static final BigDecimal VALOR_UM_PORCENTO = new BigDecimal(ProjudiPropriedades.getInstance().getTaxaJudiciariaValorUmSetentaCincoPorcento());//390779.67;
	public static final Double UM_PORCENTO = 0.01D;
	
	//TAXA JUDICIARIA: Acima de UFR(34.30) ufr 8353.66 ou R$ 272914.22 ï¿½ 1.75%
	public static final BigDecimal VALOR_UM_PONTO_SETENTA_E_CINCO_PORCENTO = VALOR_UM_PORCENTO;
	public static final BigDecimal UM_PONTO_SETENTA_E_CINCO_PORCENTO = new BigDecimal(0.0175D);
	
	public static final Double CITACAO_HORA_CERTA = 6.00D;
	public static final Double QUANTIDADE_ACRESCIMO_PESSOA = 1.00D;
	
	public static final int DESCONTO_TAXA_JUDICIARIA_0 = 0;
	public static final int DESCONTO_TAXA_JUDICIARIA_50 = 50;
	public static final int DESCONTO_TAXA_JUDICIARIA_100 = 100;
	
	public static final String LEI_REDUCAO_50_PORCENTO 	= "50";
	
	public static final BigDecimal VALOR_SALARIO_MINIMO = new BigDecimal("1045.00");
	
	public static final int LOCOMOCAO_CIVEL 			= 1;
	public static final int LOCOMOCAO_CRIMINAL 			= 2;
	public static final int LOCOMOCAO_SEGUNDO_GRAU 		= 3;
	public static final int LOCOMOCAO_CONTA_VINCULADA 	= 4;
	public static final int LOCOMOCAO_SEGUNDO_GRAU_CONT = 5;
	
	private static final long serialVersionUID = -1948678849904209121L;
	private List<GuiaItemDt> listaGuiaItemDt = null;
	private Double totalGuia = 0.0D;
	private String custasQuantitativas;
	
	/**
	 * Construtor
	 */
	public GuiaCalculoNe() {
	}
	
	/**
	 * Mï¿½todo para setar a quantidade para custas que utilizam quantidades como quantidades de pï¿½ginas ou KM.
	 * @param String custasQuantitativas
	 */
	public void setCustasQuantitativas(String custasQuantitativas) {
		this.custasQuantitativas = custasQuantitativas;
	}
	
	/**
	 * Obter a quantidade da custa especï¿½fica.
	 * @param String idCustaDt
	 * @return int quantidade
	 */
	public int getCustaQuantidade(String idCustaDt) {
		int retorno = 1;
		
		String[] idCusta_quantidade = this.custasQuantitativas.split(";");
		if( idCusta_quantidade.length > 0 ) {
			for(int i = 0; i < idCusta_quantidade.length; i++ ) {
				String idCusta = idCusta_quantidade[i].split(":")[0];
				String quantidade = idCusta_quantidade[i].split(":")[1];
				
				if( idCustaDt.equals(idCusta) ) {
					retorno = Funcoes.StringToInt(quantidade);
					break;
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo que retorna o total calculado da guia.
	 * @return Double
	 */
	public Double getTotalGuia() {
		return this.totalGuia;
	}
	
	/**
	 * Mï¿½todo para setar o valor total da guia.
	 * @param Double valor
	 */
	public void setTotalGuia(Double valor) {
		totalGuia = valor;
	}
	
	/**
	 * Mï¿½todo que faz a divisï¿½o do valor pelo valor da UFR. 
	 * @param String valor
	 * @return Double
	 * @throws Exception
	 */
	public Double dividiValorUFR(String valor) throws Exception {
		Double resultado = null;
		resultado = Funcoes.StringToDouble(valor);
		
		UfrValorNe ufrValorNe = new UfrValorNe();
		UfrValorDt ufrValorDt = ufrValorNe.consultarDataAtual();
		
		if (ufrValorDt == null) throw new MensagemException("UFR Valor nï¿½o configurado para a data atual.");
		
		resultado = resultado / ufrValorDt.obtenhaValorUFR();
		
		return resultado.doubleValue();
	}
	
	/**
	 * Mï¿½todo para recalcular o total da guia atravï¿½s da lista de guiaItemDt.
	 * @param List<GuiaItemDt> listaGuiaItemDt
	 * @throws Exception
	 */
	public void recalcularTotalGuia(List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			this.totalGuia = 0.0D;
				
			//for(int i = 0; i < listaGuiaItemDt.size(); i++) {
			for (Iterator<GuiaItemDt> iter = listaGuiaItemDt.listIterator(); iter.hasNext(); ) {
				GuiaItemDt guiaItemDt = iter.next();
				
				String valorCalculado = guiaItemDt.getValorCalculado();
				valorCalculado = valorCalculado.replace(".", "");
				valorCalculado = valorCalculado.replace(",", ".");
				BigDecimal valorCalculadoFormatado 	= new BigDecimal(Funcoes.StringToDouble(valorCalculado));
				
				if( valorCalculadoFormatado.compareTo(new BigDecimal("0.00")) > 0 ) {
				this.totalGuia = this.totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
			}
				else {
					if( valorCalculadoFormatado.compareTo(new BigDecimal("0.00")) < 0 ) {
						iter.remove();
					}
				}
			}
			
			this.retirarCasasDecimais();
		}
	}
	
	/**
	 * Mï¿½todo para verificar se o total da guia estï¿½ zerada ou inferior a zero.
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaZeradaOuNegativa() throws Exception {
		boolean retorno = false;
		
		if( this.totalGuia <= 0.0D ) {
			retorno = true;
		}
		
		return retorno;
	}
	
	public boolean isGuiaZeradaOuNegativa(List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		boolean retorno = false;
		
		BigDecimal total = new BigDecimal("0.0");
		
		if( listaGuiaItemDt != null && !listaGuiaItemDt.isEmpty() ) {
			for( GuiaItemDt guiaItemDt: listaGuiaItemDt ) {
				BigDecimal item = new BigDecimal(Funcoes.StringToDouble(guiaItemDt.getValorCalculado()));
				
				total = total.add(item);
			}
			
			if( total.compareTo(new BigDecimal("0.0")) <= 0 ) {
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Recalcular os itens da guia complementar.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt  			Itens das guias calculadas e pagas
	 * @param List listaGuiaItemDt					Itens da guia nova
	 * @param List listaGuiaItemDtLocomocao
	 * @author fasoares (Fred)
	 * 
	 * @throws Exception
	 */
	public void recalcularGuiaComplementar(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaItemDt, List listaGuiaItemDtLocomocao) throws Exception {

		
		if( guiaEmissaoDt != null && guiaEmissaoDt.getListaGuiaItemDt() != null && guiaEmissaoDt.getListaGuiaItemDt().size() > 0 && listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			
			//"for" para o total de itens das guias jï¿½ pagas, ou seja, itens antigos jï¿½ calculados
			for(int j = 0; j < guiaEmissaoDt.getListaGuiaItemDt().size(); j++ ) {
				
				GuiaItemDt guiaItemDtAntigo = (GuiaItemDt) guiaEmissaoDt.getListaGuiaItemDt().get(j);
				boolean seraRetirado = true;
				
				//"for" para os itens novos calculados para esta nova guia
				for(int i = 0; i < listaGuiaItemDt.size(); i++) {
					GuiaItemDt guiaItemDtNovo = (GuiaItemDt) listaGuiaItemDt.get(i);
					
					boolean calcularDiferenca = false;
					
					//Itens devem ser iguais(id_custa) para o recalculo
					if( guiaItemDtNovo.getCustaDt().getId().equals( guiaItemDtAntigo.getCustaDt().getId() ) ) {
						seraRetirado = false;
						
						if( 
							( guiaItemDtNovo.getCustaDt().getId_ArrecadacaoCusta().equals(ArrecadacaoCustaDt.DISTRIBUIDOR) && guiaItemDtNovo.getGuiaEmissaoDt() != null && guiaItemDtNovo.getGuiaEmissaoDt().getTipoGuiaReferenciaDescontoParcelamento() == null ) 
							|| 
							( guiaItemDtNovo.getCustaDt().getId_ArrecadacaoCusta().equals(ArrecadacaoCustaDt.PROTOCOLO) && guiaItemDtNovo.getGuiaEmissaoDt() != null && guiaItemDtNovo.getGuiaEmissaoDt().getTipoGuiaReferenciaDescontoParcelamento() == null ) 
							) {
							
							guiaItemDtNovo.setQuantidade("1");
							guiaItemDtNovo.setValorCalculado("0");
						}
						else {
							
							calcularDiferenca = true;
							
						}
					}
					else {
						//Comentï¿½rio do Fred:
						
						//Este "else" foi adicionado no perï¿½odo de homologaï¿½ï¿½o da guia complementar.
						//Marcelo da Corregedoria descreveu o seguinte caso:
						//- A guia inicial foi emitida e teve a cobranï¿½a do item 16
						//- A guia complementar, com a mudanï¿½a da natureza incluiu o item 23. O item de regimento 23 
						//ï¿½ 70% do 16. O Marcelo disse que dependendo do valor base de cï¿½lculo do processo, o item nï¿½o cobra nada a mais.
						//Ou seja, se no item 16 o valor da causa cobrou R$ 100 e se no item 23 com o valor da causa informado cobrar 100
						//Nï¿½o se cobra o item 23, pois o item 23 tem seu "cï¿½digo de regimento referï¿½ncia" o item 16.
						//Entï¿½o fiz a alteraï¿½ï¿½o abaixo.
						
						//NOTA para entender: Todo item de custa tem seu "Cï¿½digo de Regimento".
						//Pode ter item de custa que tenha o seu prï¿½prio "Cï¿½digo de Regimento" mas a referï¿½ncia de 
						//seu cï¿½lculo ï¿½ o "Cï¿½digo de Regimento Referï¿½ncia".
						//No banco e dt o "Cï¿½digo de regimento" ï¿½ o campo CODIGO_REGIMENTO
						//e o "Cï¿½digo de regimento referï¿½ncia" ï¿½ o campo CODIGO_REGIMENTO_VALOR
						
						//Caso os id's nï¿½o sejam iguais(no if acima), verifico se o "cï¿½digo do regimento" do item novo ï¿½ diferente do "cï¿½digo de regimento de referï¿½ncia" do cï¿½lculo deste mesmo item
						if( !guiaItemDtNovo.getCustaDt().getCodigoRegimento().equals(guiaItemDtNovo.getCustaDt().getCodigoRegimentoValor()) ) {
							
							//Caso seja diferente (no if anterior), verifico se o "cï¿½digo do regimento de referï¿½ncia" do item novo ï¿½ igual ao "cï¿½digo regimento" do item antigo
							if( guiaItemDtNovo.getCustaDt().getCodigoRegimentoValor().equals(guiaItemDtAntigo.getCustaDt().getCodigoRegimento()) ) {
								
								calcularDiferenca = true;
								
							}
						}
						
						if( guiaItemDtNovo.getCustaDt().getId_ArrecadacaoCusta() != null 
								&& 
							guiaItemDtAntigo.getCustaDt().getId_ArrecadacaoCusta() != null ) {
							
							if( guiaItemDtNovo.getCustaDt().getId_ArrecadacaoCusta().equals(guiaItemDtAntigo.getCustaDt().getId_ArrecadacaoCusta()) ) {
								
								calcularDiferenca = true;
								
							}
							
						}
						
						//Item da guia genérica
						if( guiaItemDtAntigo.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTA_GENERICA))
							&&
							guiaItemDtNovo.getCustaDt().getId_ArrecadacaoCusta().equals(guiaItemDtAntigo.getId_ArrecadacaoCustaGenerica()) ) {
							
							calcularDiferenca = true;
							
						}
						
					}
					
					//Calcular a diferenï¿½a?
					if( calcularDiferenca ) {
						
						BigDecimal valorNovo 	= new BigDecimal(Funcoes.StringToDouble(guiaItemDtNovo.getValorCalculado()));
						BigDecimal valorAntigo 	= new BigDecimal(Funcoes.StringToDouble(guiaItemDtAntigo.getValorCalculado()));
						
						valorNovo = valorNovo.setScale(2, BigDecimal.ROUND_UP);
						valorAntigo = valorAntigo.setScale(2, BigDecimal.ROUND_UP);
						
						//se, jï¿½ foi retirado, ou seja, jï¿½ foi abatido do valor, entï¿½o ï¿½ zerado para nï¿½o ser abatido novamente
						if( valorAntigo.compareTo(new BigDecimal("0.0")) < 0 ) {
							valorNovo = new BigDecimal(0.0);
							valorAntigo = new BigDecimal(0.0);
							seraRetirado = true;
						}
						
						guiaItemDtNovo.setValorCalculado( valorNovo.subtract(valorAntigo).toString() );
					
						calcularDiferenca = false;
					}
					
					if( guiaItemDtAntigo.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL)) 
						|| guiaItemDtAntigo.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL))
						|| guiaItemDtAntigo.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_AVALIACAO))
						|| guiaItemDtAntigo.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_PENHORA))
						|| guiaItemDtAntigo.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTAS_LOCOMOCAO))
						) {
						seraRetirado = false;
					}
					
				}
				
				//retira item alterando seu valor para negativo, ou seja, devoluï¿½ï¿½o do item pago
				if( seraRetirado ) {
					double valorDevolucao = Funcoes.StringToDouble(guiaItemDtAntigo.getValorCalculado()) * (-1);
					guiaItemDtAntigo.setValorCalculado(String.valueOf(valorDevolucao));
					
					listaGuiaItemDt.add(guiaItemDtAntigo);
				}
				else {
					if( guiaItemDtAntigo.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL)) 
						|| guiaItemDtAntigo.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL))
						|| guiaItemDtAntigo.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_AVALIACAO))
						|| guiaItemDtAntigo.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_PENHORA))
						|| guiaItemDtAntigo.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTAS_LOCOMOCAO))
						) {
						
						if( Funcoes.StringToDouble(guiaItemDtAntigo.getValorCalculado()) > 0 )
							listaGuiaItemDtLocomocao.add(guiaItemDtAntigo);
					}
				}
			}
			
		}
	}
	
	/**
	 * Recalcular os itens da guia complementar.
	 * 
	 * @param List listaGuiaItemDtAtual //itens calculados agora
	 * @param List listaGuiaItemDtPagas //itens pagos
	 * @return List listaGuiaItemDt
	 * @throws Exception
	 * @author Fred
	 */
	public List recalcularGuiaComplementar(List listaGuiaItemDtAtual, List listaGuiaItemDtPagas) throws Exception {

		if( listaGuiaItemDtAtual != null && listaGuiaItemDtAtual.size() > 0 &&
			listaGuiaItemDtPagas != null && listaGuiaItemDtPagas.size() > 0 ) {
			
			for( int i = 0; i < listaGuiaItemDtAtual.size(); i++ ) {
				GuiaItemDt guiaItemDtAtual = (GuiaItemDt) listaGuiaItemDtAtual.get(i);
				
				BigDecimal somaItem = new BigDecimal(0.0D);
				for( int m = 0; m < listaGuiaItemDtPagas.size(); m++ ) {
					GuiaItemDt guiaItemDtPago = (GuiaItemDt) listaGuiaItemDtPagas.get(m);
					
					if( guiaItemDtAtual.getCustaDt().getId().equals( guiaItemDtPago.getCustaDt().getId() ) ) {
						somaItem = somaItem.add(new BigDecimal( guiaItemDtPago.getValorCalculado() ));
						break;
					}
				}
				
				BigDecimal valorNovoCalculado = new BigDecimal(guiaItemDtAtual.getValorCalculado());
				valorNovoCalculado = valorNovoCalculado.setScale(2, BigDecimal.ROUND_UP);
				
				BigDecimal valorAntigoPago = somaItem;
				valorAntigoPago = valorAntigoPago.setScale(2, BigDecimal.ROUND_UP);
				
				guiaItemDtAtual.setValorCalculado( String.valueOf( valorNovoCalculado.subtract(valorAntigoPago) ) );
			}
			
			//Retira itens com valor zerado
			this.retirarItensValorCalculadoZerado(listaGuiaItemDtAtual);
		}
		
		return listaGuiaItemDtAtual;
	}
	
	/**
	 * Mï¿½todo para calcular o total da guia.
	 * 
	 * @param List listaGuiaItemDt
	 * @return Double
	 * @throws Exception
	 */
	public Double calcularTotalGuia(List listaGuiaItemDt) throws Exception {
		Double retorno = 0.0D;
		
		for(int i = 0; i < listaGuiaItemDt.size(); i++) {
			GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDt.get(i);
			
			retorno = retorno + Funcoes.StringToDouble( Funcoes.FormatarDecimal(guiaItemDt.getValorCalculado()).replace(".", "").replace(",", ".") );
		}
		
		retorno = Funcoes.retirarCasasDecimais(retorno);
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo que recebe os itens do cï¿½lculo e  retorna a lista calculada dos itens com o subtotal e total da guia.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaCustaModeloDt
	 * @param Map valoresReferenciaCalculo
	 * @return List listaGuiaItemDt
	 * @throws Exception
	 */
	protected List calcularItensGuia(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaCustaModeloDt, Map valoresReferenciaCalculo) throws Exception {
		
		//Inicia a propriedade lista da classe que irï¿½ conter os itens da guia.
		listaGuiaItemDt = new ArrayList();
		totalGuia = 0.0D;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			//Map com a lista de CustaDt do mesmo Codigo Referencia
			Map<String, List<CustaDt>> map = new HashMap<String, List<CustaDt>>();
			
			//Lista de chaves
			Set<String> listaChaves = new HashSet<String>();
			
			//Separa no Map os itens em comuns levando em conta a Referï¿½ncia de Cï¿½lculo
			if( listaGuiaCustaModeloDt != null ) {
				for( int i = 0; i < listaGuiaCustaModeloDt.size(); i++ ) {
					GuiaCustaModeloDt guiaCustaModeloDt = (GuiaCustaModeloDt)listaGuiaCustaModeloDt.get(i);
					CustaDt custaDt = guiaCustaModeloDt.getCustaDt();
					
					List<CustaDt> listaCustaDtMesmoCodigoReferencia = null;
					
					if( custaDt.getReferenciaCalculo() != null && map.containsKey(custaDt.getReferenciaCalculo()) )
						listaCustaDtMesmoCodigoReferencia = (List<CustaDt>)map.get(custaDt.getReferenciaCalculo());
					else 
						listaCustaDtMesmoCodigoReferencia = new ArrayList<CustaDt>();
					
					listaChaves.add(custaDt.getReferenciaCalculo());
					listaCustaDtMesmoCodigoReferencia.add(custaDt);
					map.put(custaDt.getReferenciaCalculo(), listaCustaDtMesmoCodigoReferencia);
				}
			}
			
			Iterator<String> it = listaChaves.iterator();
			
			//Obtï¿½m cada lista do Map para fazer as consultas dos valores consultados
			while(it.hasNext()) {
				
				//Obtï¿½m a chave e a lista desta chave
				String chave = (String)it.next();
				List<CustaDt> listaCustaDt = (List<CustaDt>)map.get(chave);
				
				switch(Funcoes.StringToInt(chave)) {
				
					case CustaDt.INDIFERENTE :
					case CustaDt.VALOR_CAUSA : {
						this.calcularValorIntevaloCusta(listaCustaDt, valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA).toString());
						break;
					}
					
					case CustaDt.VALOR_BENS : {
						this.calcularValorBens(listaCustaDt, valoresReferenciaCalculo.get(CustaDt.VALOR_BENS).toString());
						break;
					}
					
					case CustaDt.MANDADOS : {
						//vetor[0] = nï¿½mero de impetrantes
						//vetor[1] = cï¿½digo do processo tipo
						String vetor[] = valoresReferenciaCalculo.get(CustaDt.MANDADOS).toString().split(";");
						
						//Compara o tipo de processo
						if( vetor[1].equals(String.valueOf(ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO)) )
							this.calcularValorMandadoSegurancaColetivo(listaCustaDt);
						else
							this.calcularValorMandadoSeguranca(listaCustaDt, vetor[0]);
						
						break;
					}
					
					case CustaDt.LOCOMOCAO : {
						
						int valorLocomocao = 0; //valor default
												
						if( guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().length() > 0 ) {
							ProcessoDt processoDt = new ProcessoNe().consultarProcessoSimplificado(guiaEmissaoDt.getId_Processo());
							
							if( Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.VARA ) {
								if( processoDt.isCivel() ) {
									valorLocomocao = LOCOMOCAO_CIVEL;									
								}
								else {
									if( processoDt.isCriminal() ) {
										valorLocomocao = LOCOMOCAO_CRIMINAL;										
									}
								}
							}
							else {
								if( Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU ) {
									valorLocomocao = LOCOMOCAO_SEGUNDO_GRAU;
								}
							}
						}
						
						String quantidadeAcrescimo = null;
						if( valoresReferenciaCalculo.get(CustaDt.QUANTIDADE_ACRESCIMO_PESSOA) != null ) {
							quantidadeAcrescimo = valoresReferenciaCalculo.get(CustaDt.QUANTIDADE_ACRESCIMO_PESSOA).toString();
						}
						
						//Guia recurso inominado queixa-crime ï¿½ sï¿½ oficial
						if( valoresReferenciaCalculo.get(CustaDt.GUIA_RECURSO_INOMINADO_QUEIXA_CRIMINE) != null && valoresReferenciaCalculo.get(CustaDt.GUIA_RECURSO_INOMINADO_QUEIXA_CRIMINE).toString().equals("SIM") ) {
							valorLocomocao = LOCOMOCAO_CIVEL;
						}
						
						this.calcularLocomocaoOficial(listaCustaDt, valorLocomocao, quantidadeAcrescimo, guiaEmissaoDt.isCitacaoHoraCerta());
						break;
					}
					
					case CustaDt.LOCOMOCAO_CONTA_VINCULADA : {
						
						int valorLocomocao = 0; //valor default
						
						this.calcularLocomocaoOficial(listaCustaDt, valorLocomocao, null, false);
						break;
					}

					case CustaDt.PARTIDOR_QUANTIDADE_VALOR : {
						this.calcularQuantidadeValor(listaCustaDt, valoresReferenciaCalculo.get(CustaDt.PARTIDOR_QUANTIDADE_VALOR).toString());
						break;
					}
					
					case CustaDt.LEILAO_QUANTIDADE_VALOR : {
						this.calcularQuantidadeValor(listaCustaDt, valoresReferenciaCalculo.get(CustaDt.LEILAO_QUANTIDADE_VALOR).toString());
						break;
					}
					
					case CustaDt.TAXA_JUDICIARIA : {
						int descontoTaxaJudiciaria = GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_0;
						if( valoresReferenciaCalculo.get(CustaDt.DESCONTO_TAXA_JUDICIARIA) != null && Funcoes.StringToInt(valoresReferenciaCalculo.get(CustaDt.DESCONTO_TAXA_JUDICIARIA).toString()) != GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_0 ) {
							descontoTaxaJudiciaria = Funcoes.StringToInt(valoresReferenciaCalculo.get(CustaDt.DESCONTO_TAXA_JUDICIARIA).toString());
						}
						this.calcularTaxaJudiciaria(listaCustaDt, valoresReferenciaCalculo.get(CustaDt.TAXA_JUDICIARIA).toString().trim(), descontoTaxaJudiciaria);
						break;
					}
					
				}
				
			}
			
			
			this.retirarCasasDecimais();
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Mï¿½todo que recebe os itens do cï¿½lculo e  retorna a lista calculada dos itens com o subtotal e total da guia.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaCustaModeloDt
	 * @param Map valoresReferenciaCalculo
	 * @return List listaGuiaItemDt
	 * @throws Exception
	 */
	protected List<GuiaItemDt> calcularItensGuia_Refatorado(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaCustaModeloDt, Map valoresReferenciaCalculo) throws Exception {
		
		//Inicia a propriedade lista da classe que irï¿½ conter os itens da guia.
		listaGuiaItemDt = new ArrayList<GuiaItemDt>();
		totalGuia = 0.0D;
				
		//Map com a lista de CustaDt do mesmo Codigo Referencia
		Map<String, List<CustaDt>> map = new HashMap<String, List<CustaDt>>();
		
		//Lista de chaves
		Set<String> listaChaves = new HashSet<String>();
					
		//Separa no Map os itens em comuns levando em conta o Id
		if( listaGuiaCustaModeloDt != null ) {
			for( int i = 0; i < listaGuiaCustaModeloDt.size(); i++ ) {
				GuiaCustaModeloDt guiaCustaModeloDt = (GuiaCustaModeloDt)listaGuiaCustaModeloDt.get(i);
				CustaDt custaDt = guiaCustaModeloDt.getCustaDt();
				
				List<CustaDt> listaCustaDtMesmoId = null;
				
				if( custaDt.getId() != null && map.containsKey(custaDt.getId()) )
					listaCustaDtMesmoId = (List<CustaDt>)map.get(custaDt.getId());
				else 
					listaCustaDtMesmoId = new ArrayList<CustaDt>();
				
				listaChaves.add(custaDt.getId());
				listaCustaDtMesmoId.add(custaDt);
				map.put(custaDt.getId(), listaCustaDtMesmoId);
			}
		}
		
		Iterator<String> it = listaChaves.iterator();
		
		//Obtem cada lista do Map para fazer as consultas dos valores consultados
		while(it.hasNext()) {
			
			//Obtem a chave e a lista desta chave
			String chave = (String)it.next();
			List<CustaDt> listaCustaDt = (List<CustaDt>)map.get(chave);
			
			switch(Funcoes.StringToInt(chave)) {
				case CustaDt.PELA_EMISSAO_DOS_DOCUMENTOS_DE_COMUNICACAO : {
					if( guiaEmissaoDt.getEmissaoDocumentoQuantidade16VII().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getEmissaoDocumentoQuantidade16VII()) > 0 ) {
						this.calcularValorIntevaloCusta_Refatorado(guiaEmissaoDt, listaCustaDt, valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA).toString());
					}
					break;
				}
			}
			
			switch(Funcoes.StringToInt(chave)) {
			
				case CustaDt.LOCOMOCAO_PARA_OFICIAL : 
				case CustaDt.LOCOMOCAO_PARA_OFICIAL_ADHOC :
				case CustaDt.CUSTAS_LOCOMOCAO : {
					String quantidadeAcrescimo = null;
					if( valoresReferenciaCalculo.get(CustaDt.QUANTIDADE_ACRESCIMO_PESSOA) != null ) {
						quantidadeAcrescimo = valoresReferenciaCalculo.get(CustaDt.QUANTIDADE_ACRESCIMO_PESSOA).toString();
					}
					this.calcularLocomocaoOficial(listaCustaDt, guiaEmissaoDt.getValorLocomocaoCodigo(), quantidadeAcrescimo, guiaEmissaoDt.isCitacaoHoraCerta());
					break;
				}
				
				case CustaDt.LOCOMOCAO_PARA_TRIBUNAL :
				case CustaDt.LOCOMOCAO_PARA_AVALIACAO :
				case CustaDt.LOCOMOCAO_PARA_PENHORA : {
					this.calcularLocomocaoOficial(listaCustaDt, GuiaCalculoNe.LOCOMOCAO_CIVEL, null, false);
					break;
				}
				
				case CustaDt.LOCOMOCAO_2_GRAU : {
					this.calcularLocomocaoOficial(listaCustaDt, GuiaCalculoNe.LOCOMOCAO_SEGUNDO_GRAU, null, false);
					break;
				}
				
				case CustaDt.MANDADO_DE_SEGURANCA_CUMPRIMENTO_DE_PRECATORIAS : {
					//Ocorrência 2020/3602 - Trocado chamada de método para reconhecer o menu de % para item do escrivão do cível.
					//Texto Ocorrência: A usuária Suelene Todescato Soares, contadora da Comarca de Itapuranga, alega que as custas finais nos autos de processo nº 5288827.98.2018 - carta precatória, o sistema está cobrando as custas do Escrivão de R$349,42 novamente, de forma indevida; haja vista que as custas iniciais foram pagas através da GRJ 19780381-4-09.

					//this.calcularValorMandadoSegurancaColetivo(listaCustaDt);
					this.calcularItemAtoEscrivao(listaCustaDt, (String)valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA), (String)valoresReferenciaCalculo.get(CustaDt.PORCENTAGEM_ESCRIVAO_CIVEL), guiaEmissaoDt.getAtosEscrivaesCivel());
					break;
				}
				
				case CustaDt.AUTO_DE_AVALIACAO_DE_BENS_EM_PROCESSO_DE_QUALQUER_NATUREZA : {
					this.calcularItemQuantidadeValor(listaCustaDt, guiaEmissaoDt.getAvaliadorQuantidade() + ";" + guiaEmissaoDt.getAvaliadorValor());
					break;
				}
				
				case CustaDt.PARTILHA_OU_SOBREPARTILHA : {
					if( guiaEmissaoDt.getPartidorQuantidade() != null && Funcoes.StringToInt(guiaEmissaoDt.getPartidorQuantidade()) > 0 ) {
						this.calcularItemQuantidadeValor(listaCustaDt, guiaEmissaoDt.getPartidorQuantidade() + ";" + valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA).toString());
						break;
					}
					break;
				}
				
				case CustaDt.AUTUACAO_E_OU_PROCESSAMENTO_DE_FEITOS : {
					if( guiaEmissaoDt.getAtoEscrivao() != null && Funcoes.StringToInt(guiaEmissaoDt.getAtoEscrivao()) > 0 ) {
						this.calcularItemAtoEscrivao(listaCustaDt, (String)valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA), (String)valoresReferenciaCalculo.get(CustaDt.PROCESSO_PROCEDIMENTO_ORDINARIO), null);
					}
					break;
				}
				
				case CustaDt.REGISTRO_DE_PETICAO_INICIAL : {
					if( guiaEmissaoDt.getTaxaProtocoloQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getTaxaProtocoloQuantidade()) > 0 ) {
						this.calcularValorIntevaloCusta_Refatorado(guiaEmissaoDt, listaCustaDt, valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA).toString());
						break;
					}
					if( guiaEmissaoDt.getPregaoPorteiro() != null && Funcoes.StringToInt(guiaEmissaoDt.getPregaoPorteiro()) > 0 ) {
						this.calcularItemQuantidadeValor(listaCustaDt, guiaEmissaoDt.getPregaoPorteiro() + ";" + guiaEmissaoDt.getAvaliadorValor());
						break;
					}
					if( guiaEmissaoDt.getLeilaoQuantidade() != null && Funcoes.StringToInt(guiaEmissaoDt.getLeilaoQuantidade()) > 0 ) {
						this.calcularItemQuantidadeValor(listaCustaDt, guiaEmissaoDt.getLeilaoQuantidade() + ";" + guiaEmissaoDt.getLeilaoValor());
						break;
					}
					
					break;
				}
				
				case CustaDt.DEPOSITO_COMPREENDENDO_OS_REGISTROS_GUARDA_ESCRITURACAO : {
					this.calcularValorIntevaloCusta_Refatorado(guiaEmissaoDt, listaCustaDt, valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA).toString());
					break;
				}
				
				case CustaDt.PORTE_REMESSA : {
					
					if( guiaEmissaoDt.getPorteRemessaValorManual() != null && guiaEmissaoDt.getPorteRemessaValorManual().length() > 0 && valoresReferenciaCalculo.get(CustaDt.PORTE_REMESSA_MANUAL) != null ) {
						this.calcularItemValorInformado(listaCustaDt, (String)valoresReferenciaCalculo.get(CustaDt.PORTE_REMESSA_MANUAL));
					}
					
					break;
				}
				
				case CustaDt.PROTOCOLO_INTEGRADO_CONTADOR_SEGUNDO_GRAU : {
					if( guiaEmissaoDt.getProtocoloIntegrado() != null && guiaEmissaoDt.getProtocoloIntegrado().length() > 0 ) {
						this.calcularItemProtocoloIntegrado(listaCustaDt, guiaEmissaoDt.getProtocoloIntegrado(), (String)valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA));
					}
					break;
				}
				
				case CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO : {
					if( guiaEmissaoDt.getAtosEscrivaesCivel().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getAtosEscrivaesCivel()) > 0 ) {
						this.calcularItemAtoEscrivao(listaCustaDt, (String)valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA), (String)valoresReferenciaCalculo.get(CustaDt.PORCENTAGEM_ESCRIVAO_CIVEL), guiaEmissaoDt.getAtosEscrivaesCivel());
						break;
					}
					else {
						this.calcularItemFormalPartilhaCarta(listaCustaDt, guiaEmissaoDt, (String)valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA));
						break;
					}
				}
				
				case CustaDt.PROCESSOS_ESPECIAIS_DE_JURISDICAO_CONTENCIOSA : {
					if( guiaEmissaoDt.getAtosEscrivaesCivel().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getAtosEscrivaesCivel()) > 0 ) {
						this.calcularItemAtoEscrivao(listaCustaDt, (String)valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA), (String)valoresReferenciaCalculo.get(CustaDt.PORCENTAGEM_ESCRIVAO_CIVEL), guiaEmissaoDt.getAtosEscrivaesCivel());
						break;
					}
					break;
				}
				
				case CustaDt.PROCESSOS_CAUTELARES_SERAO_COBRADOS_40_DAS_CUSTAS : {
					if( guiaEmissaoDt.getAtosEscrivaesCivel().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getAtosEscrivaesCivel()) > 0 ) {
						this.calcularItemAtoEscrivao(listaCustaDt, (String)valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA), (String)valoresReferenciaCalculo.get(CustaDt.PORCENTAGEM_ESCRIVAO_CIVEL), guiaEmissaoDt.getAtosEscrivaesCivel());
						break;
					}
					break;
				}
				
				case CustaDt.HONORARIOS_PROCURADOR : {
					this.calcularItemHonorarios(listaCustaDt, guiaEmissaoDt.getHonorariosQuantidade() + ";" + guiaEmissaoDt.getHonorariosValor());
					break;
				}
				
				case CustaDt.TAXA_JUDICIARIA_PROCESSO : {
					int descontoTaxaJudiciaria = GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_0;
					if( valoresReferenciaCalculo.get(CustaDt.DESCONTO_TAXA_JUDICIARIA) != null && Funcoes.StringToInt(valoresReferenciaCalculo.get(CustaDt.DESCONTO_TAXA_JUDICIARIA).toString()) != GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_0 ) {
						descontoTaxaJudiciaria = Funcoes.StringToInt(valoresReferenciaCalculo.get(CustaDt.DESCONTO_TAXA_JUDICIARIA).toString());
					}
					this.calcularTaxaJudiciaria(listaCustaDt, valoresReferenciaCalculo.get(CustaDt.TAXA_JUDICIARIA).toString().trim(), descontoTaxaJudiciaria);
					break;
				}
				
				default : {
					this.calcularValorIntevaloCusta_Refatorado(guiaEmissaoDt, listaCustaDt, valoresReferenciaCalculo.get(CustaDt.VALOR_CAUSA).toString());
					break;
				}
			}
			
		}
		
		this.retirarCasasDecimais();
	
		return listaGuiaItemDt;
	}
	
	/**
	 * Mï¿½todo que recebe os itens do cï¿½lculo e  retorna a lista calculada dos itens com o subtotal e total da guia.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaCustaModeloDt
	 * @param Map valoresReferenciaCalculo
	 * @return List listaGuiaItemDt
	 * @throws Exception
	 */
	protected List calcularItensGuiaGenerica(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaCustaModeloDt, Map valoresReferenciaCalculo) throws Exception {
		
		//Inicia a propriedade lista da classe que irï¿½ conter os itens da guia.
		listaGuiaItemDt = new ArrayList();
		totalGuia = 0.0D;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			//Map com a lista de CustaDt do mesmo Codigo Referencia
			Map<String, List<CustaDt>> map = new HashMap<String, List<CustaDt>>();
			
			//Lista de chaves
			Set<String> listaChaves = new HashSet<String>();
			
			//Separa no Map os itens em comuns levando em conta o Id
			if( listaGuiaCustaModeloDt != null ) {
				for( int i = 0; i < listaGuiaCustaModeloDt.size(); i++ ) {
					GuiaCustaModeloDt guiaCustaModeloDt = (GuiaCustaModeloDt)listaGuiaCustaModeloDt.get(i);
					CustaDt custaDt = guiaCustaModeloDt.getCustaDt();
					
					List<CustaDt> listaCustaDtMesmoId = null;
					
					if( custaDt.getId() != null && map.containsKey(custaDt.getId()) )
						listaCustaDtMesmoId = (List<CustaDt>)map.get(custaDt.getId());
					else 
						listaCustaDtMesmoId = new ArrayList<CustaDt>();
					
					listaChaves.add(custaDt.getId());
					listaCustaDtMesmoId.add(custaDt);
					map.put(custaDt.getId(), listaCustaDtMesmoId);
				}
			}
			
			Iterator<String> it = listaChaves.iterator();
			
			//Obtï¿½m cada lista do Map para fazer as consultas dos valores consultados
			while(it.hasNext()) {
				
				//Obtï¿½m a chave e a lista desta chave
				String chave = (String)it.next();
				List<CustaDt> listaCustaDt = (List<CustaDt>)map.get(chave);
				
				switch(Funcoes.StringToInt(chave)) {
				
					case CustaDt.LOCOMOCAO_PARA_OFICIAL : 
					case CustaDt.LOCOMOCAO_PARA_OFICIAL_ADHOC : {
						
						int valorLocomocao = 0; //valor default
						
						if( guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().length() > 0 ) {
							ProcessoDt processoDt = new ProcessoNe().consultarProcessoSimplificado(guiaEmissaoDt.getId_Processo());
							
							if( Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.VARA ) {
								if( processoDt.isCivel() ) {
									valorLocomocao = LOCOMOCAO_CIVEL;									
								}
								else {
									if( processoDt.isCriminal() ) {
										valorLocomocao = LOCOMOCAO_CRIMINAL;										
									}
								}
							}
							else {
								if( Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU ) {
									valorLocomocao = LOCOMOCAO_SEGUNDO_GRAU;
								}
							}
						}
						
						String quantidadeAcrescimo = null;
						if( valoresReferenciaCalculo.get(CustaDt.QUANTIDADE_ACRESCIMO_PESSOA) != null ) {
							quantidadeAcrescimo = valoresReferenciaCalculo.get(CustaDt.QUANTIDADE_ACRESCIMO_PESSOA).toString();
						}
						
						//Na guia de recurso inominado ï¿½ cobrado os valores de locomoï¿½ï¿½o como sendo de criminal.
						if( valoresReferenciaCalculo.get(GuiaRecursoInominadoNe.VARIAVEL_GUIA_RECURSO_INOMINADO) != null && valoresReferenciaCalculo.get(GuiaRecursoInominadoNe.VARIAVEL_GUIA_RECURSO_INOMINADO).toString().equals(GuiaRecursoInominadoNe.VALOR_GUIA_RECURSO_INOMINADO) ) {
							valorLocomocao = LOCOMOCAO_CRIMINAL;
						}
						
						//Guia recurso inominado queixa-crime ï¿½ sï¿½ oficial
						if( valoresReferenciaCalculo.get(CustaDt.GUIA_RECURSO_INOMINADO_QUEIXA_CRIMINE) != null && valoresReferenciaCalculo.get(CustaDt.GUIA_RECURSO_INOMINADO_QUEIXA_CRIMINE).toString().equals("SIM") ) {
							valorLocomocao = LOCOMOCAO_CIVEL;
						}
						
						//Guia Execuï¿½ï¿½o Queixa-Crime
						if( valoresReferenciaCalculo.get(CustaDt.GUIA_EXECUCAO_QUEIXA_CRIMINE) != null && valoresReferenciaCalculo.get(CustaDt.GUIA_EXECUCAO_QUEIXA_CRIMINE).toString().equals("SIM") ) {
							valorLocomocao = LOCOMOCAO_CIVEL;
						}
						
						//*****************************************************
						//*****************************************************
						//Alteraï¿½ï¿½o para a ocorrï¿½ncia Ocorrï¿½ncia 2013/76036 e redmine #1759
						//Este trecho reforï¿½a para o PONTO (A) e (B) acima.
						//*****************************************************
						if( guiaEmissaoDt.isIdGuiaTipo( GuiaTipoDt.ID_RECURSO_INOMINADO_QUEIXA_CRIME)			||					guiaEmissaoDt.isIdGuiaTipo( GuiaTipoDt.ID_FINAL_EXECUCAO_QUEIXA_CRIME ) ){
							valorLocomocao = LOCOMOCAO_CIVEL;
						}
						
						if( guiaEmissaoDt.isIdGuiaTipo( GuiaTipoDt.ID_LOCOMOCAO) 							||							guiaEmissaoDt.isIdGuiaTipo( GuiaTipoDt.ID_LOCOMOCAO_COMPLEMENTAR ) ){
							
							if( valoresReferenciaCalculo.get(CustaDt.IS_LOCOMOCAO_CIVEL) != null && valoresReferenciaCalculo.get(CustaDt.IS_LOCOMOCAO_CIVEL).equals("SIM") ) {
								valorLocomocao = LOCOMOCAO_CIVEL;
							}
							else {
								valorLocomocao = LOCOMOCAO_CRIMINAL;
							}
						}
						//*****************************************************
						//*****************************************************
						
						this.calcularLocomocaoOficial(listaCustaDt, valorLocomocao, quantidadeAcrescimo, guiaEmissaoDt.isCitacaoHoraCerta());
						break;
					}
					
					case CustaDt.LOCOMOCAO_PARA_TRIBUNAL : {
						
						int valorLocomocao = 0; //valor default
						this.calcularLocomocaoOficial(listaCustaDt, valorLocomocao, null, false);
						break;
					}
					
					default : {
						this.calcularValorIntevaloCustaGenerica(guiaEmissaoDt, listaCustaDt, valoresReferenciaCalculo);
						break;
					}
				}
				
			}
			
			this.retirarCasasDecimais();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Mï¿½todo para calcular os itens da guia com rateio.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaItemDt
	 * @param Double porcentagem
	 * 
	 * @return List listaGuiaItemDt
	 * @throws Exception
	 */
	public List calcularItensGuiaRateio(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaItemDt, Double porcentagem) throws Exception {
		List listaAuxGuiaItemDt = null;
		

		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			listaAuxGuiaItemDt = new ArrayList();
			
			for( int i = 0; i < listaGuiaItemDt.size(); i++ ) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(i);
				
				Double novoValor = Funcoes.StringToDouble(guiaItemDt.getValorCalculado()) * (porcentagem / 100.0D);
				
				GuiaItemDt aux = new GuiaItemDt();
				aux.setId( guiaItemDt.getId() );
				aux.setCustaDt( guiaItemDt.getCustaDt() );
				aux.setQuantidade( guiaItemDt.getQuantidade() );
				aux.setValorReferencia( guiaItemDt.getValorReferencia() );
				aux.setValorCalculado( novoValor.toString() );
				
				listaAuxGuiaItemDt.add( aux );
			}
		}
	
		
		return listaAuxGuiaItemDt;
	}
	
	/**
	 * Mï¿½todo para calcular os parcelamentos do honorï¿½rio.
	 * 
	 * @param List listaGuiaItemDt
	 * @return List
	 * @throws Exception
	 */
	public List calcularParcelasGuia(List listaGuiaItemDt, int quantidadeParcelas, boolean calcularTodosItens, int parcelaCorrente) throws Exception {
		List listaAuxGuiaItemDt = null;
		

		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			listaAuxGuiaItemDt = new ArrayList();
			
			for( int i = 0; i < listaGuiaItemDt.size(); i++ ) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(i);
				
				if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.HONORARIOS_PROCURADOR)) ) {
					
					Double valorParcelado = Funcoes.StringToDouble(guiaItemDt.getValorCalculado()) / quantidadeParcelas;
					
					GuiaItemDt aux = new GuiaItemDt();
					CustaDt    custaAux = new CustaDt();
					custaAux.copiar(guiaItemDt.getCustaDt());
					aux.setId( guiaItemDt.getId() );
					//aux.setCustaDt( guiaItemDt.getCustaDt() );
					aux.setCustaDt(custaAux);
					aux.setQuantidade( guiaItemDt.getQuantidade() );
					aux.setValorReferencia( guiaItemDt.getValorReferencia() );
					aux.setValorCalculado(valorParcelado.toString());
					aux.setParcelas(String.valueOf(quantidadeParcelas));
					aux.setParcelaCorrente(String.valueOf(parcelaCorrente));
					aux.getCustaDt().setArrecadacaoCusta(new String("HON. PROCURADORES MUNICIPAIS (" + parcelaCorrente + "/" + quantidadeParcelas + ")"));
					
					listaAuxGuiaItemDt.add( aux );
				}
				else {
					if( calcularTodosItens ) {
						GuiaItemDt aux = new GuiaItemDt();
						aux.setId( guiaItemDt.getId() );
						aux.setCustaDt( guiaItemDt.getCustaDt() );
						aux.setQuantidade( guiaItemDt.getQuantidade() );
						aux.setValorReferencia( guiaItemDt.getValorReferencia() );
						aux.setValorCalculado( guiaItemDt.getValorCalculado() );
						
						listaAuxGuiaItemDt.add( aux );
					}
				}
			}
		}
	
		
		return listaAuxGuiaItemDt;
	}
	
	/**
	 * Mï¿½todo para calcular a reduï¿½ï¿½o de 50% no valor total da guia a pedido do juiz.
	 * @param List listaGuiaItemDt
	 * 
	 * @throws Exception
	 */
	public void calcularReducao50PorcentoGuia(List listaGuiaItemDt) throws Exception {

		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			for(int i = 0; i < listaGuiaItemDt.size(); i++ ) {
				GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDt.get(i);
				
				Double valorCalculado = Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
				valorCalculado = valorCalculado / 2;
					
				guiaItemDt.setValorCalculado(String.valueOf(valorCalculado));
			}
			
			this.recalcularTotalGuia(listaGuiaItemDt);
		}
	
	}
	
	/**
	 * Mï¿½todo para calcular a reduï¿½ï¿½o de 50% para os itens que o juiz determinar para o Artigo 18 e Lei 12832/96.
	 * @param List listaGuiaItemDt
	 * @param String reducao50Porcento
	 */
	public void calcularReducaoLei50Porcento(List listaGuiaItemDt, String reducao50Porcento) throws Exception {

		if( listaGuiaItemDt != null && reducao50Porcento != null ) {
			if( reducao50Porcento.equals(LEI_REDUCAO_50_PORCENTO) ) {
				for(int i = 0; i < listaGuiaItemDt.size(); i++ ) {
					GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDt.get(i);
					
					if( guiaItemDt.getCustaDt().getCodigoRegimento().equals(CustaDt.CALCULO) ||
						guiaItemDt.getCustaDt().getCodigoRegimento().equals(CustaDt.PROTOCOLO) || 
						guiaItemDt.getCustaDt().getCodigoRegimento().equals(CustaDt.DISTRIBUIDOR) ) {
						
						Double valorCalculado = Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
						valorCalculado = valorCalculado / 2;
						
						guiaItemDt.setValorCalculado(String.valueOf(valorCalculado));
					}
				}
				
				this.recalcularTotalGuia(listaGuiaItemDt);
			}
		}	
	}
	
	/**
	 * Mï¿½todo para calcular a citaï¿½ï¿½o por hora certa. ï¿½ acrescentado um valor.
	 * @thro
	 * ws Exception
	 */
	public void calcularCitacaoHoraCerta(List<GuiaItemDt> listaGuiaItemDt) throws Exception {

		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			this.aplicarRegraCitacaoHoraCertaLocomocoesIntimacaoCitacaoNotificacao(listaGuiaItemDt);				
			this.recalcularTotalGuia(listaGuiaItemDt);
		}
	
	}
	
	public void aplicarRegraCitacaoHoraCertaLocomocoesIntimacaoCitacaoNotificacao(List<GuiaItemDt> listaGuiaItemLocomocaoDt) {
		List<GuiaItemDt> listaGuiaItemLocomocaoCitacaoHoraCertaDt = new ArrayList<GuiaItemDt>();
		for (GuiaItemDt guiaItemDt : listaGuiaItemLocomocaoDt) {
			if (guiaItemDt.getCustaDt() != null && (guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTAS_LOCOMOCAO)) || guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL)))) {
				//Para a citação da hora certa, cada intimação existente na guia deve ser acrescida pelo valor da Tabela XII, nï¿½ 60, 1ï¿½ Nota do Regimento de Custas do ANEXO II do REGIMENTO DE CUSTAS, EMOLUMENTOS E TAXA JUDICIï¿½RIA E DOS TRIBUTOS, no ano de 2015 = 5,17.  
				
				//******************************
				//ATENCAO: Alteração sensível, não apagar este comentário
				//******************************
				//Ocorrencia 2020/11145: 
//				Texto ocorrencia: Foi aberto B.O. 11040/20 para ajustar o sistema ao provimento 44/20 limitando o pagamento de 3 locomoções por mandado ao oficial de justiça.
//				Verificando sobre a guia de locomoção quando marcamos hora certa o sistema calcula 04 locomoções para o endereço escolhido.
//				analisando o artigo 252 do novo CPC ele orienta que a citação por hora certa poderá ser feita na 3a diligência e não na 4a diligencia, portanto, seriam necessárias somente 3 locomoções.
//				solicitamos verificar se possível alterar este item na guia de locomoções e/ou demais guias onde houver a opção de Citação por hora certa.
				
				//Era adicionado 3 locomoções para cadas existe, como se multiplicasse por 4.
				//Agora será adcionado 2 para cada existente, como se fosse multiplicado por 3.
				//Para a citação da hora certa, cada intimação existente na guia deve ser multiplicada por 4
				
				//clonarGuiaItemLocomocaoIntimacaoCitacao(listaGuiaItemLocomocaoCitacaoHoraCertaDt, guiaItemDt);
				clonarGuiaItemLocomocaoIntimacaoCitacao(listaGuiaItemLocomocaoCitacaoHoraCertaDt, guiaItemDt);
				clonarGuiaItemLocomocaoIntimacaoCitacao(listaGuiaItemLocomocaoCitacaoHoraCertaDt, guiaItemDt);
			}
		}
		if (listaGuiaItemLocomocaoCitacaoHoraCertaDt.size() > 0) listaGuiaItemLocomocaoDt.addAll(listaGuiaItemLocomocaoCitacaoHoraCertaDt);
	}
	
	public void dobrarLocomocoes(List<GuiaItemDt> listaGuiaItemLocomocaoDt) {
		List<GuiaItemDt> listaGuiaItemLocomocaoDobraDt = new ArrayList<GuiaItemDt>();
		boolean dobrouLocomocao = false;
		for (GuiaItemDt guiaItemDt : listaGuiaItemLocomocaoDt) {
			if (guiaItemDt.getCustaDt() != null && (guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTAS_LOCOMOCAO)) || guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL)))) {
				clonarGuiaItemLocomocaoIntimacaoCitacao(listaGuiaItemLocomocaoDobraDt, guiaItemDt);
				dobrouLocomocao = true;
			}
		}
		if (!dobrouLocomocao) {
			for (GuiaItemDt guiaItemDt : listaGuiaItemLocomocaoDt) {
				if (guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_PENHORA))) {
					clonarGuiaItemLocomocaoIntimacaoCitacao(listaGuiaItemLocomocaoDobraDt, guiaItemDt);
					dobrouLocomocao = true;
				}
			}	
		}
		if (!dobrouLocomocao) {
			for (GuiaItemDt guiaItemDt : listaGuiaItemLocomocaoDt) {
				if (guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_AVALIACAO))) {
					clonarGuiaItemLocomocaoIntimacaoCitacao(listaGuiaItemLocomocaoDobraDt, guiaItemDt);
					dobrouLocomocao = true;
				}
			}	
		}
		if (listaGuiaItemLocomocaoDobraDt.size() > 0) listaGuiaItemLocomocaoDt.addAll(listaGuiaItemLocomocaoDobraDt);
	}
	
	private void clonarGuiaItemLocomocaoIntimacaoCitacao(List<GuiaItemDt> listaGuiaItemLocomocaoClone, GuiaItemDt guiaItemBaseDt) {
		if (guiaItemBaseDt != null && guiaItemBaseDt.getLocomocaoDt() != null && guiaItemBaseDt.getLocomocaoDt().getGuiaItemDt() != null) {
			
			GuiaItemDt guiaItemClone = guiaItemBaseDt.clonar();
			guiaItemClone.setGuiaItemVinculadoDt(null);
			listaGuiaItemLocomocaoClone.add(guiaItemClone);
			
			LocomocaoDt locomocaoClone = guiaItemBaseDt.getLocomocaoDt().clonar();
			guiaItemClone.setLocomocaoDt(locomocaoClone);
			locomocaoClone.setGuiaItemDt(guiaItemClone);			
			
			locomocaoClone.setGuiaItemContaVinculadaDt(null);
			locomocaoClone.setGuiaItemSegundoDt(null);
			locomocaoClone.setGuiaItemTerceiroDt(null);
			
			// clonar conta vinculada
			if (guiaItemBaseDt.getLocomocaoDt().getGuiaItemContaVinculadaDt() != null) {
				GuiaItemDt guiaItemContaVinculadaClone = guiaItemBaseDt.getLocomocaoDt().getGuiaItemContaVinculadaDt().clonar();
				guiaItemClone.setGuiaItemVinculadoDt(guiaItemContaVinculadaClone);
				
				listaGuiaItemLocomocaoClone.add(guiaItemClone.getGuiaItemVinculadoDt());
				
				locomocaoClone.setGuiaItemContaVinculadaDt(guiaItemClone.getGuiaItemVinculadoDt());				
			}
			
		}
	}
	
	/**
	 * Mï¿½todo para calcular a taxa Judiï¿½ria. O Calculo aqui ï¿½ feito em Reais(R$) e nï¿½o UFR.
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @param int descontoTaxaJudiciaria
	 * 
	 * @throws Exception
	 */
	private void calcularTaxaJudiciaria(List listaCustaDt, String valorReferenciaCalculo, int descontoTaxaJudiciaria) throws Exception {

		if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
			
			valorReferenciaCalculo = valorReferenciaCalculo.replace(".", "");
			valorReferenciaCalculo = valorReferenciaCalculo.replace(",", ".");
			
			if( valorReferenciaCalculo == null ) {
				valorReferenciaCalculo = "0";
			}
			if( valorReferenciaCalculo != null && valorReferenciaCalculo.isEmpty() ) {
				valorReferenciaCalculo = "0";
			}
			
			BigDecimal valorReferencia = new BigDecimal(valorReferenciaCalculo).setScale(2, BigDecimal.ROUND_UP);
			
			BigDecimal valorMeioPorcento = VALOR_MEIO_PORCENTO;
			BigDecimal meioPorcento = MEIO_PORCENTO;
			BigDecimal valorUmPorcento = VALOR_UM_PORCENTO;
			BigDecimal umPorcento = new BigDecimal(UM_PORCENTO);
			BigDecimal valorUmPontoSententaECincoPorcento = VALOR_UM_PONTO_SETENTA_E_CINCO_PORCENTO;
			BigDecimal umPontoSententaECincoPorcento = UM_PONTO_SETENTA_E_CINCO_PORCENTO;
			
			for( int i = 0; i < listaCustaDt.size(); i++ ) {
				CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
				
				Double subTotal = 0.0D;
				
				//MEIO PORCENTO
				if( valorReferencia.compareTo(valorMeioPorcento) <= 0 ) {
					valorReferencia = valorReferencia.multiply(meioPorcento);
					subTotal = subTotal + valorReferencia.doubleValue();
				}
				else {
					if( valorReferencia.compareTo(valorMeioPorcento) > 0 ) {
						subTotal = subTotal + valorMeioPorcento.multiply(meioPorcento).doubleValue();
					}
				}
				
				//UM PORCENTO
				if( valorReferencia.compareTo(valorMeioPorcento) > 0 && valorReferencia.compareTo(valorUmPorcento) <= 0 ) {
					//Somente inverte a ordem para evitar dados negativos
					subTotal = subTotal + ( (valorReferencia.subtract(valorMeioPorcento)).multiply(umPorcento) ).doubleValue();
				}
				else {
					if( valorReferencia.compareTo(valorUmPorcento) >= 0 ) {
						subTotal = subTotal + ( (valorUmPorcento.subtract(valorMeioPorcento)).multiply(umPorcento) ).doubleValue();
					}
				}
				
				BigDecimal valorReferenciaParaVerificacaoTxMaxima = null;
				
				//ACIMA DE UM PORCENTO
				if( valorReferencia.compareTo(valorUmPontoSententaECincoPorcento) > 0 ) {
					//Somente inverte a ordem para evitar dados negativos
					if( valorReferencia.compareTo(valorUmPontoSententaECincoPorcento) > 0 ) {
						
						valorReferenciaParaVerificacaoTxMaxima = new BigDecimal(valorReferenciaCalculo);
						
						valorReferenciaParaVerificacaoTxMaxima = valorReferenciaParaVerificacaoTxMaxima.setScale(2, BigDecimal.ROUND_UP);
						valorReferenciaParaVerificacaoTxMaxima = valorReferenciaParaVerificacaoTxMaxima.subtract(VALOR_UM_PONTO_SETENTA_E_CINCO_PORCENTO);
						valorReferenciaParaVerificacaoTxMaxima = valorReferenciaParaVerificacaoTxMaxima.multiply(UM_PONTO_SETENTA_E_CINCO_PORCENTO);
						
						subTotal = subTotal + (valorReferencia.subtract(valorUmPontoSententaECincoPorcento)).multiply(umPontoSententaECincoPorcento).doubleValue();
					}
				}
				
				
				//
				String aux[] = subTotal.toString().split("\\.");
				
				String centavos = null;
				if( aux[1].length() > 2 ) {
					int tamanho = 4;
					if( aux[1].length() < 4 ) {
						tamanho = aux[1].length();
					}
					centavos = aux[1].substring(0, tamanho);
				}
				else {
					centavos = aux[1] + "0";
				}
				
				//subTotal = Funcoes.StringToDouble(aux[0] + "." + centavos) - 0.0001;
				subTotal = Funcoes.StringToDouble(aux[0] + "." + centavos);
				
				BigDecimal bd = new BigDecimal(subTotal);
				bd = bd.setScale(2, BigDecimal.ROUND_UP);
				subTotal = Funcoes.StringToDouble(bd.toString().replace(",", ""));
				
				
				//Limite mï¿½nimo
				if( custaDt.getMinimo() != null && custaDt.getMinimo().length() > 0 ) {
					Double limiteMinimo = Funcoes.StringToDouble(custaDt.getMinimo());
					if( subTotal < limiteMinimo ) {
						subTotal = limiteMinimo;
					}
				}
				//Limite mï¿½ximo
				if( custaDt.getValorMaximo() != null && custaDt.getValorMaximo().length() > 0 ) {
					Double limiteMaximo = Funcoes.StringToDouble(custaDt.getValorMaximo());
					if( subTotal > limiteMaximo ) {
						subTotal = limiteMaximo;
					}
					if( valorReferenciaParaVerificacaoTxMaxima != null && valorReferenciaParaVerificacaoTxMaxima.compareTo(VALOR_UM_PONTO_SETENTA_E_CINCO_PORCENTO) >= 0 ) {
						subTotal = limiteMaximo;
					}
				}
				
				
				//Desconto Taxa Judiciï¿½ria
				if( descontoTaxaJudiciaria != GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_0 ) {
					switch(descontoTaxaJudiciaria) {
						case GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_50 : {
							subTotal = subTotal / 2;
							break;
						}
						case GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_100 : {
							subTotal = 0.0D;
							break;
						}
					}
				}
				
				
				GuiaItemDt guiaItemDt = new GuiaItemDt();
				
				guiaItemDt.setValorCalculado(subTotal.toString());
				guiaItemDt.setQuantidade("1");
				guiaItemDt.setCustaDt(custaDt);
				
				//Total
				totalGuia = totalGuia + subTotal.doubleValue();
			
				//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
				listaGuiaItemDt.add(guiaItemDt);
				
			}
		}	
	}
	
	/**
	 * Mï¿½todo para adicionar o item de custa pelo o valor informado como parametro.
	 * 
	 * @param listaCustaDt
	 * @param valorReferenciaCalculo
	 * @throws Exception
	 */
	private void calcularItemValorInformado(List listaCustaDt, String valorReferenciaCalculo) throws Exception {
		
		if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
			
			valorReferenciaCalculo = valorReferenciaCalculo.replace(".", "");
			valorReferenciaCalculo = valorReferenciaCalculo.replace(",", ".");
			
			Double valorReferencia = Funcoes.StringToDouble(valorReferenciaCalculo);
			
			if( valorReferencia <= 0.0D ) {
				CustaValorNe custaValorNe = new CustaValorNe();
			
				List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDt, valorReferenciaCalculo);
				
				if( listaGuiaItemDtAux != null && !listaGuiaItemDtAux.isEmpty() ) {
					for( int i = 0; i < listaGuiaItemDtAux.size(); i++ ) {
						GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDtAux.get(i);
						
						//Lista de custas para comparar com os itens calculados
						for(int m = 0; m < listaCustaDt.size(); m++) {
							CustaDt custaDt = (CustaDt)listaCustaDt.get(m);
							
							//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
							// e coloca ele na lista que serï¿½ utilizada na guia.
							if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
								
								//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
								guiaItemDt.setCustaDt(custaDt);
								
								guiaItemDt.setValorCalculado(guiaItemDt.getValorReferencia());
								guiaItemDt.setQuantidade("1");
								guiaItemDt.setCustaDt( (CustaDt)listaCustaDt.get(m) );
								
								//Total
								totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
								
								//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
								listaGuiaItemDt.add(guiaItemDt);
							}
						}
					}
				}
			}
			else {
				for( int i = 0; i < listaCustaDt.size(); i++ ) {
					CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
					
					GuiaItemDt guiaItemDt = new GuiaItemDt();
					
					guiaItemDt.setValorReferencia(valorReferencia.toString());
					guiaItemDt.setValorCalculado(valorReferencia.toString());
					guiaItemDt.setQuantidade("1");
					guiaItemDt.setCustaDt(custaDt);
					
					//Total
					totalGuia = totalGuia + valorReferencia.doubleValue();
					
					listaGuiaItemDt.add(guiaItemDt);
				}
			}
		}
	}
	
	/**
	 * Mï¿½todo para calcular atualizaï¿½ï¿½o de valor da causa.
	 * @param String valorCausa
	 * @param Date dataAtualizacao
	 * @return Double
	 * @throws Exception
	 */
	public Double calcularAtualizacaoValorCausa(String valorCausa, Date dataAtualizacao) throws Exception {
		Double retorno = 0.0D;

		if( valorCausa != null && valorCausa.length() > 0 && dataAtualizacao != null ) {
			UfrValorNe ufrValorNe = new UfrValorNe();
			
			valorCausa = valorCausa.replace(".", "");
			valorCausa = valorCausa.replace(",", ".");
			
			//Converter valor da causa caso seja inferior a 1994
			valorCausa = this.converterValorDataAtual(dataAtualizacao, Double.valueOf(valorCausa)).toString();
			
			UfrValorDt UFRValorDataAtualizacaoDt = ufrValorNe.consultarData(dataAtualizacao);
			
			if( valorCausa != null && valorCausa.length() > 0 && UFRValorDataAtualizacaoDt != null && UFRValorDataAtualizacaoDt.obtenhaValorUFRTaxaJudiciaria() > 0) {
				
				UfrValorDt UFRValorDataAtualDt = ufrValorNe.consultarDataAtual(); 
				
				if (UFRValorDataAtualDt == null) throw new MensagemException("UFR Valor não configurado para a data atual.");
				
				retorno = Funcoes.retirarCasasDecimais((Funcoes.StringToDouble(valorCausa)/UFRValorDataAtualizacaoDt.obtenhaValorUFRTaxaJudiciaria()) * UFRValorDataAtualDt.obtenhaValorUFRTaxaJudiciaria());
				
			} else {
				retorno = Double.valueOf(valorCausa);
			}				
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para calcular o item de locomoï¿½ï¿½o para todos os bairros e quantidade informados.
	 * @param List listaCustaDt
	 * @param List valoresIdBairro
	 * @param int valorLocomocao
	 * @param int areaProcesso
	 * @param quantidadeAcrescimoPessoa
	 * 
	 * @throws Exception
	 */
	private void calcularLocomocaoOficial(List<CustaDt> listaCustaDt, int valorLocomocao, String quantidadeAcrescimoPessoa, boolean calculaCitacaoPorHoraCerta) throws Exception {

		if(listaCustaDt != null && listaCustaDt.size() > 0) {			
			boolean acrescimoPessoaCobrado = false;				
			for (CustaDt custaDt : listaCustaDt) {
				
				int quantidadeAcrescimoPessoaInt = 0;
				if( !acrescimoPessoaCobrado ) {		
					//Acrï¿½ssimo por pessoa no mesmo local
					quantidadeAcrescimoPessoaInt = Funcoes.StringToInt(quantidadeAcrescimoPessoa);
					acrescimoPessoaCobrado = true;
				}	
				
				GuiaItemDt guiaItemDt = calcularLocomocaoOficial(custaDt, valorLocomocao, quantidadeAcrescimoPessoaInt, true, calculaCitacaoPorHoraCerta);
				
				//Total
				totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
				
				//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
				listaGuiaItemDt.add(guiaItemDt);
			}
		}	
	}
	
	public GuiaItemDt calcularLocomocaoOficial(CustaDt custaDt, int valorLocomocao, int quantidadeAcrescimoPessoa, boolean geraLocomocao, boolean calculaCitacaoPorHoraCerta) throws Exception {
		if (custaDt.getBairroLocomocao() != null) {
			String quantidade = "1";
			
			//Consulta a zona-bairro
			ZonaBairroRegiaoDt zonaBairroDt = new ZonaBairroRegiaoNe().consultarIdBairro(custaDt.getBairroLocomocao().getId());
			
			if( zonaBairroDt == null ) {
				throw new MensagemException("ATENï¿½ï¿½O: Bairro nï¿½o zoneado. Favor, entrar em contato com o Suporte para adicionar o bairro na sua respectiva zona e regiï¿½o.");
			}
			
			BairroDt bairroDt = new BairroNe().consultarId(zonaBairroDt.getId_Bairro());
			
			if(bairroDt.getCodigoSPG() == null || bairroDt.getCodigoSPG().trim().length() == 0) {
				throw new MensagemException("ATENï¿½ï¿½O: Bairro nï¿½o possui o cï¿½digo SPG informado. Favor, entrar em contato com o Suporte para adicionar o cï¿½digo do bairro SPG no cadastro de bairro.");
			}
			
			ZonaDt zonaDt = new ZonaNe().consultarId(zonaBairroDt.getId_Zona());
			
			UfrValorNe ufrValorNe = new UfrValorNe();
			UfrValorDt ufrValorDt = ufrValorNe.consultarDataAtual();
			
			if (ufrValorDt == null) throw new MensagemException("UFR Valor nï¿½o configurado para a data atual.");
			
			//calcula o subtotal
			Double valorZona = null;
			if( 
				custaDt.getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL))
				||
				custaDt.getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL_SEGUNDO_GRAU))
				) {
				valorZona = ufrValorDt.obtenhaValorURFEmReais(Funcoes.StringToDouble(Funcoes.BancoDecimal(zonaDt.getValorContaVinculada())));
			} else {
				valorZona = ufrValorDt.obtenhaValorURFEmReais(Funcoes.StringToDouble(Funcoes.BancoDecimal(zonaDt.getValor(valorLocomocao))));
			}
			
			if( quantidadeAcrescimoPessoa > 0 ) {
				valorZona = valorZona + (quantidadeAcrescimoPessoa * GuiaCalculoNe.QUANTIDADE_ACRESCIMO_PESSOA);
			}
			
			if ( calculaCitacaoPorHoraCerta ) {
				valorZona += GuiaCalculoNe.CITACAO_HORA_CERTA;
			}
										
			GuiaItemDt guiaItemDt = new GuiaItemDt();
			guiaItemDt.setValorReferencia(valorZona.toString());
			guiaItemDt.setValorCalculado(valorZona.toString());
			guiaItemDt.setQuantidade(quantidade);					
			guiaItemDt.setCustaDt(custaDt);
			
			if(geraLocomocao && !custaDt.getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL))) {
				RegiaoDt regiaoDt = new RegiaoNe().consultarId(zonaBairroDt.getId_Regiao());
				
				//Cria a locomocaoDt para ser salva
				LocomocaoDt locomocaoDt = new LocomocaoDt();
				locomocaoDt.setZonaDt(zonaDt);
				locomocaoDt.setBairroDt(bairroDt);
				locomocaoDt.setRegiaoDt(regiaoDt);	
				locomocaoDt.setGuiaItemDt(guiaItemDt);
				if (custaDt.getOficialSPGDt() != null)
					locomocaoDt.setCodigoOficialSPG(custaDt.getOficialSPGDt().getCodigoOficial());
				
				guiaItemDt.setLocomocaoDt(locomocaoDt);	
			}
			
			return guiaItemDt;
		}
		
		return null;
	}
	
	/**
	 * Mï¿½todo para itens que tem quantidade e valor de referencia informado.
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @throws Exception
	 */
	private void calcularItemQuantidadeValor(List listaCustaDt, String valorReferenciaCalculo) throws Exception {

		CustaValorNe custaValorNe = new CustaValorNe();
		
		int quantidade = Funcoes.StringToInt(valorReferenciaCalculo.split(";")[0]);
		String valor = valorReferenciaCalculo.split(";")[1];
		
		//Consulta itens comuns
		List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDt, valor.toString());
		
		//Adiciona na propriedade lista da classe os itens calculados
		//Lista de itens que foram calculados
		for(int k = 0; k < listaGuiaItemDtAux.size(); k++) {
			GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(k);
			
			//Lista de custas para comparar com os itens calculados
			for(int i = 0; i < listaCustaDt.size(); i++) {
				CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
				
				//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
				// e coloca ele na lista que serï¿½ utilizada na guia.
				if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
					
					//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
					guiaItemDt.setCustaDt(custaDt);
					
					guiaItemDt.setValorCalculado(String.valueOf(Funcoes.StringToDouble(guiaItemDt.getValorReferencia()) * quantidade));
					guiaItemDt.setQuantidade(String.valueOf(quantidade));
					guiaItemDt.setCustaDt( (CustaDt)listaCustaDt.get(0) );
					
					//Valor mï¿½ximo que pode ser cobrado
					if( custaDt.getValorMaximo() != null && 
						custaDt.getValorMaximo().length() > 0 &&
						Double.valueOf(guiaItemDt.getValorCalculado()) > Double.valueOf(custaDt.getValorMaximo()) ) {
						
						guiaItemDt.setValorCalculado( custaDt.getValorMaximo() );
					}
					
					//Valor mï¿½nimo que pode ser cobrado
					if( custaDt.getMinimo() != null && 
						custaDt.getMinimo().length() > 0 &&
						Double.valueOf(custaDt.getMinimo()) > 0.0D && 
						Double.valueOf(guiaItemDt.getValorCalculado()) < Double.valueOf(custaDt.getMinimo()) ) {
						
						guiaItemDt.setValorCalculado( custaDt.getMinimo() );
					}
					
					//Total
					totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
					
					//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
					listaGuiaItemDt.add(guiaItemDt);
				}
			}
		}
	
	}
	
	/**
	 * Mï¿½todo para calcular a particularidade do item de regimento 37 formal de partilha.
	 * @param List listaCustaDt
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	private void calcularItemFormalPartilhaCarta(List listaCustaDt, GuiaEmissaoDt guiaEmissaoDt, String valorReferenciaCalculo) throws Exception {
		

		CustaValorNe custaValorNe = new CustaValorNe();
		//Consulta itens comuns
		List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDt, valorReferenciaCalculo);
		
		//Adiciona na propriedade lista da classe os itens calculados
		//Lista de itens que foram calculados
		if( listaGuiaItemDtAux != null && listaGuiaItemDtAux.size() > 0 ) {
			for(int k = 0; k < listaGuiaItemDtAux.size(); k++) {
				GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(k);
				
				//Lista de custas para comparar com os itens calculados
				for(int i = 0; i < listaCustaDt.size(); i++) {
					CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
					
					//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
					// e coloca ele na lista que serï¿½ utilizada na guia.
					if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
						
						//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
						guiaItemDt.setCustaDt(custaDt);
						
						//Calcula o subtotal do item: Quantidade x ValorReferencia do Item
						double valor = 0.0D;
						if( custaDt.getValorAcrescimo() != null && Funcoes.StringToDouble(custaDt.getValorAcrescimo()) > 0.0D )
							valor = Funcoes.StringToDouble(custaDt.getValorAcrescimo());
						valor = valor + Funcoes.StringToDouble(guiaItemDt.getValorReferencia());
						guiaItemDt.setValorCalculado( String.valueOf(valor) );
						guiaItemDt.setQuantidade("1");
						
						//Cï¿½lculo das porcentagens
						if( guiaEmissaoDt.getFormalPartilhaCartaQuantidade() != null && guiaEmissaoDt.getFormalPartilhaCartaQuantidade().length() > 0 ) {
							
							//Variï¿½veis separadas somente para ficar claro como ï¿½ o cï¿½lculo
							final Double PORCENTAGEM_60 = 60.0D;
							final Double PORCENTAGEM_40 = 40.0D;
							final Double PORCENTAGEM_20 = 20.0D;
							
							//Variavel para valor alterado
							boolean valor_alterado = false;
							
							switch( Funcoes.StringToInt( guiaEmissaoDt.getFormalPartilhaCartaQuantidade() ) ) {
								case 1 : {
									valor = valor + ( valor * ( PORCENTAGEM_60 /100.0D) );
									valor_alterado = true;
									
									break;
								}
								case 2 : {
									valor = valor + ( valor * ( PORCENTAGEM_40 /100.0D) );
									valor_alterado = true;
									
									break;
								}
								case 3 : {
									valor = valor + ( valor * ( PORCENTAGEM_20 /100.0D) );
									valor_alterado = true;
									
									break;
								}
							}
							
							//Valor foi recalculado?
							if( valor_alterado ) {
								guiaItemDt.setValorCalculado( String.valueOf(valor) );
							}
						}
						
						//Valor mï¿½ximo que pode ser cobrado
						if( custaDt.getValorMaximo() != null && 
							Double.valueOf(guiaItemDt.getValorCalculado()) > Double.valueOf(custaDt.getValorMaximo()) ) {
							
							guiaItemDt.setValorCalculado( custaDt.getValorMaximo() );
						}
						
						//Valor mï¿½nimo que pode ser cobrado
						if( custaDt.getMinimo() != null && 
							custaDt.getMinimo().length() > 0 &&
							Double.valueOf(custaDt.getMinimo()) > 0.0D && 
							Double.valueOf(guiaItemDt.getValorCalculado()) < Double.valueOf(custaDt.getMinimo()) ) {
							
							guiaItemDt.setValorCalculado( custaDt.getMinimo() );
						}
						
						//Total
						totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
						
						//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
						listaGuiaItemDt.add(guiaItemDt);
						
						//Retira item da lista para aumentar progressivamente e significativamente o desempenho deste mï¿½todo
						listaCustaDt.remove(i);
						
						break;
					}
				}
			}
		}	
	}
	
	
	/**
	 * Mï¿½todo para item de honorarios do procurador (guia de fazenda municipal).
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @throws Exception
	 */
	private void calcularItemHonorarios(List listaCustaDt, String valorReferenciaCalculo) throws Exception {
		
		int porcentagem = Funcoes.StringToInt(valorReferenciaCalculo.split(";")[0]);
		String valor = valorReferenciaCalculo.split(";")[1];
		
		GuiaItemDt guiaItemDt = new GuiaItemDt();
		
		//Lista de custas para comparar com os itens calculados
		for(int i = 0; i < listaCustaDt.size(); i++) {
			CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
			
			//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
			// e coloca ele na lista que serï¿½ utilizada na guia.
			//if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
				
				//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
				guiaItemDt.setCustaDt(custaDt);
				
				guiaItemDt.setValorReferencia(Funcoes.BancoDecimal(valor));
				guiaItemDt.setValorCalculado(String.valueOf(Funcoes.StringToDouble(guiaItemDt.getValorReferencia()) * (Funcoes.StringToDouble(valorReferenciaCalculo.split(";")[0])/100)));
				guiaItemDt.setQuantidade(String.valueOf(porcentagem));
				//guiaItemDt.setCustaDt( (CustaDt)listaCustaDt.get(0) );
				
				//Total
				totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
				
				//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
				listaGuiaItemDt.add(guiaItemDt);
		}
	
	}
	
	/**
	 * Mï¿½todo para itens que tem quantidade e valor de referencia informado.
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @param String valorReferencia
	 * @throws Exception
	 */
	private void calcularItemProtocoloIntegrado(List listaCustaDt, String quantidadeReferencia, String valorReferencia) throws Exception {

		CustaValorNe custaValorNe = new CustaValorNe();
		
		int quantidade = Funcoes.StringToInt(quantidadeReferencia);
		//Consulta itens comuns
		List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDt, valorReferencia);
		
		//Adiciona na propriedade lista da classe os itens calculados
		//Lista de itens que foram calculados
		for(int k = 0; k < listaGuiaItemDtAux.size(); k++) {
			GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(k);
			
			//Lista de custas para comparar com os itens calculados
			for(int i = 0; i < listaCustaDt.size(); i++) {
				CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
				
				//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
				// e coloca ele na lista que serï¿½ utilizada na guia.
				if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
					
					//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
					guiaItemDt.setCustaDt(custaDt);
					
					guiaItemDt.setValorReferencia( this.getValorProtocoloIntegrado(quantidade).toString() );
					guiaItemDt.setValorCalculado(guiaItemDt.getValorReferencia());
					guiaItemDt.setQuantidade(String.valueOf(quantidade));
					guiaItemDt.setCustaDt( (CustaDt)listaCustaDt.get(0) );
					
					//Total
					totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
					
					//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
					listaGuiaItemDt.add(guiaItemDt);
				}
			}
		}
	
	}
	
	/**
	 * Mï¿½todo para itens que tem quantidade e valor de referencia informado.
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @throws Exception
	 */
	private void calcularQuantidadeValor(List listaCustaDt, String valorReferenciaCalculo) throws Exception {

		CustaValorNe custaValorNe = new CustaValorNe();
		
		int quantidade = Funcoes.StringToInt(valorReferenciaCalculo.split(";")[0]);
		String valor = valorReferenciaCalculo.split(";")[1];
		
		//Consulta itens comuns
		List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDt, valor.toString());
		
		//Adiciona na propriedade lista da classe os itens calculados
		//Lista de itens que foram calculados
		for(int k = 0; k < listaGuiaItemDtAux.size(); k++) {
			GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(k);
			
			//Lista de custas para comparar com os itens calculados
			for(int i = 0; i < listaCustaDt.size(); i++) {
				CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
				
				//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
				// e coloca ele na lista que serï¿½ utilizada na guia.
				if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
					
					//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
					guiaItemDt.setCustaDt(custaDt);
					
					guiaItemDt.setValorCalculado(String.valueOf(Funcoes.StringToDouble(guiaItemDt.getValorReferencia()) * quantidade));
					guiaItemDt.setQuantidade(String.valueOf(quantidade));
					guiaItemDt.setCustaDt( (CustaDt)listaCustaDt.get(0) );
					
					//Total
					totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
					
					//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
					listaGuiaItemDt.add(guiaItemDt);
				}
			}
		}
	
	}
	
	/**
	 * Mï¿½todo para calcular itens com valores de bens.
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @throws Exception
	 */
	private void calcularValorBens(List listaCustaDt, String valorReferenciaCalculo) throws Exception {

		CustaValorNe custaValorNe = new CustaValorNe();
		
		String id_Valor[] = valorReferenciaCalculo.split(";");
		//Lista de custas para comparar com os itens calculados
		for(int i = 0; i < listaCustaDt.size(); i++) {
			CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
			
			for(int j = 0; j < id_Valor.length; j++) {
				String idCusta = id_Valor[j].split(":")[0];
				String valorCusta = id_Valor[j].split(":")[1];
				
				if( custaDt.getId().equals(idCusta) ) {
					
					//valorCusta = valorCusta.replace(".", "");
					//valorCusta = valorCusta.replace(",", ".");
					
					List listaCustaDtAux = new ArrayList();
					listaCustaDtAux.add(custaDt);
					List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDtAux, valorCusta);
					if(listaGuiaItemDtAux!=null && listaGuiaItemDtAux.size()>0){
						GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(0);
						guiaItemDt.setValorCalculado(guiaItemDt.getValorReferencia());
						guiaItemDt.setValorReferencia(valorCusta);
						guiaItemDt.setCustaDt(custaDt);
					
						//Total
						totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
					
						//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
						listaGuiaItemDt.add(guiaItemDt);
					}
				}
			}
			
		}
	
	}
	
	/**
	 * Mï¿½todo Genï¿½rico para consultar a lista de valores a serem cobrados de acordo com o intervalo do valor de referï¿½ncia que pode ser <<VALOR DA CAUSA>> ou <<VALOR DOS BENS>> ou <<OUTROS>>.
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @throws Exception
	 */
	private void calcularValorIntevaloCusta(List listaCustaDt, String valorReferenciaCalculo) throws Exception {

		CustaValorNe custaValorNe = new CustaValorNe();
		//Consulta itens comuns
		List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDt, valorReferenciaCalculo);
		
		//Adiciona na propriedade lista da classe os itens calculados
		//Lista de itens que foram calculados
		for(int k = 0; k < listaGuiaItemDtAux.size(); k++) {
			GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(k);
			
			//Lista de custas para comparar com os itens calculados
			for(int i = 0; i < listaCustaDt.size(); i++) {
				CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
				
				//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
				// e coloca ele na lista que serï¿½ utilizada na guia.
				if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
					
					//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
					guiaItemDt.setCustaDt(custaDt);
					
					//Calcula o subtotal do item: Quantidade x ValorReferencia do Item
					int quantidade 	= Funcoes.StringToInt(guiaItemDt.getQuantidade());
					double valor 	= 0.0D;
					if( custaDt.getValorAcrescimo() != null && Funcoes.StringToDouble(custaDt.getValorAcrescimo()) > 0.0D )
						valor = Funcoes.StringToDouble(custaDt.getValorAcrescimo()) * quantidade;
					valor = valor + Funcoes.StringToDouble(guiaItemDt.getValorReferencia());
					guiaItemDt.setValorCalculado( String.valueOf(valor) );
					
					
					
					//Valor mï¿½ximo que pode ser cobrado
					if( custaDt.getValorMaximo() != null && Double.valueOf(guiaItemDt.getValorCalculado()) > Double.valueOf(custaDt.getValorMaximo()) ) {
						guiaItemDt.setValorCalculado( custaDt.getValorMaximo() );
					}
					
					//Total
					totalGuia = totalGuia + (quantidade * valor);
					
					//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
					listaGuiaItemDt.add(guiaItemDt);
					
					//Retira item da lista para aumentar progressivamente e significativamente o desempenho deste mï¿½todo
					listaCustaDt.remove(i);
					
					break;
				}
			}
		}
	
	}
	
	/**
	 * Mï¿½todo Genï¿½rico para consultar a lista de valores a serem cobrados de acordo com o intervalo do valor de referï¿½ncia que pode ser <<VALOR DA CAUSA>> ou <<VALOR DOS BENS>> ou <<OUTROS>>.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @throws Exception
	 */
	private void calcularValorIntevaloCusta_Refatorado(GuiaEmissaoDt guiaEmissaoDt, List listaCustaDt, String valorReferenciaCalculo) throws Exception {

		CustaValorNe custaValorNe = new CustaValorNe();
		//Consulta itens comuns
		List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDt, valorReferenciaCalculo);
		
		//Adiciona na propriedade lista da classe os itens calculados
		//Lista de itens que foram calculados
		if( listaGuiaItemDtAux != null && listaGuiaItemDtAux.size() > 0 ) {
			for(int k = 0; k < listaGuiaItemDtAux.size(); k++) {
				GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(k);
				
				//Lista de custas para comparar com os itens calculados
				for(int i = 0; i < listaCustaDt.size(); i++) {
					CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
					
					//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
					// e coloca ele na lista que serï¿½ utilizada na guia.
					if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
						
						//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
						guiaItemDt.setCustaDt(custaDt);
						
						int quantidade = 1;
						
						switch( Funcoes.StringToInt(custaDt.getId()) ) {
						
							case CustaDt.PARA_REALIZACAO_E_CONFERENCIA_DE_CALCULOS_E_ATRIBUICOES: {
								if( guiaEmissaoDt.getAtualizacaoValorNominalQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getAtualizacaoValorNominalQuantidade()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getAtualizacaoValorNominalQuantidade());
									break;
								}
								if( guiaEmissaoDt.getContadorQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getContadorQuantidade()) > 0 ) { 
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getContadorQuantidade());
									break;
								}
								if( guiaEmissaoDt.getCustasQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getCustasQuantidade()) > 0  && guiaEmissaoDt.getGuiaTipo() != null && !guiaEmissaoDt.getGuiaTipo().equals(GuiaTipoDt.ID_CARTA_PRECATORIA)) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getCustasQuantidade());
									break;
								}
								if( guiaEmissaoDt.getRetificacaoCalculosQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getRetificacaoCalculosQuantidade()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getRetificacaoCalculosQuantidade());
									break;
								}
								if( guiaEmissaoDt.getTransformacaoMoedaQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getTransformacaoMoedaQuantidade()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getTransformacaoMoedaQuantidade());
									break;
								}
								if( guiaEmissaoDt.getRetificacaoCustasQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getRetificacaoCustasQuantidade()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getRetificacaoCustasQuantidade());
									break;
								}
								
								break;
							}
							
							case CustaDt.REGISTRO_DE_PETICAO_INICIAL : {
								if( guiaEmissaoDt.getTaxaProtocoloQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getTaxaProtocoloQuantidade()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getTaxaProtocoloQuantidade());
									break;
								}
								if( guiaEmissaoDt.getAfixacaoEdital().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getAfixacaoEdital()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getAfixacaoEdital());
									break;
								}
								if( guiaEmissaoDt.getLeilaoQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getLeilaoQuantidade()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getLeilaoQuantidade());
									break;
								}
								
								break;
							}
							
							case CustaDt.PROCESSOS_EXEC_SENTENCA_OU_TIT_EXTRA : {
								if( guiaEmissaoDt.getEscrivaniaQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getEscrivaniaQuantidade()) > 0 )
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getEscrivaniaQuantidade());
								break;
							}
							
							case CustaDt.DESPESA_POSTAL : {
								if( guiaEmissaoDt.getCorreioQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getCorreioQuantidade()) > 0 )
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getCorreioQuantidade());
								break;
							}
							
							case CustaDt.OUTROS_GRJ : {
								if( guiaEmissaoDt.getCitacaoUrbana().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getCitacaoUrbana()) > 0 )
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getCitacaoUrbana());
								break;
							}
							
							case CustaDt.TAXA_JUDICIARIA_SERVICO_CARTA_ARREMATACAO_SITE_TJGO_ITEM_5 : {
								if( guiaEmissaoDt.getTaxaJudiciariaServicoCartaArrematacao().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getTaxaJudiciariaServicoCartaArrematacao()) > 0 )
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getTaxaJudiciariaServicoCartaArrematacao());
								break;
							}
							
							case CustaDt.TAXA_JUDICIARIA_SERVICO_CERTIDAO_SITE_TJGO_ITEM_6 : {
								if( guiaEmissaoDt.getTaxaJudiciariaServicoCertidao().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getTaxaJudiciariaServicoCertidao()) > 0 )
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getTaxaJudiciariaServicoCertidao());
								break;
							}
							
							case CustaDt.AUTO_DE_AVALIACAO_DE_BENS_EM_PROCESSO_DE_QUALQUER_NATUREZA : {
								if( guiaEmissaoDt.getAvaliadorQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getAvaliadorQuantidade()) > 0 )
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getAvaliadorQuantidade());
								break;
							}
							
							case CustaDt.CUSTA_PARTIDOR : {
								if( guiaEmissaoDt.getPartidorQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getPartidorQuantidade()) > 0 )
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getPartidorQuantidade());
								break;
							}
							
							case CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO : {
								if( guiaEmissaoDt.getPenhoraQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getPenhoraQuantidade()) > 0 )
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getPenhoraQuantidade());
								break;
							}
							
							case CustaDt.ATOS_DE_DISTRIBUICAO_DOS_PROCESSOS_FISICOS_APLICA_SE_10_SOBRE_VALOR_MINIMO_ITEM_5I : {
								if( guiaEmissaoDt.getDistribuidorQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getDistribuidorQuantidade()) > 0 )
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getDistribuidorQuantidade());
								break;
							}
							
							case CustaDt.POR_DOCUMENTO_PUBLICADO : {
								if( guiaEmissaoDt.getDocumentoDiarioJustica().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getDocumentoDiarioJustica()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getDocumentoDiarioJustica());
									break;
								}
								break;
							}
							
							case CustaDt.PARA_REALIZACAO_E_CONFERENCIA_DE_CALCULOS_E_ATRIBUICOES_10_MINIMO_ITEM_5 : {
								if( guiaEmissaoDt.getContadorQuantidadeAcrescimo().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getContadorQuantidadeAcrescimo()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getContadorQuantidadeAcrescimo());
									break;
								}
								break;
							}
							
							case CustaDt.DEPOSITO_COMPREENDENDO_OS_REGISTROS_GUARDA_ESCRITURACAO : {
								if( guiaEmissaoDt.getDepositarioPublico().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getDepositarioPublico()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getDepositarioPublico());
									break;
								}
								break;
							}
							
							case CustaDt.TAXAS_DE_SERVICO_CERTIDOES_DE_ACORDAO : {
								if( guiaEmissaoDt.getCertidaoAcordao().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getCertidaoAcordao()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getCertidaoAcordao());
									break;
								}
								break;
							}
							
							case CustaDt.TAXAS_DE_SERVICO_TRASLADOS_DESARQUIVAMENTO_DOS_AUTOS : {
								if( guiaEmissaoDt.getDesarquivamento().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getDesarquivamento()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getDesarquivamento());
									break;
								}
								break;
							}
							
							case CustaDt.TAXAS_DE_SERVICO_RESTAURACAO_DE_AUTOS : {
								if( guiaEmissaoDt.getRestauracao().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getRestauracao()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getRestauracao());
									break;
								}
								break;
							}
							
							case CustaDt.RESTAURACAO_DE_AUTOS : {
								if( guiaEmissaoDt.getRestauracaoAtos().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getRestauracaoAtos()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getRestauracaoAtos());
									break;
								}
								break;
							}
							
							case CustaDt.TAXAS_DE_SERVICO_POR_DOCUMENTO_PUBLICADO_NO_DIARIO_DE_JUSTICA : {
								if( guiaEmissaoDt.getDocumentoPublicadoQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getDocumentoPublicadoQuantidade()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getDocumentoPublicadoQuantidade());
									break;
								}
								break;
							}
							
							case CustaDt.TAXAS_DE_SERVICO_PORTE_E_REMESSA_DE_PROCESSOS_FISICOS : {
								if( guiaEmissaoDt.getPorteRemessaQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getPorteRemessaQuantidade()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getPorteRemessaQuantidade());
								}
								else {
									if( guiaEmissaoDt.getPorteRemessaValorManual().length() > 0 ) {
										guiaItemDt.setValorReferencia(guiaEmissaoDt.getPorteRemessaValorManual());
									}
								}
								break;
							}
							
							case CustaDt.TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM : {
								if( guiaEmissaoDt.getCorreioQuantidadeReg4VI().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getCorreioQuantidadeReg4VI()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getCorreioQuantidadeReg4VI());
									break;
								}
								break;
							}
							
							case CustaDt.TAXAS_DE_SERVICO_PELA_EMISSAO_DOS_DOCUMENTOS_DE_COMUNICACAO : {
								if( guiaEmissaoDt.getEmissaoDocumentoQuantidade().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getEmissaoDocumentoQuantidade()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getEmissaoDocumentoQuantidade());
									break;
								}
								break;
							}
							
							case CustaDt.TAXAS_DE_SERVICO_PELA_EMISSAO_DOS_ATOS_DE_CONSTRICAO : {
								if( guiaEmissaoDt.getAtosConstricao().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getAtosConstricao()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getAtosConstricao());
									break;
								}
								break;
							}
							
							case CustaDt.CERTIDOES_DAS_DECISOES : {
								if( guiaEmissaoDt.getCertidaoDecisao().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getCertidaoDecisao()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getCertidaoDecisao());
									break;
								}
								break;
							}
							
							case CustaDt.TRASLADOS_DESARQUIVAMENTO_DOS_AUTOS_E_OUTRAS_CERTIDOES : {
								if( guiaEmissaoDt.getDesarquivamento16II().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getDesarquivamento16II()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getDesarquivamento16II());
								}
								break;
							}
							
							case CustaDt.PORTE_E_REMESSA_DE_PROCESSOS_FISICOS : {
								if( guiaEmissaoDt.getPorteRemessaQuantidade16V().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getPorteRemessaQuantidade16V()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getPorteRemessaQuantidade16V());
									break;
								}
								break;
							}
							
							//***********************************
							//***********************************
							//***********************************
							case CustaDt.PELA_EMISSAO_DOS_DOCUMENTOS_DE_COMUNICACAO : {
								if( guiaEmissaoDt.getEmissaoDocumentoQuantidade16VII().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getEmissaoDocumentoQuantidade16VII()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getEmissaoDocumentoQuantidade16VII());
									break;
								}
								break;
							}
							//***********************************
							//***********************************
							//***********************************
							
							case CustaDt.PELA_EMISSAO_DOS_ATOS_DE_CONSTRICAO : {
								if( guiaEmissaoDt.getAtosConstricao16VIII().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getAtosConstricao16VIII()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getAtosConstricao16VIII());
									break;
								}
								break;
							}
							
							case CustaDt.FORMAL_DE_PARTILHA_CARTA_DE_SENTENCA : {
								if( guiaEmissaoDt.getFormalPartilhaCartaQuantidade16IX().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getFormalPartilhaCartaQuantidade16IX()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getFormalPartilhaCartaQuantidade16IX());
									break;
								}
								break;
							}
							
							case CustaDt.CUMPRIMENTO_DE_CARTAS_PRECATORIAS : {
								if( guiaEmissaoDt.getCumprimentoPrecatoria().length() > 0 && Funcoes.StringToInt(guiaEmissaoDt.getCumprimentoPrecatoria()) > 0 ) {
									quantidade = Funcoes.StringToInt(guiaEmissaoDt.getCumprimentoPrecatoria());
									break;
								}
								break;
							}
							
							case CustaDt.RECURSOS_CIVEIS_ORIUNDOS_DO_PRIMEIRO_GRAU : {
								if( guiaEmissaoDt.getCustasValorManual().length() > 0 && Funcoes.StringToDouble(guiaEmissaoDt.getCustasValorManual()) > 0 ) {
									guiaItemDt.setValorReferencia(guiaEmissaoDt.getCustasValorManual());
								}
								break;
							}
							
						}
						
						
						//Caso for guia complementar
						
						
						//Calcula o subtotal do item: Quantidade x ValorReferencia do Item
						double valor = 0.0D;
						if( custaDt.getValorAcrescimo() != null && Funcoes.StringToDouble(custaDt.getValorAcrescimo()) > 0.0D )
							valor = Funcoes.StringToDouble(custaDt.getValorAcrescimo()) * quantidade;
						valor = valor + Funcoes.StringToDouble(guiaItemDt.getValorReferencia());
						guiaItemDt.setValorCalculado( String.valueOf(valor * quantidade) );
						guiaItemDt.setQuantidade(String.valueOf(quantidade));
						
						
						//Valor mï¿½ximo que pode ser cobrado
						if( custaDt.getValorMaximo() != null && 
							Double.valueOf(guiaItemDt.getValorCalculado()) > Double.valueOf(custaDt.getValorMaximo()) ) {
							
							guiaItemDt.setValorCalculado( custaDt.getValorMaximo() );
						}
						
						//Valor mï¿½ximo que pode ser cobrado
						if( custaDt.getMinimo() != null && 
							custaDt.getMinimo().length() > 0 &&
							Double.valueOf(custaDt.getMinimo()) > 0.0D && 
							Double.valueOf(guiaItemDt.getValorCalculado()) < Double.valueOf(custaDt.getMinimo()) ) {
							
							guiaItemDt.setValorCalculado( custaDt.getMinimo() );
						}
						
						BigDecimal valorCalculado = new BigDecimal(Funcoes.StringToDouble(guiaItemDt.getValorCalculado()));
						valorCalculado = valorCalculado.setScale(2, BigDecimal.ROUND_HALF_UP);
						guiaItemDt.setValorCalculado(valorCalculado.toString());
						
						//Total
						totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
						
						//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
						listaGuiaItemDt.add(guiaItemDt);
						
						//Retira item da lista para aumentar progressivamente e significativamente o desempenho deste mï¿½todo
						listaCustaDt.remove(i);
						
						break;
					}
				}
			}
		}
	
	}
	
	/**
	 * Mï¿½todo Genï¿½rico para consultar a lista de valores a serem cobrados de acordo com o intervalo do valor de referï¿½ncia que pode ser <<VALOR DA CAUSA>> ou <<VALOR DOS BENS>> ou <<OUTROS>>.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaCustaDt
	 * @param Map valoresReferenciaCalculo
	 * @throws Exception
	 */
	private void calcularValorIntevaloCustaGenerica(GuiaEmissaoDt guiaEmissaoDt, List listaCustaDt, Map valoresReferenciaCalculo) throws Exception {

		List listaGuiaItemDtAux = null;
		if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
			listaGuiaItemDtAux = new ArrayList();
			for(int i = 0; i < listaCustaDt.size(); i++) {
				CustaDt custaDt = (CustaDt) listaCustaDt.get(i);
				
				GuiaItemDt guiaItemDt = new GuiaItemDt();
				guiaItemDt.setCustaDt(custaDt);
				
				listaGuiaItemDtAux.add(guiaItemDt);
			}
		}
		
		List listaVariavelIdArrecadacaoCusta = (List)valoresReferenciaCalculo.get("ListaVariavelIdArrecadacaoCusta");
		List listaVariavelQuantidadeArrecadacaoCusta = (List)valoresReferenciaCalculo.get("ListaVariavelQuantidadeArrecadacaoCusta");
		List listaVariavelValorArrecadacaoCusta = (List)valoresReferenciaCalculo.get("ListaVariavelValorArrecadacaoCusta");
		
		//Adiciona na propriedade lista da classe os itens calculados
		//Lista de itens que foram calculados
		if( listaGuiaItemDtAux != null && listaGuiaItemDtAux.size() > 0 ) {
			for(int k = 0; k < listaGuiaItemDtAux.size(); k++) {
				GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(k);
				
				//Lista de custas para comparar com os itens calculados
				for(int i = 0; i < listaCustaDt.size(); i++) {
					CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
					
					//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
					// e coloca ele na lista que serï¿½ utilizada na guia.
					if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
						
						int quantidade = Funcoes.StringToInt(listaVariavelQuantidadeArrecadacaoCusta.get(k).toString());
						guiaItemDt.setValorReferencia( Funcoes.BancoDecimal(listaVariavelValorArrecadacaoCusta.get(k).toString()) );
						guiaItemDt.setId_ArrecadacaoCustaGenerica(listaVariavelIdArrecadacaoCusta.get(k).toString());
						
						ArrecadacaoCustaDt arrecadacaoCustaDt = new ArrecadacaoCustaNe().consultarId(guiaItemDt.getId_ArrecadacaoCustaGenerica());
						custaDt.setArrecadacaoCusta(arrecadacaoCustaDt.getArrecadacaoCusta());
						custaDt.setCodigoArrecadacao(arrecadacaoCustaDt.getCodigoArrecadacao());
						
						//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
						guiaItemDt.setCustaDt(custaDt);
						
						//Calcula o subtotal do item: Quantidade x ValorReferencia do Item
						double valor = 0.0D;
						if( custaDt.getValorAcrescimo() != null && Funcoes.StringToDouble(custaDt.getValorAcrescimo()) > 0.0D ) {
							valor = Funcoes.StringToDouble(custaDt.getValorAcrescimo()) * quantidade;
						}
						valor = valor + Funcoes.StringToDouble(guiaItemDt.getValorReferencia());
						guiaItemDt.setValorCalculado( String.valueOf(valor * quantidade) );
						guiaItemDt.setQuantidade(String.valueOf(quantidade));
						
						
						//Valor mï¿½ximo que pode ser cobrado
						if( custaDt.getValorMaximo() != null && Double.valueOf(guiaItemDt.getValorCalculado()) > Double.valueOf(custaDt.getValorMaximo()) ) {
							guiaItemDt.setValorCalculado( custaDt.getValorMaximo() );
						}
						
						//Total
						totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
						
						//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
						listaGuiaItemDt.add(guiaItemDt);
						
						//Retira item da lista para aumentar progressivamente e significativamente o desempenho deste mï¿½todo
						listaCustaDt.remove(i);
						
						break;
					}
				}
			}
		}
	
	}
	
	/**
	 * Mï¿½todo para obter o valor do mandado de seguranï¿½a coletivo que ï¿½ ï¿½nico independente da quantidade de impetrantes.
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @throws Exception
	 */
	private void calcularValorMandadoSegurancaColetivo(List listaCustaDt) throws Exception {

		CustaValorNe custaValorNe = new CustaValorNe();
		
		if( listaCustaDt != null && listaCustaDt.size() == 1 ) {
			CustaDt custaDt = (CustaDt)listaCustaDt.get(0);
			
			GuiaItemDt guiaItemDt = custaValorNe.consultaValorCusta(custaDt.getId());
			
			if( guiaItemDt.getCustaDt().getId().equals(custaDt.getId()) )
				guiaItemDt.setCustaDt(custaDt);
			
			guiaItemDt.setValorCalculado(guiaItemDt.getValorReferencia());
			
			//Total
			totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
			
			//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
			listaGuiaItemDt.add(guiaItemDt);
		}
	
	}

	/**
	 * Mï¿½todo para Calcular itens de Mandados de Seguranï¿½a.(Nï¿½O COLETIVO!)
	 * @param List listaCustaDt
	 * @param String numeroImpetrantes
	 * @throws Exception
	 */
	private void calcularValorMandadoSeguranca(List listaCustaDt, String numeroImpetrantes) throws Exception {

		
		CustaValorNe custaValorNe = new CustaValorNe();
		
		//Consulta itens comuns
		List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDt, null);
		
		Double total_PARCIAL = 0.0D;
		
		for(int k = 0; k < listaGuiaItemDtAux.size(); k++) {
			GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(k);
			
			//Lista de custas para comparar com os itens calculados
			for(int i = 0; i < listaCustaDt.size(); i++) {
				
				CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
				
				//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
				// e coloca ele na lista que serï¿½ utilizada na guia.
				if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
					guiaItemDt.setCustaDt(custaDt);
					
					Integer quantidadeImpetrantes = Funcoes.StringToInt(numeroImpetrantes);
					double valor = Funcoes.StringToDouble(guiaItemDt.getValorReferencia());
					double valorAcrescimo = 0.0D;
					
					if( quantidadeImpetrantes > 1 ) {
						valorAcrescimo = (quantidadeImpetrantes - 1) * Funcoes.StringToDouble(custaDt.getValorAcrescimo());
						guiaItemDt.getCustaDt().setArrecadacaoCusta( guiaItemDt.getCustaDt().getArrecadacaoCusta() + " (ACRï¿½SCIMO POR IMPETRANTE)" );
						guiaItemDt.setQuantidade(String.valueOf(quantidadeImpetrantes - 1));
					}
					
					valor = valor + valorAcrescimo;
					
					if( custaDt.getValorMaximo() != null && valor > Funcoes.StringToDouble(custaDt.getValorMaximo()) ) 
						guiaItemDt.setValorCalculado( String.valueOf(custaDt.getValorMaximo()) );
					else
						guiaItemDt.setValorCalculado( String.valueOf(valor) );
					
					//Total
					totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
					
					//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
					listaGuiaItemDt.add(guiaItemDt);
					
					//Retira item da lista para aumentar progressivamente e significativamente o desempenho deste mï¿½todo
					listaCustaDt.remove(i);
					
					break;
				}
			}
		}
		
		
		//Total
		totalGuia = totalGuia + total_PARCIAL;
	
	}
	
	/**
	 * Mï¿½todo para calcular quantidade de KM percorridos.
	 * @param List listaCustaDt
	 * @param String quantidade
	 * @throws Exception
	 */
	private void calcularQuantidadeKM(List listaCustaDt, String quantidade) throws Exception {

		CustaValorNe custaValorNe = new CustaValorNe();
		//Consulta itens comuns
		List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDt, null);
		
		//Adiciona na propriedade lista da classe os itens calculados
		//Lista de itens que foram calculados
		for(int k = 0; k < listaGuiaItemDtAux.size(); k++) {
			GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(k);
			
			//Lista de custas para comparar com os itens calculados
			for(int i = 0; i < listaCustaDt.size(); i++) {
				CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
				
				//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
				// e coloca ele na lista que serï¿½ utilizada na guia.
				if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
					
					//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
					guiaItemDt.setCustaDt(custaDt);
					
					//Calcula o subtotal do item: Quantidade KM x Valor de Acrescimo
					int quantidadeReferencia = Funcoes.StringToInt(quantidade);
					quantidadeReferencia = getCustaQuantidade(guiaItemDt.getCustaDt().getId());
					
					double valor = 0.0D;
					if( quantidadeReferencia > 0 ) {
						guiaItemDt.setQuantidade( String.valueOf(quantidadeReferencia) );
						valor = quantidadeReferencia * Double.valueOf(custaDt.getValorAcrescimo());
					}
					guiaItemDt.setValorReferencia( custaDt.getValorAcrescimo() );
					guiaItemDt.setValorCalculado( String.valueOf( valor) );
					
					//Valor mï¿½ximo que pode ser cobrado
					if( custaDt.getValorMaximo() != null && Double.valueOf(guiaItemDt.getValorCalculado()) > Double.valueOf(custaDt.getValorMaximo()) ) {
						guiaItemDt.setValorCalculado( custaDt.getValorMaximo() );
					}
					
					//Total
					totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
					
					//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
					listaGuiaItemDt.add(guiaItemDt);
					
					//Retira item da lista para aumentar progressivamente e significativamente o desempenho deste mï¿½todo
					listaCustaDt.remove(i);
					
					break;
				}
			}
		}	
	}
	
	/**
	 * Mï¿½todo para retornar o valor cobrado para o porte remessa/retorno.
	 * @param int quantidade
	 * @return
	 * @throws Exception
	 */
	public Double getValorPorteRemessaRetorno(int quantidade) throws Exception {
		Double retorno = 0.0D;

		if( quantidade >= 1261 ) {
			retorno = 116.67D;
		}
		else {
			if( quantidade >= 1081 && quantidade <= 1260) {
				retorno = 116.67D;
			}
			else {
				if( quantidade >= 901 && quantidade <= 1080) {
					retorno = 106.33D;
				}
				else {
					if( quantidade >= 721 && quantidade <= 900) {
						retorno = 97.72D;
					}
					else {
						if( quantidade >= 541 && quantidade <= 720) {
							retorno = 89.10D;
						}
						else {
							if( quantidade >= 361 && quantidade <= 540) {
								retorno = 78.76D;
							}
							else {
								if( quantidade >= 181 && quantidade <= 360) {
									retorno = 68.43D;
								}
								else {
									if( quantidade <= 180 ) {
										retorno = 57.84D;
									}
								}
							}
						}
					}
				}
			}
		}
	
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para retornar o valor cobrado para o porte remessa/retorno.
	 * @param int quantidade
	 * @return
	 * @throws Exception
	 */
	public Double getValorProtocoloIntegrado(int quantidade) throws Exception {
		Double retorno = 0.0D;

		if( quantidade >= 1261 ) {
			retorno = 116.67D;
		}
		else {
			if( quantidade >= 1081 && quantidade <= 1260) {
				retorno = 116.67D;
			}
			else {
				if( quantidade >= 901 && quantidade <= 1080) {
					retorno = 106.33D;
				}
				else {
					if( quantidade >= 721 && quantidade <= 900) {
						retorno = 97.72D;
					}
					else {
						if( quantidade >= 541 && quantidade <= 720) {
							retorno = 89.10D;
						}
						else {
							if( quantidade >= 361 && quantidade <= 540) {
								retorno = 78.76D;
							}
							else {
								if( quantidade >= 181 && quantidade <= 360) {
									retorno = 68.43D;
								}
								else {
									if( quantidade <= 180 ) {
										retorno = 57.84D;
									}
								}
							}
						}
					}
				}
			}
		}
	
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para calcular o rateio das guias 50% para parte.
	 * @param List listaGuiaItemDt
	 * @throws Exception
	 */
	public void calcularRateio50Porcento(List listaGuiaItemDt) throws Exception {

		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			for( int i = 0; i < listaGuiaItemDt.size(); i++ ) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(i);
				
				guiaItemDt.setValorCalculado( String.valueOf(Funcoes.StringToDouble(guiaItemDt.getValorCalculado()) / 2) );
			}
		}
	
	}
	
	/**
	 * Mï¿½todo para calcular o cï¿½digo de regimento 16 para os atos do escrivï¿½o.
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @param String porcentagem
	 * @param String quantidadeReferencia
	 * @throws Exception
	 */
	public void calcularItemAtoEscrivao(List listaCustaDt, String valorReferenciaCalculo, String porcentagem, String quantidadeReferencia) throws Exception {

		CustaValorNe custaValorNe = new CustaValorNe();
		
		int quantidade = 1;
		if( quantidadeReferencia != null && Funcoes.StringToInt(quantidadeReferencia) > 0 ) {
			quantidade = Funcoes.StringToInt(quantidadeReferencia);
		}
		
		//Consulta itens comuns
		List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDt, valorReferenciaCalculo);
		
		//Adiciona na propriedade lista da classe os itens calculados
		//Lista de itens que foram calculados
		for(int k = 0; k < listaGuiaItemDtAux.size(); k++) {
			GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(k);
			
			//Lista de custas para comparar com os itens calculados
			for(int i = 0; i < listaCustaDt.size(); i++) {
				CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
				
				//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
				// e coloca ele na lista que serï¿½ utilizada na guia.
				if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
					
					//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
					guiaItemDt.setCustaDt(custaDt);
					
					BigDecimal valorReferencia = new BigDecimal(guiaItemDt.getValorReferencia());
					BigDecimal valorCalculado = valorReferencia.multiply(new BigDecimal(quantidade), MathContext.DECIMAL32);
					
					valorCalculado = valorCalculado.setScale(2, BigDecimal.ROUND_DOWN);
					
					guiaItemDt.setValorCalculado(valorCalculado.toString());
					
					if( porcentagem != null && porcentagem.length() > 0 ) {
						Double porcento = Funcoes.StringToDouble(porcentagem);
						guiaItemDt.setValorCalculado( String.valueOf((Funcoes.StringToDouble(guiaItemDt.getValorCalculado()) * porcento)/100.0D) );
					}
					
					guiaItemDt.setQuantidade(String.valueOf(quantidade));
					guiaItemDt.setCustaDt( (CustaDt)listaCustaDt.get(0) );
					
					//Total
					totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
					
					//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
					listaGuiaItemDt.add(guiaItemDt);
				}
			}
		}
	
	}
	
	/**
	 * Mï¿½todo para calcular a cobranca do item de custa para os 30% restantes. Na custa inicial ï¿½ cobrado os 70%.
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @throws Exception
	 */
	public void calcularItemCustas30Porcento(List listaCustaDt, String valorReferenciaCalculo) throws Exception {

		CustaValorNe custaValorNe = new CustaValorNe();
		
		int quantidade = 1;
		String valor = valorReferenciaCalculo;
		
		//Consulta itens comuns
		List listaGuiaItemDtAux = custaValorNe.consultaValorIntevaloCusta(listaCustaDt, valor.toString());
		
		//Adiciona na propriedade lista da classe os itens calculados
		//Lista de itens que foram calculados
		for(int k = 0; k < listaGuiaItemDtAux.size(); k++) {
			GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(k);
			
			//Lista de custas para comparar com os itens calculados
			for(int i = 0; i < listaCustaDt.size(); i++) {
				CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
				
				//Se id iguais, retirar o item da lista para terminar o processamente mais rï¿½pido
				// e coloca ele na lista que serï¿½ utilizada na guia.
				if( custaDt.getId().equals(guiaItemDt.getCustaDt().getId()) ) {
					
					//Adiciona CustaDt no GuiaItemDt com informaï¿½ï¿½es do cï¿½digo e descriï¿½ï¿½o da Arrecadaï¿½ï¿½o e dados da Custa
					guiaItemDt.setCustaDt(custaDt);
					
					guiaItemDt.setValorCalculado(String.valueOf(Funcoes.StringToDouble(guiaItemDt.getValorReferencia()) * quantidade));
					guiaItemDt.setQuantidade(String.valueOf(quantidade));
					guiaItemDt.setCustaDt( (CustaDt)listaCustaDt.get(0) );
					
					//Total
					totalGuia = totalGuia + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
					
					//Adiciona CustaDt na lista principal de GuiaItemDt que serï¿½ utilizada na emissï¿½o da Guia
					listaGuiaItemDt.add(guiaItemDt);
				}
			}
		}
	
	}
	
	/**
	 * Mï¿½todo para converter o valor para a data atual.
	 * @param Date date
	 * @param Double valor
	 * @return Double
	 * @throws Exception
	 */
	public Double converterValorDataAtual(Date date, Double valor) throws Exception {
		if(date.getTime() < Funcoes.StringToDate("01/07/1994").getTime()) {
			if (date.getTime() < Funcoes.StringToDate("01/02/1967").getTime()) {
	            //Se a data para correï¿½ï¿½o ï¿½ inferior a 1/2/1967 entï¿½o o valor fixo convertido =  (((valor fixo / 1000000000000) / 2750) * 100) / 100
	            return ((((valor / new Long("1000000000000"))) / 2750) * 100)/100;
	        }
	        else {
	        	if (date.getTime() < Funcoes.StringToDate("28/02/1986").getTime()) {
		            //Se a data para correï¿½ï¿½o ï¿½ inferior a 28/2/1986 entï¿½o o valor fixo convertido = ((( valor fixo  / 1000000000) / 2750) * 100) / 100
		            return ((((valor / 1000000000)) / 2750) * 100)/100;
	        	}
				else {
					if (date.getTime() < Funcoes.StringToDate("01/02/1989").getTime()) {
						//Se a data para correï¿½ï¿½o ï¿½ inferior a 1/2/1989 entï¿½o o valor fixo convertido = ((( valor fixo  / 1000000) / 2750) * 100) / 100;
						return ((((valor / 1000000)) / 2750) * 100)/100;
					}
					else {
						if (date.getTime() < Funcoes.StringToDate("01/08/1993").getTime()) {
							//Se a data para correï¿½ï¿½o ï¿½ inferior a 1/8/1993 entï¿½o o valor fixo convertido = ((( valor fixo  / 1000) / 2750) * 100) / 100;
							return ((((valor / 1000)) / 2750) * 100)/100;
						}
						else {
							if (date.getTime() < Funcoes.StringToDate("01/07/1994").getTime()) {
								//se a data para correï¿½ï¿½o ï¿½ inferior a 1/7/1994 entï¿½o o valor fixo convertido = (( valor fixo  / 2750) * 100) / 100;
								return ((valor / 2750) * 100)/100;
							}
							else {
								//Se nï¿½o valor fixo convertido ï¿½ igual ao valor fixo inicial.
								return valor;
							}
						}
					}
				}
	        }
		}
		
		return valor;
	}
	
	/**
	 * Mï¿½todo para calcular as locomoï¿½ï¿½es complementares.
	 * 
	 * @param List listaGuiaItemDt
	 * @param List listaGuiaItemDt_GuiaLocomocaoPaga
	 * @throws Exception
	 */
	public void calcularItensGuiaLocomocaoComplementar(List listaGuiaItemDt, List listaGuiaItemDt_GuiaLocomocaoPaga) throws Exception {
		if( listaGuiaItemDt != null && listaGuiaItemDt_GuiaLocomocaoPaga != null ) {
			for( int i = 0; i < listaGuiaItemDt.size(); i++ ) {
				GuiaItemDt guiaItemDtNovo = (GuiaItemDt) listaGuiaItemDt.get(i);
				GuiaItemDt guiaItemDtPago = (GuiaItemDt) listaGuiaItemDt_GuiaLocomocaoPaga.get(i);
				
				Double valor = Funcoes.StringToDouble(guiaItemDtNovo.getValorCalculado()) - Funcoes.StringToDouble(guiaItemDtPago.getValorCalculado());
				
				guiaItemDtNovo.setValorCalculado( valor.toString() );
			}
		}
	}
	
	/**
	 * Mï¿½todo para calcular as locomoï¿½ï¿½es complementares da guia inicial.
	 * 
	 * @param List listaGuiaItemDt
	 * @param List listaGuiaItemDt_GuiaLocomocaoPaga
	 * @throws Exception
	 */
	public void calcularItensGuiaInicialLocomocaoComplementar(List listaGuiaItemDt, List listaGuiaItemDt_GuiaLocomocaoPaga) throws Exception {

		
		if( listaGuiaItemDt != null && listaGuiaItemDt_GuiaLocomocaoPaga != null ) {
			
			int tamanhoListaGuiaItemDt 						= listaGuiaItemDt.size();
			int tamanhoListaGuiaItemDt_GuiaLocomocaoPaga 	= listaGuiaItemDt_GuiaLocomocaoPaga.size();
			
			//************* IGUAL ou PAGOS MAIOR
			if( tamanhoListaGuiaItemDt == tamanhoListaGuiaItemDt_GuiaLocomocaoPaga 
				|| tamanhoListaGuiaItemDt < tamanhoListaGuiaItemDt_GuiaLocomocaoPaga ) {
				for( int i = 0; i < tamanhoListaGuiaItemDt; i++ ) {
					GuiaItemDt guiaItemDtNovo = (GuiaItemDt) listaGuiaItemDt.get(i);
					GuiaItemDt guiaItemDtPago = (GuiaItemDt) listaGuiaItemDt_GuiaLocomocaoPaga.get(i);
					
					Double valor = Funcoes.StringToDouble(guiaItemDtNovo.getValorCalculado()) - Funcoes.StringToDouble(guiaItemDtPago.getValorCalculado());
					
					guiaItemDtNovo.setValorCalculado( valor.toString() );
				}
			}
			else {
				
				//***************** NOVOS MAIOR
				if( tamanhoListaGuiaItemDt > tamanhoListaGuiaItemDt_GuiaLocomocaoPaga ) {
					for( int i = 0; i < tamanhoListaGuiaItemDt_GuiaLocomocaoPaga; i++ ) {
						GuiaItemDt guiaItemDtNovo = (GuiaItemDt) listaGuiaItemDt.get(i);
						GuiaItemDt guiaItemDtPago = (GuiaItemDt) listaGuiaItemDt_GuiaLocomocaoPaga.get(i);
						
						Double valor = Funcoes.StringToDouble(guiaItemDtNovo.getValorCalculado()) - Funcoes.StringToDouble(guiaItemDtPago.getValorCalculado());
						
						guiaItemDtNovo.setValorCalculado( valor.toString() );
					}
				}
			}
			
		}
		
	
	}
	
	/**
	 * Mï¿½todo para retirar os itens com valores zerados.
	 * 
	 * @param List listaGuiaItemDt
	 * @throws Exception
	 */
	public void retirarItensValorCalculadoZerado(List listaGuiaItemDt) throws Exception {

		//Retirar itens com quantidade zerados
		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			for( int i = 0; i < listaGuiaItemDt.size();) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(i);
				
				if( Funcoes.StringToDouble(guiaItemDt.getValorCalculado()) <= 0.0D ) {
					listaGuiaItemDt.remove(i);
					if( i == 0) {
						i = 0;
					}
					else {
						i--;
					}
				}
				else {
					i++;
				}
			}
		}
	
	}
	
	/**
	 * Mï¿½todo para retirar casas decimais acima de 2 casas.
	 * @throws Exception
	 */
	private void retirarCasasDecimais() throws Exception {
		totalGuia = Funcoes.retirarCasasDecimais(totalGuia);		
	}
	
	/**
	 * Mï¿½todo para gerar as guias parceladas.
	 * 
	 * @param String quantidadeParcelas
	 * @param GuiaEmissaoDt guiaEmissaoReferenciaDt
	 * @param List<GuiaItemDt> listaGuiaItemDt_locomocao
	 * @param List<GuiaItemDt> listaGuiaItemDt_NAO_locomocao
	 * 
	 * @return List<GuiaEmissaoDt>
	 * 
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> parcelarGuia(String quantidadeParcelas, GuiaEmissaoDt guiaEmissaoReferenciaDt, List<GuiaItemDt> listaGuiaItemDt_locomocao, List<GuiaItemDt> listaGuiaItemDt_NAO_locomocao, String idUsuario) throws Exception {
		List<GuiaEmissaoDt> listaGuiaEmissaoDt_Parceladas = null;
		
		int quantidadeParcela = Funcoes.StringToInt(quantidadeParcelas);
		
		Map<String, BigDecimal> valorCentavosRestantes = new HashMap<String, BigDecimal>();
		
		//Gera Guias
		for( int parcela = 1; parcela <= quantidadeParcela; parcela++ ) {
			
			if( listaGuiaEmissaoDt_Parceladas == null ) {
				listaGuiaEmissaoDt_Parceladas = new ArrayList<GuiaEmissaoDt>();
			}
			
			GuiaEmissaoDt guiaEmissaoDtParcela = new GuiaEmissaoDt();
			
			guiaEmissaoDtParcela.copiar(guiaEmissaoReferenciaDt);
			
			//Limpar dados da guia
			guiaEmissaoDtParcela.setListaGuiaItemDt(new ArrayList<GuiaItemDt>());
			guiaEmissaoDtParcela.setId(null);
			guiaEmissaoDtParcela.setNumeroGuiaCompleto(null);
			guiaEmissaoDtParcela.setDataVencimento(null);
			guiaEmissaoDtParcela.setDataRecebimento(null);
			guiaEmissaoDtParcela.setId_Usuario(null);
			
			//Adiciona as novas informaï¿½ï¿½es referente ao parcelamento
			guiaEmissaoDtParcela.setIdGuiaReferenciaDescontoParcelamento(guiaEmissaoReferenciaDt.getId());
			guiaEmissaoDtParcela.setTipoGuiaReferenciaDescontoParcelamento(GuiaEmissaoDt.TIPO_GUIA_PARCELADA);
			guiaEmissaoDtParcela.setQuantidadeParcelas(quantidadeParcelas);
			guiaEmissaoDtParcela.setParcelaAtual(String.valueOf(parcela));
			guiaEmissaoDtParcela.setId_GuiaStatus(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
			guiaEmissaoDtParcela.setGuiaModeloDt(guiaEmissaoReferenciaDt.getGuiaModeloDt());
			guiaEmissaoDtParcela.setDataEmissao(null);
			guiaEmissaoDtParcela.setDataVencimento(Funcoes.getDataVencimentoGuiaParcela(parcela));
			guiaEmissaoDtParcela.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
			guiaEmissaoDtParcela.setNumeroGuiaCompleto(new GuiaEmissaoNe().getNumeroGuiaCompleto(guiaEmissaoDtParcela.getNumeroGuia()));
			guiaEmissaoDtParcela.setNumeroGuiaReferenciaDescontoParcelamento(guiaEmissaoReferenciaDt.getNumeroGuiaCompleto());
			guiaEmissaoDtParcela.setId_Usuario(idUsuario);
			guiaEmissaoDtParcela.setValorAcao(Funcoes.FormatarDecimal(guiaEmissaoReferenciaDt.getValorAcao()));
			guiaEmissaoDtParcela.setNovoValorAcao(Funcoes.FormatarDecimal(guiaEmissaoReferenciaDt.getNovoValorAcao()));
			guiaEmissaoDtParcela.setNovoValorAcaoAtualizado(Funcoes.FormatarDecimal(guiaEmissaoReferenciaDt.getNovoValorAcaoAtualizado()));
			
			guiaEmissaoDtParcela.setDataBaseAtualizacao( Funcoes.FormatarDataHora(guiaEmissaoReferenciaDt.getDataBaseAtualizacao()) );
			guiaEmissaoDtParcela.setDataBaseFinalAtualizacao( Funcoes.FormatarDataHora(guiaEmissaoReferenciaDt.getDataBaseFinalAtualizacao()) );
			
			//Adiciona os itens de locomoï¿½ï¿½o somente na primeira parcela
			if( parcela == 1 && listaGuiaItemDt_locomocao != null && !listaGuiaItemDt_locomocao.isEmpty() ) {
				guiaEmissaoDtParcela.setListaGuiaItemDt(listaGuiaItemDt_locomocao);
			}
			
			//Adiciona os itens calculados que nï¿½o sï¿½o de locomoï¿½ï¿½o
			if( listaGuiaItemDt_NAO_locomocao != null && !listaGuiaItemDt_NAO_locomocao.isEmpty() ) {
				for( GuiaItemDt guiaItemDtBase: listaGuiaItemDt_NAO_locomocao ) {
					
					BigDecimal valorCalculadoReferencia = new BigDecimal(Funcoes.StringToDouble(guiaItemDtBase.getValorCalculado()));
					
					BigDecimal valorCalculado = valorCalculadoReferencia.divide(new BigDecimal(quantidadeParcelas),  MathContext.DECIMAL32);
					valorCalculado = valorCalculado.setScale(2, BigDecimal.ROUND_DOWN);
					
					//Inï¿½cio para obter o restante em centavos
					String chave = guiaItemDtBase.getCustaDt().getId();
					
					BigDecimal resto = valorCalculadoReferencia.divide(new BigDecimal(quantidadeParcelas),  MathContext.DECIMAL32).subtract(valorCalculado);
					
					if( valorCentavosRestantes.get(chave) != null ) {
						valorCentavosRestantes.put(chave, resto.add(valorCentavosRestantes.get(chave)));
					}
					else {
						valorCentavosRestantes.put(chave, resto);
					}
					//Fim para obter o restante em centavos
					
					GuiaItemDt guiaItemDtNovo = new GuiaItemDt();
					
					guiaItemDtNovo.setCustaDt(guiaItemDtBase.getCustaDt());
					guiaItemDtNovo.setValorReferencia(guiaItemDtBase.getValorReferencia());
					guiaItemDtNovo.setValorCalculado(valorCalculado.toString());
					guiaItemDtNovo.setQuantidade(guiaItemDtBase.getQuantidade());
					if( guiaItemDtBase.getId_ArrecadacaoCustaGenerica() != null && !guiaItemDtBase.getId_ArrecadacaoCustaGenerica().isEmpty() ) {
						guiaItemDtNovo.setId_ArrecadacaoCustaGenerica(guiaItemDtBase.getId_ArrecadacaoCustaGenerica());
					}
					
					guiaEmissaoDtParcela.getListaGuiaItemDt().add(guiaItemDtNovo);
					
				}
				
			}
			
			listaGuiaEmissaoDt_Parceladas.add(guiaEmissaoDtParcela);
		}
		
//		valorCentavosRestantes.forEach((k, v) -> valorCentavosRestantes.put(k, valorCentavosRestantes.get(k).setScale(2, BigDecimal.ROUND_UP)) );
		for( String key: valorCentavosRestantes.keySet() ) {
			valorCentavosRestantes.put(key, valorCentavosRestantes.get(key).setScale(2, BigDecimal.ROUND_UP));
		}
		
		if( listaGuiaEmissaoDt_Parceladas != null && listaGuiaEmissaoDt_Parceladas.size() > 0 ) {
			for( int parcela = 0; parcela < quantidadeParcela; parcela++ ) {
				GuiaEmissaoDt guiaEmissaoDtParcela = listaGuiaEmissaoDt_Parceladas.get(parcela);
				
				if( guiaEmissaoDtParcela.getListaGuiaItemDt() != null && guiaEmissaoDtParcela.getListaGuiaItemDt().size() > 0 ) {
					for( GuiaItemDt guiaItemDt: guiaEmissaoDtParcela.getListaGuiaItemDt() ) {
						if( guiaItemDt.getCustaDt().getId() != null ) {
							BigDecimal valorCalculado = new BigDecimal(guiaItemDt.getValorCalculado());
							
							if( valorCentavosRestantes != null 
								&& valorCentavosRestantes.get(guiaItemDt.getCustaDt().getId()) != null
								&& valorCentavosRestantes.get(guiaItemDt.getCustaDt().getId()).compareTo(new BigDecimal("0.0")) > 0 ) {
								
								if( parcela != (quantidadeParcela+1) ) {
									valorCalculado = valorCalculado.add(new BigDecimal("0.01"));
									valorCentavosRestantes.put(guiaItemDt.getCustaDt().getId(), valorCentavosRestantes.get(guiaItemDt.getCustaDt().getId()).subtract(new BigDecimal("0.01")));
								}
								else {
									valorCalculado = valorCalculado.add(valorCentavosRestantes.get(guiaItemDt.getCustaDt().getId()));
								}
							}
							
							guiaItemDt.setValorCalculado(valorCalculado.toString());
						}
					}
				}
			}
		}
		
		return listaGuiaEmissaoDt_Parceladas;
	}
	
	
	public GuiaEmissaoDt descontarGuia(String porcentagemDesconto, GuiaEmissaoDt guiaEmissaoReferenciaDt, List<GuiaItemDt> listaGuiaItemDt_locomocao, List<GuiaItemDt> listaGuiaItemDt_NAO_locomocao, String idUsuario) throws Exception {
		GuiaEmissaoDt guiaEmissaoDescontadaDt = new GuiaEmissaoDt();
		
		guiaEmissaoDescontadaDt.copiar(guiaEmissaoReferenciaDt);
		
		//Limpar dados da guia
		guiaEmissaoDescontadaDt.setListaGuiaItemDt(new ArrayList<GuiaItemDt>());
		guiaEmissaoDescontadaDt.setId(null);
		guiaEmissaoDescontadaDt.setNumeroGuiaCompleto(null);
		guiaEmissaoDescontadaDt.setDataVencimento(null);
		guiaEmissaoDescontadaDt.setDataRecebimento(null);
		guiaEmissaoDescontadaDt.setId_Usuario(null);
		
		//Adiciona as novas informaï¿½ï¿½es referente ao desconto
		guiaEmissaoDescontadaDt.setIdGuiaReferenciaDescontoParcelamento(guiaEmissaoReferenciaDt.getId());
		guiaEmissaoDescontadaDt.setTipoGuiaReferenciaDescontoParcelamento(GuiaEmissaoDt.TIPO_GUIA_COM_DESCONTO);
		guiaEmissaoDescontadaDt.setId_GuiaStatus(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		guiaEmissaoDescontadaDt.setPorcentagemDesconto(Funcoes.FormatarDecimal(porcentagemDesconto));
		guiaEmissaoDescontadaDt.setGuiaModeloDt(guiaEmissaoReferenciaDt.getGuiaModeloDt());
		guiaEmissaoDescontadaDt.setDataEmissao(null);
		//Alteraï¿½ï¿½o para a ocorrï¿½ncia 2017/10105
		if( guiaEmissaoReferenciaDt.getGuiaModeloDt() != null 
			&& guiaEmissaoReferenciaDt.getGuiaModeloDt().getId_GuiaTipo() != null 
			&& (
					guiaEmissaoReferenciaDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_FINAL)
					||
					guiaEmissaoReferenciaDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_FINAL_ZERO)
				)
			) {
			
			guiaEmissaoDescontadaDt.setDataVencimento(Funcoes.getDataVencimentoGuia30Dias());
		}
		else {
			guiaEmissaoDescontadaDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
		}
		guiaEmissaoDescontadaDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
		guiaEmissaoDescontadaDt.setNumeroGuiaCompleto(new GuiaEmissaoNe().getNumeroGuiaCompleto(guiaEmissaoDescontadaDt.getNumeroGuia()));
		guiaEmissaoDescontadaDt.setNumeroGuiaReferenciaDescontoParcelamento(guiaEmissaoReferenciaDt.getNumeroGuiaCompleto());
		guiaEmissaoDescontadaDt.setId_Usuario(idUsuario);
		guiaEmissaoDescontadaDt.setValorAcao(Funcoes.FormatarDecimal(guiaEmissaoReferenciaDt.getValorAcao()));
		guiaEmissaoDescontadaDt.setNovoValorAcao(Funcoes.FormatarDecimal(guiaEmissaoReferenciaDt.getNovoValorAcao()));
		guiaEmissaoDescontadaDt.setNovoValorAcaoAtualizado(Funcoes.FormatarDecimal(guiaEmissaoReferenciaDt.getNovoValorAcaoAtualizado()));
		
		guiaEmissaoDescontadaDt.setDataBaseAtualizacao( Funcoes.FormatarDataHora(guiaEmissaoReferenciaDt.getDataBaseAtualizacao()) );
		guiaEmissaoDescontadaDt.setDataBaseFinalAtualizacao( Funcoes.FormatarDataHora(guiaEmissaoReferenciaDt.getDataBaseFinalAtualizacao()) );
		
		//Adiciona os itens de locomoï¿½ï¿½o sem calcular desconto
		if( listaGuiaItemDt_locomocao != null && !listaGuiaItemDt_locomocao.isEmpty() ) {
			guiaEmissaoDescontadaDt.setListaGuiaItemDt(listaGuiaItemDt_locomocao);
		}
		
		//Adiciona os itens calculados que nï¿½o sï¿½o de locomoï¿½ï¿½o
		if( listaGuiaItemDt_NAO_locomocao != null && !listaGuiaItemDt_NAO_locomocao.isEmpty() ) {
			for( GuiaItemDt guiaItemDtBase: listaGuiaItemDt_NAO_locomocao ) {
				
				BigDecimal valorCalculadoReferencia = new BigDecimal(Funcoes.StringToDouble(guiaItemDtBase.getValorCalculado()));
				
				BigDecimal valorPorcentagem = new BigDecimal(Funcoes.StringToDouble(guiaEmissaoDescontadaDt.getPorcentagemDesconto())).divide(new BigDecimal("100"));
				valorPorcentagem = valorPorcentagem.setScale(2, BigDecimal.ROUND_UP);
				
				BigDecimal valorCalculado = valorCalculadoReferencia.subtract(valorCalculadoReferencia.multiply(valorPorcentagem));
				valorCalculado = valorCalculado.setScale(2, BigDecimal.ROUND_UP);
				
				GuiaItemDt guiaItemDtNovo = new GuiaItemDt();
				
				guiaItemDtNovo.setCustaDt(guiaItemDtBase.getCustaDt());
				guiaItemDtNovo.setValorReferencia(guiaItemDtBase.getValorReferencia());
				guiaItemDtNovo.setValorCalculado(valorCalculado.toString());
				guiaItemDtNovo.setQuantidade(guiaItemDtBase.getQuantidade());
				
				guiaEmissaoDescontadaDt.getListaGuiaItemDt().add(guiaItemDtNovo);
			}
		}
		
		return guiaEmissaoDescontadaDt;
	}
	
	/**
	 * Mï¿½todo para verificar se o valor da causa do processo ï¿½ menor que o valor do salï¿½rio mï¿½nimo multiplicado por 40.
	 * Fonte: http://www.tjgo.jus.br/index.php/perguntas-mais-frequentes/185-tribunal/perguntas-mais-frequentes/2289-juizados-especiais-civeis
	 * 
	 * @param String valorCausa
	 * @return boolean
	 */
	public boolean isValorCausaMenorValorMaximoJuizado(String valorCausa) {
		boolean retorno = false;
		
		if( valorCausa != null && !valorCausa.isEmpty() ) {
			
			//Valor Causa
			valorCausa = valorCausa.replace(".", "");
			valorCausa = valorCausa.replace(",", ".");
			BigDecimal valorCausaProcesso = new BigDecimal(valorCausa);
			
			//Valor mï¿½ximo juizado
			BigDecimal valorMaximoJuizado = VALOR_SALARIO_MINIMO.multiply(new BigDecimal("40.0"));
		
			if( valorCausaProcesso.compareTo(valorMaximoJuizado) <= 0 ) {
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para zerar os itens calculados.
	 * 
	 * @param List<GuiaItemDt> listaGuiaItemDt
	 * @throws Exception
	 */
	public void zerarItensCalculados(List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		
		if( listaGuiaItemDt != null && !listaGuiaItemDt.isEmpty() ) {
			
			for( GuiaItemDt guiaItemDt: listaGuiaItemDt ) {
				
				switch( Integer.parseInt(guiaItemDt.getCustaDt().getId()) ) {
					case CustaDt.LOCOMOCAO_PARA_TRIBUNAL :
					case CustaDt.LOCOMOCAO_PARA_OFICIAL :
					case CustaDt.LOCOMOCAO_PARA_AVALIACAO :
					case CustaDt.LOCOMOCAO_PARA_PENHORA :
					case CustaDt.CUSTAS_LOCOMOCAO:
					case CustaDt.OFICIAL_JUSTICA: {
						break;
					}
					default: {
						guiaItemDt.setValorCalculado("0.0");
						break;
					}
				}
			}
			
			this.recalcularTotalGuia(listaGuiaItemDt);
		}
		
	}
}
