<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AdvogadoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<jsp:useBean id="ProcessoCadastroDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoCadastroDt"/>


<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%><html>
	<head>
		<title>Busca de Advogados</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
	   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Inclusão de Advogados </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />
				<input type="hidden" id="posicaoLista" name="posicaoLista">
				<input id="fluxo" name="fluxo" type="hidden" value="<%=request.getAttribute("fluxo")%>">  
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda"> Inclusão de Advogados </legend>
						
						<div class="col25">
						<label class="formEdicaoLabel" for="Id_UsuarioServentia">*OAB/Matrícula</label><br>  
					    <input class="formEdicaoInput" name="OabNumero" id="OabNumero" type="text" size="20" maxlength="30" value="<%=ProcessoCadastroDt.getOabNumero()%>" onkeypress="return DigitarSoNumero(this, event)" >
				       	</div>
				       	
				       	<div class="col15">
				       	<label class="formEdicaoLabel" for="Id_UsuarioServentia">*Complemento</label><br>
					    <select name="OabComplemento" id="OabComplemento" class="formEdicaoCombo" title="Complemento">
					    		<option></option>	
				        		<option>N</option>
					       		<option>A</option>
					        	<option>B</option>
			   		        	<option>S</option>
			   		        	<option selected><%=ProcessoCadastroDt.getOabComplemento()%></option>
				    	</select>
				    	</div>
				    	
				    	<div class="col15">
				    	<label class="formEdicaoLabel" for="Id_UsuarioServentia">*Estado</label><br>  
				    	<select id="OabUf" name ="OabUf" class="formEdicaoCombo">
							<option value=""></option>
							<option value="GO">GO</option>
							<option value="AC">AC</option>
							<option value="AL">AL</option>
							<option value="AM">AM</option>
							<option value="AP">AP</option>
							<option value="BA">BA</option>
							<option value="CE">CE</option>
							<option value="DF">DF</option>
							<option value="ES">ES</option>
							<option value="MA">MA</option>
							<option value="MG">MG</option>
							<option value="MS">MS</option>
							<option value="MT">MT</option>
							<option value="PA">PA</option>
							<option value="PB">PB</option>
							<option value="PE">PE</option>
							<option value="PI">PI</option>
							<option value="PR">PR</option>
							<option value="RJ">RJ</option>
							<option value="RN">RN</option>
							<option value="RO">RO</option>
							<option value="RR">RR</option>
							<option value="RS">RS</option>
							<option value="SC">SC</option>
							<option value="SE">SE</option>
							<option value="SP">SP</option>
							<option value="TO">TO</option>
							<option selected><%=ProcessoCadastroDt.getOabUf()%></option>
						</select>
						</div>
						
						<div class="clear"></div>
						
						<input name="imgSubmeter" type="submit" value="Consultar"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaOabDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('fluxo','1')">
						<br />
						
						<fieldset>
							<legend>Lista de Advogados para Habilitação</legend>
							<%
							 List liAdvogados =  (List) request.getSession().getAttribute("ListaAdvogadosHabilitacao");
							 UsuarioDt objTempAdvogado;
								
							if (liAdvogados != null && liAdvogados.size() > 0){
							%>
								<table class="Tabela" id="TabelaAvogados">
											<thead>
												<tr>
													<th></th>
													<th>Nome</th>
													<th width="30%">OAB/Matrícula</th>
													<th>Serventia</th>
													<th></th>
												</tr>
											</thead>
											<tbody>
												<%
													String stTempNome="";
													for(int f = 0 ; f< liAdvogados.size();f++) {
														objTempAdvogado = (UsuarioDt) liAdvogados.get(f); %>
														<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
										                   	<tr class="TabelaLinha1"> 
														<%}else{ stTempNome=""; %>    
										                   	<tr class="TabelaLinha2">
														<%}%>
										                   		<td> <%=f+1%></td>
									                    	<td>
									                    		<%= objTempAdvogado.getNome()%>
									                    	</td>
									                   		<td align="center">
									                   			<%= objTempAdvogado.getOabNumero()+ " - "+objTempAdvogado.getOabComplemento()%>
									                   		</td>
									                   		<td align="center">
									                   			<%= objTempAdvogado.getServentia()%>
									                   		</td>
									                   		<td align="center">
																<input type="radio" name="usuariosServentiaAdvogado" value="<%=objTempAdvogado.getId_UsuarioServentia()%>" />
															</td>
									                   		</tr>
												<%}%>
											</tbody>
											<%
									} else { %>
							   			<em> Insira Advogado(s) no processo. </em>
						   		<% 	} %>
						  	</table>
						  	<%if (liAdvogados != null && liAdvogados.size() > 0){%>
							  	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
									<input name="imgSubmeter" type="submit" value="Adicionar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaOabDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('fluxo','2')">
								</div>
							<%} %>
						</fieldset>	
						
						<fieldset id="VisualizaDados" class="VisualizaDados">   
					   		<legend>Advogado(s) Adicionado(s) </legend>
						   	<%
						   		List listaAdvogados = ProcessoCadastroDt.getListaAdvogados();
						   	    if (listaAdvogados != null && listaAdvogados.size() > 0){%>
									<table class="Tabela" id="TabelaAvogados">
												<thead>
													<tr>
														<th></th>
														<th>Nome</th>
														<th width="30%">OAB/Matrícula</th>
														<th>Serventia</th>
														<th></th>
													</tr>
												</thead>
												<tbody>
													<%
														String stTempNome="";
														for(int i = 0 ; i< listaAdvogados.size();i++) {
															UsuarioServentiaOabDt advogadoDt = (UsuarioServentiaOabDt)listaAdvogados.get(i);
															if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
											                   	<tr class="TabelaLinha1"> 
															<%}else{ stTempNome=""; %>    
											                   	<tr class="TabelaLinha2">
															<%}%>
											                   		<td> <%=i+1%></td>
										                    	<td>
										                    		<%= advogadoDt.getNomeUsuario()%>
										                    	</td>
										                   		<td align="center">
										                   			<%= advogadoDt.getOabNumero()+ " - "+advogadoDt.getOabComplemento()%>
										                   		</td>
										                   		<td align="center">
										                   			<%= advogadoDt.getServentia()%>
										                   		</td>
										                   		<td align="center">
																	<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoLista','<%=i%>');AlterarValue('PassoEditar','10')" title="Excluir Advogado"/><br />
																</td>
										                   		</tr>
													<%}%>
												</tbody>
												<%
										} else { %>
								   			<em> Insira Advogado(s) no processo. </em>
							   		<% 	} %>
							  	</table>
					</fieldset>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Concluir" onclick="AlterarValue('PassoEditar','-1')">
					</div>
						
				</fieldset>
			</div>
				<%@ include file="Padroes/Mensagens.jspf" %>
			</form>
		</div>
	</body>
</html>
	