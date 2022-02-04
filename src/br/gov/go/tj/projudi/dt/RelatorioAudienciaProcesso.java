package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.axis.utils.StringUtils;

public class RelatorioAudienciaProcesso implements Serializable {

	private static final long serialVersionUID = -2331231216125654498L;
	
	private String processoTipo;
	private String processoNumero;
	private String comarca;
	private String relator;
	private List promoventes;
	private List promovidos;
	private String procuradorJustica;
	private String descricaoPoloAtivo;
	private String descricaoPoloPassivo;
	
	private String idProcesso;
	private String idAudiProc;
	private String dataAgendada; 
	private String serventia;
	private String nomePoloAtivo;
	private String advogadoPoloAtivo = "";
	private String nomePoloPassivo;
	private String advogadoPoloPassivo = "";
	
	private String tipoSessao = "";
	private byte[] textoAtaAdiadoIniciado = null;
	
	private List partesOrdemProcessoPoloAtivo;
	private List partesOrdemProcessoPoloPassivo;
	
	private HashMap<Integer, String> poloAtivoRecSec; // lrcampos - 13/06/2019 Mudança para separar polo ativo por ordem de parte
	private HashMap<Integer, String> poloPassivoRecSec;  // lrcampos - 13/06/2019 - Mudança para separar polo passivo por ordem de parte
	private HashMap<Integer, String> advogadoPoloAtivoRecSec; // lrcampos - 13/06/2019 Mudança para separar advogado polo ativo por ordem de parte
	private HashMap<Integer, String> advogadoPoloPassivoRecSec;  // lrcampos - 13/06/2019 Mudança para separar advogado polo ativo por ordem de parte
	private boolean recursoSecundario; // lrcampos - 13/06/2019 - Adicionar variavel para saber se o processo possui recurso secundário.
	
	
	
	private HashMap<Integer, List<ProcessoParteDt>> poloAtivoOrdem; 	// jvosantos - 26/06/2019 12:37 - Criar variáveis e metodos para obter lista de polos por ordem
	private HashMap<Integer, List<ProcessoParteDt>> poloPassivoOrdem; 	// jvosantos - 26/06/2019 12:37 - Criar variáveis e metodos para obter lista de polos por ordem
	private Integer ordemMaxima;										// jvosantos - 26/06/2019 15:51 - Criar variável para armazenar o maior valor de ordem
	
	private List<RelatorioAudienciaProcessoParteAdvogado> listaPromoventes;		// jvosantos - 28/06/2019 17:07 - Criar variveis para armazenar as partes de processos sem recursos
	private List<RelatorioAudienciaProcessoParteAdvogado> listaPromovidos;		// jvosantos - 28/06/2019 17:07 - Criar variveis para armazenar as partes de processos sem recursos
	
	private List<RelatorioAudienciaProcessoParteAdvogado> poloAtivoRecurso;			// jvosantos - 28/06/2019 17:39 - Criar variveis para armazenar as partes de processos com recursos
	private List<RelatorioAudienciaProcessoParteAdvogado> poloPassivoRecurso;		// jvosantos - 28/06/2019 17:39 - Criar variveis para armazenar as partes de processos com recursos
	
	public RelatorioAudienciaProcesso(){ setOrdemMaxima(0); }
	
	
	public String getProcessoTipo() {
		return processoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		this.processoTipo = processoTipo;
	}

	public String getProcessoNumero() {
		return processoNumero;
	}

	public void setProcessoNumero(String processoNumero) {
		this.processoNumero = processoNumero;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}

	public String getRelator() {
		return relator;
	}

	public void setRelator(String relator) {
		this.relator = relator;
	}

	public List getPromoventes() {
		return promoventes;
	}

	@SuppressWarnings("unchecked")
	public void setPromoventes(List promoventes) {
		this.promoventes = promoventes;
		this.promoventes.sort(new Comparator<RelatorioAudienciaProcessoParteAdvogado>() {
			@Override
			public int compare(RelatorioAudienciaProcessoParteAdvogado o1, RelatorioAudienciaProcessoParteAdvogado o2) {
				return o1.getNomeParte().compareTo(o2.getNomeParte());
			}
		});
	}

