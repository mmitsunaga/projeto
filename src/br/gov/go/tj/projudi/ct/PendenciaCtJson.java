package br.gov.go.tj.projudi.ct;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.ne.PendenciaArquivoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class PendenciaCtJson extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8123149800866506878L;

	public int Permissao(){
		return PendenciaDt.CodigoPermissao;
	}
	
	protected boolean verificarPermissao(int inPaginaAtual,  UsuarioNe UsuarioSessao) throws Exception{
		
		if (UsuarioSessao != null){
            if (!UsuarioSessao.getVerificaPermissao(this.Permissao()))
                throw new Exception("<{Sem Permissão para o Usuario, Permissão n.º  " + this.Permissao() + "}> Local Exception: " + this.getClass().getName() + ".verificarPermissao(int inPaginaAtual)");

            if (inPaginaAtual > -1) {
                if ((inPaginaAtual > -1 && inPaginaAtual < 10) || ((inPaginaAtual >= (this.Permissao() * Configuracao.QtdPermissao)) && (inPaginaAtual < (this.Permissao() * Configuracao.QtdPermissao + Configuracao.QtdPermissao - 1)))) {
                    if (!UsuarioSessao.getVerificaPermissao(this.Permissao() * Configuracao.QtdPermissao + inPaginaAtual)){
                        throw new Exception("<{Sem Permissão para o Usuario, Permissão n.º Tipo.}> Local Exception: " + this.getClass().getName() + ".verificarPermissao(int inPaginaAtual)");
                    }
                } else if (!UsuarioSessao.getVerificaPermissao(inPaginaAtual)){
                    throw new Exception("<{Sem Permissão para o Usuario, Permissão n.º Tipo.}> Local Exception: " + this.getClass().getName() + ".verificarPermissao(int inPaginaAtual)");
                }
            }
			
		} else {
			throw new Exception("<{Sem Login.}> Local Exception: " + this.getClass().getName() + ".verificarPermissao(int inPaginaAtual)");
		}  
		
		return true;
	}
	
	/**
	public List getPendencias(String nome, String posicao) throws Exception {
		List Lista = null; 
		List tempList = new ArrayList(); 
		PendenciaNe obNegocio;
		WebContext wc = WebContextFactory.get();
		//testo as permissões retorno erro
		//verificarPermissao(wc);
		obNegocio =  (PendenciaNe)wc.getHttpServletRequest().getSession().getAttribute("Pendenciane");
		if (obNegocio == null )  obNegocio = new PendenciaNe();
		Lista =  obNegocio.consultarDescricao(nome, posicao);
		////System.out.println(Lista);
		tempList.add(Lista);
		tempList.add(Funcoes.StringToLong(posicao));
		tempList.add(obNegocio.getQuantidadePaginas());
		return tempList;
	} */

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception {
        int inFluxo = 0;
        try{
            if(request.getParameter("fluxo") != null){
				inFluxo = Funcoes.StringToInt( request.getParameter("fluxo").toString()); 
			 } 

            switch (paginaatual) {
                case Configuracao.LocalizarDWR:{
                	String stringBusca1 = "";
                	String stringBusca2 = "";
                	String stringBusca3 = "";
                	String stringBusca4 = "";
                	String stringBusca5 = "";
                	String stringBusca6 = "";
                	String stringBusca7 = "";
                	String stringBusca8 = "";
                    switch (inFluxo) {
                    	case 1:
	                    	 if(request.getParameter("nomeBusca") != null){
	                    		 nomebusca = request.getParameter("nomeBusca").toString(); 
	              			 }
	                     	if(request.getParameter("dataInicialInicio") != null){
	             				stringBusca1 = request.getParameter("dataInicialInicio").toString(); 
	             			 }
	                     	if(request.getParameter("dataFinalInicio") != null){
	             				stringBusca2 = request.getParameter("dataFinalInicio").toString(); 
	             			 }
	                     	if(request.getParameter("nomeBusca") != null){
	                     		nomebusca = request.getParameter("nomeBusca").toString(); 
	             			 }
	                     	consultarIntimacoesLidasDistribuicaoJSON(request, response, nomebusca, stringBusca1, stringBusca2, posicaopaginaatual, UsuarioSessao);
	                     	break;
                    	case 2:
	                    	if(request.getParameter("Id_PendenciaTipo") != null){
	                      		stringBusca3  = request.getParameter("Id_PendenciaTipo").toString(); 
	              			}
	                     	consultarPrazosDecorridosUsuarioJSON(request, response, nomebusca, stringBusca3, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 3:
	                    	if(request.getParameter("numeroProcesso") != null){
	                    		 nomebusca = request.getParameter("numeroProcesso").toString(); 
	              			}
	                     	if(request.getParameter("dataInicialInicio") != null){
	             				stringBusca1 = request.getParameter("dataInicialInicio").toString(); 
	             			}
	                     	if(request.getParameter("dataFinalInicio") != null){
	             				stringBusca2 = request.getParameter("dataFinalInicio").toString(); 
	             			}
	                     	if(request.getParameter("Id_PendenciaTipo") != null){
	                     		stringBusca3  = request.getParameter("Id_PendenciaTipo").toString(); 
	             			}
	                     	if(request.getParameter("Id_PendenciaStatus") != null){
	                     		stringBusca4  = request.getParameter("Id_PendenciaStatus").toString(); 
	             			}
	                     	if(request.getParameter("prioridade") != null){
	                     		stringBusca5 = request.getParameter("prioridade").toString(); 
	             			}
	                     	if(request.getParameter("filtroTipo") != null){
	                     		stringBusca6 = request.getParameter("filtroTipo").toString(); 
	             			}
	                     	if(request.getParameter("filtroCivelCriminal") != null){
	                     		stringBusca7 = request.getParameter("filtroCivelCriminal").toString(); 
	             			}
	                     	pendenciasDistribuicaoJSON(request, response, nomebusca, stringBusca3 , stringBusca4 , stringBusca5, stringBusca6, stringBusca7, stringBusca1, stringBusca2, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 4:
	                     	if(request.getParameter("dataInicialInicio") != null){
	             				stringBusca1 = request.getParameter("dataInicialInicio").toString(); 
	             			}
	                     	if(request.getParameter("dataFinalInicio") != null){
	             				stringBusca2 = request.getParameter("dataFinalInicio").toString(); 
	             			}
	                     	if(request.getParameter("Id_PendenciaTipo") != null){
	                     		stringBusca3  = request.getParameter("Id_PendenciaTipo").toString(); 
	             			}
	                     	if(request.getParameter("Id_PendenciaStatus") != null){
	                     		stringBusca4  = request.getParameter("Id_PendenciaStatus").toString(); 
	                     	}
	                     	if(request.getParameter("prioridade") != null){
	                     		stringBusca5 = request.getParameter("prioridade").toString(); 
	             			}
	                     	if(request.getParameter("filtroTipo") != null){
	                     		stringBusca6 = request.getParameter("filtroTipo").toString(); 
	             			}
	                     	if(request.getParameter("numeroProcesso") != null){
	                     		nomebusca = request.getParameter("numeroProcesso").toString(); 
	             			}
	                     	consultarExpedidasServentiaJSON(request, response, nomebusca, stringBusca3 , stringBusca4 , stringBusca5, stringBusca6, stringBusca1, stringBusca2, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 5:
	                     	if(request.getParameter("Id_PendenciaTipo") != null){
	                     		stringBusca1 = request.getParameter("Id_PendenciaTipo").toString(); 
	             			}
	                     	if(request.getParameter("numeroProcesso") != null){
	                     		nomebusca = request.getParameter("numeroProcesso").toString(); 
	             			}
	                     	consultarPrazosDecorridosJSON(request, response, nomebusca, stringBusca1, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 6:
	                     	if(request.getParameter("PendenciaTipo") != null){
	                     		stringBusca1 = request.getParameter("PendenciaTipo").toString(); 
	             			}
	                     	if(request.getParameter("numeroProcesso") != null){
	                     		nomebusca = request.getParameter("numeroProcesso").toString(); 
	             			}
	                     	consultarPrazosADecorrerJSON(request, response, nomebusca, stringBusca1, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 7:
                    		if(request.getParameter("numeroProcesso") != null){
	                      		nomebusca = request.getParameter("numeroProcesso").toString(); 
	              			}
	                      	if(request.getParameter("dataInicialInicio") != null){
	              				stringBusca1 = request.getParameter("dataInicialInicio").toString(); 
	              			}
	                      	if(request.getParameter("dataFinalInicio") != null){
	              				stringBusca2 = request.getParameter("dataFinalInicio").toString(); 
	              			}
	                      	if(request.getParameter("Id_PendenciaTipo") != null){
	                      		stringBusca3  = request.getParameter("Id_PendenciaTipo").toString(); 
	                      	}
	                      	if(request.getParameter("Id_PendenciaStatus") != null){
	                      		stringBusca4  = request.getParameter("Id_PendenciaStatus").toString(); 
	              			}
	                      	if(request.getParameter("prioridade") != null){
	                      		stringBusca5 = request.getParameter("prioridade").toString(); 
	              			}
	                      	if(request.getParameter("filtroTipo") != null){
	                      		stringBusca6 = request.getParameter("filtroTipo").toString(); 
	              			}
	                      	if(request.getParameter("dataInicialFim") != null){
	                      		stringBusca7 = request.getParameter("dataInicialFim").toString(); 
	              			}
	                      	if(request.getParameter("dataFinalFim") != null){
	                      		stringBusca8 = request.getParameter("dataFinalFim").toString(); 
	                      	}
	                      	consultarFinalizadasJSON(request, response, nomebusca, stringBusca3 , stringBusca4 , stringBusca5, stringBusca6, stringBusca1, stringBusca2, stringBusca7, stringBusca8, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 8:
                    		if(request.getParameter("numeroProcesso") != null){
	                       		nomebusca = request.getParameter("numeroProcesso").toString(); 
	               			}
	                     	if(request.getParameter("dataInicialInicio") != null){
	             				stringBusca1 = request.getParameter("dataInicialInicio").toString(); 
	             			}
	                     	if(request.getParameter("dataFinalInicio") != null){
	             				stringBusca2 = request.getParameter("dataFinalInicio").toString(); 
	             			}
	                     	if(request.getParameter("Id_PendenciaTipo") != null){
	                     		stringBusca3  = request.getParameter("Id_PendenciaTipo").toString(); 
	                     	}
	                     	if(request.getParameter("Id_PendenciaStatus") != null){
	                     		stringBusca4  = request.getParameter("Id_PendenciaStatus").toString(); 
	             			}
	                     	if(request.getParameter("prioridade") != null){
	                     		stringBusca5 = request.getParameter("prioridade").toString(); 
	             			}
	                     	if(request.getParameter("filtroTipo") != null){
	                     		stringBusca6 = request.getParameter("filtroTipo").toString(); 
	             			}
	                      	if(request.getParameter("dataInicialFim") != null){
	                      		stringBusca7 = request.getParameter("dataInicialFim").toString(); 
	              			}
	                      	if(request.getParameter("dataFinalFim") != null){
	                      		stringBusca8 = request.getParameter("dataFinalFim").toString(); 
	                      	}
	                     	consultarRespondidasJSON(request, response, nomebusca, stringBusca3, stringBusca4, stringBusca5, stringBusca6, stringBusca1, stringBusca2, stringBusca7, stringBusca8, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 9:
                    		if(request.getParameter("numeroProcesso") != null){
	                       		nomebusca = request.getParameter("numeroProcesso").toString(); 
	               			}
	                      	if(request.getParameter("dataInicialInicio") != null){
	              				stringBusca1 = request.getParameter("dataInicialInicio").toString(); 
	              			}
	                      	if(request.getParameter("dataFinalInicio") != null){
	              				stringBusca2 = request.getParameter("dataFinalInicio").toString(); 
	              			}
	                      	if(request.getParameter("Id_PendenciaTipo") != null){
	                      		stringBusca3  = request.getParameter("Id_PendenciaTipo").toString(); 
	                      	}
	                      	if(request.getParameter("Id_PendenciaStatus") != null){
	                      		stringBusca4  = request.getParameter("Id_PendenciaStatus").toString(); 
	              			}
	                      	if(request.getParameter("prioridade") != null){
	                      		stringBusca5 = request.getParameter("prioridade").toString(); 
	              			}
	                      	if(request.getParameter("filtroTipo") != null){
	                      		stringBusca6 = request.getParameter("filtroTipo").toString(); 
	              			}
	                      	consultarMinhasJSON(request, response, nomebusca, stringBusca3 , stringBusca4 , stringBusca5, stringBusca6, stringBusca1, stringBusca2, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 10:
	                    	if(request.getParameter("numeroProcesso") != null){
	                    		nomebusca = request.getParameter("numeroProcesso").toString(); 
	                		}
	                    	if(request.getParameter("Id_PendenciaTipo") != null){
	                     		stringBusca1 = request.getParameter("Id_PendenciaTipo").toString(); 
	             			}
	                     	consultarPrazosDecorridosUsuarioJSON(request, response, nomebusca, stringBusca1, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 11:
	                    	if(request.getParameter("numeroProcesso") != null){
	                        	nomebusca = request.getParameter("numeroProcesso").toString(); 
	                		}
	                     	if(request.getParameter("dataInicialInicio") != null){
	             				stringBusca1 = request.getParameter("dataInicialInicio").toString(); 
	             			}
	                     	if(request.getParameter("dataFinalInicio") != null){
	             				stringBusca2 = request.getParameter("dataFinalInicio").toString(); 
	             			}
	                     	if(request.getParameter("Id_PendenciaTipo") != null){
	                     		stringBusca3  = request.getParameter("Id_PendenciaTipo").toString(); 
	                     	}
	                     	if(request.getParameter("Id_PendenciaStatus") != null){
	                     		stringBusca4  = request.getParameter("Id_PendenciaStatus").toString(); 
	             			}
	                     	if(request.getParameter("prioridade") != null){
	                     		stringBusca5 = request.getParameter("prioridade").toString(); 
	             			}
	                     	if(request.getParameter("filtroTipo") != null){
	                     		stringBusca6 = request.getParameter("filtroTipo").toString(); 
	             			}
	                      	if(request.getParameter("dataInicialFim") != null){
	                      		stringBusca7 = request.getParameter("dataInicialFim").toString(); 
	              			}
	                      	if(request.getParameter("dataFinalFim") != null){
	                      		stringBusca8 = request.getParameter("dataFinalFim").toString(); 
	                      	}
	                      	consultarRespondidasUsuarioJSON(request, response, nomebusca, stringBusca3, stringBusca4, stringBusca5, stringBusca6, stringBusca1, stringBusca2, stringBusca7, stringBusca8, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 12:
                    		if(request.getParameter("numeroProcesso") != null){
	                    		 nomebusca = request.getParameter("numeroProcesso").toString(); 
	              			}
	                       	if(request.getParameter("dataInicialInicio") != null){
	               				stringBusca1 = request.getParameter("dataInicialInicio").toString(); 
	               			}
	                       	if(request.getParameter("dataFinalInicio") != null){
	               				stringBusca2 = request.getParameter("dataFinalInicio").toString(); 
	               			}
	                       	if(request.getParameter("Id_PendenciaTipo") != null){
	                       		stringBusca3  = request.getParameter("Id_PendenciaTipo").toString(); 
	               			}
	                       	if(request.getParameter("Id_PendenciaStatus") != null){
	                       		stringBusca4  = request.getParameter("Id_PendenciaStatus").toString(); 
	                       	}
	                       	if(request.getParameter("prioridade") != null){
	                       		stringBusca5 = request.getParameter("prioridade").toString(); 
	               			}
	                       	if(request.getParameter("filtroTipo") != null){
	                       		stringBusca6 = request.getParameter("filtroTipo").toString(); 
	               			}
	                       	if(request.getParameter("dataInicialFim") != null){
	                       		stringBusca7 = request.getParameter("dataInicialFim").toString(); 
	               			}
	                       	if(request.getParameter("dataFinalFim") != null){
	                       		stringBusca8 = request.getParameter("dataFinalFim").toString(); 
	                       	}
	                       	consultarFinalizadasUsuarioJSON(request, response, nomebusca, stringBusca3 , stringBusca4 , stringBusca5, stringBusca6, stringBusca1, stringBusca2, stringBusca7, stringBusca8, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 13:
                    		if(request.getParameter("numeroProcesso") != null){
	                    		 nomebusca = request.getParameter("numeroProcesso").toString(); 
	              			}
	                      	if(request.getParameter("dataInicialInicio") != null){
	              				stringBusca1 = request.getParameter("dataInicialInicio").toString(); 
	              			}
	                      	if(request.getParameter("dataFinalInicio") != null){
	              				stringBusca2 = request.getParameter("dataFinalInicio").toString(); 
	              			}
	                      	if(request.getParameter("Id_PendenciaTipo") != null){
	                      		stringBusca3  = request.getParameter("Id_PendenciaTipo").toString(); 
	                      	}
	                      	if(request.getParameter("Id_PendenciaStatus") != null){
	                      		stringBusca4  = request.getParameter("Id_PendenciaStatus").toString(); 
	              			}
	                      	if(request.getParameter("prioridade") != null){
	                      		stringBusca5 = request.getParameter("prioridade").toString(); 
	              			}
	                      	if(request.getParameter("filtroTipo") != null){
	                      		stringBusca6 = request.getParameter("filtroTipo").toString(); 
	              			}
	                      	if(request.getParameter("dataInicialFim") != null){
	                      		stringBusca7 = request.getParameter("dataInicialFim").toString(); 
	              			}
	                      	if(request.getParameter("dataFinalFim") != null){
	                      		stringBusca8 = request.getParameter("dataFinalFim").toString(); 
	              			 }
	                      	consultarPendenciasServentiaFinalizadasJSON(request, response, nomebusca, stringBusca3 , stringBusca4 , stringBusca5, stringBusca6, stringBusca1, stringBusca2, stringBusca7, stringBusca8, posicaopaginaatual, UsuarioSessao);
                            break;
                    	case 14:
	                    	if(request.getParameter("dataInicialFim") != null){
	              				stringBusca1 = request.getParameter("dataInicialFim").toString(); 
	              			}
	                      	if(request.getParameter("dataFinalFim") != null){
	              				stringBusca2 = request.getParameter("dataFinalFim").toString(); 
	              			}
	                     	if(request.getParameter("nomeBusca") != null){
	                     		nomebusca = request.getParameter("nomeBusca").toString(); 
	             			}
	                      	consultarCitacaoFinalizadasResponsavelJSON(request, response, nomebusca, stringBusca1, stringBusca2, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 15:
	                    	if(request.getParameter("nomeBusca") != null){
	                    		 nomebusca = request.getParameter("nomeBusca").toString(); 
	              			}
	                     	if(request.getParameter("dataInicialInicio") != null){
	             				stringBusca1 = request.getParameter("dataInicialInicio").toString(); 
	             			}
	                     	if(request.getParameter("dataFinalInicio") != null){
	             				stringBusca2 = request.getParameter("dataFinalInicio").toString(); 
	             			}
	                     	consultarIntimacoesDistribuidasJSON(request, response, nomebusca, stringBusca1, stringBusca2, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 16:
	                      	if(request.getParameter("id_pendencia") != null){
	              				stringBusca1 = request.getParameter("id_pendencia").toString(); 
	              			}
	                    	consultarPendenciaPaiJSON(request, response, stringBusca1, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 17:
	                       	if(request.getParameter("dataInicialFim") != null){
	               				stringBusca1 = request.getParameter("dataInicialFim").toString(); 
	               			}
	                       	if(request.getParameter("dataFinalFim") != null){
	               				stringBusca2 = request.getParameter("dataFinalFim").toString(); 
	               			}
	                       	if(request.getParameter("nomeBusca") != null){
	                       		nomebusca = request.getParameter("nomeBusca").toString(); 
	               			}
	                       	consultarFinalizadasResponsavelJSON(request, response, nomebusca, stringBusca1, stringBusca2, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 18:
	                       if(request.getParameter("id_pendencia") != null){
	                    	   stringBusca1 = request.getParameter("id_pendencia").toString(); 
	               			}
	                       	consultarArquivosPendenciaJSON(request, response, stringBusca1, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 19:
                    		if(request.getParameter("id_pendenciaArquivo") != null){
                    			stringBusca1 = request.getParameter("id_pendenciaArquivo").toString(); 
	               			}
	                       	if(request.getParameter("hash") != null){
	               				stringBusca2 = request.getParameter("hash").toString(); 
	               			}
	                       	if(request.getParameter("id_ArquivoTipo") != null){
	                       		stringBusca3  = request.getParameter("id_ArquivoTipo").toString(); 
	               			}
	                       	if(request.getParameter("arquivoTipo") != null){
	                       		stringBusca4  = request.getParameter("arquivoTipo").toString(); 
	               			}
	                    	conteudoPendenciaJSON(request, response, stringBusca1, stringBusca2, stringBusca3, stringBusca4, UsuarioSessao);
	                    	break;
                    	case 20:
	                    	if(request.getParameter("id_pendencia") != null){
	            				stringBusca1 = request.getParameter("id_pendencia").toString(); 
	            			}
	                    	consultarArquivosPendenciaFinalizadaJSON(request, response, stringBusca1, posicaopaginaatual, UsuarioSessao);
	                    	break;
	                    case 21:
	                    	if(request.getParameter("numeroProcesso") != null){
	                       		nomebusca = request.getParameter("numeroProcesso").toString(); 
	               			}
	                    	consultarPrazosDecorridosDevolucaoAutosJSON(request, response, nomebusca, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	case 22:
	                       	if(request.getParameter("id_pendencia") != null){
	               				stringBusca1 = request.getParameter("id_pendencia").toString(); 
	               			}
	                       	consultarArquivosAssinadosPendenciaJSON(request, response, stringBusca1, posicaopaginaatual, UsuarioSessao);
	                        break;
                    	default:
                            break;
                     }
                }
                default:
                    return;
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
	
	/**
	 * Consulta as pendencias que estao com o prazo decorrido
	 * @author Ronneesley Moura Teles
	 * @since 23/01/2009 15:01
	 * @param String posicao, posicao da pagina 
	 * @return List
	 * @throws Exception
	 */
	public void consultarPrazosDecorridosJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String tipoPendencia, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao(Configuracao.Curinga7, UsuarioSessao)){
			String stTemp = "";
			
			PendenciaNe pendenciaNe = new PendenciaNe();
			
			if (tipoPendencia != null && !tipoPendencia.equals("") && !tipoPendencia.equals("null")){
				stTemp = pendenciaNe.consultarPrazosDecorridosADecorrerJSON(UsuarioSessao, Funcoes.StringToInt(tipoPendencia), nomeBusca, posicao, false, true);
			}else{
				stTemp = pendenciaNe.consultarPrazosDecorridosADecorrerJSON(UsuarioSessao, null, nomeBusca, posicao, false, true);
			}
			
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	public void consultarPrazosADecorrerJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String tipoPendencia, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao(Configuracao.Curinga7, UsuarioSessao )){
			String stTemp = "";
			PendenciaNe pendenciaNe = new PendenciaNe();
			
			if (tipoPendencia != null && !tipoPendencia.equals("") && !tipoPendencia.equals("null")){
				stTemp = pendenciaNe.consultarPrazosDecorridosADecorrerJSON(UsuarioSessao, Funcoes.StringToInt(tipoPendencia), nomeBusca, posicao, true, true);
			}else{
				stTemp = pendenciaNe.consultarPrazosDecorridosADecorrerJSON(UsuarioSessao, null, nomeBusca, posicao, true, true);
			}
			
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta as pendencias que estao com o prazo decorrido de um usuário
	 * @author Leandro Bernardes
	 * @since 13/08/2009
	 * @param String posicao, posicao da pagina 
	 * @return List
	 * @throws Exception
	 */
	public void consultarPrazosDecorridosUsuarioJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String tipoPendencia, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao(Configuracao.Curinga7, UsuarioSessao)){
			String stTemp = "";
			PendenciaNe pendenciaNe = new PendenciaNe();
			
			if (tipoPendencia != null && !tipoPendencia.equals("") && !tipoPendencia.equals("null")){
				stTemp = pendenciaNe.consultarPrazosDecorridosADecorrerJSON(UsuarioSessao, Funcoes.StringToInt(tipoPendencia), nomeBusca, posicao, false, true);
			}else{
				stTemp = pendenciaNe.consultarPrazosDecorridosADecorrerJSON(UsuarioSessao, null, nomeBusca, posicao, false, true);
			}
			
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}

	/**
	 * Consulta as pendencias que estao finalizadas e o usuario e um responsavel
	 * @author Ronneesley Moura Teles
	 * @since 02/12/2008 11:117
	 * @param String posicao, posicao da pagina 
	 * @return List
	 * @throws Exception
	 */
	public void consultarFinalizadasResponsavelJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String dataInicialFim, String dataFinalFim, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Curinga7, UsuarioSessao)){
			String stTemp = "";
			PendenciaNe pendenciaNe = new PendenciaNe();

			//Configura o tipo de pendencia
			PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
			pendenciaTipoDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.INTIMACAO));
			
			stTemp = pendenciaNe.consultarFinalizadasResponsavelServentiaJSON(UsuarioSessao, pendenciaTipoDt, nomeBusca, null, null, dataInicialFim, dataFinalFim, posicao); 
		
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta as pendencias que estao finalizadas e o usuario e um responsavel
	 * @author Leandro Bernardes
	 * @since 23/07/2009 11:117
	 * @param String posicao, posicao da pagina 
	 * @return List
	 * @throws Exception
	 */

	public void consultarCitacaoFinalizadasResponsavelJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String dataInicialFim, String dataFinalFim, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Curinga7, UsuarioSessao)){
			String stTemp = "";
			PendenciaNe pendenciaNe = new PendenciaNe();

			//Configura o tipo de pendencia
			PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
			pendenciaTipoDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.CARTA_CITACAO));
			
			stTemp = pendenciaNe.consultarFinalizadasResponsavelServentiaJSON(UsuarioSessao, pendenciaTipoDt, nomeBusca, null, null, dataInicialFim, dataFinalFim, posicao); 
		
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta as pendencias que finalizadas da serventia
	 * @author Ronneesley Moura Teles
	 * @since 02/12/2008 11:117
	 * @param String posicao, posicao da pagina 
	 * @return List
	 * @throws Exception
	 */
	public void consultarFinalizadasJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String idPendenciaTipo, String idPendenciaStatus, String stPrioridade, String stFiltroTipo, String dataInicialInicio, String dataFinalInicio, String dataInicialFim, String dataFinalFim, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Curinga6, UsuarioSessao)){
			String stTemp = "";
			boolean prioridade = false;
			if(stPrioridade.equalsIgnoreCase("true")){
				prioridade = true;
			}
			Integer filtroTipo = Integer.parseInt(stFiltroTipo);
			PendenciaNe pendenciaNe = new PendenciaNe();
			
			//Configura o tipo de pendencia
			PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
			pendenciaTipoDt.setId(idPendenciaTipo.equals("null")?"":idPendenciaTipo);	
			
			PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
			pendenciaStatusDt.setId(idPendenciaStatus.equals("null")?"":idPendenciaStatus);
				
			stTemp = pendenciaNe.consultarFinalizadasJSON(UsuarioSessao, null, UsuarioSessao.getUsuarioDt().getId_Serventia(), pendenciaTipoDt, pendenciaStatusDt,	prioridade, filtroTipo, nomeBusca, dataInicialInicio, dataFinalInicio, dataInicialFim, dataFinalFim, posicao);
			
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta as pendencias que o usuario criou e estao finalizadas
	 * @since 04/12/2009 11:117
	 * @param String posicao, posicao da pagina 
	 * @return List
	 * @throws Exception
	 */
	public void consultarFinalizadasUsuarioJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String idPendenciaTipo, String idPendenciaStatus, String stPrioridade, String stFiltroTipo, String dataInicialInicio, String dataFinalInicio, String dataInicialFim, String dataFinalFim, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Curinga7, UsuarioSessao)){
			String stTemp = "";
			boolean prioridade = false;
			if(stPrioridade.equalsIgnoreCase("true")){
				prioridade = true;
			}
			Integer filtroTipo = Integer.parseInt(stFiltroTipo);
			PendenciaNe pendenciaNe = new PendenciaNe();
			
			//Configura o tipo de pendencia
			PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
			pendenciaTipoDt.setId(idPendenciaTipo.equals("null")?"":idPendenciaTipo);	
			
			PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
			pendenciaStatusDt.setId(idPendenciaStatus.equals("null")?"":idPendenciaStatus);
				
			stTemp = pendenciaNe.consultarFinalizadasJSON(UsuarioSessao, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), null, pendenciaTipoDt, pendenciaStatusDt,	prioridade, filtroTipo, nomeBusca, dataInicialInicio, dataFinalInicio, dataInicialFim, dataFinalFim, posicao);
			
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	public void consultarIntimacoesDistribuidasJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String dataInicialInicio, String dataFinalInicio, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Curinga7, UsuarioSessao)){
			String stTemp = "";
			PendenciaNe pendenciaNe = new PendenciaNe();
			
			stTemp = pendenciaNe.consultarIntimacoesDistribuidasJSON(UsuarioSessao, nomeBusca, dataInicialInicio, dataFinalInicio, posicao); 
		
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta as pendencias que respondidas da serventia
	 * @author Leandro Bernardes
	 * @param String posicao, posicao da pagina 
	 * @return List
	 * @throws Exception
	 */
	public void consultarRespondidasJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String idPendenciaTipo, String idPendenciaStatus, String stPrioridade, String stFiltroTipo, String dataInicialInicio, String dataFinalInicio, String dataInicialFim, String dataFinalFim, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Curinga6, UsuarioSessao)){
	  		String stTemp = "";
			boolean prioridade = false;
			if(stPrioridade.equalsIgnoreCase("true")){
				prioridade = true;
			}
			Integer filtroTipo = Integer.parseInt(stFiltroTipo);
			PendenciaNe pendenciaNe = new PendenciaNe();

			//Configura o tipo de pendencia
			PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
			pendenciaTipoDt.setId(idPendenciaTipo.equals("null")?"":idPendenciaTipo);	
			
			PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
			pendenciaStatusDt.setId(idPendenciaStatus.equals("null")?"":idPendenciaStatus);

			stTemp = pendenciaNe.consultarRespondidasServentiaJSON(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt,prioridade, filtroTipo, nomeBusca, dataInicialInicio, dataFinalInicio, dataInicialFim, dataFinalFim, posicao);
			
			try{
 	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta as pendencias que respondidas da serventia
	 * @author Leandro Bernardes
	 * @param String posicao, posicao da pagina 
	 * @return List
	 * @throws Exception
	 */
	public void consultarRespondidasUsuarioJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String idPendenciaTipo, String idPendenciaStatus, String stPrioridade, String stFiltroTipo, String dataInicialInicio, String dataFinalInicio, String dataInicialFim, String dataFinalFim, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Curinga7, UsuarioSessao)){
	  		String stTemp = "";
			boolean prioridade = false;
			if(stPrioridade.equalsIgnoreCase("true")){
				prioridade = true;
			}
			Integer filtroTipo = Integer.parseInt(stFiltroTipo);
			PendenciaNe pendenciaNe = new PendenciaNe();

			//Configura o tipo de pendencia
			PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
			pendenciaTipoDt.setId(idPendenciaTipo.equals("null")?"":idPendenciaTipo);	
			
			PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
			pendenciaStatusDt.setId(idPendenciaStatus.equals("null")?"":idPendenciaStatus);

			stTemp = pendenciaNe.consultarRespondidasUsuarioJSON(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt,prioridade, filtroTipo, nomeBusca, dataInicialInicio, dataFinalInicio, dataInicialFim, dataFinalFim, posicao);
			
			try{
 	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}

	/**
	 * Consulta os arquivos de uma determinada pendencia
	 * @author Ronneesley Moura Teles
	 * @since 02/09/2008 11:07
	 * @param String idPendencia, id da pendencia
	 * @return List
	 * @throws Exception
	 */
	
	public void consultarArquivosPendenciaJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Editar, UsuarioSessao)){
			String stTemp = "";
			PendenciaNe pendenciaNe = new PendenciaNe();

			//verifica se a pendencia ainda não foi movida para pendencias finalizadas
			boolean pendenciaFinalizada = false;
			PendenciaDt pendenciaDt;
			PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
			
			pendenciaDt = pendenciaNe.consultarId(nomeBusca);
			if (!(pendenciaDt != null && pendenciaDt.getId() != null && pendenciaDt.getId().length()>0)){
				pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(nomeBusca);
				pendenciaFinalizada = true;
			}
			
			if (pendenciaFinalizada){
				stTemp = pendenciaArquivoNe.consultarArquivosPendenciaFinalizadaJSON(pendenciaDt, UsuarioSessao, true, false, true, posicao);	
			}else{
				stTemp = pendenciaArquivoNe.consultarArquivosPendenciaJSON(pendenciaDt, UsuarioSessao, true, false, false, true, posicao);
			}
			stTemp = stTemp.replaceAll(".p7s", "");
			
			String stringComparison = "";
			
			boolean isAssinada = true;
        	
        	String pattern1 = "desc11\":\"";
        	String pattern2 = "\",";
        	String regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
        	Pattern pattern = Pattern.compile(regexString);
        	
            String jsonObjects[] = stTemp.split("(?<=})");
            for( int i = 2; i < jsonObjects.length - 1; i++){
            	pattern1 = "desc11\":\"";
            	pattern2 = "\",";
            	regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
            	pattern = Pattern.compile(regexString);
            	
            	isAssinada = true;
            	stringComparison = "desc11\":\"\"";
            	
            	if(jsonObjects[i].contains(stringComparison)){
            		jsonObjects[i] = jsonObjects[i].replaceFirst("desc11\":\"\"", "desc11\":\""+"Arquivo não assinado\"");
            		jsonObjects[i] = jsonObjects[i];
					isAssinada = false;
            	}else{
            		stringComparison = "desc11\":";
                	if(jsonObjects[i].contains(stringComparison)){
                		Matcher matcher = pattern.matcher(jsonObjects[i]);
                		while (matcher.find()) {
                			  String textInBetween1 = matcher.group(1);
                			  pattern1 = "CN=";
                			  pattern2 = ",L";
                			  regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
                			  pattern = Pattern.compile(regexString);
                			  matcher = pattern.matcher(textInBetween1);
                			  while (matcher.find()) {
                				  String textInBetween2 = matcher.group(1);
                				  jsonObjects[i] = jsonObjects[i].replaceFirst(textInBetween1, textInBetween2);
                			  }
                		}
                	}
            	}
            	
            	jsonObjects[i] = jsonObjects[i].replaceFirst("}", ",\"desc12\":\""+(isAssinada ? 1 : 0)+"\"}");
            	pattern1 = "desc2\":\"";
            	pattern2 = "\",";
            	regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
            	pattern = Pattern.compile(regexString);
            	Matcher matcher = pattern.matcher(jsonObjects[i]);
        		
            	while (matcher.find()) {
        			  String textInBetween1 = matcher.group(1);
//                  	if(jsonObjects[i].contains(stringComparison))
                  	if (pendenciaDt.getPendenciaTipoCodigo() != null && pendenciaDt.getPendenciaTipoCodigo().length()>0 && pendenciaDt.getPendenciaTipoCodigo().equals(String.valueOf(PendenciaTipoDt.ELABORACAO_VOTO))){
//                  		arquivoDt.setCodigoTemp(UsuarioSessao.getUsuarioDt().getNome());
                  		jsonObjects[i] = jsonObjects[i].replaceFirst("desc2\":\""+textInBetween1, "desc2\":\""+UsuarioSessao.getUsuarioDt().getNome());
                  	}
        		}
            }
            
            StringBuilder builder = new StringBuilder();
            
            for(String s : jsonObjects) {
                builder.append(s);
            }
            
            stTemp = builder.toString();
			
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta os arquivos de uma determinada pendencia
	 * @param String idPendencia, id da pendencia
	 * @return List
	 * @throws Exception
	 */
	public void consultarArquivosPendenciaFinalizadaJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Editar, UsuarioSessao )){
			String stTemp = "";
			PendenciaDt pendenciaDt;
			PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
			pendenciaDt = new PendenciaDt();
			pendenciaDt.setId(nomeBusca);

			stTemp = pendenciaArquivoNe.consultarArquivosPendenciaFinalizadaJSON(pendenciaDt, UsuarioSessao, true, false, true, posicao);	
			stTemp = stTemp.replaceAll(".p7s", "");
			
			String stringComparison = "";
			
			boolean isAssinada = true;
       	
	       	String pattern1 = "desc11\":\"";
	       	String pattern2 = "\",";
	       	String regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
	       	Pattern pattern = Pattern.compile(regexString);
       	
           String jsonObjects[] = stTemp.split("(?<=})");
           for( int i = 2; i < jsonObjects.length - 1; i++){
           	
           	pattern1 = "desc11\":\"";
           	pattern2 = "\",";
           	regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
           	pattern = Pattern.compile(regexString);
           	
           	isAssinada = true;
           	stringComparison = "desc11\":\"\"";
           	if(jsonObjects[i].contains(stringComparison)){
           		jsonObjects[i] = jsonObjects[i].replaceFirst("desc11\":\"\"", "desc11\":\""+"Arquivo não assinado\"");
           		jsonObjects[i] = jsonObjects[i];
					isAssinada = false;
           	}else{
           		stringComparison = "desc11\":";
               	if(jsonObjects[i].contains(stringComparison)){
               		Matcher matcher = pattern.matcher(jsonObjects[i]);
               		while (matcher.find()) {
               			  String textInBetween1 = matcher.group(1);
               			  pattern1 = "CN=";
               			  pattern2 = ",L";
               			  regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
               			  pattern = Pattern.compile(regexString);
               			  matcher = pattern.matcher(textInBetween1);
               			  while (matcher.find()) {
               				  String textInBetween2 = matcher.group(1);
               				  jsonObjects[i] = jsonObjects[i].replaceFirst(textInBetween1, textInBetween2);
               			  }
               		}
               	}
           	}
           	jsonObjects[i] = jsonObjects[i].replaceFirst("}", ",\"desc12\":\""+(isAssinada ? 1 : 0)+"\"}");

           	pattern1 = "desc2\":\"";
           	pattern2 = "\",";
           	regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
           	pattern = Pattern.compile(regexString);
           	Matcher matcher = pattern.matcher(jsonObjects[i]);
       		while (matcher.find()) {
       			  String textInBetween1 = matcher.group(1);
//                 	if(jsonObjects[i].contains(stringComparison))
                 	if (pendenciaDt.getPendenciaTipoCodigo() != null && pendenciaDt.getPendenciaTipoCodigo().length()>0 && pendenciaDt.getPendenciaTipoCodigo().equals(String.valueOf(PendenciaTipoDt.ELABORACAO_VOTO))){
//                 		arquivoDt.setCodigoTemp(UsuarioSessao.getUsuarioDt().getNome());
                 		jsonObjects[i] = jsonObjects[i].replaceFirst("desc2\":\""+textInBetween1, "desc2\":\""+UsuarioSessao.getUsuarioDt().getNome());
                 	}
       		}
           }           
           StringBuilder builder = new StringBuilder();
           for(String s : jsonObjects) {
               builder.append(s);
           }
           stTemp = builder.toString();

			
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta as pendencias que o usuario criou
	 * @author Ronneesley Moura Teles
	 * @since 13/11/2008 09:15
	 * @param String posicao, posicao da pagina
	 * @return List
	 * @throws Exception
	 */
	public void consultarExpedidasServentiaJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String idPendenciaTipo, String idPendenciaStatus, String stPrioridade, String stFiltroTipo, String dataInicialInicio, String dataFinalInicio, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Curinga6, UsuarioSessao)){
			String stTemp = "";
			boolean prioridade = false;
			if(stPrioridade.equalsIgnoreCase("true")){
				prioridade = true;
			}
			Integer filtroTipo = Integer.parseInt(stFiltroTipo);
			PendenciaNe pendenciaNe = new PendenciaNe();

			//Configura o tipo de pendencia
			PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
			pendenciaTipoDt.setId(idPendenciaTipo.equals("null")?"":idPendenciaTipo);	
			PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
			pendenciaStatusDt.setId(idPendenciaStatus.equals("null")?"":idPendenciaStatus);
			
			stTemp = pendenciaNe.consultarExpedidasServentiaJSON(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, nomeBusca, dataInicialInicio, dataFinalInicio, posicao); 
		
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta as pendencias que o usuario criou
	 * @author Ronneesley Moura Teles
	 * @since 13/11/2008 09:15
	 * @param String posicao, posicao da pagina
	 * @return List
	 * @throws Exception
	 */
	public void consultarMinhasJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String idPendenciaTipo, String idPendenciaStatus, String stPrioridade, String stFiltroTipo, String dataInicialInicio, String dataFinalInicio, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Curinga7, UsuarioSessao)){
			String stTemp = "";
			boolean prioridade = false;
			if(stPrioridade.equalsIgnoreCase("true")){
				prioridade = true;
			}
			Integer filtroTipo = Integer.parseInt(stFiltroTipo);
			PendenciaNe pendenciaNe = new PendenciaNe();

			//Configura o tipo de pendencia
			PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
			pendenciaTipoDt.setId(idPendenciaTipo.equals("null")?"":idPendenciaTipo);	
			PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
			pendenciaStatusDt.setId(idPendenciaStatus.equals("null")?"":idPendenciaStatus);
			
			stTemp = pendenciaNe.consultarMinhasJSON(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, nomeBusca, dataInicialInicio, dataFinalInicio, posicao); 
		
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta as pendencias finalizadas da servendtia
	 * @author Leandro Bernardes
	 * @since 25/05/2009 11:117
	 * @param String posicao, posicao da pagina 
	 * @return List
	 * @throws Exception
	 */
	public void consultarPendenciasServentiaFinalizadasJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String idPendenciaTipo, String idPendenciaStatus, String stPrioridade, String stFiltroTipo, String dataInicialInicio, String dataFinalInicio, String dataInicialFim, String dataFinalFim, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Curinga7, UsuarioSessao)){
			String stTemp = "";
			boolean prioridade = false;
			if(stPrioridade.equalsIgnoreCase("true")){
				prioridade = true;
			}
			Integer filtroTipo = Integer.parseInt(stFiltroTipo);
			PendenciaNe pendenciaNe = new PendenciaNe();
			
			//Configura o tipo de pendencia
			PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
			pendenciaTipoDt.setId(idPendenciaTipo.equals("null")?"":idPendenciaTipo);	
			
			PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
			pendenciaStatusDt.setId(idPendenciaStatus.equals("null")?"":idPendenciaStatus);
				
			stTemp = pendenciaNe.consultarPendenciasFechadasJSON(UsuarioSessao, null, UsuarioSessao.getUsuarioDt().getId_Serventia(), pendenciaTipoDt, pendenciaStatusDt,	prioridade, filtroTipo, nomeBusca, dataInicialInicio, dataFinalInicio, dataInicialFim, dataFinalFim, posicao);
			
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	
	public void consultarIntimacoesLidasDistribuicaoJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String dataInicialInicio, String dataFinalInicio, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao( Configuracao.Curinga7, UsuarioSessao)){
			String stTemp = "";
			PendenciaNe pendenciaNe = new PendenciaNe();
			stTemp = pendenciaNe.consultarIntimacoesLidasDistribuicaoJSON(UsuarioSessao, nomeBusca, dataInicialInicio, dataFinalInicio, posicao); 
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	
	/**
	 * Consulta a pendencia pai de uma pendencia
	 * @author Ronneesley Moura Teles
	 * @since 27/08/2008 17:34
	 * @param String id_Pendencia, id da pendencia pai
	 * @return PendenciaDt
	 * @throws Exception
	 */
	public void consultarPendenciaPaiJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao(Configuracao.Editar, UsuarioSessao)){
			String stTemp = "";
			PendenciaNe pendenciaNe = new PendenciaNe();

			PendenciaDt dtTemp = pendenciaNe.consultarPendenciaPai(nomeBusca);
//			stTemp = pendenciaNe.consultarPendenciaPaiJSON(nomeBusca);
			if(dtTemp == null){
//				stTemp = "[{\"id\":\"" +"-1"+ "\",\"desc1\":\"" +"null"+ "\",\"desc2\":\"" +"null"+ "\"}]";
				stTemp = "{}";
			}else{
				stTemp = "[{\"id\":\"" +dtTemp.getId()+ "\",\"desc1\":\"" +dtTemp.getPendenciaTipo()+ "\",\"desc2\":\"" +dtTemp.getPendenciaStatus()+ "\"}]";
			}
		
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	
	/**
	 * Consulta o conteudo de um arquivo de uma pendencia
	 * ATENCAO: somente sera retornado o conteudo de pendencias nao assinadas
	 * @author Ronneesley Moura Teles
	 * @since 30/09/2008 09:58
	 * @param String id_pendencia, id da pendencia que deseja 
	 * @return String
	 * @throws Exception
	 */
	public void conteudoPendenciaJSON(HttpServletRequest request, HttpServletResponse response, String id_pendenciaArquivo, String hash, String id_ArquivoTipo, String arquivoTipo, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao(Configuracao.Editar, UsuarioSessao)){
			String stTemp = "";
			String stConteudo = "";
			PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
			
			if (UsuarioSessao.VerificarCodigoHash(id_pendenciaArquivo, hash)){
				ArquivoDt arquivoDt =  pendenciaArquivoNe.conteudoArquivoNaoAssinado(id_pendenciaArquivo);
				stConteudo = arquivoDt.getArquivo();
			} else {
				throw new Exception("Acesso negado!");
			}

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("desc1", stConteudo);
			stTemp = jsonObj.toString();
			
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
			
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta os arquivos assinados de uma determinada pendencia
	 * @author Ronneesley Moura Teles
	 * @since 21/01/2009 16:17
	 * @param idPendencia id da pendencia
	 * @return lista de arquivos assinados
	 * @throws Exception
	 */
	public void consultarArquivosAssinadosPendenciaJSON(HttpServletRequest request, HttpServletResponse response, String idPendencia, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao(Configuracao.Editar, UsuarioSessao )){
			String stTemp = "";
			
			PendenciaDt pendenciaDt = new PendenciaDt();
			pendenciaDt.setId(idPendencia);
			PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();

			stTemp = pendenciaArquivoNe.consultarArquivosPendenciaJSON(pendenciaDt, UsuarioSessao, true, true, false, true, posicao);
			stTemp = stTemp.replaceAll(".p7s", "");
			String stringComparison = "";
			
        	String pattern1 = "desc11\":\"";
        	String pattern2 = "\",";
        	String regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
        	Pattern pattern = Pattern.compile(regexString);
        	
            String jsonObjects[] = stTemp.split("(?<=})");
            for( int i = 2; i < jsonObjects.length - 1; i++){
            	
            	pattern1 = "desc11\":\"";
            	pattern2 = "\",";
            	regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
            	pattern = Pattern.compile(regexString);
            	
        		stringComparison = "desc11\":";
            	if(jsonObjects[i].contains(stringComparison)){
            		Matcher matcher = pattern.matcher(jsonObjects[i]);
            		while (matcher.find()) {
            			  String textInBetween1 = matcher.group(1);
            			  pattern1 = "CN=";
            			  pattern2 = ",L";
            			  regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
            			  pattern = Pattern.compile(regexString);
            			  matcher = pattern.matcher(textInBetween1);
            			  while (matcher.find()) {
            				  String textInBetween2 = matcher.group(1);
            				  jsonObjects[i] = jsonObjects[i].replaceFirst(textInBetween1, textInBetween2);
            			  }
            		}
            	}
            } 
            
            StringBuilder builder = new StringBuilder();
            for(String s : jsonObjects) {
                builder.append(s);
            }
            
            stTemp = builder.toString();
			
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Retorna as pendencias de distribuicao [Paginacao]
	 * @author Ronneesley Moura Teles
	 * @since 28/08/2008 16:47 
	 * @param String nomeBusca, numero do processo
	 * @param idPendenciaTipo id do tipo da pendencia
	 * @param idPendenciaStatus id do status da pendencia
	 * @param boolean prioridade, prioridade na busca
	 * @param String dataInicialInicio, data inicial para o inicio
	 * @param String dataFinalInicio, data final para o inicio
	 * @param String posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public void pendenciasDistribuicaoJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String idPendenciaTipo, String idPendenciaStatus, String stPrioridade, String stFiltroTipo, String stFiltroCivelCriminal, String dataInicialInicio, String dataFinalInicio, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao(Configuracao.Curinga9, UsuarioSessao)){
			String stTemp = "";
			boolean prioridade = false;
			Integer filtroTipo;
			Integer filtroCivelCriminal;
			
			if(stPrioridade != null && (stPrioridade.equalsIgnoreCase("1")|| stPrioridade.equalsIgnoreCase("true") )){
				prioridade = true;
			}
			
			if (stFiltroTipo != null && stFiltroTipo.length()>0){
				filtroTipo = Integer.parseInt(stFiltroTipo);
			} else{
				filtroTipo = null;
			}
			
			if (stFiltroCivelCriminal != null && stFiltroCivelCriminal.length()>0){
				filtroCivelCriminal = Integer.parseInt(stFiltroCivelCriminal);
			} else{
				filtroCivelCriminal = null;
			}
			
			PendenciaNe pendenciaNe = new PendenciaNe();
			//Configura o tipo de pendencia
			PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
			pendenciaTipoDt.setId(idPendenciaTipo.equals("null")?"":idPendenciaTipo);	
			PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
			pendenciaStatusDt.setId(idPendenciaStatus.equals("null")?"":idPendenciaStatus);
			
			stTemp = pendenciaNe.consultarAbertasJSON(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, filtroCivelCriminal, nomeBusca, dataInicialInicio, dataFinalInicio, posicao); 
		
			try{
	             response.setContentType("text/x-json");
	             response.getOutputStream().write(stTemp.toString().getBytes());
	             response.flushBuffer();
	         } catch(Exception e ){
	             throw new Exception("Erro!");
	         }
		} else{
			throw new Exception("Acesso negado");
		}
	}
	
	/**
	 * Consulta as pendencias que estao com o prazo decorrido solicitação de carga
	 * @author Ronneesley Moura Teles
	 * @since 23/01/2009 15:01
	 * @param String posicao, posicao da pagina 
	 * @return List
	 * @throws Exception
	 */
	public void consultarPrazosDecorridosDevolucaoAutosJSON(HttpServletRequest request, HttpServletResponse response, String nomeBusca, String posicao, UsuarioNe UsuarioSessao) throws Exception {
		if (this.verificarPermissao(Configuracao.Curinga6, UsuarioSessao)){
			String stTemp = "";
			PendenciaNe pendenciaNe = new PendenciaNe();
			stTemp = pendenciaNe.consultarPrazosDecorridosDevolucaoAutosJSON(UsuarioSessao.getUsuarioDt().getId_Serventia(), nomeBusca, posicao);
			try{
				response.setContentType("text/x-json");
	            response.getOutputStream().write(stTemp.toString().getBytes());
	            response.flushBuffer();
	         } catch(Exception e ){
	            throw new Exception("Erro!");
	         }
		} else
			throw new Exception("Acesso negado");
	}

} 
