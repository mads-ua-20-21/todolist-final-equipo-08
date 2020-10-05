package madstodolist.controller;

import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/about")
    public String loginForm(Model model, HttpSession session) {

        if(session.getAttribute("idUsuarioLogeado") != null){
            Usuario usuario = usuarioService.findById((Long) session.getAttribute("idUsuarioLogeado"));
            model.addAttribute("usuario", usuario);
        }

        return "about";
    }

}