<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="java.util.*" %>
<%@page import="br.gov.go.tj.projudi.ne.MovimentacaoArquivoNe"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
<head>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
<style type="text/css">
.Erro {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-weight: bold;
	color: #FF0000;
	border:solid #993300 dotted;
}
.OK{
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-weight: bold;
	color: #000000;
}
	
.style1 {
	color: #990000;
	font-weight: bold;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}
.style2 {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 13px;
}
}
</style>

<script type='text/javascript' src='./js/Funcoes.js?v=20082020'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	
	

<script language="javascript" type="text/javascript" >

	$(document).ready(function(){
		$("input.chk0").click(function(){  				
				$("#a"+$(this).val() + " > li > input.chk1").prop("checked",$(this).prop("checked"));
				$("#a"+$(this).val() + " > li > ul > li > input.chk2").prop("checked",$(this).prop("checked"));
			});
					
		$("input.chk1").click(function(){  				
				$("#a"+$(this).val() + " > li > input.chk2").prop("checked",$(this).prop("checked"));
			});
	
		$("input.chk2").click(function(){ 	
				$("#Mensagem").html("0 kb Selecionados, valor máximo 10.240 KB ");
			});
	});

</script>

</head>

<body>
  <div  id="divCorpo" class="divCorpo">
 	<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
 	<div class="area"><h2>&raquo; Devolvendo Precatória</h2></div>
 			<input id="fluxo" name="fluxo" type="hidden" value="<%=request.getAttribute("fluxo")%>" />
 			<input id="id_ProcessoDeprecado" name="id_ProcessoDeprecado" type="hidden" value="<%=request.getAttribute("id_ProcessoDeprecado")%>" />
	 		<div align="center"> 
			  <p class="style1">Selecione o Status e os Arquivos que Deverão Retornar do Processo de Precatória ao Processo Principal</p>
			</div>
			
			<label class="formEdicaoLabel">Processo:</label><br>
				<a href="BuscaProcesso?Id_Processo=<%=processoDt.getId()%>"> 
					<%=processoDt.getProcessoNumeroCompleto()%>
				</a>
			<br />
			
			<label class="formEdicaoLabel" for="status">*Status  :</label><br> 
			<select name="status" id="status" class="formEdicaoCombo" >
				<option selected></option>
					<option>Cumprida</option>
					<option>Não Cumprida</option>
					<option>Parcialmente Cumprida</option>
			</select>
			<br />
			
			<div id="divEditar" class="divEditar">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<div id='ListaCheckBox' onclick="habilitaTelaENTERComponente('ListaCheckBox', 'operacao');">
				<%List listaMovimentacoes = processoDt.getListaMovimentacoes();
				  if (listaMovimentacoes != null){
					Iterator it = listaMovimentacoes.iterator(); %>
		        	<ul id="a0"> <input type="checkbox" name="chk0" class="chk0" value='0' /><strong>Todos os Arquivos</strong>
			        <%int contAux = 1; %>
			        <%	MovimentacaoArquivoNe movimentacaoArquivo = new MovimentacaoArquivoNe(); %>
			        <%	while(it.hasNext()) {
			        		MovimentacaoDt movimentacao = (MovimentacaoDt) it.next();
			        		if (!movimentacao.getCodigoTemp().equals("1")){
				        		List listMovimentacaoArquivo =   movimentacaoArquivo.consultarArquivosMovimentacao(movimentacao.getId() );
				        		if (listMovimentacaoArquivo != null) { 
				        			Iterator it2 =  listMovimentacaoArquivo.iterator();%>
				        			<li><input class="chk1" name="chk1" type="checkbox" value='<%=contAux%>'/><strong><%=movimentacao.getMovimentacaoTipo()%></strong><ul id="a<%=contAux%>">
						        	 <% while(it2.hasNext()) {
						        		   MovimentacaoArquivoDt arq = (MovimentacaoArquivoDt) it2.next();
						        			if (!arq.getCodigoTemp().equals("2")){
						        	 %>
							        			<li><input class='chk2' name='chk2' type='checkbox' value="<%=arq.getArquivoDt().getId()%>"/>
							        				<strong><%=arq.getArquivoDt().getNomeArquivoFormatado()%></strong>
							        				<input type="hidden" value='<%= Funcoes.StringToInt(arq.getArquivoDt().getArquivo()) /1024%>' id="valor<%=arq.getArquivoDt().getId()%>" /> 
							        			</li>
						        			<%} %>
						        	 <%} %>
						        		</ul></li>
						       		 <% contAux += 1; %>
						       	<%} %>
					       	<%} %>
			        <%	}%>
			        	</ul>
			        <%} %>
				</div>
			</div>
			<br />
			<div align="center">
				<button type="submit" name="operacao" value="GerarPDF" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>'); AlterarValue('fluxo','3')" >
						Devolver a Origem
		   		</button>
	   		</div>
  	 </form>
	</div>
	<%@include file="Padroes/Mensagens.jspf"%>
</body>
</html>

