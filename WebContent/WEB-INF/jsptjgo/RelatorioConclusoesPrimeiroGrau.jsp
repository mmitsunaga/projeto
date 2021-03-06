<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioConclusoesPrimeiroGrauDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="java.util.HashMap"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="relatorioConclusoesPrimeiroGrauDt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioConclusoesPrimeiroGrauDt"/>
<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		
		<script language="javascript" type="text/javascript">
		function atualizarCamposTela(tipoRelatorio) {
			if(tipoRelatorio == <%=RelatorioConclusoesPrimeiroGrauDt.REALIZADAS%> || tipoRelatorio == <%=RelatorioConclusoesPrimeiroGrauDt.RECEBIDAS%>){
				$("#fieldsetData").css('display','block');
			} else if(tipoRelatorio == <%=RelatorioConclusoesPrimeiroGrauDt.PENDENTES%>){
				$("#fieldsetData").css('display','none');
				document.getElementById('AnoInicial').value = '';
				document.getElementById('MesInicial').value = '';
				document.getElementById('AnoFinal').value = '';
				document.getElementById('MesInicial').value = '';
			}
		}
		</script>
		
	</head>
	<body onload="javascript:atualizarCamposTela(<%=relatorioConclusoesPrimeiroGrauDt.getTipoRelatorio()%>);">
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="RelatorioConclusoesPrimeiroGrau" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relat?rio" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	

			</div/><br />
			<div id="divEditar" class="divEditar">

				<input type="hidden" id="idServentia" name="IdServentia" value="<%=relatorioConclusoesPrimeiroGrauDt.getIdServentia()%>"/>
				<input type="hidden" id="Serventia" name="Serventia" value="<%=relatorioConclusoesPrimeiroGrauDt.getServentia()%>"/>
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">*Tipo de Relat?rio</legend>
					<label class="formEdicaoLabel" for="TipoRelatorio">Tipo de Relat?rio</label><br>  
				    <!-- N?o pode ser consultadas as realizadas, pois deve ser tirado no relat?rio de pend?ncias (anal?tico e sum?rio)-->
				    <input type="radio" name="TipoRelatorio" value="<%=RelatorioConclusoesPrimeiroGrauDt.RECEBIDAS%>" onclick="javascript:atualizarCamposTela(this.value);"/>Recebidas
					<input type="radio" name="TipoRelatorio" value="<%=RelatorioConclusoesPrimeiroGrauDt.PENDENTES%>" onclick="javascript:atualizarCamposTela(this.value);" />Pendentes 
			    </fieldset>
				
				<fieldset class="formEdicao" id="fieldsetData"> 
					
					<legend class="formEdicaoLegenda">Per?odo</legend>
					<div class="periodo">
					<label for="DataInicial">*M?s/Ano Inicial</label><br> 
					
					<select id="MesInicial" name ="MesInicial" class="formEdicaoCombo">
						<option value="1" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("1")){%>selected="true"<%}%>>Janeiro</option>
						<option value="2" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
						<option value="3" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("3")){%>selected="true"<%}%>>Mar?o</option>
						<option value="4" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("4")){%>selected="true"<%}%>>Abril</option>
						<option value="5" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("5")){%>selected="true"<%}%> >Maio</option>
						<option value="6" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("6")){%>selected="true"<%}%>>Junho</option>
						<option value="7" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("7")){%>selected="true"<%}%>>Julho</option>
						<option value="8" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("8")){%>selected="true"<%}%>>Agosto</option>
						<option value="9" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("9")){%>selected="true"<%}%>>Setembro</option>
						<option value="10" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("10")){%>selected="true"<%}%>>Outubro</option>
						<option value="11" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("11")){%>selected="true"<%}%>>Novembro</option>
						<option value="12" <%if(relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("12")){%>selected="true"<%}%>>Dezembro</option>
					</select>
					<select id="AnoInicial" name ="AnoInicial" class="formEdicaoCombo" >
					<%
					for(int i = Funcoes.StringToInt(relatorioConclusoesPrimeiroGrauDt.getAnoInicial().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(relatorioConclusoesPrimeiroGrauDt.getAnoInicial().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
							</div>
							<div class="periodo">			
					<label for="DataFinal">*M?s/Ano Final</label><br> 
					<select id="MesFinal" name ="MesFinal" class="formEdicaoCombo">
						<option value="1" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("1")){%>selected="true"<%}%>>Janeiro</option>
						<option value="2" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
						<option value="3" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("3")){%>selected="true"<%}%>>Mar?o</option>
						<option value="4" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("4")){%>selected="true"<%}%>>Abril</option>
						<option value="5" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("5")){%>selected="true"<%}%> >Maio</option>
						<option value="6" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("6")){%>selected="true"<%}%>>Junho</option>
						<option value="7" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("7")){%>selected="true"<%}%>>Julho</option>
						<option value="8" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("8")){%>selected="true"<%}%>>Agosto</option>
						<option value="9" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("9")){%>selected="true"<%}%>>Setembro</option>
						<option value="10" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("10")){%>selected="true"<%}%>>Outubro</option>
						<option value="11" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("11")){%>selected="true"<%}%>>Novembro</option>
						<option value="12" <%if(relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("12")){%>selected="true"<%}%>>Dezembro</option>
					</select>
					<select id="AnoFinal" name ="AnoFinal" class="formEdicaoCombo">
					<%
					for(int i = Funcoes.StringToInt(relatorioConclusoesPrimeiroGrauDt.getAnoFinal().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(relatorioConclusoesPrimeiroGrauDt.getAnoFinal().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
					</div>
					<label for="Aviso" style="float:left;margin-left:25px"><small>* Per?odo n?o pode superar 03(tr?s) meses.</small></label><br> 
				
				</fieldset>
				
			  </div>
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>