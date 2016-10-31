package com.neomind.fusion.custom.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class JRelatorioUtils
{

	public static File criarRelatorioXLSByQuerySQLComColunasExtras(List<String> colunas,
			List<Object[]> result, String nomeRelatorio, List<ColunaExtraRelatorioJDBC> colunasExtras)
	{
		Integer qtdDeColunas = result.get(0).length;
		Integer novaQtd = qtdDeColunas + colunasExtras.size();

		List<Object[]> newResult = new ArrayList<Object[]>();

		for (Object[] registro : result)
		{
			Object[] novoRegistro = new Object[novaQtd];
			for (int j = 0; j < registro.length; j++)
			{
				novoRegistro[j] = registro[j];
			}
			newResult.add(novoRegistro);
		}

		int novasCasasArray = qtdDeColunas;
		novasCasasArray = novasCasasArray - 1;
		for (ColunaExtraRelatorioJDBC colunaExtraRelatorioJDBC : colunasExtras)
		{
			colunas.add(colunaExtraRelatorioJDBC.getNomeColuna());

			novasCasasArray++;
			for (int i = 0; i < colunaExtraRelatorioJDBC.getRegistrosOrdenados().size(); i++)
			{
				String novoValor = colunaExtraRelatorioJDBC.getRegistrosOrdenados().get(i);
				Object[] novoObj = newResult.get(i);
				novoObj[novasCasasArray] = novoValor;
			}
		}

		return gerarArquivoXLS(nomeRelatorio, colunas, newResult);
	}

	public static File criarRelatorioXLSByQuerySQL(String query, String nomeRelatorio)
	{
		RetornoRelatorioJDBC retorno = JUtils.buscaObjetosJDBCComCabecalhoUtilizandoPersistXML(query);

		List<String> colunas = retorno.getColunas();
		List<Object[]> result = retorno.getRegistros();

		return gerarArquivoXLS(nomeRelatorio, colunas, result);
	}

	private static File gerarArquivoXLS(String nomeRelatorio, List<String> colunas, List<Object[]> result)
	{
		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFFont fsTitle = workbook.createFont();
		fsTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fsTitle.setColor(HSSFColor.WHITE.index);

		HSSFCellStyle sTitle = workbook.createCellStyle();
		sTitle.setFont(fsTitle);
		sTitle.setBorderBottom((short) 1);
		sTitle.setBorderLeft((short) 1);
		sTitle.setBorderRight((short) 1);
		sTitle.setBorderTop((short) 1);
		sTitle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		sTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFCellStyle sLinha1 = workbook.createCellStyle();
		sLinha1.setBorderBottom((short) 1);
		sLinha1.setBorderLeft((short) 1);
		sLinha1.setBorderRight((short) 1);
		sLinha1.setBorderTop((short) 1);
		sLinha1.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		sLinha1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFCellStyle sLinha2 = workbook.createCellStyle();
		sLinha2.setBorderBottom((short) 1);
		sLinha2.setBorderLeft((short) 1);
		sLinha2.setBorderRight((short) 1);
		sLinha2.setBorderTop((short) 1);

		HSSFSheet firstSheet = workbook.createSheet(nomeRelatorio);
		FileOutputStream fos = null;

		try
		{
			File file = File.createTempFile(nomeRelatorio, ".xls");
			fos = new FileOutputStream(file);

			HSSFRow row = firstSheet.createRow(0);

			int iCabecalho = 0;
			for (String coluna : colunas)
			{
				HSSFCell cellTitle = row.createCell(iCabecalho);
				cellTitle.setCellValue(coluna);
				cellTitle.setCellStyle(sTitle);
				iCabecalho++;
			}

			int i = 1;
			for (Object[] objeto : result)
			{
				row = firstSheet.createRow(i);

				for (int j = 0; j < colunas.size(); j++)
				{
					HSSFCell cell = row.createCell(j);
					if (objeto[j] != null)
					{
						cell.setCellValue(objeto[j].toString());
					}
					else
					{
						cell.setCellValue("");
					}

					if (i % 2 == 0)
					{
						cell.setCellStyle(sLinha1);
					}
					else
					{
						cell.setCellStyle(sLinha2);
					}
				}

				i++;
			}

			workbook.write(fos);
			fos.flush();
			fos.close();
			File arquivo = new File(file.getAbsolutePath());
			return arquivo;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
