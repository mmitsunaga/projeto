<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.TJDataHora"%><html>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.LogDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AtributoLogDt"%>

<jsp:useBean id="processoDt" class= "br.gov.go.tj.projudi.dt.ProcessoDt" scope="session"/>

<head>
	<title>Log de Processo</title>	
      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
      	  
      <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type='text/javascript' src='./js/jquery.js'></script>
      <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>

	<script type="text/javascript">
		function redimensionar(){
			var objIframe = window.parent.document.getElementById('Principal');
			if (objIframe != null) {				
				var divTextoLog = document.getElementById('divTextoLog');				
				objIframe.height = divTextoLog.clientHeight + 80;
			}			
		}		
	</script>
 
</head>	
	<body onload="redimensionar();">
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Log de Processo </h2></div>
		<form action="ProcessoLog" method="post" name="Formulario" id="Formulario">		
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend>Alterações no Processo</legend>
					
					<label class="formEdicaoLabel">Processo N&uacute;mero</label><br>
					<span class="spanDestaque"><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span>
					<br /><br />												
					<label for="AnoBusca">*Ano</label><br>						
					<select id="AnoBusca" name ="AnoBusca" class="formEdicaoCombo" >
					<%
					TJDataHora data = new TJDataHora();
					String anoBuscaSelecionado = String.valueOf(data.getAno());
					if (request.getAttribute("AnoBuscaSelecionado") != null && ((String)request.getAttribute("AnoBuscaSelecionado")).trim().length() > 0){ 
						anoBuscaSelecionado = (String) request.getAttribute("AnoBuscaSelecionado");
					}
					for(int i = data.getAno(); i >= 2010; i--) { %>
						<option value="<%=i%>" <%if(anoBuscaSelecionado.equalsIgnoreCase(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%
					}
					%>
					</select><br />					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>')">							
					</div>
					
					<fieldset class="formEdicao">	
						<legend>Pendências de Processos</legend>			
						<div id="divTextoLog">	
							<%
							List listaDePendenciasLogDt = (List) request.getAttribute("listaDePendenciasLogDt");
							
							if(listaDePendenciasLogDt != null){
								if(listaDePendenciasLogDt.size() < 1){
								%>
									<table id="Tabela" class="Tabela"><tbody><tr class="TabelaLinha1"><td>
									Não foram encontradas pendências para este processo.
									</td></tr></tbody></table>
								<%
								}
								else {
								%>
									<table id="Tabela" class="Tabela">
									<%
									String pendenciaAnterior = "";
									
									for(int k = 0; k < listaDePendenciasLogDt.size(); k++){
										
										List listaInterna = (List) listaDePendenciasLogDt.get(k);
										
										for(int m = 0; m < listaInterna.size(); m++){

											LogDt obLogDt = (LogDt) listaInterna.get(m);
											
											if((pendenciaAnterior == "") || (pendenciaAnterior != obLogDt.getId_Pendencia())){
											%>
												<thead>
													<tr class="TituloColuna1">
														<td width="50%" align="center">Pendência</td>
														<td width="50%" align="center">Tipo</td>
													</tr>
												</thead>
												<tbody>
													<tr class="TabelaLinha1">
														<td width="50%" align="center"><%= obLogDt.getId_Pendencia() %></td>
														<td width="50%" align="center"><%= obLogDt.getPendenciaTipo() %></td>
													</tr>
												</tbody>
											<%
											}//fim do if

											if(m == 0){
											%>
											<tbody>
												<tr class="TabelaLinha2">
													<td colspan=3>
														<table id="Tabela" class="Tabela">
															<thead>
																<tr class="TituloColuna1">
																	<td width="100%" align="center" colspan=2>Arquivo</td>																
																</tr>
															</thead>
															<tbody>
																<tr class="TabelaLinha1">
																	<td width="100%" align="center" colspan=2><%= obLogDt.getId_Tabela() %></td>
																</tr>
															</tbody>
															<%
															}//fim do if
															%>											
															<tbody>
																<tr class="TabelaLinha2">
																	<td colspan=3>
																		<table id="Tabela" class="Tabela">																														
																			<thead>
																				<tr class="TituloColuna1">
																					<td width="10%" align="center">Id Log</td>
																					<td width="10%" align="center">Tipo</td>
																					<td width="18%" align="center">Data - Hora</td>
																					<td width="17%" align="center">IP Computador</td>
																					<td width="11%" align="center">Id Usuário</td>																
																					<td width="34%" align="center" colspan=2>Nome</td>													
																				</tr>
																			</thead>
																			<tbody>
																				<tr class="TabelaLinha1">
																					<td width="10%" align="center"><%= obLogDt.getId() %></td>
																					<td width="10%" align="center"><%= obLogDt.getLogTipo() %></td>
																					<td width="18%" align="center"><%= obLogDt.getData() + " - " + obLogDt.getHora() %></td>
																					<td width="17%" align="center"><%= obLogDt.getIpComputador() %></td>
																					<td width="11%" align="center"><%= obLogDt.getId_Usuario() %></td>
																					<td width="34%" align="center" colspan=2><%= obLogDt.getNomeUsuario() %></td>
																				</tr>
																			</tbody>
																		</table>
																	</td>
																</tr>
															</tbody>
															<%
															if(m == (listaInterna.size() - 1)){ %>
														</table>
													</td>
												</tr>
											</tbody>												
											<%
											}//fim do if
											pendenciaAnterior = obLogDt.getId_Pendencia();
										}//fim do for
									}//fim do for
									%>
									</table>									
									<%
									
								}//fim do else
							}//fim do if
							%>							
						</div>
					</fieldset>
						
					<fieldset class="formEdicao">	
						<legend>Registros de LOG</legend>			
						<div id="divTextoLog">
							<%							
							List listaLogDt = (List) request.getAttribute("listaLogDt");
							LogDt logDt;
							boolean boLinha;
							
							if(listaLogDt != null){
								
								if(listaLogDt.size() < 1){
								%>
									<table id="Tabela" class="Tabela"><tbody><tr class="TabelaLinha1"><td>
									Não foram encontrados registros de log.
									</td></tr></tbody></table>
								<%
								}
								else {
								%>
							
									<table id="Tabela" class="Tabela">
								
									<%
									for(int i = 0; i < listaLogDt.size(); i++){
										logDt = (LogDt) listaLogDt.get(i);
										boLinha = false;
									%>
								
										<thead>
											<tr class="TituloColuna1">
												<th align="center"><%= logDt.getTabela() %></th>
											</tr>
										</thead>
										<tbody>
											<tr class="TabelaLinha2">
												<td>
													<table id="Tabela" class="Tabela">
														<thead>
															<tr class="TituloColuna1">
																<th width="20%" align="center">Data - Hora</th>
																<th width="40%" align="center">Id - CPF - Nome</th>
																<th width="15%" align="center">Tipo de Operação</th>
																<th width="10%" align="center">Id Tabela</th>
																<th width="15%" align="center">IP Computador</th>
															</tr>
														</thead>
														<tbody>	
															<tr class="TabelaLinha2">
																<td width="20%" align="center"><%= logDt.getData() + " - " + logDt.getHora() %></td>
																<td width="40%" align="center"><%= logDt.getId_Usuario() + " - " + logDt.getUsuario() + " - " + logDt.getNomeUsuario() %></td>
																<td width="15%" align="center"><%= logDt.getLogTipo() %></td>
																<td width="10%" align="center"><%= logDt.getId_Tabela() %></td>
																<td width="15%" align="center"><%= logDt.getIpComputador() %></td>
															</tr>
														</tbody>
														<thead>
															<tr class="TituloColuna">
																<th width="20%" align="center">Campo</th>
																<th width="40%" align="center">Valor Anterior</th>
																<th colspan="3" width="40%" align="center">Valor Novo</th>
															</tr>
														</thead>
														<tbody>
															<%
															List listaAtributoLogDt = logDt.getListaAtributos();
															
															if(listaAtributoLogDt != null){
																
																if(listaAtributoLogDt.size() > 0){
																
																	for(int j = 0; j < listaAtributoLogDt.size(); j++){
																		
																		AtributoLogDt atributoLogDt = (AtributoLogDt) listaAtributoLogDt.get(j);
																	%>
																	<tr class="TabelaLinha<%= (boLinha?1:2) %>">
																		<td><%= atributoLogDt.getNomeCampo() %></td>
																		<td align="center"><%= atributoLogDt.getValorAntigo() %></td>
																		<td colspan="3" align="center"><%= atributoLogDt.getValorNovo() %></td>
																	</tr>
																	<%
																		boLinha = !boLinha;
																	}
																}
																else {
																%>
																	<tr class="TabelaLinha<%= (boLinha?1:2) %>">
																		<td colspan="5" align="center">Registro vazio.</td>																	
																	</tr>
																<%	
																}
															}															
															else {
															%>
																<tr class="TabelaLinha<%= (boLinha?1:2) %>">
																	<td colspan="5" align="center">Registro vazio.</td>																	
																</tr>
															<%
															}
															%>
														</tbody>
													</table>
												</td>
											</tr>
										</tbody>
										<%
										}
									}
								}
								%>
									</table>
						</div>
					</fieldset>
					
				</fieldset>
			</div>
		</form>
 	</div> 	
 	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>