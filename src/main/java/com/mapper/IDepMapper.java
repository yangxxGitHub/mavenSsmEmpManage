package com.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.po.Dep;

@Service("DepDAO")
public interface IDepMapper {
 //��ѯ��������
	public List<Dep> findAll();
}
