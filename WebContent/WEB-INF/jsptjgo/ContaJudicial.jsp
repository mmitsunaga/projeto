<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ContaJudicialDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ContaJudicialdt" scope="session" class= "br.gov.go.tj.projudi.dt.ContaJudicialDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.BancoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de ContaJudicial  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(ProcNumero, "O Campo ProcNumero é obrigatório!")) return false;
				if (SeNulo(Banco, "O Campo Banco é obrigatório!")) return false;
				if (SeNulo(ContaJudicialNumero, "O Campo ContaJudicialNumero é obrigatório!")) return false;
				if (SeNulo(Serv, "O Campo Serv é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div id="divTitulo" class="divTitulo"> Cadastro de ContaJudicial</div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ContaJudicial" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ContaJudicial" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/ContaJudicialAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados ContaJudicial </legend>
					<label class="formEdicaoLabel" for="Id_ContaJudicial">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_ContaJudicial"  id="Id_ContaJudicial"  type="text"  readonly="true" value="<%=ContaJudicialdt.getId()%>"><br />
					<label class="formEdicaoLabel" for="ContaJudicialNumero">*ContaJudicialNumero</label><br> <input class="formEdicaoInput" name="ContaJudicialNumero" id="ContaJudicialNumero"  type="text" size="60" maxlength="60" value="<%=ContaJudicialdt.getContaJudicialNumero()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="Id_ProcParte">Processoparte
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoparte" name="imaLocalizarProcessoparte" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					<input class="FormEdicaoimgLocalizar" id="imaLimparProcessoparte" name="imaLimparProcessoparte" type="image"  src="./imagens/imgLimparPequena.png"  onclick="LimparChaveEstrangeira('Id_ProcParte','Nome'); return false;" > 			
					</label><br>  		<input  name='Id_ProcParte' id='Id_ProcParte' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Nome" id="Nome" type="text" size="60" maxlength="150" value="<%=ContaJudicialdt.getNome()%>"><br />
					<label class="formEdicaoLabel" for="ProcNumero">*ProcNumero</label><br> <input class="formEdicaoInput" name="ProcNumero" id="ProcNumero"  type="text" size="20" maxlength="20" value="<%=ContaJudicialdt.getProcNumero()%>" onkeyup=" autoTab(this,20)"><br />
					<label class="formEdicaoLabel" for="Id_Banco">Banco
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarBanco" name="imaLocalizarBanco" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BancoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 				
					</label> <br> 	<input  name='Id_Banco' id='Id_Banco' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Banco" id="Banco" type="text" size="60" maxlength="60" value="<%=ContaJudicialdt.getBanco()%>"><br />
					<label class="formEdicaoLabel" for="Id_Comarca">Comarca
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					</label><br>   					<input  name='Id_Comarca' id='Id_Comarca' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Comarca" id="Comarca" type="text" size="60" maxlength="100" value="<%=ContaJudicialdt.getComarca()%>"><br />
					<label class="formEdicaoLabel" for="Id_Serv">Serventia
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					</label><br>   					<input  name='Id_Serv' id='Id_Serv' type='hidden'  value=''>
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Serv" id="Serv" type="text" size="60" maxlength="60" value="<%=ContaJudicialdt.getServ()%>"><br />
					<label class="formEdicaoLabel" for="PessoaTipoDepositante">PessoaTipoDepositante</label><br> <input class="formEdicaoInput" name="PessoaTipoDepositante" id="PessoaTipoDepositante"  type="text" size="22" maxlength="22" value="<%=ContaJudicialdt.getPessoaTipoDepositante()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
					<label class="formEdicaoLabel" for="CpfCnpjDepositante">CpfCnpjDepositante</label> <input class="formEdicaoInput" name="CpfCnpjDepositante" id="CpfCnpjDepositante"  type="text" size="22" maxlength="22" value="<%=ContaJudicialdt.getCpfCnpjDepositante()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
					<label class="formEdicaoLabel" for="NomeDepositante">NomeDepositante</label> <br><input class="formEdicaoInput" name="NomeDepositante" id="NomeDepositante"  type="text" size="60" maxlength="150" value="<%=ContaJudicialdt.getNomeDepositante()%>" onkeyup=" autoTab(this,150)"><br />
					<label class="formEdicaoLabel" for="PessoaTipoReclamado">PessoaTipoReclamado</label><br> <input class="formEdicaoInput" name="PessoaTipoReclamado" id="PessoaTipoReclamado"  type="text" size="22" maxlength="22" value="<%=ContaJudicialdt.getPessoaTipoReclamado()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
					<label class="formEdicaoLabel" for="CpfCnpjReclamado">CpfCnpjReclamado</label> <br><input class="formEdicaoInput" name="CpfCnpjReclamado" id="CpfCnpjReclamado"  type="text" size="14" maxlength="14" value="<%=ContaJudicialdt.getCpfCnpjReclamado()%>" onkeyup=" autoTab(this,14)"><br />
					<label class="formEdicaoLabel" for="NomeReclamado">NomeReclamado</label><br> <input class="formEdicaoInput" name="NomeReclamado" id="NomeReclamado"  type="text" size="60" maxlength="150" value="<%=ContaJudicialdt.getNomeReclamado()%>" onkeyup=" autoTab(this,150)"><br />
					<label class="formEdicaoLabel" for="PessoalTipoReclamante">PessoalTipoReclamante</label><br> <input class="formEdicaoInput" name="PessoalTipoReclamante" id="PessoalTipoReclamante"  type="text" size="22" maxlength="22" value="<%=ContaJudicialdt.getPessoalTipoReclamante()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
					<label class="formEdicaoLabel" for="CpfCnpjReclamante">CpfCnpjReclamante</label><br> <input class="formEdicaoInput" name="CpfCnpjReclamante" id="CpfCnpjReclamante"  type="text" size="22" maxlength="22" value="<%=ContaJudicialdt.getCpfCnpjReclamante()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
					<label class="formEdicaoLabel" for="NomeReclamante">NomeReclamante</label><br> <input class="formEdicaoInput" name="NomeReclamante" id="NomeReclamante"  type="text" size="60" maxlength="150" value="<%=ContaJudicialdt.getNomeReclamante()%>" onkeyup=" autoTab(this,150)"><br />
					<label class="formEdicaoLabel" for="PessoaTipoAdvReclamado">PessoaTipoAdvReclamado</label> <br><input class="formEdicaoInput" name="PessoaTipoAdvReclamado" id="PessoaTipoAdvReclamado"  type="text" size="2" maxlength="2" value="<%=ContaJudicialdt.getPessoaTipoAdvReclamado()%>" onkeyup=" autoTab(this,2)"><br />
					<label class="formEdicaoLabel" for="CpfCnpjAdvReclamado">CpfCnpjAdvReclamado</label><br> <input class="formEdicaoInput" name="CpfCnpjAdvReclamado" id="CpfCnpjAdvReclamado"  type="text" size="22" maxlength="22" value="<%=ContaJudicialdt.getCpfCnpjAdvReclamado()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
					<label class="formEdicaoLabel" for="NomeAdvReclamado">NomeAdvReclamado</label> <br><input class="formEdicaoInput" name="NomeAdvReclamado" id="NomeAdvReclamado"  type="text" size="60" maxlength="150" value="<%=ContaJudicialdt.getNomeAdvReclamado()%>" onkeyup=" autoTab(this,150)"><br />
					<label class="formEdicaoLabel" for="PessoaTipoAdvReclamante">PessoaTipoAdvReclamante</label><br> <input class="formEdicaoInput" name="PessoaTipoAdvReclamante" id="PessoaTipoAdvReclamante"  type="text" size="22" maxlength="22" value="<%=ContaJudicialdt.getPessoaTipoAdvReclamante()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
					<label class="formEdicaoLabel" for="CpfCnpjAdvReclamante">CpfCnpjAdvReclamante</label><br> <input class="formEdicaoInput" name="CpfCnpjAdvReclamante" id="CpfCnpjAdvReclamante"  type="text" size="22" maxlength="22" value="<%=ContaJudicialdt.getCpfCnpjAdvReclamante()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />
					<label class="formEdicaoLabel" for="NomeAdvReclamante">NomeAdvReclamante</label><br> <input class="formEdicaoInput" name="NomeAdvReclamante" id="NomeAdvReclamante"  type="text" size="60" maxlength="150" value="<%=ContaJudicialdt.getNomeAdvReclamante()%>" onkeyup=" autoTab(this,150)"><br />
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
