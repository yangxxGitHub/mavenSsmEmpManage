package com.po;

import java.io.Serializable;

public class Salary implements Serializable {//н�ʱ�
	private Integer sid;//н�ʱ��
	private Integer eid;//н�ʶ�Ӧ��Ա�����
	private Float emoney;//н��
	public Salary() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Salary(Integer sid, Integer eid, Float emoney) {
		super();
		this.sid = sid;
		this.eid = eid;
		this.emoney = emoney;
	}
	
	//���н��ר�õ��вι���
	
	public Salary(Integer eid, Float emoney) {
		super();
		this.eid = eid;
		this.emoney = emoney;
	}
	public Integer getSid() {
		return sid;
	}
	
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Integer getEid() {
		return eid;
	}
	public void setEid(Integer eid) {
		this.eid = eid;
	}
	public Float getEmoney() {
		return emoney;
	}
	public void setEmoney(Float emoney) {
		this.emoney = emoney;
	}
	@Override
	public String toString() {
		return "Salary [sid=" + sid + ", eid=" + eid + ", emoney=" + emoney + "]";
	}
	

}
