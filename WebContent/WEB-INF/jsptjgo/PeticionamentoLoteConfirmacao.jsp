<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoDt"%>



<jsp:useBean id="dt" class= "br.gov.go.tj.projudi.dt.PeticionamentoDt" scope="request"/>




<html>
<head>
	<title>Movimentar Processo</title>	
      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
      	  
      <script type='text/javascript' src='./js/Funcoes.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
	
		<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
			<h2 align="center"> <%=request.getAttribute("MensagemOk")%> </h2>
		
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend>Confirmação <%=request.getAttribute("TituloPagina")%></legend>
					
					<%
						List processos = dt.getListaProcessos();
						if (processos != null && processos.size() > 0){
					%>
						<label class="formEdicaoLabel"> Processo(s) </label><br>
						<%
							for (int i=0;i<processos.size();i++){
								ProcessoDt objProcesso = (ProcessoDt)processos.get(i);
						%>
						<span class="span"><%=objProcesso.getProcessoNumero()%></span>
					<% 
							}
						}
					%>
					<br />
						
					<label class="formEdicaoLabel">Tipo Movimentação </label><br>  
					<span class="span"><%=dt.getMovimentacaoTipo()%></span/><br /><br />
							
					<fieldset id="ConfirmacaoCadastro" class="ConfirmacaoCadastro">
						<legend>Arquivo(s) Enviado(s)</legend>
						<br />
						<%
							List arquivos = dt.getListaArquivos();
							for (int j=0;j < arquivos.size(); j++){
								ArquivoDt arquivoDt = (ArquivoDt)arquivos.get(j);	
							
						%>
						<div> Tipo Arquivo </div>  <span> <%=arquivoDt.getArquivoTipo()%></span>
						<div> Descrição </div>  <span> <%=arquivoDt.getNomeArquivoFormatado()%></span>
						<br />
						<%
							}
						%>
					</fieldset/>
					<br />
					<br />
				</fieldset>
			</div>
		</fieldset>
		<div align="center">
			<a href="Usuario?PaginaAtual=PaginaPrincipal" title="Voltar à Página Inicial" class="link">
				<img src="./imagens/imgHome.png" />
				Voltar à Página Inicial
			</a>
		</div>		
		<br />
		<br />
 	</div>	
</body>
</html>