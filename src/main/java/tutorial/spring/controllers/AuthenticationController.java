package tutorial.spring.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tutorial.spring.dao.UserDAO;
import tutorial.spring.models.User;

import javax.servlet.http.HttpSession;

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
    public String auth(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/";
        }
        return "auth/index";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
}
