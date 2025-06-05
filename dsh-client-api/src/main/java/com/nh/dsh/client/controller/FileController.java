package com.nh.dsh.client.controller;

import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FileController {

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 保存上传的文件到临时文件夹
            File tempFile = File.createTempFile("ppt", ".pptx");
            file.transferTo(tempFile);

            // 处理PPT文件，去除水印
            File processedFile = removeWatermark(tempFile);

            // 返回处理后的文件路径或文件内容（这里以文件路径为例）
            return "文件处理成功！下载链接：/api/download/" + processedFile.getName();

        } catch (IOException e) {
            e.printStackTrace();
            return "文件处理失败";
        }
    }

    // 删除水印的逻辑
    private File removeWatermark(File inputFile) throws IOException {
        // 读取PPTX文件
        FileInputStream inputStream = new FileInputStream(inputFile);
        XMLSlideShow ppt = new XMLSlideShow(inputStream);

        // 获取幻灯片
        List<XSLFSlide> slides = ppt.getSlides();

        for (XSLFSlide slide : slides) {
            // 处理每个幻灯片上的所有形状
            if (slide.getShapes() != null && slide.getShapes().size() > 0) {
                System.out.println(slide.getShapes().size());
                if (slide.getShapes() != null && !slide.getShapes().isEmpty()) {
                    slide.getShapes().forEach(shape -> {
                        if (shape != null) {
                            if (shape != null) {  // 检查shape是否为null
                                // 删除文本框中的水印
                                if (shape instanceof XSLFTextBox) {
                                    XSLFTextBox textBox = (XSLFTextBox) shape;
                                    String text = textBox.getText();
                                    // 检查水印文本并删除
                                    if (isWatermarkText(text)) {
                                        slide.removeShape(shape); // 删除该文本框
                                    }
                                }

                                // 删除图片类水印
                                if (shape instanceof XSLFPictureShape) {
                                    slide.removeShape(shape);  // 删除图片水印
                                }

                                // 如果是背景图，也可以根据条件删除（例如，透明图像）
                                if (shape instanceof XSLFGroupShape) {
                                    XSLFGroupShape groupShape = (XSLFGroupShape) shape;
                                    groupShape.getShapes().forEach(innerShape -> {
                                        if (innerShape != null) {  // 确保内部形状不是null
                                            // 仅处理其中的图片类型形状
                                            if (innerShape instanceof XSLFPictureShape) {
                                                slide.removeShape(innerShape);  // 删除内部的图片水印
                                            }
                                        }
                                    });
                                }
                            }
                        } else {
                            System.out.println("Found a null shape, skipping.");
                        }
                    });
                } else {
                    System.out.println("No shapes found in the slide.");
                }
            }
        }

        // 保存修改后的PPT文件
        File outputFile = new File("processed_" + inputFile.getName());
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            ppt.write(outputStream);
        }

        inputStream.close();
        return outputFile;
    }

    public static void removeWatermark(String inputFilePath, String outputFilePath, String watermarkText) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(inputFilePath));
        XMLSlideShow ppt = new XMLSlideShow(inputStream);

        // 遍历每一页幻灯片
        for (XSLFSlide slide : ppt.getSlides()) {
            // 获取每一页的所有形状
            for (XSLFShape shape : slide.getShapes()) {
                // 处理文本水印
                if (shape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) shape;
                    String text = textShape.getText();
                    if (text != null && text.contains(watermarkText)) {
                        System.out.println("发现水印文本: " + text);
                        textShape.clearText(); // 清空文本
                    }
                }

                // 处理图片水印
                if (shape instanceof XSLFPictureShape) {
                    XSLFPictureShape pictureShape = (XSLFPictureShape) shape;
                    // 根据业务逻辑判断是否为水印图片（例如通过图片名或大小）
                    System.out.println("发现图片: " + pictureShape.getPictureData().getFileName());
                    slide.removeShape(pictureShape); // 移除图片
                }
            }
        }

        // 保存处理后的文件
        FileOutputStream outputStream = new FileOutputStream(new File(outputFilePath));
        ppt.write(outputStream);

        // 关闭资源
        outputStream.close();
        ppt.close();
        inputStream.close();
    }

    // 判断文本是否是水印文本
    private boolean isWatermarkText(String text) {
        // 简单的水印文本判断（可以根据实际情况调整）
        return text.contains("Watermark") || text.contains("Confidential") || text.contains("Sample");
    }

    @GetMapping("/download/{filename}")
    public File downloadFile(@PathVariable String filename) {
        // 返回处理后的文件
        File file = new File(filename);
        if (file.exists()) {
            return file;
        }
        return null;
    }
}
