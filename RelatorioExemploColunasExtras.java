package com.neomind.fusion.custom.seop.relatorio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neomind.fusion.custom.utils.ColunaExtraRelatorioJDBC;
import com.neomind.fusion.custom.utils.JDaoUtils;
import com.neomind.fusion.custom.utils.JRelatorioUtils;
import com.neomind.fusion.custom.utils.RetornoRelatorioJDBC;

@SuppressWarnings("serial")
@WebServlet("/servlet/RelatorioExemploColunasExtras")
public class RelatorioExemploColunasExtras extends HttpServlet
{

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String query = "select p.nome as 'Nome', p.data as 'Data de Entrada' from Pessoa p";

		RetornoRelatorioJDBC retorno = JDaoUtils.buscaObjetosJDBCComCabecalhoUtilizandoPersist(query, getCaminhoPersist());

		List<Object[]> processosAlvara = retorno.getRegistros();

		List<ColunaExtraRelatorioJDBC> colunasExtras = new ArrayList<ColunaExtraRelatorioJDBC>();

		ColunaExtraRelatorioJDBC coluna1 = new ColunaExtraRelatorioJDBC();
		coluna1.setNomeColuna("Nova Coluna 1");

		ColunaExtraRelatorioJDBC coluna2 = new ColunaExtraRelatorioJDBC();
		coluna2.setNomeColuna("Nova Coluna 2");

		List<String> registrosColuna1 = new ArrayList<String>();
		List<String> registrosColuna2 = new ArrayList<String>();

		for (Object[] objeto : processosAlvara)
		{
			//Popular String com os respectivos valores recuperados do objeto.
			String novaColuna1 = "";
			registrosColuna1.add(novaColuna1);
			String novaColuna2 = "";
			registrosColuna2.add(novaColuna2);
		}

		coluna1.setRegistrosOrdenados(registrosColuna1);
		coluna2.setRegistrosOrdenados(registrosColuna2);

		colunasExtras.add(coluna1);
		colunasExtras.add(coluna2);

		File arquivo = JRelatorioUtils.criarRelatorioXLSByQuerySQLComColunasExtras(retorno.getColunas(),
				retorno.getRegistros(), "nomeDoSeuRelatorio", colunasExtras);

		download(request, response, arquivo.getAbsolutePath(), "NomeDoSeuRelatorio");

	}

	private void download(HttpServletRequest request, HttpServletResponse response, String path,
			String name) throws IOException
	{
		OutputStream out = null;
		try
		{
			out = response.getOutputStream();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		File arquivo = new File(path);
		String sContentType = null;
		sContentType = "application/pdf";
		response.setContentType(sContentType);
		response.addHeader("content-disposition", "attachment; filename=" + name + ".xls");
		InputStream in = null;
		in = new BufferedInputStream(new FileInputStream(arquivo));
		if (in != null)
		{
			response.setContentLength((int) in.available());
			int l;
			byte b[] = new byte[4096];
			while ((l = in.read(b, 0, b.length)) != -1)
			{
				out.write(b, 0, l);
			}
			out.flush();
			in.close();
		}
		out.close();

	}

	/**
	 * Inserir esse método na servlet que é iniciada junto ao projeto, para que não seja preciso recuperar esse caminho em toda Servlet.
	 * @return
	 */
	private String getCaminhoPersist()
	{
		String caminho = getServletContext().getRealPath(File.separator) + File.separator + "WEB-INF"
				+ File.separator + "classes" + File.separator + "META-INF" + File.separator
				+ "persist.xml";
		return caminho;
	}

}