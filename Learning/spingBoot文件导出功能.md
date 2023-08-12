### 简介

有时候随着业务的发展，需要把一些数据组合起来导出一份精美的word文档或者PDF文档，所以悲催的后台工程师就要开始他苦逼的一天

注意标题哦 是精美的文档 不是什么简单的一批的文档...

### 技术栈

springBoot + poi-tl + aspose-words

### 引入maven依赖

````xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-scratchpad</artifactId>
            <version>4.1.2</version>
        </dependency>

        <dependency>
            <groupId>com.deepoove</groupId>
            <artifactId>poi-tl</artifactId>
            <version>1.10.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>4.1.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>4.1.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml-schemas</artifactId>
            <version>4.1.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>ooxml-schemas</artifactId>
            <version>1.1</version>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.7</version>
        </dependency>
        <!--aspose 破解 word转pdf-->
<!--        <dependency>-->
<!--            <groupId>com.aspose</groupId>-->
<!--            <artifactId>aspose-words</artifactId>-->
<!--            <version>0.0.1-SNAPSHOT</version>-->
<!--            <scope>system</scope>-->
<!--            <systemPath>${project.basedir}/src/main/resources/lib/aspose-words-16.8.0-jdk16.jar</systemPath>-->
<!--        </dependency>-->

        <dependency>
            <groupId>com.aspose</groupId>
            <artifactId>aspose-words</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/src/main/resources/lib/aspose-words-crack-20.11.jar</systemPath>
        </dependency>

<!--        <dependency>-->
<!--            <groupId>com.aspose</groupId>-->
<!--            <artifactId>aspose-words</artifactId>-->
<!--            <version>0.0.1-SNAPSHOT</version>-->
<!--            <scope>system</scope>-->
<!--            <systemPath>${project.basedir}/src/main/resources/lib/aspose-words-19.5jdk.jar</systemPath>-->
<!--        </dependency>-->

        <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>fr.opensagres.poi.xwpf.converter.pdf-gae</artifactId>
            <version>2.0.1</version>
        </dependency>
        <dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.12.1</version>
        </dependency>
        <!--html渲染插件-->
        <dependency>
            <groupId>io.github.draco1023</groupId>
            <artifactId>poi-tl-ext</artifactId>
            <version>0.3.8</version>
            <exclusions>
                <exclusion>
                    <groupId>com.deepoove</groupId>
                    <artifactId>poi-tl</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.apache.poi</groupId>
                    <artifactId>ooxml-schemas</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!--样式依赖-->
        <dependency>
            <groupId>org.w3c.css</groupId>
            <artifactId>sac</artifactId>
            <version>1.3</version>
        </dependency>
        <dependency>
            <groupId>net.sourceforge.cssparser</groupId>
            <artifactId>cssparser</artifactId>
            <version>0.9.29</version>
        </dependency>
    </dependencies>
````



### Word导出

代码片段：

