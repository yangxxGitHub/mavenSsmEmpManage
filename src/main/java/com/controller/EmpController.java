package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.po.Dep;
import com.po.Emp;
import com.po.PageBean;
import com.po.Welfare;
import com.util.AjAxUtils;
import com.util.BizServiceUtil;



@Controller
public class EmpController {
	@Resource(name="BizService")
    private BizServiceUtil bizService;

	public BizServiceUtil getBizService() {
		return bizService;
	}

	public void setBizService(BizServiceUtil bizService) {
		this.bizService = bizService;
	}
	
	@RequestMapping(value="save_Emp.do")
	 public String save(HttpServletRequest request,HttpServletResponse response,Emp emp){
		System.out.println("添加方法。。。");
		//获取网站根路径
				String realpath=request.getRealPath("/");
				System.out.println("网站根路径："+realpath);
				/***************文件上传开始***********************/
				//获取上传照片对象
				MultipartFile multipartFile=emp.getPic();
				if(multipartFile!=null&&!multipartFile.isEmpty()){ //isEmpty() 判断multipartFile对象是否为空
					//获取上传文件的名称 getOriginalFilename()
					String fname=multipartFile.getOriginalFilename();
					//改名
					if(fname.lastIndexOf(".")!=-1){
						String ext=fname.substring(fname.lastIndexOf("."));
						if(ext.equalsIgnoreCase(".jpg")){
							String newfname=new Date().getTime()+ext;
							//创建文件对象，指定文件路径
							File dostFile=new File(realpath+"/uppic/"+newfname);
							try {
								//上传(将请求传递的文件内容复制一份到刚才生成的dostFile文件中)
								FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostFile);
								emp.setPhoto(newfname);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				
				/***************文件上传结束***********************/
		boolean flag=bizService.getEmpService().save(emp);
		if(flag){
		  	//将本方法执行结果的json字符串返回到前台
		  	AjAxUtils.printString(response, "1");
		  	}else{
		  		AjAxUtils.printString(response, "0");
		  	}
		return null;
	 }
	//分页查询
	@RequestMapping(value="findPageAll_Emp.do")
	 public String findPageAll(HttpServletRequest request,HttpServletResponse response,Integer page,Integer rows){
		System.out.println("分页查询方法......"+page+"/"+rows);
		//easyui的分页需要的参数是固定的复杂格式
		Map<String,Object> map=new HashMap<>();
		PageBean pb=new PageBean();
		page=page==null||page<1?pb.getPage():page;
		rows=rows==null||rows<1?pb.getRows():rows;
		if(rows>20)rows=20;
		pb.setPage(page);
		pb.setRows(rows);
		System.out.println("111111111111111");
		List<Emp> lsemp=bizService.getEmpService().findPageAll(pb);
		System.out.println("222222222222222");
		int maxRows=bizService.getEmpService().findMaxRows();
		//拼接前台easyui分页需要的固定数据
		map.put("page", page);//当前页
		map.put("rows", lsemp);//记录集合
		map.put("total", maxRows);//总记录数
		
		   PropertyFilter propertyFilter=AjAxUtils.filterProperts("birthday","pic");
		  //将查询结果集合转化为json字符串
		  	String jsonstr=JSONObject.toJSONString(map,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
		  	System.out.println("json:"+jsonstr);
		  	//将本方法执行结果的json字符串返回到前台
		  	AjAxUtils.printString(response, jsonstr);
		return null;
	 }
	
@RequestMapping(value="doinit_Emp.do")
 public String doinit(HttpServletRequest request,HttpServletResponse response){
	Map<String, Object> map=new HashMap<String,Object>();
	List<Dep> lsdep=bizService.getDepService().findAll();
	List<Welfare> lswf=bizService.getWelfareService().findAll();
	map.put("lsdep", lsdep);
	map.put("lswf", lswf);
	   PropertyFilter propertyFilter=AjAxUtils.filterProperts("birthday","pic");
	  //将查询结果集合转化为json字符串
	  	String jsonstr=JSONObject.toJSONString(map,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
	  	System.out.println("json:"+jsonstr);
	  	//将本方法执行结果的json字符串返回到前台
	  	AjAxUtils.printString(response, jsonstr);
	return null;
 }
              //删除
@RequestMapping(value="delById_Emp.do")
public String delById(HttpServletRequest request,HttpServletResponse response,Integer eid){
	boolean flag=bizService.getEmpService().delById(eid);
	if(flag){
	  	//将本方法执行结果的json字符串返回到前台
	  	AjAxUtils.printString(response, "1");
	  	}else{
	  		AjAxUtils.printString(response, "0");
	  	}
	return null;
}

//修改用的查询单个
@RequestMapping(value="findById_Emp.do")
public String findById(HttpServletRequest request,HttpServletResponse response,Integer eid){
	Emp oldemp=bizService.getEmpService().findById(eid);
	
	   PropertyFilter propertyFilter=AjAxUtils.filterProperts("birthday","pic");
	  //将查询结果集合转化为json字符串
	  	String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
	  	System.out.println("json:"+jsonstr);
	  	//将本方法执行结果的json字符串返回到前台
	  	AjAxUtils.printString(response, jsonstr);
	return null;
}
//查看详情
@RequestMapping(value="findDatail_Emp.do")
public String findDatail(HttpServletRequest request,HttpServletResponse response,Integer eid){
	Emp oldemp=bizService.getEmpService().findById(eid);
	
	   PropertyFilter propertyFilter=AjAxUtils.filterProperts("birthday","pic");
	  //将查询结果集合转化为json字符串
	  	String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
	  	System.out.println("json:"+jsonstr);
	  	//将本方法执行结果的json字符串返回到前台
	  	AjAxUtils.printString(response, jsonstr);
	return null;
}
//修改
@RequestMapping(value="update_Emp.do")
public String update(HttpServletRequest request,HttpServletResponse response,Emp emp){
	//处理上传文件
	  
	//原来图片(通过eid查询单个对象获取该对象的图片名称)
	String oldphoto=bizService.getEmpService().findById(emp.getEid()).getPhoto();
	//网站根路径
	String realpath=request.getRealPath("/");
	/***************文件上传开始***********************/
	//获取上传照片对象
	MultipartFile multipartFile=emp.getPic();
	if(multipartFile!=null&&!multipartFile.isEmpty()){ //isEmpty() 判断multipartFile对象是否为空
		 //修改有图片，找到原来图片删除
		//获取上传文件的名称 getOriginalFilename()
		String fname=multipartFile.getOriginalFilename();
		//改名
		if(fname.lastIndexOf(".")!=-1){
			String ext=fname.substring(fname.lastIndexOf("."));
			if(ext.equalsIgnoreCase(".jpg")){
				String newfname=new Date().getTime()+ext;
				//创建文件对象，指定文件路径
				File dostFile=new File(realpath+"/uppic/"+newfname);
				try {
					//上传(将请求传递的文件内容复制一份到刚才生成的dostFile文件中)
					FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostFile);
					emp.setPhoto(newfname);
					//删除原有图片
					File oldfile=new File(realpath+"/uppic/"+oldphoto);
					if(oldfile!=null&&!oldphoto.equals("default.jpg")){
						oldfile.delete();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}else{
		 //修改时没有新图片，找到原来的图片，重新存一遍
		emp.setPhoto(oldphoto);
	}
	
	/***************文件上传结束***********************/
	
	boolean flag=bizService.getEmpService().update(emp);
	if(flag){
	  	//将本方法执行结果的json字符串返回到前台
	  	AjAxUtils.printString(response, "1");
	  	}else{
	  		AjAxUtils.printString(response, "0");
	  	}
	return null;
}
}
