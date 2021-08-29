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
		//Ա�����
		int code=daoservice.getEmpMapper().save(emp);
		//����Ա��н�ʺ͸�������ӣ������ȡ��Ա�����id���
		if(code>0){
			Integer eid=daoservice.getEmpMapper().findMaxEid();
			/********н�ʱ��濪ʼ********/
			Salary sa=new Salary(eid,emp.getEmoney());
			daoservice.getSalaryMapper().save(sa);
			/********н�ʱ������********/
			/*********Ա���������濪ʼ************/
			//��ȡԱ�������ı������
			String[] wfs=emp.getWids();
			if(wfs!=null&&wfs.length>0){
				for(int i=0;i<wfs.length;i++){
					EmpWelfare ewf=new EmpWelfare(eid,new Integer(wfs[i]));//Integer.parseInt(wfs[i]);
					daoservice.getEmpWelfareMapper().save(ewf);
				}
			}
			/*********Ա�������������************/
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
		//Ҫɾ������ɾ�ӱ�
		   //н��
		daoservice.getSalaryMapper().deByEid(eid);
		//Ա������
		daoservice.getEmpWelfareMapper().deByEid(eid);
		//ɾ��Ա��
		int code=daoservice.getEmpMapper().delById(eid);
		if(code>0){
			return true;
		}
		return false;
	}
	@Override
	public Emp findById(Integer eid) {
		//��ȡԱ������
		Emp oldemp=daoservice.getEmpMapper().findById(eid);
		/**��ȡн��**/
		Salary sa=daoservice.getSalaryMapper().findById(eid);
		if(sa!=null&&sa.getEmoney()!=null){
			oldemp.setEmoney(sa.getEmoney());
		}
		/********/
		/****��ȡ����****/
		List<Welfare> lswf=daoservice.getEmpWelfareMapper().findByEid(eid);
		//������id
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
		//�޸�Ա����Ϣ
		int code=daoservice.getEmpMapper().update(emp);
		if(code>0){
			//����н��
			  //��ȡԭ��н��
			Salary oldsa=daoservice.getSalaryMapper().findById(emp.getEid());
			if(oldsa!=null&&oldsa.getEmoney()!=null){
				//�޸�
				oldsa.setEmoney(emp.getEmoney());
				daoservice.getSalaryMapper().updateByEid(oldsa);
			}else{
				/********н�ʱ��濪ʼ********/
				Salary sa=new Salary(emp.getEid(),emp.getEmoney());
				daoservice.getSalaryMapper().save(sa);		
			}
			/********���¸���*****/
			//��ȡԭ���ĸ���
			List<Welfare> lswf=daoservice.getEmpWelfareMapper().findByEid(emp.getEid());
			if(lswf!=null&&lswf.size()>0){
				//ɾ��ԭ�и���
				daoservice.getEmpWelfareMapper().deByEid(emp.getEid());
			}
			/*********Ա���������濪ʼ************/
			//��ȡԱ�������ı������
			String[] wfs=emp.getWids();
			if(wfs!=null&&wfs.length>0){
				for(int i=0;i<wfs.length;i++){
					EmpWelfare ewf=new EmpWelfare(emp.getEid(),new Integer(wfs[i]));//Integer.parseInt(wfs[i]);
					daoservice.getEmpWelfareMapper().save(ewf);
				}
			}
			/*********Ա�������������************/
			return true;
		}
		return false;
	}

}
