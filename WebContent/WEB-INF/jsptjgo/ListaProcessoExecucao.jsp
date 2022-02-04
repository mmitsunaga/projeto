<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt" />
<jsp:useBean id="Enderecodt" scope="session" class="br.gov.go.tj.projudi.dt.EnderecoDt" />

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	<title>|<%=request.getAttribute("tempPrograma")%>| Lista processos existentes</title>
	<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
	<link href="./css/Paginacao.css" type="text/css" rel="stylesheet" />
	
   	
   	
   	<script type='text/javascript' src='./js/Funcoes.js'></script>
  	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
   	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
   	<script type='text/javascript' src='./js/tabelas.js'></script>
</head>

<body>
<div id="divCorpo" class="divCorpo">
	<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Lista de Processos Existentes</h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="posicaoLista" name="posicaoLista" type="hidden" value="<%=request.getAttribute("posicaoLista")%>">

			<div id="divEditar" class="divEditar">

				<fieldset id="VisualizaDados" class="VisualizaDados"><legend>Processos encontrados</legend>
				<br />
				<label style="color: red"><b> Selecione o Processo para vincular a nova Guia de Recolhimento que está sendo cadastrada. </b></label><br><br /><br />
			
			<%
				List liTemp = (List) request.getSession().getAttribute("ListaProcesso");
				ProcessoDt objTemp;
				boolean boLinha = false;
				if (liTemp!=null){
				
			%>
				<table id="Tabela" class="Tabela">
					<thead>
					<tr>
						<th>Nº Processo Execução</th>
						<th>Nome do Sentenciado</th>
						<th>Nome da Mãe</th>
						<th>Data de Nascimento</th>
						<th>Serventia</th>
						<th>Status</th>
						<th>Tipo</th>
						<th align="center">Sel.</th>
					</tr>
					</thead>
					<tbody id="tabListaProcessoParte">
				<%
					for (int i = 0; i < liTemp.size(); i++) {
						objTemp = (ProcessoDt) liTemp.get(i);
				%>
					<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>">
						<td title="Processo de Execução" align="center" onclick="AlterarValue('PassoEditar','23'); AlterarValue('posicaoLista','<%=i%>');"><%=Funcoes.formataNumeroProcesso(objTemp.getProcessoNumeroCompleto())%></td>
						<td	onclick="AlterarValue('PassoEditar','23'); AlterarValue('posicaoLista','<%=i%>');"><%=objTemp.getPrimeiroPoloPassivoNome()%></td>
						<td	onclick="AlterarValue('PassoEditar','23'); AlterarValue('posicaoLista','<%=i%>');"><%=objTemp.getPrimeiroPoloPassivoNomeMae()%></td>
						<td align="center" onclick="AlterarValue('PassoEditar','23'); AlterarValue('posicaoLista','<%=i%>');"><%=objTemp.getPrimeiroPoloPassivoDataNascimento()%></td>
						<td	onclick="AlterarValue('PassoEditar','23'); AlterarValue('posicaoLista','<%=i%>');"><%=objTemp.getServentia()%></td>
						<td align="center" onclick="AlterarValue('PassoEditar','23'); AlterarValue('posicaoLista','<%=i%>');"><%=objTemp.getProcessoStatus()%></td>
						<td align="center" onclick="AlterarValue('PassoEditar','23'); AlterarValue('posicaoLista','<%=i%>');"><%=objTemp.getProcessoTipo()%></td>
						<td><input name="formLocalizarimgEditar" type="image" title="Editar evento" src="./imagens/imgEditar_14x14.png" 
								onclick="AlterarValue('PassoEditar','23'); AlterarValue('posicaoLista','<%=i%>'); " /></td>
                   		<td>
					</tr>
				<% boLinha = !boLinha;
				}
				}%>
					</tbody>
				</table>
			</fieldset>			
		</div>
	</form>
</div>
	</div>
	
	<%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>
