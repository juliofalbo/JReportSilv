package com.neomind.fusion.custom.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.neomind.fusion.engine.FusionRuntime;

public class JUtils
{

	public static RetornoRelatorioJDBC buscaObjetosJDBCComCabecalhoUtilizandoPersistXML(String query)
	{
		String[] conexao = getConexaoJDBCByPersist();

		//Recupera o usuário
		String user = conexao[0];

		//Recupera a senha
		String senha = conexao[1];

		//Recupera a URL de conexão
		String sqlConexao = conexao[2];

		RetornoRelatorioJDBC retorno = buscaObjetosJDBCComCabecalho(query, sqlConexao, user, senha);

		return retorno;
	}

	public static RetornoRelatorioJDBC buscaObjetosJDBCComCabecalho(String query, String sqlConexao,
			String user, String senha)
	{
		// Criando as variáveis de conexão e de statement
		Connection con;
		Statement stmt;

		// Verificando se o driver JDBC está instalado e pode ser utilizado
		try
		{
			Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
		}
		catch (java.lang.ClassNotFoundException e)
		{
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		//Criando o modelo de Retorno
		RetornoRelatorioJDBC retorno = new RetornoRelatorioJDBC();

		//Cria a lista de colunas
		List<String> colunasCabecalho = new ArrayList<String>();

		//Cria a lista de retornos dos objetos
		List<Object[]> registros = new ArrayList<Object[]>();
		try
		{
			// Abrindo a conexão com o servidor, login e senha
			con = DriverManager.getConnection(sqlConexao, user, senha);

			stmt = con.createStatement();

			// Criando a instrução a partir do SELECT que está dentro da variável query
			ResultSet rs = stmt.executeQuery(query);

			//Recuperando o MetaData para verificar as colunas a serem exibidas que foram mapeadas na query
			ResultSetMetaData rsmd = rs.getMetaData();

			Boolean primeiraVez = true;
			// Fazendo um loop para mostrar tudo o que foi retornado do banco
			while (rs.next())
			{
				//Recupera a quantidade de colunas a serem exibidas
				Integer colunas = rsmd.getColumnCount();

				//Cria um Array de Objetos onde será setado o resultado da Query
				Object[] obj = new Object[colunas];
				for (int i = 0; i < colunas; i++)
				{
					//Recupera a coluna em envidência
					Integer colunaFor = i + 1;

					//Recupera o nome da coluna em evidência
					String coluna = rsmd.getColumnName(colunaFor);

					//Recupera o valor da coluna em evidência
					Object s = rs.getObject(coluna);
					obj[i] = s;

					//Adicionando o nome da coluna na lista de Colunas
					if (primeiraVez)
					{
						colunasCabecalho.add(coluna);
					}
				}

				//Adiciona o objeto recuperado na lista de retorno
				registros.add(obj);
				primeiraVez = false;
			}

			//Populando o objeto retorno
			retorno.setColunas(colunasCabecalho);
			retorno.setRegistros(registros);

			//Fechando a instrução e a conexão
			stmt.close();
			con.close();
		}
		catch (SQLException ex)
		{
			System.err.println("SQLException: " + ex.getMessage());
		}

		return retorno;
	}

	public static List<Object[]> buscaObjetosJDBCUtilizandoPersistXML(String query)
	{
		String[] conexao = getConexaoJDBCByPersist();

		//Recupera o usuário
		String user = conexao[0];

		//Recupera a senha
		String senha = conexao[1];

		//Recupera a URL de conexão
		String sqlConexao = conexao[2];

		return buscaObjetosJDBC(query, sqlConexao, user, senha);
	}

	private static List<Object[]> buscaObjetosJDBC(String query, String sqlConexao, String user,
			String senha)
	{
		// Criando as variáveis de conexão e de statement
		Connection con;
		Statement stmt;

		// Verificando se o driver JDBC está instalado e pode ser utilizado
		try
		{
			Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
		}
		catch (java.lang.ClassNotFoundException e)
		{
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		//Cria a lista de retornos dos objetos
		List<Object[]> retorno = new ArrayList<Object[]>();

		try
		{
			// Abrindo a conexão com o servidor, login e senha
			con = DriverManager.getConnection(sqlConexao, user, senha);

			stmt = con.createStatement();

			// Criando a instrução a partir do SELECT que está dentro da variável query
			ResultSet rs = stmt.executeQuery(query);

			//Recuperando o MetaData para verificar as colunas a serem exibidas que foram mapeadas na query
			ResultSetMetaData rsmd = rs.getMetaData();

			// Fazendo um loop para mostrar tudo o que foi retornado do banco
			while (rs.next())
			{
				//Recupera a quantidade de colunas a serem exibidas
				Integer colunas = rsmd.getColumnCount();

				//Cria um Array de Objetos onde será setado o resultado da Query
				Object[] obj = new Object[colunas];
				for (int i = 0; i < colunas; i++)
				{
					//Recupera a coluna em envidência
					Integer colunaFor = i + 1;

					//Recupera o nome da coluna em evidência
					String coluna = rsmd.getColumnName(colunaFor);

					//Recupera o valor da coluna em evidência
					Object s = rs.getObject(coluna);
					obj[i] = s;
				}

				//Adiciona o objeto recuperado na lista de retorno
				retorno.add(obj);
			}
			//Fechando a instrução e a conexão
			stmt.close();
			con.close();
		}
		catch (SQLException ex)
		{
			System.err.println("SQLException: " + ex.getMessage());
		}

		return retorno;
	}

	public static String[] getConexaoJDBCByPersist()
	{
		String[] retorno = new String[3];
		String url = "";
		String user = "";
		String senha = "";

		String caminho = getCaminhoPersist();

		File fXmlFile = new File(caminho);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try
		{
			dBuilder = dbFactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		Document doc = null;
		try
		{
			doc = dBuilder.parse(fXmlFile);
		}
		catch (SAXException | IOException e)
		{
			e.printStackTrace();
		}

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("property");

		for (int temp = 0; temp < nList.getLength(); temp++)
		{
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;
				String name = eElement.getAttribute("name");
				String value = eElement.getAttribute("value");

				if (name.equals("hibernate.connection.username"))
				{
					user = value;
					retorno[0] = user;
				}
				else if (name.equals("hibernate.connection.password"))
				{
					senha = value;
					retorno[1] = senha;
				}
				else if (name.equals("hibernate.connection.url"))
				{
					url = value;
					retorno[2] = url;
				}
			}
		}

		return retorno;
	}

	public static String getCaminhoPersist()
	{
		String caminho = FusionRuntime.getInstance().getServerPath() + File.separator + "WEB-INF"
				+ File.separator + "classes" + File.separator + "META-INF" + File.separator
				+ "persist.xml";
		return caminho;
	}

}
