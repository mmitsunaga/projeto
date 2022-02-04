<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>

<jsp:useBean id="AudienciaSegundoGraudt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaSegundoGrauDt" />

<html>
	<head>
		<title><%=request.getAttribute("TituloPagina")%></title>
    	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
		
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/MascararHoraResumida.js"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarHoraResumida.js"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type="text/javascript">
			_tempBuscaId = 'Id_Audiencia';
			_tempBuscaDescricao = 'Audiencia';
			_PaginaEditar = '<%=Configuracao.Editar%>';
			_PaginaExcluir = '<%=Configuracao.Excluir%>';
		</script>
		
		<%@ include file="./js/Paginacao.js"%>
	</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; SESSÕES PENDENTES</h2></div>
			
			<form name="Formulario" id="Formulario" action="<%=request.getAttribute("tempRetorno")%>" method="post" >
				
				<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>">
				<input type="hidden"  id="PaginaAnterior" name="PaginaAnterior" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input type="hidden" id="fluxo" name="fluxo" value="<%=request.getAttribute("fluxo")%>">
				<input type="hidden" id="PassoEditar" name="PassoEditar" value="<%=request.getAttribute("PassoEditar")%>">
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				
				<input type="hidden" id="Id_Audiencia" name="Id_Audiencia" value="<%=request.getAttribute("Id_Audiencia")%>" />
				<input type="hidden" id="Audiencia" name="Audiencia" value="" />					

				<div class="divEditar" id="divEditar">
					<% if (request.getAttribute("podeCadastrar") != null && request.getAttribute("podeCadastrar").toString().equals("true")){ %>
					<fieldset class="formEdicao">
						<legend>Criar Nova Sessão</legend>
						<div class="col20">
						<label>Data </label><br>
						<input id="Data" name="Data" size="10" maxlength="10" title="Clique para escolher uma data."  value="<%=AudienciaSegundoGraudt.getData()%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)" />
						<img id="calData" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].Data,'dd/mm/yyyy',this)" />
						</div>
						<div class="col20">
						<label class="">Hora</label><br>
						<input name="Hora" index="0" maxlength="5" size="5" onkeypress="return DigitarHoraResumida(this, event)" onkeyup="MascararHoraResumida(this); autoTab(this,5)" value="<%=AudienciaSegundoGraudt.getHora()%>" />
						</div>
						<div class=" space clear"></div>
						<div class="col15 clear space"><input type="submit" name="btnCriar" id="btnCriar" value="Criar" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');" />
							</div>
					</fieldset>

					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
					<%} %>
					<br />
					<fieldset class="formEdicao">
						<legend>Sessões Pendentes</legend>
					   	<div id="divTabela" class="divTabela">
					   	
					   		<table id="Tabela" class="Tabela">
					        	<thead>
					            	<tr class="TituloColuna">
					                 	<th>Data/Hora</th>
					                 	<th>Serventia</th>
					                  	<th class="colunaMinima">Detalhar</th>
					                  	<th class="colunaMinima">Excluir</th>
					                  	<th class="colunaMinima">Finalizar</th>
					                  	<th class="colunaMinima"></th>
					               	</tr>
					           	</thead>
					           	<tbody id="tabListaAudiencia"> 
									<%
 										List listaAudiencias = (List)request.getAttribute("ListaAudiencias");
 												 				AudienciaDt audienciaDt;
 												  				boolean linha = false;
 												  				
 												  				boolean ehCamaraSegundoGrau = (request.getSession().getAttribute("UsuarioSessaoDt") != null) && ((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).isSegundoGrau();													  

 												  				boolean ehGabineteSegundoGrau = (request.getSession().getAttribute("UsuarioSessaoDt") != null) && ((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).isGabineteSegundoGrau();
 															
 										
 												  				if (listaAudiencias != null){
 												  					for(int i = 0 ; i < listaAudiencias.size();i++) {
 												  	  					audienciaDt = (AudienciaDt)listaAudiencias.get(i);
 									%>
					  	  					<tr class="TabelaLinha<%=(linha?1:2)%>" align="center"> 
					  	                		<td onclick="selecionaSubmete('<%=audienciaDt.getId()%>','<%=audienciaDt.getDataAgendada()%>')"><%=audienciaDt.getDataAgendada()%></td>
					  	                		<td onclick="selecionaSubmete('<%=audienciaDt.getId()%>','<%=audienciaDt.getDataAgendada()%>')"><%=audienciaDt.getServentia()%></td>
				  	                   			<td>
				  	                   				<input name="formLocalizarimgEditar" type="image" src="./imagens/imgEditar.png" onclick="AlterarValue('Id_Audiencia','<%=audienciaDt.getId()%>');AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');" title="Detalhar Processos da Sessão" />
				  	                   			</td>
				  	                   			<td>
				  	                   				<input name="formLocalizarimgExcluir" type="image" src="./imagens/imgExcluir.png" onclick="AlterarValue('Id_Audiencia','<%=audienciaDt.getId()%>');AlterarValue('Audiencia','<%=audienciaDt.getDataAgendada()%>');AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');" title="Excluir Sessão" />
				  	                   			</td>
				  	                   			<td>
				  	                   				<input name="formLocalizarimgFinalizar" type="image" src="./imagens/22x22/ico_solucionar.png" onclick="AlterarAction('Formulario','AudienciaMovimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('Id_Audiencia','<%=audienciaDt.getId()%>');" title="Finalizar Sessão de Julgamento"/>
				  	                   			</td>
<!--				  	                   			<%if(ehCamaraSegundoGrau || ehGabineteSegundoGrau){%>-->
<!--				  	                   			<td>-->
<!--				  	                   				<input name="formLocalizarimgResponsaveis" type="image" src="./imagens/imgAssistente.png" onclick="AlterarAction('Formulario','AudienciaResponsavel');AlterarValue('Id_Audiencia','<%=audienciaDt.getId()%>');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" title="Visualizar Responsáveis pela Sessão"/>-->
<!--				  	                   			</td>-->
<!--				  	                   			<%}%>-->
						  	               	</tr>
					  				<%		linha = !linha;
					  					} //Fim FOR
									}%> 
					           	</tbody>
					       	</table>
					   	</div>
				   	</fieldset>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>