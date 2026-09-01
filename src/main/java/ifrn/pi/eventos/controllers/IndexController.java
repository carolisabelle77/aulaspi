package ifrn.pi.eventos.controllers;

import ifrn.pi.eventos.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @Autowired
    private EventoRepository er;

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("home"); // Abre a sua página home.html
        mv.addObject("eventos", er.findAll()); // Busca os eventos do MySQL Workbench
        return mv;
    }
}
