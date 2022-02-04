/**
 *  Javascript para manipulacao de tabelas de arquivos dos eventos dos processos de execução do projudi
 * @author Ronneesley Moura Teles
 * @since 23/09/2008 10:15
 * Dependencias: jquery.js, conflitos.js e tabela.js
 */
var TabelaCondenacoesEventoExecucao = function(id, pai, pa){
	this.id = id;
	this.pai = pai;
};
 
TabelaCondenacoesEventoExecucao.prototype = {
 	/**
 	 * Id da tabela de arquivos
 	 */
 	id: null,
 	
 	/**
 	 * Objeto pai 
 	 */
 	pai: null,
	

	/**
	 * Adicionar uma lista de arquivos na tabela
	 * @author Ronneesley Moura Teles
	 * @since 24/09/2008 10:29
	 * @param Array() lista, lista de objetos para serem dicionados
	 */
	adicionar: function(lista){
		var display = $('.imgValidar').css("display");
	 	//Percorre todos elementos da lista
	 	for (var i = 0; i < lista.length; i++){
	 	 	var obj = lista[i]; //Objeto do laco
	 	 	var linha = [];
	 	 	linha.push(obj.crimeExecucao);
	 	 	linha.push(obj.tempoPenaEmAnos);
	 	 	linha.push(obj.dataFato);
	 	 	if (obj.reincidente) linha.push("Sim");
	 	 	else linha.push("Não");
	 	 	linha.push(obj.condenacaoExecucaoSituacao);
	 	 	this.tabela.addLinha( linha );
	 	 	$('.imgValidar').css('display',display);
		}	
	},
	
	/**
	 * Criar a tabela de arquivos
	 * @author Ronneesley Moura Teles
	 * @since 24/09/2008 09:11
	 * @param Array() lista, lista de objetos caso queira adicionar no final
	 */
	criar: function(lista){
	 	var fieldset = document.createElement("fieldset");
	 	//var legend = document.createElement("legend");	 	
	 	//$j(legend).html("Arquivos");	 	
	 	//$j(fieldset).append(legend);
		$(fieldset).attr("id", this.id);
	 	$("#" + this.pai).append(fieldset);

		var cabecalho = [];
		
		cabecalho.push("Crime");
		cabecalho.push("Pena (a-m-d)");
		cabecalho.push("Data do Fato");
		cabecalho.push("Reincidência");
		cabecalho.push("Extinto");

		this.tabela = new Tabela(this.id + "_tabela", cabecalho, this.id, "Arquivos");	 
		this.tabela.criar();
		
		//Verifica se passou a lista, caso tenha, adiciona os itens
		if (lista.length>0)
			this.adicionar(lista);
	}	

}