<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>

<jsp:useBean id="ServentiaCargodt" scope="session" class= "br.gov.go.tj.projudi.dt.ServentiaCargoDt"/>

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.CargoTipoDt"%>
<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<title>Estrutura de Cargos de uma Serventia</title>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
		<script type="text/javascript" src="./js/jquery.js"> </script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type='text/javascript' src='./js/Funcoes.js'></script>
	</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Estrutura de Cargos </h2></div>
			<form action="ServentiaCargo" method="post" name="Formulario" id="Formulario">
					
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />  
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="Id_ServentiaCargo" name="Id_ServentiaCargo" type="hidden" value="" />
						
				<div id="divEditar" class="divEditar">
				
				  	<%
			    	String id_Serventia="";
			  		String id_CargoTipo="";
			        ServentiaCargoDt serventiaCargoDt;
					        
			        //Pega lista de todas as Serventias e Cargos
			        List liTemp = (List)request.getAttribute("listaServentiaCargos");
			        if (liTemp.size() > 0){
			        	for(int i = 0 ; i< liTemp.size();i++) {
			            	serventiaCargoDt = (ServentiaCargoDt)liTemp.get(i);

			            	//Se cargo atual é diferente do anterior e mudou serventia fecha tabela e fieldset
			            	if ((!id_CargoTipo.equals(serventiaCargoDt.getId_CargoTipo()) && (!id_CargoTipo.equals(""))) ||
			            			(!id_Serventia.equals(serventiaCargoDt.getId_Serventia()) && !id_Serventia.equals(""))){ %> 
			               		</tbody></table></fieldset> 
			               	<%	}
			               		
			                //Se serventia atual é diferente da anterior abre fieldset e fecha anterior	
			                if (!id_Serventia.equals(serventiaCargoDt.getId_Serventia())) {
			                	if (!id_Serventia.equals("")){ %> </fieldset> <%}	
				                    id_Serventia = serventiaCargoDt.getId_Serventia();
				                    id_CargoTipo = "";
				  	%>
						 
				    		<h3> <%= serventiaCargoDt.getServentia() %></h3>
				  			<%} 
			                //Se mudar o cargo na mesma serventia deve abrir novo fieldset
			                if (!id_CargoTipo.equals(serventiaCargoDt.getId_CargoTipo())){ 
				                id_CargoTipo = serventiaCargoDt.getId_CargoTipo();
				           	%>
				            	<fieldset class="formEdicao"> 
				                	<legend class="formEdicaoLegenda"> <%= serventiaCargoDt.getCargoTipo() %> </legend>
			                       	<table class="Tabela">
										<thead>
											<tr>
												<th width="30%">Cargo</th>
												<th>Usuário</th>
												<th width="10%" align="center">Status</th>
												<th class="colunaMinima" title="Selecione um Cargo para Edição" align="center">Editar</th>
												<th class="colunaMinima" title="Selecione um Cargo para Limpar" align="center">Limpar</th>
												<th class="colunaMinima" title="Selecione um Cargo para Excluir" align="center">Excluir</th>
											</tr>	
										</thead>
										<tbody>
							<%	} %>
										<tr>
											<td><%=serventiaCargoDt.getServentiaCargo()%> - ID: <%=serventiaCargoDt.getId()%></td>
											<td><%=(!serventiaCargoDt.getNomeUsuario().equals("")?serventiaCargoDt.getNomeUsuario():"<em>Nenhum</em>")%></td>
											<td align="center"><strong style="color:#346504"><%=(serventiaCargoDt.getCodigoTemp().equals("-1")?"<font color='red'>Inativo</font>":"Ativo")%></strong></td>
											<td align="center">
												<input name="formLocalizarimgEditar" type="image" src="./imagens/imgEditar.png" onclick="AlterarValue('Id_ServentiaCargo','<%=serventiaCargoDt.getId()%>');AlterarAction('Formulario', 'TrocaServentiaCargo');AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');" title="Editar Cargo" />
											</td>
											<% if (serventiaCargoDt.getCodigoTemp().equals(String.valueOf(ServentiaCargoDt.OCUPADO))) { %>
											<td align="center">
												<input name="formLocalizarimgEditar" type="image" src="./imagens/imgLimpar.png" onclick="AlterarValue('Id_ServentiaCargo','<%=serventiaCargoDt.getId()%>');AlterarAction('Formulario', 'TrocaServentiaCargo');AlterarValue('PaginaAtual', '<%=Configuracao.LocalizarAutoPai%>');" title="Limpar Cargo" />
											</td>
											<% } else { %>
											<td align="center">-</td>
											<% } %>
											<td align="center">
												<input name="formLocalizarimgExcluir" type="image" src="./imagens/imgExcluir.png" onclick="AlterarValue('Id_ServentiaCargo','<%=serventiaCargoDt.getId()%>');AlterarAction('Formulario', 'TrocaServentiaCargo');AlterarValue('PaginaAtual', '<%=Configuracao.Excluir%>');" title="Excluir Cargo" />
											</td>
										</tr>
					           <%
					               }//fim for
					           %>
					           			</tbody>
					           		</table>
					           </fieldset>
					      	</fieldset>
					<% } else {%>
					<fieldset class="formEdicao"> 
					<em> Nenhum Cargo foi cadastrado. </em>
					
					<% } %>
         		</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>