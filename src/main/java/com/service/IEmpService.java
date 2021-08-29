package com.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.po.Emp;
import com.po.PageBean;

public interface IEmpService {
	//���
  public boolean save(Emp emp);
  
    //��ҳ��ѯ
	public List<Emp> findPageAll(PageBean pb);
	//�ܼ�¼��
	public Integer findMaxRows();
	//ɾ��
	public boolean delById(Integer eid);
	//��ѯ����
	public Emp findById(Integer eid);
	//�޸�
	public boolean update(Emp emp);
}
