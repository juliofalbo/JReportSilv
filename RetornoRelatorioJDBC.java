package com.neomind.fusion.custom.utils;

import java.util.List;

public class RetornoRelatorioJDBC
{
	private List<String> colunas;
	private List<Object[]> registros;

	public List<String> getColunas()
	{
		return colunas;
	}

	public void setColunas(List<String> colunas)
	{
		this.colunas = colunas;
	}

	public List<Object[]> getRegistros()
	{
		return registros;
	}

	public void setRegistros(List<Object[]> registros)
	{
		this.registros = registros;
	}
}
