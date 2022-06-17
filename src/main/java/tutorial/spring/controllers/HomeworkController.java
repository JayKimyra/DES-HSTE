package tutorial.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tutorial.spring.dao.*;
import tutorial.spring.models.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/homework")
public class HomeworkController {
    private final UserDAO userDAO;
    private final TeacherStudentDAO teacherStudentDAO;
    private final ProblemDAO problemDAO;
    private final VariantDAO variantDAO;
    private final HomeworkDAO homeworkDAO;
    private final SolveDAO solveDAO;

    @Autowired
    public HomeworkController(UserDAO userDAO, TeacherStudentDAO teacherStudentDAO, ProblemDAO problemDAO, VariantDAO variantDAO, HomeworkDAO homeworkDAO, SolveDAO solveDAO) {
        this.userDAO = userDAO;
        this.teacherStudentDAO = teacherStudentDAO;
        this.problemDAO = problemDAO;
        this.variantDAO = variantDAO;
        this.homeworkDAO = homeworkDAO;
        this.solveDAO = solveDAO;
    }

    @PostMapping()
    public String create(@RequestParam String studentLogin, @RequestParam int variantId, HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null){
            return "redirect:/";//Пользователь не авторизован
        }
        User student = userDAO.findByField("login", studentLogin).get(0);
        List<TeacherStudent> teacherStudents = teacherStudentDAO.findByFields(new HashMap<String, Object>() {{
            put("teacher", user);
            put("student", student);
        }});
        if (teacherStudents.isEmpty()){
            return "redirect:/";//Нет связи учителя и ученика
        }
        Variant variant = variantDAO.findOne(variantId);
        Homework homework = new Homework(student,user,variant,false,new Timestamp(System.currentTimeMillis()));

        List<Homework> homeworkList = homeworkDAO.findByFields(new HashMap<String, Object>() {{
            put("student", student);
            put("variant", variant);
        }});
        if (!homeworkList.isEmpty()){
            return "redirect:/";//Данный вариант уже задан ученику(либо был задан)
        }
        List<Solve> solve = solveDAO.findByFields(new HashMap<String, Object>() {{
            put("user", student);
            put("variant", variant);
        }});

        System.out.println(solve);
        if (solve != null && !solve.isEmpty()){
            homework.setArchived(true);
            System.out.println("isArchived утсановлено в TRUE");
        }
        homeworkDAO.create(homework);



        return "redirect:/homework";
    }

    @GetMapping("/new/user/{login}")
    public String createPage1(HttpSession session, Model model,@PathVariable String login){
        model.addAttribute("pageTitle","Задать домашнюю работу");
        User user = (User) session.getAttribute("user");
        List<User> students = new ArrayList<>();
        for (TeacherStudent teacherStudent:
        teacherStudentDAO.findByField("teacher",user)) {
            students.add(teacherStudent.getStudent());
        }
        if (login == null) model.addAttribute("login", "");
        else model.addAttribute("login", login);
        model.addAttribute("students", students);
        return "homework/new";
    }
    @GetMapping("/new/variant/{variantId}")
    public String createPage2(HttpSession session, Model model,@PathVariable String variantId){

        model.addAttribute("pageTitle","Задать домашнюю работу");
        User user = (User) session.getAttribute("user");
        List<User> students = new ArrayList<>();
        for (TeacherStudent teacherStudent:
                teacherStudentDAO.findByField("teacher",user)) {
            students.add(teacherStudent.getStudent());
        }
        if (variantId == null) model.addAttribute("variantId", "");
        else model.addAttribute("variantId", variantId);
        model.addAttribute("students", students);
        return "homework/new";
    }
    @GetMapping("/new")
    public String createPage(HttpSession session, Model model){
        model.addAttribute("pageTitle","Задать домашнюю работу");
        User user = (User) session.getAttribute("user");
        List<User> students = new ArrayList<>();
        for (TeacherStudent teacherStudent:
                teacherStudentDAO.findByField("teacher",user)) {
            students.add(teacherStudent.getStudent());
        }
        model.addAttribute("login", "");
        model.addAttribute("variantId", "");
        model.addAttribute("students", students);
        return "homework/new";
    }



    @GetMapping()
    public String index(HttpSession session, Model model){
        model.addAttribute("pageTitle","Домашняя работа");
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";

        List<Homework> homeworkList = homeworkDAO.findByField("student" , user);

        model.addAttribute("homeworkList",homeworkList.stream().filter(x -> !x.isArchived()).collect(Collectors.toList()));
        return "homework/index";
    }
    @GetMapping("/archive")
    public String archive(HttpSession session, Model model){
        model.addAttribute("pageTitle","Архив");
        User user = (User) session.getAttribute("user");
        List<Homework> homeworkList = homeworkDAO.findByField("student" , user);
        model.addAttribute("homeworkList",homeworkList.stream().filter(Homework::isArchived).collect(Collectors.toList()));
        return "homework/archive";
    }

    @GetMapping("/archive/{variantId}")
    public String archiveVariant(HttpSession session, Model model, @PathVariable int variantId){
        model.addAttribute("pageTitle","Решенный вариант");
        User user = (User) session.getAttribute("user");
        Variant variant = variantDAO.findOne(variantId);
        List<Solve> solves = solveDAO.findByFields(new HashMap<String, Object>() {{
            put("user", user);
            put("variant", variant);
        }});
        model.addAttribute("solves",solves);
        return "homework/archive_variant";
    }

    @GetMapping("/teachers_archive")
    public String teachersArchive(HttpSession session, Model model){
        model.addAttribute("pageTitle","Архив");
        User user = (User) session.getAttribute("user");
        List<Homework> homeworkList = homeworkDAO.findByField("teacher" , user);

        model.addAttribute("homeworkList",homeworkList);
        return "homework/teachers_archive";
    }

    @GetMapping("/{studentLogin}/{variantId}")
    public String teacherVariantCheck(HttpSession session, Model model,@PathVariable String studentLogin,@PathVariable int variantId){
        User user = (User) session.getAttribute("user");

        Variant variant = variantDAO.findOne(variantId);
        User student = userDAO.findByField("login",studentLogin).get(0);
        //todo отказать в доступе если студент и учитель не связаны и если такого варианта задано не было(ничего не должно найтись)
        List<Solve> solves = solveDAO.findByFields(new HashMap<String,Object>(){{
            put("user", student);
            put("variant", variant);
        }});
        if (solves.size() == 0)
            return "homework/teachers_archive";

        model.addAttribute("solves",solves);
        return "homework/manual_check";
    }
    @PostMapping("/manual_check")
    public String manualCheck(HttpSession session, Model model,@RequestParam String studentLogin,@RequestParam int variantId,@RequestParam int problemId,@RequestParam boolean isRight){
        User user = (User) session.getAttribute("user");

        //todo отказать в доступе если студент и учитель не связаны и если такого варианта задано не было(ничего не должно найтись)

        Variant variant = variantDAO.findOne(variantId);
        Problem problem = problemDAO.findOne(problemId);
        User student = userDAO.findByField("login",studentLogin).get(0);
        Solve solve = solveDAO.findFirstByFields(new HashMap<String,Object>(){{
            put("user", student);
            put("variant", variant);
            put("problem", problem);
        }});
        solve.setCorrect(isRight);
        solveDAO.update(solve);
        return "redirect:" + "/homework/" + studentLogin + "/" + variantId;
    }



}
