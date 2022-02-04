<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<?xml version="1.0" encoding="iso-8859-1"?>
<resposta versao='1' operacao='<%=(String)request.getAttribute("Operacao")%>'>
	<situacao>OK</situacao>
	<mensagem>		
		<processo><%ProcessoDt processo = (ProcessoDt) request.getAttribute("ProcessoCompletoDt"); ServentiaDt serventia = (ServentiaDt) request.getAttribute("ServentiaDt");%>
			<numero><%=processo.getProcessoNumeroCompleto()%></numero>
			<numeroTco><%=(processo.getTcoNumero() != null?processo.getTcoNumero():"")%></numeroTco>
			<promoventes><%
				List promoventes = processo.getListaPolosAtivos();
					if (promoventes != null && promoventes.size() > 0){
						for (int i=0; i < promoventes.size();i++){
							ProcessoParteDt parte = (ProcessoParteDt)promoventes.get(i);
			%>
				<promovente>
					<nome><%=parte.getNome()%></nome>
					<cpf><%=parte.getCpfCnpj()%></cpf>
					<rg><%=parte.getRg() + " " + parte.getRgOrgaoExpedidor()%></rg>
					<dataNascimento><%=parte.getDataNascimento()%></dataNascimento>
					<ctps><%=parte.getCtps()%></ctps>
					<ufCtps><%=parte.getEstadoCtpsUf()%></ufCtps>
					<serieCtps><%=parte.getCtpsSerie()%></serieCtps>
					<pis><%=parte.getPis()%></pis>
					<tituloEleitor><%=parte.getTituloEleitor()%></tituloEleitor>
					<nomeMae><%=parte.getNomeMae()%></nomeMae>
					<sexo><%=(parte.getSexo().equals("M")?"Masculino":"Feminino")%></sexo>
					<eMail><%=parte.getEMail()%></eMail>
					<telefone><%=parte.getTelefone()%></telefone>
					<endereco>
						<logradouro><%=parte.getEnderecoParte().getLogradouro()%></logradouro>
						<numero><%=parte.getEnderecoParte().getNumero()%></numero>
						<complemento><%=parte.getEnderecoParte().getComplemento()%></complemento>
						<bairro><%=parte.getEnderecoParte().getBairro()%></bairro>
						<codCidade><%=parte.getEnderecoParte().getCidadeCodigo()%></codCidade>
						<descricaoCidade><%=parte.getEnderecoParte().getCidade()%></descricaoCidade>
						<uf><%=parte.getEnderecoParte().getUf()%></uf>
						<cep><%=parte.getEnderecoParte().getCep()%></cep>
					</endereco>
				</promovente><% 
					}
				}%>
			</promoventes>
			<promovidos><%
				List promovidos = processo.getListaPolosPassivos();
					if (promovidos != null && promovidos.size() > 0){
						for (int i=0; i < promovidos.size();i++){
							ProcessoParteDt  parte = (ProcessoParteDt)promovidos.get(i);
			%>
				<promovido>
					<nome><%=parte.getNome()%></nome>
					<cpf><%=parte.getCpfCnpj()%></cpf>
					<rg><%=parte.getRg() + " " + parte.getRgOrgaoExpedidor()%></rg>
					<dataNascimento><%=parte.getDataNascimento()%></dataNascimento>
					<ctps><%=parte.getCtps()%></ctps>
					<ufCtps><%=parte.getEstadoCtpsUf()%></ufCtps>
					<serieCtps><%=parte.getCtpsSerie()%></serieCtps>
					<pis><%=parte.getPis()%></pis>
					<tituloEleitor><%=parte.getTituloEleitor()%></tituloEleitor>
					<nomeMae><%=parte.getNomeMae()%></nomeMae>
					<sexo><%=(parte.getSexo().equals("M")?"Masculino":"Feminino")%></sexo>
					<eMail><%=parte.getEMail()%></eMail>
					<telefone><%=parte.getTelefone()%></telefone>
					<endereco>
						<logradouro><%=parte.getEnderecoParte().getLogradouro()%></logradouro>
						<numero><%=parte.getEnderecoParte().getNumero()%></numero>
						<complemento><%=parte.getEnderecoParte().getComplemento()%></complemento>
						<bairro><%=parte.getEnderecoParte().getBairro()%></bairro>
						<codCidade><%=parte.getEnderecoParte().getCidadeCodigo()%></codCidade>
						<descricaoCidade><%=parte.getEnderecoParte().getCidade()%></descricaoCidade>
						<uf><%=parte.getEnderecoParte().getUf()%></uf>
						<cep><%=parte.getEnderecoParte().getCep()%></cep>
					</endereco>
				</promovido><% 		
					} 
				}%>
			</promovidos>
			<testemunhas><% 	
				boolean boTestemunha = false;
				boolean boComunicante = false;
			  	List listaOutrasPartes = processo.getListaOutrasPartes();
				if (listaOutrasPartes != null && listaOutrasPartes.size() > 0){
					for (int i=0; i < listaOutrasPartes.size();i++){
						ProcessoParteDt parte = (ProcessoParteDt)listaOutrasPartes.get(i);
						if (Funcoes.StringToInt(parte.getProcessoParteTipoCodigo()) == ProcessoParteTipoDt.TESTEMUNHA){
							boTestemunha = true;			  	%>
				<testemunha>
					<nome><%=parte.getNome()%></nome>
					<cpf><%=parte.getCpfCnpj()%></cpf>
					<rg><%=parte.getRg() + " " + parte.getRgOrgaoExpedidor()%></rg>
					<dataNascimento><%=parte.getDataNascimento()%></dataNascimento>
					<ctps><%=parte.getCtps()%></ctps>
					<ufCtps><%=parte.getEstadoCtpsUf()%></ufCtps>
					<serieCtps><%=parte.getCtpsSerie()%></serieCtps>
					<pis><%=parte.getPis()%></pis>
					<tituloEleitor><%=parte.getTituloEleitor()%></tituloEleitor>
					<nomeMae><%=parte.getNomeMae()%></nomeMae>
					<sexo><%=(parte.getSexo().equals("M")?"Masculino":"Feminino")%></sexo>
					<eMail><%=parte.getEMail()%></eMail>
					<telefone><%=parte.getTelefone()%></telefone>
					<endereco>
						<logradouro><%=parte.getEnderecoParte().getLogradouro()%></logradouro>
						<numero><%=parte.getEnderecoParte().getNumero()%></numero>
						<complemento><%=parte.getEnderecoParte().getComplemento()%></complemento>
						<bairro><%=parte.getEnderecoParte().getBairro()%></bairro>
						<codCidade><%=parte.getEnderecoParte().getCidadeCodigo()%></codCidade>
						<descricaoCidade><%=parte.getEnderecoParte().getCidade()%></descricaoCidade>
						<uf><%=parte.getEnderecoParte().getUf()%></uf>
						<cep><%=parte.getEnderecoParte().getCep()%></cep>
					</endereco>
				</testemunha><% 		
						}
					}
				}
				if (!boTestemunha){  %>
				<testemunha>
					<nome></nome>
					<cpf></cpf>
					<rg></rg>
					<dataNascimento></dataNascimento>
					<ctps></ctps>
					<ufCtps></ufCtps>
					<serieCtps></serieCtps>
					<pis></pis>
					<tituloEleitor></tituloEleitor>
					<nomeMae></nomeMae>
					<sexo></sexo>
					<eMail></eMail>
					<telefone></telefone>
					<endereco>
						<logradouro></logradouro>
						<numero></numero>
						<complemento></complemento>
						<bairro></bairro>
						<codCidade></codCidade>
						<descricaoCidade></descricaoCidade>
						<uf></uf>
						<cep></cep>
					</endereco>
				</testemunha><%	
				} %>
			</testemunhas>
			<comunicantes><% 	
				if (listaOutrasPartes != null && listaOutrasPartes.size() > 0){
					for (int i=0; i < listaOutrasPartes.size();i++){
						ProcessoParteDt parte = (ProcessoParteDt)listaOutrasPartes.get(i);
						if (Funcoes.StringToInt(parte.getProcessoParteTipoCodigo()) == ProcessoParteTipoDt.COMUNICANTE){
							boComunicante = true;  	%>
				<comunicante>
					<nome><%=parte.getNome()%></nome>
					<cpf><%=parte.getCpfCnpj()%></cpf>
					<rg><%=parte.getRg() + " " + parte.getRgOrgaoExpedidor()%></rg>
					<dataNascimento><%=parte.getDataNascimento()%></dataNascimento>
					<ctps><%=parte.getCtps()%></ctps>
					<ufCtps><%=parte.getEstadoCtpsUf()%></ufCtps>
					<serieCtps><%=parte.getCtpsSerie()%></serieCtps>
					<pis><%=parte.getPis()%></pis>
					<tituloEleitor><%=parte.getTituloEleitor()%></tituloEleitor>
					<nomeMae><%=parte.getNomeMae()%></nomeMae>
					<sexo><%=(parte.getSexo().equals("M")?"Masculino":"Feminino")%></sexo>
					<eMail><%=parte.getEMail()%></eMail>
					<telefone><%=parte.getTelefone()%></telefone>
					<endereco>
						<logradouro><%=parte.getEnderecoParte().getLogradouro()%></logradouro>
						<numero><%=parte.getEnderecoParte().getNumero()%></numero>
						<complemento><%=parte.getEnderecoParte().getComplemento()%></complemento>
						<bairro><%=parte.getEnderecoParte().getBairro()%></bairro>
						<codCidade><%=parte.getEnderecoParte().getCidadeCodigo()%></codCidade>
						<descricaoCidade><%=parte.getEnderecoParte().getCidade()%></descricaoCidade>
						<uf><%=parte.getEnderecoParte().getUf()%></uf>
						<cep><%=parte.getEnderecoParte().getCep()%></cep>
					</endereco>
				</comunicante><%
						}
					} 
				} if (!boComunicante) {%>
				<comunicante>
					<nome></nome>
					<cpf></cpf>
					<rg></rg>
					<dataNascimento></dataNascimento>
					<ctps></ctps>
					<ufCtps></ufCtps>
					<serieCtps></serieCtps>
					<pis></pis>
					<tituloEleitor></tituloEleitor>
					<nomeMae></nomeMae>
					<sexo></sexo>
					<eMail></eMail>
					<telefone></telefone>
					<endereco>
						<logradouro></logradouro>
						<numero></numero>
						<complemento></complemento>
						<bairro></bairro>						
						<codCidade></codCidade>
						<descricaoCidade></descricaoCidade>
						<uf></uf>
						<cep></cep>
					</endereco>
				</comunicante>
				<%}%>
			</comunicantes>
			<informacoes>
				<codArea><%=processo.getAreaCodigo()%></codArea>
				<descricaoArea><%=processo.getArea()%></descricaoArea>
				<dataRecebimento><%=processo.getDataRecebimento()%></dataRecebimento>
				<codServentia><%=processo.getServentiaCodigo()%></codServentia>
				<descricaoServentia><%=(processo.getId_Recurso() != null && processo.getId_Recurso().length() > 0 && processo.getRecursoDt() != null) ? processo.getRecursoDt().getServentiaOrigem():processo.getServentia()%></descricaoServentia>
				<codComarca><%=processo.getComarcaCodigo()%></codComarca>
				<comarca><%=processo.getComarca()%></comarca>
				<serventiaSubTipoCodigo><%=processo.getServentiaSubTipoCodigo()%></serventiaSubTipoCodigo>
				<serventiaSubTipo><%=processo.getServentiaSubTipo()%></serventiaSubTipo>
				<codTipoAcao><%=processo.getProcessoTipoCodigo()%></codTipoAcao>
				<descricaoTipoAcao><%=processo.getProcessoTipo()%></descricaoTipoAcao>
				<valorAcao><%=processo.getValor()%></valorAcao>
				<maior60anos><%=processo.isMaior60Anos() %></maior60anos>
				<segredoJustica><%=processo.mostrarSegredoJustica()%></segredoJustica>
				<assuntos>
				<%List listaAssuntos = processo.getListaAssuntos();	
				if (listaAssuntos != null && listaAssuntos.size() > 0){
					for (int i=0; i < listaAssuntos.size();i++){
						ProcessoAssuntoDt assuntoDt = (ProcessoAssuntoDt)listaAssuntos.get(i);%>
					<assunto>
						<assuntoCodigo><%=assuntoDt.getAssuntoCodigo()%></assuntoCodigo>
						<assuntoDescricao><%=assuntoDt.getAssunto()%></assuntoDescricao>
					</assunto><%}
				} else {%>
					<assunto></assunto><%
				}%>
				</assuntos>
				<valorCondenacao><%=processo.getValorCondenacao()%></valorCondenacao>
				<idProcessoOriginario><%=processo.getId_ProcessoPrincipal()%></idProcessoOriginario>
				<processoOriginario><%=processo.getProcessoNumeroPrincipal()%></processoOriginario>				
				<faseProcessualCodigo><%=processo.getProcessoFaseCodigo()%></faseProcessualCodigo>
				<faseProcessual><%=processo.getProcessoFase()%></faseProcessual>	
				<dataTransitoJulgado><%=processo.getDataTransitoJulgado()%></dataTransitoJulgado>
				<status><%=processo.getProcessoStatus()%></status>
				<prioridadeCodigo><%=processo.getProcessoPrioridadeCodigo()%></prioridadeCodigo>
				<prioridade><%=processo.getProcessoPrioridade()%></prioridade>
				<efeitoSuspensivo><%=(processo.getEfeitoSuspensivo() != null && processo.getEfeitoSuspensivo().equalsIgnoreCase("true")?"Sim":"Não")%></efeitoSuspensivo>
				<julgado2Grau><%=(processo.getServentiaTipoCodigo() != null && Funcoes.StringToInt(processo.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU && processo.getJulgado2Grau() != null && processo.getJulgado2Grau().equalsIgnoreCase("true") ? "Sim" : "Não")%></julgado2Grau>
				<custa><%=processo.getCustaTipo()%></custa>
				<penhoraRosto><%=(processo.getPenhora() != null && processo.getPenhora().equalsIgnoreCase("true") ? "Sim" : "Não")%></penhoraRosto>
				<dataAudiencia><%=request.getAttribute("DataAudiencia") != null ? request.getAttribute("DataAudiencia") : ""%></dataAudiencia>
				<enderecoServentia><%=serventia.getLogradouro()%> n&ordm; <%=serventia.getNumero()%>&nbsp;<%=serventia.getComplemento()%>&nbsp;<%=serventia.getCep()%>&nbsp;<%=serventia.getBairro()%>&nbsp;<%=serventia.getCidade()%>-<%=serventia.getEstado()%></enderecoServentia>
			</informacoes>
		</processo>
	</mensagem>
</resposta>