package com.zhuxj.maven_1.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.zhuxj.maven_1.entity.Person;
import com.zhuxj.maven_1.service.PersonService;
@Controller
@RequestMapping("/person")
public class PersonController {
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private PersonService service;
	
	@ModelAttribute("person2")
	public void getEmployee(@RequestParam(value="id",required=false) Integer id,
			Map<String,Object> map){
		if(id!=null){
			map.put("person", service.getPerson(id));
		}
	}
	
	@RequestMapping("/editing")
	public String editEmployee(@ModelAttribute Person person2, BindingResult result){
		if(result.getAllErrors().size()>0){
			for(FieldError error : result.getFieldErrors()){
				System.out.println(error.getField()+" : "+error.getCode());
			}
		}
		service.editEmployee(person2);
		return "redirect:/person/personList";
	}
	
	
	@RequestMapping("/edit/{id}")
	public ModelAndView toEditEmployee(@PathVariable("id") int id,
			Map<String, Object> map){
		ModelAndView view = new ModelAndView("edit");
		map.put("employee", service.getPerson(id));
		map.put("departments", service.getDepartments());
		return view;
		
	}
	
	@RequestMapping("/personList")
	public ModelAndView index(){
		ModelAndView view = new ModelAndView("personList");
		List<Person> employees = service.getEmployees();
		view.addObject("employees", employees);
		logger.log(Level.INFO, "PERSON " +employees.get(0).toString());
		return view;
	}
	/*@RequestMapping("/clickDept")
	@ResponseBody
	public void getDepts(HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException{
		List<Department> departments = service.getDepartments();
		logger.log(Level.INFO, "a : "+departments.size());
		String result = null;
		ObjectMapper mapper = new ObjectMapper();
		result = mapper.writeValueAsString(departments);
		response.getWriter().print(result);
		logger.log(Level.INFO, "s : "+result);
	}*/
	
	@RequestMapping("/testFlt")
	public String testIndex(Model model){
		List<String> strings = new ArrayList<String>();
		strings.add("asd1");
		strings.add("asd2");
		strings.add("asd3");
		strings.add("asd4");
		for(String s : strings){
			System.out.println(s);
		}
		Map<String, Object> weaponMap = new HashMap<String, Object>();
		weaponMap.put("first", "轩辕剑1");
		weaponMap.put("second", "崆峒印");
		weaponMap.put("third", "女娲石");
		weaponMap.put("fourth", "神农鼎");
		weaponMap.put("fifth", null);
		model.addAttribute("test", "Hello World.");
		model.addAttribute("strings", strings);
		model.addAttribute("weaponMap", weaponMap);
		model.addAttribute("person_Id", new PersonSelected(service));
		return "test";
	}
	public static void main(String[] args){
		PersonController c =new PersonController();
		Set d = new HashSet();
		d.add("a");
		d.add("a");
		c.service.getPerson(1);
		System.out.println(d.size());
	}
}
