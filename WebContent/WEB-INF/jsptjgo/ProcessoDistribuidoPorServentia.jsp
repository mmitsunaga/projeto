<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<jsp:useBean id="Relatorio1" scope="session"
	class="br.gov.go.tj.projudi.dt.relatorios.ProcessoDistribuidoPorServentiaDt" />
<html>
<head>
<title><%=request.getAttribute("tempPrograma")%></title>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
<style type="text/css">
@import url('./css/Principal.css');

@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
</style>


<script type='text/javascript' src='./js/Funcoes.js'></script>
<script type='text/javascript' src='./js/jquery.js'></script>
<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
<script language="javascript" type="text/javascript"
	src="./js/Digitacao/DigitarSoNumero.js"></script>
<script language="javascript" type="text/javascript"
	src="./js/Digitacao/AutoTab.js"></script>
<script type="text/javascript"
	src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>

</head>
<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area">
			<h2>
				&raquo;
				<%=request.getAttribute("tempPrograma")%>
			</h2>
		</div>
		<form action=" <%=request.getAttribute("tempRetorno")%>" method="post"
			name="Formulario" id="Formulario">
			
			 <input id="botaoServentia" name="botaoServentia" type="hidden"
				value="<%=request.getAttribute("botaoServentia")%>" />
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden"
				value="<%=request.getAttribute("PaginaAtual")%>" /> <input
				name="TituloPagina" type="hidden"
				value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" class="imgNovo" title="Nova Consulta"
					name="imaNovo" type="image" src="./imagens/imgNovo.png"
					onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')">
				<input id="imgImprimir" class="imgImprimir" title="Gerar Relatório"
					name="imgImprimir" type="image" src="./imagens/imgImprimir.png"
					onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">

			</div/>
			<br />
			<div id="divEditar" class="divEditar">

				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Área de Distribuição</legend>
					<label class="formEdicaoLabel" for="Id_AreaDistribuicao">*Área
						de Distribuição <input class="FormEdicaoimgLocalizar"
						id="imaLocalizarAreaDistribuicao"
						name="imaLocalizarAreaDistribuicao" type="image"
						src="./imagens/imgLocalizarPequena.png"
						onclick="AlterarValue('PaginaAtual','<%=String
					.valueOf(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')">
						<input class="FormEdicaoimgLocalizar"
						id="imaLimparAreaDistribuicao" name="imaLimparAreaDistribuicao"
						type="image" src="./imagens/16x16/edit-clear.png"
						onclick="LimparChaveEstrangeira('Id_AreaDistribuicao','AreaDistribuicao'); return false;" />
					</label><br> <input id="Id_AreaDistribuicao"
						name="Id_AreaDistribuicao" type="hidden"
						value="<%=Relatorio1.getIdAreaDistribuicao()%>" /> <input
						class="formEdicaoInputSomenteLeitura" id="AreaDistribuicao"
						name="AreaDistribuicao" readonly="true" type="text" size="60"
						maxlength="60" value="<%=Relatorio1.getAreaDistribuicao()%>" /><br />
				</fieldset>
				
	 	      <%
				  String statusBotao = "";
				  								
			    	if (!request.getAttribute("imaLocalizarServentia").equals("disabled"))	{
		
			    	//	statusBotao = "disabled id='imaLocalizarServentia'";
				    
				
				%>		  	

				<fieldset class="formEdicao" id="fieldsetServentia">
					<legend class="formEdicaoLegenda">Serventia</legend>
					<label class="formEdicaoLabel" for="Id_Serventia">Serventia
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" <%=statusBotao%>
						name="imaLocalizarServentia" type="image"
						src="./imagens/imgLocalizarPequena.png"
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')">
						<input class="FormEdicaoimgLocalizar" id="imaLimparServentia"
						name="imaLimparServentia" type="image"
						src="./imagens/16x16/edit-clear.png"
						onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" />
					</label><br> <input id="Id_Serventia" name="Id_Serventia"
						type="hidden" value="<%=Relatorio1.getIdServentia()%>" /> <input
						class="formEdicaoInputSomenteLeitura" id="Serventia"
						name="Serventia" readonly="true" type="text" size="60"
						maxlength="60" value="<%=Relatorio1.getServentia()%>" /><br />
				</fieldset>

                <%
			    	}
                %>




				<fieldset class="formEdicao" id="fieldsetUsuario">
					<legend class="formEdicaoLegenda">Usuário</legend>
					<label class="formEdicaoLabel" for="Id_Usuario">Usuário <input
						class="FormEdicaoimgLocalizar" id="imaLocalizarUsuario"
						name="imaLocalizarUsuario" type="image"
						src="./imagens/imgLocalizarPequena.png"
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')">
						<input class="FormEdicaoimgLocalizar" id="imaLimparUsuario"
						name="imaLimparUsuario" type="image"
						src="./imagens/16x16/edit-clear.png"
						onclick="LimparChaveEstrangeira('Id_Usuario','Usuario'); return false;" />
					</label><br> <input id="Id_Usuario" name="Id_Usuario" type="hidden"
						value="<%=Relatorio1.getIdUsuario()%>" /> <input
						class="formEdicaoInputSomenteLeitura" id="Serventia"
						name="Usuario" readonly="true" type="text" size="60"
						maxlength="60" value="<%=Relatorio1.getUsuario()%>" /><br />
				</fieldset>

				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Período</legend>
					<label class="formEdicaoLabel" for="DataInicial">*Data
						Inicial</label><br> <input class="formEdicaoInput" name="DataInicial"
						id="DataInicial" type="text" size="10" maxlength="10"
						value="<%=Relatorio1.getDataInicial()%>"
						onkeyup="mascara_data(this)" onblur="verifica_data(this)">
					<img id="calendarioDataInicial" src="./imagens/dlcalendar_2.gif"
						height="13" width="13" title="Calendário" alt="Calendário"
						onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)" />
					<br /> <label class="formEdicaoLabel" for="DataFinal">*Data
						Final</label><br> <input class="formEdicaoInput" name="DataFinal"
						id="DataFinal" type="text" size="10" maxlength="10"
						value="<%=Relatorio1.getDataFinal()%>"
						onkeyup="mascara_data(this)" onblur="verifica_data(this)">
					<img id="calendarioDataFinal" src="./imagens/dlcalendar_2.gif"
						height="13" width="13" title="Calendário" alt="Calendário"
						onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)" /><br />
				</fieldset>

				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Tipo de Relatório</legend>
					<label class="formEdicaoLabel" for="tipo_Relatorio">*Tipo
						de Relatório</label><br> <input type="radio" name="TipoRelatorio"
						id="TipoRelatorio" value="1" checked="true" />Sintetico <input
						type="radio" name="TipoRelatorio" id="TipoRelatorio" value="2" />Analítico
				</fieldset>
			</div>
			<%
				if (request.getAttribute("MensagemOk").toString().trim().equals("") == false) {
			%>
			<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%
				}
			%>
		</form>
		<%@ include file="Padroes/Mensagens.jspf"%>
	</div>
</body>
</html>