package cn.e3mall.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

/**
 * 图片上传
 * @author 罗小黑
 *
 */
@Controller
public class PictureController {
	
	//注入配置文件中的图片路径
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	/**
	 * 指定相应结果为Content-Type:text/plan;charset=UTF-8格式，为了让浏览器都能世界json，防止兼容问题
	 * @param uploadFile
	 * @return
	 */
	@RequestMapping(value = "/pic/upload",produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
	@ResponseBody
	public String fileUpLoad(MultipartFile uploadFile){
		
		try {
			//1,创建上传图片工具类
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			//2,将文件转换成字节数组并且提取文件类型
			byte[] fileBytes = uploadFile.getBytes();
			String fileLastName = uploadFile.getOriginalFilename();
			String extName = fileLastName.substring(fileLastName.lastIndexOf(".") + 1);
			//3，执行上传方法
			String url = fastDFSClient.uploadFile(fileBytes, extName);
			//4，拼接上传图片地址
			String imageURL = IMAGE_SERVER_URL + url;
			//5，将地址转通过工具类换成KindEditor上传文件返回的json格式返回
			Map map = new HashMap<>();
			map.put("error", 0);
			map.put("url", imageURL);
			String json = JsonUtils.objectToJson(map);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			//6,上传失败。提示错误json信息
			Map map = new HashMap<>();
			map.put("error", 1);
			map.put("message", "图片上传失败");
			String json = JsonUtils.objectToJson(map);
			return json;
		}
	}
}
