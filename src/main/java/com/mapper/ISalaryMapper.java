package com.mapper;

import org.springframework.stereotype.Service;
import com.po.Emp;
import com.po.Salary;

@Service("SalaryDAO")
public interface ISalaryMapper {
//薪资添加
	public int save(Salary salary);
	//通过员工编号删除该员工薪资
	public int deByEid(Integer eid);
	//通过员工编号查询该员工薪资
	public Salary findById(Integer eid);
	//通过员工编号修改该员工薪资
	public int updateByEid(Salary salary);
}
