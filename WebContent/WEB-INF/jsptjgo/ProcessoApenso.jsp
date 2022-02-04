<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   	<script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>   
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Apensar / Criar Dependência Processo </h2></div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
			   		<legend class="formEdicaoLegenda"> Apensar / Criar Dependência Processo </legend>
			   		
			   		<br />
			   		<label for="Aviso" style="float:left;color:red;">ATENÇÃO: o processo <%=processoDt.getProcessoNumero()%> será o APENSO ou DEPENDENTE.</label><br>
			   		<label class="formEdicaoLabel"> Número do Processo </label><br>
					<span class="destaque"><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span/>
	    			<br />
	    			<label class="formEdicaoLabel"> Serventia </label><br>
	    			<span class="destaque"><%=processoDt.getServentia()%></span/>
	    			<br /><br />
	    			<% if (!processoDt.isDependente()) {  %>
		    			<label class="formEdicaoLabel" >Tipo de Vínculo &nbsp;</label><br>		
						<% if(request.getAttribute("apensoDependente").toString().length() > 0 && !request.getAttribute("apensoDependente").toString().equalsIgnoreCase("selecionar")) {%>
							<input class="formEdicaoInputSomenteLeitura" readonly name="apensoDependente" id="apensoDependente" value=<%=request.getAttribute("apensoDependente")%>>
						<% } else { %>
						 	<select id="apensoDependente" name="apensoDependente">
						 		<option value="selecionar" >Selecionar</option>
		  						<option value="apenso" >Apenso</option>
		  						<option value="dependente" >Dependente</option>
						    </select>
						<% } %>	
						<br />
						<label class="formEdicaoLabel" for="ProcessoNumeroDependente" >Processo Principal</label><br>	
					    <input class="formEdicaoInput" name="ProcessoNumeroDependente" id="ProcessoNumeroDependente" type="text" size="25" maxlength="25" onkeypress="return DigitarNumeroProcesso(this, event)" onkeyup="mascara(this, '#######.##.####.#.##.####'); autoTab(this,25)" value="<%=request.getAttribute("ProcessoNumeroDependente")%>">
			    		<em><strong> Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em><br />
		    			<br />
		    			<% if(request.getAttribute("ocultarSalvar") == null) {%>
			    			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input name="imgInserir" type="submit" value="Apensar/Criar Dependência" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');">
							</div>
						<% } %>
					<% } else { %>
						<input type="hidden" id="apensoDependente" name="apensoDependente" value="<%=request.getAttribute("apensoDependente")%>">
						<input type="hidden" id="Id_ProcessoDependente" name="Id_ProcessoDependente" value="<%=request.getAttribute("Id_ProcessoDependente")%>">
						<input type="hidden" id="ProcessoNumeroDependente" name="ProcessoNumeroDependente" value="<%=request.getAttribute("ProcessoNumeroDependente")%>">
						<span class="destaque" style="float:left;margin-left:50x;">Processo é <%=request.getAttribute("apensoDependente")%> do processo: <a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_ProcessoPrincipal()%>"><%=processoDt.getProcessoNumeroPrincipal()%></a></span>
						<input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('Id_ProcessoDependente','<%=processoDt.getId()%>');AlterarValue('ProcessoNumeroDependente','<%=processoDt.getProcessoNumeroPrincipal()%>');AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');" title="cancelar <%=request.getAttribute("apensoDependente")%> de processo">
					<% } %>	
					
				</fieldset>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
