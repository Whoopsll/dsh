package com.nh.dsh.admin.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nh.dsh.admin.common.config.CodeConfig;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.enums.ResourceTypeEnum;
import com.nh.dsh.admin.mapper.ConfigMapper;
import com.nh.dsh.admin.mapper.ForumMapper;
import com.nh.dsh.admin.model.entity.*;
import com.nh.dsh.admin.service.BookExchangeService;
import com.nh.dsh.admin.service.BookResourceService;
import com.nh.dsh.admin.service.BookService;
import com.nh.dsh.admin.utils.MinioUtils;
import com.nh.dsh.admin.utils.WxUtils;
import com.nh.dsh.admin.wechat.cache.AccessTokenCache;
import com.nh.dsh.admin.wechat.model.entity.WxMenuModel;
import com.nh.dsh.admin.wechat.service.WechatService;
import com.nh.dsh.admin.wechat.service.WxMenuService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.print.Book;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class WechatServiceImpl implements WechatService {
    @Resource
    private BookResourceService bookResourceService;
    @Resource
    private BookExchangeService bookExchangeService;
    @Resource
    private ConfigMapper configMapper;
    @Resource
    private CodeConfig codeConfig;
    @Resource
    private BookService bookService;
    @Resource
    private ForumMapper forumMapper;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private WxMenuService wxMenuService;
    @Resource
    private AccessTokenCache accessTokenCache;
    @Resource
    private MinioUtils minioUtils;
    @Value("${wxUrl}")
    private String wxUrl;

    @Override
    public String handleMessage(HttpServletRequest request) {
        try {
            // 读取微信发送的 XML 消息体
            ServletInputStream inputStream = request.getInputStream();
            String xmlMessage = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            // TODO: 解析并处理 XML 消息内容（例如回复文本消息）
            Map message = parseMessage(xmlMessage);
            String event = message.get("event").toString();
            String content = "";
            boolean isBook = false; //默认公众号获取
            int bookId = 0; //书籍id
            int configId = 0; // 书籍的公众号配置id
            int forumId = 0; //论坛id
            // 处理公众号的点击事件
            if(event.equalsIgnoreCase("CLICK")) {
                String eventKey = message.get("eventKey").toString(); //获取wxMenu的key
                List<WxMenuModel> menus = wxMenuService.getBaseMapper().selectList(new QueryWrapper<WxMenuModel>().eq("key_id", eventKey));
                if(!menus.isEmpty()) {
                    //取出第一个
                    WxMenuModel wxMenuModel = menus.get(0);
                    message.put("replyType", wxMenuModel.getReplyType());
                    message.put("replyContent", wxMenuModel.getReplyContent());
                    message.put("mediaId", wxMenuModel.getMediaId());
                    //构建回复内容
                    return buildClickEventResponse(message);
                } else {
                    message.put("replyType", 0);
                    message.put("mediaId", "");
                    message.put("replyContent", "功能暂未开放");
                    return buildClickEventResponse(message);
                }
            }

            if(event.equalsIgnoreCase("SCAN") || event.equalsIgnoreCase("subscribe")) {
                // 已关注公众号，扫码进入的情况
                String eventKey = message.get("eventKey").toString();
                if(!eventKey.isEmpty()) {
                    // 判断key是否为第三方，若为第三方，采用第三方返回方式来
                    if(eventKey.contains("VG@")) {
                        //东南大出版社印出去的书籍
                        String[] bookInfos = eventKey.split("@");
                        //获取第三方书籍id
                        String otherId = bookInfos[1];
                        BookEntity entity = bookService.getOne(new QueryWrapper<BookEntity>().eq("other_id", otherId));
                        List<BookResourceEntity> resources = bookResourceService.getBaseMapper().selectList(new QueryWrapper<BookResourceEntity>().eq("delete_flag", 0).eq("other_book_id", entity.getOtherId()).eq("type", 2));// 获取这本书的总资源
                        if (!resources.isEmpty()) {
                            // 配置了总资源入口
                            content = String.format(wxUrl +"/#/pages/book/resource?resourceId=%s&secret=%s", resources.get(0).getId(), codeConfig.getSecret());
                        } else {
                            // 未配置总资源入口
                            content = "";
                        }
                        bookId = entity.getId();
                        isBook = true;
                        message.put("bookName", entity.getBookName());
                    } else {
                        //正常系统中的书籍
                        //根据资源类型返回对应的资源链接,数组0为类型，1为资源id。exchange-兑换，resource-资源
                        String[] params = eventKey.split("_");
                        String type = event.equalsIgnoreCase("SCAN") ? params[0] : params[1];
                        if (type.equals(ResourceTypeEnum.RESOURCE.getValue())) {
                            // 资源码
                            BookResourceEntity entity = bookResourceService.getById(Integer.parseInt(event.equalsIgnoreCase("SCAN") ? params[1] : params[2]));
                            if (!entity.getLink().isEmpty() && entity.getValidStatus() == 0) {
                                content = String.format(wxUrl +"/#/pages/book/resource?resourceId=%s&secret=%s", entity.getId(), codeConfig.getSecret());
                            }
                            bookId = entity.getBookId();
                        } else if(type.equals(ResourceTypeEnum.EXCHANGE.getValue())) {
                            // 兑换码
                            BookExchangeEntity entity = bookExchangeService.getById(Integer.parseInt(event.equalsIgnoreCase("SCAN") ? params[1] : params[2]));
                            content = bookExchangeService.generateQrCodeLink(entity);
                            bookId = entity.getBookId();
                        } else if (type.equals(ResourceTypeEnum.BOOK.getValue())) {
                            BookEntity entity = bookService.getById(Integer.parseInt(event.equalsIgnoreCase("SCAN") ? params[1]: params[2]));
                            List<BookResourceEntity> resources = bookResourceService.getBaseMapper().selectList(new QueryWrapper<BookResourceEntity>().eq("delete_flag", 0).eq("book_id", entity.getId()).eq("type", 2));// 获取这本书的总资源
                            if (!resources.isEmpty()) {
                                // 配置了总资源入口
                                // content = String.format("http://demo.dianhuiyun.com.cn/dsh-client-h5/#/pages/book/resource?resourceId=%s&secret=%s", resources.get(0).getId(), codeConfig.getSecret());
                                content = String.format(wxUrl +"/#/pages/book/resource?resourceId=%s&secret=%s", resources.get(0).getId(), codeConfig.getSecret());
                            } else {
                                // 未配置总资源入口
                                content = "";
                            }
                            bookId = entity.getId();
                            isBook = true;
                            message.put("bookName", entity.getBookName());
                        }
                    }
                }
            }
            // 获取书籍内的公众号配置id
            if(configId == 0) {
                BookEntity bookEntity = bookService.getById(bookId);
                configId = bookEntity.getWxConfigId();
                message.put("bookName", bookEntity.getBookName());
            }
            // 获取公众号配置中的资源获取方式
            ConfigEntity config = configMapper.selectById(configId);
            // 获取论坛id
            List<ForumEntity> forums = forumMapper.selectList(new QueryWrapper<ForumEntity>().eq("delete_flag", 0).eq("book_id", bookId));
            if(!forums.isEmpty()) {
                forumId = forums.get(0).getId();
            }
            // 构建消息体
            message.put("isBook", isBook);
            message.put("forumId", forumId);
            message.put("resourceGetType", config == null ? 0 : config.getResourceGetType());
            String s = buildTextResponse(message, content);
            System.out.println(s);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return "服务异常";
        }
    }


    @Override
    public Result uploadImage(int configId, MultipartFile file) {
        try {
            Map result = new HashMap();
            ConfigEntity entity = configMapper.selectById(configId);
            //上传到图片素材库
            CompletableFuture<String> assetImageFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    String assetInfo = WxUtils.uploadImageAssets(entity.getAccount(), entity.getPassword(), entity.getId(), file);
                    JSONObject json = JSON.parseObject(assetInfo);
                    return json.getString("media_id");
                } catch (Exception e) {
                    throw new ServerException("文件上传异常");
                }
            });
            //上传到oss文件
            CompletableFuture<String> ossFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    // 图片上传到我们自己的服务器，用于在前端展示
                    Map<String, Object> fileResult = minioUtils.uploadFile("dsh", new MultipartFile[]{file});
                    JSONArray files = new JSONObject(fileResult).getJSONArray("urlList"); //获取files
                    if(files != null && files.size() >0) {
                        return files.get(0).toString();//取出图片链接
                    }
                    return null;
                } catch (Exception e) {
                    throw new ServerException("文件上传异常");
                }
            });
           //等待俩个任务完成
            CompletableFuture.allOf(assetImageFuture, ossFuture).join();
            result.put("mediaId", assetImageFuture.get());
            result.put("url", ossFuture.get());
            return Result.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerException("上传异常");
        }
    }

    /**
     * 构建点击事件回复
     * @param messageMap 存放用户id
     * @return
     */
    private String buildClickEventResponse(Map<String, Object> messageMap) {
        int replyType = Integer.parseInt(messageMap.get("replyType").toString());
        String replyContent = messageMap.get("replyContent").toString();
        String toUserName = messageMap.get("FromUserName").toString(); // 用户的 OpenID
        String fromUserName = messageMap.get("ToUserName").toString(); // 公众号ID
        String mediaId = messageMap.get("mediaId").toString(); // 素材ID
        String result = "";
        if (replyType == 0) {
            //回复类型0:文本，1:图片
            result = "<xml>" +
                    "<ToUserName><![CDATA[" + toUserName + "]]></ToUserName>" +
                    "<FromUserName><![CDATA[" + fromUserName + "]]></FromUserName>" +
                    "<CreateTime>" + System.currentTimeMillis() / 1000  + "</CreateTime>" +
                    "<MsgType><![CDATA[text]]></MsgType>" +
                    "<Content><![CDATA["+replyContent+"]]></Content>" +
                    "</xml>";
        } else {
            result = "<xml>" +
                    "<ToUserName><![CDATA[" + toUserName + "]]></ToUserName>" +
                    "<FromUserName><![CDATA[" + fromUserName + "]]></FromUserName>" +
                    "<CreateTime>" + System.currentTimeMillis() / 1000  + "</CreateTime>" +
                    "  <MsgType><![CDATA[image]]></MsgType>" +
                    "  <Image>" +
                    "    <MediaId><![CDATA["+ mediaId+"]]></MediaId>" +
                    "  </Image>" +
                    "</xml>";
        }

        return result;
    }

    /**
     * 构造资源相关消息回复
     * @param messageMap 存放用户id、公众号信息、书籍id、名称、资源获取类型
     * @param replyContent 回复内容
     * @return
     */
    private String buildTextResponse(Map<String, Object> messageMap, String replyContent) {
        String blankPic = "https://my-egg.oss-cn-beijing.aliyuncs.com/blank.png";
        String toUserName = messageMap.get("FromUserName").toString(); // 用户的 OpenID
        String fromUserName = messageMap.get("ToUserName").toString(); // 公众号ID
        int count = 5;
        int resourceGetType =Integer.parseInt(messageMap.get("resourceGetType").toString());
        boolean isBook = Boolean.parseBoolean(messageMap.get("isBook").toString());
        int forumId = Integer.parseInt(messageMap.get("forumId").toString());
        System.out.println("参数是====");
        System.out.println(messageMap);
        String bookName = messageMap.get("bookName").toString();
        String forumUrl = wxUrl + "/?code=041SHMll2124He4cL6ll2rzv2n3SHMl0&state=STATE#/pages/forum/forum?id=" + forumId + "&type=1";
        if (resourceGetType == 1 && !isBook) {
            count = 4;
            forumUrl += "&redirectUrl=" + replyContent;
        }
        String firstContent = "<xml>" +
                "<ToUserName><![CDATA[" + toUserName + "]]></ToUserName>" +
                "<FromUserName><![CDATA[" + fromUserName + "]]></FromUserName>" +
                "<CreateTime>" + System.currentTimeMillis() / 1000  + "</CreateTime>" +
                "<MsgType><![CDATA[news]]></MsgType>" +
                "<ArticleCount>"+count+"</ArticleCount>" +
                "<Articles>" +
                "  <item>" +
                "    <Title><![CDATA[《"+bookName+"》私域入口]]></Title>" +
                "    <Description><![CDATA[点击进去私域]]></Description>" +
                "    <PicUrl><![CDATA[https://my-egg.oss-cn-beijing.aliyuncs.com/cover.jpg]]></PicUrl>" +
                "    <Url><![CDATA[" + forumUrl + "]]></Url>" +
                "  </item>";
        String resourceContent = "  <item>" +
                "    <Title><![CDATA[《"+bookName+"》书籍资源]]></Title>" +
                "    <Description><![CDATA[点击这里获取您想要的资源信息]]></Description>" +
                "    <PicUrl><![CDATA["+ blankPic + "]]></PicUrl>" +
                "    <Url><![CDATA["+replyContent+"]]></Url>" +
                "  </item>" ;
        String endContent =
                "  <item>" +
                        "    <Title><![CDATA[东南大学出版官方抖音]]></Title>" +
                        "    <Description><![CDATA[点击进入官方抖音]]></Description>" +
                        "    <PicUrl><![CDATA["+ blankPic + "]]></PicUrl>" +
                        "    <Url><![CDATA[http://demo.dianhuiyun.com.cn/OtherSite.html?param=点击按钮复制此消息，打开抖音，来关注我吧。 https://v.douyin.com/iUUrKnmo/ 5@7.com :9pm]]></Url>" +
                        "  </item>" +
                "  <item>" +
                "    <Title><![CDATA[东南大学出版社官方微店]]></Title>" +
                "    <Description><![CDATA[点击这里进入官方微店]]></Description>" +
                "    <PicUrl><![CDATA["+ blankPic + "]]></PicUrl>" +
                "    <Url><![CDATA[https://shop90721766.m.youzan.com/wscshop/showcase/homepage?kdt_id=90529598&spm=g.1782349539]]></Url>" +
                "  </item>" +
                "  <item>" +
                "    <Title><![CDATA[东南大学出版社官网]]></Title>" +
                "    <Description><![CDATA[点击这里获取官方信息]]></Description>" +
                "    <PicUrl><![CDATA[https://my-egg.oss-cn-beijing.aliyuncs.com/logo.png]]></PicUrl>" +
                "    <Url><![CDATA[http://www.seupress.com]]></Url>" +
                "  </item>" +
                "</Articles>" +
                "</xml>";
        String result = firstContent;
        System.out.println("replyContent是否为空==" + replyContent.toString());
        if ((resourceGetType == 0 || isBook) && !replyContent.isEmpty()) {
            //公众号获取
            result += resourceContent;
        }
        return result + endContent;
    }


    private Map parseMessage(String xml) throws Exception {
        Map result = new HashMap();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        // 获取 Event 值
        String event = document.getElementsByTagName("Event").item(0).getTextContent();
        // 获取 EventKey 值
        String eventKey = document.getElementsByTagName("EventKey").item(0).getTextContent();
        // 获取 ToUserName 和 FromUserName 的值
        String toUserName = document.getElementsByTagName("ToUserName").item(0).getTextContent();
        String fromUserName = document.getElementsByTagName("FromUserName").item(0).getTextContent();
        result.put("eventKey", eventKey);
        result.put("event", event);
        result.put("ToUserName", toUserName);
        result.put("FromUserName", fromUserName);
        // 获取 MsgType 节点
        String msgType = document.getElementsByTagName("MsgType").item(0).getTextContent();
        if ("text".equals(msgType)) {
            String content = document.getElementsByTagName("Content").item(0).getTextContent();
            result.put("content", content);
            return result;
        }
        return result;
    }
}
