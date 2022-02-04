<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoFaseDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDt"%>

<jsp:useBean id="Recursodt" scope="session" class= "br.gov.go.tj.projudi.dt.RecursoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Modificar Dados de Recurso  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>   
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Modificar Dados Recurso</h2></div>
		<form action="Recurso" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Dados do Recurso </legend>

					<label class="formEdicaoLabel"> Processo </label><br>
					<span class="span"> <a href="BuscaProcesso?Id_Processo=<%=Recursodt.getId_Processo()%>"><%=Recursodt.getProcessoDt().getProcessoNumeroCompleto()%></a></span>
					<br />
					<label class="formEdicaoLabel" for="Id_ProcessoTipo">*Classe 
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
					    </label><br> 
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="200" maxlength="200" value="<%=Recursodt.getProcessoTipo()%>"/><br />	
										
					<br />
					<input type="hidden" id="posicaoLista" name="posicaoLista">
				   	<fieldset id="VisualizaDados" class="VisualizaDados" style="width: 780px;margin-left: 75px;">  
   						<legend> 
   							*Assunto(s)
   							<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
   						</legend>
				   		<%
				   			List listaAssuntos = (List) request.getSession().getAttribute("ListaAssuntosRecurso");
				   	    	if (listaAssuntos != null && listaAssuntos.size() > 0){ %>
				   			<table width="98%" border="0" cellpadding="0" cellspacing="0" style="font-size: 10px !important;">
				   				<thead align="left">
				   					<tr>
				   						<th width="90%">Descrição</th>
				   						<th>Excluir</th>
				   					</tr>
				   				</thead>
							<%
				   	    		for (int i=0;i < listaAssuntos.size();i++){
				   	    			RecursoAssuntoDt assuntoDt = (RecursoAssuntoDt)listaAssuntos.get(i); %>
					   			<tbody>
									<tr>
				       					<td>
				       					<% if (assuntoDt.getIsAtivo() != null) {  
										   	  if (assuntoDt.getIsAtivo().equals("0") || assuntoDt.getIsAtivo().equals("I")) { %>
											      <font color="red"><%=assuntoDt.getAssunto() %></font>
										 <% } else { %>
												     <%=assuntoDt.getAssunto() %>
										    <% } %>
										<% } else { %>
											<%=assuntoDt.getAssunto() %>
										<% } %>
				       					
				       			</td>
				       	 				<td><input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','2');AlterarValue('posicaoLista','<%=i%>')" title="Excluir assunto"/></td>
				       	 			</tr>
				       	 		</tbody>
				       		<%	} %>
				       	</table>
				   		<% } else { %>
				   			<em> Nenhum assunto cadastrado </em>
				   		<% } %>
					</fieldset>
					<br />

 						<label class="formEdicaoLabel" for="Id_Classificador">Classificador
 						 <input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificador" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
					   	<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador','Classificador'); return false;" title="Limpar Classificador">  
 						</label><br>  
					    <input type="hidden" name="Id_Classificador" id="Id_Classificador">
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Classificador" id="Classificador" type="text" size="50" maxlength="100" value="<%=Recursodt.getProcessoDt().getClassificador()%>"/>
						<br>
						
						<div class="col45" style="margin-top:10px">
				    		<input class="formEdicaoInput" name="SegredoJustica" id="SegredoJustica"  type="checkbox"  value="true" <% if(Recursodt.getProcessoDt().getSegredoJustica().equalsIgnoreCase("true")){%>  checked="true"<%}%>/>
				    		<label for="SegredoJustica">Segredo de Justiça</label>
					    </div>
					   
					    <br />
					    <label class="formEdicaoLabel" for="Id_ProcessoFase">*Fase Processual  
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoFase" name="imaLocalizarProcessoFase" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoFaseDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
					    </label><br>
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoFase" id="ProcessoFase" type="text" size="50" maxlength="100" value="<%=Recursodt.getProcessoDt().getProcessoFase()%>"/>
	
						<%if (!Recursodt.getProcessoDt().getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL))){%>
			    			<br>
			    			<div class="col45" style="margin-top:10px">
				    			<input class="formEdicaoInput" name="Penhora" id="Penhora"  type="checkbox"  value="true" <% if(Recursodt.getProcessoDt().getPenhora().equalsIgnoreCase("true")){%>  checked<%}%>/> 
				    			<label for="Penhora">Penhora no Rosto dos Autos</label>
				    		</div>
				    		<br />
						<%} %>

					    <label class="formEdicaoLabel" for="Id_ProcessoPrioridade">Prioridade
					    <input type="hidden" name="Id_ProcessoPrioridade" id="Id_ProcessoPrioridade">  
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoPrioridade" name="imaLocalizarProcessoPrioridade" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparPrioridade" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ProcessoPrioridade','ProcessoPrioridade'); return false;" title="Limpar Prioridade do Processo">  
					   </label><br>
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoPrioridade" id="ProcessoPrioridade" type="text" size="40" maxlength="100" value="<%=Recursodt.getProcessoDt().getProcessoPrioridade()%>"/><br />
					    
			    		<label class="formEdicaoLabel" for="Valor">Valor</label><br>
			    		<input class="formEdicaoInput" name="Valor" id="Valor"  type="text" size="20" maxlength="20" value="<%=Recursodt.getProcessoDt().getValor()%>" onkeyup="MascaraValor(this);autoTab(this,20)" onkeypress="return DigitarSoNumero(this, event)"/><br />
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						<input name="imgInserir" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
					</div>
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>