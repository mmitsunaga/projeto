<?xml version="1.0" encoding="iso-8859-1"?>
<resposta versao='1' operacao='<%=(String)request.getAttribute("Operacao")%>'>
	<situacao>OK</situacao>
	<mensagem>		
		<processo versao='1'><% ProcessoCadastroDt processo = (ProcessoCadastroDt) request.getAttribute("ProcessoCadastroDt");%>
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
					<advogados><% 
							List advogadosParte = parte.getListaAdvogados();
							if (advogadosParte != null && advogadosParte.size() > 0){
								for (int j=0; j < advogadosParte.size(); j++){	
									ProcessoParteAdvogadoDt advogadoParte = (ProcessoParteAdvogadoDt) advogadosParte.get(j); %>
						<advogado>
							<numeroOAB><%=advogadoParte.getOabNumero()%></numeroOAB>
							<estadoOAB><%=advogadoParte.getEstadoOabUf()%></estadoOAB>
							<complementoOAB><%=advogadoParte.getOabComplemento()%></complementoOAB>
						</advogado><%		
								}
							} else { %>
						<advogado>
							<numeroOAB></numeroOAB>
							<estadoOAB></estadoOAB>
							<complementoOAB></complementoOAB>
						</advogado><%  
							} %>
					</advogados>
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
					<advogados><% 
							List advogadosParte = parte.getListaAdvogados();
							if (advogadosParte != null && advogadosParte.size() > 0){
								for (int j=0; j < advogadosParte.size(); j++){	
								ProcessoParteAdvogadoDt advogadoParte = (ProcessoParteAdvogadoDt) advogadosParte.get(j); %>
						<advogado>
							<numeroOAB><%=advogadoParte.getOabNumero()%></numeroOAB>
							<estadoOAB><%=advogadoParte.getEstadoOabUf()%></estadoOAB>
							<complementoOAB><%=advogadoParte.getOabComplemento()%></complementoOAB>
						</advogado><%		
								}
							} else { %>
						<advogado>
							<numeroOAB></numeroOAB>
							<estadoOAB></estadoOAB>
							<complementoOAB></complementoOAB>
						</advogado><%  
							} %>
					</advogados>
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
							boTestemunha = true; 	%>
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
				</testemunha>
				<% 		}
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
							boComunicante = true; 	%>
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
				</comunicante>
				<%		} 
					}
				} 
				if (!boComunicante) {%>
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
					<endereco>
						<logradouro></logradouro>
						<numero></numero>
						<complemento></complemento>
						<bairro></bairro>
						<telefone></telefone>
						<codCidade></codCidade>
						<descricaoCidade></descricaoCidade>
						<uf></uf>
						<cep></cep>
					</endereco>
				</comunicante><% 
				} %>
			</comunicantes>
			<informacoes>
				<dataRecebimento><%=Funcoes.dateToStringSoData(new Date())%></dataRecebimento>
				<codArea><%=processo.getAreaDistribuicaoCodigo()%></codArea>
				<descricaoArea><%=processo.getAreaDistribuicao()%></descricaoArea>
				<codServentia><%=processo.getServentiaDt().getServentiaCodigo() %></codServentia>
				<descricaoServentia><%=processo.getServentiaDt().getServentia()%></descricaoServentia>
				<enderecoServentia><%=processo.getServentiaDt().getLogradouro()%> n&ordm; <%= processo.getServentiaDt().getNumero()%> 
							  <%=processo.getServentiaDt().getComplemento()%> <%=processo.getServentiaDt().getBairro()%>
							  <%=processo.getServentiaDt().getCidade()%> - <%=processo.getServentiaDt().getEstado()%>
							  <%=processo.getServentiaDt().getCep()%></enderecoServentia>
				<codTipoAcao><%=processo.getProcessoTipoCodigo()%></codTipoAcao>
				<descricaoTipoAcao><%=processo.getProcessoTipo()%></descricaoTipoAcao>
				<valorAcao><%=processo.getValor()%></valorAcao>
				<maior60anos><%=processo.isMaior60Anos()%></maior60anos>
				<segredoJustica><%=(processo.getSegredoJustica() != null && processo.getSegredoJustica().equals("true")? "SIM":"N&Atilde;O")%></segredoJustica><%
					if(processo.getAudienciaDt() != null && processo.getAudienciaDt().getId().length() > 0) {
						AudienciaDt audiencia = processo.getAudienciaDt();	%>
				<audiencia>
					<dataHora><%=audiencia.getDataAgendada()%></dataHora>
					<partesIntimadas><%
						List partesIntimadas = processo.getListaPartesIntimadas();
						if (partesIntimadas != null){
							for (int i=0;i<partesIntimadas.size();i++){
								ProcessoParteDt parteDt = (ProcessoParteDt)partesIntimadas.get(i);
					%>
						<nomeParte><%= parteDt.getNome() %></nomeParte>
					<%		}
						}	%>
					</partesIntimadas>
				</audiencia>
				<%	} else {	%>
				<audiencia>
					<dataHora></dataHora>
					<partesIntimadas>
						<nomeParte></nomeParte>
					</partesIntimadas>
				</audiencia><% 	
					} %>
			</informacoes>
			<arquivos>
				<%List arquivosProcesso = processo.getListaArquivos();
				  if (arquivosProcesso != null){
				  	for (int i=0; i<arquivosProcesso.size();i++){ 
				  		ArquivoDt arquivoDt = (ArquivoDt) arquivosProcesso.get(i);%>
				  		<arquivo>
				  			<idArquivo><%= arquivoDt.getId() %></idArquivo>
							<hashArquivo><%=arquivoDt.getHash() %></hashArquivo>
						</arquivo>
			        <%}
				  }%>
			</arquivos>
		</processo>
	</mensagem>
</resposta>
<%@page import="java.util.List" %>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCadastroDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<%@page import="java.util.Date"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoDt"%>