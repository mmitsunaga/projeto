<%@page import="br.gov.go.tj.projudi.dt.RegimeExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.FormaCumprimentoExecucaoDt"%>
<%@page import="java.util.*"%>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
<script type="text/javascript">
	function getSituacaoAtual(){
		camposMarcados = new Array();
		$("input[type=checkbox][name='chkRegime[]']:checked").each(function(){
		    camposMarcados.push($(this).attr('id'));
		});
		document.getElementById("RegimeExecucao").value = camposMarcados;
		
		camposMarcados = new Array();
		$("input[type=checkbox][name='chkLocal[]']:checked").each(function(){
		    camposMarcados.push($(this).attr('id'));
		});
		document.getElementById("LocalCumprimentoPena").value = camposMarcados;

		camposMarcados = new Array();
		$("input[type=checkbox][name='chkModalidade[]']:checked").each(function(){
		    camposMarcados.push($(this).attr('id'));
		});
		document.getElementById("Modalidade").value = camposMarcados;

		camposMarcados = new Array();
		$("input[type=checkbox][name='chkStatus[]']:checked").each(function(){
		    camposMarcados.push($(this).attr('id'));
		});
		document.getElementById("EventoExecucaoStatus").value = camposMarcados;

		camposMarcados = new Array();
		$("input[type=checkbox][name='chkFormaCumprimento[]']:checked").each(function(){
		    camposMarcados.push($(this).attr('id'));
		});
		document.getElementById("FormaCumprimento").value = camposMarcados;
	}
</script>

	<input id="LocalCumprimentoPena" name="LocalCumprimentoPena" type="hidden" />
	<input id="RegimeExecucao" name="RegimeExecucao" type="hidden" />
	<input id="Modalidade" name="Modalidade" type="hidden" />
	<input id="EventoExecucaoStatus" name="EventoExecucaoStatus" type="hidden" />
	<input id="FormaCumprimento" name="FormaCumprimento" type="hidden" />
	<table>
		<tr style="font: bold">
			<th>Local</th>
			<th>Regime</th>
			<th>Forma de Cumprimento</th>
			<th>Modalidade</th>
			<th>Situação</th>
		</tr>
		<tr style="vertical-align: top">
			<td>
				<%List listaLocal = (List) request.getSession().getAttribute("ListaLocal");
				if (listaLocal != null){%> 
					<%for (int i=0; i<listaLocal.size(); i++) {%>					
					<input id="<%=((LocalCumprimentoPenaDt)listaLocal.get(i)).getLocalCumprimentoPena()%>" name="chkLocal[]" type="checkbox" value="<%=((LocalCumprimentoPenaDt)listaLocal.get(i)).getId()%>"/><%=((LocalCumprimentoPenaDt)listaLocal.get(i)).getLocalCumprimentoPena()%><br />				
					<%} %>
				<%}%>
			</td>
			<td>
				<%List listaRegime_PPL = (List) request.getSession().getAttribute("SA_ListaRegime_PPL");
				if (listaRegime_PPL != null){%> 
					<%for (int i=0; i<listaRegime_PPL.size(); i++) {%>
					<input id="<%=((RegimeExecucaoDt)listaRegime_PPL.get(i)).getRegimeExecucao()%>" name="chkRegime[]" type="checkbox" value="<%=((RegimeExecucaoDt)listaRegime_PPL.get(i)).getId()%>"/><%=((RegimeExecucaoDt)listaRegime_PPL.get(i)).getRegimeExecucao()%><br />				
					<%} %>
				<%}%>
					<br />
					<input name="ExcluirPRD" type="checkbox" value="S"/>Desconsiderar as Penas Restritivas de Direito<br />
			</td>
			<td>
				<%List listaFormaCumprimento = (List) request.getSession().getAttribute("ListaFormaCumprimento");
				if (listaFormaCumprimento != null){%> 
					<%for (int i=0; i<listaFormaCumprimento.size(); i++) {%>					
					<input id="<%=((FormaCumprimentoExecucaoDt)listaFormaCumprimento.get(i)).getFormaCumprimentoExecucao()%>" name="chkFormaCumprimento[]" type="checkbox" value="<%=((FormaCumprimentoExecucaoDt)listaFormaCumprimento.get(i)).getId()%>"/><%=((FormaCumprimentoExecucaoDt)listaFormaCumprimento.get(i)).getFormaCumprimentoExecucao()%><br />
					<%} %>
				<%}%>
			</td>
			<td>
				<%List listaMod = (List) request.getSession().getAttribute("ListaModalidade");
				if (listaMod != null){%> 
					<%for (int i=0; i<listaMod.size(); i++) {%>
					<input id="<%=((RegimeExecucaoDt)listaMod.get(i)).getRegimeExecucao()%>" name="chkModalidade[]" type="checkbox" value="<%=((RegimeExecucaoDt)listaMod.get(i)).getId()%>"/><%=((RegimeExecucaoDt)listaMod.get(i)).getRegimeExecucao()%><br />					
					<%} %>
				<%}%>
			</td>
			<td>
				<%List listaStatus = (List) request.getSession().getAttribute("ListaStatus");
				if (listaStatus != null){%> 
					<%for (int i=0; i<listaStatus.size(); i++) {%>					
					<input id="<%=((EventoExecucaoStatusDt)listaStatus.get(i)).getEventoExecucaoStatus()%>" name="chkStatus[]" type="checkbox" value="<%=((EventoExecucaoStatusDt)listaStatus.get(i)).getId()%>"/><%=((EventoExecucaoStatusDt)listaStatus.get(i)).getEventoExecucaoStatus()%><br />
					<%} %>
				<%}%>
			</td>
		</tr>
	</table>
