package com.mapper;

import java.util.List;

import org.springframework.stereotype.Service;
import com.po.Emp;
import com.po.EmpWelfare;
import com.po.Welfare;

@Service("EmpWelfareDAO")
public interface IEmpWelfareMapper {
//Ա���������
	public int save(EmpWelfare empWelfare);
	//ͨ��Ա�����ɾ����Ա������
	public int deByEid(Integer eid);
	//ͨ��Ա����Ų�ѯ��Ա������
	public List<Welfare> findByEid(Integer eid);
}
