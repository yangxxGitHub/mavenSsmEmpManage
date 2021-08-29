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
		System.out.println("��ӷ���������");
		//��ȡ��վ��·��
				String realpath=request.getRealPath("/");
				System.out.println("��վ��·����"+realpath);
				/***************�ļ��ϴ���ʼ***********************/
				//��ȡ�ϴ���Ƭ����
				MultipartFile multipartFile=emp.getPic();
				if(multipartFile!=null&&!multipartFile.isEmpty()){ //isEmpty() �ж�multipartFile�����Ƿ�Ϊ��
					//��ȡ�ϴ��ļ������� getOriginalFilename()
					String fname=multipartFile.getOriginalFilename();
					//����
					if(fname.lastIndexOf(".")!=-1){
						String ext=fname.substring(fname.lastIndexOf("."));
						if(ext.equalsIgnoreCase(".jpg")){
							String newfname=new Date().getTime()+ext;
							//�����ļ�����ָ���ļ�·��
							File dostFile=new File(realpath+"/uppic/"+newfname);
							try {
								//�ϴ�(�����󴫵ݵ��ļ����ݸ���һ�ݵ��ղ����ɵ�dostFile�ļ���)
								FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostFile);
								emp.setPhoto(newfname);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				
				/***************�ļ��ϴ�����***********************/
		boolean flag=bizService.getEmpService().save(emp);
		if(flag){
		  	//��������ִ�н����json�ַ������ص�ǰ̨
		  	AjAxUtils.printString(response, "1");
		  	}else{
		  		AjAxUtils.printString(response, "0");
		  	}
		return null;
	 }
	//��ҳ��ѯ
	@RequestMapping(value="findPageAll_Emp.do")
	 public String findPageAll(HttpServletRequest request,HttpServletResponse response,Integer page,Integer rows){
		System.out.println("��ҳ��ѯ����......"+page+"/"+rows);
		//easyui�ķ�ҳ��Ҫ�Ĳ����ǹ̶��ĸ��Ӹ�ʽ
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
		//ƴ��ǰ̨easyui��ҳ��Ҫ�Ĺ̶�����
		map.put("page", page);//��ǰҳ
		map.put("rows", lsemp);//��¼����
		map.put("total", maxRows);//�ܼ�¼��
		
		   PropertyFilter propertyFilter=AjAxUtils.filterProperts("birthday","pic");
		  //����ѯ�������ת��Ϊjson�ַ���
		  	String jsonstr=JSONObject.toJSONString(map,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
		  	System.out.println("json:"+jsonstr);
		  	//��������ִ�н����json�ַ������ص�ǰ̨
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
	  //����ѯ�������ת��Ϊjson�ַ���
	  	String jsonstr=JSONObject.toJSONString(map,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
	  	System.out.println("json:"+jsonstr);
	  	//��������ִ�н����json�ַ������ص�ǰ̨
	  	AjAxUtils.printString(response, jsonstr);
	return null;
 }
              //ɾ��
@RequestMapping(value="delById_Emp.do")
public String delById(HttpServletRequest request,HttpServletResponse response,Integer eid){
	boolean flag=bizService.getEmpService().delById(eid);
	if(flag){
	  	//��������ִ�н����json�ַ������ص�ǰ̨
	  	AjAxUtils.printString(response, "1");
	  	}else{
	  		AjAxUtils.printString(response, "0");
	  	}
	return null;
}

//�޸��õĲ�ѯ����
@RequestMapping(value="findById_Emp.do")
public String findById(HttpServletRequest request,HttpServletResponse response,Integer eid){
	Emp oldemp=bizService.getEmpService().findById(eid);
	
	   PropertyFilter propertyFilter=AjAxUtils.filterProperts("birthday","pic");
	  //����ѯ�������ת��Ϊjson�ַ���
	  	String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
	  	System.out.println("json:"+jsonstr);
	  	//��������ִ�н����json�ַ������ص�ǰ̨
	  	AjAxUtils.printString(response, jsonstr);
	return null;
}
//�鿴����
@RequestMapping(value="findDatail_Emp.do")
public String findDatail(HttpServletRequest request,HttpServletResponse response,Integer eid){
	Emp oldemp=bizService.getEmpService().findById(eid);
	
	   PropertyFilter propertyFilter=AjAxUtils.filterProperts("birthday","pic");
	  //����ѯ�������ת��Ϊjson�ַ���
	  	String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
	  	System.out.println("json:"+jsonstr);
	  	//��������ִ�н����json�ַ������ص�ǰ̨
	  	AjAxUtils.printString(response, jsonstr);
	return null;
}
//�޸�
@RequestMapping(value="update_Emp.do")
public String update(HttpServletRequest request,HttpServletResponse response,Emp emp){
	//�����ϴ��ļ�
	  
	//ԭ��ͼƬ(ͨ��eid��ѯ���������ȡ�ö����ͼƬ����)
	String oldphoto=bizService.getEmpService().findById(emp.getEid()).getPhoto();
	//��վ��·��
	String realpath=request.getRealPath("/");
	/***************�ļ��ϴ���ʼ***********************/
	//��ȡ�ϴ���Ƭ����
	MultipartFile multipartFile=emp.getPic();
	if(multipartFile!=null&&!multipartFile.isEmpty()){ //isEmpty() �ж�multipartFile�����Ƿ�Ϊ��
		 //�޸���ͼƬ���ҵ�ԭ��ͼƬɾ��
		//��ȡ�ϴ��ļ������� getOriginalFilename()
		String fname=multipartFile.getOriginalFilename();
		//����
		if(fname.lastIndexOf(".")!=-1){
			String ext=fname.substring(fname.lastIndexOf("."));
			if(ext.equalsIgnoreCase(".jpg")){
				String newfname=new Date().getTime()+ext;
				//�����ļ�����ָ���ļ�·��
				File dostFile=new File(realpath+"/uppic/"+newfname);
				try {
					//�ϴ�(�����󴫵ݵ��ļ����ݸ���һ�ݵ��ղ����ɵ�dostFile�ļ���)
					FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostFile);
					emp.setPhoto(newfname);
					//ɾ��ԭ��ͼƬ
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
		 //�޸�ʱû����ͼƬ���ҵ�ԭ����ͼƬ�����´�һ��
		emp.setPhoto(oldphoto);
	}
	
	/***************�ļ��ϴ�����***********************/
	
	boolean flag=bizService.getEmpService().update(emp);
	if(flag){
	  	//��������ִ�н����json�ַ������ص�ǰ̨
	  	AjAxUtils.printString(response, "1");
	  	}else{
	  		AjAxUtils.printString(response, "0");
	  	}
	return null;
}
}
