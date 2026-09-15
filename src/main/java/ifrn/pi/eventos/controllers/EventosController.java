package ifrn.pi.eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ifrn.pi.eventos.models.Convidado;
import ifrn.pi.eventos.models.Evento;
import ifrn.pi.eventos.repositories.ConvidadoRepository;
import ifrn.pi.eventos.repositories.EventoRepository;

@Controller
@RequestMapping("/eventos")
public class EventosController {

    @Autowired
    private EventoRepository er;

    @Autowired
    private ConvidadoRepository cr;

    @GetMapping("/form")
    public String form() {
        return "formEvento";
    }

    @PostMapping("/form")
    public String salvar(Evento evento) {
        er.save(evento);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public ModelAndView detalhesEvento(@PathVariable("id") Long id) {

        Evento evento = er.findById(id).orElse(null);

        ModelAndView mv = new ModelAndView("detalhes");
        mv.addObject("evento", evento);

        return mv;
    }

    @PostMapping("/{id}")
    public String salvarConvidado(@PathVariable("id") Long id, Convidado convidado) {

        Evento evento = er.findById(id).orElse(null);

        convidado.setEvento(evento);
        cr.save(convidado);

        return "redirect:/eventos/" + id;
    }
}
