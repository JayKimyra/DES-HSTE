package tutorial.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tutorial.spring.dao.ProblemDAO;
import tutorial.spring.dao.UserDAO;
import tutorial.spring.models.Problem;
import tutorial.spring.models.User;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/problems")
public class ProblemController {
    private final ProblemDAO problemDAO;
    private final UserDAO userDAO;

    @Autowired
    public ProblemController(ProblemDAO problemDAO, UserDAO userDAO) {
        this.problemDAO = problemDAO;
        this.userDAO = userDAO;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("problems", problemDAO.findAllWithLimit(20,0));

        return "problems/index";
    }
    @GetMapping("/user/{id}")
    public String problemsUser(Model model, @PathVariable int id){
        List<Problem> problems = problemDAO.findByField("author",userDAO.findOne(id));
        model.addAttribute("problems", problems);
        return "problems/index";
    }


    @GetMapping("/get")
    public String index2(Model model,@RequestParam(defaultValue= "5") int limit, @RequestParam(defaultValue= "0") int offset){
        model.addAttribute("problems", problemDAO.findAllWithLimit(limit,offset));
        return "problems/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("problem", problemDAO.findOne(id));
        return "problems/show";
    }
    @GetMapping("/new")
    public String newProblem(@ModelAttribute("problem") Problem problem){

        return "problems/new";
    }
    @PostMapping()
    public String create(@ModelAttribute("problem") Problem problem,HttpSession session){
        User user = getUserFromSession(session);
        if (user == null){
            return "redirect:/problems";
        }
        problem.setAuthor(user);
        problem.setMaxPoints(1L);
        if (problem.getImgUrl().replace(" ","").isEmpty()){
            problem.setImgUrl(null);
        }
        problemDAO.create(problem);
        return "redirect:/problems";
    }

    private User getUserFromSession(HttpSession session) {
        Object sessionUser = session.getAttribute("user");
        if (sessionUser == null){
            System.out.println("Не авторизирован");
            return null;
        }
        System.out.println(sessionUser);
        return (User) sessionUser;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,@PathVariable("id") int id){
        model.addAttribute("problem",problemDAO.findOne(id));
        return "problems/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("problem") Problem problem,@PathVariable("id") int id, HttpSession session){
        User user = getUserFromSession(session);
        if (user == null){
            return "redirect:/problems";
        }
        Problem problemToUpdate = problemDAO.findOne(id);
        System.out.println("Автор данной записи "+problemToUpdate.getAuthor());
        if (!user.equals(problemToUpdate.getAuthor())){
            System.out.println("У вас нет прав менять данную запись");
            return "redirect:/problems";
        }
        System.out.println(problem.getText());
        problem.setId((long) id);
        problemToUpdate.setText(problem.getText());
        problemToUpdate.setAnswer(problem.getAnswer());
        problemToUpdate.setType(problem.getType());
        problemDAO.update(problemToUpdate);
        return "redirect:/problems";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){

        problemDAO.deleteById(id);
        return "redirect:/problems";
    }

}
