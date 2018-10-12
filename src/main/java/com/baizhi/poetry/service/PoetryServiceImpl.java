package com.baizhi.poetry.service;

import com.baizhi.poetry.dao.PoetryDAO;
import com.baizhi.poetry.entity.Poetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PoetryServiceImpl implements PoetryService {

    @Autowired
    private PoetryDAO poetryDAO;

    @Override
    public List<Poetry> queryAll() {
        return poetryDAO.findAll();
    }
}
