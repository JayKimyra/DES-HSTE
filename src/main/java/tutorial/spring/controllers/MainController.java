package tutorial.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tutorial.spring.dao.ProblemDAO;
import tutorial.spring.dao.TeacherStudentDAO;
import tutorial.spring.dao.UserDAO;
import tutorial.spring.dao.VariantDAO;
import tutorial.spring.models.Problem;
import tutorial.spring.models.TeacherStudent;
import tutorial.spring.models.User;
import tutorial.spring.models.Variant;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MainController {
    private final VariantDAO variantDAO;
    private final ProblemDAO problemDAO;
    private final UserDAO userDAO;
    private final TeacherStudentDAO teacherStudentDAO;


    @Autowired
    public MainController(VariantDAO variantDAO, ProblemDAO problemDAO, UserDAO userDAO, TeacherStudentDAO teacherStudentDAO) {
        this.variantDAO = variantDAO;
        this.problemDAO = problemDAO;
        this.userDAO = userDAO;
        this.teacherStudentDAO = teacherStudentDAO;
    }
    @GetMapping()
    public String auth(){
        return "redirect:/init";
    }

    @GetMapping("/init")
    public String index(Model model){
        dbInit();
        return "redirect:/variants";
    }

    @GetMapping("/my")
    public String myProfile(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");

        List<User> students = teacherStudentDAO.findByFields(new HashMap<String, Object>() {{
            put("teacher", user);
            put("accepted", true);
        }}).stream().map(TeacherStudent::getStudent).collect(Collectors.toList());

        model.addAttribute("students", students);

        List<User> teachers = teacherStudentDAO.findByFields(new HashMap<String, Object>() {{
            put("student", user);
            put("accepted", true);
        }}).stream().map(TeacherStudent::getTeacher).collect(Collectors.toList());

        model.addAttribute("teachers", teachers);
        return "my";
    }




    public void dbInit(){
        userInit();
        problemInit();
        variantInit();
    }


    private void problemInit() {

        User user1 = userDAO.findOne(1);
        User user2 = userDAO.findOne(2);
        Problem problem1 = new Problem("Сколько будет 2+2*2?","6","Математика",user1, 1L,null);
        Problem problem2 = new Problem("Ближайшая к солнцу звезда?","Проксима Центавра","Физика",user1, 1L,null);
        Problem problem3 = new Problem("Чему равна сумма углов в треугольнике?(В градусах)","180","Математика",user2, 1L,"https://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/Penrose_triangle.svg/1200px-Penrose_triangle.svg.png");
        Problem problem4 = new Problem("Чему равна сумма углов в прямоугольнике?(В градусах)","360","Математика",user2, 1L,"https://itest.kz/uploads/images/8_%D0%9F%D1%80%D1%8F%D0%BC%D0%BE%D1%83%D0%B3%D0%BE%D0%BB%D1%8C%D0%BD%D0%B8%D0%BA,%20%D1%80%D0%BE%D0%BC%D0%B1%20%D0%B8%20%D0%BA%D0%B2%D0%B0%D0%B4%D1%80%D0%B0%D1%82_01-01.svg");
        problemDAO.create(problem1);
        problemDAO.create(problem2);
        problemDAO.create(problem3);
        problemDAO.create(problem4);
        for (int i = 0; i < 1; i++) {
            Problem problem = new Problem("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","Древняя Греция", i % 2 == 0 ? user1 : user2, 10L,"https://kubnews.ru/upload/iblock/bd2/bd25600b91e9e877cc46f83c94875947.jpg");
            problemDAO.create(problem);
        }
    }

    private void variantInit() {
        Set<Problem> problems = new HashSet<>();
        User user = userDAO.findOne(1);
        problems.addAll(problemDAO.findNRandomsByField(2,"type","Математика"));
        problems.addAll(problemDAO.findNRandomsByField(1,"type","Физика"));
        Variant variant = new Variant(problems, user);
        switch (new Random().nextInt(10)){
            case 0: variant.setImgUrl("https://russianblogs.com/images/669/c6e82c62b1f5bff2edbac3f98c33fae5.gif"); break;
            case 1: variant.setImgUrl("https://snipp.ru/uploads/images/donut.png"); break;
            case 2: variant.setImgUrl("https://99px.ru/sstorage/86/2020/04/image_860904200656497228095.gif"); break;
            case 3: variant.setImgUrl("https://i.gifer.com/origin/f8/f8f529070f97969af7c859b519653818.gif"); break;
            case 4: variant.setImgUrl("https://i.gifer.com/origin/e9/e9b78bdeb818d33c10d549e891259ce1_w200.gif"); break;
            case 5: variant.setImgUrl("https://bestanimations.com/media/homer/1246167591Homer-06-june.gif"); break;
            case 6: variant.setImgUrl("https://i.gifer.com/tDS.gif"); break;
            case 7: variant.setImgUrl("https://i.gifer.com/origin/9d/9d23569e3ca06bd331a123f8052f1d6c.gif"); break;
            case 8: variant.setImgUrl("https://i.gifer.com/origin/9a/9ac08e6b84e39c5dc68224c3c37a9514.gif"); break;
            case 9: variant.setImgUrl("https://i.gifer.com/OPge.gif"); break;
            case 10: variant.setImgUrl("https://assets.htmlacademy.ru/img/blog/113/gif/gif.gif"); break;

        }

        variantDAO.create(variant);
    }

    public void userInit(){
        User user1 = new User("JayKimyra","123");
        User user2 = new User("GENBY_8","123");
        User user3 = new User("ED","123");


        userDAO.create(user1);
        userDAO.create(user2);
        userDAO.create(user3);

        TeacherStudent teacherStudent1 = new TeacherStudent(user1,user2,true, user1);
        TeacherStudent teacherStudent2 = new TeacherStudent(user1,user3,true, user2);
        teacherStudentDAO.create(teacherStudent1);
        teacherStudentDAO.create(teacherStudent2);

    }
}
