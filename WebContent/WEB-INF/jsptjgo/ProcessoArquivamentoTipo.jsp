<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ProcessoArquivamentoTipoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ProcessoArquivamentoTipodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoArquivamentoTipoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Tipo de Arquivamento de Processo  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='js/jquery.js'> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(ProcessoArquivamentoTipo, "O campo Tipo de Arquivamento é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div id="divTitulo" class="divTitulo"> Cadastro de Tipo de Arquivamento de Processo</div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ProcessoArquivamentoTipo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ProcessoArquivamentoTipo" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="consultarTodos" name="consultarTodos" type="hidden" value="1" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getParameter("tempFluxo1")%>" />
			
			
			<div id="divEditar" class="divEditar">
				<div id="divPortaBotoes" class="divPortaBotoes">
					<%@ include file="Padroes/Botoes.jspf"%>					
					<a class="divPortaBotoesLink" href="Ajuda/ProcessoArquivamentoTipoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
					<%if(ProcessoArquivamentoTipodt.getId() != null && !ProcessoArquivamentoTipodt.getId().equals("")){%>			    	
				    	<%if(ProcessoArquivamentoTipodt.isAtivo()) {%>			      		
				      		<input id="imgBloquearProcessoArquivamentoTipo" alt="Bloquear Processo Arquivamento Tipo" title="Bloquear Processo Arquivamento Tipo" name="imgBloquearProcessoArquivamentoTipo" type="image"  src="./imagens/22x22/ico_fechar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('tempFluxo1','<%=ProcessoArquivamentoTipoDt.FLUXO_PROCESSO_ARQUIVAMENTO_TIPO_BLOQUEIO%>');">
				    	<%} else { %>			    		
				    		<input id="imgDesbloquearProcessoArquivamentoTipo" alt="Desbloquear Processo Arquivamento Tipo" title="Desbloquear Processo Arquivamento Tipo" name="imgDesbloquearProcessoArquivamentoTipo" type="image"  src="./imagens/22x22/ico_liberar.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('tempFluxo1','<%=ProcessoArquivamentoTipoDt.FLUXO_PROCESSO_ARQUIVAMENTO_TIPO_DESBLOQUEIO%>');">
				    	<%}%> 
				  	<%} %>
				</div>
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados do Tipo de Arquivamento de Processo &nbsp;&nbsp;
						<%if(ProcessoArquivamentoTipodt.getId() != null && !ProcessoArquivamentoTipodt.getId().equals("")){%>			    	
				    	<%if(!ProcessoArquivamentoTipodt.isAtivo()) {%>			      		
				      		<label for="Aviso" style="color:red;">< BLOQUEADO ></label><br> 
				    	<%}%> 
				  	<%} %>
				  	</legend>
					<label class="formEdicaoLabel" for="Id_PrococessoArquivamentoTipo">Identificador</label> <input class="formEdicaoInputSomenteLeitura" name="Id_PrococessoArquivamentoTipo"  id="Id_PrococessoArquivamentoTipo"  type="text"  readonly="true" value="<%=ProcessoArquivamentoTipodt.getId()%>"><br />
					<label class="formEdicaoLabel" for="ProcessoArquivamentoTipo">*Tipo de Arquivamento</label> <input class="formEdicaoInput" name="ProcessoArquivamentoTipo" id="ProcessoArquivamentoTipo"  type="text" size="60" maxlength="60" value="<%=ProcessoArquivamentoTipodt.getProcessoArquivamentoTipo()%>" onkeyup=" autoTab(this,60)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
