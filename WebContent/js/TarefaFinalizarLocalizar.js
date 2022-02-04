function buscaDados(posicao, tamanho) {
	if (posicao==null) posicao=$("#CaixaTextoPosicionar").val()-1;
	url = 'Tarefa?AJAX=ajax&PaginaAtual=3&fluxo=1&nomeBusca='+document.getElementById('nomeBusca').value+'&Id_Projeto='+document.getElementById('Id_Projeto').value+'&PosicaoPaginaAtual=' + posicao;
	
    var corpoTabela = "";
    corpoTabela +='<td onclick="AlterarValue(\'PaginaAtual\',\'6\'); AlterarValue(\'Id_Tarefa\',\'' + 'JsoNid' + '\'); FormSubmit(\'Formulario\');">' + 'JsoNdesc1' + '</td>';
    corpoTabela +='<td class="Centralizado" onclick="AlterarValue(\'PaginaAtual\',\'6\'); AlterarValue(\'Id_Tarefa\',\'' + 'JsoNid' + '\'); FormSubmit(\'Formulario\');">' + 'JsoNdesc2' + '</td>';
    corpoTabela +='<td class="Centralizado" onclick="AlterarValue(\'PaginaAtual\',\'6\'); AlterarValue(\'Id_Tarefa\',\'' + 'JsoNid' + '\'); FormSubmit(\'Formulario\');">' + 'JsoNdesc3' + '</td>';
    corpoTabela +='<td onclick="AlterarValue(\'PaginaAtual\',\'6\'); AlterarValue(\'Id_Tarefa\',\'' + 'JsoNid' + '\'); FormSubmit(\'Formulario\');">' + document.getElementById('Projeto').value + '</td>';
    corpoTabela +='<td class="Centralizado"><input name="formLocalizarimgEditar" type="image" style="align:center;" src="./imagens/imgEditar.png" onclick="AlterarValue(\'PaginaAtual\',\'6\'); AlterarValue(\'Id_Tarefa\',\'' + 'JsoNid' + '\'); FormSubmit(\'Formulario\');"/>  </td>';
    corpoTabela +='</tr>';	
    
    buscaDadosJSON(posicao, tamanho, url, corpoTabela);
	
}