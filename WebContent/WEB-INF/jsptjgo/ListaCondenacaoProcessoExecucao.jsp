<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>

<jsp:useBean id="ProcessoExecucaodt_PE" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoExecucaoDt" />

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	<title>|<%=request.getAttribute("tempPrograma")%>| Lista parte</title>
	<link href="./css/Principal.css" type="text/css" rel="stylesheet" />

	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<%@ include file="./js/Paginacao.js"%>
	<script type="text/javascript">
		function confirmaAtivarDesativar(mensagem, PaginaAtual, PassoEditar, posicao){
			if (confirm(mensagem)){
				if (PaginaAtual != '') AlterarValue('PaginaAtual', PaginaAtual);
				if (PassoEditar != '') AlterarValue('PassoEditar', PassoEditar);
				AlterarValue('posicaoLista',posicao);
				return true;
			} else return false;
		}
	</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Lista das Condenações</h2></div>
		<div id="divLocalizar" class="divLocalizar">
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="posicaoLista" name="posicaoLista" type="hidden" value="<%=request.getAttribute("posicaoLista")%>">
			
			<fieldset id="VisualizaDados" class="VisualizaDados"><legend> Dados do Processo </legend>
				<div> N&uacute;mero</div>
				<span><a href="BuscaProcesso?Id_Processo=<%=ProcessoExecucaodt_PE.getProcessoDt().getId()%>"><%=ProcessoExecucaodt_PE.getProcessoDt().getProcessoNumeroCompleto()%></a></span>
				<div> Sentenciado</div>
				<span> <%=((ProcessoParteDt)ProcessoExecucaodt_PE.getProcessoDt().getListaPolosPassivos().get(0)).getNome()%></span>
			</fieldset>

			<div id="divTabela" class="divTabela">
			<table id="Tabela" class="Tabela">
				<thead>
					<tr>
						<th class="colunaMinima"></th>
						<th>Nº do Processo de Ação Penal</th>
						<th>Data do Trânsito em Julgado/ <br />Guia de Recolhimento Provisória</th>
						<th>Total da Condenação (a-m-d)</th>
						<th>Ativar/Desativar</th>
						<th align="center">Sel.</th>
						<th align="center">Exc.</th>
					</tr>
				</thead>
				<tbody id="tabListaCondenacaoProcesso">
				<%
					List liTemp = (List) request.getSession().getAttribute("listaAcoesPenais");
					ProcessoExecucaoDt objTemp;
					boolean boLinha = false;
					
					if( liTemp != null ) {
						for (int i = 0; i < liTemp.size(); i++) {
							objTemp = (ProcessoExecucaoDt) liTemp.get(i);
					%>
					<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>">
						<td><%=i + 1%></td>
						<td	align="center" onclick="AlterarValue('PaginaAtual','6'); AlterarValue('PassoEditar','2'); AlterarValue('posicaoLista','<%=i%>'); FormSubmit('Formulario');"><%=objTemp.getNumeroAcaoPenal()%></td>
						<td	align="center" onclick="AlterarValue('PaginaAtual','6'); AlterarValue('PassoEditar','2'); AlterarValue('posicaoLista','<%=i%>'); FormSubmit('Formulario');"><%=objTemp.getDataTransitoJulgado()%></td>
						<td align="center" onclick="AlterarValue('PaginaAtual','6'); AlterarValue('PassoEditar','2'); AlterarValue('posicaoLista','<%=i%>'); FormSubmit('Formulario');"><%=objTemp.getTempoTotalCondenacaoAnos()%></td>
						<td align="center">
						<% if (objTemp.isAtivo()){ %>
							<input name="formAtivar" type="image" title="Desativar Ação Penal...." src="./imagens/16x16/accept.png" 
								onclick="return confirmaAtivarDesativar('Deseja realmente desativar esta ação penal?', '<%=Configuracao.Curinga6%>', '3', '<%=i%>');" />
						<% } else { %>
							<input name="formAtivar" type="image" title="Ativar Ação Penal" src="./imagens/16x16/remove.png" 
								onclick="return confirmaAtivarDesativar('Deseja realmente ativar esta ação penal?', '<%=Configuracao.Curinga6%>', '3', '<%=i%>');" />
						<% } %>
						</td>
						<td align="center"><input name="formLocalizarimgEditar" type="image" title="Selecionar Ação Penal" src="./imagens/imgEditarPequena.png" 
									onclick="AlterarValue('PaginaAtual','6'); AlterarValue('PassoEditar','2'); AlterarValue('posicaoLista','<%=i%>'); " /></td>
						<td align="center">
							<input name="formLocalizarimgexcluir" type="image" title="Excluir Ação Penal" src="./imagens/imgExcluir_14x14.png" 
									onclick="return confirmaExclusao('Confirma exclusão da Ação Penal, irá excluir todos os eventos relacionados a ela?', '<%=Configuracao.Curinga6%>', '4', '<%=i%>');" />
						</td>
	
					</tr>
					<% 
						boLinha = !boLinha;
					}
				}%>
			</tbody>
		</table>
		<br />
		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
			<input name="imgInserir" type="submit" value="Cadastrar Ação Penal" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','24');"> 
	    </div>

		</div>
		</form>
	</div>
	
	</div>
	<%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>

