<script type="text/javascript"> 
	function buscaDados(posicao, tamanho) {
		if (posicao==null) {
			posicao=$("#CaixaTextoPosicionar").val()-1;
		}
		var prioridade = 0;
		if ( $('#prioridade').is(':checked')){
			prioridade=$('#prioridade').val();
		}
		 
			
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=3&dataInicialInicio='+document.getElementById('dataInicialInicio').value+'&dataFinalInicio='+document.getElementById('dataFinalInicio').value+'&Id_PendenciaTipo='+document.getElementById('Id_PendenciaTipo').value+'&Id_PendenciaStatus='+document.getElementById('Id_PendenciaStatus').value+'&prioridade='+prioridade+'&filtroTipo='+Formulario.filtroTipo.value+'&filtroCivelCriminal='+Formulario.filtroCivelCriminal.value + '&PosicaoPaginaAtual=' + posicao + '&numeroProcesso=' + document.getElementById('numeroProcesso').value;
	
		var corpoTabela = "";
		corpoTabela +='<td class="colunaMinima lista_id">' + 'JsoNid' + '</td>';
		corpoTabela +='<td width="160" align="center">';
		corpoTabela +='<a href="BuscaProcesso?Id_Processo=' + 'JsoNdesc5' + '">' + 'JsoNdesc1' + '</a></td>';
		corpoTabela +='<td>' + 'JsoNdesc2' + '</td>';
		corpoTabela +='<td class="lista_data">' + 'JsoNdesc3' + '</td>';
		corpoTabela +='<td>' + 'JsoNdesc4' + '</td>';
		corpoTabela +='<td name="' + 'JsoNsubstitute'  + 'JsoNdesc6' + '">';
		corpoTabela += "<input type=\"checkbox\" name=\"pendencia[]\" value=\"" + 'JsoNid' + "\" onclick=\"distribuir(this)\" ";
		corpoTabela += " title=\"Clique para escolher a pessoa que receber&aacute; a pend&ecirc;ncia\""; 
		corpoTabela += " />";
		corpoTabela +='</td>';
		corpoTabela +='<td id="pendencia' + 'JsoNid' + '" width="350"></td>';
		corpoTabela +='<td align="center">';
		corpoTabela +='<a href=\"Pendencia?PaginaAtual=-1&amp;pendencia=' + 'JsoNid' + '&amp;CodigoPendencia='+ 'JsoNhash' +'&amp;fluxo=1&amp;NovaPesquisa=true\">';
		corpoTabela +='<img src=\"imagens/' + '22x22/ico_solucionar.png' + '\" alt=\"Solucionar\" title=\"' + 'Solucionar a pend&ecirc;ncia' + '\" \/>';
		corpoTabela +='<\/a>';
		corpoTabela +='</td>';
	
		buscaDados2JSON(posicao, tamanho, url, corpoTabela,null,null,null);
	}
//	
//	function jsonCallBack(){
//		$("[name^='JsoNsubstitute8']").html("");
//	}
</script>