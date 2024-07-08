package com.ideas2it.cms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ideas2it.cms.customexception.GradeDatabaseException;
import com.ideas2it.cms.customexception.HibernateDbConnectionException;
import com.ideas2it.cms.model.Grade;
import com.ideas2it.cms.service.GradeService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/cms/api/v1/grades")
public class GradeController {
}