<script type="text/javascript"> 
	function buscaDados(posicao, tamanho) {
		if (posicao==null) {
			posicao=$("#CaixaTextoPosicionar").val()-1;
		}
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=8&dataInicialInicio='+document.getElementById('dataInicialInicio').value+'&dataFinalInicio='+document.getElementById('dataFinalInicio').value+'&Id_PendenciaTipo='+document.getElementById('Id_PendenciaTipo').value+'&Id_PendenciaStatus='+document.getElementById('Id_PendenciaStatus').value+'&prioridade='+true+'&filtroTipo='+Formulario.filtroTipo.value+'&dataInicialFim='+document.getElementById('dataInicialFim').value +'&dataFinalFim='+document.getElementById('dataFinalFim').value +'&numeroProcesso='+document.getElementById('numeroProcesso').value+ '&PosicaoPaginaAtual=' + posicao;
		
		var corpoTabela = "";
		corpoTabela +='<td class="colunaMinima lista_id">' + 'JsoNid' + '</td>';
		corpoTabela +='<td width="160" align="center">';
		corpoTabela +='<a href="BuscaProcesso?Id_Processo=' + 'JsoNdesc8' + '">' + 'JsoNdesc3' + '</a></td>';
		corpoTabela +='<td>' + 'JsoNdesc2' + '</td>';
		corpoTabela +='<td>' + 'JsoNdesc1' + '</td>';
		corpoTabela +='<td class="lista_data">' + 'JsoNdesc4' + '</td>';
		corpoTabela +='<td class="lista_data">' + 'JsoNdesc5' + '</td>';
		corpoTabela +='<td class="lista_data">' + 'JsoNdesc6' + '</td>';
		corpoTabela +='<td>' + 'JsoNdesc7' + '</td>';
		corpoTabela +='<td align="center">';
		corpoTabela +='<a href=\"Pendencia?PaginaAtual=6&amp;pendencia=' + 'JsoNid' + '&amp;CodigoPendencia='+ 'JsoNhash' +'&amp;fluxo=11\">';
		corpoTabela +='<img src=\"imagens/' + 'imgLocalizarPequena.png' + '\" alt=\"Selecionar\" title=\"' + 'Ver detalhes da pend&ecirc;ncia' + '\" \/>';
		corpoTabela +='<\/a>';
		corpoTabela +='</td>';
		
		buscaDados2JSON(posicao, tamanho, url, corpoTabela,null,null,null);
	}
</script>