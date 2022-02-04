<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.LogDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AtributoLogDt"%>
<%@page import="br.gov.go.tj.utils.TJDataHora"%>

<jsp:useBean id="Serventiadt" class= "br.gov.go.tj.projudi.dt.ServentiaDt" scope="session"/>

<html>
<head>
	<title>Log de Serventia</title>	
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
		<div class="area"><h2>&raquo; Log de Serventia </h2></div>
		<form action="Serventia" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend>Altera��es na Serventia</legend>
					
					<label class="formEdicaoLabel"> Serventia</label><br/>
					
					<span class="spanDestaque"><a href="Serventia?Id_Serventia=<%=Serventiadt.getId()%>"><%=Serventiadt.getServentia()%></a></span>
					
					<br /><br />												
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
					</select><br />
										
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')" />							
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
									N�o foram encontrados registros de log.
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
																<th width="20%">Data - Hora</th>
																<th width="40%">Id - CPF - Nome</th>
																<th width="15%">Tipo de Opera��o</th>
																<th width="10%">Id Tabela</th>
																<th width="15%">IP Computador</th>
															</tr>
															<tr class="TituloColuna">
																<th width="20%" style='font-weight:normal' class="Linha1"><%= logDt.getData() + " - " + logDt.getHora() %></th>
																<th width="40%" style='font-weight:normal' class="Linha1"><%= logDt.getId_Usuario() + " - " + logDt.getUsuario() + " - " + logDt.getNomeUsuario() %></th>
																<th width="15%" style='font-weight:normal' class="Linha1"><%= logDt.getLogTipo() %></th>
																<th width="10%" style='font-weight:normal' class="Linha1"><%= logDt.getId_Tabela() %></th>
																<th width="15%" style='font-weight:normal' class="Linha1"><%= logDt.getIpComputador() %></th>
															</tr>
															<tr class="TituloColuna">
																<th width="20%">Campo</th>
																<th width="40%">Valor Anterior</th>
																<th colspan="3" width="40%">Valor Novo</th>
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