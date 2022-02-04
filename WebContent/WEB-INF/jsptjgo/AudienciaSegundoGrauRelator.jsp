<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoTipoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="java.util.ArrayList"%>
<jsp:useBean id="UsuarioSessao" scope="session"	class="br.gov.go.tj.projudi.ne.UsuarioNe" />

<html>
<head>
<title><%=request.getAttribute("TituloPagina")%></title>

<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">

<style type="text/css">
@import url('./css/Principal.css');

@import url('./css/Paginacao.css');
</style>

<link href="./css/menusimples.css" type="text/css" rel="stylesheet" />

<script type='text/javascript' src='./js/Funcoes.js'></script>
<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
<script type='text/javascript' src='./js/jquery.js'></script>
<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>

<script type="text/javascript" src="./js/acessibilidadeMenu.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		criarMenu('opcoes', 'Principal', 'menuA', 'menuAHover');
	});
</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area">
			<h2>&raquo; PROCESSOS A SEREM JULGADOS POR RELATOR</h2>
		</div>

		<%
			AudienciaProcessoFisicoDt audienciaProcessoFisicoDt = null;
		%>

		<form name="Formulario" id="Formulario"
			action="<%=request.getAttribute("tempRetorno")%>" method="post">

			<input type="hidden" id="PaginaAtual" name="PaginaAtual"
				value="<%=request.getAttribute("PaginaAtual")%>"> <input
				id="PaginaAnterior" name="PaginaAnterior" type="hidden"
				value="<%=request.getAttribute("PaginaAnterior")%>"> <input
				id="SomentePendentesAcordao" name="SomentePendentesAcordao"
				type="hidden"
				value="<%=request.getAttribute("SomentePendentesAcordao")%>">
			<input id="SomentePendentesAssinatura"
				name="SomentePendentesAssinatura" type="hidden"
				value="<%=request.getAttribute("SomentePendentesAssinatura")%>">
			<input id="SomentePreAnalisadas" name="SomentePreAnalisadas"
				type="hidden"
				value="<%=request.getAttribute("SomentePreAnalisadas")%>">

			<div id="divEditar" class="divEditar">

				<fieldset id="formLocalizar" class="formLocalizar">
					<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta
						de Sessões do Relator </legend>
					<label id="formLocalizarLabel" class="formLocalizarLabel">Especificar
						Sessão de Julgamento: </label><br> <select name="Id_Sessao"
						id="Id_Sessao">
						<option value="">--Selecione uma Sessão--</option>
						<%
							String id_Sessao = (String) request.getAttribute("Id_Sessao");
						List listaSessoesAbertas = (List) request.getAttribute("ListaSessoesAbertas");
						for (int i = 0; i < listaSessoesAbertas.size(); i++) {
							AudienciaDt audienciaSessao = (AudienciaDt) listaSessoesAbertas.get(i);
						%>
						<option value="<%=audienciaSessao.getId()%>"
							<%=(id_Sessao.equals(audienciaSessao.getId()) ? "selected" : "")%>><%=audienciaSessao.getDataAgendada()%>
						</option>
						<%
							}
						%>
					</select> <input id="formLocalizarBotao" class="formLocalizarBotao"
						type="submit" name="Localizar" value="Consultar" />
				</fieldset>
				<div id="divTabela" class="divTabela">
					<table id="Tabela" class="Tabela">
						<thead>
							<tr class="TituloColuna1">
								<th colspan="7"><%=request.getAttribute("NomeTitulo")%></th>
							</tr>
						</thead>
					</table>
				</div>
				<%
					AudienciaDt audienciaDt;
				boolean linha = false;
				int quantidadeProcessos = 0;
				int quantidadeProcessosTotal = 0;
				List listaSessoes = (List) request.getAttribute("ListaSessoes");

				boolean ehCamaraSegundoGrau = UsuarioSessao.isSegundoGrau();
				boolean ehGabineteElegivel = UsuarioSessao.isGabineteSegundoGrau() || UsuarioSessao.isGabineteFluxoUPJ();
				boolean ehTurmaJulgadora = UsuarioSessao.isTurmaJulgadora();
				boolean ehJuizTurma = UsuarioSessao.isJuizTurma();
				boolean ehDesembargador = UsuarioSessao.isDesembargador();

				//Verificar se é câmara ou gabinete
				if (ehCamaraSegundoGrau || ehGabineteElegivel) {
				%>
				<div id="divTabela" class="divTabela">
					<table id="Tabela" class="Tabela">
						<thead>
							<tr class="TituloColuna1">
								<th colspan="7">Julgamentos Adiados</th>
							</tr>
							<tr class="TituloColuna">
								<th width="12%">Sessão Adiamento</th>
								<th width="16%">Processo</th>
								<th>Recorrente</th>
								<th>Recorrido</th>
								<th>MP Responsável</th>
								<th width="10%">Movimentação</th>
							</tr>
						</thead>
						<tbody id="tabListaAudiencia">
							<%
								//Verificar se foram encontradas sessões
							if (listaSessoes != null) {
								// Organizar os dados de cada sessão consultada para montar seu dados na tela
								for (int i = 0; i < listaSessoes.size(); i++) {
									audienciaDt = (AudienciaDt) listaSessoes.get(i);
									//Buscar o(s) processo(s) vinculados à sessão em questão
									List processosSessao = audienciaDt.getListaAudienciaProcessoDt();
									List processosSessaoJulgamentoIniciado = new ArrayList();
									//Verificar se a sessão está vinculada a algum processo
									if (processosSessao != null && processosSessao.size() > 0) {
								for (int j = 0; j < audienciaDt.getListaAudienciaProcessoDt().size(); j++) {
									AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosSessao.get(j);
									//Verifica status da audiência/sessão
									if (audienciaProcessoDt.isJulgamentoIniciado()) {
										processosSessaoJulgamentoIniciado.add(audienciaProcessoDt);
									}
								}
									}
									quantidadeProcessos = processosSessaoJulgamentoIniciado.size();
									quantidadeProcessosTotal += quantidadeProcessos;
									if (quantidadeProcessos > 0) {
								//Variável para controlar se o processo é o primeiro a ser exibido vinculado à sessão
							%>
							<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
								<td rowspan="<%=quantidadeProcessos%>"><%=audienciaDt.getDataAgendada()%></td>
								<%
									for (int j = 0; j < processosSessaoJulgamentoIniciado.size(); j++) {
									AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosSessaoJulgamentoIniciado.get(j);
									audienciaProcessoFisicoDt = null;
									if (audienciaProcessoDt != null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {
										audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt) audienciaProcessoDt;
									}
								%>
								<!-- Se é do segundo processo para frente deve abrir uma nova linha para mostrar corretamente -->
								<%
									if (j > 0) {
								%>
							
							<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
								<%
									}
								%>
								<!--  -->
								<%
									if (audienciaProcessoDt.getProcessoDt() != null) {
								%>
								<td><a
									href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>">
										<%=audienciaProcessoDt.getProcessoDt().getProcessoNumero()%>
										&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%>
								</a></td>
								<%
									} else if (audienciaProcessoFisicoDt != null) {
								%>
								<td><%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
									&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%></td>
								<%
									} else {
								%>
								<td>-</td>
								<%
									}
								%>
								<!-- PROMOVENTES -->
								<td>
									<ul class="partes">
										<%
											if (audienciaProcessoDt.getProcessoDt() != null) {
										%>
										<%
											List listaPromoventes = audienciaProcessoDt.getProcessoDt().getListaPolosAtivos();
										if (listaPromoventes != null) {
											for (int y = 0; y < listaPromoventes.size(); y++) {
												ProcessoParteDt promovente = (ProcessoParteDt) listaPromoventes.get(y);
										%>
										<li><%=promovente.getNome()%></li>
										<%
											}
										}
										} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
										%>
										<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente()%></li>
										<%
											}
										%>
									</ul>
								</td>
								<!-- FIM PROMOVENTES -->

								<!-- PROMOVIDOS -->
								<td>
									<ul class="partes">
										<%
											if (audienciaProcessoDt.getProcessoDt() != null) {
										%>
										<%
											List listaPromovidos = audienciaProcessoDt.getProcessoDt().getListaPolosPassivos();
										if (listaPromovidos != null) {
											for (int y = 0; y < listaPromovidos.size(); y++) {
												ProcessoParteDt promovido = (ProcessoParteDt) listaPromovidos.get(y);
										%>
										<li><%=promovido.getNome()%></li>
										<%
											}
										}
										} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
										%>
										<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido()%></li>
										<%
											}
										%>
									</ul>
								</td>
								<!-- FIM PROMOVIDOS -->
								<td>
									<%
										if (audienciaProcessoDt.getNomeMPProcesso() != null && audienciaProcessoDt.getNomeMPProcesso().trim().length() > 0) {
									%> <%=audienciaProcessoDt.getNomeMPProcesso()%>
									<%
										} else {
									%> Não há <%
										}
									%>
								</td>
								<!-- Se era o primeiro registro até aqui, mostra o status da audiência e a partir do próximo não será mais necessário. Caso contrário deve fechar a nova linha que abriu -->
								<%
									if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo()
										.equalsIgnoreCase(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA))
										&& (request.getAttribute("podeMovimentar") != null
										&& request.getAttribute("podeMovimentar").toString().equals("true"))) {
								%>
								<td>
									<%
										if ((((request.getAttribute("PodeAnalisar") != null)
											&& (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S")))
											|| ((request.getAttribute("PodePreAnalisar") != null)
											&& (String.valueOf(request.getAttribute("PodePreAnalisar")).trim().equalsIgnoreCase("S"))))) {
										if (!audienciaProcessoDt.isAnalistaPodeMovimentar()) {
									%>
									<div id="opcoes" class="menuEspecial">
										<ul>
											<li>Opções
												<ul>
													<%
														if (!ehDesembargador) {
													%>
													<li><a
														href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=1&SomentePreAnalisadas=<%=request.getAttribute("SomentePreAnalisadas")%>"
														title="Pré-Analisar Acórdão/Ementa"> Pré-Analisar
															Acórdão/Ementa </a></li>
													<%
														}
													%>
													<%
														if ((request.getAttribute("PodeAnalisar") != null)
															&& (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S"))) {
													%>
													<li><a
														href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=2&SomentePreAnalisadas=<%=request.getAttribute("SomentePreAnalisadas")%>"
														title="Inserir Acórdão/Ementa"> Inserir Acórdão/Ementa
													</a></li>
													<%
														}
													%>
												</ul>
											</li>
										</ul>
									</div> <%
 	} else {
 %> Aguardando Extrato da Ata de Julgamento
									<%
 	}
 %> <%
 	}
 %>
								</td>
								<%
									} else {
								%>
								<td><%=audienciaDt.getAudienciaProcessoDt().getDataMovimentacao()%></td>
								<%
									}
								}
								%>
							</tr>
							<%
								}
							linha = !linha;
							} //Fim FOR AUDIENCIADT

							// Organizar os dados de cada sessão consultada para montar seu dados na tela
							for (int i = 0; i < listaSessoes.size(); i++) {
							audienciaDt = (AudienciaDt) listaSessoes.get(i);
							//Buscar o(s) processo(s) vinculados à sessão em questão
							List processosSessao = audienciaDt.getListaAudienciaProcessoDt();
							List processosSessaoJulgamentoAdiado = new ArrayList();
							//Verificar se a sessão está vinculada a algum processo
							if (processosSessao != null && processosSessao.size() > 0) {
							for (int j = 0; j < audienciaDt.getListaAudienciaProcessoDt().size(); j++) {
								AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosSessao.get(j);
								//Verifica status da audiência/sessão
								if (audienciaProcessoDt.isJulgamentoAdiado()) {
									processosSessaoJulgamentoAdiado.add(audienciaProcessoDt);
								}
							}
							}
							quantidadeProcessos = processosSessaoJulgamentoAdiado.size();
							quantidadeProcessosTotal += quantidadeProcessos;
							if (quantidadeProcessos > 0) {
							//Variável para controlar se o processo é o primeiro a ser exibido vinculado à sessão
							%>
							<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
								<td rowspan="<%=quantidadeProcessos%>"><%=audienciaDt.getDataAgendada()%></td>
								<%
									for (int j = 0; j < processosSessaoJulgamentoAdiado.size(); j++) {
									AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosSessaoJulgamentoAdiado.get(j);
									audienciaProcessoFisicoDt = null;
									if (audienciaProcessoDt != null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {
										audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt) audienciaProcessoDt;
									}
								%>
								<!-- Se é do segundo processo para frente deve abrir uma nova linha para mostrar corretamente -->
								<%
									if (j > 0) {
								%>
							
							<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
								<%
									}
								%>
								<!-- PROCESSO -->
								<%
									if (audienciaProcessoDt.getProcessoDt() != null) {
								%>
								<td><a
									href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>">
										<%=audienciaProcessoDt.getProcessoDt().getProcessoNumero()%>
										&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%>
								</a></td>
								<%
									} else if (audienciaProcessoFisicoDt != null) {
								%>
								<td><%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
									&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%></td>
								<%
									} else {
								%>
								<td>-</td>
								<%
									}
								%>
								<!-- FIM PROCESSO -->

								<!-- PROMOVENTES -->
								<td>
									<ul class="partes">
										<%
											if (audienciaProcessoDt.getProcessoDt() != null) {
										%>
										<%
											List listaPromoventes = audienciaProcessoDt.getProcessoDt().getListaPolosAtivos();
										if (listaPromoventes != null) {
											for (int y = 0; y < listaPromoventes.size(); y++) {
												ProcessoParteDt promovente = (ProcessoParteDt) listaPromoventes.get(y);
										%>
										<li><%=promovente.getNome()%></li>
										<%
											}
										}
										} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
										%>
										<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente()%></li>
										<%
											}
										%>
									</ul>
								</td>
								<!-- FIM PROMOVENTES -->

								<!-- PROMOVIDOS -->
								<td>
									<ul class="partes">
										<%
											if (audienciaProcessoDt.getProcessoDt() != null) {
										%>
										<%
											List listaPromovidos = audienciaProcessoDt.getProcessoDt().getListaPolosPassivos();
										if (listaPromovidos != null) {
											for (int y = 0; y < listaPromovidos.size(); y++) {
												ProcessoParteDt promovido = (ProcessoParteDt) listaPromovidos.get(y);
										%>
										<li><%=promovido.getNome()%></li>
										<%
											}
										}
										} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
										%>
										<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido()%></li>
										<%
											}
										%>
									</ul>
								</td>
								<!-- FIM PROMOVIDOS -->

								<td>
									<%
										if (audienciaProcessoDt.getNomeMPProcesso() != null && audienciaProcessoDt.getNomeMPProcesso().trim().length() > 0) {
									%> <%=audienciaProcessoDt.getNomeMPProcesso()%>
									<%
										} else {
									%> Não há <%
										}
									%>
								</td>

								<!-- Se era o primeiro registro até aqui, mostra o status da audiência e a partir do próximo não será mais necessário. Caso contrário deve fechar a nova linha que abriu -->
								<%
									if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo()
										.equalsIgnoreCase(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA))
										&& (request.getAttribute("podeMovimentar") != null
										&& request.getAttribute("podeMovimentar").toString().equals("true"))) {
								%>
								<td>
									<%
										if ((((request.getAttribute("PodeAnalisar") != null)
											&& (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S")))
											|| ((request.getAttribute("PodePreAnalisar") != null)
											&& (String.valueOf(request.getAttribute("PodePreAnalisar")).trim().equalsIgnoreCase("S"))))) {
										if (!audienciaProcessoDt.isAnalistaPodeMovimentar()) {
									%>
									<div id="opcoes" class="menuEspecial">
										<ul>
											<li>Opções
												<ul>
													<%
														if (!ehDesembargador) {
													%>
													<li><a
														href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=1&SomentePreAnalisadas=<%=request.getAttribute("SomentePreAnalisadas")%>"
														title="Pré-Analisar Acórdão/Ementa"> Pré-Analisar
															Acórdão/Ementa </a></li>
													<%
														}
													%>
													<%
														if ((request.getAttribute("PodeAnalisar") != null)
															&& (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S"))) {
													%>
													<li><a
														href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=2&SomentePreAnalisadas=<%=request.getAttribute("SomentePreAnalisadas")%>"
														title="Inserir Acórdão/Ementa"> Inserir Acórdão/Ementa
													</a></li>
													<%
														}
													%>
												</ul>
											</li>
										</ul>
									</div> <%
 	} else {
 %> Aguardando Extrato da Ata de Julgamento
									<%
 	}
 %> <%
 	}
 %>
								</td>
								<%
									} else {
								%>
								<td><%=audienciaDt.getAudienciaProcessoDt().getDataMovimentacao()%></td>
								<%
									}
								}
								%>
							</tr>
							<%
								}
							linha = !linha;
							} //Fim FOR AUDIENCIADT
							if (quantidadeProcessosTotal == 0) {
							%>
							<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
								<td>-</td>
								<td>-</td>
								<td>-</td>
								<td>-</td>
								<td>-</td>
								<td>-</td>
							</tr>
							<%
								}
							} else {
							%>
							<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
								<td colspan="5"><em> Nenhum Processo a ser Julgado.</td>
							</tr>
							<%
								}
							%>
						</tbody>
					</table>
				</div>

				<%
					}
				linha = false;
				quantidadeProcessos = 0;
				quantidadeProcessosTotal = 0;
				%>
				<div id="divTabela" class="divTabela">
					<table id="Tabela" class="Tabela">
						<thead>
							<tr class="TituloColuna1">
								<th colspan="7">Pauta do dia</th>
							</tr>
							<tr class="TituloColuna">
								<th width="12%">Data Sessão</th>
								<th width="16%">Processo</th>
								<th>Recorrente</th>
								<th>Recorrido</th>
								<th>MP Responsável</th>
								<th width="10%">Movimentação</th>
							</tr>
						</thead>
						<tbody id="tabListaAudiencia">
							<%
								//Verificar se foram encontradas sessões
							if (listaSessoes != null) {
								// Organizar os dados de cada sessão consultada para montar seu dados na tela
								for (int i = 0; i < listaSessoes.size(); i++) {
									audienciaDt = (AudienciaDt) listaSessoes.get(i);
									//Buscar o(s) processo(s) vinculados à sessão em questão
									List processosSessao = audienciaDt.getListaAudienciaProcessoDt();
									List processosSessaoJulgamentoPautaDia = new ArrayList();
									//Verificar se a sessão está vinculada a algum processo
									if (processosSessao != null && processosSessao.size() > 0) {
								for (int j = 0; j < audienciaDt.getListaAudienciaProcessoDt().size(); j++) {
									AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosSessao.get(j);
									//Verifica status da audiência/sessão
									if (audienciaProcessoDt.isPautaDoDia()) {
										processosSessaoJulgamentoPautaDia.add(audienciaProcessoDt);
									}
								}
									}
									quantidadeProcessos = processosSessaoJulgamentoPautaDia.size();
									quantidadeProcessosTotal += quantidadeProcessos;
									if (quantidadeProcessos > 0) {
								//Variável para controlar se o processo é o primeiro a ser exibido vinculado à sessão
							%>
							<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
								<td rowspan="<%=quantidadeProcessos%>"><%=audienciaDt.getDataAgendada()%></td>
								<%
									for (int j = 0; j < processosSessaoJulgamentoPautaDia.size(); j++) {
									AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosSessaoJulgamentoPautaDia.get(j);
									audienciaProcessoFisicoDt = null;
									if (audienciaProcessoDt != null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {
										audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt) audienciaProcessoDt;
									}
								%>
								<!-- Se é do segundo processo para frente deve abrir uma nova linha para mostrar corretamente -->
								<%
									if (j > 0) {
								%>
							
							<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
								<%
									}
								%>
								<!-- PROCESSO -->
								<%
									if (audienciaProcessoDt.getProcessoDt() != null) {
								%>
								<td><a
									href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>">
										<%=audienciaProcessoDt.getProcessoDt().getProcessoNumero()%>
										&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%>
								</a></td>
								<%
									} else if (audienciaProcessoFisicoDt != null) {
								%>
								<td><%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
									&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%></td>
								<%
									} else {
								%>
								<td>-</td>
								<%
									}
								%>
								<!-- FIM PROCESSO -->

								<!-- PROMOVENTES -->
								<td>
									<ul class="partes">
										<%
											if (audienciaProcessoDt.getProcessoDt() != null) {
										%>
										<%
											List listaPromoventes = audienciaProcessoDt.getProcessoDt().getListaPolosAtivos();
										if (listaPromoventes != null) {
											for (int y = 0; y < listaPromoventes.size(); y++) {
												ProcessoParteDt promovente = (ProcessoParteDt) listaPromoventes.get(y);
										%>
										<li><%=promovente.getNome()%></li>
										<%
											}
										}
										} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
										%>
										<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente()%></li>
										<%
											}
										%>
									</ul>
								</td>
								<!-- FIM PROMOVENTES -->

								<!-- PROMOVIDOS -->
								<td>
									<ul class="partes">
										<%
											if (audienciaProcessoDt.getProcessoDt() != null) {
										%>
										<%
											List listaPromovidos = audienciaProcessoDt.getProcessoDt().getListaPolosPassivos();
										if (listaPromovidos != null) {
											for (int y = 0; y < listaPromovidos.size(); y++) {
												ProcessoParteDt promovido = (ProcessoParteDt) listaPromovidos.get(y);
										%>
										<li><%=promovido.getNome()%></li>
										<%
											}
										}
										} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
										%>
										<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido()%></li>
										<%
											}
										%>
									</ul>
								</td>
								<!-- FIM PROMOVIDOS -->

								<td>
									<%
										if (audienciaProcessoDt.getNomeMPProcesso() != null && audienciaProcessoDt.getNomeMPProcesso().trim().length() > 0) {
									%> <%=audienciaProcessoDt.getNomeMPProcesso()%>
									<%
										} else {
									%> Não há <%
										}
									%>
								</td>

								<!-- Se era o primeiro registro até aqui, mostra o status da audiência e a partir do próximo não será mais necessário. Caso contrário deve fechar a nova linha que abriu -->
								<%
									if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo()
										.equalsIgnoreCase(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA))
										&& (request.getAttribute("podeMovimentar") != null
										&& request.getAttribute("podeMovimentar").toString().equals("true"))) {
								%>
								<td>
									<%
										if ((((request.getAttribute("PodeAnalisar") != null)
											&& (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S")))
											|| ((request.getAttribute("PodePreAnalisar") != null)
											&& (String.valueOf(request.getAttribute("PodePreAnalisar")).trim().equalsIgnoreCase("S"))))) {
										if (ehCamaraSegundoGrau || ehGabineteElegivel) {
											if (!audienciaProcessoDt.isAnalistaPodeMovimentar()) {
									%>
									<div id="opcoes" class="menuEspecial">
										<ul>
											<li>Opções
												<ul>
													<%
														if (!ehDesembargador) {
													%>
													<li><a
														href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=1&SomentePreAnalisadas=<%=request.getAttribute("SomentePreAnalisadas")%>"
														title="Pré-Analisar Acórdão/Ementa"> Pré-Analisar
															Acórdão/Ementa </a></li>
													<%
														}
													%>
													<%
														if ((request.getAttribute("PodeAnalisar") != null)
															&& (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S"))) {
													%>
													<li><a
														href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=2&SomentePreAnalisadas=<%=request.getAttribute("SomentePreAnalisadas")%>"
														title="Inserir Acórdão/Ementa"> Inserir Acórdão/Ementa
													</a></li>
													<%
														}
													%>
												</ul>
											</li>
										</ul>
									</div> <%
 	} else {
 %> Aguardando Extrato da Ata de
									Julgamento <%
 	}
 %> <%
 	} else {
 if (ehJuizTurma) {
 %>

									<div id="opcoes" class="menuEspecial">
										<ul>
											<li>Opções
												<ul>
													<li><a
														href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=1&SomentePreAnalisadas=N"
														title="Pré-Analisar"> Pré-Analisar </a></li>
													<li><a
														href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>"
														title="Concluir - Executa a movimentação da(s) audiência(s) selecionada(s)">
															Movimentar </a></li>
												</ul>
											</li>
										</ul>
									</div> <%
 	} else {

 if (ehTurmaJulgadora && !((UsuarioDt) request.getSession().getAttribute("UsuarioSessaoDt")).isAnalistaJudiciario()) {
 %>

									<div id="opcoes" class="menuEspecial">
										<ul>
											<li>Opções
												<ul>
													<li><a
														href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=1"
														title="Pré-Analisar"> Pré-Analisar </a></li>
												</ul>
											</li>
										</ul>
									</div> <%
 	} else {
 %> <a
									href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>"
									title="Concluir - Executa a movimentação da(s) audiência(s) selecionada(s)">
										Movimentar </a> <%
 	}

 }
 }

 }
 %>
								</td>
								<%
									} else {
								%>

								<td><%=audienciaDt.getAudienciaProcessoDt().getDataMovimentacao()%></td>
								<%
									}
								}
								%>
							</tr>
							<%
								}
							linha = !linha;
							} //Fim FOR AUDIENCIADT
							if (quantidadeProcessosTotal == 0) {
							%>
							<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
								<td>-</td>
								<td>-</td>
								<td>-</td>
								<td>-</td>
								<td>-</td>
								<td>-</td>
							</tr>
							<%
								}
							} else {
							%>
							<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
								<td colspan="5"><em> Nenhum Processo a ser Julgado.</td>
							</tr>
							<%
								}

							linha = false;
							quantidadeProcessos = 0;
							quantidadeProcessosTotal = 0;

							//Verificar se é câmara ou gabinete
							if (ehCamaraSegundoGrau || ehGabineteElegivel) {
							%>
							<div id="divTabela" class="divTabela">
								<table id="Tabela" class="Tabela">
									<thead>
										<tr class="TituloColuna1">
											<th colspan="7">Em Mesa Para Julgamento</th>
										</tr>
										<tr class="TituloColuna">
											<th width="12%">Data Sessão</th>
											<th width="16%">Processo</th>
											<th>Recorrente</th>
											<th>Recorrido</th>
											<th>MP Responsável</th>
											<th width="10%">Movimentação</th>
										</tr>
									</thead>
									<tbody id="tabListaAudiencia">
										<%
											//Verificar se foram encontradas sessões
										if (listaSessoes != null) {
											// Organizar os dados de cada sessão consultada para montar seu dados na tela
											for (int i = 0; i < listaSessoes.size(); i++) {
												audienciaDt = (AudienciaDt) listaSessoes.get(i);
												//Buscar o(s) processo(s) vinculados à sessão em questão
												List processosSessao = audienciaDt.getListaAudienciaProcessoDt();
												List processosSessaoJulgamentoEmMesaParaJulgamento = new ArrayList();
												//Verificar se a sessão está vinculada a algum processo
												if (processosSessao != null && processosSessao.size() > 0) {
											for (int j = 0; j < audienciaDt.getListaAudienciaProcessoDt().size(); j++) {
												AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosSessao.get(j);
												//Verifica status da audiência/sessão
												if (audienciaProcessoDt.isJulgamentoEmMesaParaJulgamento()) {
													processosSessaoJulgamentoEmMesaParaJulgamento.add(audienciaProcessoDt);
												}
											}
												}
												quantidadeProcessos = processosSessaoJulgamentoEmMesaParaJulgamento.size();
												quantidadeProcessosTotal += quantidadeProcessos;
												if (quantidadeProcessos > 0) {
											//Variável para controlar se o processo é o primeiro a ser exibido vinculado à sessão
										%>
										<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
											<td rowspan="<%=quantidadeProcessos%>"><%=audienciaDt.getDataAgendada()%></td>
											<%
												for (int j = 0; j < processosSessaoJulgamentoEmMesaParaJulgamento.size(); j++) {
												AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) processosSessaoJulgamentoEmMesaParaJulgamento
												.get(j);
												audienciaProcessoFisicoDt = null;
												if (audienciaProcessoDt != null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {
													audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt) audienciaProcessoDt;
												}
											%>
											<!-- Se é do segundo processo para frente deve abrir uma nova linha para mostrar corretamente -->
											<%
												if (j > 0) {
											%>
										
										<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
											<%
												}
											%>
											<!-- PROCESSO -->
											<%
												if (audienciaProcessoDt.getProcessoDt() != null) {
											%>
											<td><a
												href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getProcessoDt().getId()%>">
													<%=audienciaProcessoDt.getProcessoDt().getProcessoNumero()%>
													&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%>
											</a></td>
											<%
												} else if (audienciaProcessoFisicoDt != null) {
											%>
											<td><%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
												&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%></td>
											<%
												} else {
											%>
											<td>-</td>
											<%
												}
											%>

											<!-- PROMOVENTES -->
											<td>
												<ul class="partes">
													<%
														if (audienciaProcessoDt.getProcessoDt() != null) {
													%>
													<%
														List listaPromoventes = audienciaProcessoDt.getProcessoDt().getListaPolosAtivos();
													if (listaPromoventes != null) {
														for (int y = 0; y < listaPromoventes.size(); y++) {
															ProcessoParteDt promovente = (ProcessoParteDt) listaPromoventes.get(y);
													%>
													<li><%=promovente.getNome()%></li>
													<%
														}
													}
													} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
													%>
													<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovente()%></li>
													<%
														}
													%>
												</ul>
											</td>
											<!-- FIM PROMOVENTES -->

											<!-- PROMOVIDOS -->
											<td>
												<ul class="partes">
													<%
														if (audienciaProcessoDt.getProcessoDt() != null) {
													%>
													<%
														List listaPromovidos = audienciaProcessoDt.getProcessoDt().getListaPolosPassivos();
													if (listaPromovidos != null) {
														for (int y = 0; y < listaPromovidos.size(); y++) {
															ProcessoParteDt promovido = (ProcessoParteDt) listaPromovidos.get(y);
													%>
													<li><%=promovido.getNome()%></li>
													<%
														}
													}
													} else if (audienciaProcessoFisicoDt != null && audienciaProcessoFisicoDt.getProcessoFisicoDt() != null) {
													%>
													<li><%=audienciaProcessoFisicoDt.getProcessoFisicoDt().getPromovido()%></li>
													<%
														}
													%>
												</ul>
											</td>
											<!-- FIM PROMOVIDOS -->

											<td>
												<%
													if (audienciaProcessoDt.getNomeMPProcesso() != null && audienciaProcessoDt.getNomeMPProcesso().trim().length() > 0) {
												%>
												<%=audienciaProcessoDt.getNomeMPProcesso()%> <%
 	} else {
 %>
												Não há <%
 	}
 %>
											</td>

											<!-- Se era o primeiro registro até aqui, mostra o status da audiência e a partir do próximo não será mais necessário. Caso contrário deve fechar a nova linha que abriu -->
											<%
												if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo()
													.equalsIgnoreCase(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA))
													&& (request.getAttribute("podeMovimentar") != null
													&& request.getAttribute("podeMovimentar").toString().equals("true"))) {
											%>
											<td>
												<%
													if ((((request.getAttribute("PodeAnalisar") != null)
														&& (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S")))
														|| ((request.getAttribute("PodePreAnalisar") != null)
														&& (String.valueOf(request.getAttribute("PodePreAnalisar")).trim().equalsIgnoreCase("S"))))) {
													if (!audienciaProcessoDt.isAnalistaPodeMovimentar()) {
												%>
												<div id="opcoes" class="menuEspecial">
													<ul>
														<li>Opções
															<ul>
																<%
																	if (!ehDesembargador) {
																%>
																<li><a
																	href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=1&SomentePreAnalisadas=<%=request.getAttribute("SomentePreAnalisadas")%>"
																	title="Pré-Analisar Acórdão/Ementa"> Pré-Analisar
																		Acórdão/Ementa </a></li>
																<%
																	}
																%>
																<%
																	if ((request.getAttribute("PodeAnalisar") != null)
																		&& (String.valueOf(request.getAttribute("PodeAnalisar")).trim().equalsIgnoreCase("S"))) {
																%>
																<li><a
																	href="AudienciaProcessoMovimentacao?Id_AudienciaProcesso=<%=audienciaProcessoDt.getId()%>&PaginaAtual=<%=Configuracao.Novo%>&fluxo=<%=request.getAttribute("fluxo")%>&TipoAudienciaProcessoMovimentacao=2&SomentePreAnalisadas=<%=request.getAttribute("SomentePreAnalisadas")%>"
																	title="Inserir Acórdão/Ementa"> Inserir
																		Acórdão/Ementa </a></li>
																<%
																	}
																%>
															</ul>
														</li>
													</ul>
												</div> <%
 	} else {
 %> Aguardando Extrato da Ata de Julgamento <%
 	}
 %>
												<%
													}
												%>
											</td>
											<%
												} else {
											%>
											<td><%=audienciaDt.getAudienciaProcessoDt().getDataMovimentacao()%></td>
											<%
												}
											}
											%>
										</tr>
										<%
											}
										linha = !linha;
										} //Fim FOR AUDIENCIADT
										if (quantidadeProcessosTotal == 0) {
										%>
										<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
											<td>-</td>
											<td>-</td>
											<td>-</td>
											<td>-</td>
											<td>-</td>
											<td>-</td>
										</tr>
										<%
											}
										} else {
										%>
										<tr class="TabelaLinha<%=(linha ? 1 : 2)%>">
											<td colspan="5"><em> Nenhum Processo a ser Julgado.</td>
										</tr>
										<%
											}
										%>
										<%
											}
										%>
									</tbody>
								</table>
							</div>
							</div>
							</form>
							<%@ include file="Padroes/Mensagens.jspf"%>
							</div>
</body>
</html>