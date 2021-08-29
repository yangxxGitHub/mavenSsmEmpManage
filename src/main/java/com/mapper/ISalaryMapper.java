package com.mapper;

import org.springframework.stereotype.Service;
import com.po.Emp;
import com.po.Salary;

@Service("SalaryDAO")
public interface ISalaryMapper {
//н�����
	public int save(Salary salary);
	//ͨ��Ա�����ɾ����Ա��н��
	public int deByEid(Integer eid);
	//ͨ��Ա����Ų�ѯ��Ա��н��
	public Salary findById(Integer eid);
	//ͨ��Ա������޸ĸ�Ա��н��
	public int updateByEid(Salary salary);
}
