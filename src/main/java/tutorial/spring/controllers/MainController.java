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
        return "redirect:/variants";
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
        User user3 = userDAO.findOne(3);
        /*Problem problem1 = new Problem("Сколько будет 2+2*2?","6","Математика",user1, 1L,null);
        Problem problem2 = new Problem("Ближайшая к солнцу звезда?","Проксима Центавра","Физика",user1, 1L,null);
        Problem problem3 = new Problem("Чему равна сумма углов в треугольнике?(В градусах)","180","Математика",user2, 1L,"https://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/Penrose_triangle.svg/1200px-Penrose_triangle.svg.png");
        Problem problem4 = new Problem("Чему равна сумма углов в прямоугольнике?(В градусах)","360","Математика",user2, 1L,"https://itest.kz/uploads/images/8_%D0%9F%D1%80%D1%8F%D0%BC%D0%BE%D1%83%D0%B3%D0%BE%D0%BB%D1%8C%D0%BD%D0%B8%D0%BA,%20%D1%80%D0%BE%D0%BC%D0%B1%20%D0%B8%20%D0%BA%D0%B2%D0%B0%D0%B4%D1%80%D0%B0%D1%82_01-01.svg");*/

        Problem problem1 = new Problem("Задание по математика за 5 класс","На изготовление одного пододеяльника требуется 4 м 40 см полотна, а на одну наволочку — 90 см полотна. Всего было израсходовано 80 м полотна.\n" +
                "Пододеяльников сшили 10 штук. Сколько сшили наволочек?", "Решение отсутствует","40","Математика",user1, 1L,null);
        Problem problem2 = new Problem("Математика ЕГЭ","На олимпиаде по русскому языку 250 участников разместили в трёх аудиториях. В первых двух удалось разместить по 120 человек, оставшихся перевели в запасную аудиторию в другом корпусе. Найдите вероятность того, что случайно выбранный участник писал олимпиаду в запасной аудитории.","Всего в запасную аудиторию направили 250−120 −120=10 человек. Поэтому вероятность того, что случайно выбранный участник писал олимпиаду в запасной аудитории, равна 10:250=0,04.","0,04","Математика",user1, 1L,null);

        Problem problem3 = new Problem();
        problem3.setTitle("Анализ графика функций");
        problem3.setImgUrl("https://img-fotki.yandex.ru/get/42925/167134308.23/0_14cd5d_7fe10b0c_orig.jpg");
        problem3.setText("Найдите точку экстремума изображенного на рисунке графика");
        problem3.setAnswer("0");
        problem3.setSolution("Из графика явно видно, что данная функция является функцией вида y=|x| поэтому точка экстремума единственная и находится в точке 0.");
        problem3.setMaxPoints(1L);
        problem3.setAuthor(user2);
        problem3.setType("Математика");


        Problem problem4 = new Problem();
        problem4.setTitle("Рисование");
        problem4.setImgUrl("https://i2.wp.com/pro-kletochki.ru/wp-content/uploads/2018/08/%D0%9D%D1%8F%D1%88%D0%BD%D1%8B%D0%B5-%D0%BA%D0%BE%D1%82%D0%B8%D0%BA%D0%B8-%D0%B4%D0%BB%D1%8F-%D1%81%D1%80%D0%B8%D1%81%D0%BE%D0%B2%D0%BA%D0%B818.png?resize=640%2C360&ssl=1");
        problem4.setText("Нарисуйте подобного котика");
        problem4.setAnswer("Ответа нет");
        problem4.setSolution("Данное задание оценивается индивидуально преподавателем");
        problem4.setMaxPoints(1L);
        problem4.setAuthor(user3);
        problem4.setType("Искусство");
        problem4.setDrawable(true);

        problemDAO.create(problem3);
        problemDAO.create(problem2);
        problemDAO.create(problem1);
        problemDAO.create(problem4);
        /*for (int i = 0; i < 1; i++) {
            Problem problem = new Problem("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","Древняя Греция", i % 2 == 0 ? user1 : user2, 10L,"https://kubnews.ru/upload/iblock/bd2/bd25600b91e9e877cc46f83c94875947.jpg");
            problemDAO.create(problem);
        }*/
    }

    private void variantInit() {
        Set<Problem> problems = new HashSet<>();
        User user = userDAO.findOne(1);
        problems.addAll(problemDAO.findNRandomsByField(2,"type","Математика"));
        problems.addAll(problemDAO.findNRandomsByField(1,"type","Физика"));
        problems.addAll(problemDAO.findNRandomsByField(1,"type","Искусство"));
        Variant variant = new Variant(problems, user);
        switch (new Random().nextInt(11)){
            case 0: variant.setImgUrl("https://cdn-icons-png.flaticon.com/128/4207/4207253.png"); break;
            case 1: variant.setImgUrl("https://snipp.ru/uploads/images/donut.png"); break;
            case 2: variant.setImgUrl("https://99px.ru/sstorage/86/2020/04/image_860904200656497228095.gif"); break;
            case 3: variant.setImgUrl("https://cdn-icons-png.flaticon.com/128/4345/4345547.png"); break;
            case 4: variant.setImgUrl("https://i.gifer.com/origin/e9/e9b78bdeb818d33c10d549e891259ce1_w200.gif"); break;
            case 5: variant.setImgUrl("https://cdn-icons-png.flaticon.com/128/5769/5769278.png"); break;
            case 6: variant.setImgUrl("https://cdn-icons-png.flaticon.com/128/6600/6600176.png"); break;
            case 7: variant.setImgUrl("https://i.gifer.com/origin/9d/9d23569e3ca06bd331a123f8052f1d6c.gif"); break;
            case 8: variant.setImgUrl("https://cdn-icons-png.flaticon.com/128/91/91591.png"); break;
            case 9: variant.setImgUrl(null); break;
            case 10: variant.setImgUrl(null); break;

        }

        variantDAO.create(variant);
    }

    public void userInit(){
        User user1 = new User("JayKimyra","123");
        User user2 = new User("GENBY_8","123");
        User user3 = new User("ED","123");
        user3.setInfo("В ближайшее время планирую подготовиться к экзаменам для поступления в университет и заняться личным саморазвитием для успешного карьерного роста.\n" +
                "Планирую сдавать математику, русский, информатику и физику, поэтому ищу репетиторов по всем данным предметам.\n"+
                "Я очень ответственный: никогда не пропускаю уроки и всегда делаю домашнее задание.\n");
        user1.setImgUrl("https://hotpotmedia.s3.us-east-2.amazonaws.com/8-XWUIYcwHzPGeCtl.png");
        user2.setImgUrl("https://img.freepik.com/free-vector/cartoon-rat-holding-cheese_14588-416.jpg?w=2000");
        user3.setImgUrl("https://thumbs.dreamstime.com/b/vector-pixel-art-pig-pet-vector-pixel-art-pig-pet-isolated-cartoon-138802178.jpg");
        userDAO.create(user1);
        userDAO.create(user2);
        userDAO.create(user3);

        TeacherStudent teacherStudent1 = new TeacherStudent(user1,user2,true, user1);
        TeacherStudent teacherStudent2 = new TeacherStudent(user1,user3,true, user2);
        teacherStudentDAO.create(teacherStudent1);
        teacherStudentDAO.create(teacherStudent2);

    }

}
