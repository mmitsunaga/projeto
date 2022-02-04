<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoPrisaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoDt"%>

<jsp:useBean id="MandadoPrisaodt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoPrisaoDt"/>
<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>
<html>
<head>
	<title> |<%=request.getAttribute("tempPrograma")%> </title>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarNumeroProcesso.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	  	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	  	<script language="javascript" type="text/javascript" src="./js/Digitacao/EvitarEspacoDuplo.js" ></script>

	<%@ include file="js/InsercaoArquivo.js"%>	
	<script type="text/javascript" src="./js/FormProcessoParteCrimes.js"></script>
	
</head>

<body >
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo;<%=request.getAttribute("titulo")%></h2></div>
		<form action="MandadoPrisao" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">

			<%String conteudoArquivosSemAspasDuplas = String.valueOf(request.getAttribute("textoEditor")).replaceAll("\"","ASPAS_DUPLAS").replaceAll("&","&amp;");%>
			<input id="conteudoArquivos" name="conteudoArquivos" type="hidden" value="<%=conteudoArquivosSemAspasDuplas%>" />

			<div id="divEditar" class="divEditar">

				<!--INÍCIO DADOS DO PROCESSO-->
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend> Processo </legend>
					<div> N&uacute;mero</div>
					<span> <a href="BuscaProcesso?Id_Processo=<%=processoDt.getId()%>&PassoBusca=2"><%=processoDt.getProcessoNumeroCompleto()%></a></span>
					<div> Área</div>
					<span class="span2"> <%=processoDt.getArea()%></span><br />
				</fieldset>
				<!--FIM DADOS DO PROCESSO-->


				<!--INÍCIO DADOS MANDADO DE PRISÃO-->
				<fieldset id="formEdicao" class="formEdicao">
					<legend>Dados do Mandado de Prisão</legend>

					<label class="formEdicaoLabel" for="NumeroMandado" style="width:210px">Número do Mandado</label><br>
					<%if (MandadoPrisaodt.getMandadoPrisaoNumero().length() > 0){ %>
					<span class="span"><b>&nbsp; &nbsp;<%=processoDt.getProcessoNumeroCompleto() + "." + MandadoPrisaodt.getMandadoPrisaoNumero()%></b></span><br />
					<%} %>
					<br />

<!-- 					<label class="formEdicaoLabel" for="DataPrisao" style="width:210px">Data da Prisão</label><br>     -->
<%-- 				    <input class="formEdicaoInput" name="DataPrisao" id="DataPrisao"  type="text" size="12" maxlength="10" value="<%=MandadoPrisaodt.getDataPrisao()%>" onkeypress="return formataCampo(event, this, 10)">  --%>
<!-- 					<br /> -->
					<input type='hidden' id='Id_ProcessoParte' name='Id_ProcessoParte' value='<%=MandadoPrisaodt.getId_ProcessoParte()%>' />
					<input type='hidden' id='Id_PrisaoTipo' name='Id_PrisaoTipo' value='<%=MandadoPrisaodt.getProcessoPartePrisaoDt().getId_PrisaoTipo()%>' />
					<input type='hidden' id='Id_LocalCumpPena' name='Id_LocalCumpPena' value='<%=MandadoPrisaodt.getProcessoPartePrisaoDt().getId_LocalCumpPena()%>' />												
																									
					<blockquote class='col10'>
						<label  for='DataPrisao'>*Dt.Pris&atilde;o
							<img class='imgMargeLeft' src='./imagens/dlcalendar_2.gif' height='13' width='13' title='Calendário'  alt='Calendário' onclick="displayCalendar(document.forms[0].DataPrisao,'dd/mm/yyyy',this)"/>
						</label> 
						<input class='formEdicaoInput' name='DataPrisao'  id='DataPrisao' type='text' size='10' maxlength='10' onkeyup='mascara_data(this)' onblur='verifica_data(this)' onkeypress='return DigitarSoNumero(this, event)' value='<%=MandadoPrisaodt.getProcessoPartePrisaoDt().getDataPrisao()%>'>
					</blockquote>
					<blockquote class='col25'>
						<label  for='id'>*Pris&atilde;o Tipo <img class='imgMargeLeft' src='./imagens/imgLocalizarPequena.png' onclick="MostrarBuscaPadrao('Formulario','PrisaoTipo','Consulta de Tipo de Pris&atilde;o', 'Digite o Tipo de Pris&atilde;o e clique em consultar.', 'Id_PrisaoTipo', 'PrisaoTipo', ['Pris&atilde;o'], [], '2', '15')" ></label>
						<input class='formEdicaoInputSomenteLeitura' name='PrisaoTipo'  id='PrisaoTipo' size='45' type='text'   value='<%=MandadoPrisaodt.getProcessoPartePrisaoDt().getPrisaoTipo()%>'>
					</blockquote>
					<blockquote class='col10'>
						<label  for='PrazoPrisao'>Prazo</label>
						<input class='formEdicaoInput' name='PrazoPrisao'  id='PrazoPrisao' size='10' type='text' value='<%=MandadoPrisaodt.getProcessoPartePrisaoDt().getPrazoPrisao()%>' onkeypress='return DigitarSoNumero(this, event)' onkeyup='autoTab(this,22)'/>
					</blockquote>
					<blockquote class='col35'>
						<label  for='LocalCumpPena'>*Local<img class='imgMargeLeft' src='./imagens/imgLocalizarPequena.png' onclick="MostrarBuscaPadrao('Formulario','LocalCumprimentoPena','Consulta de Local de Cumprimento de Pena', 'Digite o nome do Local.', 'Id_LocalCumpPena', 'LocalCumpPena', ['Local'], [], '2', '15')" ></label> 
						<input class='formEdicaoInputSomenteLeitura' name='LocalCumpPena'  id='LocalCumpPena' size='60' type='text'  readonly value='<%=MandadoPrisaodt.getProcessoPartePrisaoDt().getLocalCumpPena()%>'>
					</blockquote>
					<br />					
<!-- 					<blockquote class='col60'> -->
<!-- 						<label  for='Email'>*Observacao</label> -->
<%-- 						<input class='formEdicaoInput' name='Observacao' id='Observacao'  type='text' size='60' maxlength='255' value='<%=MandadoPrisaodt.getProcessoPartePrisaoDt().getObservacao()%>' onkeyup='autoTab(this,60)'> --%>
<!-- 					</blockquote> -->
			
				</fieldset>		
				<br />	
				<!--  Inserção de Arquivos com opção de usar Editor de Modelos -->
				<%@ include file="Padroes/InsercaoArquivosAssinador.jspf"%>				
				<!--FIM DADOS MANDADO DE PRISÃO-->				
				<div id="divConfirmarSalvar" class="ConfirmarSalvar">
					<input id="concluir" name="concluir" type="hidden" value="">
					<button type="submit" name="operacao" value="Cumprir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>');AlterarValue('tempFluxo1','<%=request.getAttribute("tempFluxo1")%>');AlterarValue('concluir','true');" >
						<!-- <img src="imagens/22x22/ico_sucesso.png" /> -->
						<%=request.getAttribute("descBotao")%>
					</button>
				</div>		
				<br />
			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
		 	
</body>
<script type="text/javascript">
		$('.corpo').hide();		
	</script>
</html>

