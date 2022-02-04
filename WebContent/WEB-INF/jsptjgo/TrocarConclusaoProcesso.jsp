<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>

<jsp:useBean id="PendenciaResponsaveldt" class= "br.gov.go.tj.projudi.dt.PendenciaResponsavelDt" scope="session"/>
<jsp:useBean id="Pendenciadt" class= "br.gov.go.tj.projudi.dt.PendenciaDt" scope="session"/>
<jsp:useBean id="processoDt" class= "br.gov.go.tj.projudi.dt.ProcessoDt" scope="session"/>

<html>
<head>
	<title>Trocar responsável por conclusão de processo de 1º grau</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
    
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>	
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo;Trocar responsável por conclusão de processo de 1º grau</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="Quantidade" name="Quantidade" type="hidden" value="<%=request.getAttribute("Quantidade")%>" />
				<input id="NovoResponsavel" name="NovoResponsavel" type="hidden" value="<%=request.getAttribute("NovoResponsavel")%>" />
				<input id="idConclusao" name="idConclusao" type="hidden" value="<%=request.getAttribute("idConclusao")%>" />
				<input id="nomeMagistradoAtual" name="nomeMagistradoAtual" type="hidden" value="<%=request.getAttribute("nomeMagistradoAtual")%>" />
						
				<div id="divEditar" class="divEditar">
					
				      	<div> Número Processo </div>
						<span><a href="BuscaProcesso?PaginaAtual=-1&Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a></span/>
						<br />
						
						<label class="formEdicaoLabel" style="color:red;">Responsável atual pela conclusão: <%=request.getAttribute("nomeMagistradoAtual")%></label>
						<br>
				    
						<fieldset class="formEdicao">
							<legend>Magistrado que receberá a conclusão</legend>
							<table class="Tabela" id="TabelaArquivos">
								<thead>
									<tr>
										<th></th>
										<th width="20%">Descrição do Cargo</th>
										<th width="30%">Serventia</th>
										<th width="50%" colspan="2">Responsável</th>
										<th></th>
									</tr>
								</thead>							
								<tbody>
									<%
										List liMagistradosReceberaConclusoes = Pendenciadt.getMagistradoResponsaveisAtuais();
									    ServentiaCargoDt objTempNovoResponsavel;
										String stTempNomeNovoResponsavel="";
										String stTempNome="";
										for(int f = 0 ; f< liMagistradosReceberaConclusoes.size();f++) {
											objTempNovoResponsavel = (ServentiaCargoDt)liMagistradosReceberaConclusoes.get(f); %>
											<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
							                   	<tr class="TabelaLinha1"> 
											<%}else{ stTempNome=""; %>    
							                   	<tr class="TabelaLinha2">
											<%}%>
							                   	<td> <%=f+1%></td>
							                <%if (objTempNovoResponsavel.getServentiaCargo() != null && objTempNovoResponsavel.getServentiaCargo().length()>0) {%>
												<td align="center"><%=objTempNovoResponsavel.getServentiaCargo()%></td>
												<td align="center"><%=objTempNovoResponsavel.getServentia()%></td>
											<% } else {%>
													<td align="center">---</td>
													<td align="center">---</td>
											<%} %>
						                    	
						                    <%if (objTempNovoResponsavel.getNomeUsuario() != null && objTempNovoResponsavel.getNomeUsuario().length()>0) {%>
						                        <td><%=objTempNovoResponsavel.getCargoTipo() %></td>
												<td><%=objTempNovoResponsavel.getNomeUsuario()%></td>
											<% } else {%>
													<td align="center">---</td>
													<td align="center">---</td>
											<%} %>
											<%  boolean checked2 = false;
						                   		if ( request.getAttribute("NovoResponsavel") != null 
						                   				&& request.getAttribute("NovoResponsavel").toString().equals(objTempNovoResponsavel.getId())){ 
						                   			checked2 = true;
						                   		}
						                   	%> 
						                   		<td align="center">
													<input type="radio" id="id_UsuarioServentiaCargoNovoResponsavel" <%=checked2?"checked":"" %> name="id_UsuarioServentiaCargoNovoResponsavel" value="<%=objTempNovoResponsavel.getId()%>" />
												</td>
						                   		</tr>
											<%}%>
								</tbody>
							</table>
						</fieldset>
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						</div>
					
				</div>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>