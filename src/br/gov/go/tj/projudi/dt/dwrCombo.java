package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class dwrCombo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1037037234164022622L;
	private String option;
	private String value;

	private String tittle;
	private String height;
	private String width;
	private String name;
	private String id;
	private String size;
	private String selected;
	
	public dwrCombo(String option, String value, String selected) {
		this(option, value);
		this.selected = selected;
	}

	public dwrCombo(String option, String value) {
		super();
		this.option = option;
		this.value = value;
	}

	public dwrCombo() {
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}
}
