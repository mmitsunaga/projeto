<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<jsp:useBean id="Pendenciadt" scope="session" class="br.gov.go.tj.projudi.dt.PendenciaDt" />
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoDt"%>
<html>
<head>
	<title>Pend&ecirc;ncia</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	
	
	<script type='text/javascript' src='./dwr/interface/Pendencia.js'></script>
		
	<style type="text/css">
		@import url('./css/Principal.css');
		
		div.pendencia {
			padding: 5px;
		}
		
		div.pendencia label.lbl {
			font-weight: bold;
			float: left;
			width: 120px;
			text-align: right;
			padding-right: 10px;
			margin-top:0;
		}
	</style>
	<style type="text/css"> #bkg_projudi{ display:none } </style>
</head>
<body>
<%@ include file="/CabecalhoPublico.html" %> 
  <div id="divCorpo" class="divCorpo" >
    
  	<div id="divTitulo" class="divTitulo">Detalhes da Publicação (<%=Pendenciadt.getId()%>)</div>
	<form action="Pendencia" method="post" name="Formulario" id="Formulario">
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input type="hidden" name="fluxo" id="fluxo" value="3" />
		<input type="hidden" name="Id_PendenciaPai" value="<%=Pendenciadt.getId()%>" />
		<input type="hidden" name="Id_Pendencia" value="<%=Pendenciadt.getId()%>" />


		<br /><br />
		
		<div class="pendencia">
			<div class="dados">
				<div>
					<div style="width: 50%; float: left;">
						<label class="lbl">Tipo:</label>
						<%=Pendenciadt.getPendenciaTipo()%>
						<br />
					<div style="width: 50%; float: left;">
						<label class="lbl">Cadastrador:</label>
						<%=Pendenciadt.getNomeUsuarioCadastrador()%>
						<br />
						<label class="lbl">Data de Publicação:</label>
						<%=Pendenciadt.getDataInicio()%>
						<br />
					</div>
				</div>				
				<br />	<br />
							
				<%
					List lista = Pendenciadt.getListaArquivos();
				
					if (lista != null && lista.size() > 0){%>
						<fieldset class="fieldEdicaoEscuro">
							<legend>Arquivos Publicação</legend>
							
							<table id="tabelaAndamentos" class="Tabela">
								<thead>
									<tr>
										<th class="colunaMinima">Num.</th>
										<th>Tipo</th>
										<th>Nome</th>
										<th>Usuário Assinador</th>
										<th >Data de Inserção</th>
										<th class="colunaMinima">Abrir</th>
									</tr>
								</thead>
				
								<tfoot>
									<tr>
										<td colspan="9">Quantidade de andamentos: <%=lista.size()%></td>
									</tr>
								</tfoot>
								
								<tbody id="TabelaArquivos">
									<%
									Iterator it = lista.iterator();
									int qtd = 0;
									while (it.hasNext()){
										ArquivoDt obj = (ArquivoDt)it.next();
										%>
										<tr>
											<td><%=obj.getId()%></td>
											<td><%=obj.getArquivoTipo() %></td>
											<td><%=obj.getNomeArquivoFormatado()%></td>
											<td><%=obj.getUsuarioAssinadorFormatado()%></td>
											<td width="128"><%=obj.getDataInsercao()%></td>
											<td class="colunaMinima">
												<img src="imagens/22x22/ico_editar.png" alt="Abrir Arquivo Publicação" title="Abrir Arquivo Publicação"
													onclick="window.open('PendenciaPublica?PaginaAtual=<%=Configuracao.Editar%>&amp;Id_Arquivo=<%=obj.getId()%>&amp;PassoBusca=2','_blank','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes,fullscreen=yes')">
											</td>
										</tr>
									<%
									}
									%>
								</tbody>
							</table>
						</fieldset>
				<%}%>
			</div>
		</div>
				
		<%@ include file="Padroes/Mensagens.jspf"%>
  </form>
 </div>
</body>
</html>