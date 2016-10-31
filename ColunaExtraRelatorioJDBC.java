package com.neomind.fusion.custom.utils;

import java.util.List;

public class ColunaExtraRelatorioJDBC
{
	private String nomeColuna;
	private List<String> registrosOrdenados;

	public String getNomeColuna()
	{
		return nomeColuna;
	}

	public void setNomeColuna(String nomeColuna)
	{
		this.nomeColuna = nomeColuna;
	}

	public List<String> getRegistrosOrdenados()
	{
		return registrosOrdenados;
	}

	public void setRegistrosOrdenados(List<String> registrosOrdenados)
	{
		this.registrosOrdenados = registrosOrdenados;
	}

}
