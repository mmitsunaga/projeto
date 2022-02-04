package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.PreparedStatementTJGO;

public class Select {
	StringBuilder query;
	PreparedStatementTJGO ps;
	
	public Select(String columns, PreparedStatementTJGO ps) {
		this.ps = ps;
		query = new StringBuilder();
		addText("SELECT " + columns);
	}
	
	private void addText(String text) {
		query.append(" ").append(text).append(" ");
	}
	
	private void addArgument(Object text) throws Exception {
		if(text instanceof Integer) {
			ps.adicionarLong((int) text);
		} else if(text instanceof Boolean) {
			ps.adicionarBoolean((boolean) text);
		} else {
			ps.adicionarString(String.valueOf(text));
		}
	}
	
	public String build() {
		return query.toString();
	}
	
	public Select from(String table) {
		addText("FROM " + table);
		return this;
	}
	
	public Select from(String table, String alias) {
		addText("FROM " + table + " " + alias);
		return this;
	}
	
	public Select where() {
		addText("WHERE");
		return this;
	}
	
	public Select whereDummy() {
		addText("WHERE 1=1");
		return this;
	}
	
	public Select whereEqual(String col, Object argument) throws Exception {
		addText("WHERE " + col + " = ?");
		addArgument(argument);
		return this;
	}
	
	
	
	public Select and() {
		addText("AND");
		return this;
	}
	
	public Select andNull(String col) {
		addText("AND " + col + " IS NULL");
		return this;
	}
	
	public Select andNotNull(String col) {
		addText("AND " + col + " IS NOT NULL");
		return this;
	}
	
	public Select addRaw(String text) {
		addText(text);
		return this;
	}
	
	public Select or() {
		addText("OR");
		return this;
	}
	
	public Select subquery(String subquery) {
		addText("(" + subquery + ")");
		return this;
	}
	
	public Select in(String col, Object ...arguments) throws Exception {
		addText(col + " IN (" + getQuestionMark(arguments.length) + ")");
		for(Object argument : arguments) {
			addArgument(argument);
		}
		return this;
	}
	
	public String getQuestionMark(int size) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size; i++) {
			builder.append("?,");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
	
	public Select andEqual(String col, Object argument) throws Exception {
		addText("AND " + col + " = ?");
		addArgument(argument);
		return this;
	}
	
	public Select condition(String col, String operator, Object argument) throws Exception {
		addText(col + " " + operator + " ?");
		addArgument(argument);
		return this;
	}
	
	public Select conditionQuery(String col, String operator, String subquery) throws Exception {
		addText(col + " " + operator);
		subquery(subquery);
		return this;
	}
	
	public Select equalQuery(String col, String subquery) throws Exception {
		conditionQuery(col, "=", subquery);
		return this;
	}
	
	public Select exists(String subquery) throws Exception {
		addText("EXISTS");
		subquery(subquery);
		return this;
	}
	
	public Select notExists(String subquery) throws Exception {
		addText("NOT EXISTS");
		subquery(subquery);
		return this;
	}
	
	public Select orEqual(String col, String argument) throws Exception {
		addText("OR " + col + " = ?");
		addArgument(argument);
		return this;
	}
	
	public Select less(String col, String argument) throws Exception {
		addText(col + " < ?");
		addArgument(argument);
		return this;
	}
	public Select lessOrEqual(String col, String argument) throws Exception {
		addText(col + " <= ?");
		addArgument(argument);
		return this;
	}
	
	public Select greater(String col, String argument) throws Exception {
		addText(col + " > ?");
		addArgument(argument);
		return this;
	}
	
	public Select greaterOrEqual(String col, String argument) throws Exception {
		addText(col + " >= ?");
		addArgument(argument);
		return this;
	}
	
	public Select join(String table, String alias, String condition) {
		addText("JOIN " + table + " " + alias + " ON " + condition);
		return this;
	}
	
	public Select leftJoin(String table, String alias, String condition) {
		addText("LEFT JOIN " + table + " " + alias + " ON " + condition);
		return this;
	}
	
	public Select order(String text) {
		addText("ORDER BY " + text);
		return this;
	}
	
	public Select group(String text) {
		addText("GROUP BY " + text);
		return this;
	}
	
}
