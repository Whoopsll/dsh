package com.nh.dsh.admin.utils;

import cn.hutool.core.io.IoUtil;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.util.List;

public class PdfGeneratorUtils {

    //网站审核报告
    private static void generatorPdf(ServletOutputStream outputStream) {
        // 创建文档
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document,outputStream);
            // 打开文档
            document.open();
            // 获取字体文件流
            byte[] fontBytes = IoUtil.readBytes(new ClassPathResource("fonts/STSong.ttf").getInputStream());
            // 添加标题
            BaseFont baseFont = BaseFont.createFont("STSong.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, true, fontBytes, null);
            Font titleFont = new Font(baseFont, 16, Font.NORMAL);
            Font chineseFont = new Font(baseFont, 12, Font.NORMAL);
            Paragraph title = new Paragraph("Web资源不合规报告", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            // 添加空行
            document.add(new Paragraph("\n"));
            // 添加空行
            document.add(new Paragraph("\n"));
            PdfPTable inputTable = new PdfPTable(3); // 4列
            inputTable.setWidthPercentage(100);
            inputTable.setWidths(new float[]{1,1,1}); // 设置列宽比例
            PdfPCell firstCell = new PdfPCell(new Phrase("服务地址:", chineseFont));
            PdfPCell secondCell = new PdfPCell(new Phrase("用户名:", chineseFont));
            PdfPCell thirdCell = new PdfPCell(new Phrase("日期:", chineseFont));
            secondCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            thirdCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            firstCell.setBorder(PdfPCell.NO_BORDER);
            secondCell.setBorder(PdfPCell.NO_BORDER);
            thirdCell.setBorder(PdfPCell.NO_BORDER);
            inputTable.addCell(firstCell);
            inputTable.addCell(secondCell);
            inputTable.addCell(thirdCell);
            document.add(inputTable);
            // 添加空白行，模拟边距
            PdfPTable table = new PdfPTable(1);
            // 添加一个空白单元格来模拟下边距
            PdfPCell emptyCell = new PdfPCell(new Phrase("")); // 空单元格
            emptyCell.setBorder(PdfPCell.NO_BORDER);  // 去掉边框
            emptyCell.setMinimumHeight(5f);          // 设置高度，相当于下边距
            table.addCell(emptyCell);
            document.add(table);
            // 添加数据表格
            PdfPTable dataTable = new PdfPTable(4); // 4列
            dataTable.setWidthPercentage(100);
            dataTable.setWidths(new float[]{1, 4, 4, 2}); // 设置列宽比例

            // 表头
            dataTable.addCell(new Phrase("序号", chineseFont));
            dataTable.addCell(new Phrase("链接地址", chineseFont));
            dataTable.addCell(new Phrase("不合规原因", chineseFont));
            dataTable.addCell(new Phrase("审核时间", chineseFont));

            // 表格内容示例（可动态填充）
            for (int i = 0; i < 3; i++) {
                dataTable.addCell(String.valueOf(i + 1)); // 序号
                dataTable.addCell(new Phrase("http://example.com/resource" + (i + 1), chineseFont));
                dataTable.addCell(new Phrase("违规原因示例" + (i + 1), chineseFont));
                dataTable.addCell(new Phrase("2024-11-25", chineseFont));
            }
            document.add(dataTable);

            // 添加统计信息
            Paragraph stats = new Paragraph("统计：10 条", chineseFont);
            stats.setAlignment(Element.ALIGN_RIGHT);
            document.add(stats);

            // 关闭文档
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            document.close();
            try {
                outputStream.close();
            } catch (IOException err) {
                e.printStackTrace();
            }
        } finally {
            document.close();
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //下载网站审核报告
    public static void downloadReport(HttpServletResponse response, String fileName) {
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''审核报告.pdf");
        response.setCharacterEncoding("UTF-8");

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            generatorPdf(outputStream);
            outputStream.flush(); // 确保数据写入完毕
        } catch (IOException e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "PDF 生成失败");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    //其他审核报告PDF文件生成
    private static void generatorOtherAuditPdf(ServletOutputStream outputStream, String logs,String titleStr) {
        // 创建文档
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        float leading = 30f; // 行高（leading）
        try {
            // 创建 PDF 写入器
            PdfWriter.getInstance(document,outputStream);
            // 打开文档
            document.open();
            // 获取字体文件流
            byte[] fontBytes = IoUtil.readBytes(new ClassPathResource("fonts/kaiti.ttf").getInputStream());
            // 添加标题
            BaseFont baseFont = BaseFont.createFont("kaiti.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, true, fontBytes, null);
            Font titleFont = new Font(baseFont, 20, Font.NORMAL);
            Font subTitle = new Font(baseFont, 16, Font.NORMAL);
            Font chineseFont = new Font(baseFont, 13, Font.NORMAL);
            Paragraph title = new Paragraph(titleStr + "审核报告", titleFont);
            title.setLeading(leading);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));
            //转换json字符串
            Gson gson = new Gson();
            Paragraph stats = new Paragraph(logs, chineseFont);
            stats.setLeading(leading);
            stats.setAlignment(Element.ALIGN_LEFT); // 设置左对齐
            document.add(stats);
            // 关闭文档
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            document.close();
            try {
                outputStream.close();
            } catch (IOException err) {
                e.printStackTrace();
            }
        } finally {
            document.close();
        }
    }
    //下载其他审核报告文件
    public static void downloadOtherAuditReport(HttpServletResponse response, String logs, String title) {
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + title + ".pdf");
        response.setCharacterEncoding("UTF-8");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            generatorOtherAuditPdf(outputStream, logs,title);
            outputStream.flush(); // 确保数据写入完毕
        } catch (IOException e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "PDF 生成失败");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
