<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.LogDt"%>
<%@page import="br.gov.go.tj.projudi.dt.LogTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AdvogadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AtributoLogDt"%>
<%@page import="br.gov.go.tj.utils.TJDataHora"%>

<jsp:useBean id="Advogadodt" class= "br.gov.go.tj.projudi.dt.UsuarioDt" scope="session"/>

<html>
<head>
	<title>Log de Advogado</title>	
      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
      	  
      <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type='text/javascript' src='./js/jquery.js'></script>
      <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
      
</head>	
	<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Log de Advogado</h2></div>
		<form action="Advogado" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend>Alterações no Advogado</legend>
					
					<label class="formEdicaoLabel">Advogado</label><br/>
										
					<span class="spanDestaque"><a href="Advogado?Id_Advogado=<%=Advogadodt.getId()%>"><%=Advogadodt.getNome()%></a></span>
					
					<br/><br/>	
					<label for="AnoBusca">*Ano</label><br>						
								
					<select id="AnoBusca" name ="AnoBusca" class="formEdicaoCombo" >
					<%
					TJDataHora data = new TJDataHora();
					String anoBuscaSelecionado = String.valueOf(data.getAno());
					if (request.getAttribute("AnoBuscaSelecionado") != null && ((String)request.getAttribute("AnoBuscaSelecionado")).trim().length() > 0) 
						anoBuscaSelecionado = (String) request.getAttribute("AnoBuscaSelecionado");
					for(int i = data.getAno(); i >= 2010; i--) { %>
						<option value="<%=i%>" <%if(anoBuscaSelecionado.equalsIgnoreCase(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select><br/>
										
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>')" />							
					</div>
					
					<div>
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
									Não foram encontrados registros de log.
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
												<th><%= logDt.getTabela() %></th>
											</tr>
										</thead>
										<tbody>
											<tr class="TabelaLinha2">
												<td>
													<table id="Tabela" class="Tabela">
														<thead>
															<tr class="TituloColuna1">
																<th width="17%">Data - Hora</th>
																<th width="38%">Responsável pela Operação</th>
																<th width="15%">Tipo de Operação</th>																
																<th width="13%">IP Computador</th>
																<th width="17%">Id Usuário Alterado</th>
															</tr>
															<tr class="TituloColuna">
																<th width="17%" style='font-weight:normal' class="Linha1"><%= logDt.getData() + " - " + logDt.getHora() %></th>
																<th width="38%" style='font-weight:normal' class="Linha1"><%= logDt.getId_Usuario() + " - " + logDt.getUsuario() + " - " + logDt.getNomeUsuario() %></th>
																<th width="15%" style='font-weight:normal' class="Linha1"><%= logDt.getLogTipo() %></th>																
																<th width="13%" style='font-weight:normal' class="Linha1"><%= logDt.getIpComputador() %></th>
																<th width="17%" style='font-weight:normal' class="Linha1"><%= logDt.getId_Tabela() %></th>
															</tr>
															<tr class="TituloColuna">
																<th width="17%">Campo</th>
																<th width="38%">Valor Anterior</th>
																<th colspan="3" width="45%">Valor Novo</th>
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
																		<td><%= atributoLogDt.getValorAntigo() %></td>
																		<td colspan="3"><%= atributoLogDt.getValorNovo() %></td>
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
								%>
									</table>
								<%
								}
							}						
							%>
						</div>
					</fieldset>					
					</div>
					
				</fieldset>
			</div>
		</form>
 	</div> 	
 	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>