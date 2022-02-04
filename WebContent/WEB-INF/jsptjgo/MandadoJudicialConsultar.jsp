<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoJudicialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoTipoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="mandadoJudicialDt" scope="session" class="br.gov.go.tj.projudi.dt.MandadoJudicialDt"/>
<jsp:useBean id="pendenciaDt" 		scope="session" class="br.gov.go.tj.projudi.dt.PendenciaDt"/>
<jsp:useBean id="processoParteDt" 	scope="session" class="br.gov.go.tj.projudi.dt.ProcessoParteDt"/>
<jsp:useBean id="processoDt" 		scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Distribuir  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script language="javascript" type="text/javascript">
	function VerificarCampos(Formulario) {
		if(SeNulo(Formulario.novoStatus, "Informe o Novo Status do Mandado Judicial!")) {
			return false;
		}
		if(SeNulo(Formulario.idLocomocaoFrutiferas, "Informe a Quantidade de Locomoções Frutíferas!")) {
			return false;
		}
		if(SeNulo(Formulario.idLocomocaoInfrutiferas, "Informe a Quantidade de Locomoções Infrutíferas!")) {
			return false;
		}
		if(!validarRadioButtons(Formulario.locomocaoHoraMarcada,"Informe se este foi necessário hora marcada!")) {
			return false;
		}
		return true;
	}
	/*
	function preencherLocomocoes() {
		Mostrar('divFinalizar');
	}
	*/
	</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Mandado Judicial</h2></div>
		
		<form id="Formulario" name="Formulario" action="<%=request.getAttribute("tempRetorno")%>" method="post" OnSubmit="JavaScript:return VerificarCampos(document.Formulario)">
			<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
			
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgSalvar" class="imgSalvar" title="Salvar - Alterar status do Mandado Judicial" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>');"/>
				<input id="imgLimpar" style="background-image: url('./imagens/imgLimpar.png');width:24px;height:24px;" title="Limpar - Limpar Formulário" name="imgLimpar" type="reset"  value="" src="./imagens/imgLimpar.png" />
			</div>
			
			<div id="divEditar" class="divEditar">
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend class="formEdicaoLegenda">Dados do Mandado Judicial</legend>
					<br/>
					
					<div>Número Processo&nbsp;&nbsp;</div>
					<span class="span1"><%=pendenciaDt.getProcessoNumero()%></span>
					<br />
					
					<div>Natureza&nbsp;&nbsp;</div>
					<span class="span1"><%=processoDt.getProcessoTipo().toUpperCase()%></span>
					<br/>
					
					<div>Pendência Tipo&nbsp;&nbsp;</div>
					<span class="span1"><%=pendenciaDt.getPendenciaTipo().toUpperCase()%></span>
					<br />
					
					<div>Requerido&nbsp;&nbsp;</div>
					<span class="span1"><%=pendenciaDt.getNomeParte()%></span>
					<br/>
					
					<div>Endereço&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getLogradouro()%></span>
					<br/>
					
					<div>Complemento&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getComplemento()%></span>
					<br/>
					
					<div>Bairro&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getBairro()%></span>
					<br/>
					
					<div>CEP&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getCep()%></span>
					<br/>
					
					<div>Localidade&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getCidade()%></span>
					<br/>
					
					<div>UF&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getEnderecoParte().getUf()%></span>
					<br/>
					
					<div>CPF/CGC&nbsp;&nbsp;</div>
					<span class="span1"><%=processoParteDt.getCpfCnpj()%></span>
					<br/>
				</fieldset>
			</div>
			<br/>
			<div id="divEditar" class="divEditar">
				<fieldset id="VisualizaDados" class="VisualizaDados">
<%-- 					<legend class="formEdicaoLegenda">Status | <b><%=mandadoJudicialDt.getMandadoJudicialStatusDt().getMandadoJudicialStatus() %></b></legend> --%>
					
					<div>Assistência&nbsp;&nbsp;</div>
					<span class="span1"><%=(mandadoJudicialDt.getAssistencia().equals(MandadoJudicialDt.SIM_ASSISTENCIA)?"SIM":"NÃO")%></span>
					
					<br/>
					
					<div>Oficial Companheiro&nbsp;&nbsp;</div>
					<span class="span1"><%=(mandadoJudicialDt.getUsuarioServentiaDt_2() == null?"NÃO":"SIM")%></span>
			       	
			       	<br/>
			       	
		       		<div>Tipo de Mandado&nbsp;&nbsp;</div>
			       	<span class="span1"><%=mandadoJudicialDt.getMandadoTipo()%></span>
			       	
				</fieldset>
			</div>
			<br/>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend id="formLocalizarLegenda" class="formEdicaoLegenda">Alterar Status do Mandado Judicial</legend>
			       	
		       		<label id="novoStatusMandadoJudicialLabel" for="novoStatusMandadoJudicial" class="formEdicaoLabel">*Status do Mandado Judicial</label><br>
			       	<select id="novoStatus" name="novoStatus" class="formEdicaoInput" onChange="preencherLocomocoes();">
			       		<option value=""></option>
				       	<%
				       	List liTemp = (List)request.getAttribute("listaStatusMandadoJudicial");
				       	for(int i = 0; i < liTemp.size(); i++ ) {
				       		MandadoJudicialStatusDt mandadoJudicialStatusDt = (MandadoJudicialStatusDt)liTemp.get(i);
				       		%>
				       		<option value="<%=mandadoJudicialStatusDt.getId()%>"><%=mandadoJudicialStatusDt.getMandadoJudicialStatus()%></option>
				       		<%
				       	}
				       	%>
			       	</select>
			       	<!-- 
			       	<div id="divFinalizar" style="display:none;">
			       	 -->
			       		<br/>
			       		
			       		<label id="locomocaoFrutiferasLabel" for="idLocomocaoFrutiferas" class="formEdicaoLabel">*Locomoções Frutíferas</label><br>
			       		<input type="text" id="idLocomocaoFrutiferas" name="idLocomocaoFrutiferas" class="formEdicaoInput" size="10" maxlength="2" onkeypress="return DigitarSoNumero(this, event)" value="" />
			       		
			       		<br/>
			       		
			       		<label id="locomocaoInfrutiferasLabel" for="idLocomocaoInfrutiferas" class="formEdicaoLabel">*Locomoções Infrutíferas</label><br>
			       		<input type="text" id="idLocomocaoInfrutiferas" name="idLocomocaoInfrutiferas" class="formEdicaoInput" size="10" maxlength="2" onkeypress="return DigitarSoNumero(this, event)" value="" />
			       		
			       		<br/>
			       		
			       		<label id="locomocaoHoraMarcadaLabel" for="locomocaoHoraMarcada" class="formEdicaoLabel">*Locomoção com Hora Marcada?</label><br>
						&nbsp;&nbsp;
						<input type="radio" id="idLocomocaoHoraMarcada" name="idLocomocaoHoraMarcada" class="formEdicaoInput" value="<%=MandadoJudicialDt.SIM_LOCOMOCAO_HORA_MARCADA%>" />Sim
				       	<input type="radio" id="idLocomocaoHoraMarcada" name="idLocomocaoHoraMarcada" class="formEdicaoInput" value="<%=MandadoJudicialDt.NAO_LOCOMOCAO_HORA_MARCADA%>" checked />Não
				    <!-- 
			       	</div>
			       	 -->
			       	
				</fieldset>
			</div>
			
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			
		</form>
	</div>
</body>
</html>