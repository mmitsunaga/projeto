<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoDt"%>
<jsp:useBean id="ProcessoExecucaodt_PE" scope="request" class= "br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	</head>

	<body>	
		<div id="divCorpo" class="divCorpo">
			<div id="Editar">
				<fieldset id="Editar">
					<h2 align="center"> A Guia de Recolhimento foi cadastrada com sucesso! </h2>
					<br />
					<%@ include file="ProcessoExecucaoConfirmacao.jspf" %>
<!--					<blockquote id="divBotoesCentralizados" class="divBotoesCentralizados">-->
<!--						<input name="imgImprimir" type="submit" value="Imprimir" OnClick="javascript:Imprimir()"> -->
<!--					</blockquote>-->
				</fieldset>
			</div>
		</div>
	</body>
</html>	