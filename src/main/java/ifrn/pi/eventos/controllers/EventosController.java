
package ifrn.pi.eventos.controllers;

import java.util.List;
import java.util.Optional;
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
    public String form(Evento evento) {
        return "formEvento";
    }

    @PostMapping
    public String salvar(Evento evento) {
        er.save(evento);
        return "redirect:/eventos";
    }

    @GetMapping
    public ModelAndView listar() {
        List<Evento> eventos = er.findAll();
        ModelAndView mv = new ModelAndView("lista");
        mv.addObject("eventos", eventos);
        return mv;
    }

    @GetMapping("/{id}")
    public ModelAndView detalhesEvento(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("detalhes");
        Optional<Evento> opt = er.findById(id);
        
        if (opt.isEmpty()) {
            mv.setViewName("redirect:/eventos");
            return mv;
        }
        
        Evento evento = opt.get();
        mv.addObject("evento", evento);
        
        List<Convidado> convidados = cr.findByEvento(evento);
        mv.addObject("convidados", convidados);
        
        return mv;
    }

    @PostMapping("/{id}")
    public String salvarConvidado(@PathVariable("id") Long id, Convidado convidado) {
        Optional<Evento> opt = er.findById(id);
        
        if (!opt.isEmpty()) {
            Evento evento = opt.get();
            convidado.setEvento(evento);
            cr.save(convidado);
        }
        
        return "redirect:/eventos/" + id;
    }

    @GetMapping("/{id}/selecionar")
    public ModelAndView selecionarEvento(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView();
        Optional<Evento> opt = er.findById(id);
        
        if (opt.isEmpty()) {
            mv.setViewName("redirect:/eventos");
            return mv;
        }
        
        Evento evento = opt.get();
        mv.setViewName("formEvento");
        mv.addObject("evento", evento);
        return mv;
    }

    @GetMapping("/{id}/remover")
    public String apagarEvento(@PathVariable Long id) {
        Optional<Evento> opt = er.findById(id);
        
        if (!opt.isEmpty()) {
            Evento evento = opt.get();
            List<Convidado> convidados = cr.findByEvento(evento);
            cr.deleteAll(convidados);
            er.delete(evento);
        }
        return "redirect:/eventos";
    }

    @GetMapping("/{idEvento}/convidados/{idConvidado}/remover")
    public String apagarConvidado(@PathVariable Long idEvento, @PathVariable Long idConvidado) {
        Optional<Convidado> opt = cr.findById(idConvidado);
        
        if (!opt.isEmpty()) {
            Convidado convidado = opt.get();
            cr.delete(convidado);
        }
        return "redirect:/eventos/" + idEvento;
    }
}
