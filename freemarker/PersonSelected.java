package com.zhuxj.maven_1.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.zhuxj.maven_1.dao.PersonDao;
import com.zhuxj.maven_1.entity.Person;
import com.zhuxj.maven_1.service.PersonService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateNumberModel;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

public class PersonSelected implements TemplateDirectiveModel {

	private PersonService service;
	
	public PersonSelected(PersonService service){
		this.service = service;
	}
	
	public void execute(Environment arg0, Map arg1, TemplateModel[] arg2, TemplateDirectiveBody arg3)
			throws TemplateException, IOException {
		// TODO Auto-generated method stub
		Object paramValue = arg1.get("personId");
		int id = 0;
		if(paramValue instanceof TemplateNumberModel){
			id = ((TemplateNumberModel)paramValue).getAsNumber().intValue();
		}
		Person person = service.getPerson(id);
		/*Person person = new Person();
		person.setId(1);
		person.setName("asd");*/
		System.out.println(person.getName());
		arg0.setVariable("person", DEFAULT_WRAPPER.wrap(person));
		arg3.render(arg0.getOut());
	}

}