	public List getPromovidos() {
		return promovidos;
	}

	@SuppressWarnings("unchecked")
	public void setPromovidos(List promovidos) {
		this.promovidos = promovidos;
		this.promovidos.sort(new Comparator<RelatorioAudienciaProcessoParteAdvogado>() {
			@Override
			public int compare(RelatorioAudienciaProcessoParteAdvogado o1, RelatorioAudienciaProcessoParteAdvogado o2) {
				return o1.getNomeParte().compareTo(o2.getNomeParte());
			}
		});
	}

	public String getProcuradorJustica() {
		return procuradorJustica;
	}

	public void setProcuradorJustica(String procuradorJustica) {
		this.procuradorJustica = procuradorJustica;
	}	
	
	public String getDescricaoPoloAtivo() {
		return descricaoPoloAtivo;
	}


	public void setDescricaoPoloAtivo(String descricaoPoloAtivo) {
		this.descricaoPoloAtivo = descricaoPoloAtivo;
	}


	public String getDescricaoPoloPassivo() {
		return descricaoPoloPassivo;
	}


	public void setDescricaoPoloPassivo(String descricaoPoloPassivo) {
		this.descricaoPoloPassivo = descricaoPoloPassivo;
	}


	public String getIdProcesso() {
		return idProcesso;
	}


	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}


	public String getDataAgendada() {
		return dataAgendada;
	}


	public void setDataAgendada(String dataAgendada) {
		this.dataAgendada = dataAgendada;
	}


	public String getServentia() {
		return serventia;
	}


	public void setServentia(String serventia) {
		this.serventia = serventia;
	}


	public String getNomePoloAtivo() {
		return nomePoloAtivo;
	}


	public void setNomePoloAtivo(String nomePoloAtivo) {
		this.nomePoloAtivo = nomePoloAtivo;
	}


	public String getAdvogadoPoloAtivo() {
		return advogadoPoloAtivo;
	}


	public void setAdvogadoPoloAtivo(String advogadoPoloAtivo) {
		this.advogadoPoloAtivo = advogadoPoloAtivo;
	}


	public String getNomePoloPassivo() {
		return nomePoloPassivo;
	}


	public void setNomePoloPassivo(String nomePoloPassivo) {
		this.nomePoloPassivo = nomePoloPassivo;
	}


	public String getAdvogadoPoloPassivo() {
		return advogadoPoloPassivo;
	}


	public void setAdvogadoPoloPassivo(String advogadoPoloPassivo) {
		this.advogadoPoloPassivo = advogadoPoloPassivo;
	}
	
	public void addAdvogadoPoloAtivo(String nomeNovo, String oabNovo, String complementoNovo) {
		if (nomeNovo != null && !nomeNovo.equalsIgnoreCase("") && !this.advogadoPoloAtivo.contains(nomeNovo)) {
			if(this.advogadoPoloAtivo.length() > 0) {
				this.advogadoPoloAtivo += ", ";
			}
			this.advogadoPoloAtivo += nomeNovo + " - " + oabNovo + "/" + complementoNovo;
		}
	}
	
	public void addAdvogadoPoloPassivo(String nomeNovo, String oabNovo, String complementoNovo) {
		//leo
		if (nomeNovo != null && !nomeNovo.equalsIgnoreCase("") && !(this.advogadoPoloPassivo.contains(nomeNovo) && this.advogadoPoloPassivo.contains(oabNovo))) {
			if(this.advogadoPoloPassivo.length() > 0) {
				this.advogadoPoloPassivo += ", ";
			}
			this.advogadoPoloPassivo += nomeNovo + " - " + oabNovo + "/" + complementoNovo;
		}
	}


	public byte[] getTextoAtaAdiadoIniciado() {
		return textoAtaAdiadoIniciado;
	}


	public void setTextoAtaAdiadoIniciado(byte[] textoAtaAdiadoIniciado) {
		this.textoAtaAdiadoIniciado = textoAtaAdiadoIniciado;
	}


	public String getTipoSessao() {
		return tipoSessao;
	}


	public void setTipoSessao(String tipoSessao) {
		if (tipoSessao == null) tipoSessao = AudienciaProcessoDt.STATUS_JULGAMENTO_PAUTA_DIA;
		this.tipoSessao = tipoSessao;
	}


	public String getIdAudiProc() {
		return idAudiProc;
	}


	public void setIdAudiProc(String idAudiProc) {
		this.idAudiProc = idAudiProc;
	}


	public List getPartesOrdemProcessoPoloAtivo() {
		return partesOrdemProcessoPoloAtivo;
	}


	public void setPartesOrdemProcessoPoloAtivo(List partesOrdemProcessoPoloAtivo) {
		this.partesOrdemProcessoPoloAtivo = partesOrdemProcessoPoloAtivo;
	}


	public List getPartesOrdemProcessoPoloPassivo() {
		return partesOrdemProcessoPoloPassivo;
	}


	public void setPartesOrdemProcessoPoloPassivo(List partesOrdemProcessoPoloPassivo) {
		this.partesOrdemProcessoPoloPassivo = partesOrdemProcessoPoloPassivo;
	}


	public HashMap<Integer, String> getPoloPassivoRecSec() {
		return poloPassivoRecSec;
	}


	public void setPoloPassivoRecSec(HashMap<Integer, String> poloPassivoRecSec) {
		this.poloPassivoRecSec = poloPassivoRecSec;
	}


	public HashMap<Integer, String> getPoloAtivoRecSec() {
		return poloAtivoRecSec;
	}


	public void setPoloAtivoRecSec(HashMap<Integer, String> poloAtivoRecSec) {
		this.poloAtivoRecSec = poloAtivoRecSec;
	}


	public HashMap<Integer, String> getAdvogadoPoloAtivoRecSec() {
		return advogadoPoloAtivoRecSec;
	}


	public void setAdvogadoPoloAtivoRecSec(HashMap<Integer, String> advogadoPoloAtivoRecSec) {
		this.advogadoPoloAtivoRecSec = advogadoPoloAtivoRecSec;
	}


	public HashMap<Integer, String> getAdvogadoPoloPassivoRecSec() {
		return advogadoPoloPassivoRecSec;
	}


	public void setAdvogadoPoloPassivoRecSec(HashMap<Integer, String> advogadoPoloPassivoRecSec) {
		this.advogadoPoloPassivoRecSec = advogadoPoloPassivoRecSec;
	}


	public boolean isRecursoSecundario() {
		return recursoSecundario;
	}


	public void setRecursoSecundario(boolean recursoSecundario) {
		this.recursoSecundario = recursoSecundario;
	}

	public String getListaNomePoloAtivo() {
		String nome = "";
		
		for(RelatorioAudienciaProcessoParteAdvogado n : (List<RelatorioAudienciaProcessoParteAdvogado>) this.getPromoventes()) {
			nome += n.getNomeParte().replace("*", "") + ", ";
		}
		
		if(nome.length() > 0)
			nome = nome.substring(0, nome.length() - 2);
		
		return nome;
	}

	public String getListaNomePoloPassivo() {
		String nome = "";
		
		for(RelatorioAudienciaProcessoParteAdvogado n : (List<RelatorioAudienciaProcessoParteAdvogado>) this.getPromovidos()) {
			nome += n.getNomeParte().replace("*", "") + ", ";
		}
		
		if(nome.length() > 0)
			nome = nome.substring(0, nome.length() - 2);
		
		return nome;
	}

	public String getPolosAgrupados() {
		String res = "";
		
		
		// Pega o Recurso Secundario
		for(int i = 1; i <= ordemMaxima; ++i) {
			if(poloAtivoOrdem != null && poloAtivoOrdem.containsKey(i) && !poloAtivoOrdem.get(i).isEmpty()) {
				
				String res1 = "";
				ArrayList<String> resTemp = new ArrayList<String>();
				
				int j = 0;
				
				for(j = 0; j < poloAtivoOrdem.get(i).size(); ++j) {
					ProcessoParteDt x = poloAtivoOrdem.get(i).get(j);
					resTemp.add(x.getNome().toUpperCase());
				}
				
				resTemp.sort(new Comparator<String>() {
			        @Override
			        public int compare(String a, String b) {
			            return  a.compareTo(b);
			        }
			    });
				
				for(j = 0; j < resTemp.size(); ++j) {
					res1 += resTemp.get(j) + ", ";
				}
				
				res += i + "º " + descricaoPoloAtivo.toUpperCase() + "(S): " + res1 + '\n';
				
				// jvosantos - 11/07/2019 12:34 - Refatoração do código usando lambda do Java 8 para código Java 7 conforme novas orientações da DSI
//				res += i + "º " + descricaoPoloAtivo.toUpperCase() + "(S): " + String.join(", ", poloAtivoOrdem.get(i).stream().map(x -> x.getNome().toUpperCase()).sorted().collect(Collectors.toCollection(ArrayList::new)))+"\n";
			}
				
			if(poloPassivoOrdem != null && poloPassivoOrdem.containsKey(i) && !poloPassivoOrdem.get(i).isEmpty()) {
				
				String res1 = "";
				ArrayList<String> resTemp = new ArrayList<String>();
				
				int j = 0;
				
				for(j = 0; j < poloPassivoOrdem.get(i).size(); ++j) {
					ProcessoParteDt x = poloPassivoOrdem.get(i).get(j);
					resTemp.add(x.getNome().toUpperCase());
				}
				
				resTemp.sort(new Comparator<String>() {
			        @Override
			        public int compare(String a, String b) {
			            return  a.compareTo(b);
			        }
			    });
				
				for(j = 0; j < resTemp.size(); ++j) {
					res1 += resTemp.get(j) + ", ";
				}
				
				res += i + "º " + descricaoPoloPassivo.toUpperCase() + "(S): " + res1 + '\n';

				// jvosantos - 11/07/2019 12:34 - Refatoração do código usando lambda do Java 8 para código Java 7 conforme novas orientações da DSI
				//res += i + "º " + descricaoPoloPassivo.toUpperCase() + "(S): " + String.join(", ", (Iterable<? extends CharSequence>) poloPassivoOrdem.get(i).stream().map(x -> x.getNome().toUpperCase()).sorted().collect(Collectors.toCollection(ArrayList::new)))+"\n";
			}
		}
		
		// Pega os polos do Recurso
		if(StringUtils.isEmpty(res)) {
			if(poloAtivoRecurso != null && !poloAtivoRecurso.isEmpty()) {
				
				String res1 = "";
				ArrayList<String> resTemp = new ArrayList<String>();
				
				int j = 0;
				
				for(j = 0; j < poloAtivoRecurso.size(); ++j) {
					RelatorioAudienciaProcessoParteAdvogado x = poloAtivoRecurso.get(j);
					resTemp.add(x.getNomeParte().substring(1).toUpperCase());
				}
				
				resTemp.sort(new Comparator<String>() {
			        @Override
			        public int compare(String a, String b) {
			            return  a.compareTo(b);
			        }
			    });
				
				for(j = 0; j < resTemp.size(); ++j) {
					res1 += resTemp.get(j) + ", ";
				}
				
				res += descricaoPoloAtivo.toUpperCase() + "(S): " + res1 + '\n';
				
				// jvosantos - 11/07/2019 12:34 - Refatoração do código usando lambda do Java 8 para código Java 7 conforme novas orientações da DSI
				//res += descricaoPoloAtivo.toUpperCase() + "(S): " + String.join(", ", (Iterable<? extends CharSequence>) poloAtivoRecurso.stream().map(x -> x.getNomeParte().substring(1).toUpperCase()).sorted().collect(Collectors.toCollection(ArrayList::new))) + "\n";
			}
			if(poloPassivoRecurso != null && !poloPassivoRecurso.isEmpty()) {
				
				String res1 = "";
				ArrayList<String> resTemp = new ArrayList<String>();
				
				int j = 0;
				
				for(j = 0; j < poloPassivoRecurso.size(); ++j) {
					RelatorioAudienciaProcessoParteAdvogado x = poloPassivoRecurso.get(j);
					resTemp.add(x.getNomeParte().substring(1).toUpperCase());
				}
				
				resTemp.sort(new Comparator<String>() {
			        @Override
			        public int compare(String a, String b) {
			            return  a.compareTo(b);
			        }
			    });
				
				for(j = 0; j < resTemp.size(); ++j) {
					res1 += resTemp.get(j) + ", ";
				}
				
				res += descricaoPoloPassivo.toUpperCase() + "(S): " + res1 + '\n';
				
				// jvosantos - 11/07/2019 12:34 - Refatoração do código usando lambda do Java 8 para código Java 7 conforme novas orientações da DSI
				//res += descricaoPoloPassivo.toUpperCase() + "(S): " + String.join(", ", (Iterable<? extends CharSequence>) poloPassivoRecurso.stream().map(x -> x.getNomeParte().substring(1).toUpperCase()).sorted().collect(Collectors.toCollection(ArrayList::new))) + "\n";
			}
		}
		
		// Pega as partes do Processo
		if(StringUtils.isEmpty(res)) {
			if(listaPromoventes != null && !listaPromoventes.isEmpty()) {
				
				String res1 = "";
				ArrayList<String> resTemp = new ArrayList<String>();
				
				int j = 0;
				
				for(j = 0; j < listaPromoventes.size(); ++j) {
					RelatorioAudienciaProcessoParteAdvogado x = listaPromoventes.get(j);
					resTemp.add(x.getNomeParte().substring(1).toUpperCase());
				}
				
				resTemp.sort(new Comparator<String>() {
			        @Override
			        public int compare(String a, String b) {
			            return  a.compareTo(b);
			        }
			    });
				
				for(j = 0; j < resTemp.size(); ++j) {
					res1 += resTemp.get(j) + ", ";
				}
				
				res += descricaoPoloAtivo.toUpperCase() + "(S): " + res1 + '\n';
				
				// jvosantos - 11/07/2019 12:34 - Refatoração do código usando lambda do Java 8 para código Java 7 conforme novas orientações da DSI
				//res += descricaoPoloAtivo.toUpperCase() + "(S): " + String.join(", ", (Iterable<? extends CharSequence>)listaPromoventes.stream().map(x -> x.getNomeParte().substring(1).toUpperCase()).sorted().collect(Collectors.toCollection(ArrayList::new))) + "\n";
			}
			if(listaPromovidos != null && !listaPromovidos.isEmpty()) {
				
				String res1 = "";
				ArrayList<String> resTemp = new ArrayList<String>();
				
				int j = 0;
				
				for(j = 0; j < listaPromovidos.size(); ++j) {
					RelatorioAudienciaProcessoParteAdvogado x = listaPromovidos.get(j);
					resTemp.add(x.getNomeParte().substring(1).toUpperCase());
				}
				
				resTemp.sort(new Comparator<String>() {
			        @Override
			        public int compare(String a, String b) {
			            return  a.compareTo(b);
			        }
			    });
				
				for(j = 0; j < resTemp.size(); ++j) {
					res1 += resTemp.get(j) + ", ";
				}
				
				res += descricaoPoloPassivo.toUpperCase() + "(S): " + res1 + '\n';
				
				// jvosantos - 11/07/2019 12:34 - Refatoração do código usando lambda do Java 8 para código Java 7 conforme novas orientações da DSI
				//res += descricaoPoloPassivo.toUpperCase() + "(S): " + String.join(", ", (Iterable<? extends CharSequence>)listaPromovidos.stream().map(x -> x.getNomeParte().substring(1).toUpperCase()).sorted().collect(Collectors.toCollection(ArrayList::new))) + "\n";
			}
		}
		
		return res;
	}


	// jvosantos - 26/06/2019 12:37 - Criar variáveis e metodos para obter lista de polos por ordem
	public HashMap<Integer, List<ProcessoParteDt>> getPoloAtivoOrdem() {
		return poloAtivoOrdem;
	}

	// jvosantos - 26/06/2019 12:37 - Criar variáveis e metodos para obter lista de polos por ordem
	public void setPoloAtivoOrdem(HashMap<Integer, List<ProcessoParteDt>> poloAtivoOrdem) {
		this.poloAtivoOrdem = poloAtivoOrdem;
	}

	// jvosantos - 26/06/2019 12:37 - Criar variáveis e metodos para obter lista de polos por ordem
	public HashMap<Integer, List<ProcessoParteDt>> getPoloPassivoOrdem() {
		return poloPassivoOrdem;
	}

	// jvosantos - 26/06/2019 12:37 - Criar variáveis e metodos para obter lista de polos por ordem
	public void setPoloPassivoOrdem(HashMap<Integer, List<ProcessoParteDt>> poloPassivoOrdem) {
		this.poloPassivoOrdem = poloPassivoOrdem;
	}

	// jvosantos - 26/06/2019 12:37 - Criar variáveis e metodos para obter lista de polos por ordem
	public Integer getOrdemMaxima() {
		return ordemMaxima;
	}

	// jvosantos - 26/06/2019 12:37 - Criar variáveis e metodos para obter lista de polos por ordem
	public void setOrdemMaxima(Integer ordemMaxima) {
		this.ordemMaxima = ordemMaxima;
	}

	// jvosantos - 28/06/2019 17:07 - Criar variveis para armazenar as partes de processos sem recursos
	public List<RelatorioAudienciaProcessoParteAdvogado> getListaPromoventesSemRecurso() {
		return listaPromoventes;
	}

	// jvosantos - 28/06/2019 17:07 - Criar variveis para armazenar as partes de processos sem recursos
	public void setListaPromoventesSemRecurso(List<RelatorioAudienciaProcessoParteAdvogado> listaPromoventes) {
		this.listaPromoventes = listaPromoventes;
	}

	// jvosantos - 28/06/2019 17:07 - Criar variveis para armazenar as partes de processos sem recursos
	public List<RelatorioAudienciaProcessoParteAdvogado> getListaPromovidosSemRecurso() {
		return listaPromovidos;
	}

	// jvosantos - 28/06/2019 17:07 - Criar variveis para armazenar as partes de processos sem recursos
	public void setListaPromovidosSemRecurso(List<RelatorioAudienciaProcessoParteAdvogado> listaPromovidos) {
		this.listaPromovidos = listaPromovidos;
	}

	// jvosantos - 28/06/2019 17:39 - Criar variveis para armazenar as partes de processos com recursos
	public List<RelatorioAudienciaProcessoParteAdvogado> getPoloAtivoRecurso() {
		return poloAtivoRecurso;
	}

	// jvosantos - 28/06/2019 17:39 - Criar variveis para armazenar as partes de processos com recursos
	public void setPoloAtivoRecurso(List<RelatorioAudienciaProcessoParteAdvogado> poloAtivoRecurso) {
		this.poloAtivoRecurso = poloAtivoRecurso;
	}

	// jvosantos - 28/06/2019 17:39 - Criar variveis para armazenar as partes de processos com recursos
	public List<RelatorioAudienciaProcessoParteAdvogado> getPoloPassivoRecurso() {
		return poloPassivoRecurso;
	}

	// jvosantos - 28/06/2019 17:39 - Criar variveis para armazenar as partes de processos com recursos
	public void setPoloPassivoRecurso(List<RelatorioAudienciaProcessoParteAdvogado> poloPassivoRecurso) {
		this.poloPassivoRecurso = poloPassivoRecurso;
	}
	
}
