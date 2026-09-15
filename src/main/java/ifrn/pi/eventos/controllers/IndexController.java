package ifrn.pi.eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import ifrn.pi.eventos.repositories.EventoRepository;

@Controller
public class IndexController {

    @Autowired
    private EventoRepository er;

    @GetMapping("/")
    public ModelAndView index() {

        ModelAndView mv = new ModelAndView("home");
        mv.addObject("eventos", er.findAll());

        return mv;
    }
}