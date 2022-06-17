package tutorial.spring.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tutorial.spring.dao.UserDAO;
import tutorial.spring.models.User;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class AuthenticationController {
    private final UserDAO userDAO;

    @Autowired
    public AuthenticationController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @PostMapping("/auth")
    public String auth(@RequestParam String login, @RequestParam String password, HttpSession session){
        User user = userDAO.findByField("login",login).get(0);
        if (user.getPassword().equals(password)){
            System.out.println("Пароль подошел");
            session.setAttribute("user", user);
            return "redirect:/";
        }
        else {
            System.out.println("Пароль неверный");
            session.setAttribute("user", null);
            return "redirect:/";
        }
    }

    @GetMapping("/auth")
    public String auth(HttpSession session, Model model){
        model.addAttribute("pageTitle","Авторизация");
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/";
        }


        return "auth/index";
    }


    @GetMapping("/registration")
    public String registration(HttpSession session, Model model){
        model.addAttribute("pageTitle","Регистрация");
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/";
        }
        return "users/new";
    }
    @PostMapping("/registration")
    public String registration(@RequestParam String login, @RequestParam String password, @RequestParam String info, @RequestParam String imgUrl, HttpSession session){
        List<User> users = userDAO.findByField("login",login);
        if (!users.isEmpty()){
            System.out.println("Данный логин уже занят");
            session.setAttribute("loginError", "Данный логин уже занят");
            return "redirect:/registration";
        }
        session.removeAttribute("loginError");
        User newUser = new User(login,password);
        newUser.setImgUrl(imgUrl);
        newUser.setInfo(info);
        userDAO.create(newUser);
        return "redirect:/auth";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
}
