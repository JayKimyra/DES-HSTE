package tutorial.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tutorial.spring.dao.*;
import tutorial.spring.models.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/variants")
public class VariantController {
    private final VariantDAO variantDAO;
    private final ProblemDAO problemDAO;
    private final UserDAO userDAO;
    private final SolveDAO solveDAO;
    private final HomeworkDAO homeworkDAO;

    @Autowired
    public VariantController(VariantDAO variantDAO, ProblemDAO problemDAO, UserDAO userDAO, SolveDAO solveDAO, HomeworkDAO homeworkDAO) {
        this.variantDAO = variantDAO;
        this.problemDAO = problemDAO;
        this.userDAO = userDAO;
        this.solveDAO = solveDAO;
        this.homeworkDAO = homeworkDAO;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("variants", variantDAO.findAll());
        return "variants/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        Variant variant = variantDAO.findOne(id);
        model.addAttribute("variant", variant);
        return "variants/show";
    }
    @GetMapping("/new")
    public String newVariant(@ModelAttribute("variant") Variant variant, Model model){
        model.addAttribute("problems", problemDAO.findAll());
        return "variants/new";
    }

    @GetMapping("/user/{id}")
    public String variantsUser(Model model, @PathVariable int id){
        List<Variant> variants = variantDAO.findByField("owner",userDAO.findOne(id));
        model.addAttribute("variants", variants);
        return "variants/index";
    }

    @PostMapping()
    public String create(Model model, @RequestParam String[] type, @RequestParam Integer[] count, @RequestParam String imgSource, HttpSession session){
        User user = (User) session.getAttribute("user");
        Set<Problem> list = new HashSet<>();
        for (int i = 0;i < type.length;i++){
            list.addAll(problemDAO.findNRandomsByField(count[i],"type",type[i]));
            System.out.println(type[i]+" "+count[i]);
        }
        list.forEach(System.out::println);
        Variant variant = new Variant();
        variant.setProblems(list);
        variant.setOwner(user);
        if (!imgSource.isEmpty()) variant.setImgUrl(imgSource);
        variantDAO.create(variant);
        return "redirect:/variants";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,@PathVariable("id") int id){
        model.addAttribute("variant",variantDAO.findOne(id));
        return "variants/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("variant") Variant variant,@PathVariable("id") int id){
        variant.setId((long) id);
        variantDAO.update(variant);
        return "redirect:/variants";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){

        variantDAO.deleteById(id);
        return "redirect:/variants";
    }

    @PostMapping("/check")
    public String checkForm(@RequestParam Map<String,String> allParams, Model model,HttpSession session) {
        User user = (User) session.getAttribute("user");

        Variant variant = variantDAO.findOne(Long.parseLong(allParams.get("variant_id")));
        List<Problem> problems = variant.getProblems().stream().sorted((o1, o2) -> (int) (o1.getId()-o2.getId())).collect(Collectors.toList());
        List<Solve> solves = new ArrayList<>();
        for(Problem problem : problems){
            String answer = allParams.get(problem.getId().toString());
            boolean isCorrect = answer.toLowerCase(Locale.ROOT).equals(problem.getAnswer().toLowerCase(Locale.ROOT));

            Solve solve = new Solve(user, variant, problem, answer,isCorrect, isCorrect? problem.getMaxPoints() : 0);
            solves.add(solve);
            if (user != null){
                List<Solve> solvesFromDb = solveDAO.findByFields(new HashMap<String, Object>() {{
                    put("variant", variant);
                    put("problem", problem);
                    put("user", user);
                }});
                System.out.println("Найденные варианты:");
                if (solvesFromDb != null && solvesFromDb.isEmpty()){
                    solveDAO.create(solve);
                }
                else{
                    solvesFromDb.forEach(System.out::println);
                    System.out.println("Ответ не сохранился, уже существует запись!");
                }
            }

        }
        if (user != null){
            List<Homework> homeworkList = homeworkDAO.findByFields(new HashMap<String, Object>() {{
                put("variant", variant);
                put("student", user);
            }});
            if (homeworkList != null && !homeworkList.isEmpty()){
                Homework homework = homeworkList.get(0);
                homework.setArchived(true);
                homeworkDAO.update(homework);
                System.out.println("Данный вариант был домашней работой! Сохранен результат для домашней работы");
            }
        }
        model.addAttribute("solves", solves);
        return "/variants/check";
    }


}
