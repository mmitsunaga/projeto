<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.TJDataHora"%><html>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.LogDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AtributoLogDt"%>

<jsp:useBean id="processoDt" class= "br.gov.go.tj.projudi.dt.ProcessoDt" scope="session"/>

<head>
	<title>Log de Acesso ao Processo</title>	
      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
    <link href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" type="text/css" rel="stylesheet" media="screen" />
      	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type='text/javascript' src='./js/jquery.js'></script>
      <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	  <script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	  <script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	  <script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	  <script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>	
	
	<script type="text/javascript">
		function redimensionar(){
			var objIframe = window.parent.document.getElementById('Principal');
			if (objIframe != null) {				
				var divTextoLog = document.getElementById('divTextoLog');				
				objIframe.height = divTextoLog.clientHeight + 80;
			}			
		}		
	</script>
	
	<script type="text/javascript">
		$(document).ready(function(){			
			$("#nomeBusca").focus();
		});
		
		//Preenche o campo filtro e clica no botao
		function filtrar(numero){
			$("#nomeBusca").val(numero);			
			$("#divBotoesCentralizados").trigger("click");
		}
	</script>
	
<script language="javascript" type="text/javascript">
    function VerificarCampos() {
    	if (document.Formulario.PaginaAtual.value == <%=Configuracao.Curinga7%>){
       		with(document.Formulario) {
       			if (SeNulo(data_Inicial, "O campo Data Inicial é obrigatório!")) return false;
    		    if (SeNulo(data_Final, "O campo Data Final é obrigatório!")) return false;
              	submit();
       		}
       	}
	}
</script>
 
</head>	
	<body onload="redimensionar();">
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Log de  Acesso ao Processo</h2></div>
		<form action="ProcessoLog" method="post" name="Formulario" id="Formulario">		
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		
<!-- 			<div id="divEditar" class="divEditar"> -->
<!-- 				<fieldset class="formEdicao"> -->
<!-- 					<legend>Alterações no Processo</legend> -->
					
			<div id="divLocalizar" class="divLocalizar">
				<fieldset id="formLocalizar" class="formLocalizar"  > 
					<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Solicitação de Acesso ao Processo e baixa de arquivo</legend>
					
					<label class="formEdicaoLabel">Processo N&uacute;mero</label><br>
					<span class="spanDestaque"><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span>
					<br /><br />		
					
					<label class="formLocalizarLabel"><font color="#FF0000"><b>Escolha datas dentro do mesmo ano.</b></font></label><br>
					<div class='col15'>
						<label><img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" alt="Calendário" onclick="displayCalendar(document.forms[0].data_Inicial,'dd/mm/yyyy',this)" />*Data de In&iacute;cio</label>										
						<input class="formLocalizarInput" name="data_Inicial" id="data_Inicial" type="text" value="<%=request.getAttribute("data_Inicial")%>" maxlength="60" title="Data inicial" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)" />				
					</div>
					<div class='col15'>
					<label><img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" alt="Calendário" onclick="displayCalendar(document.forms[0].data_Final,'dd/mm/yyyy',this)" />*Data de Final</label>
						<input class="formLocalizarInput" name="data_Final" id="data_Final" type="text" value="<%=request.getAttribute("data_Final")%>" maxlength="60" title="Data final" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)" />					
					</div>
					<br>									
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>')">							
					</div>
					
					<fieldset class="formEdicao">	
						<legend>Acessos ao Processo</legend>
					
						<fieldset class="formEdicao">	
							<legend>Pedidos de Liberação de Acesso</legend>			
							<div id="divTextoLog">
								<%
								List listaLiberacaoAcesso = (List) request.getAttribute("listaLiberacaoAcesso");
								
								if(listaLiberacaoAcesso != null){
									
									if(listaLiberacaoAcesso.size() < 1){
									%>
										<table id="Tabela" class="Tabela"><tbody><tr class="TabelaLinha1"><td>
										Não foram encontrados pedidos de liberação de acesso.
										</td></tr></tbody></table>
									<%
									}
									else {
										String strLinha;
										String[] arrayStr;								
										%>
										<table id="Tabela" class="Tabela">
											<thead>
												<tr class="TituloColuna1">
													<th width="4%" align="center">n.</th>
													<th width="18%" align="center">Data Início</th>
													<th width="18%" align="center">Data Fim</th>
													<th width="20%" align="center">Nome</th>
													<th width="20%" align="center">CPF</th>
													<th width="20%" align="center">OAB</th>
												</tr>
											</thead>
											
											<tbody>														
												<%															
												for(int i = 0; i < listaLiberacaoAcesso.size(); i++){
													strLinha = listaLiberacaoAcesso.get(i).toString();
													arrayStr = strLinha.split(";");
												%>	
												<tr class="TabelaLinha2">
													<td width="4%" align="center"><%=(i+1)%></td>
													<td width="18%" align="center"><%= arrayStr[0] %></td>
													<td width="18%" align="center"><%= arrayStr[1].equalsIgnoreCase("null") ? "" : arrayStr[1] %></td>
													<td width="20%" align="center"><%= arrayStr[2] %></td>
													<td width="20%" align="center"><%= arrayStr[3] %></td>
													<td width="20%" align="center"><%= arrayStr[4].equalsIgnoreCase("null") ? "" : arrayStr[4] %></td>
												</tr>
												<%
												}
												%>															
											</tbody>														
										</table>
									<%
									}
								}
								%>												
							</div>							
						</fieldset>
					
						<fieldset class="formEdicao">	
							<legend>Acesso a arquivos do processo</legend>			
							<div id="divTextoLog">
								<%
								List listaAcessoArquivo = (List) request.getAttribute("listaAcessoArquivo");
								
								if(listaAcessoArquivo != null){
									if(listaAcessoArquivo.size() < 1){
									%>
										<table id="Tabela" class="Tabela"><tbody><tr class="TabelaLinha1"><td>
										Não foram encontrados registros de acesso a arquivos do processo.
										</td></tr></tbody></table>
									<%
									}
									else {
										String strLinha;
										String[] arrayStr;								
										%>
										<table id="Tabela" class="Tabela">
											<thead>
												<tr class="TituloColuna1">
													<th width="4%" align="center">n.</th>
													<th width="14%" align="center">Usuário</th>
													<th width="10%" align="center">CPF</th>
													<th width="10%" align="center">Arquivo</th>
													<th width="34%" align="center">Movimentação</th>
													<th width="14%" align="center">Data Acesso</th>
													<th width="14%" align="center">IP Acesso</th>
												</tr>
											</thead>
											<tbody>														
												<%															
												for(int i = 0; i < listaAcessoArquivo.size(); i++){
													strLinha = listaAcessoArquivo.get(i).toString();
													arrayStr = strLinha.split(";");
												%>	
												<tr class="TabelaLinha2">
													<td width="4%" align="center"><%=(i+1)%></td>
													<td width="14%" align="center"><%= arrayStr[0] %></td>
													<td width="10%" align="center"><%= arrayStr[1] %></td>
													<td width="10%" align="center"><%= arrayStr[2] %></td>
													<td width="34%" align="center"><%= arrayStr[3] %></td>
													<td width="14%" align="center"><%= arrayStr[4] %></td>
													<td width="14%" align="center"><%= arrayStr[5] %></td>
												</tr>
												<%
												}
												%>															
											</tbody>														
										</table>
									<%
									}
								}
								%>												
							</div>							
						</fieldset>
						
					</fieldset>
					
				</fieldset>
			</div>
		</form>
 	</div> 	
 	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>