<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaAfastamentoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AfastamentoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="usuarioServentiaAfastamentoDt" scope="session"
	class="br.gov.go.tj.projudi.dt.UsuarioServentiaAfastamentoDt" />
<jsp:useBean id="afastamentoDt" scope="session"
	class="br.gov.go.tj.projudi.dt.AfastamentoDt" />

<html>
<head>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />



<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
<link href="./css/form.css" type="text/css" rel="stylesheet" />
<link href="./css/font-awesome-4.4.0/css/font-awesome.min.css"
	type="text/css" rel="stylesheet" />
<script type="text/javascript" src="./js/jquery.js"></script>
<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
<script type='text/javascript' src='./js/Funcoes.js'></script>
<script language="javascript" type="text/javascript"
	src="./js/Digitacao/DigitarSoNumero.js"></script>
<script language="javascript" type="text/javascript"
	src="./js/Digitacao/AutoTab.js"></script>

</head>

<body>

	<div id="divCorpo" class="divCorpo">
		<div class="area">
			<h2>&raquo; Afastamento / Retorno do Oficial de Justiça</h2>
		</div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post"
			name="Formulario" id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden"
				value="<%=request.getAttribute("PaginaAtual")%>" /> <input
				id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden"
				value="<%=request.getAttribute("PosicaoPaginaAtual")%>" /> <input
				id="PaginaAnterior" name="PaginaAnterior" type="hidden"
				value="<%=request.getAttribute("PaginaAnterior")%>" /> <input
				name="__Pedido__" type="hidden"
				value="<%=request.getAttribute("__Pedido__")%>" /> <input
				id="idUsuarioServentiaAfastamento"
				name="idUsuarioServentiaAfastamento" type="hidden"
				value="<%=request.getAttribute("idUsuarioServentiaAfastamento")%>" />
			<input id="idUsuario" name="idUsuario" type="hidden"
				value="<%=request.getAttribute("idUsuario")%>" /> <input
				id="idUsuarioServentia" name="idUsuarioServentia" type="hidden"
				value="<%=request.getAttribute("idUsuarioServentia")%>" />
				
				<input id="Id_UsuarioServentiaAfastamento="Id_UsuarioServentiaAfastamento" type="hidden"
							value="<%=usuarioServentiaAfastamentoDt.getId()%>" />
			<%
				usuarioServentiaAfastamentoDt = (UsuarioServentiaAfastamentoDt) request
						.getAttribute("usuarioServentiaAfastamentoDt");
			%>

			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">
						<%=request.getAttribute("nomeServentia")%>
					</legend>

					<div id="divesquerda" class="col45">

                       <% 
                       String label = "";
                       String valor = "";
                       if (usuarioServentiaAfastamentoDt.getId_Usuario().equalsIgnoreCase("")) {
						  label = "Id Afastamento";
						  valor = usuarioServentiaAfastamentoDt.getId();
						} else {
							label = "Id Usuário";
							valor = usuarioServentiaAfastamentoDt.getId_Usuario();
						  }
						%>    
						
						<label class="formEdicaoLabel" for="id"> <%=label%></label> <br> <input
							class="formEdicaoInputSomenteLeitura" name="id" id="id"
							type="text" readonly="readonly" size="5"
							value="<%=valor%>" /> <br>					
						
						<br> <label class="formEdicaoLabel" for="cpf">Cpf </label> <label
							class="formEdicaoLabel" for="nomeOficial">Oficial de
							Justiça</label><br> <input class="formEdicaoInputSomenteLeitura"
							name="cpfUsuario" id="cpfUsuario" type="text" readonly="readonly"
							size="20"
							value="<%=usuarioServentiaAfastamentoDt.getCpfUsuario()%>" /> <input
							class="formEdicaoInputSomenteLeitura" name="oficialJustica"
							id="oficialJustica" readonly="readonly" type="text" size="56"
							value="<%=usuarioServentiaAfastamentoDt.getNomeUsuario()%>" /><br>
						<br> <label class="col45" for="afastamento">Afastamento</label>


						<label class="formEdicaoLabel" for="dataInicio">Data
							Início</label><br> <input class="formEdicaoInputSomenteLeitura"
							name="tipoAfastamento" id="tipoAfastamento" type="text"
							readonly="readonly" size="36"
							value="<%=usuarioServentiaAfastamentoDt.getAfastamento()%>" /> <input
							class="formEdicaoInputSomenteLeitura" name="dataInicio"
							id="dataInicio" type="text" readonly="readonly" size="20"
							value="<%=usuarioServentiaAfastamentoDt.getDataInicio()%>" /> <br>
						<br>

						<%
							if (usuarioServentiaAfastamentoDt.getAcao().equalsIgnoreCase("Afastar")) {
						%>


						<label class="formEdicaoLabel" for="tipoArquivo">*Tipo
							Afastamento</label><br> <select id="idAfastamento"
							name="idAfastamento" class="formEdicaoCombo">
							<!-- 	<option value=""></option>  -->
							<%
								List listTemp = (List) request.getAttribute("listaAfastamento");
									if (listTemp != null) {
										for (int i = 0; i < listTemp.size(); i++) {
											afastamentoDt = (AfastamentoDt) listTemp.get(i);
							%>
							<option value="<%=afastamentoDt.getId()%>"
								<%if (afastamentoDt.getId() != null
								&& afastamentoDt.getId().equals(request.getAttribute("idAfastamento"))) {%>
								selected="selected" <%}%>>
							
							<%=afastamentoDt.getAfastamento()%>
							</option>
							<%
								}
									}
							%>
						</select><br>

						<%
							}
						%>


						<%
							if (usuarioServentiaAfastamentoDt.getAcao().equalsIgnoreCase("Afastar")) {
						%>

						<br> <label class="formEdicaoLabel" for="MotivoAfastamento">Motivo
							Inicio</label><br>

						<textarea id="motivoInicio" name="motivoInicio" rows="2" cols="50"
							class="formEdicaoInput"><%=usuarioServentiaAfastamentoDt.getMotivoInicio()%></textarea>
							
						<div id="divPortaBotoes" class="divBotoesCentralizados">
						  <button onclick="">
								<%=usuarioServentiaAfastamentoDt.getAcao()%>
						  </button>
						</div>							
							
						<br>
						<%
							} else  if (usuarioServentiaAfastamentoDt.getAcao().equalsIgnoreCase("Retornar")) {
						%>

						<br> <label class="formEdicaoLabel" for="MotivoAfastamento">Motivo
							Inicio</label><br>

						<textarea id="motivoInicio" name="motivoInicio" rows="2" cols="50"
							class="formEdicaoInput" readonly> <%=usuarioServentiaAfastamentoDt.getMotivoInicio()%></textarea>

						<br> <br> <label class="formEdicaoLabel"
							for="MotivoAfastamento">Motivo Fim</label><br>

						<textarea id="motivoFim" name="motivoFim" rows="2" cols="50"
							class="formEdicaoInput"> <%=usuarioServentiaAfastamentoDt.getMotivoFim()%></textarea>
						<br>
						
						<div id="divPortaBotoes" class="divBotoesCentralizados">
							<button onclick="">
								<%=usuarioServentiaAfastamentoDt.getAcao()%>
							</button>
						</div>

						<%
							} else {
						%>
						<br> <label class="formEdicaoLabel" for="MotivoAfastamento">Numero dos Mandados Atrasados</label><br>

						<textarea id="motivoInicio" name="motivoInicio" rows="2" cols="50"
							class="formEdicaoInput" readonly> <%=usuarioServentiaAfastamentoDt.getMotivoInicio()%></textarea>
						<%}%>

						
				</fieldset>
			</div>
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf"%> 
</body>


</html>