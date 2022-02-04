<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoPrisaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCriminalDt"%>

<jsp:useBean id="MandadoPrisaodt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoPrisaoDt"/>
<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de MandadoPrisao  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>

	<script language="javascript" type="text/javascript">
		function confirma(mensagem, PaginaAtual, PosicaoLista, OrigemMandado) {
			if (confirm(mensagem)) {
				if (PaginaAtual != ''){
					AlterarValue('PaginaAtual', PaginaAtual);
					AlterarValue('posicaoLista', PosicaoLista);
					submit();
				}
			} else
				return;
		}
	</script>

</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Mandado de Prisao</h2></div>
		<form action="MandadoPrisao" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="posicaoLista" name="posicaoLista" type="hidden" value="<%=request.getAttribute("posicaoLista")%>"/>
			<div id="divEditar" class="divEditar">

<br />
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend><%=request.getAttribute("tempLegenda")%></legend>
				<%
					List liTemp = (List) request.getSession().getAttribute("ListaMandadoPrisao");
					MandadoPrisaoDt objTemp;
					boolean boLinha = false;
					if (liTemp != null && liTemp.size()>0){
				%>
					<table id="Tabela" class="Tabela" >
						<thead>
						<tr>
							<th align="center">Processo</th>
							<th align="center">Nº Mandado</th>
							<th align="center">Nome da Parte</th>
							<th align="center">Sigilo</th>
							<th align="center">Prisão Tipo</th>
							<th align="center">Data de Emissão</th>
							<th align="center">Data de Expedição</th>
							<th align="center">Data de Impressão</th>
							<th align="center">Data de Validade</th>
							<th align="center">Status</th>
							<th align="center">Sel.</th>
						</tr>
						</thead>
						<tbody id="tabListaProcessoParte">
					<%
						for (int i = 0; i < liTemp.size(); i++) {
							objTemp = (MandadoPrisaoDt) liTemp.get(i);
					%>
						<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>">
							<td align="center" class="colunaMinima"><%=objTemp.getProcessoNumero()+"."+objTemp.getDigitoVerificador()%></td>
							<td align="center" class="colunaMinima"><%=objTemp.getMandadoPrisaoNumero()%></td>
							<td style="width: 30%"><%=objTemp.getProcessoParte()%></td>
							<td align="center" style="width: 7%"><%if (objTemp.isSigilo()){%>SIM<%} else {%>Não<%}%></td>
							<td style="width: 10%"><%=Funcoes.verificarCampo(objTemp.getPrisaoTipo(),"","-")%></td>
							<td align="center" style="width: 10%"><%=Funcoes.verificarCampo(objTemp.getDataEmissao(),"","-")%></td>
							<td align="center" style="width: 10%"><%=Funcoes.verificarCampo(objTemp.getDataExpedicao(),"","-")%></td>
							<td align="center" style="width: 10%"><%=Funcoes.verificarCampo(objTemp.getDataImpressao(),"","-")%></td>					
							<td align="center" style="width: 10%"><%=objTemp.getDataValidade()%></td>
							<td align="center" style="width: 15%"><%=Funcoes.verificarCampo(objTemp.getMandadoPrisaoStatus(),"","-")%></td>
							<td align="center" class="colunaMinima"><input name="formLocalizarimgEditar" type="image" title="Selecionar Mandado de Prisão" src="./imagens/imgEditarPequena.png" 
									onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>'); AlterarValue('posicaoLista','<%=i%>'); " /></td>
						</tr>
					<% boLinha = !boLinha;
						}%>
						</tbody>
					</table>
					<br />
					<span style="width: 500px">Total de <%=request.getAttribute("tempLegenda")%>: <%=liTemp.size()%></span>
					<% } else {%>
					<em> Nenhum <%=request.getAttribute("tempLegenda")%>. </em>
					<%} %>

				</fieldset>		
			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