````java
public void referenceHotelExport( List<GroupReferenceHotel> referenceHotelList) {
        String fileName = "参考酒店.docx";
        String resource = "/plan/referenceHotel.docx";
        // 在模版内需要显示渲染的字段
        Map<String,Object> map = new HashMap<>();
        for (GroupReferenceHotel groupReferenceHotel : referenceHotelList) {
            Map<Integer, List<GroupReferenceHotelSon>> collect = groupReferenceHotel.getGroupReferenceHotelSons().stream().collect(Collectors.groupingBy(u -> u.getReferType()));
            for (Map.Entry<Integer, List<GroupReferenceHotelSon>> integerListEntry : collect.entrySet()) {
                if (integerListEntry.getKey().compareTo(0) == 0) {
                    groupReferenceHotel.setGroupReferenceZero(integerListEntry.getValue());
                }
                if (integerListEntry.getKey().compareTo(1) == 0) {
                    groupReferenceHotel.setGroupReferenceOne(integerListEntry.getValue());
                }
            }
        }
        map.put("groupReferenceHotel",referenceHotelList);
        try {
            ClassPathResource templatePath = new ClassPathResource(resource);
            InputStream inputStream = templatePath.getInputStream();
            OutputStream fos = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=\"" + new String(fileName.getBytes("UTF-8"), "iso-8859-1") + "\"");
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Type", "application/vnd.ms-word");
            response.addHeader("Content-Type", "application/x-msword");
            WordUtil.replaceWord(fos,inputStream,map);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
````

对应的 "参考酒店.docx" 的模版

这里关于 poi-tl的语法使用我就不说了 ，想了解的直接去他们官网看就行了

![image-20230616124640516](https://picgo-1300442379.cos.ap-shanghai.myqcloud.com/img/image-20230616124640516.png) 

WordUtil类的代码：

````java
package com.wintac.util;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.HackLoopTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.xwpf.NiceXWPFDocument;
import com.wintac.Policy.DetailTablePolicy;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.ddr.poi.html.HtmlRenderPolicy;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2023/2/20 15:54
 * @Author tangwei
 */
public class WordUtil {
    /**
     * 添加自定义配置
     */
    public static final Configure config = Configure
            .builder()
             // 自定义表格的生成
            .bind("quotation",new DetailTablePolicy())
            // html内容生成导出word
            .bind("htmlContent",new HtmlRenderPolicy())
            // 图片的处理
            .addPlugin('%', new PoiPicPolicy())
            .build();

    // 导出Word
    public static void replaceWord(OutputStream response, InputStream in, Map<String, Object> params) throws Exception {
        XWPFTemplate template = null;
        try {
            template = XWPFTemplate.compile(in,config).render(params);
            template.write(response);
        }finally {
            if (template != null) {
                template.close();
            }
        }
    }

    // 导出PDF
    public static void replacePdf(OutputStream response,InputStream in, Map<String, Object> params) throws Exception {
        XWPFTemplate template = null;
        try {
            template = XWPFTemplate.compile(in,config).render(params);
            ByteArrayOutputStream wordOut = new ByteArrayOutputStream(30 * 1024);
            template.write(wordOut);
            byte[] byteArray = wordOut.toByteArray();
            byte[] bytes = WordAsposeUtil.wordToPDF(new ByteArrayInputStream(byteArray),(params.get("mapUrl") != null ? params.get("mapUrl").toString() : ""));
            response.write(bytes);
        }finally {
            if (template != null) {
                template.close();
            }
        }
    }
}
````



#### 自定义表格的生成（DetailTablePolicy）

DetailTablePolicy 类：

注意：此代码的大体流程就是这样，具体需要根据你们自己的业务逻辑来生成，所以这块需要动点脑子才能实现效果

````java
package com.wintac.Policy;

import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.BorderStyle;
import com.deepoove.poi.data.style.CellStyle;
import com.deepoove.poi.data.style.ParagraphStyle;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.policy.TextRenderPolicy;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import com.wintac.Request.orc.quotationExcel.IndividualQuotation;
import com.wintac.Request.orc.quotationExcel.IndividualQuotationDetail;
import com.wintac.Request.orc.quotationExcel.Quotation;
import com.wintac.util.DetailTableMapUtil;
import lombok.SneakyThrows;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DetailTablePolicy implements RenderPolicy {
    @SneakyThrows
    @Override
    public void render(ElementTemplate eleTemplate, Object data, XWPFTemplate template) {
        RunTemplate runTemplate = (RunTemplate) eleTemplate;
        XWPFRun run = runTemplate.getRun();

        if (Objects.isNull(data)) {
            TextRenderPolicy.Helper.renderTextRun(run, "");
            return;
        }

        String[] items;
        Quotation quotation = JSONObject.parseObject(data.toString()).toJavaObject(Quotation.class);

        if (quotation.getIndividualQuotations().isEmpty()) {
            TextRenderPolicy.Helper.renderTextRun(run, "");
            return;
        }
        //设置的合并规则
        MergeCellRule.MergeCellRuleBuilder mergeCellRuleBuilder = MergeCellRule.builder();
        String[] meterHeader = {"a", "b"};
        //设置表头
        RowRenderData header = Rows.of(meterHeader).rowExactHeight(1.3).bgColor("DDDDDD").center()
                .textColor("000000").textFontSize(9).create();
        Tables.TableBuilder tableBuilder = Tables.ofWidth(18.46).addRow(header);
        int startRows = 1;
        int endRows = 0;
        //表体内容
        TextRenderData[] textRenderDataArr = new TextRenderData[2];
        for (int i = 0; i < meterHeader.length; i++) {
            TextRenderData textRenderData = new TextRenderData();
            if (meterHeader[i].equals("a")) {
                textRenderData.setText("我是a");
            }
            if (meterHeader[i].equals("b")) {
                textRenderData.setText("我是b");
            }
            Style style = new Style();
            style.setFontSize(8);
            textRenderData.setStyle(style);
            textRenderDataArr[i] = textRenderData;
        }
        RowRenderData row = Rows.of(textRenderDataArr).textFontSize(1).center().create();
        tableBuilder.addRow(row);
        
        //设置样式颜色尺寸等等
        BorderStyle borderStyle = new BorderStyle();
        borderStyle.setColor("DDDDDD");
        borderStyle.setSize(4);
        borderStyle.setType(XWPFTable.XWPFBorderType.SINGLE);
        double[] doubles = new double[]{3,3};
        TableRenderData tableRenderData = tableBuilder.border(borderStyle).width(6,doubles).center().create();
        
        /**
         * MergeCellRule支持多合并规则,会以Map的形式存入可以看一下源码
         * !!! 一定要设置完规则后再调用 MergeCellRule的build方法进行构建
         */
        tableRenderData.setMergeRule(mergeCellRuleBuilder.build());
        TableRenderPolicy.Helper.renderTable(run, tableRenderData);
        //todo 如果不加入下面代码生成的word会保留模板中的${table}
        TextRenderPolicy.Helper.renderTextRun(run, "");
    }
}
````



#### 图片的处理

poi-tl 对于图片的处理还是有点不到位 所以需要我们自行去处理一波

CustomPictureRenderData类：
````java
package com.wintac.model;

import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.RenderData;
import lombok.Data;

/**
 * @Description TODO
 * @Date 2023/2/20 15:43
 * @Author tangwei
 */
@Data
public class CustomPictureRenderData implements RenderData {
    public CustomPictureRenderData(int width, int height, String path, byte[] data, String altMeta, int leftOffset, int topOffset,int picType) {
        this.width = width;
        this.height = height;
        this.path = path;
        this.data = data;
        this.altMeta = altMeta;
        this.leftOffset = leftOffset;
        this.topOffset = topOffset;
        this.picType = picType;
    }

    public CustomPictureRenderData(int width, int height,byte[] data,int leftOffset, int topOffset,int picType,int imageType) {
        this.width = width;
        this.height = height;
        this.data = data;
        this.leftOffset = leftOffset;
        this.topOffset = topOffset;
        this.picType = picType;
        this.imageType = imageType;
    }

    /**
     * 图片格式
     */
    private int picType;
    /**
     * 图片宽度
     */
    private int width;
    /**
     * 图片高度
     */
    private int height;
    /**
     * 图片路径
     */
    private String path;
    /**
     * 0 图片 1 图标
     */
    private int imageType;
    /**
     * 图片二进制数据
     */
    private transient byte[] data;

    /**
     * 当图片不存在时，显示的文字
     */
    private String altMeta = "";

    /**
     * 左偏量
     */
    private int leftOffset;
    /**
     * 上偏移量
     */
    private int topOffset;
}
````

PoiPicPolicy类：

````java
package com.wintac.util;

import cn.hutool.core.util.RandomUtil;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.policy.PictureRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import com.wintac.model.CustomPictureRenderData;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Description TODO
 * @Date 2023/2/20 15:44
 * @Author tangwei
 */
@Configuration
public class PoiPicPolicy extends AbstractRenderPolicy<CustomPictureRenderData> {
    @Override
    protected boolean validate(CustomPictureRenderData data) {
        return (null != data && (null != data.getData() || null != data.getPath()));
    }

    @Override
    protected void afterRender(RenderContext<CustomPictureRenderData> context) {
        clearPlaceholder(context, false);
    }

    @Override
    protected void reThrowException(RenderContext<CustomPictureRenderData> context, Exception e) {
        logger.info("Render picture {}, error: {}",  context.getEleTemplate(),e.getMessage());
        context.getRun().setText(context.getData().getAltMeta(), 0);

    }

    @Override
    public void doRender(RenderContext<CustomPictureRenderData> renderContext) throws Exception {
        Helper.renderPicture(renderContext.getRun(), renderContext.getData());
    }

    public static class Helper {
        public static final int EMU = 9525;

        public static void renderPicture(XWPFRun run, CustomPictureRenderData picture) throws Exception {
            int suggestFileType = picture.getPicType();
            InputStream ins = null;
            try {
                ins = null == picture.getData() ? new FileInputStream(picture.getPath())
                        : new ByteArrayInputStream(picture.getData());
                String numberCode = RandomUtil.randomStringUpper(4);
                run.addPicture(ins, suggestFileType, "Generated" + numberCode, picture.getWidth() * EMU,
                        picture.getHeight() * EMU);
                CTDrawing drawing = run.getCTR().getDrawingArray(0);
                CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();

                //拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
                CTAnchor anchor = getAnchorWithGraphic(graphicalobject,
                        Units.toEMU(picture.getWidth()), Units.toEMU(picture.getHeight()),
                        Units.toEMU(picture.getLeftOffset()), Units.toEMU(picture.getTopOffset()),picture.getImageType());
                //添加浮动属性
                drawing.setAnchorArray(new CTAnchor[]{anchor});
                //删除行内属性
                drawing.removeInline(0);
            } finally {
                IOUtils.closeQuietly(ins);
            }
        }

        /**
         * @param ctGraphicalObject 图片数据
         * @param width             宽
         * @param height            高
         * @param leftOffset        水平偏移 left
         * @param topOffset         垂直偏移 top
         * @return
         * @throws Exception
         */
        private static CTAnchor getAnchorWithGraphic(CTGraphicalObject ctGraphicalObject, int width, int height,
                                                     int leftOffset, int topOffset,int imageType) {
            //图片排列方式
            String xml = "<wp:anchor allowOverlap=\"0\" layoutInCell=\"1\" locked=\"0\" behindDoc=\"0\" relativeHeight=\"0\" simplePos=\"0\" distR=\"0\" distL=\"0\" distB=\"0\" distT=\"0\" " +
                    " xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\"" +
                    " xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\"" +
                    " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" >" +
                    "<wp:simplePos y=\"0\" x=\"0\"/>" +
                    "<wp:positionH relativeFrom=\"column\">" +
//                    "<wp:align>center</wp:align>" +
                    "<wp:posOffset>" + leftOffset + "</wp:posOffset>" +
                    "</wp:positionH>" +
                    "<wp:positionV relativeFrom=\"paragraph\">" +
                    "<wp:posOffset>" + topOffset + "</wp:posOffset>" +
                    "</wp:positionV>" +
                    "<wp:extent cy=\""+height+"\" cx=\""+width+"\"/>" +
                    "<wp:effectExtent b=\"0\" r=\"0\" t=\"0\" l=\"0\"/>" +
                    "<wp:wrapTopAndBottom/>" +
                    "<wp:docPr descr=\"Picture Alt\" name=\"Picture Hit\" id=\"0\"/>" +
                    "<wp:cNvGraphicFramePr>" +
                    "<a:graphicFrameLocks noChangeAspect=\"true\" xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" />" +
                    "</wp:cNvGraphicFramePr>" +
                    "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                    "<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\" xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                    "<pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                    "<pic:nvPicPr>" +
                    "<pic:cNvPr name=\"Picture Hit\" id=\"1\"/>" +
                    "<pic:cNvPicPr/>" +
                    "</pic:nvPicPr>" +
                    "<pic:blipFill>" +
                    "<a:blip r:embed=\"0\"/>" +
                    "<a:stretch>" +
                    "<a:fillRect/>" +
                    "</a:stretch>" +
                    "</pic:blipFill>" +
                    "<pic:spPr>" +
                    "<a:xfrm>" +
                    "<a:off y=\"0\" x=\"0\"/>" +
                    "<a:ext cy=\""+height+"\" cx=\""+width+"\"/>" +
                    "</a:xfrm>" +
                    "<a:prstGeom prst=\"rect\">" +
                    "<a:avLst/>" +
                    "</a:prstGeom>" +
                    "</pic:spPr>" +
                    "</pic:pic>" +
                    "</a:graphicData>" +
                    "</a:graphic>" +
                    "<wp14:sizeRelH relativeFrom=\"margin\">" +
                    "<wp14:pctWidth>0</wp14:pctWidth>" +
                    "</wp14:sizeRelH>" +
                    "<wp14:sizeRelV relativeFrom=\"margin\">" +
                    "<wp14:pctHeight>0</wp14:pctHeight>" +
                    "</wp14:sizeRelV>" +
                    "</wp:anchor>";

            //图标排列方式
            String anchorXML =
                    "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                            + "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"0\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"0\">"
                            + "<wp:simplePos x=\"0\" y=\"0\"/>"
                            + "<wp:positionH relativeFrom=\"column\">"
                            + "<wp:posOffset>" + leftOffset + "</wp:posOffset>"
                            + "</wp:positionH>"
                            + "<wp:positionV relativeFrom=\"paragraph\">"
                            + "<wp:posOffset>" + topOffset + "</wp:posOffset>" +
                            "</wp:positionV>"
                            + "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
                            + "<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/>"
                            + "<wp:wrapNone/>"
                            + "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"G:/11.png\"/><wp:cNvGraphicFramePr/>"
                            + "</wp:anchor>";

            CTDrawing drawing = null;
            try {
                if (imageType == 0) {
                    drawing = CTDrawing.Factory.parse(xml);
                }
                if (imageType == 1) {
                    drawing = CTDrawing.Factory.parse(anchorXML);
                }
            } catch (XmlException e) {
                e.printStackTrace();
            }
            CTAnchor anchor = drawing.getAnchorArray(0);
            anchor.setGraphic(ctGraphicalObject);
            return anchor;
        }

        public static int suggestFileType(String imgFile) {
            int format = 0;

            if (imgFile.endsWith(".emf")) format = XWPFDocument.PICTURE_TYPE_EMF;
            else if (imgFile.endsWith(".wmf")) format = XWPFDocument.PICTURE_TYPE_WMF;
            else if (imgFile.endsWith(".pict")) format = XWPFDocument.PICTURE_TYPE_PICT;
            else if (imgFile.endsWith(".jpeg") || imgFile.endsWith(".jpg"))
                format = XWPFDocument.PICTURE_TYPE_JPEG;
            else if (imgFile.endsWith(".png")) format = XWPFDocument.PICTURE_TYPE_PNG;
            else if (imgFile.endsWith(".dib")) format = XWPFDocument.PICTURE_TYPE_DIB;
            else if (imgFile.endsWith(".gif")) format = XWPFDocument.PICTURE_TYPE_GIF;
            else if (imgFile.endsWith(".tiff")) format = XWPFDocument.PICTURE_TYPE_TIFF;
            else if (imgFile.endsWith(".eps")) format = XWPFDocument.PICTURE_TYPE_EPS;
            else if (imgFile.endsWith(".bmp")) format = XWPFDocument.PICTURE_TYPE_BMP;
            else if (imgFile.endsWith(".wpg")) format = XWPFDocument.PICTURE_TYPE_WPG;
            else {
                throw new RenderException("Unsupported picture: " + imgFile
                        + ". Expected emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg");
            }
            return format;
        }
    }
}
````



#### 总结

好了 这些类都处理完成后，回过头来看 WordUtil 

![image-20230616131229890](https://picgo-1300442379.cos.ap-shanghai.myqcloud.com/img/image-20230616131229890.png)

在导出文档的时候有一个map 需要传入进来做数据处理,如果你定义了表格了表格数据，就需要在map里面设定值，

这个值需要跟这里bind方法所对应的字段一样，所以在执行导出word的replaceWord方法后，会自动执行刚刚咱们自己定义的DetailTablePolicy类里面的方法,从而导出表格

在docx模版里面的语法是 {{#quotation}}



关于图片的处理，下面贴一个使用示例：

````java
            map.put("mapPic", new CustomPictureRenderData(525, 302, ReadFileUtil.readStream(ReadFileUtil.getImageStream(wordRequest.getMapPic())), 0, 10, XWPFDocument.PICTURE_TYPE_PNG,0));
````

在docx模版里面的语法是 {{%mapPic}}



关于html内容生成导出word

我这里使用的html的内容是通过富文本产生的,等于说是把这段代码可以生成到改word的任意位置

在docx模版里面的语法是 {{#htmlContent}}



### PDF导出

我这里是通过*aspose-words*来做的PDF导出，据说是现在市面上较好的word转pdf的工具，实测还不错，但是收费！！！，如果有需要这个破解版jar包的 可以加v:

<img src="https://picgo-1300442379.cos.ap-shanghai.myqcloud.com/img/image-20230616132618839.png" alt="image-20230616132618839" style="zoom:25%;" />

具体使用参照 WordUtil 类里面的 replacePdf 方法

下面我提供一下WordAsposeUtil类：

````java
package com.wintac.util;

import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.aspose.words.*;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.plugin.table.HackLoopTableRenderPolicy;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WordAsposeUtil {
        private static final String linuxPathFont = "/usr/share/fonts/chinese";

        /**
         * word转pdf
         *
         * @param wordInStream: word文件输入流;
         */
        public static byte[] wordToPDF(InputStream wordInStream,String mapUrl) {
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream(1024);
                ByteArrayOutputStream byteOutDocx = new ByteArrayOutputStream(1024);
                try {
                        InputStream licenseIn = WordUtil.class.getClassLoader().getResourceAsStream("license.xml");
                        License aposeLic = new License();
                        aposeLic.setLicense(licenseIn);

                        //因linux环境没有语言包,当项目部署到linux环境后,转成的pdf中的中文是类似乱码的,所以需要去加载此目录下的语言包;
                        OsInfo osInfo = SystemUtil.getOsInfo();
                        if (osInfo.isLinux()) {
                                System.out.println("我是linux环境");
                                //====================================== 注意 ============================================
                                //注意: 需要把项目中resource/fonts目录下的语言包全部放到linux中的此目录下,否则linux环境中处理是乱码;
                                //以下是我自己定义的目录,你可随意更改为你linux上的目录;
                                FontSettings.getDefaultInstance().setFontsFolders(
                                    new String[]{"/usr/share/fonts", "/usr/share/fonts/chinese", "/usr/share/fonts/ttf-dejavu"}, true
                                );
                                //=======================================================================================
                        }

                        Document docx = new Document(wordInStream);
                        docx.save(byteOutDocx, SaveFormat.DOC);

                        // Address是将要被转化的word文档
                        Document doc = new Document(new ByteArrayInputStream(byteOutDocx.toByteArray()));
                        doc.save(byteOut, SaveFormat.PDF);
                } catch (Exception e) {
                        log.error("word转pdf异常::{}", e);
                        throw new RuntimeException("word转pdf异常");
                }
                return byteOut.toByteArray();
        }
}
````



### 总结

在导出PDF和Word的时候 响应头的设置

````java
response.addHeader("Content-Disposition", "attachment;filename=\"" + new String(fileName.getBytes("UTF-8"), "iso-8859-1") + "\"");
            response.setCharacterEncoding("utf-8");
            if (wordRequest.getItemFileType().compareTo(1) == 0 || wordRequest.getItemFileType().compareTo(3) == 0) {
                response.setContentType("application/force-download");
                WordUtil.replacePdf(fos,inputStream,map);
            }
            if (wordRequest.getItemFileType().compareTo(0) == 0 || wordRequest.getItemFileType().compareTo(2) == 0) {
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Type", "application/vnd.ms-word");
                response.addHeader("Content-Type", "application/x-msword");
                WordUtil.replaceWord(fos,inputStream,map);
            }
````



⚠️ 在PDF导出的时候可能在本地没有问题。但是部署到服务器上可能会出现乱码的现象，造成这种现象的原因是因为服务器上缺少字体造成的，解决办法 参考我发表的 “制作jdk1.8字体库镜像.md”



## Vue 下载 

仅供参考

````js
exportPlanToWord(budgetMainId,schemeId, fileName,itemFileType,checkItemContent, fileSuffix,callback) {
    let apiType = getType(this.ApiType);
    ServiceFileDownLoadFiles({
      url: `/${apiType}/api/plan/exportPlan`,
      method: "post",
      data: qs.stringify({ budgetMainId: budgetMainId,programmeId: schemeId,itemFileType: itemFileType,checkItemContent: JSON.stringify(checkItemContent)}),
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      }
    }).then(res => {
      let fileTypeMime = '' // 文件 mime 类型，移动端必传，否则下载不成功；pc端可传可不传
      switch (fileSuffix) { // 获取后缀对应的 mime
      case 'png':
        fileTypeMime = 'image/png';
        break;
      case 'doc' || 'pdf':
        fileTypeMime = 'application/msword';
        break;
      case 'docx' || 'pdf':
        fileTypeMime = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';
        break;
      case 'jpg':
      case 'jpeg':
        fileTypeMime = 'image/jpeg';
        break;
      case 'gif':
        fileTypeMime = 'image/gif';
        break;
      case 'svg':
        fileTypeMime = 'image/svg+xml';
        break;
      case 'tif':
      case 'tiff':
        fileTypeMime = 'image/tiff';
        break;
      case 'txt':
        fileTypeMime = 'text/plain';
        break;
      case 'ppt':
        fileTypeMime = 'application/vnd.ms-powerpoint';
        break;
      case 'pptx':
        fileTypeMime = 'application/vnd.openxmlformats-officedocument.presentationml.presentation';
        break;
      case 'xls':
        fileTypeMime = 'application/vnd.ms-excel';
        break;
      case 'xlsx':
        fileTypeMime = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
        break;
      case 'zip':
        fileTypeMime = 'application/zip';
        break;
      case '7z':
        fileTypeMime = 'application/x-7z-compressed';
        break;
      }
      const myblob = new Blob([res.data]);
      if ('download' in document.createElement('a')) {
        let blob = window.URL.createObjectURL(myblob)
        let link = document.createElement('a')
        link.style.display = 'none'
        link.href = blob
        link.setAttribute('download', fileName + fileSuffix)
        document.body.appendChild(link)
        link.click()
        setTimeout(() => {
          document.body.removeChild(link) //下载完成移除元素
          window.URL.revokeObjectURL(blob) //释放掉 blob 对象
        }, 1000);
      }
      else {
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(blob, fileName + fileSuffix);
        } else {
          let URL = window.URL || window.webkitURL;
          let downloadUrl = URL.createObjectURL(blob);
          window.location = downloadUrl;
        }
      }
      // loading.close()
      callback(true)
    })
  }
````



ServiceFileDownLoadFiles：

````js
export const ServiceFileDownLoadFiles = axios.create({
  timeout: 20000, // 请求超时时间
  baseURL: ConfigBaseURL,
  method: "post",
  responseType:'blob'
})
````

