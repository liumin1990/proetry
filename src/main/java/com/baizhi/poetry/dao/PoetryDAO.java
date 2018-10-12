package com.baizhi.poetry.dao;

import com.baizhi.poetry.entity.Poetry;

import java.util.List;

public interface PoetryDAO {


    public List<Poetry> findAll();
}
