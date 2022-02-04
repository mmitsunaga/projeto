<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoJudicialDt"%>
<jsp:useBean id="mandadoJudicial" scope="session"
	class="br.gov.go.tj.projudi.dt.MandadoJudicialDt" />

<html>
<head>
<title>Valida Ordem Pagamento de Mandados com Custas</title>

<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">

<style type="text/css">
@import url('./css/Principal.css');

@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
</style>

<script type='text/javascript' src='./js/Funcoes.js'></script>
<script type='text/javascript'
	src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	
	<style>
    .tdStatPagCdg<%=MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO%>{
    	 color: blue;
    }
    .tdStatPagCdg<%=MandadoJudicialDt.ID_PAGAMENTO_ENVIADO%>{
    	color: green;
    }
    .tdStatPagCdg<%=MandadoJudicialDt.ID_PAGAMENTO_NEGADO%>{
    	color: red;
    }
    .tdStatPagCdg<%=MandadoJudicialDt.ID_PAGAMENTO_PENDENTE%>{
    	color: black;
    }
   </style>

</head>

<body>

	<div id="divCorpo" class="divCorpo">
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post"
			name="Formulario" id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden"
				value="<%=request.getAttribute("PaginaAtual")%>" /> <input
				id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden"
				value="<%=request.getAttribute("PosicaoPaginaAtual")%>" /> <input
				id="PaginaAnterior" name="PaginaAnterior" type="hidden"
				value="<%=request.getAttribute("PaginaAnterior")%>" /> 
				
		
			<input id="Fluxo" name="Fluxo" type="hidden"
				value="<%=request.getAttribute("Fluxo")%>" /> <input id="idMandJud"
				name="idMandJud" type="hidden"
				value="<%=request.getAttribute("idmandJud")%>" />			
							
			<div id="divEditar" class="divEditar">
				<h2>&raquo; Autoriza Pagamento de Mandados com Custas</h2>	
				
							
			
			<input type="image"  src="./imagens/imgAtualizar.png"   title="Lista Mandados Concluídos na Data Informada" class="imgEditar" 
						onclick="AlterarValue('PaginaAtual','6'); 
					             AlterarValue('Fluxo','buscaOrdemPg')"				             
						value="Autorizar" name="botaoAutoriza">
			</div>
			

			<div id="divEditar" class="divEditar">

				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">  					 
						
						<%=request.getAttribute("nomeServentia")%>			  
						
					</legend>

				  <label class="formEdicaoLabel" for="dataReferencia">*Esta pesquisa lista os mandados de 60 dias antes, até data informada.
						</label><br> <input class="formEdicaoInput"
						name="dataReferencia" id="dataReferencia" type="text" size="10"
						maxlength="10" value="<%=request.getAttribute("dataReferencia")==null?"":request.getAttribute("dataReferencia")%>" onkeyup="mascara_data(this)"
						onblur="verifica_data(this)"> <img
						id="calendarioDataReferencia" src="./imagens/dlcalendar_2.gif"
						height="13" width="13" title="Calendário" alt="Calendário"
						onclick="displayCalendar(document.forms[0].dataReferencia,'dd/mm/yyyy',this)" />
					<br />
					<br />	
					
					<label class="formEdicaoLabel" for="tipoArquivo">*Status Pagamento</label><br> 							
					
					<select id="idMandJudPagamentoStatus" name ="idMandJudPagamentoStatus" class="formEdicaoCombo" >
					<%
					List listTemp = (List) request.getAttribute("listaStatusPagamento");
					if (listTemp != null) {
							%>
						<!-- 	<option value="" ></option>  -->
							<%
							MandadoJudicialDt mandJudTmp;
							for (int i = 0; i < listTemp.size(); i++) {
								mandJudTmp = (MandadoJudicialDt) listTemp.get(i);
							%>					
							<option value="<%=mandJudTmp.getIdMandJudPagamentoStatus()%>"
								
							<%
							if(mandJudTmp.getIdMandJudPagamentoStatus() != null &&  mandJudTmp.getIdMandJudPagamentoStatus().equals(request.getAttribute("idMandJudPagamentoStatus"))){ 
							%>
					 		 selected="selected"
							<% 
							} 
							%>								 
					        >
							<%=mandJudTmp.getMandJudPagamentoStatus()%> 
							</option>
							<%
							}
					}
					%>		
					</select><br />							
			</div>

			</fieldset>
	</div>

	<div id="divLocalizar" class="divCorpo">

		<div id="divTabela" class="divTabela">

			<table id="Tabela" class="Tabela">
				<thead>
					<tr class="TituloColuna">
						<!--      		<td width="8%"/> -->
						<td></td>
						<td>Número Mandado</td>
						<td>Data Conclusão</td>
						<td>Tipo</td>
						<td>Processo</td>
						<td>Oficial</td>
						<td>Companheiro</td>
						<td>Locomoções</td>
						<td>Valor</td>
						<td>Status</td>
						<td>Editar</td>
					</tr>
				</thead>
				<tbody id="listaMandado">
					<%
						List liTemp = (List) request.getAttribute("listaMandado");
						int contaMandados = 0;
						boolean boLinha = false;
						//Percorre Lista Geral de Mandados
						if (liTemp != null) {
							for (int i = 0; i < liTemp.size(); i++) {
								mandadoJudicial = (MandadoJudicialDt) liTemp.get(i);
								contaMandados++;
								if (mandadoJudicial.getNomeUsuarioServentia_2() == null)
									mandadoJudicial.setNomeUsuarioServentia_2("");
					%>

					<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>">

						<td><%=i + 1%></td>
						<td><%=mandadoJudicial.getId()%></td>
						<td><%=mandadoJudicial.getDataRetorno()%></td>
						<td><%=mandadoJudicial.getMandadoTipo()%></td>
						<td><%=mandadoJudicial.getProcNumero()%></td>
						<td><%=mandadoJudicial.getNomeUsuarioServentia_1()%></td>
						<td><%=mandadoJudicial.getNomeUsuarioServentia_2()%></td>
						<td><%=mandadoJudicial.getQuantidadeLocomocao()%></td>
						<td><%=mandadoJudicial.getValorLocomocao()%></td>
						<td><div class='tdStatPagCdg<%=mandadoJudicial.getIdMandJudPagamentoStatus()%>'><%=mandadoJudicial.getMandJudPagamentoStatus()%></div></td>


						<td><input id="imgEditar" class="imgEditar"
							title="Edita Ordem de Pagamento" name="imgEditar" type="image"
							src="./imagens/imgEditar.png"
							onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>') ; AlterarValue('Fluxo','editaOrdemPg');							
						             AlterarValue('idMandJud','<%=mandadoJudicial.getId()%>') ">

						</td>

					</tr>

					<%
						}
						}
					%>
				</tbody>

			</table>
		</div>
		
		<%@ include file="Padroes/MensagemErro.jsp"%>
		<%@ include file="Padroes/MensagemOk.jsp"%>
		
	</div>	
	</form>

	</div>
</body>
</html>