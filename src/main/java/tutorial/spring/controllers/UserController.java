package tutorial.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tutorial.spring.dao.TeacherStudentDAO;
import tutorial.spring.dao.UserDAO;
import tutorial.spring.models.TeacherStudent;
import tutorial.spring.models.User;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserDAO userDAO;
    private final TeacherStudentDAO teacherStudentDAO;

    @Autowired
    public UserController(UserDAO userDAO, TeacherStudentDAO teacherStudentDAO) {
        this.userDAO = userDAO;
        this.teacherStudentDAO = teacherStudentDAO;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("user", userDAO.findOne(id));
        return "users/show";
    }
    @GetMapping("/request")
    public String showRequest(@ModelAttribute("user") User user){
        return "users/request";
    }

    @GetMapping("/requests")
    public String getRequests(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        List<User> students = teacherStudentDAO.findByFields(new HashMap<String, Object>() {{
            put("teacher", user);
            put("accepted", false);
        }}).stream().filter(x -> !x.getSender().equals(user)).map(TeacherStudent::getStudent).collect(Collectors.toList());

        model.addAttribute("students", students);

        List<User> teachers = teacherStudentDAO.findByFields(new HashMap<String, Object>() {{
            put("student", user);
            put("accepted", false);
        }}).stream().filter(x -> !x.getSender().equals(user)).map(TeacherStudent::getTeacher).collect(Collectors.toList());

        model.addAttribute("teachers", teachers);
        return "users/requests";
    }
    @PostMapping("/addTeacher")
    public String requestTeacher(@RequestParam String login,HttpSession session){
        User user = (User) session.getAttribute("user");
        User teacher = userDAO.findByField("login",login).get(0);

        List<TeacherStudent> teacherStudents = teacherStudentDAO.findByFields(new HashMap<String, Object>() {{
            put("teacher", teacher);
            put("student", user);
        }});
        if (!teacherStudents.isEmpty()){
            System.out.println("Запрос уже существует");
            return "redirect:/my";
        }


        TeacherStudent teacherStudent = new TeacherStudent(teacher, user,false, user);
        teacherStudentDAO.create(teacherStudent);
        return "redirect:/my";
    }
    @PostMapping("/addStudent")
    public String requestStudent(@RequestParam String login,HttpSession session){
        User user = (User) session.getAttribute("user");
        User student = userDAO.findByField("login",login).get(0);

        List<TeacherStudent> teacherStudents = teacherStudentDAO.findByFields(new HashMap<String, Object>() {{
            put("teacher", user);
            put("student", student);
        }});
        if (!teacherStudents.isEmpty()){
            System.out.println("Запрос уже существует");
            return "redirect:/my";
        }
        TeacherStudent teacherStudent = new TeacherStudent(user, student,false, user);
        teacherStudentDAO.create(teacherStudent);
        return "redirect:/my";
    }


    @PostMapping("/acceptTeacher/{id}")
    public String acceptTeacher(@PathVariable long id,HttpSession session){
        User user = (User) session.getAttribute("user");
        User teacher = userDAO.findOne(id);


        List<TeacherStudent> teacherStudents = teacherStudentDAO.findByFields(new HashMap<String, Object>() {{
            put("teacher", teacher);
            put("student", user);
        }});

        if (teacherStudents == null || teacherStudents.isEmpty() || teacherStudents.get(0).isAccepted()){
            System.out.println("Такого запроса не существует");
            return "redirect:/users/requests";
        }

        TeacherStudent teacherStudent = teacherStudents.get(0);
        teacherStudent.setAccepted(true);
        teacherStudentDAO.update(teacherStudent);
        return "redirect:/users/requests";
    }

    @PostMapping("/acceptStudent/{id}")
    public String acceptStudent(@PathVariable long id,HttpSession session){
        User user = (User) session.getAttribute("user");
        User student = userDAO.findOne(id);

        List<TeacherStudent> teacherStudents = teacherStudentDAO.findByFields(new HashMap<String, Object>() {{
            put("teacher", user);
            put("student", student);
        }});

        if (teacherStudents == null || teacherStudents.isEmpty() || teacherStudents.get(0).isAccepted()){
            System.out.println("Такого запроса не существует");
            return "redirect:/users/requests";
        }
        System.out.println(teacherStudents.size());
        TeacherStudent teacherStudent = teacherStudents.get(0);
        teacherStudent.setAccepted(true);
        teacherStudentDAO.update(teacherStudent);
        return "redirect:/users/requests";
    }





}
