package com.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.po.Dep;
import com.po.Welfare;

@Service("WelfareDAO")
public interface IWelfareMapper {
      //��ѯ��������
	public List<Welfare> findAll();
}
