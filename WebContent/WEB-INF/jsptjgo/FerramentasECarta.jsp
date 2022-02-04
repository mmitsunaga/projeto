<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='/js/jquery.js'></script>
		<script type='text/javascript' src='/js/ui/jquery-ui.min.js'></script>  
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" enctype="multipart/form-data" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<input type="hidden" id="tempFluxo1" name="tempFluxo1"  value="<%=request.getAttribute("tempFluxo1")%>"/>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Histórico</legend>
					<div>						
						<label class="formEdicaoLabel" for="Id_Pendencia6">ID_PEND</label>												
						<input class="formEdicaoInput" id="Id_Pendencia6" name="Id_Pendencia6" value="<%=request.getAttribute("Id_Pendencia6")%>" type="text" size="60" maxlength="60"/>
						<br />		
						<label class="formEdicaoLabel" for="CodRastreamento6">COD_RASTREAMENTO</label>												
						<input class="formEdicaoInput" id="CodRastreamento6" name="CodRastreamento6" value="<%=request.getAttribute("CodRastreamento6")%>" type="text" size="60" maxlength="60"/>
						<br />
						<label class="formEdicaoLabel" for="Intervalo6">INTERVALO(DIAS)</label>												
						<input class="formEdicaoInput" id="Intervalo6" name="Intervalo6" value="<%=request.getAttribute("Intervalo6")%>" type="text" size="60" maxlength="60"/>
						<br />
						<% 
						String idPendencia = "";
						String codRastreamento = "";
						String[][] historico = (String[][]) request.getAttribute("HistoricoCarta");
						if(historico != null) {	
							for (int j = 1; j < historico.length; j++) {
								if(historico[j][0] != null && historico[j][0].contains("ecibo")) { %>							
									<table style='width:100%;text-align:center;font-size:78%;' border='1' cellpadding='2' cellspacing='1'>
										<tr><th colspan='5'>**********************************************************<%=historico[j][0]%>**********************************************************</th></tr>
										<tr><th>TIPO REGISTRO</th><th>ID_PEND</th><th>LOTE</th><th>COD_RASTREAMENTO</th><th>INFORMAÇÃO LIMITE POSTAGEM</th></tr>
										<tr><td><%=historico[j][1]%></td><td><%=historico[j][2]%></td><td><%=historico[j][3]%></td><td><%=historico[j][4]%></td><td></td></tr>
									</table>
									<%idPendencia = historico[j][2];
									codRastreamento = historico[j][4];
								} else if(historico[j][0] != null && historico[j][0].contains("nconsistencia")) {%>
									<table style='width:100%;text-align:center;font-size:78%;' border='1' cellpadding='2' cellspacing='1'>
										<tr><th colspan='4'>**********************************************************<%=historico[j][0]%>**********************************************************</th></tr>
										<tr><th>TIPO REGISTRO</th><th>ID_PEND INCONSISTENTE</th><th>ID TIPO INCONSISTÊNCIA OBJETO</th><th>MENSAGEM TIPO INCONSISTÊNCIA OBJETO</th></tr>
										<tr><td><%=historico[j][1]%></td><td><%=historico[j][2]%></td><td><%=historico[j][3]%></td><td><%=historico[j][4]%></td></tr>
									</table>	
								<%} else if(historico[j][0] != null && historico[j][0].contains("ostado")) {%>					
									<table style='width:100%;text-align:center;font-size:78%;' border='1' cellpadding='2' cellspacing='1'>
										<tr><th colspan='15'>***********************************************<%=historico[j][0]%>***********************************************</th></tr>
										<tr><th>LOTE</th><th>COD_RASTREAMENTO</th><th>TIPO</th><th>SERVIÇO</th><th>CATEGORIA</th><th>EVENTO</th><th>COD</th><th>DATA_EXPEDICAO</th><th>HORA_EXPEDICAO</th><th>SRO</th><th>UNIDADE</th><th>CEP</th><th>CIDADE</th><th>UF</th><th>ENTREGA</th></tr>
										<tr><td><%=historico[j][1]%></td><td><%=historico[j][2]%></td><td><%=historico[j][3]%></td><td><%=historico[j][4]%></td><td><%=historico[j][5]%></td><td><%=historico[j][6]%></td><td><%=historico[j][7]%></td><td><%=historico[j][8]%></td><td><%=historico[j][9]%></td><td><%=historico[j][10]%></td><td><%=historico[j][11]%></td><td><%=historico[j][12]%></td><td><%=historico[j][13]%></td><td><%=historico[j][14]%></td><td><%=historico[j][15]%></td></tr>
									</table>
								<%} else if(historico[j][0] != null && historico[j][0].contains("inalizador")) {%>				
									<table style='width:100%;text-align:center;font-size:78%;' border='1' cellpadding='2' cellspacing='1'>
										<tr><th colspan='15'>**********************************************<%=historico[j][0]%>**********************************************</th></tr>
										<tr><th>LOTE</th><th>COD_RASTREAMENTO</th><th>TIPO</th><th>SERVIÇO</th><th>CATEGORIA</th><th>EVENTO</th><th>COD</th><th>DATA_ENTREGA</th><th>HORA_ENTREGA</th><th>SRO</th><th>UNIDADE</th><th>CEP</th><th>CIDADE</th><th>UF</th><th>POSTAGEM</th></tr>
										<tr><td><%=historico[j][1]%></td><td><%=historico[j][2]%></td><td><%=historico[j][3]%></td><td><%=historico[j][4]%></td><td><%=historico[j][5]%></td><td><%=historico[j][6]%></td><td><%=historico[j][7]%></td><td><%=historico[j][8]%></td><td><%=historico[j][9]%></td><td><%=historico[j][10]%></td><td><%=historico[j][11]%></td><td><%=historico[j][12]%></td><td><%=historico[j][13]%></td><td><%=historico[j][14]%></td><td><%=historico[j][15]%></td></tr>
									</table>
								<%} else if(historico[j][0] != null && historico[j][0].contains("evolucao")) {%>
									<table style='width:100%;text-align:center;font-size:78%;' border='1' cellpadding='2' cellspacing='1'>
										<tr><th colspan='7'>********************************************************<%=historico[j][0]%>********************************************************</th></tr>
										<tr><th>TIPO REGISTRO</th><th>ID_PEND</th><th>LOTE</th><th>COD_RASTREAMENTO</th><th>NÚMERO AR</th><th>COD_BAIXA</th><th>NOME IMAGEM AR</th></tr>
										<tr><td><%=historico[j][1]%></td><td><%=historico[j][2]%></td><td><%=historico[j][3]%></td><td><%=historico[j][4]%></td><td><%=historico[j][5]%></td><td><%=historico[j][6]%></td><td><%=historico[j][7]%></td>
									</table>
								<%}%>
							<%}%>	
							<table style='width:100%;text-align:center;font-size:78%;' border='1' cellpadding='2' cellspacing='1'>
								<tr><th colspan='9'><%=historico[1][0]%></th></tr>
								<tr><th>ID_PEND</th><th>ID_PEND_TIPO</th><th>ID_PEND_STATUS</th><th>ID_MOVI</th><th>ID_PROC</th><th>ID_PROC_PARTE</th><th>DATA_INICIO</th><th>ID_USU_FINALIZADOR</th><th>DATA_FIM</th></tr>
								<tr><td><%=historico[1][1]%></td><td><%=historico[1][2]%></td><td><%=historico[1][3]%></td><td><%=historico[1][4]%></td><td><%=historico[1][5]%></td><td><%=historico[1][6]%></td><td><%=historico[1][7]%></td><td><%=historico[1][8]%></td><td><%=historico[1][9]%></td></tr>
							</table>
							<table style='width:100%;text-align:center;font-size:78%;' border='1' cellpadding='2' cellspacing='1'>
								<tr><th colspan='13'><%=historico[2][0]%></th></tr>
								<tr><th>ID_PEND_CORREIOS</th><th>ID_PEND</th><th>COD_MODELO</th><th>MAO_PROPRIA</th><th>ID_PROC_CUSTA_TIPO</th><th>ORDEM_SERVICO</th><th>MATRIZ</th><th>LOTE</th><th>COD_RASTREAMENTO</th><th>COD_INCONSISTENCIA</th><th>DATA_EXPEDICAO</th><th>DATA_ENTREGA</th><th>COD_BAIXA</th></tr>
								<tr><td><%=historico[2][1]%></td><td><%=historico[2][2]%></td><td><%=historico[2][3]%></td><td><%=historico[2][4]%></td><td><%=historico[2][5]%></td><td><%=historico[2][6]%></td><td><%=historico[2][7]%></td><td><%=historico[2][8]%></td><td><%=historico[2][9]%></td><td><%=historico[2][10]%></td><td><%=historico[2][11]%></td><td><%=historico[2][12]%></td><td><%=historico[2][13]%></td></tr>
							</table>
								<table style='width:100%;text-align:center;font-size:78%;' border='1' cellpadding='2' cellspacing='1'>
								<tr><th colspan='13'>MOVIMENTAÇÃO&nbsp&nbsp&nbsp&nbsp<%=historico[0][1]%>&nbsp&nbsp&nbsp&nbsp<%=historico[0][2]%></th></tr>
								<tr><td class="colunaMinima">FINAL</td><td><%=historico[3][0]%></td><td><%=historico[3][1]%></td></tr>
								<tr><td class="colunaMinima">INICIAL</td><td><%=historico[3][2]%></td><td><%=historico[3][3]%></td></tr>
								<tr><td colspan='13'><%=historico[3][4]%></td></tr>
							</table>
							<%=historico[0][0]%>
						<%}%>					
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Enviar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','6');">
					</div>
				</fieldset>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Enviar Cartas</legend>
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND</th><th>ID_PEND_TIPO</th><th>ID_PEND_STATUS</th><th>ID_MOVI</th><th>ID_PROC</th><th>ID_PROC_PARTE</th><th>DATA_INICIO</th><th>ID_USU_FINALIZADOR</th><th>DATA_FIM</th></tr>
					  <tr><td>145019220</td><td>2</td><td style="color:red">15</td><td>7390216</td><td>368455</td><td>830976</td><td>09/02/21</td><td></td><td></td></tr>
					</table> 
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND_CORREIOS</th><th>ID_PEND</th><th>COD_MODELO</th><th>MAO_PROPRIA</th><th>ID_PROC_CUSTA_TIPO</th><th>ORDEM_SERVICO</th><th>MATRIZ</th><th>LOTE</th><th>COD_RASTREAMENTO</th><th>COD_INCONSISTENCIA</th><th>DATA_EXPEDICAO</th><th>DATA_ENTREGA</th><th>COD_BAIXA</th></tr>
					  <tr><td>44</td><td>145019220</td><td>68</td><td>0</td><td>2</td><td>0</td><td style="color:red">17921</td><td style="color:red">6</td><td></td><td></td><td></td><td></td><td></td></tr>
					</table> 
				</fieldset>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">e-Carta_MM_LL_DDMMAAAAHHMMSS_Recibo.zip</legend>
					<div>						
						<label class="formEdicaoLabel" for="Id_Pendencia1">ID_PEND</label>												
						<input class="formEdicaoInput" id="Id_Pendencia1" name="Id_Pendencia1" value="<%=request.getAttribute("Id_Pendencia1")%>" type="text" size="60" maxlength="60"/>
						<br />		
						<label class="formEdicaoLabel" for="CodRastreamento1">COD_RASTREAMENTO</label>												
						<input class="formEdicaoInput" id="CodRastreamento1" name="CodRastreamento1" value="<%=request.getAttribute("CodRastreamento1")%>" type="text" size="60" maxlength="60"/>
						<br />				
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Enviar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','1');">
					</div>
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND</th><th>ID_PEND_TIPO</th><th>ID_PEND_STATUS</th><th>ID_MOVI</th><th>ID_PROC</th><th>ID_PROC_PARTE</th><th>DATA_INICIO</th><th>ID_USU_FINALIZADOR</th><th>DATA_FIM</th></tr>
					  <tr><td>145019220</td><td>2</td><td>15</td><td>7390216</td><td>368455</td><td>830976</td><td>09/02/21</td><td></td><td></td></tr>
					</table> 
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND_CORREIOS</th><th>ID_PEND</th><th>COD_MODELO</th><th>MAO_PROPRIA</th><th>ID_PROC_CUSTA_TIPO</th><th>ORDEM_SERVICO</th><th>MATRIZ</th><th>LOTE</th><th>COD_RASTREAMENTO</th><th>COD_INCONSISTENCIA</th><th>DATA_EXPEDICAO</th><th>DATA_ENTREGA</th><th>COD_BAIXA</th></tr>
					  <tr><td>44</td><td>145019220</td><td>68</td><td>0</td><td>2</td><td>0</td><td>17921</td><td>6</td><td style="color:red">BH163382065BR</td><td></td><td></td><td></td><td></td></tr>
					</table> 
				</fieldset>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">e-Carta_MM_LL_DDMMAAAAHHMMSS_Inconsistencia.zip</legend>
					<div>						
						<label class="formEdicaoLabel" for="Id_Pendencia2">ID_PEND</label>												
						<input class="formEdicaoInput" id="Id_Pendencia2" name="Id_Pendencia2" value="<%=request.getAttribute("Id_Pendencia2")%>" type="text" size="60" maxlength="60"/>
						<br />	
						<label class="formEdicaoLabel" for="CodInconsistencia2">COD_INCONSISTENCIA</label>												
						<input class="formEdicaoInput" id="CodInconsistencia2" name="CodInconsistencia2" value="<%=request.getAttribute("CodInconsistencia2")%>" type="text" size="60" maxlength="60"/>
						<br />
						<label class="formEdicaoLabel" for="MsgInconsistencia2">MSG_INCONSISTENCIA</label>												
						<input class="formEdicaoInput" id="MsgInconsistencia2" name="MsgInconsistencia2" value="<%=request.getAttribute("MsgInconsistencia2")%>" type="text" size="60" maxlength="60"/>
						<br />					
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Enviar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','2');">
					</div>
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND</th><th>ID_PEND_TIPO</th><th>ID_PEND_STATUS</th><th>ID_MOVI</th><th>ID_PROC</th><th>ID_PROC_PARTE</th><th>DATA_INICIO</th><th>ID_USU_FINALIZADOR</th><th>DATA_FIM</th></tr>
					  <tr><td>145019220</td><td>2</td><td>15</td><td>7390216</td><td>368455</td><td>830976</td><td>09/02/21</td><td></td><td></td></tr>
					</table> 
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND_CORREIOS</th><th>ID_PEND</th><th>COD_MODELO</th><th>MAO_PROPRIA</th><th>ID_PROC_CUSTA_TIPO</th><th>ORDEM_SERVICO</th><th>MATRIZ</th><th>LOTE</th><th>COD_RASTREAMENTO</th><th>COD_INCONSISTENCIA</th><th>DATA_EXPEDICAO</th><th>DATA_ENTREGA</th><th>COD_BAIXA</th></tr>
					  <tr><td>44</td><td>145019220</td><td>68</td><td>0</td><td>2</td><td>0</td><td>17921</td><td>6</td><td>BH163382065BR</td><td style="color:red">24</td><td></td><td></td><td></td></tr>
					</table> 
				</fieldset>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">e-Carta_MM_LL_Rastreamento_Antecipado_PostadoDDMMAAAAHHMMSS.zip (Movimentação Inicial)</legend>
					<div>						
						<label class="formEdicaoLabel" for="Id_Pendencia3">ID_PEND</label>												
						<input class="formEdicaoInput" id="Id_Pendencia3" name="Id_Pendencia3" value="<%=request.getAttribute("Id_Pendencia3")%>" type="text" size="60" maxlength="60"/>
						<br />
						<label class="formEdicaoLabel" for="CodRastreamento3">COD_RASTREAMENTO</label>												
						<input class="formEdicaoInput" id="CodRastreamento3" name="CodRastreamento3" value="<%=request.getAttribute("CodRastreamento3")%>" type="text" size="60" maxlength="60"/>
						<br />
						<label class="formEdicaoLabel" for="DataPostagem3">DATA_EXPEDICAO</label>
						<input class="formEdicaoInput" name="DataPostagem3" id="DataPostagem3"  value="<%=request.getAttribute("DataPostagem3")%>" type="text" size="60" maxlength="60"> 
						<br />
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Enviar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','3');">
					</div>
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND</th><th>ID_PEND_TIPO</th><th>ID_PEND_STATUS</th><th>ID_MOVI</th><th>ID_PROC</th><th>ID_PROC_PARTE</th><th>DATA_INICIO</th><th>ID_USU_FINALIZADOR</th><th>DATA_FIM</th></tr>
					  <tr><td>145019220</td><td>2</td><td style="color:red">16</td><td>7390216</td><td>368455</td><td>830976</td><td>09/02/21</td><td></td><td></td></tr>
					</table> 
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND_CORREIOS</th><th>ID_PEND</th><th>COD_MODELO</th><th>MAO_PROPRIA</th><th>ID_PROC_CUSTA_TIPO</th><th>ORDEM_SERVICO</th><th>MATRIZ</th><th>LOTE</th><th>COD_RASTREAMENTO</th><th>COD_INCONSISTENCIA</th><th>DATA_EXPEDICAO</th><th>DATA_ENTREGA</th><th>COD_BAIXA</th></tr>
					  <tr><td>44</td><td>145019220</td><td>68</td><td>0</td><td>2</td><td>0</td><td>17921</td><td>6</td><td>BH163382065BR</td><td>24</td><td style="color:red">05/08/2020</td><td></td><td></td></tr>
					</table> 
				</fieldset>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">e-Carta_MM_LL_Rastreamento_Antecipado_FinalizadorDDMMAAAAHHMMSS.zip</legend>
					<div>						
						<label class="formEdicaoLabel" for="CodRastreamento4">COD_RASTREAMENTO</label>												
						<input class="formEdicaoInput" id="CodRastreamento4" name="CodRastreamento4" value="<%=request.getAttribute("CodRastreamento4")%>" type="text" size="60" maxlength="60"/>
						<br />
						<label class="formEdicaoLabel" for="CodBaixa4">COD_BAIXA</label>												
						<input class="formEdicaoInput" id="CodBaixa4" name="CodBaixa4" value="<%=request.getAttribute("CodBaixa4")%>" type="text" size="60" maxlength="60"/>
						<br />
						<label class="formEdicaoLabel" for="DataEntrega4">DATA_ENTREGA</label>
						<input class="formEdicaoInput" name="DataEntrega4" id="DataEntrega4"  value="<%=request.getAttribute("DataEntrega4")%>" type="text" size="60" maxlength="60"> 
						<br />
						<label class="formEdicaoLabel" for="HoraEntrega4">HORA_ENTREGA</label>
						<input class="formEdicaoInput" name="HoraEntrega4" id="HoraEntrega4"  value="<%=request.getAttribute("HoraEntrega4")%>" type="text" size="60" maxlength="60"> 
						<br />
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Enviar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','4');">
					</div>
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND</th><th>ID_PEND_TIPO</th><th>ID_PEND_STATUS</th><th>ID_MOVI</th><th>ID_PROC</th><th>ID_PROC_PARTE</th><th>DATA_INICIO</th><th>ID_USU_FINALIZADOR</th><th>DATA_FIM</th></tr>
					  <tr><td>145019220</td><td>2</td><td style="color:red">2</td><td>7390216</td><td>368455</td><td>830976</td><td>09/02/21</td><td></td><td></td></tr>
					</table> 
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND_CORREIOS</th><th>ID_PEND</th><th>COD_MODELO</th><th>MAO_PROPRIA</th><th>ID_PROC_CUSTA_TIPO</th><th>ORDEM_SERVICO</th><th>MATRIZ</th><th>LOTE</th><th>COD_RASTREAMENTO</th><th>COD_INCONSISTENCIA</th><th>DATA_EXPEDICAO</th><th>DATA_ENTREGA</th><th>COD_BAIXA</th></tr>
					  <tr><td>44</td><td>145019220</td><td>68</td><td>0</td><td>2</td><td>0</td><td>17921</td><td>6</td><td>BH163382065BR</td><td>24</td><td>05/08/2020</td><td style="color:red">07/08/2020</td><td style="color:red">1</td></tr>
					</table>
				</fieldset>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">e-Carta_MM_LL_DevolucaoAR_DDMMAAAAHHMMSS.zip (Movimentação Final)</legend>
					<div>						
						<label class="formEdicaoLabel" for="Id_Pendencia5">ID_PEND</label>												
						<input class="formEdicaoInput" id="Id_Pendencia5" name="Id_Pendencia5" value="<%=request.getAttribute("Id_Pendencia5")%>" type="text" size="60" maxlength="60"/>
						<br />
						<label class="formEdicaoLabel" for="CodRastreamento5">COD_RASTREAMENTO</label>												
						<input class="formEdicaoInput" id="CodRastreamento5" name="CodRastreamento5" value="<%=request.getAttribute("CodRastreamento5")%>" type="text" size="60" maxlength="60"/>
						<br />
						<label class="formEdicaoLabel" for="NomeArquivo5">NOME_ARQ</label>												
						<input class="formEdicaoInput" id="NomeArquivo5" name="NomeArquivo5" value="<%=request.getAttribute("NomeArquivo5")%>" type="text" size="60" maxlength="60"/>
						<br />
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Enviar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','5');">
					</div>
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND</th><th>ID_PEND_TIPO</th><th>ID_PEND_STATUS</th><th>ID_MOVI</th><th>ID_PROC</th><th>ID_PROC_PARTE</th><th>DATA_INICIO</th><th>ID_USU_FINALIZADOR</th><th>DATA_FIM</th></tr>
					  <tr><td>145019220</td><td>2</td><td>2</td><td>7390216</td><td>368455</td><td>830976</td><td>09/02/21</td><td style="color:red">1</td><td style="color:red">09/02/21</td></tr>
					</table> 
					<table style="width:100%;text-align:center;font-size:78%;" border="1" cellpadding="2" cellspacing="1">
					  <tr><th>ID_PEND_CORREIOS</th><th>ID_PEND</th><th>COD_MODELO</th><th>MAO_PROPRIA</th><th>ID_PROC_CUSTA_TIPO</th><th>ORDEM_SERVICO</th><th>MATRIZ</th><th>LOTE</th><th>COD_RASTREAMENTO</th><th>COD_INCONSISTENCIA</th><th>DATA_EXPEDICAO</th><th>DATA_ENTREGA</th><th>COD_BAIXA</th></tr>
					  <tr><td>44</td><td>145019220</td><td>68</td><td>0</td><td>2</td><td>0</td><td>17921</td><td>6</td><td>BH163382065BR</td><td>24</td><td>05/08/2020</td><td>07/08/2020</td><td>1</td></tr>
					</table>
				</fieldset>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Arquivo</legend>
					<div>						
						<label class="formEdicaoLabel" for="filename">ARQUIVO</label>
						<input type="file" name="arquivo" id="filename" size="50" /> <br>
						<br />
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Inserir arquivo .zip no BD" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','8');">
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Recibo>>Inconsistência>>Postado>>Finalizado>>AR" title="Recebe arquivo .txt. Um id_pend ou cod_rastreamento em cada linha" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','10');">
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Descartar Pendências" title="Recebe arquivo .txt. Um id_pend em cada linha" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','12');">
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Substituir Arquivo Corrompido(AR)" title="Recebe arquivo .txt. Código de Rastreamento e IdMovimentacaoArquivo em cada linha" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','7');">
					</div>
				</fieldset>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Reexecutar Arquivo</legend>
					<div>						
						<label class="formEdicaoLabel" for="NomeArquivo9">NOME_ARQ</label>
						<input class="formEdicaoInput" name="NomeArquivo9" id="NomeArquivo9" value="<%=request.getAttribute("NomeArquivo9")%>" type="text" size="60" maxlength="60"> 
						<br />
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Enviar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','9');">
					</div>
				</fieldset>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Preencher Meta Dados</legend>
					<div>						
						<label class="formEdicaoLabel" for="NumeroRegistros11">NÚMERO DE REGISTROS</label>
						<input class="formEdicaoInput" name="NumeroRegistros11" id="NumeroRegistros11" value="<%=request.getAttribute("NumeroRegistros11")%>" type="text" size="60" maxlength="60"> 
						<br />
					</div>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Enviar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','11');">
					</div>
				</fieldset>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Corrigir Data Inserção Arquivos ZIP</legend>
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Enviar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('tempFluxo1','13');">
					</div>
				</fieldset>
			  </div>
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%} else if (request.getAttribute("MensagemErro").toString().trim().equals("") == false){ %>
				<div class="divMensagemErro" id="MensagemErro"><%=request.getAttribute("MensagemErro").toString().trim()%></div>
			<%}%>
			</form>
		</div>   
	</body>
</html>