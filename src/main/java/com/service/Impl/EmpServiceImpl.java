package com.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.po.Emp;
import com.po.EmpWelfare;
import com.po.PageBean;
import com.po.Salary;
import com.po.Welfare;
import com.service.IEmpService;
import com.util.DaoServiceUtil;

@Service("EmpBiz")
@Transactional
public class EmpServiceImpl implements IEmpService {
	   @Resource(name="DaoService")
		private DaoServiceUtil daoservice;
		public DaoServiceUtil getDaoservice() {
			return daoservice;
		}
		public void setDaoservice(DaoServiceUtil daoservice) {
			this.daoservice = daoservice;
		}

	@Override
	public boolean save(Emp emp) {
		//员工添加
		int code=daoservice.getEmpMapper().save(emp);
		//处理员工薪资和福利的添加，必须获取该员工点的id编号
		if(code>0){
			Integer eid=daoservice.getEmpMapper().findMaxEid();
			/********薪资保存开始********/
			Salary sa=new Salary(eid,emp.getEmoney());
			daoservice.getSalaryMapper().save(sa);
			/********薪资保存结束********/
			/*********员工福利保存开始************/
			//获取员工福利的编号数组
			String[] wfs=emp.getWids();
			if(wfs!=null&&wfs.length>0){
				for(int i=0;i<wfs.length;i++){
					EmpWelfare ewf=new EmpWelfare(eid,new Integer(wfs[i]));//Integer.parseInt(wfs[i]);
					daoservice.getEmpWelfareMapper().save(ewf);
				}
			}
			/*********员工福利保存结束************/
			return true;
		}
		return false;
	}
	@Override
	public List<Emp> findPageAll(PageBean pb) {
		System.out.println("3333333333");
		return daoservice.getEmpMapper().findPageAll(pb.getPage(),pb.getRows());
	}
	@Override
	public Integer findMaxRows() {
		// TODO Auto-generated method stub
		return daoservice.getEmpMapper().findMaxRows();
	}
	@Override
	public boolean delById(Integer eid) {
		//要删主表先删从表
		   //薪资
		daoservice.getSalaryMapper().deByEid(eid);
		//员工福利
		daoservice.getEmpWelfareMapper().deByEid(eid);
		//删除员工
		int code=daoservice.getEmpMapper().delById(eid);
		if(code>0){
			return true;
		}
		return false;
	}
	@Override
	public Emp findById(Integer eid) {
		//获取员工对象
		Emp oldemp=daoservice.getEmpMapper().findById(eid);
		/**获取薪资**/
		Salary sa=daoservice.getSalaryMapper().findById(eid);
		if(sa!=null&&sa.getEmoney()!=null){
			oldemp.setEmoney(sa.getEmoney());
		}
		/********/
		/****获取福利****/
		List<Welfare> lswf=daoservice.getEmpWelfareMapper().findByEid(eid);
		//处理福利id
		String[] wids=new String[lswf.size()];
		for(int i=0;i<wids.length;i++){
			Welfare wf=lswf.get(i);
			wids[i]=wf.getWid().toString();
		}
		oldemp.setWids(wids);
		oldemp.setLswf(lswf);
		return oldemp;
	}
	@Override
	public boolean update(Emp emp) {
		//修改员工信息
		int code=daoservice.getEmpMapper().update(emp);
		if(code>0){
			//更新薪资
			  //获取原来薪资
			Salary oldsa=daoservice.getSalaryMapper().findById(emp.getEid());
			if(oldsa!=null&&oldsa.getEmoney()!=null){
				//修改
				oldsa.setEmoney(emp.getEmoney());
				daoservice.getSalaryMapper().updateByEid(oldsa);
			}else{
				/********薪资保存开始********/
				Salary sa=new Salary(emp.getEid(),emp.getEmoney());
				daoservice.getSalaryMapper().save(sa);		
			}
			/********更新福利*****/
			//获取原来的福利
			List<Welfare> lswf=daoservice.getEmpWelfareMapper().findByEid(emp.getEid());
			if(lswf!=null&&lswf.size()>0){
				//删除原有福利
				daoservice.getEmpWelfareMapper().deByEid(emp.getEid());
			}
			/*********员工福利保存开始************/
			//获取员工福利的编号数组
			String[] wfs=emp.getWids();
			if(wfs!=null&&wfs.length>0){
				for(int i=0;i<wfs.length;i++){
					EmpWelfare ewf=new EmpWelfare(emp.getEid(),new Integer(wfs[i]));//Integer.parseInt(wfs[i]);
					daoservice.getEmpWelfareMapper().save(ewf);
				}
			}
			/*********员工福利保存结束************/
			return true;
		}
		return false;
	}

}
